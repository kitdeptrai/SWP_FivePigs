package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.CartDao;
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

@WebServlet(name = "VerifyCheckoutOtpServlet", urlPatterns = {"/verify-checkout-otp"})
public class VerifyCheckoutOtpServlet extends HttpServlet {

    private static final int MAX_ATTEMPTS = 5;
    private static final long RESEND_COOLDOWN_MS = 60_000L;

    private final CartDao cartDao = new CartDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!hasPendingCheckout(request.getSession(false))) {
            response.sendRedirect(request.getContextPath() + "/cart?msg=empty");
            return;
        }
        request.setAttribute("activePage", "cart");
        request.getRequestDispatcher("/WEB-INF/views/customer/verify_checkout_otp.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!hasPendingCheckout(session)) {
            response.sendRedirect(request.getContextPath() + "/cart?msg=empty");
            return;
        }

        String action = trim(request.getParameter("action"));
        if ("resend".equals(action)) {
            handleResend(request, response, session);
            return;
        }

        handleVerify(request, response, session);
    }

    private void handleVerify(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {

        User user = (User) session.getAttribute("user");
        Integer pendingUserId = (Integer) session.getAttribute(CartServlet.CHECKOUT_PENDING_USER_ID);
        if (user == null || user.getUserId() == null || pendingUserId == null || !pendingUserId.equals(user.getUserId())) {
            clearCheckoutOtpSession(session);
            response.sendRedirect(request.getContextPath() + "/cart?msg=empty");
            return;
        }

        String inputOtp = trim(request.getParameter("otp"));
        String otpHash = (String) session.getAttribute(CartServlet.CHECKOUT_OTP_HASH);
        Long expire = (Long) session.getAttribute(CartServlet.CHECKOUT_OTP_EXPIRE);
        Integer attempts = (Integer) session.getAttribute(CartServlet.CHECKOUT_OTP_ATTEMPTS);

        if (attempts == null) {
            attempts = 0;
        }

        if (otpHash == null || expire == null || System.currentTimeMillis() > expire) {
            clearCheckoutOtpSession(session);
            request.setAttribute("error", "OTP expired. Please checkout again.");
            request.setAttribute("expired", true);
            request.setAttribute("activePage", "cart");
            request.getRequestDispatcher("/WEB-INF/views/customer/verify_checkout_otp.jsp").forward(request, response);
            return;
        }

        if (attempts >= MAX_ATTEMPTS) {
            clearCheckoutOtpSession(session);
            request.setAttribute("error", "Too many wrong attempts. Please checkout again.");
            request.setAttribute("locked", true);
            request.setAttribute("activePage", "cart");
            request.getRequestDispatcher("/WEB-INF/views/customer/verify_checkout_otp.jsp").forward(request, response);
            return;
        }

        if (inputOtp == null || !OtpUtil.hashOtp(inputOtp).equals(otpHash)) {
            attempts++;
            session.setAttribute(CartServlet.CHECKOUT_OTP_ATTEMPTS, attempts);
            request.setAttribute("error", "Incorrect OTP. Remaining attempts: " + Math.max(0, MAX_ATTEMPTS - attempts));
            request.setAttribute("activePage", "cart");
            request.getRequestDispatcher("/WEB-INF/views/customer/verify_checkout_otp.jsp").forward(request, response);
            return;
        }

        clearCheckoutOtpSession(session);

        response.sendRedirect(request.getContextPath() + "/create-payment");
    }

    private void handleResend(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {

        User user = (User) session.getAttribute("user");
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/cart");
            return;
        }

        Long lastSent = (Long) session.getAttribute(CartServlet.CHECKOUT_OTP_LAST_SENT);
        long now = System.currentTimeMillis();
        if (lastSent != null && now - lastSent < RESEND_COOLDOWN_MS) {
            long waitSec = (RESEND_COOLDOWN_MS - (now - lastSent) + 999) / 1000;
            request.setAttribute("error", "Please wait " + waitSec + "s before resending OTP.");
            request.setAttribute("activePage", "cart");
            request.getRequestDispatcher("/WEB-INF/views/customer/verify_checkout_otp.jsp").forward(request, response);
            return;
        }

        String otp = OtpUtil.generateOtp();
        session.setAttribute(CartServlet.CHECKOUT_OTP_HASH, OtpUtil.hashOtp(otp));
        session.setAttribute(CartServlet.CHECKOUT_OTP_EXPIRE, now + 5 * 60 * 1000L);
        session.setAttribute(CartServlet.CHECKOUT_OTP_ATTEMPTS, 0);
        session.setAttribute(CartServlet.CHECKOUT_PENDING_USER_ID, user.getUserId());
        session.setAttribute(CartServlet.CHECKOUT_OTP_LAST_SENT, now);

        String body = "<div style='font-family:Arial,sans-serif;max-width:540px;margin:0 auto;'>"
                + "<h2 style='color:#6b70ff'>Checkout Verification</h2>"
                + "<p>Your new OTP to confirm checkout:</p>"
                + "<h3 style='color:#0f172a;font-size:28px;letter-spacing:4px'><b>" + otp + "</b></h3>"
                + "<p>This OTP expires in <b>5 minutes</b>.</p>"
                + "</div>";

        new Thread(() -> EmailService.sendHtmlEmail(user.getEmail(), "FIVEPIGS - Checkout OTP", body)).start();

        request.setAttribute("message", "A new OTP has been sent to your email.");
        request.setAttribute("activePage", "cart");
        request.getRequestDispatcher("/WEB-INF/views/customer/verify_checkout_otp.jsp").forward(request, response);
    }

    private boolean hasPendingCheckout(HttpSession session) {
        if (session == null) {
            return false;
        }
        return session.getAttribute(CartServlet.CHECKOUT_OTP_HASH) != null
                && session.getAttribute(CartServlet.CHECKOUT_OTP_EXPIRE) != null
                && session.getAttribute(CartServlet.CHECKOUT_PENDING_USER_ID) != null;
    }

    private void clearCheckoutOtpSession(HttpSession session) {
        if (session == null) {
            return;
        }
        session.removeAttribute(CartServlet.CHECKOUT_OTP_HASH);
        session.removeAttribute(CartServlet.CHECKOUT_OTP_EXPIRE);
        session.removeAttribute(CartServlet.CHECKOUT_OTP_ATTEMPTS);
        session.removeAttribute(CartServlet.CHECKOUT_PENDING_USER_ID);
        session.removeAttribute(CartServlet.CHECKOUT_OTP_LAST_SENT);
    }

    private String trim(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
