package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.dao.ReviewScoreDao;
import com.fivepigs.app.model.ReviewScore;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "ReviewerHistoryDetailServlet", urlPatterns = {"/reviewer_history_detail"})
public class ReviewerHistoryDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("reviewScoreId");
        if (idRaw == null || idRaw.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/reviewer_history");
            return;
        }

        try {
            int reviewScoreId = Integer.parseInt(idRaw);

            ReviewScoreDao dao = new ReviewScoreDao();
            ReviewScore review = dao.getReviewDetailById(reviewScoreId);

            if (review == null) {
                response.sendRedirect(request.getContextPath() + "/reviewer_history");
                return;
            }

            request.setAttribute("review", review);
            request.getRequestDispatcher("/WEB-INF/views/reviewer/reviewer_history_detail.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/reviewer_history");
        }
    }
}