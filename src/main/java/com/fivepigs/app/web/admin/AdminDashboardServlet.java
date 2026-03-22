package com.fivepigs.app.web.admin;

import com.fivepigs.app.dao.AdminDao;
import com.fivepigs.app.web.DashboardServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
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
        int newUsers = adminDao.getNewUsersToday();
        int totalDownloads = adminDao.getTotalDownloads();

        // 2️⃣ Doanh thu theo tháng
        List<AdminDao.RevenueByMonthRow> revenueByMonth = adminDao.getRevenueByMonth();
        double maxRevenue = revenueByMonth.stream().mapToDouble(AdminDao.RevenueByMonthRow::getRevenue).max().orElse(0);
        if (maxRevenue > 0) {
            for (AdminDao.RevenueByMonthRow row : revenueByMonth) {
                int percent = (int) Math.round((row.getRevenue() / maxRevenue) * 100);
                row.setPercent(Math.max(5, percent));
            }
        }

        // 3️⃣ Top 5 app bán chạy
        List<AdminDao.TopAppRow> topAppsBestSeller = adminDao.getTop5AppsBestSeller();

        // 4️⃣ Recent activities
        List<AdminDao.ActivityRow> recentActivities = adminDao.getRecentActivities(8);

        // ====== Set attributes for JSP ======
        req.setAttribute("totalRevenue", totalRevenue);
        req.setAttribute("totalApps", totalApps);
        req.setAttribute("totalUsers", totalUsers);
        req.setAttribute("pendingReports", pendingReports);
        req.setAttribute("newUsers", newUsers);
        req.setAttribute("totalDownloads", totalDownloads);

        req.setAttribute("revenueByMonth", revenueByMonth);
        req.setAttribute("topAppsBestSeller", topAppsBestSeller);
        req.setAttribute("recentActivities", recentActivities == null ? new ArrayList<>() : recentActivities);

        // reuse authorization + forward logic in DashboardServlet
        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);

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
