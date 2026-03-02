package com.fivepigs.app.web.admin;

import com.fivepigs.app.dao.AdminDao;
import com.fivepigs.app.web.DashboardServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends DashboardServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AdminDao adminDao = new AdminDao();

        // 1️⃣ System Overview Cards
        double totalRevenue = adminDao.getTotalRevenue();
        int totalApps = adminDao.getTotalProducts();
        int totalUsers = adminDao.getTotalUsers();
        int pendingReports = adminDao.getUnreadOrPendingReports();

        // 2️⃣ Doanh thu theo tháng
        List<AdminDao.RevenueByMonthRow> revenueByMonth = adminDao.getRevenueByMonth();

        // 3️⃣ Top 5 app bán chạy
        List<AdminDao.TopAppRow> topAppsBestSeller = adminDao.getTop5AppsBestSeller();

        // ====== Set attributes for JSP ======
        req.setAttribute("totalRevenue", totalRevenue);
        req.setAttribute("totalApps", totalApps);
        req.setAttribute("totalUsers", totalUsers);
        req.setAttribute("pendingReports", pendingReports);
        
        req.setAttribute("revenueByMonth", revenueByMonth);
        req.setAttribute("topAppsBestSeller", topAppsBestSeller);

        // reuse authorization + forward logic in DashboardServlet
        super.doGet(req, resp);
    }

    @Override
    protected String getDashboardPath() {
        return "/WEB-INF/views/admin/dashboard.jsp";
    }

    @Override
    protected boolean isAuthorized(String roleName) {
        return "Admin".equals(roleName);
    }
}
