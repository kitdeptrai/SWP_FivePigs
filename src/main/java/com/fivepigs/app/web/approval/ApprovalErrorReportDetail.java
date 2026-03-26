package com.fivepigs.app.web.approval;

import com.fivepigs.app.dao.NotificationDao;
import com.fivepigs.app.dao.ReportDao;
import com.fivepigs.app.model.Report;
import com.fivepigs.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ApprovalErrorReportDetail", urlPatterns = { "/ApprovalErrorReportDetail" })
public class ApprovalErrorReportDetail extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String reportIdStr = request.getParameter("reportId");
        if (reportIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/ApprovalErrorReports");
            return;
        }

        try {
            int reportId = Integer.parseInt(reportIdStr);
            ReportDao reportDao = new ReportDao();
            Report report = reportDao.getErrorApprovalReportById(reportId);
            if (report == null) {
                response.sendRedirect(request.getContextPath() + "/ApprovalErrorReports");
                return;
            }
            request.setAttribute("report", report);
            request.getRequestDispatcher("/WEB-INF/views/Approval/approval_error_detail.jsp")
                    .forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User approver = (User) request.getSession().getAttribute("user");
        if (approver == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String reportIdStr = request.getParameter("reportId");
        String decision    = request.getParameter("decision"); // "APPROVE" or "REJECT"
        String note        = request.getParameter("note");

        if (reportIdStr == null || decision == null || decision.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/ApprovalErrorReports");
            return;
        }

        try {
            int reportId = Integer.parseInt(reportIdStr);

            // 1. Load report để lấy softwareId, tên phần mềm, tên người báo cáo
            ReportDao reportDao = new ReportDao();
            Report report = reportDao.getErrorApprovalReportById(reportId);
            if (report == null) {
                response.sendRedirect(request.getContextPath() + "/ApprovalErrorReports");
                return;
            }

            int softwareId = report.getSoftwareId();
            String appName = report.getSoftwareName() != null
                    ? report.getSoftwareName()
                    : ("Software #" + softwareId);

            // 2. Lấy vendorId trực tiếp từ bảng Software
            Integer vendorId = reportDao.getVendorIdBySoftwareId(softwareId);

            // 3. Xử lý quyết định (DB transaction)
            reportDao.processErrorReport(reportId, softwareId, decision);

            // 4. Gửi notification cho vendor
            if (vendorId != null) {
                NotificationDao notifDao = new NotificationDao();
                String title, content, type, priority;

                if ("APPROVE".equalsIgnoreCase(decision)) {
                    title   = "Phần mềm của bạn đã bị gỡ khỏi thị trường – " + appName;
                    content = "Báo cáo lỗi về phần mềm \"" + appName + "\" đã được xác nhận bởi bộ phận phê duyệt. "
                            + "Phần mềm của bạn đã bị tạm ngừng hiển thị trên thị trường."
                            + (note != null && !note.isBlank() ? " Ghi chú: " + note : "");
                    type     = "ERROR_APPROVED";
                    priority = "HIGH";
                } else {
                    title   = "Báo cáo lỗi về phần mềm của bạn đã bị từ chối – " + appName;
                    content = "Báo cáo lỗi về phần mềm \"" + appName + "\" đã được xem xét và không có đủ căn cứ. "
                            + "Phần mềm của bạn vẫn tiếp tục hoạt động bình thường trên thị trường."
                            + (note != null && !note.isBlank() ? " Ghi chú: " + note : "");
                    type     = "ERROR_REJECTED";
                    priority = "MEDIUM";
                }

                notifDao.insertNotification(
                        vendorId,
                        title,
                        content,
                        type,
                        priority,
                        request.getContextPath() + "/vendor_software_detail?id=" + softwareId
                );
            }

            response.sendRedirect(request.getContextPath() + "/ApprovalErrorReports");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }


    @Override
    public String getServletInfo() {
        return "Approval Error Report Detail";
    }
}
