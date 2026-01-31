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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String fullName = trim(req.getParameter("fullName"));
        String email = trim(req.getParameter("email"));
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        req.setAttribute("fullName", fullName);
        req.setAttribute("email", email);

        String error = userService.validateRegistration(fullName, email, password, confirmPassword);
        if (error != null) {
            req.setAttribute("error", error);
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        try {
            if (userService.emailExists(email)) {
                req.setAttribute("error", "Email đã tồn tại");
                req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
                return;
            }

            // ===== OTP FLOW START =====

            String otp = com.fivepigs.app.util.OtpUtil.generateOtp();

            var session = req.getSession();
            session.setAttribute("reg_fullName", fullName);
            session.setAttribute("reg_email", email);
            session.setAttribute("reg_password", password);
            session.setAttribute("reg_otp", otp);
            session.setAttribute("reg_otp_expire",
                    System.currentTimeMillis() + 5 * 60 * 1000);

            // gửi mail async để không block
            new Thread(() ->
                    com.fivepigs.app.util.EmailService.sendEmail(
                            email,
                            "Verify Email OTP",
                            "<h2>Mã OTP của bạn: <b>" + otp + "</b></h2>"
                    )
            ).start();

            resp.sendRedirect(req.getContextPath() + "/verify-register-otp");

        } catch (SQLException e) {
            throw new ServletException("Lỗi database", e);
        }
    }


    private static String trim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
