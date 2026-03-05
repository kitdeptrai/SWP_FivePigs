/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fivepigs.app.web.vendor;

import com.fivepigs.app.dao.*;
import com.fivepigs.app.model.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author MinhPD
 */
@WebServlet(name = "VendorDashboard", urlPatterns = {"/vendor_dashboard"})
public class VendorDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            SoftwareDao swdao = new SoftwareDao();

            VendorDao vddao = new VendorDao();
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null) {
                request.getRequestDispatcher("/login").forward(request, response);
                return;
            }
            List<Software> top3revenue = swdao.Top3RevenueByVendor(user.getUserId());
            Map<Integer, Double> revenueMap = vddao.revenueMap(user.getUserId());
            Map<Integer, Double> downloadMap = swdao.downloadByWeek(user.getUserId());
            Integer sumApprovedApps = swdao.totalAppByStatusAndVendor(user.getUserId(), "APPROVED");
            Integer sumDownloadApps = swdao.totalDownloadByVendor(user.getUserId());
            Double sumRevenue = vddao.sumRevenue(user.getUserId());
            Double avgRating = swdao.avgRatingByVendorId(user.getUserId());
            request.setAttribute("downloadByWeek", downloadMap);
            request.setAttribute("revenueByWeek", revenueMap);
            request.setAttribute("top3revenue", top3revenue);
            request.setAttribute("sumApprovedApps", sumApprovedApps == null ? 0 : sumApprovedApps);
            request.setAttribute("sumDownloadApps", sumDownloadApps == null ? 0 : sumDownloadApps);
            request.setAttribute("sumRevenue", sumRevenue == null ? 0.0 : sumRevenue);
            request.setAttribute("avgRating", avgRating == null ? 0.0 : avgRating);
            request.getRequestDispatcher("/WEB-INF/views/vendor/vendor_dashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace(); // log server
            request.setAttribute("error", "Can not load data from dashboard");
            request.getRequestDispatcher("/WEB-INF/views/vendor/vendor_dashboard.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
