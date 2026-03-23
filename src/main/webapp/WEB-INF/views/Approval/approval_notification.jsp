<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="activeMenu" value="notifications" />

<html>
    <head>
        <title>Notifications</title>

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/assets/reviewer/reviewer.css">

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/assets/reviewer/notifications.css">

        <link rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    </head>
    <body>

        <div class="layout">

            <%@ include file="layout/sidebar.jsp" %>

            <main class="main notifications-page">

                <div class="noti-head">
                    <div class="noti-head-left">
                        <h1 class="page-title">Notifications</h1>
                        <p class="subtitle">Stay updated with your review assignments and activities</p>
                    </div>

                    <div class="noti-head-right">
                        <form action="${pageContext.request.contextPath}/approval_notifications" method="post">
                            <input type="hidden" name="action" value="markAllRead">
                            <button type="submit" class="noti-btn">
                                <i class="fa-solid fa-check"></i> Mark All Read
                            </button>
                        </form>

                        <form action="${pageContext.request.contextPath}/approval_notifications"
                              method="post"
                              onsubmit="return confirm('Delete ALL notifications?');">
                            <input type="hidden" name="action" value="deleteAll">
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
                        <p>Unread Notifications</p>
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
                       href="${pageContext.request.contextPath}/approval_notifications?read=all&type=${selectedType}&keyword=${keyword}">
                        All
                    </a>

                    <a class="chip ${selectedRead eq 'unread' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/approval_notifications?read=unread&type=${selectedType}&keyword=${keyword}">
                        Unread (${unreadCount})
                    </a>

                    <a class="chip ${selectedRead eq 'read' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/approval_notifications?read=read&type=${selectedType}&keyword=${keyword}">
                        Read
                    </a>

                    <div class="filter-divider"></div>

                    <a class="chip ${selectedType eq 'all' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/approval_notifications?read=${selectedRead}&type=all&keyword=${keyword}">
                        All Types
                    </a>

                    <a class="chip ${selectedType eq 'SUBMITTED' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/approval_notifications?read=${selectedRead}&type=SUBMITTED&keyword=${keyword}">
                        Submitted
                    </a>

                    <a class="chip ${selectedType eq 'APPROVED' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/approval_notifications?read=${selectedRead}&type=APPROVED&keyword=${keyword}">
                        Approved
                    </a>

                    <a class="chip ${selectedType eq 'REJECTED' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/approval_notifications?read=${selectedRead}&type=REJECTED&keyword=${keyword}">
                        Rejected
                    </a>

                    <a class="chip ${selectedType eq 'PENDING_APPROVAL' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/approval_notifications?read=${selectedRead}&type=PENDING_APPROVAL&keyword=${keyword}">
                        Pending Approval
                    </a>
                </div>

                <form class="noti-search-wrapper" method="get"
                      action="${pageContext.request.contextPath}/approval_notifications">
                    <input type="hidden" name="read" value="${selectedRead}">
                    <input type="hidden" name="type" value="${selectedType}">
                    <div class="noti-search-box">
                        <i class="fa-solid fa-magnifying-glass"></i>
                        <input type="text"
                               name="keyword"
                               value="${keyword}"
                               placeholder="Search notifications..." />
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
                            <div class="noti-left">
                                <i class="fa-regular fa-clock"></i>
                            </div>

                            <div class="noti-body">
                                <div class="noti-top">
                                    <div class="noti-title-line">
                                        <span class="noti-title">${n.title}</span>

                                        <c:choose>
                                            <c:when test="${n.priority eq 'HIGH' || n.priority eq 'High' || n.priority eq 'CRITICAL' || n.priority eq 'Critical'}">
                                                <span class="badge b-high">High Priority</span>
                                            </c:when>
                                            <c:when test="${n.priority eq 'MEDIUM' || n.priority eq 'Medium'}">
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
                                            <c:when test="${n.type eq 'SUBMITTED'}">
                                                <span class="badge b-med">Submitted</span>
                                            </c:when>
                                            <c:when test="${n.type eq 'APPROVED'}">
                                                <span class="badge b-low">Approved</span>
                                            </c:when>
                                            <c:when test="${n.type eq 'REJECTED'}">
                                                <span class="badge b-high">Rejected</span>
                                            </c:when>
                                            <c:when test="${n.type eq 'PENDING_APPROVAL'}">
                                                <span class="badge b-med">Pending Approval</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge b-low">Other</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <div class="noti-time">${formattedCreatedAt}</div>
                                </div>

                                <div class="noti-desc">${n.content}</div>

                                <div class="noti-actions">
                                    <button type="button"
                                            class="link-action"
                                            data-id="${n.notificationId}"
                                            data-title="${fn:escapeXml(n.title)}"
                                            data-content="${fn:escapeXml(n.content)}"
                                            data-priority="${n.priority}"
                                            data-type="${n.type}"
                                            data-time="${formattedCreatedAt}"
                                            data-related-url="${fn:escapeXml(n.relatedUrl)}"
                                            onclick="openDetails(this)">
                                        View Details <i class="fa-solid fa-chevron-right"></i>
                                    </button>

                                    <form action="${pageContext.request.contextPath}/approval_notifications"
                                          method="post"
                                          style="display:inline;">
                                        <input type="hidden" name="action" value="toggle">
                                        <input type="hidden" name="id" value="${n.notificationId}">
                                        <button type="submit" class="link-action">
                                            <i class="fa-solid fa-check"></i>
                                            <c:choose>
                                                <c:when test="${n.read}">Mark Unread</c:when>
                                                <c:otherwise>Mark Read</c:otherwise>
                                            </c:choose>
                                        </button>
                                    </form>

                                    <form action="${pageContext.request.contextPath}/approval_notifications"
                                          method="post"
                                          style="display:inline;"
                                          onsubmit="return confirm('Delete this notification?');">
                                        <input type="hidden" name="action" value="delete">
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

        <div id="detailsModal" class="noti-modal" onclick="closeIfBackdrop(event)">
            <div class="noti-modal-box">
                <button type="button" class="noti-modal-close" onclick="closeModal()">✕</button>

                <div class="modal-header">
                    <div class="modal-icon">
                        <i class="fa-regular fa-clock"></i>
                    </div>
                    <div>
                        <div id="mTitle" class="modal-title"></div>
                        <div class="modal-badges">
                            <span id="mPriority" class="badge b-high">High Priority</span>
                            <span id="mType" class="badge b-low">Type</span>
                        </div>
                    </div>
                </div>

                <div id="mContent" class="modal-content"></div>

                <div class="modal-grid">
                    <div class="mini-card">
                        <div class="mini-label">
                            <i class="fa-regular fa-calendar"></i> DATE & TIME
                        </div>
                        <div id="mTime" class="mini-value"></div>
                    </div>

                    <div class="mini-card">
                        <div class="mini-label">
                            <i class="fa-regular fa-circle-check"></i> STATUS
                        </div>
                        <div id="mStatus" class="mini-value"></div>
                    </div>
                </div>

                <div class="modal-footer">
                    <button id="goBtn" type="button" class="btn-dark" onclick="goRelated()">
                        Go to Related Page <i class="fa-solid fa-arrow-right"></i>
                    </button>
                </div>
            </div>
        </div>

        <script>
            let currentRelatedUrl = "";

            function mapType(type) {
                if (type === "SUBMITTED") return "Submitted";
                if (type === "APPROVED") return "Approved";
                if (type === "REJECTED") return "Rejected";
                if (type === "PENDING_APPROVAL") return "Pending Approval";
                return "Other";
            }

            function mapStatus(type) {
                if (type === "SUBMITTED") return "Waiting for Review";
                if (type === "APPROVED") return "Approved";
                if (type === "REJECTED") return "Rejected";
                if (type === "PENDING_APPROVAL") return "Waiting for Final Approval";
                return "Active";
            }

            function openDetails(btn) {
                const title = btn.dataset.title || "";
                const content = btn.dataset.content || "";
                const priority = btn.dataset.priority || "";
                const type = btn.dataset.type || "";
                const time = btn.dataset.time || "";
                const relatedUrl = btn.dataset.relatedUrl || "";

                document.getElementById("mTitle").innerText = title;
                document.getElementById("mContent").innerText = content;
                document.getElementById("mTime").innerText = time;
                document.getElementById("mStatus").innerText = mapStatus(type);

                currentRelatedUrl = relatedUrl;

                const pri = priority.toLowerCase();
                const priEl = document.getElementById("mPriority");
                priEl.className = "badge " + (
                        (pri === "high" || pri === "critical") ? "b-high" :
                        (pri === "medium") ? "b-med" : "b-low"
                );
                priEl.innerText =
                        (pri === "high" || pri === "critical") ? "High Priority" :
                        (pri === "medium") ? "Medium" : "Low";

                const typeEl = document.getElementById("mType");
                typeEl.className =
                        "badge " + (
                                (type === "REJECTED") ? "b-high" :
                                (type === "SUBMITTED" || type === "PENDING_APPROVAL") ? "b-med" :
                                "b-low"
                        );
                typeEl.innerText = mapType(type);

                const goBtn = document.getElementById("goBtn");
                if (!currentRelatedUrl) {
                    goBtn.style.opacity = ".5";
                    goBtn.style.pointerEvents = "none";
                } else {
                    goBtn.style.opacity = "1";
                    goBtn.style.pointerEvents = "auto";
                }

                document.getElementById("detailsModal").classList.add("open");
                document.body.classList.add("modal-open");
            }

            function closeModal() {
                document.getElementById("detailsModal").classList.remove("open");
                document.body.classList.remove("modal-open");
            }

            function closeIfBackdrop(e) {
                if (e.target.id === "detailsModal") {
                    closeModal();
                }
            }

            function goRelated() {
                if (currentRelatedUrl) {
                    window.location.href = currentRelatedUrl;
                }
            }

            document.addEventListener("keydown", function (e) {
                if (e.key === "Escape") {
                    closeModal();
                }
            });
        </script>

    </body>
</html>