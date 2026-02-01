package com.fivepigs.app.web;

import com.fivepigs.app.service.UserService;
import com.fivepigs.app.util.EmailService;
import com.fivepigs.app.util.OtpUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/forgot_password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");

        try {
            if (email == null || email.trim().isEmpty()) {
                req.setAttribute("error", "Vui lòng nhập email.");
                req.getRequestDispatcher("/WEB-INF/views/forgot_password.jsp").forward(req, resp);
                return;
            }

            // Avoid user enumeration: always show a generic success message.
            boolean exists = userService.emailExists(email);

            HttpSession session = req.getSession();

            // Simple rate limit: 1 OTP per 60s per session
            Long lastSentAt = (Long) session.getAttribute("reset_otp_last_sent");
            if (lastSentAt != null && System.currentTimeMillis() - lastSentAt < 60_000) {
                req.setAttribute("email", email);
                req.setAttribute("success", "Nếu email tồn tại trong hệ thống, mã OTP sẽ được gửi. Vui lòng kiểm tra hộp thư (và spam). Bạn có thể thử gửi lại sau 60 giây.");
                req.getRequestDispatcher("/WEB-INF/views/forgot_password.jsp").forward(req, resp);
                return;
            }

            if (exists) {
                String otp = OtpUtil.generateOtp();

                session.setAttribute("reset_email", email);
                session.setAttribute("reset_otp", otp);
                session.setAttribute("reset_otp_expire", System.currentTimeMillis() + 5 * 60 * 1000); // 5 minutes
                session.setAttribute("reset_otp_last_sent", System.currentTimeMillis());

                String emailBody = "<div style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                        + "<h2>Yêu cầu đặt lại mật khẩu</h2>"
                        + "<p>Chúng tôi đã nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn. Vui lòng sử dụng mã OTP dưới đây:</p>"
                        + "<h3 style='color: #dc3545; font-size: 24px; letter-spacing: 2px;'><b>" + otp + "</b></h3>"
                        + "<p>Mã OTP này sẽ hết hạn trong 5 phút.</p>"
                        + "<p>Nếu bạn không yêu cầu điều này, vui lòng bỏ qua email này.</p>"
                        + "</div>";

                new Thread(() -> EmailService.sendHtmlEmail(email, "Yêu cầu đặt lại mật khẩu", emailBody)).start();

                // Require OTP verification before allowing password change
                session.setAttribute("reset_verified", false);
                resp.sendRedirect(req.getContextPath() + "/verify-reset-otp");
                return;
            }

            req.setAttribute("email", email);
            req.setAttribute("success", "Nếu email tồn tại trong hệ thống, mã OTP sẽ được gửi. Vui lòng kiểm tra hộp thư (và spam)."
                    + " Nếu bạn đã nhận được OTP, bạn có thể tiếp tục đặt lại mật khẩu.");
            req.getRequestDispatcher("/WEB-INF/views/forgot_password.jsp").forward(req, resp);
            return;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Lỗi database khi kiểm tra email", e);
        }
    }
}
