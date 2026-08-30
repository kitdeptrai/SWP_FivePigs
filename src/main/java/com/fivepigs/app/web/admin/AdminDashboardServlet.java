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
        // Lấy session hiện tại (không tạo mới) để kiểm tra đăng nhập và quyền admin.
        HttpSession session = req.getSession(false);
        // Lấy thông tin user từ session.
        User user = session == null ? null : (User) session.getAttribute("user");
        // Lấy roleName từ session để xác thực quyền.
        String roleName = session == null ? null : (String) session.getAttribute("roleName");

        // Nếu chưa đăng nhập hoặc không phải admin thì chuyển về trang login.
        if (user == null || !isAuthorized(roleName)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Lấy giá trị phần trăm commission từ form.
        String percentRaw = req.getParameter("commissionPercent");
        // Validate bắt buộc nhập.
        if (percentRaw == null || percentRaw.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard?error=missing_commission");
            return;
        }

        double percent;
        try {
            // Parse chuỗi sang số thực.
            percent = Double.parseDouble(percentRaw.trim());
        } catch (NumberFormatException e) {
            // Không parse được số thì trả lỗi dữ liệu không hợp lệ.
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard?error=invalid_commission");
            return;
        }

        // Ràng buộc nghiệp vụ: commission chỉ nằm trong khoảng 0..20.
        if (percent < 0 || percent > 20) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard?error=commission_out_of_range");
            return;
        }

        // Gọi DAO để cập nhật commission vào DB.
        AdminDao adminDao = new AdminDao();
        try {
            adminDao.setCommissionPercent(percent, user.getUserId());
            // Cập nhật thành công thì redirect kèm thông báo success.
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard?success=commission_updated");
        } catch (SQLException e) {
            // Nếu lỗi DB thì trả về dashboard với cờ lỗi.
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard?error=db_error");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Tạo DAO để lấy dữ liệu thống kê dashboard.
        AdminDao adminDao = new AdminDao();

        // Lấy các KPI chính.
        double commissionPercent = adminDao.getCommissionPercent();
        double totalRevenue = adminDao.getTotalRevenue();
        int totalApps = adminDao.getTotalProducts();
        int totalUsers = adminDao.getTotalUsers();
        int pendingReports = adminDao.getUnreadOrPendingReports();
        int newUsers = adminDao.getNewUsersToday();
        int totalDownloads = adminDao.getTotalDownloads();

        // Lấy doanh thu theo tháng để vẽ biểu đồ.
        List<AdminDao.RevenueByMonthRow> revenueByMonth = adminDao.getRevenueByMonth();
        // Tìm doanh thu lớn nhất để chuẩn hóa phần trăm cột theo tháng.
        double maxRevenue = revenueByMonth.stream()
                .mapToDouble(AdminDao.RevenueByMonthRow::getRevenue)
                .max()
                .orElse(0);
        if (maxRevenue > 0) {
            for (AdminDao.RevenueByMonthRow row : revenueByMonth) {
                // Tính độ cao cột theo tỉ lệ % so với tháng cao nhất.
                int percent = (int) Math.round((row.getRevenue() / maxRevenue) * 100);
                // Đặt ngưỡng tối thiểu 5% để cột nhỏ vẫn nhìn thấy được trên UI.
                row.setPercent(Math.max(5, percent));
            }
        }

        // Lấy top 5 ứng dụng bán chạy.
        List<AdminDao.TopAppRow> topAppsBestSeller = adminDao.getTop5AppsBestSeller();

        // Đẩy toàn bộ dữ liệu sang JSP qua request attributes.
        req.setAttribute("commissionPercent", commissionPercent);
        req.setAttribute("totalRevenue", totalRevenue);
        req.setAttribute("totalApps", totalApps);
        req.setAttribute("totalUsers", totalUsers);
        req.setAttribute("pendingReports", pendingReports);
        req.setAttribute("newUsers", newUsers);
        req.setAttribute("totalDownloads", totalDownloads);
        req.setAttribute("revenueByMonth", revenueByMonth);
        req.setAttribute("topAppsBestSeller", topAppsBestSeller);

        // Forward đến trang dashboard admin để render dữ liệu.
        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
    }

    @Override
    protected String getDashboardPath() {
        return "/WEB-INF/views/admin/dashboard.jsp";
    }

    @Override
    protected boolean isAuthorized(String roleName) {
        return "admin".equalsIgnoreCase(roleName);
    }
}
