package com.fivepigs.app.web.admin;

import com.fivepigs.app.web.DashboardServlet;
import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "AdminUsersServlet", urlPatterns = {"/admin/users"})
public class AdminUsersServlet extends DashboardServlet {

    @Override
    protected String getDashboardPath() {
        return "/WEB-INF/views/admin/users.jsp";
    }

    @Override
    protected boolean isAuthorized(String roleName) {
        return "Admin".equals(roleName);
    }
}
