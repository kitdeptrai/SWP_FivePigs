/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fivepigs.app.web.vendor;

import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.Software;
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
import java.util.List;

/**
 *
 * @author MinhPD
 */
@WebServlet(name = "MyProductsServlet", urlPatterns = {"/vendor/my_products"})
public class VendorMyProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
        SoftwareDao swdao = new SoftwareDao();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }
        
        Integer totalApps = swdao.totalProductsByVendor(user.getUserId());
        Integer pendingApps=swdao.totalAppByStatusAndVendor(user.getUserId(),"PENDING%");
        Integer activeApps=swdao.totalAppByStatusAndVendor(user.getUserId(), "ACTIVE");
        Double revenueByVendor=swdao.totalRevenueByVendor(user.getUserId());
        List<Software> softwareCardList=swdao.getSoftwareCardListByVendorID(user.getUserId());
        
        request.setAttribute("totalApps", totalApps== null ? 0 : totalApps);
        request.setAttribute("pendingApps",pendingApps== null ? 0 : pendingApps);
        request.setAttribute("activeApps", activeApps== null ? 0 : activeApps);
        request.setAttribute("revenueByVendor", revenueByVendor== null ? 0 : revenueByVendor);
        request.setAttribute("softwareCardList",softwareCardList);
        request.getRequestDispatcher("/WEB-INF/views/vendor/my_products.jsp").forward(request, response);
        }catch(SQLException e){
            
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
