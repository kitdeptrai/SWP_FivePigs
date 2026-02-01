package com.fivepigs.app.web;

import com.fivepigs.app.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/verify-register-otp")
public class VerifyRegisterOtpServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/WEB-INF/views/verify_otp.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String inputOtp = req.getParameter("otp");
        var session = req.getSession();

        String otp = (String) session.getAttribute("reg_otp");
        Long expire = (Long) session.getAttribute("reg_otp_expire");

        if (otp == null || expire == null || System.currentTimeMillis() > expire) {
            req.setAttribute("error", "OTP hết hạn");
            doGet(req, resp);
            return;
        }

        if (!otp.equals(inputOtp)) {
            req.setAttribute("error", "OTP không đúng");
            doGet(req, resp);
            return;
        }

        try {
            String fullName = (String) session.getAttribute("reg_fullName");
            String email = (String) session.getAttribute("reg_email");
            String password = (String) session.getAttribute("reg_password");

            userService.registerUser(fullName, email, password);

            session.invalidate();

            resp.sendRedirect(req.getContextPath() + "/login?registered=1");

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
