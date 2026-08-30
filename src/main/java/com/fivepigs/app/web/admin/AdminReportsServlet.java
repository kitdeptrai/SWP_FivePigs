package com.fivepigs.app.web.admin;

import com.fivepigs.app.service.AdminService;
import com.fivepigs.app.web.DashboardServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AdminReportsServlet", urlPatterns = {"/admin/reports"})
public class AdminReportsServlet extends DashboardServlet {
    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Đọc filter danh sách feedback/report từ request.
        String keyword = req.getParameter("keyword");
        String status = req.getParameter("status");
        String selectedFeedbackId = req.getParameter("feedbackId");

        // Lấy dữ liệu report người dùng theo phân trang.
        AdminService.PageResult<?> pageResult = adminService.getUserFeedbackPage(
                req.getParameter("page"),
                10,
                keyword,
                status
        );

        // Set attributes cho JSP render danh sách + phân trang + filter.
        req.setAttribute("reports", pageResult.getItems());
        req.setAttribute("currentPage", pageResult.getCurrentPage());
        req.setAttribute("totalPages", pageResult.getTotalPages());
        req.setAttribute("keyword", keyword);
        req.setAttribute("status", status);
        req.setAttribute("selectedFeedbackId", selectedFeedbackId);

        // Nếu có chọn feedback cụ thể thì nạp thêm dữ liệu chi tiết.
        if (selectedFeedbackId != null && !selectedFeedbackId.isBlank()) {
            req.setAttribute("selectedFeedback", adminService.getFeedbackDetail(selectedFeedbackId));
        }

        // Dùng flow chung dashboard để auth + forward view.
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Chỉ chấp nhận action markRead từ form admin reports.
        String action = req.getParameter("action");
        if (!"markRead".equalsIgnoreCase(action)) {
            resp.sendRedirect(req.getContextPath() + "/admin/reports?error=invalid_action");
            return;
        }

        // Lấy feedbackId cần cập nhật trạng thái.
        String feedbackId = req.getParameter("feedbackId");
        try {
            String error = adminService.markFeedbackAsRead(feedbackId);
            if (error != null) {
                // Có lỗi nghiệp vụ thì quay về list kèm mã lỗi.
                resp.sendRedirect(req.getContextPath() + "/admin/reports?error=" + error);
                return;
            }
            // Thành công thì quay về list kèm cờ success.
            resp.sendRedirect(req.getContextPath() + "/admin/reports?success=marked_read");
        } catch (Exception e) {
            // Lỗi hệ thống/DB thì trả mã lỗi chung.
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/reports?error=db_error");
        }
    }

    @Override
    protected String getDashboardPath() {
        return "/WEB-INF/views/admin/reports.jsp";
    }

    @Override
    protected boolean isAuthorized(String roleName) {
        return "admin".equalsIgnoreCase(roleName);
    }
}
