package com.fivepigs.app.web;

import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "ApprovalDashboardServlet", urlPatterns = {"/approval_dashboard"})
public class ApprovalDashboardServlet extends DashboardServlet {
    @Override
    protected String getDashboardPath() {
        return "/WEB-INF/views/approval_dashboard.jsp";
    }

    @Override
    protected boolean isAuthorized(String roleName) {
        return "Approval".equals(roleName);
    }
}
