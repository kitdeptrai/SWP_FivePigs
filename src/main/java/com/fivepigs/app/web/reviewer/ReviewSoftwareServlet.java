/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.dao.ReviewScoreDao;
import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.ReviewScore;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "ReviewSoftwareServlet", urlPatterns = {"/review_software"})
public class ReviewSoftwareServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("softwareId");
        if (idRaw == null || idRaw.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/reviewer_pending");
            return;
        }

        try {
            int softwareId = Integer.parseInt(idRaw);

            SoftwareDao softwareDao = new SoftwareDao();
            Software software = softwareDao.getSoftwareById(softwareId);

            if (software == null) {
                response.sendRedirect(request.getContextPath() + "/reviewer_pending");
                return;
            }

            request.setAttribute("software", software);
            request.getRequestDispatcher("/WEB-INF/views/reviewer/reviewer_software.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/reviewer_pending");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            User user = (User) session.getAttribute("user");
            int reviewerId = user.getUserId();

            int softwareId = Integer.parseInt(request.getParameter("softwareId"));

            int noMalware = request.getParameter("no_malware") != null ? 1 : 0;
            int noCopyright = request.getParameter("no_copyright_violation") != null ? 1 : 0;
            int noSpam = request.getParameter("no_spam_content") != null ? 1 : 0;

            int uiUxScore = Integer.parseInt(request.getParameter("ui_ux_score"));
            int technicalScore = Integer.parseInt(request.getParameter("technical_score"));
            int performanceScore = Integer.parseInt(request.getParameter("performance_score"));
            int documentationScore = Integer.parseInt(request.getParameter("documentation_score"));

            String reviewNote = request.getParameter("review_note");

            double totalScore = (uiUxScore + technicalScore + performanceScore + documentationScore) / 4.0;

            String decision = totalScore >= 5.0
                    && noMalware == 1
                    && noCopyright == 1
                    && noSpam == 1
                            ? "APPROVED"
                            : "REJECTED";

            ReviewScoreDao reviewScoreDao = new ReviewScoreDao();
            SoftwareDao softwareDao = new SoftwareDao();

            ReviewScore reviewScore = new ReviewScore();
            reviewScore.setSoftwareId(softwareId);
            reviewScore.setReviewerId(reviewerId);
            reviewScore.setNoMalware(noMalware == 1);
            reviewScore.setNoCopyrightViolation(noCopyright == 1);
            reviewScore.setNoSpamContent(noSpam == 1);
            reviewScore.setUiUxScore(uiUxScore);
            reviewScore.setTechnicalScore(technicalScore);
            reviewScore.setPerformanceScore(performanceScore);
            reviewScore.setDocumentationScore(documentationScore);
            reviewScore.setTotalScore(totalScore);
            reviewScore.setDecision(decision);
            reviewScore.setReviewNote(reviewNote);

            reviewScoreDao.insertReviewScore(reviewScore);

            if ("APPROVED".equals(decision)) {
                softwareDao.updateSoftwareStatus(softwareId, "PENDING_APPROVAL");
            } else {
                softwareDao.updateSoftwareStatus(softwareId, "REJECTED");
            }

            response.sendRedirect(request.getContextPath() + "/reviewer_history");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Submit review failed.");
            request.getRequestDispatcher("/WEB-INF/views/reviewer/reviewer_software.jsp")
                    .forward(request, response);
        }
    }

}
