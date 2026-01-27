package com.fivepigs.app.web;

import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin_dashboard"})
public class AdminDashboardServlet extends DashboardServlet {
    @Override
    protected String getDashboardPath() {
        return "/WEB-INF/views/admin_dashboard.jsp";
    }

    @Override
    protected boolean isAuthorized(String roleName) {
        return "Admin".equals(roleName);
    }
}
