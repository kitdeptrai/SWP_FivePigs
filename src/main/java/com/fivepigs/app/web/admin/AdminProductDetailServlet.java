package com.fivepigs.app.web.admin;

import com.fivepigs.app.dao.SoftwareImageDao;
import com.fivepigs.app.model.SoftwareImage;
import com.fivepigs.app.service.AdminService;
import com.fivepigs.app.web.DashboardServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "AdminProductDetailServlet", urlPatterns = {"/admin/products/detail"})
public class AdminProductDetailServlet extends DashboardServlet {
    private final AdminService adminService = new AdminService();
    private final SoftwareImageDao softwareImageDao = new SoftwareImageDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String softwareId = req.getParameter("softwareId");
        var product = adminService.getProductDetail(softwareId);
        if (product == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/products?error=invalid_id");
            return;
        }

        try {
            List<SoftwareImage> images = softwareImageDao.getImagesBySoftwareId(Integer.parseInt(softwareId));
            req.setAttribute("images", images);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        req.setAttribute("product", product);
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
