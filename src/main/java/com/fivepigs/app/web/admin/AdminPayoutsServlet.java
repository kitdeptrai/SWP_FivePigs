package com.fivepigs.app.web.admin;

import com.fivepigs.app.model.User;
import com.fivepigs.app.service.AdminService;
import com.fivepigs.app.web.DashboardServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet(name = "AdminPayoutsServlet", urlPatterns = {"/admin/payouts"})
public class AdminPayoutsServlet extends DashboardServlet {
    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String status = req.getParameter("status");
        String keyword = req.getParameter("keyword");
        String fromDate = req.getParameter("fromDate");
        String toDate = req.getParameter("toDate");
        String sortById = req.getParameter("sortById");

        AdminService.PageResult<?> pageResult = adminService.getVendorPayoutsPage(
                req.getParameter("page"),
                10,
                status,
                keyword,
                fromDate,
                toDate,
                sortById
        );

        String approveToken = UUID.randomUUID().toString();
        HttpSession session = req.getSession();
        session.setAttribute("adminPayoutApproveToken", approveToken);

        req.setAttribute("approveToken", approveToken);
        req.setAttribute("payouts", pageResult.getItems());
        req.setAttribute("currentPage", pageResult.getCurrentPage());
        req.setAttribute("totalPages", pageResult.getTotalPages());
        req.setAttribute("status", status);
        req.setAttribute("keyword", keyword);
        req.setAttribute("fromDate", fromDate);
        req.setAttribute("toDate", toDate);
        req.setAttribute("sortById", sortById == null || sortById.isBlank() ? "desc" : sortById);
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        String roleName = session == null ? null : (String) session.getAttribute("roleName");

        if (user == null || !isAuthorized(roleName)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String payoutId = req.getParameter("payoutId");
        Integer adminUserId = user.getUserId();

        String approveToken = req.getParameter("approveToken");
        String expectedToken = session == null ? null : (String) session.getAttribute("adminPayoutApproveToken");
        if (approveToken == null || expectedToken == null || !approveToken.equals(expectedToken)) {
            resp.sendRedirect(req.getContextPath() + "/admin/payouts?error=invalid_token");
            return;
        }

        try {
            String error = adminService.approveVendorPayout(payoutId, adminUserId);
            if (error != null) {
                resp.sendRedirect(req.getContextPath() + "/admin/payouts?error=" + error);
                return;
            }
            session.removeAttribute("adminPayoutApproveToken");
            resp.sendRedirect(req.getContextPath() + "/admin/payouts?success=approved");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/payouts?error=db_error");
        }
    }

    @Override
    protected String getDashboardPath() {
        return "/WEB-INF/views/admin/payouts.jsp";
    }

    @Override
    protected boolean isAuthorized(String roleName) {
        return "admin".equalsIgnoreCase(roleName);
    }
}
