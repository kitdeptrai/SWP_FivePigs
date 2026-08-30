package com.fivepigs.app.web.admin;

import com.fivepigs.app.service.AdminService;
import com.fivepigs.app.web.DashboardServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AdminProductDetailServlet", urlPatterns = {"/admin/products/detail"})
public class AdminProductDetailServlet extends DashboardServlet {
    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy id report sản phẩm cần xem chi tiết từ URL.
        String reportId = req.getParameter("reportId");
        // Gọi service để lấy đầy đủ thông tin report.
        var report = adminService.getProductReportDetail(reportId);
        // Nếu reportId không hợp lệ hoặc không tồn tại thì quay về danh sách với thông báo lỗi.
        if (report == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/products?error=invalid_id");
            return;
        }

        // Đẩy dữ liệu report vào request để trang detail sử dụng.
        req.setAttribute("report", report);
        // Dùng flow chung để kiểm tra auth và forward đến JSP.
        super.doGet(req, resp);
    }

    @Override
    protected String getDashboardPath() {
        return "/WEB-INF/views/admin/product_detail.jsp";
    }

    @Override
    protected boolean isAuthorized(String roleName) {
        return "admin".equalsIgnoreCase(roleName);
    }
}
