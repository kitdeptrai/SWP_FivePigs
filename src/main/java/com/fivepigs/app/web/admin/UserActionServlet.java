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
        "/admin/users/create",
        "/admin/users/update",
        "/admin/users/enable",
        "/admin/users/disable"
})
public class UserActionServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String servletPath = req.getServletPath();
        try {
            switch (servletPath) {
                case "/admin/users/create" -> handleCreate(req, resp);
                case "/admin/users/update" -> handleUpdate(req, resp);
                case "/admin/users/enable" -> handleStatus(req, resp, "ACTIVE", "enabled");
                case "/admin/users/disable" -> handleStatus(req, resp, "INACTIVE", "disabled");
                default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/users?error=db_error");
        }
    }

    private void handleCreate(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String roleName = req.getParameter("roleName");

        String error = adminService.createUser(fullName, email, phone, roleName);
        if (error != null) {
            resp.sendRedirect(req.getContextPath() + "/admin/users?error=" + error);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/users?success=1");
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String userIdStr = req.getParameter("userId");
        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String status = req.getParameter("status");
        String roleName = req.getParameter("roleName");

        String error = adminService.updateUser(userIdStr, fullName, phone, status, roleName);
        if (error != null) {
            resp.sendRedirect(req.getContextPath() + "/admin/users?error=" + error);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/users?success=updated");
    }

    private void handleStatus(HttpServletRequest req, HttpServletResponse resp, String status, String success) throws IOException, SQLException {
        String userIdStr = req.getParameter("userId");

        String error = adminService.setUserStatus(userIdStr, status);
        if (error != null) {
            resp.sendRedirect(req.getContextPath() + "/admin/users?error=" + error);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/users?success=" + success);
    }
}
