# Flow 01: Admin Dashboard

> Mục tiêu: mô tả luồng Dashboard Admin theo tuyến Frontend → Backend → DAO và thêm giải thích cho từng ý để dễ đối chiếu với code thực tế.

## Frontend (JSP)
- File: `src/main/webapp/WEB-INF/views/admin/dashboard.jsp`
  - Giải thích: Đây là view chính của màn Dashboard admin, nhận dữ liệu qua `request attributes` do servlet set.

- Form chính:
  - `POST ${contextPath}/admin/dashboard`
    - Giải thích: Submit form theo phương thức POST để cập nhật cấu hình (commission), tránh đưa dữ liệu chỉnh sửa lên URL.
  - Input: `commissionPercent`
    - Giải thích: Trường nhập phần trăm hoa hồng hệ thống (đơn vị %), backend sẽ validate trước khi ghi DB.

- Hiển thị dữ liệu từ backend:
  - `totalRevenue`, `totalUsers`, `newUsers`, `totalApps`, `totalDownloads`
    - Giải thích: Bộ KPI tổng quan hiển thị dạng card/thống kê nhanh.
  - `revenueByMonth`, `topAppsBestSeller`
    - Giải thích: Dữ liệu cho biểu đồ doanh thu theo tháng và danh sách app bán chạy.

## Backend (Servlet)
- File: `src/main/java/com/fivepigs/app/web/admin/AdminDashboardServlet.java`
  - Giải thích: Servlet điều phối request dashboard; `doGet` để xem dữ liệu, `doPost` để cập nhật commission.

- `doGet()`:
  1. Tạo `AdminDao`
     - Giải thích: Chuẩn bị lớp truy cập dữ liệu để lấy thống kê.
  2. Gọi các hàm lấy KPI
     - Giải thích: Truy vấn DB lấy số liệu tổng hợp cần cho dashboard.
  3. Tính phần trăm bar chart theo tháng
     - Giải thích: Chuẩn hóa dữ liệu doanh thu về tỉ lệ để render biểu đồ trực quan.
  4. `setAttribute(...)`
     - Giải thích: Đưa dữ liệu vào request scope để JSP đọc và hiển thị.
  5. `forward` về `dashboard.jsp`
     - Giải thích: Chuyển tiếp request đến view (server-side render), giữ nguyên dữ liệu attributes.

- `doPost()`:
  1. Lấy `commissionPercent`
     - Giải thích: Đọc giá trị người dùng nhập từ form.
  2. Validate rỗng/số/range 0..20
     - Giải thích: Chặn input sai định dạng hoặc vượt giới hạn nghiệp vụ.
  3. Gọi `adminDao.setCommissionPercent(percent, userId)`
     - Giải thích: Nếu hợp lệ thì cập nhật commission trong DB và ghi nhận người chỉnh sửa.
  4. Redirect success/error
     - Giải thích: Điều hướng lại dashboard kèm trạng thái thành công/thất bại để hiển thị thông báo.

## DAO (SQL)
- File: `src/main/java/com/fivepigs/app/dao/AdminDao.java`
  - Giải thích: Nơi chứa câu lệnh SQL phục vụ dashboard admin.

- Hàm chính dùng trong flow:
  - `getCommissionPercent()`
    - Giải thích: Lấy mức commission hiện tại để hiển thị lên form.
  - `setCommissionPercent(...)`
    - Giải thích: Cập nhật mức commission mới vào DB.
  - `getTotalRevenue()`
    - Giải thích: Lấy tổng doanh thu toàn hệ thống.
  - `getTotalProducts()`
    - Giải thích: Lấy tổng số sản phẩm/app.
  - `getTotalUsers()`
    - Giải thích: Lấy tổng số người dùng.
  - `getUnreadOrPendingReports()`
    - Giải thích: Đếm số report chưa xử lý để cảnh báo admin.
  - `getNewUsersToday()`
    - Giải thích: Đếm user mới đăng ký trong ngày.
  - `getTotalDownloads()`
    - Giải thích: Tổng lượt tải app.
  - `getRevenueByMonth()`
    - Giải thích: Doanh thu theo từng tháng cho chart.
  - `getTop5AppsBestSeller()`
    - Giải thích: Top 5 app doanh thu/số lượng cao nhất.