package com.fivepigs.app.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin_dashboard"})
public class AdminDashboardServlet extends DashboardServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ====== Demo data (sau này thay bằng DAO) ======
        int totalProducts = 1240;
        int totalDownloads = 32540;
        int totalEmployees = 18;
        double totalRevenue = 125000000;

        List<Integer> revenueByMonth = Arrays.asList(
                10000000, 15000000, 12000000,
                20000000, 18000000, 25000000
        );

        List<String> months = Arrays.asList(
                "Jan", "Feb", "Mar", "Apr", "May", "Jun"
        );

        List<Integer> topDownloads = Arrays.asList(5200, 4300, 3900, 3100, 2800);
        List<String> topApps = Arrays.asList(
                "App A", "App B", "App C", "App D", "App E"
        );

        // ====== set attribute ======
        req.setAttribute("totalProducts", totalProducts);
        req.setAttribute("totalDownloads", totalDownloads);
        req.setAttribute("totalEmployees", totalEmployees);
        req.setAttribute("totalRevenue", totalRevenue);
        req.setAttribute("revenueByMonth", revenueByMonth);
        req.setAttribute("months", months);
        req.setAttribute("topDownloads", topDownloads);
        req.setAttribute("topApps", topApps);

        // reuse authorization + forward logic in DashboardServlet
        super.doGet(req, resp);
    }

    @Override
    protected String getDashboardPath() {
        return "/WEB-INF/views/admin/admin_dashboard.jsp";
    }

    @Override
    protected boolean isAuthorized(String roleName) {
        return "Admin".equals(roleName);
    }
}
