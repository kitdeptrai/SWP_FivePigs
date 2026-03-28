package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.Software;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {

    private final SoftwareDao softwareDao = new SoftwareDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = normalize(request.getParameter("q"));
        String dept = normalizeDept(request.getParameter("dept"));
        String genre = normalize(request.getParameter("genre"));
        Integer categoryId = mapCategoryId(dept);

        List<Software> results = new ArrayList<>();

        if (!keyword.isEmpty() || !genre.isEmpty()) {
            try {
                results = softwareDao.searchSoftwareWithIcon(keyword, categoryId, genre.isEmpty() ? null : genre, 80);
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }

        request.setAttribute("activePage", "");
        request.setAttribute("searchKeyword", keyword);
        request.setAttribute("searchDept", dept);
        request.setAttribute("searchGenre", genre);
        request.setAttribute("searchResults", results);
        request.setAttribute("resultCount", results.size());
        request.getRequestDispatcher("/WEB-INF/views/customer/search.jsp").forward(request, response);
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }

    private String normalizeDept(String value) {
        String v = normalize(value).toLowerCase();
        return switch (v) {
            case "apps", "games" -> v;
            default -> "all";
        };
    }

    private Integer mapCategoryId(String dept) {
        return switch (dept) {
            case "apps" -> 1;
            case "games" -> 2;
            default -> null;
        };
    }
}
