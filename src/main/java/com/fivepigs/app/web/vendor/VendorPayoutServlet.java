/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fivepigs.app.web.vendor;

import com.fivepigs.app.dao.PayoutDao;
import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.dao.VendorDao;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.User;
import com.fivepigs.app.model.VendorPayout;
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

/**
 *
 * @author MinhPD
 */
@WebServlet(name = "PayoutServlet", urlPatterns = {"/vendor/payout"})
public class VendorPayoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            VendorDao vdao = new VendorDao();
            PayoutDao pdao = new PayoutDao();

            List<VendorPayout> list = vdao.getPayoutByVendorId(user.getUserId());
            double balance = pdao.getAvailableBalance(user.getUserId());

            request.setAttribute("balance", balance);
            request.setAttribute("list", list);

            request.getRequestDispatcher("/WEB-INF/views/vendor/payout.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            int vendorId = user.getUserId();

            PayoutDao pdao = new PayoutDao();
            VendorDao vdao = new VendorDao();

            // ===== GET PARAM =====
            double amount = Double.parseDouble(request.getParameter("amount"));
            String paymentMethod = request.getParameter("paymentMethod");
            String paymentAccount = request.getParameter("paymentAccount");

            double balance = pdao.getAvailableBalance(vendorId);

            // ===== VALIDATE =====
            if (amount < 50) {
                response.sendRedirect(request.getContextPath()
                        + "/vendor/payout?error=min");
                return;
            }

            if (amount > balance) {
                response.sendRedirect(request.getContextPath()
                        + "/vendor/payout?error=balance");
                return;
            }

            if (paymentMethod == null || paymentMethod.isEmpty()) {
                response.sendRedirect(request.getContextPath()
                        + "/vendor/payout?error=method");
                return;
            }

            if (paymentAccount == null || paymentAccount.isEmpty()) {
                response.sendRedirect(request.getContextPath()
                        + "/vendor/payout?error=account");
                return;
            }

            // ===== INSERT =====
            vdao.createPayoutRequest(vendorId, amount, paymentMethod, paymentAccount);

            response.sendRedirect(request.getContextPath() + "/vendor/payout?success=1");

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/plain");
            e.printStackTrace(response.getWriter());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
