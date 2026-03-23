package com.fivepigs.app.web.admin;

import com.fivepigs.app.dao.AdminDao;
import com.fivepigs.app.model.User;
import com.fivepigs.app.web.DashboardServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends DashboardServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        String roleName = session == null ? null : (String) session.getAttribute("roleName");

        if (user == null || !isAuthorized(roleName)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String percentRaw = req.getParameter("commissionPercent");
        if (percentRaw == null || percentRaw.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard?error=missing_commission");
            return;
        }

        double percent;
        try {
            percent = Double.parseDouble(percentRaw.trim());
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard?error=invalid_commission");
            return;
        }

        if (percent < 0 || percent > 20) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard?error=commission_out_of_range");
            return;
        }

        AdminDao adminDao = new AdminDao();
        try {
            adminDao.setCommissionPercent(percent, user.getUserId());
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard?success=commission_updated");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard?error=db_error");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AdminDao adminDao = new AdminDao();

        double commissionPercent = adminDao.getCommissionPercent();
        double totalRevenue = adminDao.getTotalRevenue();
        int totalApps = adminDao.getTotalProducts();
        int totalUsers = adminDao.getTotalUsers();
        int pendingReports = adminDao.getUnreadOrPendingReports();
        int newUsers = adminDao.getNewUsersToday();
        int totalDownloads = adminDao.getTotalDownloads();

        List<AdminDao.RevenueByMonthRow> revenueByMonth = adminDao.getRevenueByMonth();
        double maxRevenue = revenueByMonth.stream()
                .mapToDouble(AdminDao.RevenueByMonthRow::getRevenue)
                .max()
                .orElse(0);
        if (maxRevenue > 0) {
            for (AdminDao.RevenueByMonthRow row : revenueByMonth) {
                int percent = (int) Math.round((row.getRevenue() / maxRevenue) * 100);
                row.setPercent(Math.max(5, percent));
            }
        }

        List<AdminDao.TopAppRow> topAppsBestSeller = adminDao.getTop5AppsBestSeller();

        req.setAttribute("commissionPercent", commissionPercent);
        req.setAttribute("totalRevenue", totalRevenue);
        req.setAttribute("totalApps", totalApps);
        req.setAttribute("totalUsers", totalUsers);
        req.setAttribute("pendingReports", pendingReports);
        req.setAttribute("newUsers", newUsers);
        req.setAttribute("totalDownloads", totalDownloads);
        req.setAttribute("revenueByMonth", revenueByMonth);
        req.setAttribute("topAppsBestSeller", topAppsBestSeller);

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
