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

        // 1️⃣ Kiểm tra session
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        int reviewerId = user.getUserId();
        ReviewHistoryDao dao = new ReviewHistoryDao();
        // 2️⃣ Lấy page từ request
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
        if (keyword != null) {
            keyword = keyword.trim();
        }

       
        int totalRecords;
        List<ReviewHistoryDTO> historyList;

        if (keyword != null && !keyword.isEmpty()) {
            totalRecords = dao.countSearchHistory(reviewerId, keyword);
        } else {
            totalRecords = dao.countHistory(reviewerId);
        }

        int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
        if (totalPages == 0) {
            totalPages = 1;
        }

        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        int offset = (currentPage - 1) * PAGE_SIZE;

        if (keyword != null && !keyword.isEmpty()) {
            historyList = dao.searchHistoryByReviewer(reviewerId, keyword, offset, PAGE_SIZE);
        } else {
            historyList = dao.getHistoryByReviewer(reviewerId, offset, PAGE_SIZE);
        }

        // 7️⃣ Set attribute
        request.setAttribute("user", user);
        request.setAttribute("historyList", historyList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("keyword", keyword);

        // 8️⃣ Forward
        request.getRequestDispatcher(
                "/WEB-INF/views/reviewer/reviewer_history.jsp")
                .forward(request, response);
    }
}
