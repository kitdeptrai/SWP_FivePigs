package com.fivepigs.app.web;

import com.fivepigs.app.util.EmailService;
import com.fivepigs.app.util.OtpUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/resend-otp")
public class ResendOtpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        // Check if registration data exists in session
        if (session == null || session.getAttribute("reg_email") == null) {
            // If not, redirect to the registration page
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        String email = (String) session.getAttribute("reg_email");

        // Generate a new OTP
        String otp = OtpUtil.generateOtp();

        // Update OTP and expiration time in session (5 minutes)
        session.setAttribute("reg_otp", OtpUtil.hashOtp(otp));
        session.setAttribute("reg_otp_expire", System.currentTimeMillis() + 5 * 60 * 1000);
        session.setAttribute("reg_otp_attempts", 0);

        // Prepare HTML email body
        String emailBody = "<div style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                + "<h2>Mã OTP mới của bạn</h2>"
                + "<p>Vui lòng sử dụng mã OTP dưới đây để hoàn tất quá trình đăng ký:</p>"
                + "<h3 style='color: #007bff; font-size: 24px; letter-spacing: 2px;'><b>" + otp + "</b></h3>"
                + "<p>Mã OTP này sẽ hết hạn trong 5 phút.</p>"
                + "<p>Trân trọng,<br/>Đội ngũ FivePigs</p>"
                + "</div>";

        // Send the new OTP email asynchronously
        new Thread(() -> EmailService.sendHtmlEmail(email, "Mã xác thực OTP mới của bạn", emailBody)).start();

        // Redirect back to the verification page with a success message
        resp.sendRedirect(req.getContextPath() + "/verify-register-otp?resend=true");
    }
}
