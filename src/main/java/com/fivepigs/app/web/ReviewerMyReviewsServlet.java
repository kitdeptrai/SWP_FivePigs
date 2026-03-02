/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.fivepigs.app.web;

import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.Software;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import com.fivepigs.app.model.User;

/**
 *
 * @author Admin
 */
@WebServlet(name = "ReviewerMyReviewsServlet", 
            urlPatterns = {"/reviewer_my_reviews"})
public class ReviewerMyReviewsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, 
                         HttpServletResponse response)
            throws ServletException, IOException {

        try {

            // 1️⃣ Lấy user đang login từ session
            User user = (User) request.getSession()
                                        .getAttribute("user");

            if (user == null) {
                response.sendRedirect("login");
                return;
            }

            // 2️⃣ Gọi DAO
            SoftwareDao dao = new SoftwareDao();
            List<Software> myReviewList =
                    dao.getMyReviews(user.getUserId());

            // 3️⃣ Đẩy sang JSP
            request.setAttribute("myReviewList", myReviewList);
            
            request.setAttribute("user", user);

            request.getRequestDispatcher(
                "/WEB-INF/views/reviewer/reviewer_my_reviews.jsp")
                .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
        }
    }

    @Override
    public String getServletInfo() {
        return "Reviewer My Reviews Page";
    }
}