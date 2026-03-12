<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FIVEPIGS - Notifications</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/customer/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/customer/header.jsp"></jsp:include>

    <div class="content-section active-section notifications-page-customer">
        <div class="notifications-page-head">
            <div>
                <h2 class="page-title">Notifications</h2>
                <p class="notifications-subtitle">Recent store updates, purchases and account alerts.</p>
            </div>
            <form method="post" action="${pageContext.request.contextPath}/notifications">
                <input type="hidden" name="action" value="markAllRead">
                <button type="submit" class="install-btn" style="padding:10px 18px; box-shadow:none;">Mark all as read</button>
            </form>
        </div>

        <c:choose>
            <c:when test="${empty notifications}">
                <div class="notifications-empty-card">
                    <i class="fa-regular fa-bell-slash"></i>
                    <h3>No notifications yet</h3>
                    <p>When the store has something important for you, it will show up here.</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="notifications-list-customer">
                    <c:forEach var="n" items="${notifications}">
                        <div class="notification-card-customer ${n.read ? '' : 'unread'}">
                            <div class="notification-icon-customer">
                                <i class="fa-regular fa-bell"></i>
                            </div>
                            <div class="notification-content-customer">
                                <div class="notification-title-row-customer">
                                    <h3>${n.title}</h3>
                                    <c:if test="${not n.read}">
                                        <span class="notification-pill-customer">New</span>
                                    </c:if>
                                </div>
                                <p>${n.content}</p>
                                <small>${n.createdAt}</small>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
