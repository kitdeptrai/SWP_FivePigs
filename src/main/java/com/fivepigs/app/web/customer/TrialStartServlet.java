package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.LicenseDao;
import com.fivepigs.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet(name = "TrialStartServlet", urlPatterns = {"/trial/start"})
public class TrialStartServlet extends HttpServlet {

    private final LicenseDao licenseDao = new LicenseDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer softwareId = parseInt(request.getParameter("softwareId"));
        if (softwareId == null) {
            response.sendRedirect(request.getContextPath() + "/customer_dashboard");
            return;
        }

        Integer userId = resolveUserId(request.getSession(false));
        if (userId == null) {
            String redirectTarget = request.getContextPath() + "/product?pid=" + softwareId;
            response.sendRedirect(request.getContextPath() + "/login?redirect=" +
                    URLEncoder.encode(redirectTarget, StandardCharsets.UTF_8));
            return;
        }

        try {
            String result = licenseDao.startDemoTrial(userId, softwareId);
            if ("started".equals(result)) {
                response.sendRedirect(request.getContextPath() + "/library?msg=trial_started");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/product?pid=" + softwareId + "&demoMsg=" + result);
        } catch (SQLException e) {
            throw new ServletException("Unable to start demo trial", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/customer_dashboard");
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
        if (userIdAttr instanceof Integer integerValue) {
            return integerValue;
        }
        if (userIdAttr instanceof String stringValue) {
            return parseInt(stringValue);
        }
        return null;
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
