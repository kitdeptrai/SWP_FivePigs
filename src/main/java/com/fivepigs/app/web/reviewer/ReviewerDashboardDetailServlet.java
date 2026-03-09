package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.dao.ReviewScoreDao;
import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "ReviewerDashboardDetailServlet", urlPatterns = {"/reviewer_dashboard_detail"})
public class ReviewerDashboardDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            User user = (User) session.getAttribute("user");
            int reviewerId = user.getUserId();

            SoftwareDao softwareDao = new SoftwareDao();
            ReviewScoreDao reviewScoreDao = new ReviewScoreDao();

            int inReviewCount = softwareDao.countPendingReviewSoftware();
            int reviewedCount = reviewScoreDao.countReviewsByReviewer(reviewerId);

            request.setAttribute("user", user);
            request.setAttribute("inReviewCount", inReviewCount);
            request.setAttribute("reviewedCount", reviewedCount);

            request.getRequestDispatcher("/WEB-INF/views/reviewer/reviewer_dashboard_detail.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/reviewer_dashboard");
        }
    }
}