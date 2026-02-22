package com.fivepigs.app.web.admin;

import com.fivepigs.app.dao.AdminDao;
import com.fivepigs.app.util.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/users/create")
public class AddUserServlet extends HttpServlet {
    private final AdminDao adminDao = new AdminDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String roleName = req.getParameter("roleName"); // 'Customer' or 'Vendor'

        if (fullName == null || email == null || roleName == null || fullName.isEmpty() || email.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/users?error=missing_fields");
            return;
        }

        if (adminDao.emailExists(email)) {
            resp.sendRedirect(req.getContextPath() + "/admin/users?error=email_exists");
            return;
        }

        try {
            adminDao.createUser(fullName, email, phone, roleName);
            
            // Gửi email thông báo mật khẩu mặc định
            String subject = "Tài khoản FivePigs của bạn đã được tạo";
            String body = "<div style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                    + "<h2>Chào mừng " + fullName + "!</h2>"
                    + "<p>Tài khoản của bạn trên hệ thống FivePigs đã được tạo thành công.</p>"
                    + "<p><strong>Email đăng nhập:</strong> " + email + "</p>"
                    + "<p><strong>Mật khẩu mặc định:</strong> 123456</p>"
                    + "<p>Vui lòng đăng nhập và đổi mật khẩu ngay để bảo mật tài khoản.</p>"
                    + "<p>Trân trọng,<br/>Đội ngũ Quản trị FivePigs</p>"
                    + "</div>";
            
            new Thread(() -> EmailService.sendHtmlEmail(email, subject, body)).start();

            resp.sendRedirect(req.getContextPath() + "/admin/users?success=1");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/users?error=db_error");
        }
    }
}
