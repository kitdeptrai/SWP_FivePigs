<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FIVEPIGS - News</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap"
          rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/customer/sidebar.jsp">
    <jsp:param name="activePage" value="news" />
</jsp:include>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/customer/header.jsp"></jsp:include>

    <div id="News" class="content-section active-section">
        <div class="news-toolbar">
            <a href="${pageContext.request.contextPath}/news"
               class="news-filter ${selectedType == 'ALL' ? 'active' : ''}">All</a>
            <a href="${pageContext.request.contextPath}/news?type=NEW_RELEASE"
               class="news-filter ${selectedType == 'NEW_RELEASE' ? 'active' : ''}">New Release</a>
            <a href="${pageContext.request.contextPath}/news?type=UPDATE"
               class="news-filter ${selectedType == 'UPDATE' ? 'active' : ''}">Updates</a>
            <a href="${pageContext.request.contextPath}/news?type=SALE"
               class="news-filter ${selectedType == 'SALE' ? 'active' : ''}">Sales</a>
        </div>

        <div class="fixed-layout-grid-news">
            <div class="left-column">
                <c:choose>
                    <c:when test="${not empty featuredNews}">
                        <c:choose>
                            <c:when test="${empty featuredNews.coverImage}">
                                <c:set var="featuredCoverUrl" value="${pageContext.request.contextPath}/assets/images/default_icon.png" />
                            </c:when>
                            <c:when test="${fn:startsWith(featuredNews.coverImage, 'http')}">
                                <c:set var="featuredCoverUrl" value="${featuredNews.coverImage}" />
                            </c:when>
                            <c:otherwise>
                                <c:set var="featuredCoverUrl" value="${pageContext.request.contextPath}/assets/${featuredNews.coverImage}" />
                            </c:otherwise>
                        </c:choose>

                        <div class="featured-banner"
                             style="background-image: linear-gradient(to right, rgba(0,0,0,0.72), transparent), url('${featuredCoverUrl}');">
                            <span class="news-tag">${empty featuredNews.newsType ? 'NEWS' : featuredNews.newsType}</span>
                            <h1 style="font-size: 32px; margin-bottom: 10px; line-height: 1.2;">${featuredNews.title}</h1>
                            <p style="opacity: 0.9; margin-bottom: 20px; max-width: 700px;">${featuredNews.summary}</p>
                            <div class="news-meta-line">
                                <c:if test="${not empty featuredNews.softwareName}">
                                    <span><i class="fa-solid fa-cube"></i> ${featuredNews.softwareName}</span>
                                </c:if>
                                <c:if test="${not empty featuredNews.publishedDateLabel}">
                                    <span><i class="fa-regular fa-clock"></i> ${featuredNews.publishedDateLabel}</span>
                                </c:if>
                            </div>
                            <c:choose>
                                <c:when test="${not empty featuredNews.softwareId}">
                                    <a class="news-visit-btn" href="${pageContext.request.contextPath}/product?pid=${featuredNews.softwareId}">Visit now</a>
                                </c:when>
                                <c:otherwise>
                                    <a class="news-visit-btn" href="${pageContext.request.contextPath}/news">Explore</a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="featured-banner news-featured-empty">
                            <h1 style="font-size: 32px; margin-bottom: 10px; line-height: 1.2;">No news yet</h1>
                            <p style="opacity: 0.9; margin-bottom: 20px; max-width: 700px;">When you add records to the News table, the newest story will appear here automatically.</p>
                        </div>
                    </c:otherwise>
                </c:choose>

                <c:if test="${empty newsList}">
                    <div class="info-product">
                        <div class="info-header">
                            <p>No matching stories</p>
                            <div>EMPTY</div>
                        </div>
                        <div class="info-body"><p>Your News page is wired up. Once you add records to the `news` table, items will show here automatically.</p></div>
                    </div>
                </c:if>

                <c:forEach var="item" items="${newsList}">
                    <div class="info-product">
                        <div class="info-header">
                            <p>${item.title}</p>
                            <div>${empty item.newsType ? 'news' : item.newsType}</div>
                        </div>

                        <div class="info-meta-row">
                            <c:if test="${not empty item.softwareName}">
                                <span><i class="fa-solid fa-cube"></i> ${item.softwareName}</span>
                            </c:if>
                            <c:if test="${not empty item.publishedDateLabel}">
                                <span><i class="fa-regular fa-calendar"></i> ${item.publishedDateLabel}</span>
                            </c:if>
                        </div>

                        <div class="info-body"><p>${empty item.summary ? item.content : item.summary}</p></div>

                        <div class="info-footer">
                            <c:choose>
                                <c:when test="${not empty item.softwareId}">
                                    <a class="news-detail-link" href="${pageContext.request.contextPath}/product?pid=${item.softwareId}">View Details</a>
                                </c:when>
                                <c:otherwise>
                                    <a class="news-detail-link" href="${pageContext.request.contextPath}/news">View Details</a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
