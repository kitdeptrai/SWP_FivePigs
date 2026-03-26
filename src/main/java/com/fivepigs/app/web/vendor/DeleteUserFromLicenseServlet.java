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
import java.sql.SQLException;

/**
 *
 * @author MinhPD
 */
@WebServlet(name = "DeleteUserFromLicenseServlet", urlPatterns = {"/vendor/remove-user-license"})
public class DeleteUserFromLicenseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
        int licenseId = Integer.parseInt(request.getParameter("licenseId"));
        LicenseDao ldao=new LicenseDao();
        int userId = Integer.parseInt(request.getParameter("userId"));

        ldao.delete(licenseId, userId);

        response.sendRedirect("/vendor/user_license_management?licenseId=" + licenseId);
        }catch(SQLException e){
            System.out.println(e);
        }
    }

}
