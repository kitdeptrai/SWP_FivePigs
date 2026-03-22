<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="activeMenu" value="dashboard" />
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Reviewer Dashboard</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/reviewer/reviewer.css">
    </head>
    <body>

        <div class="layout">

            <%@ include file="layout/sidebar.jsp" %>

            <!-- MAIN CONTENT -->
            <main class="main">
                <h1>Reviewer Dashboard</h1>
                <p class="subtitle">Monitor your review performance and quality metrics</p>

                <!-- TOP CARDS -->
                <div class="cards">

                    <!-- Card 1 -->
                    <div class="card">
                        <div class="card-icon warning">
                            <svg width="22" height="22" fill="none" stroke="currentColor" stroke-width="2"
                                 stroke-linecap="round" stroke-linejoin="round"
                                 viewBox="0 0 24 24">
                            <circle cx="12" cy="12" r="10"/>
                            <path d="M12 6v6l4 2"/>
                            </svg>
                        </div>

                        <p class="card-title">Pending Reviews</p>
                        <h2>${pendingReviewApp}</h2>
                        <span class="warning-text">Total</span>
                    </div>

                    <!-- Card 2 -->
                    <div class="card">
                        <div class="card-icon success">
                            <svg width="22" height="22" fill="none" stroke="currentColor" stroke-width="2"
                                 stroke-linecap="round" stroke-linejoin="round"
                                 viewBox="0 0 24 24">
                            <path d="M20 6L9 17l-5-5"/>
                            </svg>
                        </div>

                        <p class="card-title">Completed Reviews</p>
                        <h2>${completeReviewApp}</h2>
                        <span class="success-text">Total</span>
                    </div>



                    <!-- Card 3 -->
                    <div class="card">
                        <div class="card-icon purple">
                            <svg width="22" height="22" fill="none" stroke="currentColor" stroke-width="2"
                                 stroke-linecap="round" stroke-linejoin="round"
                                 viewBox="0 0 24 24">
                            <polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/>
                            <polyline points="17 6 23 6 23 12"/>
                            </svg>
                        </div>

                        <p class="card-title">Reviewed Today</p>
                        <h2>${reviewedToday}</h2>
                        <span class="purple-text">Total</span>
                    </div>

                </div>

                <div class="grid-2">

                    <a href="${pageContext.request.contextPath}/reviewer_dashboard_detail" class="panel dashboard-link-panel">
                        <div class="panel-header">
                            <h3>Total software reviews</h3>
                            <span class="target">${reviewedCount} / ${inReviewCount + reviewedCount}</span>
                        </div>

                        <p class="label">Reviews Completed</p>
                        <div class="progress-bar">
                            <div class="progress-fill"
                                 style="width:${(inReviewCount + reviewedCount) > 0 ? (reviewedCount * 100 / (inReviewCount + reviewedCount)) : 0}%">
                            </div>
                        </div>

                        <div class="stats">
                            <div>
                                <h2>${inReviewCount}</h2>
                                <p>In Review</p>
                            </div>
                            <div>
                                <h2 class="green">${reviewedCount}</h2>
                                <p>Reviewed</p>
                            </div>
                        </div>
                    </a>


                </div>

                <div class="recent-review">
                    <h2>Recent Review Activity</h2>

                    <c:forEach var="s" items="${pendingList}">
                        <a href="${pageContext.request.contextPath}/reviewer_pending" class="review-item review-link">
                            <div class="left">
                                <div class="info">
                                    <div class="name">${s.name}</div>
                                    <div class="meta">
                                        ${s.categoryName} • ${s.version} • ${s.shortDescription}
                                    </div>
                                </div>
                            </div>

                            <div class="right pending">
                                <svg width="22" height="22" viewBox="0 0 24 24" fill="none">
                                <circle cx="12" cy="12" r="9" stroke="#facc15" stroke-width="2"/>
                                <path d="M12 7v5l3 2" stroke="#facc15" stroke-width="2" stroke-linecap="round"/>
                                </svg>
                            </div>
                        </a>
                    </c:forEach>
                </div>

            </main>
        </div>

    </body>
</html>