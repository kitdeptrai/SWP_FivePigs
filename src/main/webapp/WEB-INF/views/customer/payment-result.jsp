<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FIVEPIGS - Payment Result</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css">
</head>
<body>
<div class="main-content" style="margin-left:0; max-width:760px; padding:48px 24px;">
    <div class="profile-card" style="max-width:560px; margin:0 auto; text-align:center;">
        <div style="font-size:52px; margin-bottom:16px; color:${success ? '#2f855a' : '#c53030'};">
            <i class="fa-solid ${success ? 'fa-circle-check' : 'fa-circle-xmark'}"></i>
        </div>
        <h2 class="page-title" style="margin-bottom:12px;">${message}</h2>
        <c:if test="${not empty amount}">
            <p style="margin-bottom:18px; color:#667085;">So tien: ${amount} VND</p>
        </c:if>
        <a href="${pageContext.request.contextPath}${success ? '/library?msg=checkout_success' : '/cart'}" class="install-btn" style="text-decoration:none; display:inline-flex; align-items:center; justify-content:center;">
            ${success ? 'Open Library' : 'Back to Cart'}
        </a>
    </div>
</div>
</body>
</html>
