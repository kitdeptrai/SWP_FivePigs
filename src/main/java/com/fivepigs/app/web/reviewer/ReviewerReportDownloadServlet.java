package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "ReviewerReportDownloadServlet", urlPatterns = {"/reviewer/report-download"})
public class ReviewerReportDownloadServlet extends HttpServlet {

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

        Integer softwareId = parseInt(request.getParameter("softwareId"));
        if (softwareId == null) {
            response.sendRedirect(request.getContextPath() + "/reviewer_error_reports");
            return;
        }

        try {
            if (!hasReviewerAccess(user.getUserId(), softwareId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            String fileUrl = softwareDao.getActiveFileUrlBySoftwareId(softwareId);
            if (fileUrl == null || fileUrl.isBlank()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No file found");
                return;
            }

            if (fileUrl.startsWith("http://") || fileUrl.startsWith("https://")) {
                response.sendRedirect(fileUrl);
                return;
            }

            String path = fileUrl.startsWith("/") ? fileUrl : "/" + fileUrl;
            InputStream in = request.getServletContext().getResourceAsStream(path);
            if (in == null && !path.startsWith("/assets/")) {
                in = request.getServletContext().getResourceAsStream("/assets/" + fileUrl);
            }

            if (in == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
                return;
            }

            String fileName = path.substring(path.lastIndexOf("/") + 1);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            try (InputStream stream = in; OutputStream out = response.getOutputStream()) {
                stream.transferTo(out);
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private boolean hasReviewerAccess(int reviewerId, int softwareId) throws Exception {
        String sql = """
            SELECT 1
            FROM Review_Score
            WHERE reviewer_id = ? AND software_id = ?
            LIMIT 1
        """;

        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, reviewerId);
            ps.setInt(2, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Integer parseInt(String value) {
        try {
            return value == null ? null : Integer.parseInt(value.trim());
        } catch (Exception e) {
            return null;
        }
    }
}