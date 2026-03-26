package com.fivepigs.app.web;

import com.fivepigs.app.model.User;
import com.fivepigs.app.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final UserService userService;

    public LoginServlet() {
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String redirect = sanitizeRedirect(req.getParameter("redirect"));
        if (redirect != null) {
            req.setAttribute("redirect", redirect);
        }

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            if (redirect != null) {
                resp.sendRedirect(req.getContextPath() + redirect);
                return;
            }
            redirectToDashboard(req, resp, (User) session.getAttribute("user"));
            return;
        }

        CaptchaApiServlet.ensureCaptcha(req, "login");
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String email = trim(req.getParameter("email"));
        String password = req.getParameter("password");
        String redirect = sanitizeRedirect(req.getParameter("redirect"));

        req.setAttribute("email", email);
        req.setAttribute("redirect", redirect);

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            req.setAttribute("error", "Please enter both email and password.");
            CaptchaApiServlet.ensureCaptcha(req, "login");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        String captchaError = CaptchaApiServlet.requireVerified(req.getSession(), "login");
        if (captchaError != null) {
            req.setAttribute("error", captchaError);
            CaptchaApiServlet.ensureCaptcha(req, "login");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        try {
            User user = userService.login(email, password);
            if (user == null) {
                req.setAttribute("error", "Invalid email or password.");
                CaptchaApiServlet.ensureCaptcha(req, "login");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                return;
            }

            HttpSession session = req.getSession();
            String roleName = userService.getRoleName(user.getRoleId());
            user.setRoleName(roleName);
            session.setAttribute("user", user);
            session.setAttribute("roleName", roleName);

            if (redirect != null) {
                resp.sendRedirect(req.getContextPath() + redirect);
                return;
            }

            redirectToDashboard(req, resp, user);
        } catch (SQLException e) {
            throw new ServletException("Database connection error", e);
        }
    }

    private void redirectToDashboard(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException, ServletException {
        try {
            String roleName = userService.getRoleName(user.getRoleId());
            if (roleName == null) {
                roleName = "User";
            }

            String roleNameLower = roleName.toLowerCase();
            String dashboardPath;
            switch (roleNameLower) {
                case "admin":
                    dashboardPath = "/admin/dashboard";
                    break;
                case "aproval":
                case "approval":
                    dashboardPath = "/approval_dashboard";
                    break;
                case "reviewer":
                    dashboardPath = "/reviewer_dashboard";
                    break;
                case "vendor":
                    dashboardPath = "/vendor/dashboard";
                    break;
                case "customer":
                case "user":
                default:
                    dashboardPath = "/customer_dashboard";
                    break;
            }
            resp.sendRedirect(req.getContextPath() + dashboardPath);
        } catch (SQLException e) {
            throw new ServletException("Failed to load role information", e);
        }
    }

    private static String trim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static String sanitizeRedirect(String redirect) {
        if (redirect == null) return null;
        String t = redirect.trim();
        if (t.isEmpty()) return null;
        if (!t.startsWith("/")) return null;
        if (t.startsWith("//")) return null;
        if (t.contains("://")) return null;
        return t;
    }
}
