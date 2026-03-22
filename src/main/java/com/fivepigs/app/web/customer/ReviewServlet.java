package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.ReviewDao;
import com.fivepigs.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ReviewServlet", urlPatterns = {"/review"})
public class ReviewServlet extends HttpServlet {

    private final ReviewDao reviewDao = new ReviewDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        Integer softwareId = parseInt(request.getParameter("softwareId"));
        Integer rating = parseInt(request.getParameter("rating"));
        String comment = param(request, "comment");

        if (softwareId == null) {
            response.sendRedirect(request.getContextPath() + "/customer_dashboard");
            return;
        }

        if (user == null || user.getUserId() == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/product?pid=" + softwareId);
            return;
        }

        if (rating == null || rating < 1 || rating > 5) {
            response.sendRedirect(request.getContextPath() + "/product?pid=" + softwareId + "&reviewMsg=invalid_rating");
            return;
        }

        if (comment == null || comment.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/product?pid=" + softwareId + "&reviewMsg=empty_comment");
            return;
        }

        try {
            if (!reviewDao.hasOwnedLicense(user.getUserId(), softwareId)) {
                response.sendRedirect(request.getContextPath() + "/product?pid=" + softwareId + "&reviewMsg=not_owned");
                return;
            }
            if (reviewDao.hasUserReviewed(user.getUserId(), softwareId)) {
                response.sendRedirect(request.getContextPath() + "/product?pid=" + softwareId + "&reviewMsg=exists");
                return;
            }

            reviewDao.addReview(user.getUserId(), softwareId, rating, comment);
            response.sendRedirect(request.getContextPath() + "/product?pid=" + softwareId + "&reviewMsg=added");
        } catch (SQLException e) {
            throw new ServletException("Unable to submit review", e);
        }
    }

    private Integer parseInt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String param(HttpServletRequest request, String key) {
        String value = request.getParameter(key);
        return value == null ? null : value.trim();
    }
}
