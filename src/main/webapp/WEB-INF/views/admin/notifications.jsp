<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="activeMenu" value="notifications" />

<html>
    <head>
        <title>Admin Notifications</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/vendor/vendor.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/reviewer/pending.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/vendor/notification.css">
        <link rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <style>
            :root {
                --dark-blue: #1e293b;
                --sidebar-bg: #0f172a;
                --card-bg: #ffffff;
                --text-main: #334155;
                --bg-gray: #f1f5f9;
                --accent: #3b82f6;
            }

            body {
                background-color: var(--bg-gray);
                color: var(--text-main);
                margin: 0;
                font-family: 'Inter', system-ui, -apple-system, sans-serif;
            }

            .layout {
                display: grid;
                grid-template-columns: 260px 1fr;
                min-height: 100vh;
            }

            .sidebar {
                background-color: var(--sidebar-bg);
                color: white;
                padding: 24px 16px;
                display: flex;
                flex-direction: column;
            }

            .sidebar h2 {
                font-size: 24px;
                margin-bottom: 32px;
                color: #fff;
                padding: 0 12px;
            }

            .menu-item {
                padding: 12px 16px;
                border-radius: 8px;
                color: #94a3b8;
                text-decoration: none;
                margin-bottom: 4px;
                display: flex;
                align-items: center;
                gap: 12px;
                transition: all 0.2s;
            }

            .menu-item:hover {
                background-color: rgba(255, 255, 255, 0.1);
                color: white;
            }

            .menu-item.active {
                background-color: var(--accent);
                color: white;
            }

            .logout-btn {
                margin-top: auto;
                color: #ef4444;
                font-weight: 600;
            }

            .logout-btn:hover {
                background-color: rgba(239, 68, 68, 0.1);
            }

            /* OVERRIDE NOTIFICATIONS.CSS FOR LIGHT THEME */
            .notifications-page {
                --bg-card: #ffffff;
                --bg-card-hover: #f8fafc;
                --border-soft: #e2e8f0;
                --border-strong: #cbd5e1;
                --text-main: #1e293b;
                --text-soft: #475569;
                --text-muted: #64748b;
                --primary-soft: #eff6ff;
                --danger-soft: #fef2f2;
                --shadow-soft: 0 1px 3px rgba(0,0,0,0.1);
            }

            .notifications-page .chip,
            .notifications-page .chip:visited {
                background: #ffffff;
                color: #334155;
                border: 1px solid #e2e8f0;
            }

            .notifications-page .chip:hover {
                background: #f1f5f9;
            }

            .notifications-page .chip.active {
                background: var(--accent);
                color: #ffffff;
                border-color: var(--accent);
            }

            .notifications-page .noti-search-box input {
                color: #1e293b;
            }

            .notifications-page .noti-search-box i {
                color: #64748b;
            }

            .notifications-page .noti-btn {
                background: #ffffff;
                border: 1px solid #cbd5e1;
            }

            .notifications-page .b-low {
                background: #f1f5f9;
                color: #475569;
                border-color: #cbd5e1;
            }
        </style>
    </head>
    <body>
        <div class="layout">
            <jsp:include page="sidebar.jsp" />
            <main class="main notifications-page">

                <div class="noti-head">
                    <div class="noti-head-left">
                        <h1 class="page-title">Admin Notifications</h1>
                        <p class="subtitle">System events, payouts, and user management alerts</p>
                    </div>

                    <div class="noti-head-right">
                        <form action="${pageContext.request.contextPath}/admin_notifications" method="post">
                            <input type="hidden" name="action" value="markAllRead" />
                            <button type="submit" class="noti-btn">
                                <i class="fa-solid fa-check"></i> Mark All Read
                            </button>
                        </form>

                        <form action="${pageContext.request.contextPath}/admin_notifications"
                              method="post" onsubmit="return confirm('Delete ALL notifications?');">
                            <input type="hidden" name="action" value="deleteAll" />
                            <button type="submit" class="noti-btn danger">
                                <i class="fa-solid fa-trash"></i> Delete All
                            </button>
                        </form>
                    </div>
                </div>

                <div class="noti-stats">
                    <div class="stat-card">
                        <div class="stat-icon"><i class="fa-solid fa-bell"></i></div>
                        <h2>${totalCount}</h2>
                        <p>Total Notifications</p>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon"><i class="fa-solid fa-bell-slash"></i></div>
                        <h2>${unreadCount}</h2>
                        <p>Unread</p>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon red"><i class="fa-solid fa-circle-exclamation"></i></div>
                        <h2>${highCount}</h2>
                        <p>High Priority</p>
                    </div>
                </div>

                <div class="noti-filters">
                    <div class="filter-label"><i class="fa-solid fa-filter"></i> Filters:</div>

                    <a class="chip ${selectedRead eq 'all' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/admin_notifications?read=all&type=${selectedType}&keyword=${keyword}">All</a>
                    <a class="chip ${selectedRead eq 'unread' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/admin_notifications?read=unread&type=${selectedType}&keyword=${keyword}">Unread (${unreadCount})</a>
                    <a class="chip ${selectedRead eq 'read' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/admin_notifications?read=read&type=${selectedType}&keyword=${keyword}">Read</a>

                    <div class="filter-divider"></div>

                    <a class="chip ${selectedType eq 'all' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/admin_notifications?read=${selectedRead}&type=all&keyword=${keyword}">All Types</a>
                    <a class="chip ${selectedType eq 'PAYOUT_REQUEST' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/admin_notifications?read=${selectedRead}&type=PAYOUT_REQUEST&keyword=${keyword}">Payouts</a>
                    <a class="chip ${selectedType eq 'NEW_USER' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/admin_notifications?read=${selectedRead}&type=NEW_USER&keyword=${keyword}">New Users</a>
                    <a class="chip ${selectedType eq 'USER_UPDATE' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/admin_notifications?read=${selectedRead}&type=USER_UPDATE&keyword=${keyword}">User Updates</a>
                </div>

                <form class="noti-search-wrapper" method="get" action="${pageContext.request.contextPath}/admin_notifications">
                    <input type="hidden" name="read" value="${selectedRead}">
                    <input type="hidden" name="type" value="${selectedType}">
                    <div class="noti-search-box">
                        <i class="fa-solid fa-magnifying-glass"></i>
                        <input type="text" name="keyword" value="${keyword}" placeholder="Search notifications..." />
                    </div>
                </form>

                <div class="pending-container">
                    <c:if test="${empty notifications}">
                        <div class="noti-card">
                            <div class="noti-body">
                                <div class="noti-title">No notifications</div>
                                <div class="noti-desc">No matching notifications found.</div>
                            </div>
                        </div>
                    </c:if>

                    <c:forEach var="n" items="${notifications}">
                        <fmt:formatDate var="formattedCreatedAt" value="${n.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                        <div class="noti-card ${!n.read ? 'unread-card' : ''}">
                            <div class="noti-left"><i class="fa-regular fa-clock"></i></div>
                            <div class="noti-body">
                                <div class="noti-top">
                                    <div class="noti-title-line">
                                        <span class="noti-title">${n.title}</span>

                                        <c:choose>
                                            <c:when test="${n.priority eq 'HIGH' || n.priority eq 'CRITICAL'}">
                                                <span class="badge b-high">High Priority</span>
                                            </c:when>
                                            <c:when test="${n.priority eq 'MEDIUM'}">
                                                <span class="badge b-med">Medium</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge b-low">Low</span>
                                            </c:otherwise>
                                        </c:choose>

                                        <c:if test="${!n.read}">
                                            <span class="badge b-unread">Unread</span>
                                        </c:if>

                                        <c:choose>
                                            <c:when test="${n.type eq 'PAYOUT_REQUEST'}">
                                                <span class="badge b-high">Payout Request</span>
                                            </c:when>
                                            <c:when test="${n.type eq 'NEW_USER'}">
                                                <span class="badge b-low">New User</span>
                                            </c:when>
                                            <c:when test="${n.type eq 'USER_UPDATE'}">
                                                <span class="badge b-med">User Update</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge b-low">${n.type}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="noti-time">${formattedCreatedAt}</div>
                                </div>
                                <div class="noti-desc">${n.content}</div>

                                <div class="noti-actions">
                                    <a href="${pageContext.request.contextPath}${n.relatedUrl}" class="button btn-dark" style="text-decoration: none; display: inline-block;">
                                        View Details <i class="fa-solid fa-chevron-right"></i>
                                    </a>

                                    <form action="${pageContext.request.contextPath}/admin_notifications" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="toggle" />
                                        <input type="hidden" name="id" value="${n.notificationId}">
                                        <button type="submit" class="link-action">
                                            <i class="fa-solid fa-check"></i> <c:choose><c:when test="${n.read}">Mark Unread</c:when><c:otherwise>Mark Read</c:otherwise></c:choose>
                                                </button>
                                            </form>

                                                <form action="${pageContext.request.contextPath}/admin_notifications" method="post" style="display:inline;" onsubmit="return confirm('Delete this notification?');">
                                        <input type="hidden" name="action" value="delete" />
                                        <input type="hidden" name="id" value="${n.notificationId}">
                                        <button type="submit" class="link-action danger">
                                            <i class="fa-solid fa-trash"></i> Delete
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </main>
        </div>
    </body>
</html>