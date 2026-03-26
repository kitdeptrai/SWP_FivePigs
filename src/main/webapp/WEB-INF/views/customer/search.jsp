<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FIVEPIGS - Search</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/customer/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/customer/header.jsp"></jsp:include>

    <div class="content-section active-section" style="padding-bottom:24px;">
        <h2 class="page-title" style="margin-bottom:10px;">Search</h2>

        <c:url var="allUrl" value="/search">
            <c:param name="q" value="${searchKeyword}"/>
            <c:param name="genre" value="${searchGenre}"/>
            <c:param name="dept" value="all"/>
        </c:url>
        <c:url var="appsUrl" value="/search">
            <c:param name="q" value="${searchKeyword}"/>
            <c:param name="genre" value="${searchGenre}"/>
            <c:param name="dept" value="apps"/>
        </c:url>
        <c:url var="gamesUrl" value="/search">
            <c:param name="q" value="${searchKeyword}"/>
            <c:param name="genre" value="${searchGenre}"/>
            <c:param name="dept" value="games"/>
        </c:url>

        <div style="display:flex;gap:10px;align-items:center;margin-bottom:16px;flex-wrap:wrap;">
            <a href="${allUrl}" style="text-decoration:none;padding:7px 14px;border-radius:999px;border:1px solid ${searchDept == 'all' ? '#6b70ff' : '#d6dbe9'};background:${searchDept == 'all' ? '#eef1ff' : '#fff'};color:${searchDept == 'all' ? '#4e56e8' : '#555'};font-weight:700;">All departments</a>
            <a href="${appsUrl}" style="text-decoration:none;padding:7px 14px;border-radius:999px;border:1px solid ${searchDept == 'apps' ? '#6b70ff' : '#d6dbe9'};background:${searchDept == 'apps' ? '#eef1ff' : '#fff'};color:${searchDept == 'apps' ? '#4e56e8' : '#555'};font-weight:700;">Apps</a>
            <a href="${gamesUrl}" style="text-decoration:none;padding:7px 14px;border-radius:999px;border:1px solid ${searchDept == 'games' ? '#6b70ff' : '#d6dbe9'};background:${searchDept == 'games' ? '#eef1ff' : '#fff'};color:${searchDept == 'games' ? '#4e56e8' : '#555'};font-weight:700;">Games</a>
        </div>

        <c:if test="${not empty searchGenre}">
            <div style="margin-bottom:14px; display:flex; align-items:center; gap:8px; color:#5a6478;">
                <span style="padding:6px 12px; background:#eef1ff; color:#4e56e8; border-radius:999px; font-weight:700;">Genre: ${searchGenre}</span>
            </div>
        </c:if>

        <c:choose>
            <c:when test="${empty searchKeyword and empty searchGenre}">
                <div class="library-empty">
                    <i class="fa-solid fa-magnifying-glass"></i>
                    <h3>Type a keyword or open a genre</h3>
                    <p>Try app name, game name, or click a genre section from Apps/Games.</p>
                </div>
            </c:when>
            <c:otherwise>
                <p style="margin-bottom:14px; color:#5a6478;">
                    Found <strong>${resultCount}</strong> result(s)
                    <c:if test="${not empty searchKeyword}">for <strong>"${searchKeyword}"</strong></c:if>
                    <c:if test="${not empty searchGenre}">in genre <strong>${searchGenre}</strong></c:if>
                </p>

                <c:if test="${empty searchResults}">
                    <div class="library-empty">
                        <i class="fa-regular fa-face-frown"></i>
                        <h3>No results</h3>
                        <p>Try another keyword or switch filter.</p>
                    </div>
                </c:if>

                <c:if test="${not empty searchResults}">
                    <div class="app-list-grid" style="grid-auto-flow:row;grid-template-columns:repeat(auto-fill,minmax(320px,1fr));grid-template-rows:none;grid-auto-columns:auto;">
                        <c:forEach var="sw" items="${searchResults}">
                            <a href="${pageContext.request.contextPath}/product?pid=${sw.softwareId}" class="app-list-item" style="padding:12px; border-radius:14px; border:1px solid #eceff6; background:#fff;">
                                <c:choose>
                                    <c:when test="${not empty sw.iconUrl}">
                                        <img src="${pageContext.request.contextPath}/assets/${sw.iconUrl}" class="app-icon-lg">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/assets/images/default_icon.png" class="app-icon-lg">
                                    </c:otherwise>
                                </c:choose>

                                <div class="app-details" style="min-width:0;">
                                    <div class="app-name">${sw.name}</div>
                                    <div style="font-size:12px;color:#6b7280;margin-top:3px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">
                                        ${sw.shortDescription}
                                    </div>
                                    <div class="app-meta" style="margin-top:6px;">
                                        <i class="fa-solid fa-star"></i> ${sw.avgRating}
                                        <span style="margin:0 6px;">|</span>
                                        <i class="fa-solid fa-download"></i> ${sw.downloadCount}
                                        <span style="margin:0 6px;">|</span>
                                        <span>${sw.categoryId == 3 ? 'Games' : 'Apps'}</span>
                                    </div>
                                </div>

                                <div class="app-price">
                                    <c:choose>
                                        <c:when test="${sw.isFree == 1}">Free</c:when>
                                        <c:otherwise>$${sw.price}</c:otherwise>
                                    </c:choose>
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
