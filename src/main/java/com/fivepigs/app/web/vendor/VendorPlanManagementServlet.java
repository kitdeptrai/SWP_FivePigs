/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fivepigs.app.web.vendor;

import com.fivepigs.app.dao.SoftwarePricingDao;
import com.fivepigs.app.model.SoftwarePricing;
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
@WebServlet(name = "VendorPlanManagementServlet", urlPatterns = {"/vendor/plan_management"})
public class VendorPlanManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Integer softwareId=Integer.parseInt(request.getParameter("softwareId"));
            SoftwarePricingDao swpdao = new SoftwarePricingDao();
            List<SoftwarePricing> list=swpdao.getPlanBySoftwareId(softwareId);
            request.setAttribute("softwareId", softwareId);
            request.setAttribute("list", list);
            request.getRequestDispatcher("/WEB-INF/views/vendor/plan_management.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();

            response.setContentType("text/plain");
            e.printStackTrace(response.getWriter());
        }
    }

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
