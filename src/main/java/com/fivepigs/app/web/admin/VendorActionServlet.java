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
        "/admin/vendors/enable",
        "/admin/vendors/disable"
})
public class VendorActionServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Đảm bảo đọc đúng dữ liệu UTF-8 từ form POST.
        req.setCharacterEncoding("UTF-8");

        // Xác định action theo endpoint gọi vào (enable/disable).
        String servletPath = req.getServletPath();
        try {
            switch (servletPath) {
                // Bật lại trạng thái vendor.
                case "/admin/vendors/enable" -> handleStatus(req, resp, "ACTIVE", "enabled");
                // Khóa (disable) trạng thái vendor.
                case "/admin/vendors/disable" -> handleStatus(req, resp, "INACTIVE", "disabled");
                // Endpoint không hợp lệ.
                default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            // Lỗi DB thì quay về trang vendors với cờ lỗi.
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/vendors?error=db_error");
        }
    }

    private void handleStatus(HttpServletRequest req, HttpServletResponse resp, String status, String success) throws IOException, SQLException {
        // Lấy userId vendor cần cập nhật trạng thái.
        String userIdStr = req.getParameter("userId");

        // Gọi service đổi trạng thái tài khoản vendor.
        String error = adminService.setUserStatus(userIdStr, status);
        if (error != null) {
            // Có lỗi nghiệp vụ thì redirect về list kèm mã lỗi.
            resp.sendRedirect(req.getContextPath() + "/admin/vendors?error=" + error);
            return;
        }
        // Thành công thì redirect với cờ success.
        resp.sendRedirect(req.getContextPath() + "/admin/vendors?success=" + success);
    }
}
