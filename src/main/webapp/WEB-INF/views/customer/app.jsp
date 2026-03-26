<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FIVEPIGS - Apps</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css">
</head>

<body>

<jsp:include page="/WEB-INF/views/customer/sidebar.jsp">
    <jsp:param name="activePage" value="apps"/>
</jsp:include>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/customer/header.jsp"></jsp:include>

    <div class="content-scroll-area">
        <div id="apps" class="content-section active-section">
            <c:if test="${not empty appWarning}">
                <div style="padding:10px 14px; border-radius:10px; background:#fff7e6; color:#9a6700; margin-bottom:14px;">
                        ${appWarning}
                </div>
            </c:if>

            <div class="fixed-layout-grid">
                <div class="left-column" style="height: var(--sidebar-total-height);">
                    <c:set var="randomAppImage" value="${pageContext.request.contextPath}/assets/images/default_icon.png"/>
                    <c:if test="${not empty randomApp and not empty randomApp.iconUrl}">
                        <c:set var="randomAppImage" value="${pageContext.request.contextPath}/assets/${randomApp.iconUrl}"/>
                    </c:if>
                    <div class="featured-banner"
                         style="height:100%; background-image: linear-gradient(to top, rgba(74,20,140,0.88), rgba(74,20,140,0.18)), url('${randomAppImage}'); padding:0; background-size:cover; background-position:center; background-repeat:no-repeat; overflow:hidden;">
                        <div style="height:100%; width:100%; display:flex; flex-direction:column; justify-content:flex-end; padding:30px;">
                            <c:choose>
                                <c:when test="${not empty randomApp}">
                                    <div style="display:flex; align-items:center; gap:16px; margin-bottom:18px;">
                                        <img src="${randomAppImage}" alt="${randomApp.name}" style="width:78px; height:78px; object-fit:cover; border-radius:18px; border:2px solid rgba(255,255,255,0.22); background:rgba(255,255,255,0.12);">
                                        <div style="font-size:12px; font-weight:700; letter-spacing:1px; text-transform:uppercase; opacity:0.92;">Random app spotlight</div>
                                    </div>
                                    <h1 style="font-size: 40px; margin-bottom: 10px;">${randomApp.name}</h1>
                                    <p style="opacity: 0.9; max-width: 500px; margin-bottom: 20px;">
                                        <c:choose>
                                            <c:when test="${not empty randomApp.shortDescription}">
                                                ${randomApp.shortDescription}
                                            </c:when>
                                            <c:otherwise>
                                                Explore one of the latest apps available on FIVEPIGS.
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    <a href="${pageContext.request.contextPath}/product?pid=${randomApp.softwareId}"
                                       style="padding: 10px 25px; border: none; background: white; color: #6b70ff; font-weight: 700; border-radius: 8px; cursor: pointer; width: fit-content; text-decoration:none; display:inline-flex;">View product</a>
                                </c:when>
                                <c:otherwise>
                                    <h1 style="font-size: 40px; margin-bottom: 10px;">Top Productivity Apps</h1>
                                    <p style="opacity: 0.9; max-width: 500px; margin-bottom: 20px;">Upgrade your workflow with best tools.</p>
                                    <a href="${pageContext.request.contextPath}/search?q=Tool"
                                       style="padding: 10px 25px; border: none; background: white; color: #6b70ff; font-weight: 700; border-radius: 8px; cursor: pointer; width: fit-content; text-decoration:none; display:inline-flex;">Explore</a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <div class="right-column-box" style="background: linear-gradient(135deg, #667eea, #764ba2); color:white;">
                    <h3 style="margin-bottom:10px;">Featured</h3>
                    <p style="font-size:12px; opacity:0.8; margin-bottom:20px;">Most downloaded this week</p>
                    <div style="margin-top:auto; background:rgba(255,255,255,0.15); padding:15px; border-radius:12px;">
                        <c:choose>
                            <c:when test="${not empty featuredApp}">
                                <a href="${pageContext.request.contextPath}/product?pid=${featuredApp.softwareId}" style="display:flex; gap:14px; align-items:center; text-decoration:none; color:inherit;">
                                    <c:choose>
                                        <c:when test="${not empty featuredApp.iconUrl}">
                                            <img src="${pageContext.request.contextPath}/assets/${featuredApp.iconUrl}" alt="${featuredApp.name}" style="width:68px; height:68px; object-fit:cover; border-radius:16px; background:rgba(255,255,255,0.12);">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/assets/images/default_icon.png" alt="${featuredApp.name}" style="width:68px; height:68px; object-fit:cover; border-radius:16px; background:rgba(255,255,255,0.12);">
                                        </c:otherwise>
                                    </c:choose>
                                    <div>
                                        <h4 style="margin-bottom:5px;">${featuredApp.name}</h4>
                                        <p style="font-size:12px; opacity:0.8; margin-bottom:8px;">
                                            <c:choose>
                                                <c:when test="${not empty featuredApp.shortDescription}">
                                                    ${featuredApp.shortDescription}
                                                </c:when>
                                                <c:otherwise>
                                                    ${featuredApp.downloadCount} downloads
                                                </c:otherwise>
                                            </c:choose>
                                        </p>
                                        <div style="font-size:12px; font-weight:700;">
                                            <c:choose>
                                                <c:when test="${featuredApp.isFree == 1}">Free</c:when>
                                                <c:otherwise>${featuredApp.price} VND</c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <h4 style="margin-bottom:5px;">No featured app</h4>
                                <p style="font-size:12px; opacity:0.8;">There is no app data to display right now.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <div class="store-filter-toolbar" style="display:flex;justify-content:space-between;align-items:center;gap:16px;flex-wrap:wrap;margin:0 0 18px;"><div class="genre-chip-row" style="margin:0;">
                <c:url var="allAppsUrl" value="/app" />
                <a href="${allAppsUrl}" class="genre-chip ${empty selectedGenre ? 'active' : ''}">All</a>
                <c:forEach var="genre" items="${genres}">
                    <c:url var="genreFilterUrl" value="/app">
                        <c:param name="genre" value="${genre}"/>
                    </c:url>
                    <a href="${genreFilterUrl}" class="genre-chip ${selectedGenre eq genre ? 'active' : ''}">${genre}</a>
                </c:forEach>
            </div>

            <form method="get" action="${pageContext.request.contextPath}/app" style="display:flex;gap:10px;align-items:center;flex-wrap:wrap;margin:0;background:#fff;padding:10px 12px;border-radius:14px;box-shadow:0 4px 16px rgba(15,23,42,0.05);">
                <c:if test="${not empty selectedGenre}">
                    <input type="hidden" name="genre" value="${selectedGenre}">
                </c:if>
                <label for="appSort" style="font-weight:700;color:#374151;">Sort</label>
                <select id="appSort" name="sort" style="border:1px solid #dbe1ec;border-radius:10px;padding:8px 10px;">
                    <option value="name" ${selectedSort == 'name' ? 'selected' : ''}>Name</option>
                    <option value="price" ${selectedSort == 'price' ? 'selected' : ''}>Price</option>
                </select>
                <select name="order" style="border:1px solid #dbe1ec;border-radius:10px;padding:8px 10px;">
                    <option value="asc" ${selectedOrder == 'asc' ? 'selected' : ''}>Ascending</option>
                    <option value="desc" ${selectedOrder == 'desc' ? 'selected' : ''}>Descending</option>
                </select>
                <button type="submit" class="install-btn" style="padding:8px 14px;box-shadow:none;">Apply</button>
            </form></div>

            <c:choose>
                <c:when test="${not empty selectedGenre}">
                    <div class="section-header section-header-tight">
                        ${selectedGenre} <span class="section-header-sub">Filtered apps</span>
                    </div>

                    <c:choose>
                        <c:when test="${empty genreResults}">
                            <div class="genre-empty-state">No apps found for this genre.</div>
                        </c:when>
                        <c:otherwise>
                            <div class="filtered-app-grid">
                                <c:forEach var="sw" items="${genreResults}">
                                    <a href="${pageContext.request.contextPath}/product?pid=${sw.softwareId}" class="app-list-item filtered-app-card">
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
                                            <div class="app-meta">
                                                <i class="fa-solid fa-star"></i> ${sw.avgRating} |
                                                <c:choose>
                                                    <c:when test="${sw.isFree == 1}">Free</c:when>
                                                    <c:otherwise>${sw.price} VND</c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>

                                        <div class="app-price">
                                            <c:choose>
                                                <c:when test="${sw.isFree == 1}">Free</c:when>
                                                <c:otherwise>${sw.price} VND</c:otherwise>
                                            </c:choose>
                                        </div>
                                    </a>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <c:forEach var="entry" items="${sections}">
                        <c:url var="genreSearchUrl" value="/search">
                            <c:param name="genre" value="${entry.key}"/>
                            <c:param name="dept" value="apps"/>
                        </c:url>
                        <div class="section-header">
                            <a href="${genreSearchUrl}" style="text-decoration:none;color:inherit;display:inline-flex;align-items:center;gap:10px;">
                                ${entry.key} <i class="fa-solid fa-chevron-right"></i>
                            </a>
                        </div>

                        <div class="app-list-grid">
                            <c:forEach var="sw" items="${entry.value}">
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
                                        <div class="app-meta">
                                            <i class="fa-solid fa-star"></i> ${sw.avgRating} |
                                            <c:choose>
                                                <c:when test="${sw.isFree == 1}">Free</c:when>
                                                <c:otherwise>${sw.price} VND</c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <div class="app-price">
                                        <c:choose>
                                            <c:when test="${sw.isFree == 1}">Free</c:when>
                                            <c:otherwise>${sw.price} VND</c:otherwise>
                                        </c:choose>
                                    </div>
                                </a>
                            </c:forEach>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>


