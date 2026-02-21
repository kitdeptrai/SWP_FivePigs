package com.fivepigs.app.web.admin;

import com.fivepigs.app.web.DashboardServlet;
import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "AdminReportsServlet", urlPatterns = {"/admin/reports"})
public class AdminReportsServlet extends DashboardServlet {

    @Override
    protected String getDashboardPath() {
        return "/WEB-INF/views/admin/reports.jsp";
    }

    @Override
    protected boolean isAuthorized(String roleName) {
        return "Admin".equals(roleName);
    }
}
