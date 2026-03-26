package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.dao.ReportDao;
import com.fivepigs.app.model.Report;
import com.fivepigs.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "ReviewerErrorReportDetailServlet", urlPatterns = {"/reviewer_error_report_detail"})
public class ReviewerErrorReportDetailServlet extends HttpServlet {

    private final ReportDao reportDao = new ReportDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String reportIdRaw = request.getParameter("reportId");
        if (reportIdRaw == null || reportIdRaw.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/reviewer_error_reports");
            return;
        }

        try {
            int reportId = Integer.parseInt(reportIdRaw);

            Report report = reportDao.getReportDetailForReviewer(reportId, user.getUserId());

            if (report == null) {
                response.sendRedirect(request.getContextPath() + "/reviewer_error_reports");
                return;
            }

            request.setAttribute("user", user);
            request.setAttribute("activeMenu", "errorReports");
            request.setAttribute("report", report);

            request.getRequestDispatcher("/WEB-INF/views/reviewer/reviewer_error_report_detail.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/reviewer_error_reports");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int reportId = Integer.parseInt(request.getParameter("reportId"));
            boolean bugConfirmed = "true".equals(request.getParameter("bugConfirmed"));
            String reviewerNote = request.getParameter("reviewerNote");
            String reproduceSteps = request.getParameter("reproduceSteps");

            reportDao.submitReviewerReportResult(
                    reportId,
                    user.getUserId(),
                    bugConfirmed,
                    reviewerNote,
                    reproduceSteps
            );

            response.sendRedirect(request.getContextPath() + "/reviewer_error_reports?msg=submitted");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}