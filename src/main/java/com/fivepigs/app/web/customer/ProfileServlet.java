package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.AdminDao;
import com.fivepigs.app.dao.UserDao;
import com.fivepigs.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();
    private final AdminDao adminDao = new AdminDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User sessionUser = resolveSessionUser(request.getSession(false));
        if (sessionUser == null || sessionUser.getEmail() == null || sessionUser.getEmail().isBlank()) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/profile");
            return;
        }

        try {
            User latest = userDao.findByEmail(sessionUser.getEmail());
            if (latest != null) {
                request.getSession().setAttribute("user", latest);
                request.setAttribute("profileUser", latest);
            } else {
                request.setAttribute("profileUser", sessionUser);
            }

            request.setAttribute("activePage", "profile");
            request.getRequestDispatcher("/WEB-INF/views/customer/profile.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User sessionUser = resolveSessionUser(request.getSession(false));
        if (sessionUser == null || sessionUser.getEmail() == null || sessionUser.getEmail().isBlank()) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/profile");
            return;
        }

        String fullName = trim(request.getParameter("fullName"));
        if (fullName == null || fullName.length() < 2 || fullName.length() > 100) {
            response.sendRedirect(request.getContextPath() + "/profile?msg=invalid_name");
            return;
        }

        try {
            AdminDao.UserRow row = adminDao.findUserById(sessionUser.getUserId());
            if (row == null) {
                response.sendRedirect(request.getContextPath() + "/profile?msg=update_failed");
                return;
            }

            adminDao.updateUser(
                    sessionUser.getUserId(),
                    fullName,
                    row.getPhone(),
                    row.getStatus(),
                    row.getRoleName()
            );

            User latest = userDao.findByEmail(sessionUser.getEmail());
            if (latest != null) {
                request.getSession().setAttribute("user", latest);
            }
            response.sendRedirect(request.getContextPath() + "/profile?msg=updated");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private User resolveSessionUser(HttpSession session) {
        if (session == null) return null;
        return (User) session.getAttribute("user");
    }

    private String trim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
