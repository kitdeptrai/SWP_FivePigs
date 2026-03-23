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
        "/admin/products/approve",
        "/admin/products/reject"
})
public class AdminProductActionServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String servletPath = req.getServletPath();

        try {
            switch (servletPath) {
                case "/admin/products/approve" -> handleApprove(req, resp);
                case "/admin/products/reject" -> handleReject(req, resp);
                default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/products?error=db_error");
        }
    }

    private void handleApprove(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String error = adminService.approveReport(req.getParameter("reportId"));
        if (error != null) {
            resp.sendRedirect(req.getContextPath() + "/admin/products?error=" + error);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/products?success=approved");
    }

    private void handleReject(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String error = adminService.rejectReport(req.getParameter("reportId"));
        if (error != null) {
            resp.sendRedirect(req.getContextPath() + "/admin/products?error=" + error);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/products?success=rejected");
    }
}
