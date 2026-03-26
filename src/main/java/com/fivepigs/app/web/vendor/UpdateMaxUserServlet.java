/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fivepigs.app.web.vendor;

import com.fivepigs.app.dao.LicenseDao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.sql.SQLException;

/**
 *
 * @author MinhPD
 */
@WebServlet(name = "UpdateMaxUserServlet", urlPatterns = {"/vendor/update_maxuser"})
public class UpdateMaxUserServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int licenseId = Integer.parseInt(request.getParameter("licenseId"));
            int newMax = Integer.parseInt(request.getParameter("maxUsers"));
            LicenseDao ldao = new LicenseDao();
            int currentUsers = ldao.countUsers(licenseId);

            if (newMax < currentUsers) {
                String error = "Max users cannot be less than current assigned users (" + currentUsers + ")";
                
                response.sendRedirect(
                        request.getContextPath()
                        + "/vendor/user_license_management?licenseId=" + licenseId
                        + "&error=" + URLEncoder.encode(error, "UTF-8")
                );
                return;
            }

            ldao.updateMaxUsers(licenseId, newMax);

            response.sendRedirect("/vendor/user_license_management?licenseId=" + licenseId);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

}
