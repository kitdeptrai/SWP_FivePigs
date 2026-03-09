/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.fivepigs.app.web.vendor;

import com.fivepigs.app.dao.LicenseDao;
import com.fivepigs.app.dao.SoftwareDao;
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

/**
 *
 * @author MinhPD
 */
@WebServlet(name="ChangeStatusOfLicenseServlet", urlPatterns={"/change_status_license"})
public class ChangeStatusOfLicenseServlet extends HttpServlet {
   

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            LicenseDao lidao = new LicenseDao();
            
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null) {
                request.getRequestDispatcher("/login").forward(request, response);
                return;
            }
            Integer licenseId=Integer.parseInt(request.getParameter("licenseId"));
            String status=request.getParameter("status");
            lidao.changeStatusSoftware(status, licenseId);
           
            response.sendRedirect(request.getContextPath() + "/vendor/license_management");
        } catch (SQLException e) {
            e.printStackTrace(); // log server
            
        }
    }

}
