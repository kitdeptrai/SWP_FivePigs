<%-- 
    Document   : noification
    Created on : Mar 5, 2026, 3:35:34 PM
    Author     : MinhPD
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <title>Notifications</title>

   
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/vendor/vendor.css">  
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/reviewer/pending.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/reviewer/notifications.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<div class="layout">

    <!-- ================= SIDEBAR ================= -->
    <jsp:include page="layout/side_bar.jsp"/>

    <!-- ================= MAIN ================= -->
    <main class="main">

        <!-- ===== Header (giống ảnh 2) ===== -->
        <div class="noti-head">
            <div class="noti-head-left">
                <h1 class="page-title">Notifications</h1>
                <p class="subtitle">Stay updated with your review assignments and activities</p>
            </div>

            <div class="noti-head-right">
                <!-- Mark All Read -->
                <form action="${pageContext.request.contextPath}/reviewer_notification_mark_all_read" method="post">
                    <button type="submit" class="noti-btn">
                        <i class="fa-solid fa-check"></i> Mark All Read
                    </button>
                </form>

                <!-- Delete All -->
                <form action="${pageContext.request.contextPath}/reviewer_notification_delete_all"
                      method="post"
                      onsubmit="return confirm('Delete ALL notifications?');">
                    <button type="submit" class="noti-btn danger">
                        <i class="fa-solid fa-trash"></i> Delete All
                    </button>
                </form>
            </div>
        </div>

        <!-- ===== Stats (3 ô) ===== -->
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

        <!-- ===== Filter bar ===== -->
        <div class="noti-filters">
            <div class="filter-label"><i class="fa-solid fa-filter"></i> Filters:</div>

            <button class="chip active" data-read="all">All</button>
            <button class="chip" data-read="unread">Unread (${unreadCount})</button>
            <button class="chip" data-read="read">Read</button>

            <div class="filter-divider"></div>

            <button class="chip active" data-type="all">All Types</button>
            <button class="chip" data-type="New Assignments">New Assignments</button>
            <button class="chip" data-type="In Progress">In Progress</button>
            <button class="chip" data-type="Completed">Completed</button>
            <button class="chip" data-type="Updates">Updates</button>
            <button class="chip" data-type="System">System</button>
        </div>

        <!-- ===== Search ===== -->
        <form class="search-wrapper" onsubmit="return false;">
            <div class="search-box">
                <i class="fa-solid fa-magnifying-glass"></i>
                <input type="text"
                       id="searchInput"
                       placeholder="Search notifications...">
            </div>
        </form>

        <!-- ===== List ===== -->
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
                     data-title="${fn:toLowerCase(n.title)}">

                    <div class="noti-left">
                        <i class="fa-regular fa-clock"></i>
                    </div>

                    <div class="noti-body">
                        <div class="noti-top">
                            <div class="noti-title-line">
                                <span class="noti-title">${n.title}</span>

                                <!-- priority badge -->
                                <c:choose>
                                    <c:when test="${n.priority eq 'High' || n.priority eq 'Critical'}">
                                        <span class="badge b-high">High Priority</span>
                                    </c:when>
                                    <c:when test="${n.priority eq 'Medium'}">
                                        <span class="badge b-med">Medium</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge b-low">Low</span>
                                    </c:otherwise>
                                </c:choose>

                                <!-- unread badge -->
                                <c:if test="${!n.read}">
                                    <span class="badge b-unread">Unread</span>
                                </c:if>
                            </div>

                            <div class="noti-time">${n.createdAt}</div>
                        </div>

                        <div class="noti-desc">${n.content}</div>

                        <div class="noti-actions">

                            <!-- View Details -> modal -->
                            <button type="button" class="link-action"
                                    onclick="openDetails(
                                        '${n.notificationId}',
                                        '${fn:escapeXml(n.title)}',
                                        '${fn:escapeXml(n.content)}',
                                        '${n.priority}',
                                        '${n.type}',
                                        '${n.createdAt}',
                                        '${n.relatedUrl}'
                                    )">
                                View Details <i class="fa-solid fa-chevron-right"></i>
                            </button>

                            <!-- Toggle Read/Unread -->
                            <form action="${pageContext.request.contextPath}/reviewer_notification_toggle"
                                  method="post" style="display:inline;">
                                <input type="hidden" name="id" value="${n.notificationId}">
                                <button type="submit" class="link-action">
                                    <i class="fa-solid fa-check"></i>
                                    <c:choose>
                                        <c:when test="${n.read}">Mark Unread</c:when>
                                        <c:otherwise>Mark Read</c:otherwise>
                                    </c:choose>
                                </button>
                            </form>

                            <!-- Delete -->
                            <form action="${pageContext.request.contextPath}/reviewer_notification_delete"
                                  method="post" style="display:inline;"
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

<!-- ===== Modal View Details ===== -->
<div id="detailsModal" class="modal" onclick="closeIfBackdrop(event)">
    <div class="modal-box">
        <button type="button" class="modal-close" onclick="closeModal()">✕</button>

        <div class="modal-header">
            <div class="modal-icon"><i class="fa-regular fa-clock"></i></div>
            <div>
                <div id="mTitle" class="modal-title"></div>
                <div class="modal-badges">
                    <span id="mPriority" class="badge b-high">High Priority</span>
                    <span id="mType" class="badge b-low">System</span>
                </div>
            </div>
        </div>

        <div id="mContent" class="modal-content"></div>

        <div class="modal-grid">
            <div class="mini-card">
                <div class="mini-label"><i class="fa-regular fa-calendar"></i> DATE & TIME</div>
                <div id="mTime" class="mini-value"></div>
            </div>

            <div class="mini-card">
                <div class="mini-label"><i class="fa-regular fa-circle-check"></i> STATUS</div>
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

<!-- ===== JS: search + filters + modal ===== -->
<script>
let readFilter = "all";   // all | unread | read
let typeFilter = "all";   // all | ...

// click chips
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

// search input
document.getElementById("searchInput").addEventListener("keyup", applyFilters);

function applyFilters(){
    const q = (document.getElementById("searchInput").value || "").toLowerCase().trim();
    const cards = document.querySelectorAll(".notification-card");

    cards.forEach(card => {
        const isRead = card.getAttribute("data-read") === "true";
        const type = card.getAttribute("data-type") || "";
        const title = card.getAttribute("data-title") || "";

        let okRead = true;
        if (readFilter === "unread") okRead = !isRead;
        if (readFilter === "read") okRead = isRead;

        let okType = (typeFilter === "all") ? true : (type === typeFilter);

        let okSearch = (q === "") ? true : title.includes(q);

        card.style.display = (okRead && okType && okSearch) ? "flex" : "none";
    });
}

// ===== Modal =====
let currentRelatedUrl = "";

function openDetails(id, title, content, priority, type, time, relatedUrl){
    document.getElementById("mTitle").innerText = title;
    document.getElementById("mContent").innerText = content;
    document.getElementById("mTime").innerText = time;
    document.getElementById("mStatus").innerText = "Active";

    currentRelatedUrl = relatedUrl || "";

    // priority badge
    const pri = (priority || "Low").toLowerCase();
    const priEl = document.getElementById("mPriority");
    priEl.className = "badge " + ((pri === "high" || pri === "critical") ? "b-high" : (pri === "medium" ? "b-med" : "b-low"));
    priEl.innerText = (pri === "high" || pri === "critical") ? "High Priority" : (pri === "medium" ? "Medium" : "Low");

    // type badge
    const typeEl = document.getElementById("mType");
    typeEl.className = "badge b-low";
    typeEl.innerText = type || "System";

    // enable/disable go btn
    const goBtn = document.getElementById("goBtn");
    if (!currentRelatedUrl) {
        goBtn.style.opacity = ".5";
        goBtn.style.pointerEvents = "none";
    } else {
        goBtn.style.opacity = "1";
        goBtn.style.pointerEvents = "auto";
    }

    document.getElementById("detailsModal").classList.add("open");
}

function closeModal(){
    document.getElementById("detailsModal").classList.remove("open");
}

function closeIfBackdrop(e){
    if (e.target.id === "detailsModal") closeModal();
}

function goRelated(){
    if (currentRelatedUrl) {
        window.location.href = "${pageContext.request.contextPath}" + currentRelatedUrl;
    }
}
</script>

</body>
</html>
