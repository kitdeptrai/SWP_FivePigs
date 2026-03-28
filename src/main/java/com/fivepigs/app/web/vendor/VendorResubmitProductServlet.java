/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fivepigs.app.web.vendor;

import com.fivepigs.app.dao.GenreDao;
import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.User;
import com.fivepigs.app.service.SoftwareService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import com.fivepigs.app.config.Db;
import java.sql.PreparedStatement;

/**
 *
 * @author MinhPD
 */
@WebServlet(name = "VendorResubmitProductServlet", urlPatterns = {"/vendor/resubmit_product"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 200,
        maxRequestSize = 1024 * 1024 * 500
)
public class VendorResubmitProductServlet extends HttpServlet {

    private SoftwareService softwareService = new SoftwareService();
    private SoftwareDao softwareDao = new SoftwareDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Integer softwareId = Integer.parseInt(request.getParameter("softwareId"));
            GenreDao gdao = new GenreDao();
            SoftwareDao swdao = new SoftwareDao();
            Software software = swdao.getSoftwareDetailBySoftwareIdVendor(softwareId);
            request.setAttribute("softwareId", softwareId);
            request.setAttribute("listGenre", gdao.getAllGenre());
            request.setAttribute("software", software);

            request.getRequestDispatcher("/WEB-INF/views/vendor/resubmit_product.jsp").forward(request, response);
        } catch (SQLException e) {

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            User user = getLoggedInUser(request, response);
            if (user == null) {
                return;
            }

            int softwareId = Integer.parseInt(request.getParameter("softwareId"));

            String name = request.getParameter("productName");
            String version = request.getParameter("version");
            String shortDesc = request.getParameter("shortDescription");
            String description = request.getParameter("description");
            String releaseNote = request.getParameter("releaseNote");
            String systemRequire = request.getParameter("systemRequire");
            String categoryParam = request.getParameter("category");
            String priceParam = request.getParameter("price");
            String[] genreIds = request.getParameterValues("genres");
            // ===== VALIDATE =====
            String validationError = softwareService.validateUpload(
                    name, description, priceParam, categoryParam
            );

            if (validationError != null) {
                showError(request, response, validationError, 1);
                return;
            }

            int categoryId = Integer.parseInt(categoryParam);
            double price = Double.parseDouble(priceParam);

            Part softwareFile = request.getPart("softwareFile");
            Part thumbnail = request.getPart("thumbnail");
            String fileError = softwareService.validateFiles(softwareFile, thumbnail);

            if (fileError != null) {
                showError(request, response, fileError, 3);
                return;
            }
            // ===== UPDATE SOFTWARE =====
            softwareDao.updateSoftware(
                    softwareId,
                    name,
                    shortDesc,
                    categoryId
            );
            softwareService.updatePricingAfterResubmit(softwareId, price);

            // ===== UPDATE DETAIL =====
            softwareDao.updateSoftwareDetail(
                    softwareId,
                    description,
                    systemRequire,
                    releaseNote
            );
            if (genreIds != null) {
                softwareService.updateSoftwareGenres(softwareId, genreIds);
            }
            // ===== FILES =====
            // ===== NEW THUMBNAIL =====
            if (thumbnail != null && thumbnail.getSize() > 0) {

                String thumbnailPath = saveFile(request, thumbnail, "images/" + softwareId);

                softwareDao.updateThumbnail(
                        softwareId,
                        thumbnailPath
                );
            }

            // ===== NEW GALLERY =====
            Collection<Part> parts = request.getParts();

            for (Part part : parts) {

                if ("additionalImages".equals(part.getName()) && part.getSize() > 0) {

                    String imagePath = saveFile(request, part, "images/" + softwareId);

                    softwareService.addSoftwareImage(
                            softwareId,
                            imagePath,
                            false
                    );
                }
            }

            // ===== NEW VERSION =====
            if (softwareFile != null && softwareFile.getSize() > 0) {

                String softwarePath = saveFile(request, softwareFile, "software/" + softwareId);

                softwareService.addSoftwareVersion(
                        softwareId,
                        version,
                        softwarePath,
                        releaseNote,
                        softwareFile.getSize()
                );
            }
            int reviewerId = 2; // TODO: sau này query role

            String title = "Software resubmitted - " + name;
            String content = "Software \"" + name + "\" has been resubmitted after rejection and is waiting for review.";

            insertNotification(reviewerId, title, content);
            response.sendRedirect(
                    request.getContextPath()
                    + "/vendor/product_detail?softwareId=" + softwareId
            );

        } catch (Exception e) {

            e.printStackTrace();
            response.getWriter().println(e.getMessage());
        }
    }

    private User getLoggedInUser(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("/login");
        }

        return user;
    }

    private void showError(HttpServletRequest request,
            HttpServletResponse response,
            String message,
            int step) throws ServletException, IOException {

        request.setAttribute("error", message);
        request.setAttribute("errorStep", step);

        request.getRequestDispatcher("/WEB-INF/views/vendor/resubmit_product.jsp")
                .forward(request, response);
    }

    private String saveFile(HttpServletRequest request,
            Part filePart,
            String folder) throws IOException {

        String uploadDir = request.getServletContext()
                .getRealPath("/uploads/" + folder);

        File dir = new File(uploadDir);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = System.currentTimeMillis()
                + "_"
                + filePart.getSubmittedFileName();

        String fullPath = uploadDir + File.separator + fileName;

        filePart.write(fullPath);

        return "uploads/" + folder + "/" + fileName;
    }

    private void insertNotification(int userId, String title, String content) throws Exception {

        String sql = "INSERT INTO Notification "
                + "(user_id, title, content, is_read, type, priority, related_url) "
                + "VALUES (?, ?, ?, 0, 'RESUBMITTED', 'HIGH', '/reviewer_pending')";

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, title);
            ps.setString(3, content);

            ps.executeUpdate();
        }
    }
}
