package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.NewsDao;
import com.fivepigs.app.model.News;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="NewsServlet", urlPatterns={"/news"})
public class NewsServlet extends HttpServlet {

    private final NewsDao newsDao = new NewsDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = normalizeType(request.getParameter("type"));

        try {
            List<News> newsList = newsDao.getPublishedNews(type, 12);
            News featuredNews = newsList.isEmpty() ? null : newsList.get(0);
            List<News> latestNews = newsList.size() > 1
                    ? new ArrayList<>(newsList.subList(1, newsList.size()))
                    : new ArrayList<>();

            request.setAttribute("selectedType", type == null ? "ALL" : type);
            request.setAttribute("featuredNews", featuredNews);
            request.setAttribute("newsList", latestNews);
            request.setAttribute("activePage", "news");
            request.getRequestDispatcher("/WEB-INF/views/customer/news.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Unable to load news", e);
        }
    }

    private String normalizeType(String type) {
        if (type == null) {
            return null;
        }
        String trimmed = type.trim().toUpperCase();
        if (trimmed.isEmpty() || "ALL".equals(trimmed)) {
            return null;
        }
        return trimmed;
    }
}
