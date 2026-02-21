package com.fivepigs.app.web.admin;

import com.fivepigs.app.web.DashboardServlet;
import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "AdminEmployeesServlet", urlPatterns = {"/admin/employees"})
public class AdminEmployeesServlet extends DashboardServlet {

    @Override
    protected String getDashboardPath() {
        return "/WEB-INF/views/admin/employees.jsp";
    }

    @Override
    protected boolean isAuthorized(String roleName) {
        return "Admin".equals(roleName);
    }
}
