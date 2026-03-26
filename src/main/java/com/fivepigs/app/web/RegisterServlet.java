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
 * Controller: Handles request/response for registration
 * Follows MVC pattern
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private final UserService userService;

    public RegisterServlet() {
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Show register form (View)
        CaptchaApiServlet.ensureCaptcha(req, "register");
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
            CaptchaApiServlet.ensureCaptcha(req, "register");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        String captchaError = CaptchaApiServlet.requireVerified(req.getSession(), "register");
        if (captchaError != null) {
            req.setAttribute("error", captchaError);
            CaptchaApiServlet.ensureCaptcha(req, "register");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        try {
            if (userService.emailExists(email)) {
                req.setAttribute("error", "Email already exists.");
                CaptchaApiServlet.ensureCaptcha(req, "register");
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

            // Build email content
            String emailBody = "<div style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                    + "<h2>Verify your account</h2>"
                    + "<p>Thank you for registering. Please use the OTP below to complete your registration:</p>"
                    + "<h3 style='color: #007bff; font-size: 24px; letter-spacing: 2px;'><b>" + otp + "</b></h3>"
                    + "<p>This OTP will expire in 5 minutes.</p>"
                    + "<p>Best regards,<br/>FivePigs Team</p>"
                    + "</div>";

            // Send email asynchronously to avoid blocking request
            new Thread(() ->
                    com.fivepigs.app.util.EmailService.sendHtmlEmail(
                            email,
                            "Your OTP verification code",
                            emailBody
                    )
            ).start();

            resp.sendRedirect(req.getContextPath() + "/verify-register-otp");

        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }


    private static String trim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
