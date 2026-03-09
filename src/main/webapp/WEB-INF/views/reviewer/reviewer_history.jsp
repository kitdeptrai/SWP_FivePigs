<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
    </head>
    <body>

        <div class="layout">

            <%@ include file="layout/sidebar.jsp" %>

            <main class="main">

                <h1 class="page-title">Review History</h1>
                <p class="subtitle">
                    Completed software reviews
                </p>

                <form class="search-wrapper" method="get"
                      action="${pageContext.request.contextPath}/reviewer_history">
                    <div class="search-box">
                        <i class="fa-solid fa-magnifying-glass"></i>
                        <input type="text"
                               id="searchInput"
                               name="keyword"
                               value="${keyword}"
                               placeholder="Search review history...">
                    </div>
                </form>

                <div class="history-container">

                    <c:forEach var="h" items="${historyList}">
                        <div class="history-card">

                            <div class="card-left">
                                <img src="${pageContext.request.contextPath}/${h.imageUrl}"
                                     class="software-thumb">

                                <div class="software-info">
                                    <h3>${h.softwareName}</h3>
                                    <p class="meta">Version ${h.version}</p>
                                    <p class="meta">
                                        Reviewed at: ${h.createdAt}
                                    </p>
                                    <p class="meta score">
                                        Total Score: ${h.totalScore}
                                    </p>
                                </div>
                            </div>

                            <div class="card-right">

                                <div class="card-right">
                                    <a class="details-btn"
                                       href="${pageContext.request.contextPath}/reviewer_history_detail?reviewScoreId=${h.reviewScoreId}">
                                        View Details
                                    </a>
                                </div>



                            </div>

                        </div>
                    </c:forEach>

                </div>
                <c:if test="${totalPages > 1}">
                    <div class="pagination">

                        <c:if test="${currentPage > 1}">
                            <a href="${pageContext.request.contextPath}/reviewer_history?page=${currentPage - 1}&keyword=${keyword}"
                               class="page-btn">
                                ← Previous
                            </a>
                        </c:if>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <a href="${pageContext.request.contextPath}/reviewer_history?page=${i}&keyword=${keyword}"
                               class="page-number ${i == currentPage ? 'active' : ''}">
                                ${i}
                            </a>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <a href="${pageContext.request.contextPath}/reviewer_history?page=${currentPage + 1}&keyword=${keyword}"
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