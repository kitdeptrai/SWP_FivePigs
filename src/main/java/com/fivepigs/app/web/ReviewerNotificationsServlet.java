package com.fivepigs.app.web;

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

        NotificationDao dao = new NotificationDao();
        List<Notification> list = dao.getByUser(userId);

        int total = list.size();
        int unread = 0;
        int high = 0;

        for (Notification n : list) {
            if (!n.isRead()) unread++;

            String p = n.getPriority();
            if (p != null && (p.equalsIgnoreCase("High") || p.equalsIgnoreCase("Critical"))) {
                high++;
            }
        }

        request.setAttribute("notifications", list);
        request.setAttribute("totalCount", total);
        request.setAttribute("unreadCount", unread);
        request.setAttribute("highCount", high);

        request.getRequestDispatcher("/WEB-INF/views/reviewer/reviewer_notifications.jsp")
                .forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Reviewer Notifications Page";
    }
}