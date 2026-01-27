package com.fivepigs.app.web;

import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "VendorDashboardServlet", urlPatterns = {"/vendor_dashboard"})
public class VendorDashboardServlet extends DashboardServlet {
    @Override
    protected String getDashboardPath() {
        return "/WEB-INF/views/vendor_dashboard.jsp";
    }

    @Override
    protected boolean isAuthorized(String roleName) {
        return "Vendor".equals(roleName);
    }
}
