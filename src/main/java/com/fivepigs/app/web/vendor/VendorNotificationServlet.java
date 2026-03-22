/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.fivepigs.app.web.vendor;

import com.fivepigs.app.dao.NotificationDao;
import com.fivepigs.app.model.Notification;
import com.fivepigs.app.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author MinhPD
 */
@WebServlet(name="VendorNotificationServlet", urlPatterns={"/vendor/notification"})
public class VendorNotificationServlet extends HttpServlet {

    @Override
 
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = user.getUserId();

        NotificationDao dao = new NotificationDao();
        List<Notification> list = dao.getByUser(userId);

        int total = list.size();
        int unread = 0;
        int high = 0;

        for (Notification n : list) {
            if (!n.isRead()) unread++;

            String p = n.getPriority();
            if (p != null && (p.equalsIgnoreCase("High") || p.equalsIgnoreCase("Critical"))) {
                high++;
            }
        }

        request.setAttribute("notifications", list);
        request.setAttribute("totalCount", total);
        request.setAttribute("unreadCount", unread);
        request.setAttribute("highCount", high);

        request.getRequestDispatcher("/WEB-INF/views/vendor/notification.jsp")
                .forward(request, response);
    }

     


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
       
    }

}
