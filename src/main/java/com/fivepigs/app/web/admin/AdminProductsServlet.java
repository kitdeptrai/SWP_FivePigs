package com.fivepigs.app.web.admin;

import com.fivepigs.app.service.AdminService;
import com.fivepigs.app.web.DashboardServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AdminProductsServlet", urlPatterns = {"/admin/products"})
public class AdminProductsServlet extends DashboardServlet {
    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Đọc tham số lọc từ query string.
        String keyword = req.getParameter("keyword");
        String status = req.getParameter("status");

        // Gọi service lấy dữ liệu report sản phẩm theo trang.
        AdminService.PageResult<?> pageResult = adminService.getProductsPage(
                req.getParameter("page"),
                10,
                keyword,
                status
        );

        // Set dữ liệu ra request để JSP render danh sách + phân trang + filter hiện tại.
        req.setAttribute("products", pageResult.getItems());
        req.setAttribute("currentPage", pageResult.getCurrentPage());
        req.setAttribute("totalPages", pageResult.getTotalPages());
        req.setAttribute("keyword", keyword);
        req.setAttribute("status", status);

        // Dùng luồng chung của DashboardServlet để kiểm tra quyền và forward view.
        super.doGet(req, resp);
    }

    @Override
    protected String getDashboardPath() {
        return "/WEB-INF/views/admin/products.jsp";
    }

    @Override
    protected boolean isAuthorized(String roleName) {
        return "admin".equalsIgnoreCase(roleName);
    }
}
