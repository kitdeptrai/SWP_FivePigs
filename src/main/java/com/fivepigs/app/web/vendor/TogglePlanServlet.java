/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.fivepigs.app.web.vendor;

import com.fivepigs.app.dao.SoftwarePricingDao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author MinhPD
 */
@WebServlet(name="TogglePlanServlet", urlPatterns={"/vendor/toggle_plan"})
public class TogglePlanServlet extends HttpServlet {
   

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
       
    } 


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
       try {

            int pricingId = Integer.parseInt(request.getParameter("pricingId"));

            SoftwarePricingDao dao = new SoftwarePricingDao();

            dao.togglePlanStatus(pricingId);

            // redirect lại trang plan
            String softwareId = request.getParameter("softwareId");
            response.sendRedirect(request.getContextPath() + "/vendor/plan_management?softwareId=" + softwareId);

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            e.printStackTrace(response.getWriter());
        }
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
