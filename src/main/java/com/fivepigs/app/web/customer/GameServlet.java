package com.fivepigs.app.web.customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.dao.SoftwareDao;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name="GameServlet", urlPatterns={"/game"})
public class GameServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet GameController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GameController at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("activePage", "games");
        SoftwareDao sdao = new SoftwareDao();
        String selectedGenre = normalizeGenre(request.getParameter("genre"));
        String selectedSort = normalizeSort(request.getParameter("sort"));
        String selectedOrder = normalizeOrder(request.getParameter("order"));

        try {
            List<Software> softwareList = sdao.getSoftwareByCategoryWithIcon("2");
            Software featuredGame = sdao.getTopDownloadedByCategoryWithIcon(2);
            Software randomGame = sdao.getRandomSoftwareByCategoryWithIcon(2);
            Map<String, List<Software>> sections = new LinkedHashMap<>();
            List<String> genres = new ArrayList<>();
            List<Software> genreResults = new ArrayList<>();

            try {
                genres = sdao.getGenresByCategory(2);
                if (selectedGenre == null) {
                    for (String genre : genres) {
                        List<Software> list = sdao.getSoftwareByCategoryAndGenre(2, genre);
                        sortSoftwareList(list, selectedSort, selectedOrder);
                        sections.put(genre, list);
                    }
                } else {
                    genreResults = sdao.getSoftwareByCategoryAndGenre(2, selectedGenre);
                    sortSoftwareList(genreResults, selectedSort, selectedOrder);
                }
            } catch (SQLException ignored) {
                request.setAttribute("gameWarning", "Thieu bang genre/software_genre, dang hien thi danh sach game mac dinh.");
                selectedGenre = null;
            }

            if (selectedGenre == null && sections.isEmpty()) {
                sortSoftwareList(softwareList, selectedSort, selectedOrder);
                sections.put("All Games", softwareList != null ? softwareList : new ArrayList<>());
            }

            if (selectedGenre != null && genreResults.isEmpty()) {
                request.setAttribute("gameWarning", "Khong tim thay game thuoc genre '" + selectedGenre + "'. Dang hien thi ket qua rong.");
            }

            request.setAttribute("genres", genres);
            request.setAttribute("selectedGenre", selectedGenre);
            request.setAttribute("selectedSort", selectedSort);
            request.setAttribute("selectedOrder", selectedOrder);
            request.setAttribute("genreResults", genreResults);
            request.setAttribute("sections", sections);
            request.setAttribute("softwareToShow", softwareList);
            request.setAttribute("featuredGame", featuredGame);
            request.setAttribute("randomGame", randomGame);
        } catch (SQLException e) {
            request.setAttribute("genres", new ArrayList<String>());
            request.setAttribute("selectedGenre", selectedGenre);
            request.setAttribute("selectedSort", selectedSort);
            request.setAttribute("selectedOrder", selectedOrder);
            request.setAttribute("genreResults", new ArrayList<Software>());
            request.setAttribute("sections", new LinkedHashMap<String, List<Software>>());
            request.setAttribute("softwareToShow", new ArrayList<Software>());
            request.setAttribute("featuredGame", null);
            request.setAttribute("randomGame", null);
            request.setAttribute("gameWarning", "Khong tai duoc du lieu game tu database.");
        }

        request.getRequestDispatcher("/WEB-INF/views/customer/game.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private String normalizeGenre(String genre) {
        if (genre == null) {
            return null;
        }
        String trimmed = genre.trim();
        if (trimmed.isEmpty() || "all".equalsIgnoreCase(trimmed)) {
            return null;
        }
        return trimmed;
    }

    private String normalizeSort(String sort) {
        if ("price".equalsIgnoreCase(sort) || "name".equalsIgnoreCase(sort)) {
            return sort.toLowerCase();
        }
        return "name";
    }

    private String normalizeOrder(String order) {
        if ("desc".equalsIgnoreCase(order)) {
            return "desc";
        }
        return "asc";
    }

    private void sortSoftwareList(List<Software> list, String sort, String order) {
        if (list == null || list.isEmpty()) {
            return;
        }

        Comparator<Software> comparator;
        if ("price".equals(sort)) {
            comparator = Comparator.comparing(sw -> sw.getPrice() == null ? 0.0 : sw.getPrice());
        } else {
            comparator = Comparator.comparing(sw -> sw.getName() == null ? "" : sw.getName().toLowerCase());
        }

        if ("desc".equals(order)) {
            comparator = comparator.reversed();
        }

        list.sort(comparator.thenComparing(Software::getSoftwareId, Comparator.nullsLast(Integer::compareTo)));
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
