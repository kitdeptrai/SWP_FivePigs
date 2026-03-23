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
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AppServlet", urlPatterns = {"/app"})
public class AppServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("activePage", "apps");
        SoftwareDao sdao = new SoftwareDao();
        String selectedGenre = normalizeGenre(request.getParameter("genre"));
        String selectedSort = normalizeSort(request.getParameter("sort"));
        String selectedOrder = normalizeOrder(request.getParameter("order"));

        try {
            List<Software> softwareList = sdao.getSoftwareByCategoryWithIcon("1");
            Map<String, List<Software>> sections = new LinkedHashMap<>();
            List<String> genres = new ArrayList<>();
            List<Software> genreResults = new ArrayList<>();

            try {
                genres = sdao.getGenresByCategory(1);
                if (selectedGenre == null) {
                    for (String genre : genres) {
                        List<Software> list = sdao.getSoftwareByCategoryAndGenre(1, genre);
                        sections.put(genre, list);
                    }
                } else {
                    genreResults = sdao.getSoftwareByCategoryAndGenre(1, selectedGenre);
                }
            } catch (SQLException ignored) {
                request.setAttribute("appWarning", "Thieu bang genre/software_genre, dang hien thi danh sach app mac dinh.");
                selectedGenre = null;
            }

            if (selectedGenre == null && sections.isEmpty()) {
                sortSoftwareList(softwareList, selectedSort, selectedOrder);
                sections.put("All Apps", softwareList != null ? softwareList : new ArrayList<>());
            }

            if (selectedGenre != null && genreResults.isEmpty()) {
                request.setAttribute("appWarning", "Khong tim thay app thuoc genre '" + selectedGenre + "'. Dang hien thi ket qua rong.");
            }

            request.setAttribute("genres", genres);
            request.setAttribute("selectedGenre", selectedGenre);
            request.setAttribute("selectedSort", selectedSort);
            request.setAttribute("selectedOrder", selectedOrder);
            request.setAttribute("genreResults", genreResults);
            request.setAttribute("sections", sections);
            request.setAttribute("softwareToShow", softwareList);
        } catch (SQLException e) {
            request.setAttribute("genres", new ArrayList<String>());
            request.setAttribute("selectedGenre", selectedGenre);
            request.setAttribute("selectedSort", selectedSort);
            request.setAttribute("selectedOrder", selectedOrder);
            request.setAttribute("genreResults", new ArrayList<Software>());
            request.setAttribute("sections", new LinkedHashMap<String, List<Software>>());
            request.setAttribute("softwareToShow", new ArrayList<>());
            request.setAttribute("appWarning", "Khong tai duoc du lieu app tu database.");
        }

        request.getRequestDispatcher("/WEB-INF/views/customer/app.jsp").forward(request, response);
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
}
