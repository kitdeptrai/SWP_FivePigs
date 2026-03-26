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
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author MinhPD
 */
@WebServlet(name = "LicenseUserManagementServlet", urlPatterns = {"/vendor/user_license_management"})
public class LicenseUserManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
        int licenseId = Integer.parseInt(request.getParameter("licenseId"));
        LicenseDao ldao=new LicenseDao();
        License license = ldao.getLicenseById(licenseId);
        List<User> users = ldao.getUsersByLicenseId(licenseId);

        request.setAttribute("license", license);
        request.setAttribute("listUsers", users);

        request.getRequestDispatcher("/WEB-INF/views/vendor/license_users.jsp").forward(request, response);
        }catch(SQLException e){
            System.out.println(e);
        }
    }

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

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
