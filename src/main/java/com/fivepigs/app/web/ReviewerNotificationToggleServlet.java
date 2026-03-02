package com.fivepigs.app.web;

import com.fivepigs.app.dao.NotificationDao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ReviewerNotificationToggleServlet", urlPatterns = {"/reviewer_notification_toggle"})
public class ReviewerNotificationToggleServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String idRaw = request.getParameter("id");
        int id = -1;
        try { id = Integer.parseInt(idRaw); } catch (Exception ignored) {}

        if (id > 0) {
            new NotificationDao().toggleRead(id);
        }

        response.sendRedirect(request.getContextPath() + "/reviewer_notifications");
    }
}