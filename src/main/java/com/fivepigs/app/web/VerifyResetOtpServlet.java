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

@WebServlet("/verify-reset-otp")
public class VerifyResetOtpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("reset_email") == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        // Handle resend OTP
        String resend = req.getParameter("resend");
        if ("true".equals(resend)) {
            Long lastSentAt = (Long) session.getAttribute("reset_otp_last_sent");
            if (lastSentAt != null && System.currentTimeMillis() - lastSentAt < 60_000) {
                req.setAttribute("error", "Please wait 60 seconds before resending OTP.");
            } else {
                String email = (String) session.getAttribute("reset_email");
                String otp = OtpUtil.generateOtp();
                session.setAttribute("reset_otp_hash", OtpUtil.hashOtp(otp));
                session.setAttribute("reset_otp_expire", System.currentTimeMillis() + 5 * 60 * 1000);
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
                req.setAttribute("success", "OTP resent. Please check your email.");
            }
            req.getRequestDispatcher("/WEB-INF/views/verify_reset_otp.jsp").forward(req, resp);
            return;
        }

        req.getRequestDispatcher("/WEB-INF/views/verify_reset_otp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("reset_email") == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        String inputOtp = req.getParameter("otp");
        String sessionOtpHash = (String) session.getAttribute("reset_otp_hash");
        Long expireTime = (Long) session.getAttribute("reset_otp_expire");
        Integer attempts = (Integer) session.getAttribute("reset_otp_attempts");
        if (attempts == null) attempts = 0;

        if (sessionOtpHash == null || expireTime == null || System.currentTimeMillis() > expireTime) {
            session.invalidate();
            req.setAttribute("error", "OTP expired or invalid. Please try again.");
            req.getRequestDispatcher("/WEB-INF/views/forgot_password.jsp").forward(req, resp);
            return;
        }

        if (inputOtp == null || !OtpUtil.hashOtp(inputOtp).equals(sessionOtpHash)) {
            attempts++;
            session.setAttribute("reset_otp_attempts", attempts);
            if (attempts >= 5) {
                session.invalidate();
                req.setAttribute("error", "You entered the wrong OTP too many times. Please start again.");
                req.getRequestDispatcher("/WEB-INF/views/forgot_password.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("error", "Incorrect OTP. " + (5 - attempts) + " attempts remaining.");
            req.getRequestDispatcher("/WEB-INF/views/verify_reset_otp.jsp").forward(req, resp);
            return;
        }

        session.setAttribute("reset_verified", true);
        resp.sendRedirect(req.getContextPath() + "/reset-password");
    }
}
