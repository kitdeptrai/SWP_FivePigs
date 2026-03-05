package com.fivepigs.app.controller.reviewer;

import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ReviewerMyReviewsServlet", urlPatterns = {"/reviewer_my_reviews"})
public class ReviewerMyReviewsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            SoftwareDao dao = new SoftwareDao();

            // ✅ My Reviews = Assigned but NOT reviewed yet
            List<Software> myReviewList = dao.getMyAssignedPendingReviews(user.getUserId());

            request.setAttribute("myReviewList", myReviewList);
            request.setAttribute("user", user);

            request.getRequestDispatcher("/WEB-INF/views/reviewer/reviewer_my_reviews.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(500);
        }
    }
}