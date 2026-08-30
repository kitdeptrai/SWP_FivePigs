package com.fivepigs.app.web.admin;

import com.fivepigs.app.service.AdminService;
import com.fivepigs.app.web.DashboardServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminOrdersServlet", urlPatterns = {"/admin/orders"})
public class AdminOrdersServlet extends DashboardServlet {
    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Đọc bộ lọc tìm kiếm đơn hàng từ query params.
        String keyword = req.getParameter("keyword");
        String fromDate = req.getParameter("fromDate");
        String toDate = req.getParameter("toDate");
        String selectedOrderId = req.getParameter("orderId");

        // Lấy danh sách đơn thành công theo trang + theo điều kiện lọc.
        AdminService.PageResult<?> pageResult = adminService.getSuccessfulOrdersPage(
                req.getParameter("page"),
                10,
                keyword,
                fromDate,
                toDate
        );

        // Set dữ liệu list + paging + filter để JSP render.
        req.setAttribute("orders", pageResult.getItems());
        req.setAttribute("currentPage", pageResult.getCurrentPage());
        req.setAttribute("totalPages", pageResult.getTotalPages());
        req.setAttribute("keyword", keyword);
        req.setAttribute("fromDate", fromDate);
        req.setAttribute("toDate", toDate);
        req.setAttribute("selectedOrderId", selectedOrderId);

        // Nếu admin chọn 1 đơn cụ thể thì tải thêm chi tiết item của đơn đó.
        if (selectedOrderId != null && !selectedOrderId.isBlank()) {
            List<com.fivepigs.app.dao.AdminDao.AdminOrderDetailRow> details = adminService.getOrderDetails(selectedOrderId);
            req.setAttribute("orderDetails", details);
            req.setAttribute("orderDetailsCount", details.size());
            // Tính tổng giá trị phần chi tiết để hiển thị nhanh trên UI.
            double detailsTotal = details.stream().mapToDouble(d -> d.getPrice()).sum();
            req.setAttribute("orderDetailsTotal", detailsTotal);
        }

        // Dùng flow chung dashboard để auth + forward view.
        super.doGet(req, resp);
    }

    @Override
    protected String getDashboardPath() {
        return "/WEB-INF/views/admin/orders.jsp";
    }

    @Override
    protected boolean isAuthorized(String roleName) {
        return "admin".equalsIgnoreCase(roleName);
    }
}
