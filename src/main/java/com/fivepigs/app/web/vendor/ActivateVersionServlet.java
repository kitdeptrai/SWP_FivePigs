/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fivepigs.app.web.vendor;

import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author MinhPD
 */
@WebServlet(name = "ActivateVersionServlet", urlPatterns = {"/vendor/activate_version"})
public class ActivateVersionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            // ===== CHECK LOGIN =====
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // ===== GET PARAM =====
            int versionId = Integer.parseInt(request.getParameter("versionId"));
            int softwareId = Integer.parseInt(request.getParameter("softwareId"));

            SoftwareDao dao = new SoftwareDao();

            // ===== LOGIC ACTIVE VERSION =====
            dao.activateVersion(softwareId, versionId);

            // ===== REDIRECT BACK =====
            response.sendRedirect(
                    request.getContextPath() + "/vendor/version_management?softwareId=" + softwareId
            );

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            e.printStackTrace(response.getWriter());
        }
    }

}
