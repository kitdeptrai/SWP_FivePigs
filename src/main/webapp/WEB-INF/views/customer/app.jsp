<%--
    Document   : app
    Created on : Feb 2, 2026, 9:36:02 PM
    Author     : kiet
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FIVEPIGS - Corrected Layout</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <!-- font Noto Sans -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap"
        rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css">

        </head>

        <body>

                <jsp:include page="/WEB-INF/views/customer/sidebar.jsp">
                    <jsp:param name="activePage" value="apps" />
                </jsp:include>

                <div class="main-content">
                    <jsp:include page="/WEB-INF/views/customer/header.jsp"></jsp:include>


        <div id="apps" class="content-section active-section">
            <div class="fixed-layout-grid">
                <div class="left-column" style="height: var(--sidebar-total-height);">
                    <div class="featured-banner"
                        style="height:100%; background-image: url('https://images.unsplash.com/photo-1551288049-bebda4e38f71?q=80&w=2070&auto=format&fit=crop'); padding:0;">
                        <div
                            style="background: linear-gradient(to top, #4a148c, transparent); height:100%; width:100%; display:flex; flex-direction:column; justify-content:flex-end; padding:30px;">
                            <h1 style="font-size: 40px; margin-bottom: 10px;">Productivity Tools</h1>
                            <p style="opacity: 0.9; max-width: 500px; margin-bottom: 20px;">Upgrade your workflow.</p>
                            <button
                                style="padding: 12px 30px; border: none; background: white; color: #4a148c; font-weight: 700; border-radius: 8px; cursor: pointer; width:fit-content;">Explore</button>
                        </div>
                    </div>
                </div>
                <div class="right-column-box"
                    style="background: linear-gradient(135deg, #667eea, #764ba2); color:white;">
                    <h3 style="margin-bottom:10px;">Featured</h3>
                    <div style="font-size: 60px; text-align:center; margin-top:50px;"><i class="fa-brands fa-figma"></i>
                    </div>
                    <p style="text-align:center; margin-top:10px;">Figma</p>
                </div>
            </div>

            <div class="section-header">
                    Social Media<i class="fa-solid fa-chevron-right"></i>
                  </div>

                  <div class="app-list-grid">
                    <c:forEach var="sw" items="${softwareToShow}">
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
                              <c:otherwise>$${sw.price}</c:otherwise>
                            </c:choose>
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
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>

</html>