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
        req.setCharacterEncoding("UTF-8");

        String servletPath = req.getServletPath();
        try {
            switch (servletPath) {
                case "/admin/vendors/enable" -> handleStatus(req, resp, "ACTIVE", "enabled");
                case "/admin/vendors/disable" -> handleStatus(req, resp, "INACTIVE", "disabled");
                default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/vendors?error=db_error");
        }
    }

    private void handleStatus(HttpServletRequest req, HttpServletResponse resp, String status, String success) throws IOException, SQLException {
        String userIdStr = req.getParameter("userId");

        String error = adminService.setUserStatus(userIdStr, status);
        if (error != null) {
            resp.sendRedirect(req.getContextPath() + "/admin/vendors?error=" + error);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/vendors?success=" + success);
    }
}
