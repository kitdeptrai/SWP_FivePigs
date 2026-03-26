package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.CartDao;
import com.fivepigs.app.dao.NotificationDao;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    private final CartDao cartDao = new CartDao();
    private final NotificationDao notificationDao = new NotificationDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = resolveUserId(request.getSession(false));
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/cart");
            return;
        }

        try {
            List<Software> cartItems = cartDao.getCartItems(userId);
            double total = cartDao.getCartTotal(userId);

            request.setAttribute("activePage", "cart");
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("cartTotal", total);
            request.setAttribute("cartCount", cartItems.size());
            request.getRequestDispatcher("/WEB-INF/views/customer/cart.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer userId = resolveUserId(session);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/cart");
            return;
        }

        String action = param(request, "action");
        String redirect = param(request, "redirect");
        if (redirect == null || redirect.isBlank()) {
            redirect = request.getContextPath() + "/cart";
        }

        try {
            if ("add".equals(action)) {
                Integer softwareId = parseInt(param(request, "softwareId"));
                if (softwareId == null) {
                    softwareId = parseInt(param(request, "appId"));
                }

                if (softwareId != null) {
                    boolean added = cartDao.addToCart(userId, softwareId);
                    redirect = appendMsg(redirect, added ? "added" : "exists");
                }
                response.sendRedirect(redirect);
                return;
            }

            if ("remove".equals(action)) {
                Integer softwareId = parseInt(param(request, "softwareId"));
                if (softwareId != null) {
                    cartDao.removeFromCart(userId, softwareId);
                }
                response.sendRedirect(request.getContextPath() + "/cart?msg=removed");
                return;
            }

            if ("checkout".equals(action)) {
                List<Software> cartItems = cartDao.getCartItems(userId);
                if (cartItems == null || cartItems.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/cart?msg=empty");
                    return;
                }


                double total = cartDao.getCartTotal(userId);
                if (total <= 0) {
                    int count = cartDao.checkout(userId);
                    if (count > 0) {

                        notificationDao.insertNotification(
                                userId,
                                count + " free item(s) added to your Library",
                                "Purchase completed. Your free items are now available in Library."
                        );
                    }
                    response.sendRedirect(request.getContextPath() + "/library?msg=checkout_success");
                    return;
                }

                session.setAttribute("checkout_total", total);
                session.setAttribute("checkout_pending_user_id", userId);
                response.sendRedirect(request.getContextPath() + "/create-payment");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/cart");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private Integer resolveUserId(HttpSession session) {
        if (session == null) {
            return null;
        }
        User user = (User) session.getAttribute("user");
        if (user != null && user.getUserId() != null) {
            return user.getUserId();
        }

        Object userIdAttr = session.getAttribute("userId");
        if (userIdAttr instanceof Integer) {
            return (Integer) userIdAttr;
        }
        if (userIdAttr instanceof String) {
            return parseInt((String) userIdAttr);
        }
        return null;
    }

    private String appendMsg(String url, String msg) {
        return url + (url.contains("?") ? "&" : "?") + "msg=" + msg;
    }

    private String param(HttpServletRequest request, String key) {
        String value = request.getParameter(key);
        return value == null ? null : value.trim();
    }

    private Integer parseInt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
