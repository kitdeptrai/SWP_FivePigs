/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fivepigs.app.web.vendor;

import com.fivepigs.app.dao.LicenseDao;
import com.fivepigs.app.model.License;
import com.fivepigs.app.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MinhPD
 */
@WebServlet(name = "LicenseManagementServlet", urlPatterns = {"/vendor/license_management"})
public class VendorLicenseManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null) {
                request.getRequestDispatcher("/login").forward(request, response);
                return;
            }
            LicenseDao lidao = new LicenseDao();
            lidao.updateExpiredLicense();
            List<License> listLicense = lidao.getLicenseByVendorId(user.getUserId());
            Integer totalLicense = lidao.getTotalLicenseByVendor(user.getUserId());
            Integer totalLicenseActive = lidao.getTotalLicenseByVendorAndStatus(user.getUserId(), "ACTIVE");
            Integer totalLicenseExpire = lidao.getTotalLicenseByVendorAndStatus(user.getUserId(), "EXPIRED");
            Integer totalLicenseRevoke = lidao.getTotalLicenseByVendorAndStatus(user.getUserId(), "REVOKED");

            request.setAttribute("totalLicense", totalLicense);
            request.setAttribute("totalLicenseActive", totalLicenseActive);
            request.setAttribute("totalLicenseExpire", totalLicenseExpire);
            request.setAttribute("totalLicenseRevoke", totalLicenseRevoke);
            request.setAttribute("listLicense", listLicense);
            request.getRequestDispatcher("/WEB-INF/views/vendor/license_management.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            e.printStackTrace(response.getWriter());

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

}
