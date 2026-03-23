package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.AboutMediaDao;
import com.fivepigs.app.dao.SiteSettingDao;
import com.fivepigs.app.model.AboutMedia;
import com.fivepigs.app.model.SiteSetting;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet(name="AboutServlet", urlPatterns={"/aboutus"})
public class AboutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("activePage", "about");

        SiteSettingDao settingDao = new SiteSettingDao();
        AboutMediaDao mediaDao = new AboutMediaDao();

        try {
            Map<String, String> about = settingDao.getAllAsMap();
            List<AboutMedia> media = mediaDao.getAll();

            request.setAttribute("about", about);
            request.setAttribute("aboutMedia", media);

            request.getRequestDispatcher("/WEB-INF/views/customer/aboutus.jsp")
                    .forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
