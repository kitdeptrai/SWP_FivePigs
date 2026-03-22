package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.ReportDao;
import com.fivepigs.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name="ReportServlet", urlPatterns={"/report-product"})
public class ReportServlet extends HttpServlet {

    private final ReportDao reportDao = new ReportDao();
    private static final String DEFAULT_STATUS = "PENDING";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/library");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        if(user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/library");
            return;
        }

        String softwareIdRaw = request.getParameter("softwareId");
        String reason = request.getParameter("reason");

        if (softwareIdRaw == null || softwareIdRaw.isBlank() || reason == null || reason.trim().isBlank()) {
            response.sendRedirect(request.getContextPath() + "/library?reportMsg=invalid_reason");
            return;
        }
        try {
            int softwareId = Integer.parseInt(softwareIdRaw);
            reportDao.reportFromLicense(softwareId, user.getUserId(), reason.trim(), DEFAULT_STATUS);
            response.sendRedirect(request.getContextPath() + "/library?reportMsg=submitted");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/library?reportMsg=invalid_software");
        } catch (SQLException e) {
            response.sendRedirect(request.getContextPath() + "/library?reportMsg=failed");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
