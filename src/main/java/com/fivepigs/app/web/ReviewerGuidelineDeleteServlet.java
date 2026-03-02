package com.fivepigs.app.web;

import com.fivepigs.app.dao.ReviewGuidelineDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ReviewerGuidelineDeleteServlet", urlPatterns = {"/reviewer_guideline_delete"})
public class ReviewerGuidelineDeleteServlet extends HttpServlet {

    private final ReviewGuidelineDao guidelineDao = new ReviewGuidelineDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String idRaw = request.getParameter("guidelineId");
        int guidelineId = -1;
        try { guidelineId = Integer.parseInt(idRaw); } catch (Exception ignored) {}

        if (guidelineId > 0) {
            guidelineDao.deleteGuideline(guidelineId);
        }

        response.sendRedirect(request.getContextPath() + "/reviewer_guidelines");
    }
}