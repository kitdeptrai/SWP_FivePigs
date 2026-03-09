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
import java.util.Map;

/**
 *
 * @author MinhPD
 */
@WebServlet(name="ChangeStatusOfProductServlet", urlPatterns={"/vendor/change_status_product"})
public class ChangeStatusOfProductServlet extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            SoftwareDao swdao = new SoftwareDao();
            
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null) {
                request.getRequestDispatcher("/login").forward(request, response);
                return;
            }
            Integer softwareId=Integer.parseInt(request.getParameter("softwareId"));
            String status=request.getParameter("status");
            swdao.changeStatusSoftware(user.getUserId(), softwareId, status);
           
            response.sendRedirect(request.getContextPath() + "/vendor/my_products");
        } catch (SQLException e) {
            e.printStackTrace(); // log server
            
        }
    }

}
