package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.dao.ReviewGuidelineDao;
import com.fivepigs.app.model.ReviewGuideline;
import com.fivepigs.app.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ReviewerGuidelineUpdateServlet", urlPatterns = {"/reviewer_guideline_update"})
public class ReviewerGuidelineUpdateServlet extends HttpServlet {

    private final ReviewGuidelineDao guidelineDao = new ReviewGuidelineDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String guidelineIdRaw = trim(request.getParameter("guidelineId"));
        String category = trim(request.getParameter("category"));
        String priority = trim(request.getParameter("priority"));
        String title = trim(request.getParameter("title"));
        String description = trim(request.getParameter("description"));
        String icon = trim(request.getParameter("icon"));
        String color = trim(request.getParameter("color"));

        String keyword = trim(request.getParameter("keyword"));
        String currentCategory = trim(request.getParameter("currentCategory"));

        String[] itemTextsRaw = request.getParameterValues("itemText");
        List<String> itemTexts = normalizeItems(itemTextsRaw);

        List<String> errors = new ArrayList<>();

        int guidelineId = 0;
        try {
            guidelineId = Integer.parseInt(guidelineIdRaw);
            if (guidelineId <= 0) {
                errors.add("Guideline ID is invalid.");
            }
        } catch (Exception e) {
            errors.add("Guideline ID is invalid.");
        }

        if (isBlank(category)) {
            errors.add("Category is required.");
        }
        if (isBlank(priority)) {
            errors.add("Priority is required.");
        }
        if (isBlank(title)) {
            errors.add("Title is required.");
        }

        if (!isBlank(priority) && !isValidPriority(priority)) {
            errors.add("Priority is invalid.");
        }

        if (itemTexts.isEmpty()) {
            errors.add("At least one checklist item is required.");
        }

        if (!errors.isEmpty()) {
            request.setAttribute("errorMessages", errors);
            request.setAttribute("openModal", "edit");

            request.setAttribute("editGuidelineId", guidelineIdRaw);
            request.setAttribute("editCategory", category);
            request.setAttribute("editPriority", priority);
            request.setAttribute("editTitle", title);
            request.setAttribute("editDescription", description);
            request.setAttribute("editIcon", icon);
            request.setAttribute("editColor", color);
            request.setAttribute("editItems", itemTexts);

            request.setAttribute("keyword", keyword);
            request.setAttribute("category", currentCategory);

            request.getRequestDispatcher("/reviewer_guidelines").forward(request, response);
            return;
        }

        try {
            ReviewGuideline g = new ReviewGuideline();
            g.setGuidelineId(guidelineId);
            g.setCategory(category);
            g.setPriority(priority);
            g.setTitle(title);
            g.setDescription(description);
            g.setIcon(icon);
            g.setColor(color);

            guidelineDao.updateGuideline(g, itemTexts);

            String redirectUrl = request.getContextPath() + "/reviewer_guidelines";
            String query = buildQuery(keyword, currentCategory);
            response.sendRedirect(redirectUrl + query);

        } catch (Exception e) {
            e.printStackTrace();

            errors.add("Failed to update guideline. Please try again.");

            request.setAttribute("errorMessages", errors);
            request.setAttribute("openModal", "edit");

            request.setAttribute("editGuidelineId", guidelineIdRaw);
            request.setAttribute("editCategory", category);
            request.setAttribute("editPriority", priority);
            request.setAttribute("editTitle", title);
            request.setAttribute("editDescription", description);
            request.setAttribute("editIcon", icon);
            request.setAttribute("editColor", color);
            request.setAttribute("editItems", itemTexts);

            request.setAttribute("keyword", keyword);
            request.setAttribute("category", currentCategory);

            request.getRequestDispatcher("/reviewer_guidelines").forward(request, response);
        }
    }

    private List<String> normalizeItems(String[] rawItems) {
        List<String> items = new ArrayList<>();
        if (rawItems == null) {
            return items;
        }

        for (String s : rawItems) {
            String v = trim(s);
            if (!v.isEmpty()) {
                items.add(v);
            }
        }
        return items;
    }

    private boolean isValidPriority(String priority) {
        return "Critical".equalsIgnoreCase(priority)
                || "High".equalsIgnoreCase(priority)
                || "Medium".equalsIgnoreCase(priority)
                || "Low".equalsIgnoreCase(priority);
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String buildQuery(String keyword, String category) {
        List<String> params = new ArrayList<>();

        if (!isBlank(keyword)) {
            params.add("keyword=" + encode(keyword));
        }
        if (!isBlank(category)) {
            params.add("category=" + encode(category));
        }

        if (params.isEmpty()) {
            return "";
        }
        return "?" + String.join("&", params);
    }

    private String encode(String value) {
        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }
}