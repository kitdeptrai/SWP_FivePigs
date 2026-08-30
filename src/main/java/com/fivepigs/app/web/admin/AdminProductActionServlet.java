package com.fivepigs.app.web.admin;

import com.fivepigs.app.service.AdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet({
        "/admin/products/approve",
        "/admin/products/reject"
})
public class AdminProductActionServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Đảm bảo đọc đúng dữ liệu tiếng Việt khi gửi form POST.
        req.setCharacterEncoding("UTF-8");
        // Lấy đường dẫn servlet thực tế để phân nhánh approve/reject.
        String servletPath = req.getServletPath();

        try {
            switch (servletPath) {
                // Duyệt report sản phẩm.
                case "/admin/products/approve" -> handleApprove(req, resp);
                // Từ chối report sản phẩm.
                case "/admin/products/reject" -> handleReject(req, resp);
                // Endpoint không hợp lệ.
                default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            // Có lỗi DB thì quay về trang danh sách với cờ lỗi.
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/products?error=db_error");
        }
    }

    private void handleApprove(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        // Gọi service chuyển trạng thái report sang nhánh đã duyệt.
        String error = adminService.approveReport(req.getParameter("reportId"));
        if (error != null) {
            // Nếu có lỗi nghiệp vụ thì redirect về list kèm mã lỗi.
            resp.sendRedirect(req.getContextPath() + "/admin/products?error=" + error);
            return;
        }
        // Thành công thì redirect với cờ success để hiển thị thông báo.
        resp.sendRedirect(req.getContextPath() + "/admin/products?success=approved");
    }

    private void handleReject(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        // Gọi service chuyển trạng thái report sang nhánh từ chối.
        String error = adminService.rejectReport(req.getParameter("reportId"));
        if (error != null) {
            // Nếu có lỗi nghiệp vụ thì redirect về list kèm mã lỗi.
            resp.sendRedirect(req.getContextPath() + "/admin/products?error=" + error);
            return;
        }
        // Thành công thì redirect với cờ success để hiển thị thông báo.
        resp.sendRedirect(req.getContextPath() + "/admin/products?success=rejected");
    }
}
