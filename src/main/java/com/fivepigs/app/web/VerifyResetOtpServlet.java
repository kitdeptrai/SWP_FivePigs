package com.fivepigs.app.web;

import com.fivepigs.app.util.EmailService;
import com.fivepigs.app.util.OtpUtil;
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

        // Handle resend OTP
        String resend = req.getParameter("resend");
        if ("true".equals(resend)) {
            Long lastSentAt = (Long) session.getAttribute("reset_otp_last_sent");
            if (lastSentAt != null && System.currentTimeMillis() - lastSentAt < 60_000) {
                req.setAttribute("error", "Vui lòng chờ 60 giây trước khi gửi lại OTP.");
            } else {
                String email = (String) session.getAttribute("reset_email");
                String otp = OtpUtil.generateOtp();
                session.setAttribute("reset_otp_hash", OtpUtil.hashOtp(otp));
                session.setAttribute("reset_otp_expire", System.currentTimeMillis() + 5 * 60 * 1000);
                session.setAttribute("reset_otp_last_sent", System.currentTimeMillis());
                session.setAttribute("reset_otp_attempts", 0);
                String emailBody = "<div style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                        + "<h2>Yêu cầu đặt lại mật khẩu</h2>"
                        + "<p>Chúng tôi đã nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn. Vui lòng sử dụng mã OTP dưới đây:</p>"
                        + "<h3 style='color: #dc3545; font-size: 24px; letter-spacing: 2px;'><b>" + otp + "</b></h3>"
                        + "<p>Mã OTP này sẽ hết hạn trong 5 phút.</p>"
                        + "<p>Nếu bạn không yêu cầu điều này, vui lòng bỏ qua email này.</p>"
                        + "</div>";
                new Thread(() -> EmailService.sendHtmlEmail(email, "Yêu cầu đặt lại mật khẩu", emailBody)).start();
                req.setAttribute("success", "Đã gửi lại mã OTP. Vui lòng kiểm tra email.");
            }
            req.getRequestDispatcher("/WEB-INF/views/verify_reset_otp.jsp").forward(req, resp);
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
        String sessionOtpHash = (String) session.getAttribute("reset_otp_hash");
        Long expireTime = (Long) session.getAttribute("reset_otp_expire");
        Integer attempts = (Integer) session.getAttribute("reset_otp_attempts");
        if (attempts == null) attempts = 0;

        if (sessionOtpHash == null || expireTime == null || System.currentTimeMillis() > expireTime) {
            session.invalidate();
            req.setAttribute("error", "OTP đã hết hạn hoặc không hợp lệ. Vui lòng thử lại.");
            req.getRequestDispatcher("/WEB-INF/views/forgot_password.jsp").forward(req, resp);
            return;
        }

        if (inputOtp == null || !OtpUtil.hashOtp(inputOtp).equals(sessionOtpHash)) {
            attempts++;
            session.setAttribute("reset_otp_attempts", attempts);
            if (attempts >= 5) {
                session.invalidate();
                req.setAttribute("error", "Bạn đã nhập sai OTP quá 5 lần. Vui lòng thực hiện lại.");
                req.getRequestDispatcher("/WEB-INF/views/forgot_password.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("error", "Mã OTP không chính xác. Còn " + (5 - attempts) + " lần thử.");
            req.getRequestDispatcher("/WEB-INF/views/verify_reset_otp.jsp").forward(req, resp);
            return;
        }

        session.setAttribute("reset_verified", true);
        resp.sendRedirect(req.getContextPath() + "/reset-password");
    }
}
