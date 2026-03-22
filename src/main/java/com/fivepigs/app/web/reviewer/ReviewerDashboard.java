/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.dao.ReviewScoreDao;
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
 * @author Admin
 */
@WebServlet(name = "ReviewerDashboard", urlPatterns = {"/reviewer_dashboard"})
public class ReviewerDashboard extends HttpServlet {

   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ReviewerDashboard</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReviewerDashboard at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
         HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null) {
                request.getRequestDispatcher("/login").forward(request, response);
                return;
            }

        try {
            SoftwareDao sDao = new SoftwareDao();
            ReviewScoreDao reviewScoreDao = new ReviewScoreDao();

           
            int reviewerId = user.getUserId();

            Integer pendingReviewApp = sDao.pendingReviewApp();
            Integer completeReviewApp = sDao.completeReviewApp();
            Integer reviewedToday = sDao.reviewedToday();

            int inReviewCount = sDao.countPendingReviewSoftware();
            int reviewedCount = reviewScoreDao.countReviewsByReviewer(reviewerId);

            request.setAttribute("user", user);
            request.setAttribute("pendingReviewApp", pendingReviewApp);
            request.setAttribute("completeReviewApp", completeReviewApp);
            request.setAttribute("reviewedToday", reviewedToday);
            request.setAttribute("pendingList", sDao.getPendingSoftware());

            request.setAttribute("inReviewCount", inReviewCount);
            request.setAttribute("reviewedCount", reviewedCount);

            request.getRequestDispatcher("/WEB-INF/views/reviewer/reviewer_dashboard.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

  
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
