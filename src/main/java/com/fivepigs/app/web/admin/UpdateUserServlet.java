package com.fivepigs.app.web.admin;

import com.fivepigs.app.dao.AdminDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/users/update")
public class UpdateUserServlet extends HttpServlet {
    private final AdminDao adminDao = new AdminDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String userIdStr = req.getParameter("userId");
        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String status = req.getParameter("status");
        String roleName = req.getParameter("roleName"); // Customer or Vendor

        if (userIdStr == null || fullName == null || status == null || roleName == null ||
                userIdStr.isBlank() || fullName.isBlank() || status.isBlank() || roleName.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/admin/users?error=missing_fields");
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/users?error=invalid_id");
            return;
        }

        String roleLower = roleName.trim().toLowerCase();
        if (!(roleLower.equals("customer") || roleLower.equals("vendor"))) {
            resp.sendRedirect(req.getContextPath() + "/admin/users?error=invalid_role");
            return;
        }

        String st = status.trim().toUpperCase();
        if (!(st.equals("ACTIVE") || st.equals("INACTIVE"))) {
            resp.sendRedirect(req.getContextPath() + "/admin/users?error=invalid_status");
            return;
        }

        try {
            adminDao.updateUser(userId, fullName.trim(), (phone == null ? null : phone.trim()), st, roleName.trim());
            resp.sendRedirect(req.getContextPath() + "/admin/users?success=updated");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/users?error=db_error");
        }
    }
}
