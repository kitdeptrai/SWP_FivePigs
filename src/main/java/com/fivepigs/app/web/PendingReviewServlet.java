/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.fivepigs.app.web;

import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "PendingReviewServlet", urlPatterns = {"/reviewer_pending"})
public class PendingReviewServlet extends HttpServlet {
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    try {
        String keyword = request.getParameter("keyword");

        SoftwareDao dao = new SoftwareDao();
        List<Software> list;

        if (keyword != null && !keyword.trim().isEmpty()) {
            list = dao.searchPendingSoftware(keyword);
        } else {
            list = dao.getPendingSoftware();
        }

        request.setAttribute("pendingList", list);
        request.setAttribute("user", user);

        request.getRequestDispatcher("/WEB-INF/views/reviewer/reviewer_pending.jsp")
               .forward(request, response);

    } catch (SQLException e) {
        e.printStackTrace();
        response.sendError(500);
    }
}
  
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
