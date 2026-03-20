package com.fivepigs.app.web.payment;

import com.fivepigs.app.config.VNPayConfig;
import com.fivepigs.app.util.VNPayUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "VNPayPaymentServlet", urlPatterns = {"/create-payment"})
public class VNPayPaymentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object checkoutPendingUser = session == null ? null : session.getAttribute("checkout_pending_user_id");
        Object vendorApplyPendingUser = session == null ? null : session.getAttribute("vendor_apply_pending_user_id");

        if (session == null || (checkoutPendingUser == null && vendorApplyPendingUser == null)) {
            response.sendRedirect(request.getContextPath() + "/cart?msg=empty");
            return;
        }

        boolean isVendorApplyFlow = vendorApplyPendingUser != null;
        Object totalAttr = isVendorApplyFlow
                ? session.getAttribute("vendor_apply_total")
                : session.getAttribute("checkout_total");

        Double total = totalAttr instanceof Double ? (Double) totalAttr : null;
        if (total == null || total <= 0) {
            response.sendRedirect(request.getContextPath() + (isVendorApplyFlow ? "/vendor-apply?msg=invalid_amount" : "/cart?msg=empty"));
            return;
        }

        long amount = Math.round(total * 24000 * 100);
        String txnRef = String.valueOf(System.currentTimeMillis());  //dung thoi gian hien tai lam ma giao dich
        session.setAttribute("vnp_txn_ref", txnRef);

        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        params.put("vnp_Amount", String.valueOf(amount));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", txnRef);
        params.put("vnp_OrderInfo", (isVendorApplyFlow ? "FIVEPIGS vendor apply " : "FIVEPIGS checkout ") + txnRef);
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", buildReturnUrl(request));
        params.put("vnp_IpAddr", request.getRemoteAddr());

        Calendar cld = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        params.put("vnp_CreateDate", formatter.format(cld.getTime()));

        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if (hashData.length() > 0) {
                hashData.append('&');
                query.append('&');
            }
            String encoded = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8);
            hashData.append(fieldName).append('=').append(encoded);
            query.append(fieldName).append('=').append(encoded);
        }

        String secureHash = VNPayUtil.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        response.sendRedirect(VNPayConfig.vnp_Url + "?" + query);
    }

    private String buildReturnUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + "/vnpay-return";
    }
}
