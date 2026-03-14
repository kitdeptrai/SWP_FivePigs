package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.CategoryDao;
import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.Category;
import com.fivepigs.app.model.SoftwareImage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="ProductServlet", urlPatterns={"/product"})
public class ProductServlet extends HttpServlet {

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
            out.println("<title>Servlet ProductController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProductController at " + request.getContextPath () + "</h1>");
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

        String id = request.getParameter("pid");

        SoftwareDao sdao = new SoftwareDao();
        CategoryDao cdao = new CategoryDao();

        try {
            Software p = sdao.GETALLSOFTWAREBYID(id);
            SoftwareImage picon = sdao.getThumbnailBySoftwareId(id);

            List<Software> softwareToShow = new ArrayList<>();
            List<Category> listCate = new ArrayList<>();
            List<SoftwareImage> screenshots = sdao.getScreenshotsBySoftwareId(id);

            listCate = cdao.GETALLCATEGORY();
            softwareToShow = sdao.GET_ALL_SOFTWARE();

            request.setAttribute("softwareToShow", softwareToShow);
            request.setAttribute("listCate", listCate);
            request.setAttribute("detail", p);
            request.setAttribute("icon", picon);
            request.setAttribute("screenshots", screenshots);
            request.getRequestDispatcher("/WEB-INF/views/customer/single-product.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
