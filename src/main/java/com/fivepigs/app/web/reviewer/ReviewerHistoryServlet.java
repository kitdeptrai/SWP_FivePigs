package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.dao.ReviewHistoryDao;
import com.fivepigs.app.model.ReviewHistoryDTO;
import com.fivepigs.app.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/reviewer_history")
public class ReviewerHistoryServlet extends HttpServlet {

    private static final int PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        int reviewerId = user.getUserId();

        int currentPage = 1;
        String pageParam = request.getParameter("page");

        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) {
                    currentPage = 1;
                }
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        String keyword = request.getParameter("keyword");
        String decision = request.getParameter("decision");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");

        if (keyword == null) keyword = "";
        if (decision == null || decision.trim().isEmpty()) decision = "all";
        if (fromDate == null) fromDate = "";
        if (toDate == null) toDate = "";

        keyword = keyword.trim();
        decision = decision.trim();

        ReviewHistoryDao dao = new ReviewHistoryDao();

        int totalRecords = dao.countFilteredHistory(reviewerId, keyword, decision, fromDate, toDate);

        int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
        if (totalPages == 0) {
            totalPages = 1;
        }

        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        int offset = (currentPage - 1) * PAGE_SIZE;

        List<ReviewHistoryDTO> historyList = dao.getFilteredHistoryByReviewer(
                reviewerId, keyword, decision, fromDate, toDate, offset, PAGE_SIZE
        );

        request.setAttribute("user", user);
        request.setAttribute("historyList", historyList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        request.setAttribute("keyword", keyword);
        request.setAttribute("decision", decision);
        request.setAttribute("fromDate", fromDate);
        request.setAttribute("toDate", toDate);

        request.getRequestDispatcher("/WEB-INF/views/reviewer/reviewer_history.jsp")
                .forward(request, response);
    }
}