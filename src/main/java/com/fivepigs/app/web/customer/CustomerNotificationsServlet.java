package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.NotificationDao;
import com.fivepigs.app.model.Notification;
import com.fivepigs.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CustomerNotificationsServlet", urlPatterns = {"/notifications"})
public class CustomerNotificationsServlet extends HttpServlet {

    private final NotificationDao notificationDao = new NotificationDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = resolveSessionUser(request.getSession(false));
        if (user == null || user.getUserId() == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/notifications");
            return;
        }

        String selectedOrder = normalizeOrder(request.getParameter("order"));
        List<Notification> notifications = notificationDao.getByUser(user.getUserId(), selectedOrder);
        request.setAttribute("activePage", "notifications");
        request.setAttribute("notifications", notifications);
        request.setAttribute("selectedOrder", selectedOrder);
        request.getRequestDispatcher("/WEB-INF/views/customer/notifications.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = resolveSessionUser(request.getSession(false));
        if (user == null || user.getUserId() == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/notifications");
            return;
        }

        String action = request.getParameter("action");
        String selectedOrder = normalizeOrder(request.getParameter("order"));
        if ("markAllRead".equals(action)) {
            notificationDao.markAllRead(user.getUserId());

        } else if ("delete".equals(action)) {
            Integer notificationId = parseInt(request.getParameter("notificationId"));
            if (notificationId != null) {
                notificationDao.delete(notificationId);
            }
        } else if ("deleteAll".equals(action)) {
            notificationDao.deleteAll(user.getUserId());
        } else if("readmark".equals(action))
        response.sendRedirect(request.getContextPath() + "/notifications?order=" + selectedOrder);
    }

    private User resolveSessionUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (User) session.getAttribute("user");
    }

    private String normalizeOrder(String order) {
        return "asc".equalsIgnoreCase(order) ? "asc" : "desc";
    }

    private Integer parseInt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
