package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CustomerDashboardServlet", urlPatterns = {"/customer_dashboard"})
public class CustomerDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user_dashboard");
            return;
        }

        loadHomeLists(request);
        request.setAttribute("activePage", "home");
        request.getRequestDispatcher("/WEB-INF/views/customer/home.jsp").forward(request, response);
    }

    private void loadHomeLists(HttpServletRequest request) {
        SoftwareDao dao = new SoftwareDao();
        try {
            List<Software> trendList = dao.getTopDownloadWithIcon(12);
            List<Software> bestSellingList = dao.getBestSellingWithIcon(12);
            request.setAttribute("trendList", trendList);
            request.setAttribute("bestSellingList", bestSellingList);
        } catch (SQLException e) {
            request.setAttribute("trendList", new ArrayList<Software>());
            request.setAttribute("bestSellingList", new ArrayList<Software>());

        }
    }
}