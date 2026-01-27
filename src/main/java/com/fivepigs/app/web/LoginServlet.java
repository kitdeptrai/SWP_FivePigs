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

/**
 * Controller: Xử lý đăng nhập
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final UserService userService;

    public LoginServlet() {
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Kiểm tra đã đăng nhập chưa
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // Đã đăng nhập, redirect đến dashboard
            redirectToDashboard(req, resp, (User) session.getAttribute("user"));
            return;
        }
        
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String email = trim(req.getParameter("email"));
        String password = req.getParameter("password");

        req.setAttribute("email", email);

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            req.setAttribute("error", "Vui lòng nhập đầy đủ email và mật khẩu");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        try {
            User user = userService.login(email, password);
            if (user == null) {
                req.setAttribute("error", "Email hoặc mật khẩu không đúng");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                return;
            }

            // Lưu user vào session
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            session.setAttribute("roleName", userService.getRoleName(user.getRoleId()));

            // Redirect đến dashboard theo role
            redirectToDashboard(req, resp, user);
        } catch (SQLException e) {
            throw new ServletException("Lỗi kết nối database", e);
        }
    }

    private void redirectToDashboard(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException, ServletException {
        try {
            String roleName = userService.getRoleName(user.getRoleId());
            if (roleName == null) {
                roleName = "User";
            }
            
            // Redirect đến dashboard theo role
            String dashboardPath = "/" + roleName.toLowerCase() + "_dashboard";
            resp.sendRedirect(req.getContextPath() + dashboardPath);
        } catch (SQLException e) {
            throw new ServletException("Lỗi lấy thông tin role", e);
        }
    }

    private static String trim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
