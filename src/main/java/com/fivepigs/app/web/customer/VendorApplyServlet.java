package com.fivepigs.app.web.customer;

import com.fivepigs.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "VendorApplyServlet", urlPatterns = {"/vendor-apply"})
public class VendorApplyServlet extends HttpServlet {

    private static final double VENDOR_APPLY_FEE_USD = 5.0;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User sessionUser = resolveSessionUser(session);
        if (sessionUser == null || sessionUser.getEmail() == null || sessionUser.getEmail().isBlank()) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/vendor-apply");
            return;
        }

        String roleName = session == null ? null : (String) session.getAttribute("roleName");
        if (roleName != null && roleName.equalsIgnoreCase("vendor")) {
            response.sendRedirect(request.getContextPath() + "/vendor/dashboard");
            return;
        }

        request.setAttribute("activePage", "vendorApply");
        request.setAttribute("vendorApplyFee", VENDOR_APPLY_FEE_USD);
        request.getRequestDispatcher("/WEB-INF/views/customer/vendor-apply.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User sessionUser = resolveSessionUser(session);
        if (sessionUser == null || sessionUser.getEmail() == null || sessionUser.getEmail().isBlank()) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/vendor-apply");
            return;
        }

        String roleName = session == null ? null : (String) session.getAttribute("roleName");
        if (roleName != null && roleName.equalsIgnoreCase("vendor")) {
            response.sendRedirect(request.getContextPath() + "/vendor/dashboard");
            return;
        }

        String agreePolicy = request.getParameter("agreePolicy");
        if (!"true".equalsIgnoreCase(agreePolicy)) {
            response.sendRedirect(request.getContextPath() + "/vendor-apply?error=must_agree");
            return;
        }

        HttpSession activeSession = request.getSession();
        activeSession.setAttribute("vendor_apply_pending_user_id", sessionUser.getUserId());
        activeSession.setAttribute("vendor_apply_total", VENDOR_APPLY_FEE_USD);
        response.sendRedirect(request.getContextPath() + "/create-payment");
    }

    private User resolveSessionUser(HttpSession session) {
        if (session == null) return null;
        return (User) session.getAttribute("user");
    }
}
