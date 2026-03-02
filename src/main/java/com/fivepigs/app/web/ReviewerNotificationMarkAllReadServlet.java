package com.fivepigs.app.web;

import com.fivepigs.app.dao.NotificationDao;
import com.fivepigs.app.model.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ReviewerNotificationMarkAllReadServlet", urlPatterns = {"/reviewer_notification_mark_all_read"})
public class ReviewerNotificationMarkAllReadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        new NotificationDao().markAllRead(user.getUserId());

        response.sendRedirect(request.getContextPath() + "/reviewer_notifications");
    }
}