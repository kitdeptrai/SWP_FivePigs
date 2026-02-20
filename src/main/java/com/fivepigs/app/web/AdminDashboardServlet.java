package com.fivepigs.app.web;

import com.fivepigs.app.dao.AdminDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin_dashboard"})
public class AdminDashboardServlet extends DashboardServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AdminDao adminDao = new AdminDao();

        // ====== Lấy dữ liệu thật từ Database ======
        int totalProducts = adminDao.getTotalProducts();
        int totalDownloads = adminDao.getTotalDownloads();
        int totalEmployees = adminDao.getTotalStaff();
        double totalRevenue = adminDao.getTotalRevenue();

        // ====== Dữ liệu thật cho biểu đồ ======
        Map<String, Double> revenueByMonthMap = adminDao.getRevenueByMonth();
        List<Integer> revenueByMonth = new ArrayList<>();
        List<String> months = new ArrayList<>();
        for (Map.Entry<String, Double> e : revenueByMonthMap.entrySet()) {
            months.add(e.getKey());
            revenueByMonth.add((int) Math.round(e.getValue()));
        }

        // ====== Top apps theo downloads (Option B: hiển thị số downloads, progress theo tỷ lệ so với top 1) ======
        List<Map<String, Object>> topAppsRaw = adminDao.getTopAppsByDownloads();
        List<String> topApps = new ArrayList<>();
        List<Integer> topDownloads = new ArrayList<>();
        int maxDownloads = 0;
        for (Map<String, Object> row : topAppsRaw) {
            int dl = ((Number) row.get("downloads")).intValue();
            if (dl > maxDownloads) maxDownloads = dl;
        }
        for (Map<String, Object> row : topAppsRaw) {
            topApps.add(String.valueOf(row.get("name")));
            int dl = ((Number) row.get("downloads")).intValue();
            int pct = (maxDownloads <= 0) ? 0 : (int) Math.round(dl * 100.0 / maxDownloads);
            topDownloads.add(pct);
        }

        req.setAttribute("topAppsDownloads", topAppsRaw);

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
