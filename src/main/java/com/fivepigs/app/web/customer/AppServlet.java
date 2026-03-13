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

        try {
            List<Software> softwareList = sdao.getSoftwareByCategoryWithIcon("1");
            Map<String, List<Software>> sections = new LinkedHashMap<>();

            try {
                List<String> genres = sdao.getGenresByCategory(1); // category 1 = apps
                for (String genre : genres) {
                    List<Software> list = sdao.getSoftwareByCategoryAndGenre(1, genre);
                    sections.put(genre, list);
                }
            } catch (SQLException ignored) {
                sections.put("All Apps", softwareList);
                request.setAttribute("appWarning", "Thieu bang genre/software_genre, dang hien thi danh sach app mac dinh.");
            }

            if (sections.isEmpty()) {
                sections.put("All Apps", softwareList != null ? softwareList : new ArrayList<>());
            }

            request.setAttribute("sections", sections);
            request.setAttribute("softwareToShow", softwareList);
        } catch (SQLException e) {
            request.setAttribute("sections", new LinkedHashMap<String, List<Software>>());
            request.setAttribute("softwareToShow", new ArrayList<>());
            request.setAttribute("appWarning", "Khong tai duoc du lieu app tu database.");
        }

        request.getRequestDispatcher("/WEB-INF/views/customer/app.jsp").forward(request, response);
    }
}
