package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.dao.NotificationDao;
import com.fivepigs.app.model.Notification;
import com.fivepigs.app.model.User;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(name = "ReviewerNotificationsServlet", urlPatterns = {"/reviewer_notifications"})
public class ReviewerNotificationsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = user.getUserId();

        String read = request.getParameter("read");
        String type = request.getParameter("type");
        String keyword = request.getParameter("keyword");

        if (read == null || read.isBlank()) read = "all";
        if (type == null || type.isBlank()) type = "all";
        if (keyword == null) keyword = "";

        NotificationDao dao = new NotificationDao();

        List<Notification> list = dao.filterByUser(userId, read, type, keyword);

        int total = dao.countByUser(userId);
        int unread = dao.countUnreadByUser(userId);
        int high = dao.countHighPriorityByUser(userId);

        int countSubmitted = dao.countByType(userId, "SUBMITTED");
        int countApproved = dao.countByType(userId, "APPROVED");
        int countRejected = dao.countByType(userId, "REJECTED");
        int countPendingApproval = dao.countByType(userId, "PENDING_APPROVAL");

        request.setAttribute("notifications", list);
        request.setAttribute("totalCount", total);
        request.setAttribute("unreadCount", unread);
        request.setAttribute("highCount", high);

        request.setAttribute("countSubmitted", countSubmitted);
        request.setAttribute("countApproved", countApproved);
        request.setAttribute("countRejected", countRejected);
        request.setAttribute("countPendingApproval", countPendingApproval);

        request.setAttribute("selectedRead", read);
        request.setAttribute("selectedType", type);
        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("/WEB-INF/views/reviewer/reviewer_notifications.jsp")
                .forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Reviewer Notifications Page";
    }
}