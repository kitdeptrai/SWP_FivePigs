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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name="GameServlet", urlPatterns={"/game"})
public class GameServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("activePage", "games");
        SoftwareDao sdao = new SoftwareDao();

        try {
            List<Software> softwareList = sdao.getSoftwareByCategoryWithIcon("2");
            Map<String, List<Software>> sections = new LinkedHashMap<>();

            try {
                List<String> genres = sdao.getGenresByCategory(2); // category 3 = games
                for (String genre : genres) {
                    List<Software> list = sdao.getSoftwareByCategoryAndGenre(2, genre);
                    sections.put(genre, list);
                }
            } catch (SQLException ignored) {
                sections.put("All Games", softwareList);
                request.setAttribute("gameWarning", "Thieu bang genre/software_genre, dang hien thi danh sach game mac dinh.");
            }

            if (sections.isEmpty()) {
                sections.put("All Games", softwareList != null ? softwareList : new ArrayList<>());
            }

            request.setAttribute("sections", sections);
            request.setAttribute("softwareToShow", softwareList);
        } catch (SQLException e) {
            request.setAttribute("sections", new LinkedHashMap<String, List<Software>>());
            request.setAttribute("softwareToShow", new ArrayList<>());
            request.setAttribute("gameWarning", "Khong tai duoc du lieu game tu database.");
        }

        request.getRequestDispatcher("/WEB-INF/views/customer/game.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
