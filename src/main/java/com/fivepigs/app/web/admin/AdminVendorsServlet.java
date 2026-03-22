package com.fivepigs.app.web.admin;

import com.fivepigs.app.service.AdminService;
import com.fivepigs.app.web.DashboardServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AdminVendorsServlet", urlPatterns = {"/admin/vendors"})
public class AdminVendorsServlet extends DashboardServlet {
    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        String status = req.getParameter("status");

        AdminService.PageResult<?> pageResult = adminService.getVendorsPage(
                req.getParameter("page"),
                10,
                keyword,
                status
        );

        req.setAttribute("users", pageResult.getItems());
        req.setAttribute("currentPage", pageResult.getCurrentPage());
        req.setAttribute("totalPages", pageResult.getTotalPages());
        req.setAttribute("keyword", keyword);
        req.setAttribute("status", status);
        super.doGet(req, resp);
    }

    @Override
    protected String getDashboardPath() {
        return "/WEB-INF/views/admin/vendors.jsp";
    }

    @Override
    protected boolean isAuthorized(String roleName) {
        return "admin".equalsIgnoreCase(roleName);
    }
}
