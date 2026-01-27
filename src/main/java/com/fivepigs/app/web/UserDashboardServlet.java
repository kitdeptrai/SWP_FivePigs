package com.fivepigs.app.web;

import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "UserDashboardServlet", urlPatterns = {"/user_dashboard"})
public class UserDashboardServlet extends DashboardServlet {
    @Override
    protected String getDashboardPath() {
        return "/WEB-INF/views/user_dashboard.jsp";
    }

    @Override
    protected boolean isAuthorized(String roleName) {
        return "User".equals(roleName);
    }
}
