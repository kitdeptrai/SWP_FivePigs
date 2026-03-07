package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.UserDao;
import com.fivepigs.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "SettingsServlet", urlPatterns = {"/settings"})
public class SettingsServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User sessionUser = resolveSessionUser(request.getSession(false));
        if (sessionUser == null || sessionUser.getEmail() == null || sessionUser.getEmail().isBlank()) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/settings");
            return;
        }

        request.setAttribute("selectedTab", resolveTab(request));
        request.setAttribute("activePage", "settings");
        request.getRequestDispatcher("/WEB-INF/views/customer/settings.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User sessionUser = resolveSessionUser(request.getSession(false));
        if (sessionUser == null || sessionUser.getEmail() == null || sessionUser.getEmail().isBlank()) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/settings");
            return;
        }

        String tab = resolveTab(request);
        String redirectBase = request.getContextPath() + "/settings?tab=" + tab;

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (currentPassword == null || newPassword == null || confirmPassword == null
                || currentPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
            response.sendRedirect(redirectBase + "&msg=missing_fields");
            return;
        }

        if (newPassword.length() < 6 || newPassword.length() > 72) {
            response.sendRedirect(redirectBase + "&msg=invalid_password");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            response.sendRedirect(redirectBase + "&msg=confirm_not_match");
            return;
        }

        try {
            User latest = userDao.findByEmail(sessionUser.getEmail());
            if (latest == null) {
                response.sendRedirect(redirectBase + "&msg=user_not_found");
                return;
            }

            if (!currentPassword.equals(latest.getPassword())) {
                response.sendRedirect(redirectBase + "&msg=wrong_current_password");
                return;
            }

            userDao.updatePassword(sessionUser.getEmail(), newPassword);
            latest.setPassword(newPassword);
            request.getSession().setAttribute("user", latest);
            response.sendRedirect(redirectBase + "&msg=password_updated");

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private User resolveSessionUser(HttpSession session) {
        if (session == null) return null;
        return (User) session.getAttribute("user");
    }

    private String resolveTab(HttpServletRequest request) {
        String tab = request.getParameter("tab");
        if (tab == null) {
            return "store_settings";
        }

        return switch (tab) {
            case "payment_methods", "redeem_code", "payment_help", "devices", "feedback", "store_settings" -> tab;
            default -> "store_settings";
        };
    }
}

