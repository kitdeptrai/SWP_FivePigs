package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.dao.ReviewGuidelineDao;
import com.fivepigs.app.model.ReviewGuideline;
import com.fivepigs.app.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "ReviewerGuidelineCreateServlet", urlPatterns = {"/reviewer_guideline_create"})
public class ReviewerGuidelineCreateServlet extends HttpServlet {

    private final ReviewGuidelineDao dao = new ReviewGuidelineDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // lấy user từ session
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        Integer createdBy = (user != null) ? user.getUserId() : null;

        String category = request.getParameter("category");
        String priority = request.getParameter("priority");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String icon = request.getParameter("icon");
        String color = request.getParameter("color");

        // checklist items
        String[] itemTextsArr = request.getParameterValues("itemText");
        List<String> itemTexts = new ArrayList<>();
        if (itemTextsArr != null) itemTexts.addAll(Arrays.asList(itemTextsArr));

        ReviewGuideline g = new ReviewGuideline();
        g.setCategory(category);
        g.setPriority(priority);
        g.setTitle(title);
        g.setDescription(description);
        g.setIcon(icon);
        g.setColor(color);
        g.setCreatedBy(createdBy);

        dao.createGuideline(g, itemTexts);

        response.sendRedirect(request.getContextPath() + "/reviewer_guidelines");
    }
}