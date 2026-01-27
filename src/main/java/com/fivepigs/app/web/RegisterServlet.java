package com.fivepigs.app.web;

import com.fivepigs.app.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller: Xử lý request/response cho chức năng đăng ký
 * Tuân theo mô hình MVC
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private final UserService userService;

    public RegisterServlet() {
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Hiển thị form đăng ký (View)
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        // Lấy dữ liệu từ form
        String fullName = trim(req.getParameter("fullName"));
        String email = trim(req.getParameter("email"));
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        // Giữ lại giá trị đã nhập để hiển thị lại nếu có lỗi
        req.setAttribute("fullName", fullName);
        req.setAttribute("email", email);

        // Validation (delegate to Service layer)
        String error = userService.validateRegistration(fullName, email, password, confirmPassword);
        if (error != null) {
            req.setAttribute("error", error);
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        try {
            // Kiểm tra email đã tồn tại chưa
            if (userService.emailExists(email)) {
                req.setAttribute("error", "Email đã tồn tại");
                req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
                return;
            }

            // Đăng ký user (delegate to Service layer)
            userService.registerUser(fullName, email, password);

            // Chuyển đến trang thành công
            req.setAttribute("fullName", fullName);
            req.getRequestDispatcher("/WEB-INF/views/register_success.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Lỗi kết nối database", e);
        }
    }

    private static String trim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
