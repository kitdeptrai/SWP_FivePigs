# Giải thích từng dòng code liên quan đến admin (AdminAuthorizationFilter)

Tài liệu này giải thích **từng dòng** trong file `src/main/java/com/fivepigs/app/web/admin/AdminAuthorizationFilter.java`.

---

## 1. Package và import

- `package com.fivepigs.app.web.admin;`  
  Khai báo package của class, đặt class trong nhóm chức năng admin.

- `import com.fivepigs.app.model.User;`  
  Dùng model `User` để lấy thông tin user từ session.

- `import jakarta.servlet.Filter;`  
  Interface `Filter` của Servlet API, dùng để chặn/cho phép request.

- `import jakarta.servlet.FilterChain;`  
  Dùng để tiếp tục chuỗi filter nếu hợp lệ.

- `import jakarta.servlet.ServletException;`  
  Exception chuẩn cho servlet.

- `import jakarta.servlet.ServletRequest;`  
  Định nghĩa request ở mức tổng quát.

- `import jakarta.servlet.ServletResponse;`  
  Định nghĩa response ở mức tổng quát.

- `import jakarta.servlet.annotation.WebFilter;`  
  Annotation để khai báo filter bằng URL pattern.

- `import jakarta.servlet.http.HttpServletRequest;`  
  Kiểu request HTTP, cần để lấy session và URL.

- `import jakarta.servlet.http.HttpServletResponse;`  
  Kiểu response HTTP, cần để redirect.

- `import jakarta.servlet.http.HttpSession;`  
  Dùng để lấy session của user.

- `import java.io.IOException;`  
  Exception chuẩn cho I/O.

- `import java.net.URLEncoder;`  
  Dùng để encode URL khi redirect về login.

- `import java.nio.charset.StandardCharsets;`  
  Dùng charset UTF-8 khi encode URL.

---

## 2. Khai báo filter

- `@WebFilter(urlPatterns = {"/admin/*"})`  
  Áp dụng filter cho toàn bộ đường dẫn bắt đầu bằng `/admin/`.

- `public class AdminAuthorizationFilter implements Filter {`  
  Khai báo class filter để phân quyền admin.

---

## 3. Phương thức doFilter

- `@Override`  
  Ghi đè method của interface `Filter`.

- `public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)`  
  Hàm chạy trước khi request vào servlet admin.

- `if (!(request instanceof HttpServletRequest httpRequest)`  
  Kiểm tra request có phải HTTP hay không.

- `|| !(response instanceof HttpServletResponse httpResponse)) {`  
  Kiểm tra response có phải HTTP hay không.

- `chain.doFilter(request, response);`  
  Nếu không phải HTTP thì cho đi tiếp.

- `return;`  
  Thoát sớm.

- `HttpSession session = httpRequest.getSession(false);`  
  Lấy session hiện tại, không tạo mới.

- `User user = session == null ? null : (User) session.getAttribute("user");`  
  Lấy user từ session nếu có.

---

## 4. Trường hợp chưa đăng nhập

- `if (user == null) {`  
  Nếu không có user trong session nghĩa là chưa đăng nhập.

- `String uri = httpRequest.getRequestURI();`  
  Lấy URI đầy đủ của request.

- `String contextPath = httpRequest.getContextPath();`  
  Lấy context path của project.

- `String target = uri.startsWith(contextPath) ? uri.substring(contextPath.length()) : uri;`  
  Cắt bỏ context path để lấy đường dẫn tương đối.

- `String query = httpRequest.getQueryString();`  
  Lấy query string nếu có.

- `if (query != null && !query.isBlank()) {`  
  Nếu có query, nối vào target.

- `target = target + "?" + query;`  
  Ghép query vào URL.

- `httpResponse.sendRedirect(contextPath + "/login?redirect="`  
  Redirect về trang login, kèm tham số redirect.

- `+ URLEncoder.encode(target, StandardCharsets.UTF_8));`  
  Encode URL redirect để an toàn.

- `return;`  
  Dừng filter để không cho vào admin.

---

## 5. Trường hợp không phải admin

- `if (!isAdmin(user, session)) {`  
  Nếu user không phải admin.

- `String roleName = resolveRoleName(user, session);`  
  Lấy role name từ user/session.

- `String dashboardPath = resolveDashboardPath(roleName);`  
  Chọn dashboard phù hợp với role.

- `httpResponse.sendRedirect(httpRequest.getContextPath() + dashboardPath);`  
  Redirect về dashboard tương ứng role.

- `return;`  
  Dừng filter, không cho vào admin.

---

## 6. Trường hợp là admin

- `chain.doFilter(request, response);`  
  Cho phép request tiếp tục vào servlet admin.

---

## 7. Hàm isAdmin

- `private boolean isAdmin(User user, HttpSession session) {`  
  Hàm kiểm tra user có phải admin không.

- `String roleName = resolveRoleName(user, session);`  
  Lấy role name.

- `return roleName != null && "admin".equalsIgnoreCase(roleName.trim());`  
  So sánh roleName với "admin" (không phân biệt hoa thường).

---

## 8. Hàm resolveRoleName

- `private String resolveRoleName(User user, HttpSession session) {`  
  Lấy role name từ `User` hoặc session.

- `if (user == null) { return null; }`  
  Không có user thì trả null.

- `String roleName = user.getRoleName();`  
  Ưu tiên lấy roleName từ user trong session.

- `if (roleName == null && session != null) { ... }`  
  Nếu user chưa có roleName thì fallback lấy từ session.

- `Object roleInSession = session.getAttribute("roleName");`  
  Lấy roleName lưu trong session.

- `if (roleInSession instanceof String) { roleName = (String) roleInSession; }`  
  Ép kiểu và gán lại.

- `return roleName;`  
  Trả về roleName cuối cùng.

---

## 9. Hàm resolveDashboardPath

- `private String resolveDashboardPath(String roleName) {`  
  Chọn dashboard phù hợp theo role.

- `if (roleName == null) { return "/customer_dashboard"; }`  
  Không có role thì về dashboard customer.

- `String role = roleName.trim().toLowerCase();`  
  Chuẩn hóa role để so sánh.

- `return switch (role) { ... };`  
  Map role → dashboard:
  - `admin` → `/admin/dashboard`
  - `approval` → `/approval_dashboard`
  - `reviewer` → `/reviewer_dashboard`
  - `vendor` → `/vendor/dashboard`
  - `customer/user` → `/customer_dashboard`
  - default → `/customer_dashboard`

---

Nếu bạn muốn mình giải thích **từng dòng** của toàn bộ các servlet admin (không chỉ filter) thì nói mình, mình sẽ tạo thêm một tài liệu tổng hợp theo từng file admin.
