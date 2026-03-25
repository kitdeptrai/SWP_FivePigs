<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FIVEPIGS - Verify Checkout OTP</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/customer/sidebar.jsp">
    <jsp:param name="activePage" value="cart"/>
</jsp:include>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/customer/header.jsp"></jsp:include>

    <div class="content-section active-section" style="max-width:720px;">
        <h2 style="margin-bottom:16px;">Verify Checkout OTP</h2>

        <c:if test="${param.sent == '1'}">
            <div style="margin-bottom:12px;padding:10px 12px;background:#e8f7ee;color:#1f7a44;border-radius:10px;">
                OTP has been sent to your email. Please enter it to confirm purchase.
            </div>
        </c:if>
        <c:if test="${not empty message}">
            <div style="margin-bottom:12px;padding:10px 12px;background:#e8f7ee;color:#1f7a44;border-radius:10px;">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div style="margin-bottom:12px;padding:10px 12px;background:#ffecec;color:#9a2c2c;border-radius:10px;">${error}</div>
        </c:if>

        <div style="background:#fff;border-radius:16px;padding:20px;box-shadow:0 4px 10px rgba(0,0,0,0.04);border:1px solid #eceff7;">
            <form method="post" action="${pageContext.request.contextPath}/verify-checkout-otp" style="display:grid;gap:12px;">
                <label style="font-weight:700;">Enter OTP</label>
                <input type="text" name="otp" maxlength="6" required placeholder="6-digit OTP"
                       style="padding:12px;border:1px solid #dfe3ef;border-radius:10px;outline:none;font-size:16px;letter-spacing:2px;">
                <button type="submit" style="border:none;background:var(--primary-color);color:#fff;padding:12px 14px;border-radius:10px;font-weight:700;cursor:pointer;">
                    Confirm Purchase
                </button>
            </form>

            <form method="post" action="${pageContext.request.contextPath}/verify-checkout-otp" style="margin-top:10px;">
                <input type="hidden" name="action" value="resend">
                <button type="submit" style="border:none;background:#eef1ff;color:#3044a5;padding:10px 14px;border-radius:10px;font-weight:700;cursor:pointer;">
                    Resend OTP
                </button>
            </form>

            <a href="${pageContext.request.contextPath}/cart" style="display:inline-block;margin-top:12px;color:#4f46e5;text-decoration:none;font-weight:700;">
                <i class="fa-solid fa-arrow-left"></i> Back to Cart
            </a>
        </div>
    </div>
</div>
</body>
</html>