package com.fivepigs.app.web.admin;

import com.fivepigs.app.dao.NotificationDao;
import com.fivepigs.app.model.Notification;
import com.fivepigs.app.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminNotificationsServlet", urlPatterns = { "/admin_notifications" })
public class AdminNotificationsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRoleName())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        NotificationDao dao = new NotificationDao();
        int userId = user.getUserId();

        String readFilter = request.getParameter("read") != null ? request.getParameter("read") : "all";
        String typeFilter = request.getParameter("type") != null ? request.getParameter("type") : "all";
        String keyword = request.getParameter("keyword") != null ? request.getParameter("keyword") : "";

        List<Notification> notifications = dao.filterByUser(userId, readFilter, typeFilter, keyword);
        int totalCount = dao.countByUser(userId);
        int unreadCount = dao.countUnreadByUser(userId);
        int highCount = dao.countHighPriorityByUser(userId);

        request.setAttribute("notifications", notifications);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("unreadCount", unreadCount);
        request.setAttribute("highCount", highCount);
        request.setAttribute("selectedRead", readFilter);
        request.setAttribute("selectedType", typeFilter);
        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("/WEB-INF/views/admin/notifications.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRoleName())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        NotificationDao dao = new NotificationDao();
        int userId = user.getUserId();
        String action = request.getParameter("action");

        if ("toggle".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.toggleRead(id);
        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.delete(id);
        } else if ("markAllRead".equals(action)) {
            dao.markAllRead(userId);
        } else if ("deleteAll".equals(action)) {
            dao.deleteAll(userId);
        }

        response.sendRedirect(request.getContextPath() + "/admin_notifications");
    }
}
