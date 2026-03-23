/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fivepigs.app.web.vendor;

import com.fivepigs.app.dao.SoftwareDao;
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
import java.sql.SQLException;

/**
 *
 * @author MinhPD
 */
@WebServlet(name = "UploadVersionServlet", urlPatterns = {"/vendor/upload_version"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 200,
        maxRequestSize = 1024 * 1024 * 500
)
public class VendorUploadVersionServlet extends HttpServlet {

    private SoftwareService softwareService = new SoftwareService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null) {
                request.getRequestDispatcher("/login").forward(request, response);
                return;
            }
            String versionName = request.getParameter("versionName");
            String releaseNote = request.getParameter("releaseNote");
            Integer softwareId = Integer.parseInt(request.getParameter("softwareId"));
            Part file = request.getPart("softwareFile");
            String fileError = softwareService.validateFiles(file);

            if (fileError != null) {
                showError(request, response, fileError);
                return;
            }
            String softwarePath = saveFile(request, file, "software/" + softwareId);
            softwareService.addSoftwareVersion(softwareId, versionName, softwarePath, releaseNote, file.getSize());
            
            response.sendRedirect(request.getContextPath() + "/vendor/version_management?softwareId="+softwareId);
        } catch (Exception e) {
            e.printStackTrace();

            response.setContentType("text/plain");
            e.printStackTrace(response.getWriter());

        }
    }

    private void showError(HttpServletRequest request,
            HttpServletResponse response,
            String message) throws ServletException, IOException {

        request.setAttribute("error", message);

        request.getRequestDispatcher("/WEB-INF/views/vendor/upload_product.jsp")
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

        String originalName = filePart.getSubmittedFileName();

        String fileName = System.currentTimeMillis() + "_" + originalName;

        String fullPath = uploadDir + File.separator + fileName;

        filePart.write(fullPath);

        return "uploads/" + folder + "/" + fileName;
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
