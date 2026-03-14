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
        "/admin/employees/create",
        "/admin/employees/update",
        "/admin/employees/enable",
        "/admin/employees/disable"
})
public class EmployeeActionServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String servletPath = req.getServletPath();
        try {
            switch (servletPath) {
                case "/admin/employees/create" -> handleCreate(req, resp);
                case "/admin/employees/update" -> handleUpdate(req, resp);
                case "/admin/employees/enable" -> handleStatus(req, resp, "ACTIVE", "enabled");
                case "/admin/employees/disable" -> handleStatus(req, resp, "INACTIVE", "disabled");
                default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/employees?error=db_error");
        }
    }

    private void handleCreate(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String roleName = req.getParameter("roleName");

        String error = adminService.createEmployee(fullName, email, phone, roleName);
        if (error != null) {
            resp.sendRedirect(req.getContextPath() + "/admin/employees?error=" + error);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/employees?success=1");
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String userIdStr = req.getParameter("userId");
        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String status = req.getParameter("status");
        String roleName = req.getParameter("roleName");

        String error = adminService.updateEmployee(userIdStr, fullName, phone, status, roleName);
        if (error != null) {
            resp.sendRedirect(req.getContextPath() + "/admin/employees?error=" + error);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/employees?success=updated");
    }

    private void handleStatus(HttpServletRequest req, HttpServletResponse resp, String status, String success) throws IOException, SQLException {
        String userIdStr = req.getParameter("userId");

        String error = adminService.setUserStatus(userIdStr, status);
        if (error != null) {
            resp.sendRedirect(req.getContextPath() + "/admin/employees?error=" + error);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/employees?success=" + success);
    }
}
