<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="activeMenu" value="history" />

<!DOCTYPE html>
<html>
    <head>
        <title>Review History</title>

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/assets/reviewer/reviewer.css">

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/assets/reviewer/reviewHistory.css">

        <link rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

        <style>
            .filter-form {
                display: flex;
                flex-wrap: wrap;
                gap: 12px;
                margin-bottom: 20px;
                align-items: center;
            }

            .filter-form .search-box {
                flex: 1 1 260px;
                display: flex;
                align-items: center;
                gap: 8px;
                
                padding: 10px 14px;
            }

            .filter-form .search-box input {
                border: none;
                outline: none;
                width: 100%;
            }

            .filter-form select,
            .filter-form input[type="date"],
            .filter-form button,
            .filter-form a {
                padding: 10px 14px;
                border-radius: 10px;
                border: 1px solid #ddd;
                background: #fff;
                text-decoration: none;
                color: #333;
            }

            .filter-form button {
                cursor: pointer;
                background: #2563eb;
                color: #fff;
                border: none;
            }

            .decision-badge {
                display: inline-block;
                padding: 6px 12px;
                border-radius: 20px;
                font-size: 13px;
                font-weight: 600;
                margin-top: 6px;
            }

            .decision-approved {
                background: #dcfce7;
                color: #166534;
            }

            .decision-rejected {
                background: #fee2e2;
                color: #991b1b;
            }

            .empty-state {
                padding: 24px;
                background: #fff;
                border-radius: 12px;
                text-align: center;
                color: #666;
            }
        </style>
    </head>
    <body>

        <div class="layout">

            <%@ include file="layout/sidebar.jsp" %>

            <main class="main">

                <h1 class="page-title">Review History</h1>
                <p class="subtitle">
                    Completed software reviews
                </p>

                <form class="filter-form" method="get"
                      action="${pageContext.request.contextPath}/reviewer_history">

                    <div class="search-box">
                        <i class="fa-solid fa-magnifying-glass"></i>
                        <input type="text"
                               name="keyword"
                               value="${keyword}"
                               placeholder="Search review history...">
                    </div>

                    <select name="decision">
                        <option value="all" ${decision == 'all' ? 'selected' : ''}>All Decisions</option>
                        <option value="APPROVED" ${decision == 'APPROVED' ? 'selected' : ''}>Approved</option>
                        <option value="REJECTED" ${decision == 'REJECTED' ? 'selected' : ''}>Rejected</option>
                    </select>

                    <input type="date" name="fromDate" value="${fromDate}">
                    <input type="date" name="toDate" value="${toDate}">

                    <button type="submit">
                        <i class="fa-solid fa-filter"></i> Filter
                    </button>

                    <a href="${pageContext.request.contextPath}/reviewer_history">
                        Reset
                    </a>
                </form>

                <div class="history-container">
                    <c:choose>
                        <c:when test="${empty historyList}">
                            <div class="empty-state">
                                No review history matches your filters.
                            </div>
                        </c:when>

                        <c:otherwise>
                            <c:forEach var="h" items="${historyList}">
                                <div class="history-card">

                                    <div class="card-left">
                                        <img src="${pageContext.request.contextPath}/${h.imageUrl}"
                                             class="software-thumb"
                                             onerror="this.src='${pageContext.request.contextPath}/assets/images/img2.png'">

                                        <div class="software-info">
                                            <h3>${h.softwareName}</h3>
                                            <p class="meta">Version ${empty h.version ? 'N/A' : h.version}</p>
                                            <p class="meta">Reviewed at: ${h.createdAt}</p>
                                            <p class="meta score">Total Score: ${h.totalScore}</p>

                                            <span class="decision-badge ${h.decision == 'APPROVED' ? 'decision-approved' : 'decision-rejected'}">
                                                ${h.decision}
                                            </span>
                                        </div>
                                    </div>

                                    <div class="card-right">
                                        <a class="details-btn"
                                           href="${pageContext.request.contextPath}/reviewer_history_detail?reviewScoreId=${h.reviewScoreId}">
                                            View Details
                                        </a>
                                    </div>

                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>

                <c:if test="${totalPages > 1}">
                    <div class="pagination">

                        <c:if test="${currentPage > 1}">
                            <a href="${pageContext.request.contextPath}/reviewer_history?page=${currentPage - 1}&keyword=${keyword}&decision=${decision}&fromDate=${fromDate}&toDate=${toDate}"
                               class="page-btn">
                                ← Previous
                            </a>
                        </c:if>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <a href="${pageContext.request.contextPath}/reviewer_history?page=${i}&keyword=${keyword}&decision=${decision}&fromDate=${fromDate}&toDate=${toDate}"
                               class="page-number ${i == currentPage ? 'active' : ''}">
                                ${i}
                            </a>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <a href="${pageContext.request.contextPath}/reviewer_history?page=${currentPage + 1}&keyword=${keyword}&decision=${decision}&fromDate=${fromDate}&toDate=${toDate}"
                               class="page-btn">
                                Next →
                            </a>
                        </c:if>

                    </div>
                </c:if>

            </main>

        </div>
    </body>
</html>