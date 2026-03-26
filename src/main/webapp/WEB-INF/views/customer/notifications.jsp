<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
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
            <div style="display:flex;gap:10px;align-items:center;flex-wrap:wrap;justify-content:flex-end;">
                <form method="get" action="${pageContext.request.contextPath}/notifications" style="display:flex;gap:10px;align-items:center;flex-wrap:wrap;background:#fff;padding:10px 12px;border-radius:14px;box-shadow:0 4px 16px rgba(15,23,42,0.05);">
                    <label for="notificationOrder" style="font-weight:700;color:#374151;">Sort by date</label>
                    <select id="notificationOrder" name="order" style="border:1px solid #dbe1ec;border-radius:10px;padding:8px 10px;">
                        <option value="desc" ${selectedOrder == 'desc' ? 'selected' : ''}>Newest first</option>
                        <option value="asc" ${selectedOrder == 'asc' ? 'selected' : ''}>Oldest first</option>
                    </select>
                    <button type="submit" class="install-btn" style="padding:8px 14px; box-shadow:none;">Apply</button>
                </form>
                <form method="post" action="${pageContext.request.contextPath}/notifications" style="display:inline;">
                    <input type="hidden" name="order" value="${selectedOrder}">
                    <input type="hidden" name="action" value="markAllRead">
                    <button type="submit" class="install-btn" style="padding:10px 18px; box-shadow:none;">Mark all as read</button>
                </form>
                <form method="post" action="${pageContext.request.contextPath}/notifications" style="display:inline;">
                    <input type="hidden" name="order" value="${selectedOrder}">
                    <input type="hidden" name="action" value="deleteAll">
                    <button type="submit" class="library-report-cancel" style="padding:10px 18px;">Delete all</button>
                </form>
            </div>
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
                            <form method="post" action="${pageContext.request.contextPath}/notifications" style="margin-left:auto;">
                                <input type="hidden" name="order" value="${selectedOrder}">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="notificationId" value="${n.notificationId}">
                                <button type="submit" name="readmark" class="install-btn" style="padding:10px 18px; box-shadow:none;>Mark read</button>
                                <button type="submit" class="library-report-cancel" style="padding:8px 12px;">Delete</button>
                            </form>
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
