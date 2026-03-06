<%-- 
    Document   : library
    Created on : Feb 2, 2026, 9:36:08 PM
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
            <jsp:param name="activePage" value="library" />
        </jsp:include>

        <div class="main-content">
            <jsp:include page="/WEB-INF/views/customer/header.jsp"></jsp:include>

                <div id="library" class="content-section active-section">
                    <h2 style="margin-bottom: 20px;">My Library</h2>
                    <div class="library-grid">

                    <c:forEach var="sw" items="${libraryList}">

                        <div class="library-card">

                            <c:choose>
                                <c:when test="${not empty sw.iconUrl}">
                                    <img src="${pageContext.request.contextPath}/assets/${sw.iconUrl}"
                                         class="library-icon">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/assets/images/default_icon.png"
                                         class="library-icon">
                                </c:otherwise>
                            </c:choose>

                            <div class="library-info">
                                <div class="library-name">${sw.name}</div>
                                <div class="library-status">Installed</div>
                            </div>

                        </div>

                    </c:forEach>

                </div>
            </div>

        </div>

        <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
    </body>

</html>