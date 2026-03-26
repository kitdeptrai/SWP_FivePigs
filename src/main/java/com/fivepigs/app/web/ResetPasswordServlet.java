package com.fivepigs.app.web;

import com.fivepigs.app.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("reset_email") == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        Boolean verified = (Boolean) session.getAttribute("reset_verified");
        if (verified == null || !verified) {
            resp.sendRedirect(req.getContextPath() + "/verify-reset-otp");
            return;
        }

        req.getRequestDispatcher("/WEB-INF/views/reset_password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("reset_email") == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        Boolean verified = (Boolean) session.getAttribute("reset_verified");
        if (verified == null || !verified) {
            resp.sendRedirect(req.getContextPath() + "/verify-reset-otp");
            return;
        }

        String newPassword = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        String email = (String) session.getAttribute("reset_email");

        if (newPassword == null || newPassword.length() < 6 || newPassword.length() > 72) {
            req.setAttribute("error", "New password must be between 6 and 72 characters.");
            req.getRequestDispatcher("/WEB-INF/views/reset_password.jsp").forward(req, resp);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("error", "New passwords do not match.");
            req.getRequestDispatcher("/WEB-INF/views/reset_password.jsp").forward(req, resp);
            return;
        }

        try {
            userService.resetPassword(email, newPassword);

            // Invalidate OTP after success to prevent reuse
            session.removeAttribute("reset_otp_hash");
            session.removeAttribute("reset_otp_expire");
            session.removeAttribute("reset_otp_last_sent");
            session.removeAttribute("reset_otp_attempts");
            session.removeAttribute("reset_email");

            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login?reset=success");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database error while updating password", e);
        }
    }
}
