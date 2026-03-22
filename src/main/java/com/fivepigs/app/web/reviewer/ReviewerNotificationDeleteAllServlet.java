package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.dao.NotificationDao;
import com.fivepigs.app.model.User;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ReviewerNotificationDeleteAllServlet", urlPatterns = {"/reviewer_notification_delete_all"})
public class ReviewerNotificationDeleteAllServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Chỉ reviewer mới được thao tác
        if (!Integer.valueOf(5).equals(user.getRoleId())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        NotificationDao dao = new NotificationDao();
        dao.deleteAll(user.getUserId());

        response.sendRedirect(request.getContextPath() + "/reviewer_notifications");
    }
}