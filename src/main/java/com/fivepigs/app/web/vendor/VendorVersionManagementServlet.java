/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fivepigs.app.web.vendor;

import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.Software;
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
@WebServlet(name = "VendorVersionManagementServlet", urlPatterns = {"/vendor/version_management"})
public class VendorVersionManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            SoftwareDao sdao=new SoftwareDao();
            int softwareId=Integer.parseInt(request.getParameter("softwareId"));
            List<Software> listVersion=sdao.getSoftwareVersionBySoftwareId(softwareId);
            request.setAttribute("listVersion", listVersion);
            request.setAttribute("softwareId", softwareId);
            request.getRequestDispatcher("/WEB-INF/views/vendor/version_management.jsp").forward(request, response);
        } catch (SQLException e) {

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
