package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@WebServlet(name = "ReviewerSoftwareDownloadServlet", urlPatterns = {"/reviewer/software-download"})
public class ReviewerSoftwareDownloadServlet extends HttpServlet {

    private final SoftwareDao softwareDao = new SoftwareDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idRaw = request.getParameter("softwareId");
        if (idRaw == null || idRaw.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/reviewer_pending");
            return;
        }

        try {
            int softwareId = Integer.parseInt(idRaw);

            String fileUrl = softwareDao.getActiveFileUrlBySoftwareId(softwareId);

            if (fileUrl == null || fileUrl.isBlank()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No software file found.");
                return;
            }

            // Nếu file là external link
            if (fileUrl.startsWith("http://") || fileUrl.startsWith("https://")) {
                response.sendRedirect(fileUrl);
                return;
            }

            String path = fileUrl.startsWith("/") ? fileUrl : "/" + fileUrl;

            InputStream in = request.getServletContext().getResourceAsStream(path);

            if (in == null && !path.startsWith("/uploads/")) {
                in = request.getServletContext().getResourceAsStream("/uploads/" + fileUrl);
            }

            if (in == null && !path.startsWith("/assets/")) {
                in = request.getServletContext().getResourceAsStream("/assets/" + fileUrl);
            }

            if (in == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found in server.");
                return;
            }

            String fileName = path.substring(path.lastIndexOf("/") + 1);

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            try (InputStream input = in; OutputStream out = response.getOutputStream()) {
                input.transferTo(out);
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/reviewer_pending");
        } catch (Exception e) {
            throw new ServletException("Unable to download software file", e);
        }
    }
}
