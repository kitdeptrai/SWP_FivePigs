package com.fivepigs.app.web.approval;

import com.fivepigs.app.model.User;
import com.fivepigs.app.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ApprovalProfileServlet", urlPatterns = {"/approval_profile"})
public class ApprovalProfileServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        request.setAttribute("profileUser", user);

        request.getRequestDispatcher("/WEB-INF/views/Approval/approval_profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (newPassword == null || newPassword.length() < 6 || newPassword.length() > 72) {
            request.setAttribute("error", "Password must be between 6 and 72 characters.");
            doGet(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            doGet(request, response);
            return;
        }

        try {
            userService.resetPassword(user.getEmail(), newPassword);
            
            // Re-fetch or update user session if necessary, but password change shouldn't affect current basic session properties
            user.setPassword(newPassword);
            session.setAttribute("user", user);

            request.setAttribute("success", "Password updated successfully!");
            doGet(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database error updating password", e);
        }
    }
}
