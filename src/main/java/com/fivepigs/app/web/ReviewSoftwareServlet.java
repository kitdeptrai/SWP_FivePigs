/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.fivepigs.app.web;

import com.fivepigs.app.dao.ReviewScoreDao;
import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.ReviewScore;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "ReviewSoftwareServlet", urlPatterns = {"/review/submit"})
public class ReviewSoftwareServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            int softwareId = Integer.parseInt(request.getParameter("softwareId"));
            int reviewerId = (int) request.getSession().getAttribute("userId");

            int ui = Integer.parseInt(request.getParameter("uiUx"));
            int tech = Integer.parseInt(request.getParameter("technical"));
            int perf = Integer.parseInt(request.getParameter("performance"));
            int doc = Integer.parseInt(request.getParameter("documentation"));

            boolean noMalware = request.getParameter("noMalware") != null;
            boolean noCopyright = request.getParameter("noCopyright") != null;
            boolean noSpam = request.getParameter("noSpam") != null;

            double total = (ui + tech + perf + doc) / 4.0;
            String decision = total >= 7 ? "APPROVED" : "REJECTED";

            // Tạo object ReviewScore
            ReviewScore rs = new ReviewScore();
            rs.setSoftwareId(softwareId);
            rs.setReviewerId(reviewerId);
            rs.setNoMalware(noMalware);
            rs.setNoCopyrightViolation(noCopyright);
            rs.setNoSpamContent(noSpam);
            rs.setUiUxScore(ui);
            rs.setTechnicalScore(tech);
            rs.setPerformanceScore(perf);
            rs.setDocumentationScore(doc);
            rs.setTotalScore(total);
            rs.setDecision(decision);

            ReviewScoreDao reviewDao = new ReviewScoreDao();
            SoftwareDao softwareDao = new SoftwareDao();

            boolean saved = reviewDao.saveReviewScore(rs);

            if (saved) {
                //  UPDATE STATUS 
                softwareDao.updateStatus(softwareId, decision);
            }

            response.sendRedirect(request.getContextPath() + "/reviewer_dashboard");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}