package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet(name = "ReviewerDownloadSoftwareServlet", urlPatterns = {"/reviewer/download-software"})
public class ReviewerDownloadSoftwareServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!Integer.valueOf(5).equals(user.getRoleId())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only reviewer can download this file.");
            return;
        }

        String idRaw = request.getParameter("softwareId");
        if (idRaw == null || idRaw.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing softwareId.");
            return;
        }

        try {
            int softwareId = Integer.parseInt(idRaw);

            SoftwareDao dao = new SoftwareDao();
            Software software = dao.getSoftwareById(softwareId);

            if (software == null || software.getSoftwareVersion() == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Software not found.");
                return;
            }

            String dbPath = software.getSoftwareVersion().getFileUrl();

            if (dbPath == null || dbPath.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Software file path is empty.");
                return;
            }

            System.out.println("DEBUG softwareId = " + softwareId);
            System.out.println("DEBUG db file_url = [" + dbPath + "]");

            String normalized = dbPath.trim().replace("\\", "/");

            // bỏ dấu / ở đầu nếu có
            normalized = normalized.replaceFirst("^/+", "");

            // nếu path có chứa uploads/ ở giữa thì cắt từ uploads/
            int uploadsIndex = normalized.indexOf("uploads/");
            if (uploadsIndex >= 0) {
                normalized = normalized.substring(uploadsIndex);
            }

            if (!normalized.startsWith("uploads/")) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Invalid file path. DB value = " + dbPath);
                return;
            }

            String appRoot = getServletContext().getRealPath("/");
            if (appRoot == null) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot resolve app root.");
                return;
            }

            File baseDir = new File(getServletContext().getRealPath("/uploads")).getCanonicalFile();
            File file = new File(appRoot, normalized).getCanonicalFile();

            System.out.println("DEBUG normalized = [" + normalized + "]");
            System.out.println("DEBUG physical path = [" + file.getAbsolutePath() + "]");

            if (!file.getPath().startsWith(baseDir.getPath())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid file path.");
                return;
            }

            if (!file.exists() || !file.isFile()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Physical file not found: " + file.getAbsolutePath());
                return;
            }

            String contentType = getServletContext().getMimeType(file.getName());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            response.reset();
            response.setContentType(contentType);
            response.setContentLengthLong(file.length());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

            try (FileInputStream in = new FileInputStream(file); OutputStream out = response.getOutputStream()) {

                byte[] buffer = new byte[8192];
                int bytesRead;

                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "softwareId is invalid.");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
