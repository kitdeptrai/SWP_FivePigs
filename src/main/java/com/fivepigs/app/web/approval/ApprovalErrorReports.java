package com.fivepigs.app.web.approval;

import com.fivepigs.app.dao.ReportDao;
import com.fivepigs.app.model.Report;
import com.fivepigs.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ApprovalErrorReports", urlPatterns = {"/ApprovalErrorReports"})
public class ApprovalErrorReports extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            ReportDao reportDao = new ReportDao();
            List<Report> errorReports = reportDao.getErrorApprovalReports();
            request.setAttribute("errorReports", errorReports);
            request.getRequestDispatcher("/WEB-INF/views/Approval/approval_errors.jsp")
                    .forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public String getServletInfo() {
        return "Approval Error Reports";
    }
}
