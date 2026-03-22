package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.dao.ReviewGuidelineDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ReviewerGuidelinesServlet", urlPatterns = {"/reviewer_guidelines"})
public class ReviewerGuidelinesServlet extends HttpServlet {

    private final ReviewGuidelineDao guidelineDao = new ReviewGuidelineDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        response.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("keyword");
        String category = request.getParameter("category"); // "Security" / "" / null

        request.setAttribute("keyword", keyword == null ? "" : keyword.trim());
        request.setAttribute("category", category == null ? "" : category.trim());

        request.setAttribute("categories", guidelineDao.getCategories());
        request.setAttribute("stats", guidelineDao.getStats(category));
        request.setAttribute("guidelines", guidelineDao.findAll(keyword, category));

        request.getRequestDispatcher("/WEB-INF/views/reviewer/reviewer_guidelines.jsp")
               .forward(request, response);
    }
}