package com.fivepigs.app.web;

import com.fivepigs.app.service.UserService;
import com.fivepigs.app.util.OtpUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/verify-register-otp")
public class VerifyRegisterOtpServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("reg_email") == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/verify_otp.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("reg_email") == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        String inputOtp = req.getParameter("otp");
        String otpHash = (String) session.getAttribute("reg_otp");
        Long expire = (Long) session.getAttribute("reg_otp_expire");
        Integer attempts = (Integer) session.getAttribute("reg_otp_attempts");
        if (attempts == null) attempts = 0;

        if (otpHash == null || expire == null || System.currentTimeMillis() > expire) {
            session.invalidate();
            req.setAttribute("error", "OTP đã hết hạn. Vui lòng đăng ký lại.");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        if (inputOtp == null || !OtpUtil.hashOtp(inputOtp).equals(otpHash)) {
            attempts++;
            session.setAttribute("reg_otp_attempts", attempts);
            if (attempts >= 5) {
                session.invalidate();
                req.setAttribute("error", "Bạn đã nhập sai OTP quá 5 lần. Vui lòng đăng ký lại.");
                req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("error", "OTP không đúng. Còn " + (5 - attempts) + " lần thử.");
            req.getRequestDispatcher("/WEB-INF/views/verify_otp.jsp").forward(req, resp);
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
