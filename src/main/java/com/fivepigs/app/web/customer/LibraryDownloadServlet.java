package com.fivepigs.app.web.customer;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.dao.UserSoftwareStateDao;
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
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "LibraryDownloadServlet", urlPatterns = {"/library/download"})
public class LibraryDownloadServlet extends HttpServlet {

    private final SoftwareDao softwareDao = new SoftwareDao();
    private final UserSoftwareStateDao stateDao = new UserSoftwareStateDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = resolveUserId(request.getSession(false));
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/library");
            return;
        }

        Integer softwareId = parseInt(request.getParameter("softwareId"));
        if (softwareId == null) {
            response.sendRedirect(request.getContextPath() + "/library?msg=invalid_download");
            return;
        }

        try {
            if (!hasOwnedLicense(userId, softwareId)) {
                response.sendRedirect(request.getContextPath() + "/library?msg=not_owned");
                return;
            }

            String softwareName = softwareDao.getSoftwareNameById(softwareId);
            if (softwareName == null || softwareName.isBlank()) {
                softwareName = "software-" + softwareId;
            }

            String fileUrl;
            try {
                fileUrl = softwareDao.getActiveFileUrlBySoftwareId(softwareId);
            } catch (SQLException ignored) {
                fileUrl = null;
            }

            stateDao.markDownloaded(userId, softwareId);
            softwareDao.increaseDownloadCount(softwareId);

            if (fileUrl != null && !fileUrl.isBlank()) {
                String normalized = fileUrl.trim();
                if (normalized.startsWith("http://") || normalized.startsWith("https://")) {
                    response.sendRedirect(normalized);
                    return;
                }
            }

            InputStream in = resolveDownloadStream(request, fileUrl);
            if (in != null) {
                String fileName = extractFileName(fileUrl, softwareName + ".bin");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

                try (InputStream stream = in; OutputStream out = response.getOutputStream()) {
                    stream.transferTo(out);
                }
                return;
            }

            String fallbackName = softwareName.replaceAll("[^a-zA-Z0-9-_]", "_") + "-download.txt";
            String payload = "Owned package placeholder for: " + softwareName + "\n" +
                    "User ID: " + userId + "\n" +
                    "Software ID: " + softwareId + "\n" +
                    "Status: OWNED (downloaded)\n" +
                    "Note: No active binary file_url configured yet in Software_Version.";

            response.setContentType("text/plain; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fallbackName + "\"");
            response.getOutputStream().write(payload.getBytes(StandardCharsets.UTF_8));

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private InputStream resolveDownloadStream(HttpServletRequest request, String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return null;
        }

        String normalized = fileUrl.trim();
        if (normalized.startsWith("http://") || normalized.startsWith("https://")) {
            return null;
        }

        String path = normalized.startsWith("/") ? normalized : "/" + normalized;
        InputStream in = request.getServletContext().getResourceAsStream(path);
        if (in != null) {
            return in;
        }

        if (!path.startsWith("/assets/")) {
            in = request.getServletContext().getResourceAsStream("/assets/" + normalized.replaceFirst("^/", ""));
        }

        return in;
    }

    private boolean hasOwnedLicense(int userId, int softwareId) throws SQLException {
        String sql = "SELECT 1 FROM fivepigs.license " +
                "WHERE customer_id = ? AND software_id = ? " +
                "AND (status IS NULL OR UPPER(status) <> 'REVOKED') LIMIT 1";

        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, userId);
            st.setInt(2, softwareId);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Integer resolveUserId(HttpSession session) {
        if (session == null) return null;

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

    private Integer parseInt(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String extractFileName(String fileUrl, String fallback) {
        if (fileUrl == null || fileUrl.isBlank()) return fallback;
        String normalized = fileUrl.replace("\\", "/");
        int idx = normalized.lastIndexOf('/');
        if (idx >= 0 && idx < normalized.length() - 1) {
            return normalized.substring(idx + 1);
        }
        return fallback;
    }
}
