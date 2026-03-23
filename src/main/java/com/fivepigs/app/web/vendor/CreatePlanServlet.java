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
@WebServlet(name = "CreatePlanServlet", urlPatterns = {"/vendor/create_plan"})
public class CreatePlanServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            int softwareId = Integer.parseInt(request.getParameter("softwareId"));
            String planName = request.getParameter("planName").trim().toUpperCase();
            int maxUsers = Integer.parseInt(request.getParameter("maxUsers"));
            double price = Double.parseDouble(request.getParameter("price"));
            planName = planName.trim().toUpperCase();

            SoftwarePricingDao dao = new SoftwarePricingDao();

            if (planName.equals("BASIC") || planName.equals("DEMO")) {
                response.sendRedirect(request.getContextPath()
                        + "/vendor/plan_management?softwareId=" + softwareId + "&error=reserved");
                return;
            }

            if (dao.isPlanNameExist(softwareId, planName)) {
                response.sendRedirect(request.getContextPath()
                        + "/vendor/plan_management?softwareId=" + softwareId + "&error=duplicate");
                return;
            }

            if (maxUsers <= 0 || price < 0) {
                response.sendRedirect(request.getContextPath()
                        + "/vendor/plan_management?softwareId=" + softwareId + "&error=invalid");
                return;
            }

            // ===== INSERT =====
            dao.createPlan(softwareId, planName, maxUsers, price);

            response.sendRedirect(request.getContextPath()
                    + "/vendor/plan_management?softwareId=" + softwareId);

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            e.printStackTrace(response.getWriter());
        }
    }

}
