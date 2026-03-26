package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.dao.ReportDao;
import com.fivepigs.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "ReviewerErrorReportsServlet", urlPatterns = {"/reviewer_error_reports"})
public class ReviewerErrorReportsServlet extends HttpServlet {

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

        try {
            request.setAttribute("user", user);
            request.setAttribute("activeMenu", "errorReports");
            request.setAttribute("reportList", reportDao.getReportsForReviewer(user.getUserId()));
            request.getRequestDispatcher("/WEB-INF/views/reviewer/reviewer_error_reports.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}