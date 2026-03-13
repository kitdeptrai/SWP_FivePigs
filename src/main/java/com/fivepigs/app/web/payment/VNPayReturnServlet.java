package com.fivepigs.app.web.payment;

import com.fivepigs.app.dao.CartDao;
import com.fivepigs.app.dao.NotificationDao;
import com.fivepigs.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "VNPayReturnServlet", urlPatterns = {"/vnpay-return"})
public class VNPayReturnServlet extends HttpServlet {

    private final CartDao cartDao = new CartDao();
    private final NotificationDao notificationDao = new NotificationDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        String responseCode = request.getParameter("vnp_ResponseCode");
        String transactionStatus = request.getParameter("vnp_TransactionStatus");
        String amount = request.getParameter("vnp_Amount");
        String txnRef = request.getParameter("vnp_TxnRef");
        String sessionTxnRef = session == null ? null : (String) session.getAttribute("vnp_txn_ref");

        boolean success = user != null
                && txnRef != null
                && txnRef.equals(sessionTxnRef)
                && "00".equals(responseCode)
                && "00".equals(transactionStatus);

        if (success) {
            try {
                int count = cartDao.checkout(user.getUserId());
                if (count > 0) {
                    notificationDao.insertNotification(
                            user.getUserId(),
                            "Purchase completed",
                            count + " item(s) have been added to your Library."
                    );
                }
                request.setAttribute("message", "Thanh toan thanh cong!");
                request.setAttribute("success", true);
                if (amount != null && !amount.isBlank()) {
                    request.setAttribute("amount", Long.parseLong(amount) / 100);
                }
            } catch (SQLException e) {
                throw new ServletException(e);
            } finally {
                clearCheckoutSession(session);
            }
        } else {
            request.setAttribute("message", "Thanh toan that bai!");
            request.setAttribute("success", false);
            clearCheckoutSession(session);
        }

        request.getRequestDispatcher("/WEB-INF/views/customer/payment-result.jsp").forward(request, response);
    }

    private void clearCheckoutSession(HttpSession session) {
        if (session == null) {
            return;
        }
        session.removeAttribute("checkout_total");
        session.removeAttribute("checkout_pending_user_id");
        session.removeAttribute("vnp_txn_ref");
    }
}
