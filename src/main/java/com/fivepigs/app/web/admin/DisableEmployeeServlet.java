package com.fivepigs.app.web.admin;

import com.fivepigs.app.dao.AdminDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/employees/disable")
public class DisableEmployeeServlet extends HttpServlet {
    private final AdminDao adminDao = new AdminDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userIdStr = req.getParameter("userId");

        if (userIdStr == null || userIdStr.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/admin/employees?error=missing_id");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);
            adminDao.setUserStatus(userId, "INACTIVE");
            resp.sendRedirect(req.getContextPath() + "/admin/employees?success=disabled");
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/employees?error=db_error");
        }
    }
}
