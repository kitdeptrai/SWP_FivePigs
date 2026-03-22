<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
                        <form action="${pageContext.request.contextPath}/reviewer_notification_mark_all_read" method="post">
                            <button type="submit" class="noti-btn">
                                <i class="fa-solid fa-check"></i> Mark All Read
                            </button>
                        </form>

                        <form action="${pageContext.request.contextPath}/reviewer_notification_delete_all"
                              method="post"
                              onsubmit="return confirm('Delete ALL notifications?');">
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

                    <button type="button" class="chip active" data-read="all">All</button>
                    <button type="button" class="chip" data-read="unread">Unread (${unreadCount})</button>
                    <button type="button" class="chip" data-read="read">Read</button>

                    <div class="filter-divider"></div>

                    <button type="button" class="chip active" data-type="all">All Types</button>
                    <button type="button" class="chip" data-type="PRODUCT_SUBMITTED">Submitted</button>
                    <button type="button" class="chip" data-type="REVIEW_APPROVED">Approved</button>
                    <button type="button" class="chip" data-type="REVIEW_REJECTED">Rejected</button>
                    <button type="button" class="chip" data-type="PENDING_APPROVAL">Pending Approval</button>
                </div>

                <form class="noti-search-wrapper" onsubmit="return false;">
                    <div class="noti-search-box">
                        <i class="fa-solid fa-magnifying-glass"></i>
                        <input type="text"
                               id="searchInput"
                               placeholder="Search notifications..." />
                    </div>
                </form>

                <div class="pending-container">

                    <c:if test="${empty notifications}">
                        <div class="noti-card">
                            <div class="noti-body">
                                <div class="noti-title">No notifications</div>
                                <div class="noti-desc">You have no notifications yet.</div>
                            </div>
                        </div>
                    </c:if>

                    <c:forEach var="n" items="${notifications}">
                        <div class="noti-card notification-card"
                             data-read="${n.read}"
                             data-type="${n.type}"
                             data-title="${fn:toLowerCase(n.title)}"
                             data-content="${fn:toLowerCase(n.content)}">

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

                                        <c:if test="${not empty n.type}">
                                            <span class="badge b-low">
                                                <c:choose>
                                                    <c:when test="${n.type eq 'PRODUCT_SUBMITTED'}">Submitted</c:when>
                                                    <c:when test="${n.type eq 'REVIEW_APPROVED'}">Approved</c:when>
                                                    <c:when test="${n.type eq 'REVIEW_REJECTED'}">Rejected</c:when>
                                                    <c:when test="${n.type eq 'PENDING_APPROVAL'}">Pending Approval</c:when>
                                                    <c:otherwise>${n.type}</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </c:if>

                                        <c:if test="${!n.read}">
                                            <span class="badge b-unread">Unread</span>
                                        </c:if>
                                    </div>

                                    <div class="noti-time">${n.createdAt}</div>
                                </div>

                                <div class="noti-desc">${n.content}</div>

                                <div class="noti-actions">
                                    <button type="button"
                                            class="link-action"
                                            onclick="openDetails(
                                                            '${n.notificationId}',
                                                            '${fn:escapeXml(n.title)}',
                                                            '${fn:escapeXml(n.content)}',
                                                            '${n.priority}',
                                                            '${n.type}',
                                                            '${n.createdAt}',
                                                            '${n.relatedUrl}',
                                                            '${n.read}'
                                                            )">
                                        View Details <i class="fa-solid fa-chevron-right"></i>
                                    </button>

                                    <form action="${pageContext.request.contextPath}/reviewer_notification_toggle"
                                          method="post"
                                          style="display:inline;">
                                        <input type="hidden" name="id" value="${n.notificationId}">
                                        <button type="submit" class="link-action">
                                            <i class="fa-solid fa-check"></i>
                                            <c:choose>
                                                <c:when test="${n.read}">Mark Unread</c:when>
                                                <c:otherwise>Mark Read</c:otherwise>
                                            </c:choose>
                                        </button>
                                    </form>

                                    <form action="${pageContext.request.contextPath}/reviewer_notification_delete"
                                          method="post"
                                          style="display:inline;"
                                          onsubmit="return confirm('Delete this notification?');">
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
                            <span id="mType" class="badge b-low"></span>
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
            let readFilter = "all";
            let typeFilter = "all";
            let currentRelatedUrl = "";

            document.querySelectorAll(".chip").forEach(btn => {
                btn.addEventListener("click", () => {
                    if (btn.dataset.read) {
                        document.querySelectorAll(".chip[data-read]").forEach(b => b.classList.remove("active"));
                        btn.classList.add("active");
                        readFilter = btn.dataset.read;
                    }

                    if (btn.dataset.type) {
                        document.querySelectorAll(".chip[data-type]").forEach(b => b.classList.remove("active"));
                        btn.classList.add("active");
                        typeFilter = btn.dataset.type;
                    }

                    applyFilters();
                });
            });

            document.getElementById("searchInput").addEventListener("keyup", applyFilters);

            function applyFilters() {
                const q = (document.getElementById("searchInput").value || "").toLowerCase().trim();
                const cards = document.querySelectorAll(".notification-card");

                cards.forEach(card => {
                    const isRead = card.getAttribute("data-read") === "true";
                    const type = card.getAttribute("data-type") || "";
                    const title = card.getAttribute("data-title") || "";
                    const content = card.getAttribute("data-content") || "";

                    let okRead = true;
                    if (readFilter === "unread") okRead = !isRead;
                    if (readFilter === "read") okRead = isRead;

                    let okType = true;
                    if (typeFilter === "all") {
                        okType = true;
                    } else {
                        okType = (type === typeFilter);
                    }

                    const okSearch = (q === "") ? true : (title.includes(q) || content.includes(q));

                    card.style.display = (okRead && okType && okSearch) ? "flex" : "none";
                });
            }

            function getTypeLabel(type) {
                if (type === "PRODUCT_SUBMITTED") return "Submitted";
                if (type === "REVIEW_APPROVED") return "Approved";
                if (type === "REVIEW_REJECTED") return "Rejected";
                if (type === "PENDING_APPROVAL") return "Pending Approval";
                return type || "";
            }

            function openDetails(id, title, content, priority, type, time, relatedUrl, isRead) {
                document.getElementById("mTitle").innerText = title || "";
                document.getElementById("mContent").innerText = content || "";
                document.getElementById("mTime").innerText = time || "";
                document.getElementById("mStatus").innerText = (isRead === "true") ? "Read" : "Unread";

                currentRelatedUrl = relatedUrl || "";

                const pri = (priority || "LOW").toLowerCase();
                const priEl = document.getElementById("mPriority");
                priEl.className = "badge " + (
                        (pri === "high" || pri === "critical") ? "b-high" :
                        (pri === "medium") ? "b-med" : "b-low"
                );
                priEl.innerText =
                        (pri === "high" || pri === "critical") ? "High Priority" :
                        (pri === "medium") ? "Medium" : "Low";

                const typeEl = document.getElementById("mType");
                const typeLabel = getTypeLabel(type);
                if (typeLabel && typeLabel.trim() !== "") {
                    typeEl.className = "badge b-low";
                    typeEl.innerText = typeLabel;
                    typeEl.style.display = "inline-flex";
                } else {
                    typeEl.innerText = "";
                    typeEl.style.display = "none";
                }

                const goBtn = document.getElementById("goBtn");
                if (!currentRelatedUrl || currentRelatedUrl.trim() === "") {
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
                if (currentRelatedUrl && currentRelatedUrl.trim() !== "") {
                    window.location.href = "${pageContext.request.contextPath}" + currentRelatedUrl;
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