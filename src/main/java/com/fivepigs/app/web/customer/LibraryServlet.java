package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.Software;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name="LibraryServlet", urlPatterns={"/library"})
public class LibraryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("activePage", "library");

        // ví dụ: bạn lưu userId trong session
        HttpSession session = request.getSession(false);
        Integer userId = (session == null) ? null : (Integer) session.getAttribute("userId");

        if (userId == null) {
            // chưa login thì chuyển về home/login (tùy bạn)
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        SoftwareDao sdao = new SoftwareDao();
        try {
            List<Software> myLibrary = sdao.getLibraryByUserIdWithIcon(userId);
            request.setAttribute("libraryList", myLibrary);

            request.getRequestDispatcher("/WEB-INF/views/customer/library.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}