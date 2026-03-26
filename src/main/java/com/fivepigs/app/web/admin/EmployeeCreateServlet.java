package com.fivepigs.app.web.admin;

import com.fivepigs.app.dao.AdminDao;
import com.fivepigs.app.dao.NotificationDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "EmployeeCreateServlet", urlPatterns = {"/admin/employees/create"})
public class EmployeeCreateServlet extends HttpServlet {
    private final AdminDao adminDao = new AdminDao();
    private static final String DEFAULT_PASSWORD = "123456";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String fullName = trim(req.getParameter("fullName"));
        String email    = trim(req.getParameter("email"));
        String phone    = trim(req.getParameter("phone"));
        String roleName = trim(req.getParameter("roleName"));

        if (fullName == null || email == null || roleName == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/employees?error=missing_fields#add-employee");
            return;
        }

        try {
            if (adminDao.emailExists(email)) {
                resp.sendRedirect(req.getContextPath() + "/admin/employees?error=email_exists#add-employee");
                return;
            }

            adminDao.createEmployee(fullName, email, phone, roleName, DEFAULT_PASSWORD);

            // Thông báo đến admin về việc thêm nhân viên mới
            NotificationDao notifDao = new NotificationDao();
            notifDao.broadcastToAdmins(
                "New Employee Added",
                "Admin added new employee: " + fullName + " (" + email + ") with role " + roleName + ".",
                "USER_UPDATE",
                "LOW",
                "/admin/employees"
            );

            // Gửi email thông báo mật khẩu mặc định (async)
            String emailBody = "<div style='font-family: Arial, sans-serif;'>"
                    + "<h2>Your FivePigs account has been created</h2>"
                    + "<p>Hello <b>" + fullName + "</b>,</p>"
                    + "<p>Your account has been created with the following credentials:</p>"
                    + "<ul><li>Email: <b>" + email + "</b></li>"
                    + "<li>Password: <b>" + DEFAULT_PASSWORD + "</b></li>"
                    + "<li>Role: <b>" + roleName + "</b></li></ul>"
                    + "<p>Please change your password after logging in.</p>"
                    + "<p>Team FivePigs</p></div>";
            final String emailFinal = email;
            new Thread(() ->
                com.fivepigs.app.util.EmailService.sendHtmlEmail(emailFinal, "Your FivePigs account credentials", emailBody)
            ).start();

            resp.sendRedirect(req.getContextPath() + "/admin/employees?success=1");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/employees?error=db_error#add-employee");
        }
    }

    private static String trim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
