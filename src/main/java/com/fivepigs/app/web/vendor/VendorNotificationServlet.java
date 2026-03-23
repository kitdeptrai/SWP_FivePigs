package com.fivepigs.app.web.vendor;

import com.fivepigs.app.dao.NotificationDao;
import com.fivepigs.app.model.Notification;
import com.fivepigs.app.model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

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

        String read = request.getParameter("read");
        String type = request.getParameter("type");
        String keyword = request.getParameter("keyword");

        if (read == null || read.isBlank()) read = "all";
        if (type == null || type.isBlank()) type = "all";
        if (keyword == null) keyword = "";

        NotificationDao dao = new NotificationDao();
        
        List<Notification> list = dao.filterByUser(userId, read, type, keyword);

        int total = dao.countByUser(userId);
        int unread = dao.countUnreadByUser(userId);
        int high = dao.countHighPriorityByUser(userId);

        int countApproved = dao.countByType(userId, "APPROVED");
        int countRejected = dao.countByType(userId, "REJECTED");

        request.setAttribute("notifications", list);
        request.setAttribute("totalCount", total);
        request.setAttribute("unreadCount", unread);
        request.setAttribute("highCount", high);
        
        request.setAttribute("countApproved", countApproved);
        request.setAttribute("countRejected", countRejected);
        
        request.setAttribute("selectedRead", read);
        request.setAttribute("selectedType", type);
        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("/WEB-INF/views/vendor/notification.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        String idStr = request.getParameter("id");
        
        NotificationDao dao = new NotificationDao();
        
        if ("markAllRead".equals(action)) {
            dao.markAllRead(user.getUserId());
        } else if ("deleteAll".equals(action)) {
            dao.deleteAll(user.getUserId());
        } else if ("toggle".equals(action) && idStr != null) {
            try {
                dao.toggleRead(Integer.parseInt(idStr));
            } catch(NumberFormatException e){}
        } else if ("delete".equals(action) && idStr != null) {
            try {
                dao.delete(Integer.parseInt(idStr));
            } catch(NumberFormatException e){}
        }

        response.sendRedirect(request.getContextPath() + "/vendor/notification");
    }

}
