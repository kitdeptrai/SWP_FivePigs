package com.fivepigs.app.web;

import com.fivepigs.app.service.UserService;
import com.fivepigs.app.util.EmailService;
import com.fivepigs.app.util.OtpUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/forgot_password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");

        try {
            if (email == null || email.trim().isEmpty()) {
                req.setAttribute("error", "Please enter your email.");
                req.getRequestDispatcher("/WEB-INF/views/forgot_password.jsp").forward(req, resp);
                return;
            }

            // Check if email exists
            boolean exists = userService.emailExists(email);
            if (!exists) {
                req.setAttribute("error", "Account does not exist.");
                req.getRequestDispatcher("/WEB-INF/views/forgot_password.jsp").forward(req, resp);
                return;
            }

            HttpSession session = req.getSession();

            // Simple rate limit: 1 OTP per 60s per session
            Long lastSentAt = (Long) session.getAttribute("reset_otp_last_sent");
            if (lastSentAt != null && System.currentTimeMillis() - lastSentAt < 60_000) {
                req.setAttribute("email", email);
                req.setAttribute("success", "OTP was sent recently. Please try again after 60 seconds.");
                req.getRequestDispatcher("/WEB-INF/views/forgot_password.jsp").forward(req, resp);
                return;
            }

            String otp = OtpUtil.generateOtp();

            session.setAttribute("reset_email", email);
            session.setAttribute("reset_otp_hash", OtpUtil.hashOtp(otp));
            session.setAttribute("reset_otp_expire", System.currentTimeMillis() + 5 * 60 * 1000); // 5 minutes
            session.setAttribute("reset_otp_last_sent", System.currentTimeMillis());
            session.setAttribute("reset_otp_attempts", 0);

            String emailBody = "<div style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                    + "<h2>Password reset request</h2>"
                    + "<p>We received a request to reset your password. Please use the OTP below:</p>"
                    + "<h3 style='color: #dc3545; font-size: 24px; letter-spacing: 2px;'><b>" + otp + "</b></h3>"
                    + "<p>This OTP will expire in 5 minutes.</p>"
                    + "<p>If you did not request this, please ignore this email.</p>"
                    + "</div>";

            new Thread(() -> EmailService.sendHtmlEmail(email, "Password reset request", emailBody)).start();

            // Require OTP verification before allowing password change
            session.setAttribute("reset_verified", false);
            resp.sendRedirect(req.getContextPath() + "/verify-reset-otp");
            return;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database error while checking email", e);
        }
    }
}
