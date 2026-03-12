<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
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
                <div class="featured-banner"
                     style="background-image: linear-gradient(to right, rgba(0,0,0,0.7), transparent), url('https://images.unsplash.com/photo-1513364776144-60967b0f800f?q=80&w=2071&auto=format&fit=crop');">
                    <h1 style="font-size: 32px; margin-bottom: 10px; line-height: 1.2;">Discover trending apps<br>and games on FIVEPIGS</h1>
                    <p style="opacity: 0.9; margin-bottom: 20px;">Action and Shooter.</p>
                    <a href="${pageContext.request.contextPath}/search?q="
                       style="padding: 10px 25px; border: none; background: white; color: #6b70ff; font-weight: 700; border-radius: 8px; cursor: pointer; width: fit-content; text-decoration:none; display:inline-flex;">Explore</a>
                </div>
                <div class="sub-banners-row">
                    <div class="sub-card">
                        <div class="sub-card-content">
                            <h3>Top Downloads</h3>
                            <p style="font-size: 12px;">SALE IN WINTER</p>
                        </div>
                    </div>

                    <div class="sub-card" style="background:linear-gradient(135deg,#4f46e5,#7c3aed)">
                        <div class="sub-card-content" style="background: linear-gradient(transparent, rgba(0,0,0,0.45));">
                            <h3>Best Selling</h3>
                            <p style="font-size: 12px;">Paid orders ranking</p>
                        </div>
                    </div>
                </div>
            </div>

            <div class="right-column-box">
                <div class="toggle-container">
                    <div id="home-trend" class="toggle-btn active" onclick="toggleList('home', 'trend')">TRENDING</div>
                    <div id="home-best" class="toggle-btn" onclick="toggleList('home', 'best')">BEST SELLING</div>
                </div>

                <div class="scrollable-list" id="home-list-trend">
                    <c:if test="${empty trendList}">
                        <p style="color:#6b7280; font-size:14px;">No trending data.</p>
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