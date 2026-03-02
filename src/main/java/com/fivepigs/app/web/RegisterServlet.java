package com.fivepigs.app.web;

import com.fivepigs.app.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller: Xử lý request/response cho chức năng đăng ký
 * Tuân theo mô hình MVC
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private final UserService userService;

    public RegisterServlet() {
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Hiển thị form đăng ký (View)
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String fullName = trim(req.getParameter("fullName"));
        String email = trim(req.getParameter("email"));
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        req.setAttribute("fullName", fullName);
        req.setAttribute("email", email);

        String error = userService.validateRegistration(fullName, email, password, confirmPassword);
        if (error != null) {
            req.setAttribute("error", error);
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        try {
            if (userService.emailExists(email)) {
                req.setAttribute("error", "Email đã tồn tại");
                req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
                return;
            }

            // ===== OTP FLOW START =====

            String otp = com.fivepigs.app.util.OtpUtil.generateOtp();

            var session = req.getSession();
            session.setAttribute("reg_fullName", fullName);
            session.setAttribute("reg_email", email);
            session.setAttribute("reg_password", password);
            session.setAttribute("reg_otp", com.fivepigs.app.util.OtpUtil.hashOtp(otp));
            session.setAttribute("reg_otp_expire",
                    System.currentTimeMillis() + 5 * 60 * 1000);
            session.setAttribute("reg_otp_attempts", 0);

            // Chuẩn bị nội dung email HTML
            String emailBody = "<div style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                    + "<h2>Xác thực tài khoản của bạn</h2>"
                    + "<p>Cảm ơn bạn đã đăng ký. Vui lòng sử dụng mã OTP dưới đây để hoàn tất quá trình:</p>"
                    + "<h3 style='color: #007bff; font-size: 24px; letter-spacing: 2px;'><b>" + otp + "</b></h3>"
                    + "<p>Mã OTP này sẽ hết hạn trong 5 phút.</p>"
                    + "<p>Trân trọng,<br/>Đội ngũ FivePigs</p>"
                    + "</div>";

            // Gửi mail async để không block request
            new Thread(() ->
                    com.fivepigs.app.util.EmailService.sendHtmlEmail(
                            email,
                            "Mã xác thực OTP của bạn",
                            emailBody
                    )
            ).start();

            resp.sendRedirect(req.getContextPath() + "/verify-register-otp");

        } catch (SQLException e) {
            throw new ServletException("Lỗi database", e);
        }
    }


    private static String trim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
