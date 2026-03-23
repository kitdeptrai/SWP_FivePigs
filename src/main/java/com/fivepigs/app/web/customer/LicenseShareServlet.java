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
import java.sql.SQLException;

@WebServlet(name = "LicenseShareServlet", urlPatterns = {"/license/share"})
public class LicenseShareServlet extends HttpServlet {

    private final LicenseDao licenseDao = new LicenseDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer ownerId = resolveUserId(request.getSession(false));
        if (ownerId == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/library");
            return;
        }

        Integer softwareId = parseInt(request.getParameter("softwareId"));
        String email = request.getParameter("shareEmail");
        if (softwareId == null) {
            response.sendRedirect(request.getContextPath() + "/library?shareMsg=invalid_software");
            return;
        }
        if (email == null || email.trim().isBlank()) {
            response.sendRedirect(request.getContextPath() + "/library?shareMsg=invalid_email&shareSoftwareId=" + softwareId);
            return;
        }

        try {
            String result = licenseDao.shareOwnedLicenseByEmail(ownerId, softwareId, email.trim());
            response.sendRedirect(request.getContextPath() + "/library?shareMsg=" + result + "&shareSoftwareId=" + softwareId);
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
        if (userIdAttr instanceof Integer integer) {
            return integer;
        }
        if (userIdAttr instanceof String value) {
            return parseInt(value);
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
