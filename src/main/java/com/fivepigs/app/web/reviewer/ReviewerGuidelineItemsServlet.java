package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.dao.ReviewGuidelineDao;
import com.fivepigs.app.model.ReviewGuidelineItem;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ReviewerGuidelineItemsServlet", urlPatterns = {"/reviewer_guideline_items"})
public class ReviewerGuidelineItemsServlet extends HttpServlet {

    private final ReviewGuidelineDao dao = new ReviewGuidelineDao();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String idStr = request.getParameter("guidelineId");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing guidelineId");
            return;
        }

        int guidelineId = Integer.parseInt(idStr);

        List<ReviewGuidelineItem> items = dao.getItemsByGuidelineId(guidelineId);
        response.getWriter().write(gson.toJson(items));
    }
}