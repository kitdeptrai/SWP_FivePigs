package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.CartDao;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.User;
import com.fivepigs.app.util.EmailService;
import com.fivepigs.app.util.OtpUtil;
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

    public static final String CHECKOUT_OTP_HASH = "checkout_otp_hash";
    public static final String CHECKOUT_OTP_EXPIRE = "checkout_otp_expire";
    public static final String CHECKOUT_OTP_ATTEMPTS = "checkout_otp_attempts";
    public static final String CHECKOUT_PENDING_USER_ID = "checkout_pending_user_id";
    public static final String CHECKOUT_OTP_LAST_SENT = "checkout_otp_last_sent";

    private static final long CHECKOUT_OTP_TTL_MS = 5 * 60 * 1000L;

    private final CartDao cartDao = new CartDao();

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
                session.setAttribute("checkout_total", total);

                User user = resolveUser(session);
                if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
                    response.sendRedirect(request.getContextPath() + "/login?redirect=/cart");
                    return;
                }

                issueCheckoutOtp(session, user);
                response.sendRedirect(request.getContextPath() + "/verify-checkout-otp?sent=1");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/cart");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void issueCheckoutOtp(HttpSession session, User user) {
        String otp = OtpUtil.generateOtp();
        session.setAttribute(CHECKOUT_OTP_HASH, OtpUtil.hashOtp(otp));
        session.setAttribute(CHECKOUT_OTP_EXPIRE, System.currentTimeMillis() + CHECKOUT_OTP_TTL_MS);
        session.setAttribute(CHECKOUT_OTP_ATTEMPTS, 0);
        session.setAttribute(CHECKOUT_PENDING_USER_ID, user.getUserId());
        session.setAttribute(CHECKOUT_OTP_LAST_SENT, System.currentTimeMillis());

        String body = "<div style='font-family:Arial,sans-serif;max-width:540px;margin:0 auto;'>"
                + "<h2 style='color:#6b70ff'>Checkout Verification</h2>"
                + "<p>Use OTP below to confirm your purchase on FIVEPIGS:</p>"
                + "<h3 style='color:#0f172a;font-size:28px;letter-spacing:4px'><b>" + otp + "</b></h3>"
                + "<p>This OTP expires in <b>5 minutes</b>.</p>"
                + "</div>";

        new Thread(() -> EmailService.sendHtmlEmail(user.getEmail(), "FIVEPIGS - Checkout OTP", body)).start();
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

    private User resolveUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (User) session.getAttribute("user");
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
