<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FIVEPIGS - My Library</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css">
</head>
<body>

<jsp:include page="/WEB-INF/views/customer/sidebar.jsp">
    <jsp:param name="activePage" value="library"/>
</jsp:include>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/customer/header.jsp"></jsp:include>

    <div id="library" class="content-section active-section">
        <h2 style="margin-bottom: 20px; font-size: 44px; font-weight: 800;">My Library</h2>

        <c:if test="${param.msg == 'not_owned'}">
            <div style="background:#ffecec;color:#9a2c2c;border-radius:10px;padding:10px 12px;margin-bottom:14px;">You do not own this software.</div>
        </c:if>

        <c:choose>
            <c:when test="${empty libraryList}">
                <div style="background:#fff;border-radius:16px;padding:30px;color:#6f7691;">
                    Library is empty. Go to store and checkout to own products.
                </div>
            </c:when>

            <c:otherwise>
                <div class="library-grid">
                    <c:forEach var="sw" items="${libraryList}" varStatus="loop">
                        <div class="lib-card">
                            <div class="lib-thumb ${loop.index % 3 == 0 ? 'bg-mc' : (loop.index % 3 == 1 ? 'bg-cod' : 'bg-music')}">
                                <c:choose>
                                    <c:when test="${not empty sw.iconUrl}">
                                        <img src="${pageContext.request.contextPath}/assets/${sw.iconUrl}" alt="${sw.name}"
                                             style="width:68px;height:68px;border-radius:14px;object-fit:cover;box-shadow:0 8px 18px rgba(0,0,0,.15);">
                                    </c:when>
                                    <c:otherwise>
                                        <i class="${loop.index % 3 == 0 ? 'fa-solid fa-cube' : (loop.index % 3 == 1 ? 'fa-solid fa-gun' : 'fa-brands fa-apple')}"
                                           style="font-size:20px;"></i>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="lib-info">
                                <h4 style="font-size:25px; line-height:1.0; margin-bottom:6px; font-weight:800;">${sw.name}</h4>
                                <c:choose>
                                    <c:when test="${downloadedMap[sw.softwareId]}">
                                        <p style="font-size:15px; color:#2f855a; font-weight:700;">Owned � Downloaded</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p style="font-size:15px; color:#5a67d8; font-weight:700;">Owned</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div style="display:flex; gap:8px; padding:0 12px 12px;">
                                <form method="post" action="${pageContext.request.contextPath}/library/download" style="margin:0;">
                                    <input type="hidden" name="softwareId" value="${sw.softwareId}">
                                    <button type="submit" class="install-btn" style="padding:8px 14px; font-size:14px; box-shadow:none;">
                                        <c:choose>
                                            <c:when test="${downloadedMap[sw.softwareId]}">Download Again</c:when>
                                            <c:otherwise>Download</c:otherwise>
                                        </c:choose>
                                    </button>
                                </form>
                                <a href="${pageContext.request.contextPath}/product?pid=${sw.softwareId}" class="install-btn"
                                   style="padding:8px 14px; font-size:14px; box-shadow:none; background:#eceff7; color:#2d3748; text-decoration:none;">
                                    Detail
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
