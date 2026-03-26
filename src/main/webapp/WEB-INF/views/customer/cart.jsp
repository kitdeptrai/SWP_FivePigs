<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FIVEPIGS - Cart</title>
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

    <div class="content-section active-section">
        <h2 style="margin-bottom: 20px;">My Cart</h2>

        <c:if test="${param.msg == 'added'}"><div style="margin-bottom:12px;padding:10px 12px;background:#e8f7ee;color:#1f7a44;border-radius:10px;">Added to cart.</div></c:if>
        <c:if test="${param.msg == 'exists'}"><div style="margin-bottom:12px;padding:10px 12px;background:#fff6e5;color:#8a5b00;border-radius:10px;">Product already exists in cart or is already in your library.</div></c:if>
        <c:if test="${param.msg == 'removed'}"><div style="margin-bottom:12px;padding:10px 12px;background:#eef2ff;color:#3044a5;border-radius:10px;">Removed from cart.</div></c:if>
        <c:if test="${param.msg == 'empty'}"><div style="margin-bottom:12px;padding:10px 12px;background:#fff6e5;color:#8a5b00;border-radius:10px;">Your cart is empty.</div></c:if>

        <div style="display:grid;grid-template-columns:2fr 1fr;gap:20px;align-items:start;">
            <div style="background:#fff;border-radius:16px;padding:16px;box-shadow:0 4px 10px rgba(0,0,0,0.04);">
                <c:choose>
                    <c:when test="${empty cartItems}">
                        <p style="color:#666;">There are no products in your cart yet.</p>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="item" items="${cartItems}">
                            <div style="display:flex;align-items:center;justify-content:space-between;padding:10px 0;border-bottom:1px solid #eee;gap:12px;">
                                <div style="display:flex;align-items:center;gap:10px;min-width:0;">
                                    <img src="${pageContext.request.contextPath}/assets/${item.iconUrl}" alt="${item.name}" style="width:54px;height:54px;border-radius:12px;object-fit:cover;" onerror="this.src='${pageContext.request.contextPath}/assets/images/default_icon.png'">
                                    <div>
                                        <div style="font-weight:700;">${item.name}</div>
                                        <div style="font-size:13px;color:#666;">
                                            <c:if test="${not empty item.planName}">
                                                <span>${item.planName}</span>
                                                <c:if test="${not empty item.planMaxUsers}">
                                                    <span> • ${item.planMaxUsers} user<c:if test="${item.planMaxUsers != 1}">s</c:if></span>
                                                </c:if>
                                                <span> � 365-day access</span>
                                                <br>
                                            </c:if>
                                            <c:choose>
                                                <c:when test="${item.isFree == 1}">Free</c:when>
                                                <c:otherwise>$${item.price}</c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                                <form method="post" action="${pageContext.request.contextPath}/cart" style="margin:0;">
                                    <input type="hidden" name="action" value="remove">
                                    <input type="hidden" name="softwareId" value="${item.softwareId}">
                                    <button type="submit" style="border:none;background:#ffecec;color:#cc2c2c;padding:8px 10px;border-radius:8px;cursor:pointer;">
                                        <i class="fa-solid fa-trash"></i>
                                    </button>
                                </form>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>

            <div style="background:#fff;border-radius:16px;padding:16px;box-shadow:0 4px 10px rgba(0,0,0,0.04);">
                <div style="font-size:14px;color:#666;">Items</div>
                <div style="font-size:22px;font-weight:800;margin-bottom:12px;">${cartCount}</div>
                <div style="font-size:14px;color:#666;">Total</div>
                <div style="font-size:26px;font-weight:800;margin-bottom:16px;">$${cartTotal}</div>

                <form method="post" action="${pageContext.request.contextPath}/cart">
                    <input type="hidden" name="action" value="checkout">
                    <button type="submit" style="width:100%;border:none;background:var(--primary-color);color:#fff;padding:12px 14px;border-radius:10px;font-weight:700;cursor:pointer;">
                        Checkout
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>

