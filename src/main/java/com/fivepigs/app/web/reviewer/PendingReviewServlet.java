package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.dao.CategoryDao;
import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.Category;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "PendingReviewServlet", urlPatterns = {"/reviewer_pending"})
public class PendingReviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int reviewerId = user.getUserId();

            String keyword = request.getParameter("keyword");
            String categoryIdRaw = request.getParameter("categoryId");
            String priceType = request.getParameter("priceType");

            if (keyword == null) {
                keyword = "";
            } else {
                keyword = keyword.trim();
            }

            Integer categoryId = null;
            if (categoryIdRaw != null && !categoryIdRaw.trim().isEmpty()) {
                try {
                    categoryId = Integer.parseInt(categoryIdRaw);
                } catch (NumberFormatException e) {
                    categoryId = null;
                }
            }

            if (priceType == null || priceType.trim().isEmpty()) {
                priceType = "all";
            }

            SoftwareDao softwareDao = new SoftwareDao();
            CategoryDao categoryDao = new CategoryDao();

            List<Software> list = softwareDao.filterMyAssignedPendingReviews(
                    reviewerId, keyword, categoryId, priceType
            );

            List<Category> categories = categoryDao.GETALLCATEGORY();

            request.setAttribute("pendingList", list);
            request.setAttribute("categories", categories);
            request.setAttribute("selectedKeyword", keyword);
            request.setAttribute("selectedCategoryId", categoryId);
            request.setAttribute("selectedPriceType", priceType);
            request.setAttribute("user", user);

            request.getRequestDispatcher("/WEB-INF/views/reviewer/reviewer_pending.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(500);
        }
    }

    @Override
    public String getServletInfo() {
        return "Pending review page with filters";
    }
}
