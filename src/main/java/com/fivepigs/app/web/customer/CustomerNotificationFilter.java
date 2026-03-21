package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.NotificationDao;
import com.fivepigs.app.model.Notification;
import com.fivepigs.app.model.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebFilter(urlPatterns = {
        "/customer_dashboard",
        "/library",
        "/cart",
        "/product",
        "/search",
        "/app",
        "/game",
        "/aboutus",
        "/news",
        "/profile",
        "/settings",
        "/verify-checkout-otp",
        "/notifications"
})
public class CustomerNotificationFilter implements Filter {

    private final NotificationDao notificationDao = new NotificationDao();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest) {
            HttpSession session = httpRequest.getSession(false);
            User user = session == null ? null : (User) session.getAttribute("user");

            int unreadCount = 0;
            List<Notification> topNotifications = Collections.emptyList();
            if (user != null && user.getUserId() != null) {
                unreadCount = notificationDao.countUnreadByUser(user.getUserId());
                topNotifications = notificationDao.getTopByUser(user.getUserId(), 5);
            }

            request.setAttribute("unreadCount", unreadCount);
            request.setAttribute("topNotifications", topNotifications);
        }

        chain.doFilter(request, response);
    }
}
