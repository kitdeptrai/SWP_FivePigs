package com.fivepigs.app.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/verify-reset-otp")
public class VerifyResetOtpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("reset_email") == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        req.getRequestDispatcher("/WEB-INF/views/verify_reset_otp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("reset_email") == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        String inputOtp = req.getParameter("otp");
        String sessionOtp = (String) session.getAttribute("reset_otp");
        Long expireTime = (Long) session.getAttribute("reset_otp_expire");

        if (sessionOtp == null || expireTime == null || System.currentTimeMillis() > expireTime) {
            session.invalidate();
            req.setAttribute("error", "OTP đã hết hạn hoặc không hợp lệ. Vui lòng thử lại.");
            req.getRequestDispatcher("/WEB-INF/views/forgot_password.jsp").forward(req, resp);
            return;
        }

        if (inputOtp == null || !sessionOtp.equals(inputOtp)) {
            req.setAttribute("error", "Mã OTP không chính xác.");
            req.getRequestDispatcher("/WEB-INF/views/verify_reset_otp.jsp").forward(req, resp);
            return;
        }

        session.setAttribute("reset_verified", true);
        resp.sendRedirect(req.getContextPath() + "/reset-password");
    }
}
