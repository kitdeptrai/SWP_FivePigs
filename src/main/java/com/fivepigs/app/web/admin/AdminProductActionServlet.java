package com.fivepigs.app.web.admin;

import com.fivepigs.app.service.AdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet({
        "/admin/products/update",
        "/admin/products/enable",
        "/admin/products/disable"
})
public class AdminProductActionServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String servletPath = req.getServletPath();

        try {
            switch (servletPath) {
                case "/admin/products/update" -> handleUpdate(req, resp);
                case "/admin/products/enable" -> handleStatus(req, resp, "ACTIVE", "enabled");
                case "/admin/products/disable" -> handleStatus(req, resp, "INACTIVE", "disabled");
                default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/products?error=db_error");
        }
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String error = adminService.updateProduct(
                req.getParameter("softwareId"),
                req.getParameter("name"),
                req.getParameter("shortDescription"),
                req.getParameter("categoryId"),
                req.getParameter("price"),
                req.getParameter("isFree"),
                req.getParameter("status")
        );

        if (error != null) {
            resp.sendRedirect(req.getContextPath() + "/admin/products?error=" + error);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/products?success=updated");
    }

    private void handleStatus(HttpServletRequest req, HttpServletResponse resp, String status, String success) throws IOException, SQLException {
        String error = adminService.updateProductStatus(req.getParameter("softwareId"), status);
        if (error != null) {
            resp.sendRedirect(req.getContextPath() + "/admin/products?error=" + error);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/products?success=" + success);
    }
}
