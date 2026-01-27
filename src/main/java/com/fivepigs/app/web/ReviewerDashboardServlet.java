package com.fivepigs.app.web;

import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "ReviewerDashboardServlet", urlPatterns = {"/reviewer_dashboard"})
public class ReviewerDashboardServlet extends DashboardServlet {
    @Override
    protected String getDashboardPath() {
        return "/WEB-INF/views/reviewer_dashboard.jsp";
    }

    @Override
    protected boolean isAuthorized(String roleName) {
        return "Reviewer".equals(roleName);
    }
}
