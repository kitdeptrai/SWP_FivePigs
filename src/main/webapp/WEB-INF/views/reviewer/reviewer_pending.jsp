<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="activeMenu" value="pending" />

<!DOCTYPE html>
<html>
    <head>
        <title>Pending Reviews</title>

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/assets/reviewer/reviewer.css">

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/assets/reviewer/pending.css">

        <link rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    </head>
    <body>

        <div class="layout">

            <%@ include file="layout/sidebar.jsp" %>

            <main class="main">

                <h1 class="page-title">Pending Reviews</h1>
                <p class="subtitle">
                    Software awaiting technical review and quality assessment
                </p>

                <form class="search-wrapper" method="get"
                      action="${pageContext.request.contextPath}/reviewer_pending">
                    <div class="search-box">
                        <i class="fa-solid fa-magnifying-glass"></i>
                        <input type="text"
                               id="searchInput"
                               name="keyword"
                               value="${param.keyword}"
                               placeholder="Search by software name, vendor, category...">
                    </div>
                </form>

                <div class="pending-container">

                    <c:forEach var="s" items="${pendingList}">
                        <div class="software-card">

                            <div class="card-left">
                                <img src="${pageContext.request.contextPath}/assets/images/img2.png"
                                     class="software-img">

                                <div class="software-info">
                                    <h3>${s.name}</h3>

                                    <p class="meta">
                                        Version ${s.version}
                                    </p>

                                    <p class="desc">
                                        ${s.shortDescription}
                                    </p>

                                    <div class="tags">
                                        <span class="tag">
                                            <c:choose>
                                                <c:when test="${not empty s.categoryName}">
                                                    ${s.categoryName}
                                                </c:when>
                                                <c:otherwise>N/A</c:otherwise>
                                            </c:choose>
                                        </span>
                                        <span class="tag">$${s.price}</span>
                                    </div>

                                    <a class="review-btn"
                                       href="${pageContext.request.contextPath}/review_software?softwareId=${s.softwareId}">
                                        <i class="fa-solid fa-eye"></i> Start Review
                                    </a>
                                </div>
                            </div>

                            <div class="status-badge">
                                Pending Review
                            </div>

                        </div>
                    </c:forEach>

                </div>

            </main>

        </div>    
    </body>
</html>