/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fivepigs.app.web.vendor;

import com.fivepigs.app.dao.ReviewDao;
import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.dao.SoftwareImageDao;
import com.fivepigs.app.model.Review;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.SoftwareImage;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author MinhPD
 */
@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/product_detail"})
public class ProductDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Integer softwareId = Integer.parseInt(request.getParameter("softwareId"));
            SoftwareDao swdao = new SoftwareDao();
            SoftwareImageDao idao = new SoftwareImageDao();
            ReviewDao rwdao = new ReviewDao();
            Integer downloadCount = swdao.getDownloadBySoftwareId(softwareId);
            Double revenue = swdao.getRevenueBySoftwareId(softwareId);
            Double avgRating = swdao.getRatingBySoftwareId(softwareId);
            Integer totalLicense = swdao.getTotalLicenseBySoftwareId(softwareId);
            Software infoSoftware = swdao.getSoftwareDetailBySoftwareId(softwareId);
            List<SoftwareImage> listImage = idao.getImagesBySoftwareId(softwareId);
            List<Review> listReview = rwdao.getReviewListBySoftwareId(softwareId);
            request.setAttribute("listImage", listImage);
            request.setAttribute("listReview", listReview);
            request.setAttribute("infoSoftware", infoSoftware);
            request.setAttribute("downloadCount", downloadCount == null ? 0 : downloadCount);
            request.setAttribute("revenue", revenue == null ? 0 : revenue);
            request.setAttribute("avgRating", avgRating == null ? 0 : avgRating);
            request.setAttribute("totalLicense", totalLicense == null ? 0 : totalLicense);
            request.getRequestDispatcher("/WEB-INF/views/vendor/product_detail.jsp").forward(request, response);
        } catch (SQLException e) {

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
