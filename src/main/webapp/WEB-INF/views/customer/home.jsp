<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FIVEPIGS - Home</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css">
</head>
<body>

<jsp:include page="/WEB-INF/views/customer/sidebar.jsp">
    <jsp:param name="activePage" value="home" />
</jsp:include>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/customer/header.jsp"></jsp:include>

    <div id="home" class="content-section active-section">
        <c:if test="${not empty homeWarning}">
            <div style="padding:10px 14px; border-radius:10px; background:#fff7e6; color:#9a6700; margin-bottom:14px;">
                ${homeWarning}
            </div>
        </c:if>

        <div class="fixed-layout-grid">
            <div class="left-column">
                <c:set var="randomHomeImage" value="${pageContext.request.contextPath}/assets/images/default_icon.png"/>
                <c:if test="${not empty randomHomeSoftware and not empty randomHomeSoftware.iconUrl}">
                    <c:set var="randomHomeImage" value="${pageContext.request.contextPath}/assets/${randomHomeSoftware.iconUrl}"/>
                </c:if>
                <div class="featured-banner"
                     style="background-image: linear-gradient(to right, rgba(0,0,0,0.7), transparent), url('${randomHomeImage}');">
                    <c:choose>
                        <c:when test="${not empty randomHomeSoftware}">
                            <div style="display:flex; align-items:center; gap:16px; margin-bottom:18px;">
                                <img src="${randomHomeImage}" alt="${randomHomeSoftware.name}" style="width:84px; height:84px; object-fit:cover; border-radius:20px; border:2px solid rgba(255,255,255,0.18); background:rgba(255,255,255,0.12);">
                                <div style="font-size:12px; font-weight:700; letter-spacing:1px; text-transform:uppercase; opacity:0.92;">Random spotlight</div>
                            </div>
                            <h1 style="font-size: 32px; margin-bottom: 10px; line-height: 1.2;">${randomHomeSoftware.name}</h1>
                            <p style="opacity: 0.9; margin-bottom: 20px;">
                                <c:choose>
                                    <c:when test="${not empty randomHomeSoftware.shortDescription}">
                                        ${randomHomeSoftware.shortDescription}
                                    </c:when>
                                    <c:otherwise>
                                        Explore a randomly featured software from the FIVEPIGS marketplace.
                                    </c:otherwise>
                                </c:choose>
                            </p>
                            <a href="${pageContext.request.contextPath}/product?pid=${randomHomeSoftware.softwareId}"
                               style="padding: 10px 25px; border: none; background: white; color: #6b70ff; font-weight: 700; border-radius: 8px; cursor: pointer; width: fit-content; text-decoration:none; display:inline-flex;">View product</a>
                        </c:when>
                        <c:otherwise>
                            <h1 style="font-size: 32px; margin-bottom: 10px; line-height: 1.2;">DISCOVER CREATIVE APPS<br>AND GAMES</h1>
                            <p style="opacity: 0.9; margin-bottom: 20px;">Explore standout software across the FIVEPIGS marketplace.</p>
                            <a href="${pageContext.request.contextPath}/search?q=design"
                               style="padding: 10px 25px; border: none; background: white; color: #6b70ff; font-weight: 700; border-radius: 8px; cursor: pointer; width: fit-content; text-decoration:none; display:inline-flex;">Explore</a>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="sub-banners-row">
                    <c:set var="topAppImage" value="${pageContext.request.contextPath}/assets/images/default_icon.png"/>
                    <c:if test="${not empty topApp and not empty topApp.iconUrl}">
                        <c:set var="topAppImage" value="${pageContext.request.contextPath}/assets/${topApp.iconUrl}"/>
                    </c:if>
                    <c:url var="topAppUrl" value="/search"/>
                    <c:if test="${not empty topApp}">
                        <c:url var="topAppUrl" value="/product">
                            <c:param name="pid" value="${topApp.softwareId}"/>
                        </c:url>
                    </c:if>
                    <c:set var="topGameImage" value="${pageContext.request.contextPath}/assets/images/default_icon.png"/>
                    <c:if test="${not empty topGame and not empty topGame.iconUrl}">
                        <c:set var="topGameImage" value="${pageContext.request.contextPath}/assets/${topGame.iconUrl}"/>
                    </c:if>
                    <c:url var="topGameUrl" value="/search">
                        <c:param name="dept" value="games"/>
                    </c:url>
                    <c:if test="${not empty topGame}">
                        <c:url var="topGameUrl" value="/product">
                            <c:param name="pid" value="${topGame.softwareId}"/>
                        </c:url>
                    </c:if>
                    <a href="${topAppUrl}" class="sub-card" style="text-decoration:none; color:inherit;">
                        <img src="${topAppImage}" alt="${not empty topApp ? topApp.name : 'Top app'}">
                        <div class="sub-card-content">
                            <h3>
                                <c:choose>
                                    <c:when test="${not empty topApp}">${topApp.name}</c:when>
                                    <c:otherwise>Top App</c:otherwise>
                                </c:choose>
                            </h3>
                            <p style="font-size: 12px;">
                                <c:choose>
                                    <c:when test="${not empty topApp}">Most downloaded app</c:when>
                                    <c:otherwise>No app data available</c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </a>

                    <a href="${topGameUrl}" class="sub-card" style="background:linear-gradient(135deg,#4f46e5,#7c3aed); text-decoration:none; color:inherit;">
                        <img src="${topGameImage}" alt="${not empty topGame ? topGame.name : 'Top game'}">
                        <div class="sub-card-content" style="background: linear-gradient(transparent, rgba(0,0,0,0.45));">
                            <h3>
                                <c:choose>
                                    <c:when test="${not empty topGame}">${topGame.name}</c:when>
                                    <c:otherwise>Top Game</c:otherwise>
                                </c:choose>
                            </h3>
                            <p style="font-size: 12px;">
                                <c:choose>
                                    <c:when test="${not empty topGame}">Most downloaded game</c:when>
                                    <c:otherwise>No game data available</c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </a>
                </div>
            </div>

            <div class="right-column-box">
                <div class="toggle-container">
                    <div id="home-trend" class="toggle-btn active" onclick="toggleList('home', 'trend')">TOP DOWNLOADS</div>
                    <div id="home-best" class="toggle-btn" onclick="toggleList('home', 'best')">BEST SELLING</div>
                </div>

                <div class="scrollable-list" id="home-list-trend">
                    <c:if test="${empty trendList}">
                        <p style="color:#6b7280; font-size:14px;">No download ranking data.</p>
                    </c:if>
                    <c:forEach var="sw" items="${trendList}">
                        <a href="${pageContext.request.contextPath}/product?pid=${sw.softwareId}" class="trend-item" style="text-decoration:none; color:inherit;">
                            <c:choose>
                                <c:when test="${not empty sw.iconUrl}">
                                    <img src="${pageContext.request.contextPath}/assets/${sw.iconUrl}" class="trend-icon">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/assets/images/default_icon.png" class="trend-icon">
                                </c:otherwise>
                            </c:choose>
                            <div class="trend-info">
                                <h4>${sw.name}</h4>
                                <div class="trend-sub">
                                    ${sw.avgRating} <i class="fa-solid fa-star"></i> | ${sw.downloadCount} downloads
                                </div>
                            </div>
                            <div class="trend-action">
                                <c:choose>
                                    <c:when test="${sw.isFree == 1}">Free</c:when>
                                    <c:otherwise>$${sw.price}</c:otherwise>
                                </c:choose>
                            </div>
                        </a>
                    </c:forEach>
                </div>

                <div class="scrollable-list" id="home-list-best" style="display:none;">
                    <c:if test="${empty bestSellingList}">
                        <p style="color:#6b7280; font-size:14px;">No best selling data.</p>
                    </c:if>
                    <c:forEach var="sw" items="${bestSellingList}">
                        <a href="${pageContext.request.contextPath}/product?pid=${sw.softwareId}" class="trend-item" style="text-decoration:none; color:inherit;">
                            <c:choose>
                                <c:when test="${not empty sw.iconUrl}">
                                    <img src="${pageContext.request.contextPath}/assets/${sw.iconUrl}" class="trend-icon">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/assets/images/default_icon.png" class="trend-icon">
                                </c:otherwise>
                            </c:choose>
                            <div class="trend-info">
                                <h4>${sw.name}</h4>
                                <div class="trend-sub">
                                    ${sw.avgRating} <i class="fa-solid fa-star"></i> | Revenue: $${sw.revenue}
                                </div>
                            </div>
                            <div class="trend-action">
                                <c:choose>
                                    <c:when test="${sw.isFree == 1}">Free</c:when>
                                    <c:otherwise>$${sw.price}</c:otherwise>
                                </c:choose>
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </div>
        </div>

        <div class="section-header">Recommended For You <i class="fa-solid fa-chevron-right"></i></div>
        <div class="app-list-grid">
            <c:forEach var="sw" items="${trendList}">
                <a href="${pageContext.request.contextPath}/product?pid=${sw.softwareId}" class="app-list-item">
                    <c:choose>
                        <c:when test="${not empty sw.iconUrl}">
                            <img src="${pageContext.request.contextPath}/assets/${sw.iconUrl}" class="app-icon-lg">
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/assets/images/default_icon.png" class="app-icon-lg">
                        </c:otherwise>
                    </c:choose>
                    <div class="app-details">
                        <div class="app-name">${sw.name}</div>
                        <div class="app-meta"><i class="fa-solid fa-star"></i> ${sw.avgRating}</div>
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
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>


