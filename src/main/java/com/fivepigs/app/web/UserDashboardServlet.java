package com.fivepigs.app.web;

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

@WebServlet(name = "UserDashboardServlet", urlPatterns = {"/user_dashboard"})
public class UserDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        if (user != null) {
            resp.sendRedirect(req.getContextPath() + "/customer_dashboard");
            return;
        }

        loadHomeLists(req);
        req.setAttribute("activePage", "home");
        req.getRequestDispatcher("/WEB-INF/views/customer/home.jsp").forward(req, resp);
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
            request.setAttribute("homeWarning", "Khong tai duoc du lieu Trending/Best Selling.");
        }
    }
}