package com.fivepigs.app.web;

import com.fivepigs.app.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Base servlet cho các dashboard
 */
public abstract class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        
        // Kiểm tra đã đăng nhập chưa
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        String roleName = (String) session.getAttribute("roleName");
        
        // Kiểm tra role có đúng không
        if (!isAuthorized(roleName)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.setAttribute("user", user);
        req.setAttribute("roleName", roleName);
        req.getRequestDispatcher(getDashboardPath()).forward(req, resp);
    }

    protected abstract String getDashboardPath();
    protected abstract boolean isAuthorized(String roleName);
}
