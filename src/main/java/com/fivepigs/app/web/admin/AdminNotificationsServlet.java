package com.fivepigs.app.web.admin;

import com.fivepigs.app.web.DashboardServlet;
import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "AdminNotificationsServlet", urlPatterns = {"/admin/notifications"})
public class AdminNotificationsServlet extends DashboardServlet {

    @Override
    protected String getDashboardPath() {
        return "/WEB-INF/views/admin/notifications.jsp";
    }

    @Override
    protected boolean isAuthorized(String roleName) {
        return "Admin".equals(roleName);
    }
}
