<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<%@ page import="com.fivepigs.app.model.User" %>
<%
    User currentUser = (User) session.getAttribute("user");
    String avatarSrc = null;
    if (currentUser != null) {
        String avatar = currentUser.getAvatar();
        if (avatar != null && !avatar.isBlank()) {
            avatarSrc = request.getContextPath() + "/assets/" + avatar;
        } else {
            avatarSrc = "https://ui-avatars.com/api/?name=" + currentUser.getFullName() + "&background=6c5ce7&color=fff";
        }
    }
%>

<div class="header">
    <form action="${pageContext.request.contextPath}/search" method="get" class="search-group">
        <i class="fa-solid fa-magnifying-glass" style="color: #b2bec3;"></i>
        <input type="text" name="q" value="${param.q}" placeholder="Search apps, games...">
        <c:if test="${not empty param.dept}">
            <input type="hidden" name="dept" value="${param.dept}">
        </c:if>
    </form>

    <div class="user-actions">
        <% if (currentUser != null) { %>
            <div class="icon-btn notification-wrap" tabindex="0">
                <i class="fa-regular fa-bell"></i>

                <c:if test="${unreadCount > 0}">
                    <span class="noti-badge">${unreadCount}</span>
                </c:if>

                <div class="noti-dropdown">
                    <div class="noti-dropdown-head">
                        <span>Notifications</span>
                        <a class="noti-view-all" href="${pageContext.request.contextPath}/notifications">View all</a>
                    </div>

                    <c:if test="${empty topNotifications}">
                        <div class="noti-empty">No notifications yet.</div>
                    </c:if>

                    <c:forEach var="n" items="${topNotifications}">
                        <a class="noti-item" href="${pageContext.request.contextPath}/notifications">
                            <div class="noti-title-row">
                                <div class="noti-title">${n.title}</div>
                                <c:if test="${not n.read}">
                                    <span class="noti-dot"></span>
                                </c:if>
                            </div>
                            <div class="noti-content">${n.content}</div>
                        </a>
                    </c:forEach>
                </div>
            </div>
        <% } else { %>
            <div class="icon-btn" title="Login to view notifications"><i class="fa-regular fa-bell"></i></div>
        <% } %>

        <% if (currentUser == null) { %>
            <a href="${pageContext.request.contextPath}/login"
               class="menu-item"
               style="margin-bottom:0; background:#6b70ff; color:#fff; padding:10px 16px; border-radius:10px;">
                <i class="fa-solid fa-right-to-bracket"></i> Login
            </a>
        <% } else { %>
            <div class="user-profile-container" onclick="toggleUserDropdown()">
                <div class="user-info">
                    <div class="avatar">
                        <img src="<%= avatarSrc %>" alt="User">
                    </div>
                    <span class="user-name"><%= currentUser.getFullName() %></span>
                    <i class="fa-solid fa-caret-down" style="font-size: 12px; color: #636e72;"></i>
                </div>

                <div id="userDropdown" class="dropdown-menu ms-account-menu">
                    <div class="ms-account-header">
                        <div class="ms-account-row">
                            <div class="avatar ms-account-avatar">
                                <img src="<%= avatarSrc %>" alt="User">
                            </div>
                            <div class="ms-account-meta">
                                <div class="ms-account-name"><%= currentUser.getFullName() %></div>
                                <div class="ms-account-email"><%= currentUser.getEmail() %></div>
                                <a href="${pageContext.request.contextPath}/logout" class="ms-account-signout">Sign out</a>
                            </div>
                        </div>
                    </div>

                    <a href="${pageContext.request.contextPath}/profile" class="dropdown-item"><i class="fa-regular fa-user"></i> My Profile</a>
                    <div class="divider"></div>
                    <a href="${pageContext.request.contextPath}/settings?tab=feedback" class="dropdown-item"><i class="fa-regular fa-message"></i> Send feedback</a>
                    <a href="${pageContext.request.contextPath}/settings?tab=store_settings" class="dropdown-item"><i class="fa-solid fa-gear"></i> Store settings</a>
                </div>
            </div>
        <% } %>
    </div>
</div>
