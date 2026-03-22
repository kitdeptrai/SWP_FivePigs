package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.dao.NotificationDao;
import com.fivepigs.app.model.Notification;
import com.fivepigs.app.model.User;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ReviewerNotificationsServlet", urlPatterns = {"/reviewer_notifications"})
public class ReviewerNotificationsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Chỉ reviewer mới được vào trang này
        if (!Integer.valueOf(5).equals(user.getRoleId())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        int userId = user.getUserId();

        NotificationDao dao = new NotificationDao();
        List<Notification> list = dao.getByUser(userId);

        int totalCount = list.size();
        int unreadCount = 0;
        int highCount = 0;

        for (Notification n : list) {
            if (!n.isRead()) {
                unreadCount++;
            }

            String priority = n.getPriority();
            if (priority != null &&
                (priority.equalsIgnoreCase("HIGH") || priority.equalsIgnoreCase("CRITICAL"))) {
                highCount++;
            }
        }

        request.setAttribute("notifications", list);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("unreadCount", unreadCount);
        request.setAttribute("highCount", highCount);

        request.getRequestDispatcher("/WEB-INF/views/reviewer/reviewer_notifications.jsp")
                .forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Reviewer Notifications Page";
    }
}