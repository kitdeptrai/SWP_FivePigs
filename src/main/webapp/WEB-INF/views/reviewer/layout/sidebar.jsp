<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<aside class="sidebar">
    <div class="sidebar-top">
        <div class="logo">
            <img src="${pageContext.request.contextPath}/assets/images/pig.png" alt="Pig Logo">
            <div class="logo-text">
                <h2>FivePigs</h2>
                <span>Software Market</span>
            </div>
        </div>

        <ul class="menu">
            <li>
                <a href="${pageContext.request.contextPath}/reviewer_dashboard"
                   class="menu-link ${activeMenu == 'dashboard' ? 'active' : ''}">
                    <span class="icon">📊</span>
                    <span class="text">Dashboard</span>
                </a>
            </li>

            <li>
                <a href="${pageContext.request.contextPath}/reviewer_pending"
                   class="menu-link ${activeMenu == 'pending' ? 'active' : ''}">
                    <span class="icon">🕒</span>
                    <span class="text">Pending Reviews</span>
                </a>
            </li>

            <li>
                <a href="${pageContext.request.contextPath}/reviewer_history"
                   class="menu-link ${activeMenu == 'history' ? 'active' : ''}">
                    <span class="icon">⏱</span>
                    <span class="text">Review History</span>
                </a>
            </li>

            <li>
                <a href="${pageContext.request.contextPath}/reviewer_error_reports"
                   class="menu-link ${activeMenu == 'errorReports' ? 'active' : ''}">
                    <span class="icon">🐞</span>
                    <span class="text">Error Reports</span>
                </a>
            </li>

            <li>
                <a href="${pageContext.request.contextPath}/reviewer_guidelines"
                   class="menu-link ${activeMenu == 'guidelines' ? 'active' : ''}">
                    <span class="icon">📖</span>
                    <span class="text">Review Guidelines</span>
                </a>
            </li>

            <li>
                <a href="${pageContext.request.contextPath}/reviewer_notifications"
                   class="menu-link ${activeMenu == 'notifications' ? 'active' : ''}">
                    <span class="icon">🔔</span>
                    <span class="text">Notifications</span>
                </a>
            </li>

            <li>
                <a href="${pageContext.request.contextPath}/reviewer_profile"
                   class="menu-link ${activeMenu == 'profile' ? 'active' : ''}">
                    <span class="icon">👤</span>
                    <span class="text">Profile</span>
                </a>
            </li>
        </ul>
    </div>

    <div class="sidebar-bottom">
        <a href="${pageContext.request.contextPath}/reviewer_profile" class="user-box profile-link">
            <c:choose>
                <c:when test="${not empty user.avatar}">
                    <img
                        class="sidebar-avatar-img"
                        src="${pageContext.request.contextPath}/assets/${user.avatar}"
                        alt="Avatar"
                        loading="lazy"
                        onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/images/img2.png';">
                </c:when>
                <c:otherwise>
                    <div class="avatar">
                        <c:choose>
                            <c:when test="${not empty user.fullName}">
                                ${fn:toUpperCase(fn:substring(user.fullName, 0, 1))}
                            </c:when>
                            <c:otherwise>U</c:otherwise>
                        </c:choose>
                    </div>
                </c:otherwise>
            </c:choose>

            <div class="user-meta">
                <p class="name" title="${user.fullName}">
                    <c:choose>
                        <c:when test="${not empty user.fullName}">
                            ${user.fullName}
                        </c:when>
                        <c:otherwise>User</c:otherwise>
                    </c:choose>
                </p>
                <p class="role">Reviewer</p>
            </div>
        </a>

        <a class="logout" href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
</aside>