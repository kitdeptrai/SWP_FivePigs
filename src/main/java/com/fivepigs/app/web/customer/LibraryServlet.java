package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.dao.UserSoftwareStateDao;
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
import java.util.List;
import java.util.Map;

@WebServlet(name = "LibraryServlet", urlPatterns = {"/library"})
public class LibraryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("activePage", "library");

        Integer userId = resolveUserId(request.getSession(false));
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/library");
            return;
        }

        SoftwareDao softwareDao = new SoftwareDao();
        UserSoftwareStateDao stateDao = new UserSoftwareStateDao();

        try {
            List<Software> myLibrary = softwareDao.getLibraryByUserIdWithIcon(userId);
            Map<Integer, Boolean> downloadedMap = stateDao.getDownloadedMapByUser(userId);

            request.setAttribute("libraryList", myLibrary);
            request.setAttribute("downloadedMap", downloadedMap);

            request.getRequestDispatcher("/WEB-INF/views/customer/library.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private Integer resolveUserId(HttpSession session) { 
        if (session == null) {
            return null;
        }

        User user = (User) session.getAttribute("user");
        if (user != null && user.getUserId() != null) {
            return user.getUserId();
        }

        Object userIdAttr = session.getAttribute("userId");
        if (userIdAttr instanceof Integer) {
            return (Integer) userIdAttr;
        }
        if (userIdAttr instanceof String) {
            try {
                return Integer.valueOf((String) userIdAttr);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }

        return null;
    }
}
