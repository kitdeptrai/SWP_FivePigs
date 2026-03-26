/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fivepigs.app.web.vendor;

import com.fivepigs.app.dao.VendorDao;
import com.fivepigs.app.model.User;
import com.fivepigs.app.model.VendorEarning;
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
@WebServlet(name = "VendorTransactionServlet", urlPatterns = {"/vendor/transaction_management"})
public class VendorTransactionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            // 🔐 lấy vendor từ session
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            int vendorId = user.getUserId();
            VendorDao vdao=new VendorDao();
  
            List<VendorEarning> transactions
                    = vdao.getVendorTransactions(vendorId);

          
            request.setAttribute("transactions", transactions);

         
            request.getRequestDispatcher("/WEB-INF/views/vendor/vendor_transactions.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error loading vendor transactions", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
