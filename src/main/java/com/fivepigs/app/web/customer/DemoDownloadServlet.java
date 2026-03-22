package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.dao.SoftwareDemoDao;
import com.fivepigs.app.model.SoftwareDemoVersion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet(name = "DemoDownloadServlet", urlPatterns = {"/demo/download"})
public class DemoDownloadServlet extends HttpServlet {

    private final SoftwareDemoDao softwareDemoDao = new SoftwareDemoDao();
    private final SoftwareDao softwareDao = new SoftwareDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer softwareId = parseInt(request.getParameter("softwareId"));
        if (softwareId == null) {
            response.sendRedirect(request.getContextPath() + "/customer_dashboard");
            return;
        }

        try {
            SoftwareDemoVersion demo = softwareDemoDao.getActiveDemoBySoftwareId(softwareId);
            if (demo == null || demo.getDemoFileUrl() == null || demo.getDemoFileUrl().isBlank()) {
                response.sendRedirect(request.getContextPath() + "/product?pid=" + softwareId + "&demoMsg=unavailable");
                return;
            }

            String softwareName = softwareDao.getSoftwareNameById(softwareId);
            if (softwareName == null || softwareName.isBlank()) {
                softwareName = "software-" + softwareId;
            }

            String fileUrl = demo.getDemoFileUrl().trim();
            if (fileUrl.startsWith("http://") || fileUrl.startsWith("https://")) {
                response.sendRedirect(fileUrl);
                return;
            }

            InputStream in = resolveDemoStream(request, fileUrl);
            if (in != null) {
                String fileName = extractFileName(fileUrl, softwareName + "-demo.bin");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                try (InputStream stream = in; OutputStream out = response.getOutputStream()) {
                    stream.transferTo(out);
                }
                return;
            }

            String fallbackName = softwareName.replaceAll("[^a-zA-Z0-9-_]", "_") + "-demo.txt";
            String payload = "Demo package placeholder for: " + softwareName + "\n"
                    + "Software ID: " + softwareId + "\n"
                    + "Demo version: " + (demo.getVersionName() == null ? "Updating" : demo.getVersionName()) + "\n"
                    + "Note: The demo_file_url exists in DB but no matching file was found in the web assets.";
            response.setContentType("text/plain; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fallbackName + "\"");
            response.getOutputStream().write(payload.getBytes(StandardCharsets.UTF_8));
        } catch (SQLException e) {
            throw new ServletException("Unable to download demo", e);
        }
    }

    private InputStream resolveDemoStream(HttpServletRequest request, String fileUrl) {
        String path = fileUrl.startsWith("/") ? fileUrl : "/" + fileUrl;
        InputStream in = request.getServletContext().getResourceAsStream(path);
        if (in != null) {
            return in;
        }

        if (!path.startsWith("/assets/")) {
            return request.getServletContext().getResourceAsStream("/assets/" + fileUrl.replaceFirst("^/", ""));
        }
        return null;
    }

    private String extractFileName(String fileUrl, String fallback) {
        String normalized = fileUrl.replace("\\", "/");
        int idx = normalized.lastIndexOf('/');
        if (idx >= 0 && idx < normalized.length() - 1) {
            return normalized.substring(idx + 1);
        }
        return fallback;
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
