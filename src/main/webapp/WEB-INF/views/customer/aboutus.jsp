<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>FIVEPIGS - About</title>

  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">

  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css" />
</head>
<body>

  <jsp:include page="/WEB-INF/views/customer/sidebar.jsp">
    <jsp:param name="activePage" value="about" />
  </jsp:include>

  <div class="main-content">
    <jsp:include page="/WEB-INF/views/customer/header.jsp"></jsp:include>

    <div class="content-scroll-area" style="padding: 24px;">
      <div style="max-width: 1100px; margin: 0 auto;">

        <!-- HERO (take first hero image if exists) -->
        <c:set var="heroUrl" value="" />
        <c:forEach var="m" items="${aboutMedia}">
          <c:if test="${m.mediaType == 'hero' && empty heroUrl}">
            <c:set var="heroUrl" value="${m.imageUrl}" />
          </c:if>
        </c:forEach>

        <div style="background:#fff; border-radius:16px; overflow:hidden; box-shadow:0 2px 10px rgba(0,0,0,0.04);">
          <c:if test="${not empty heroUrl}">
            <div style="height:220px; background-size:cover; background-position:center;
                        background-image:url('${pageContext.request.contextPath}/assets/${heroUrl}');">
            </div>
          </c:if>

          <div style="padding:22px;">
            <h1 style="margin:0 0 10px 0; font-size:28px;">About FIVEPIGS</h1>
            <p style="margin:0; color:#666; line-height:1.7;">
              FIVEPIGS is a clean, fast marketplace for games and apps.
              We focus on smooth browsing, reliable downloads, and a simple library experience.
            </p>
          </div>
        </div>

        <!-- GALLERY (store/software images) -->
        <div style="margin-top:16px; background:#fff; border-radius:16px; padding:22px; box-shadow:0 2px 10px rgba(0,0,0,0.04);">
          <h2 style="margin:0 0 14px 0; font-size:20px;">Product Preview</h2>

          <div style="display:grid; grid-template-columns: repeat(3, 1fr); gap:12px;">
            <c:forEach var="m" items="${aboutMedia}">
              <c:if test="${m.mediaType == 'gallery'}">
                <div style="border-radius:14px; overflow:hidden; border:1px solid #eee;">
                  <img src="${pageContext.request.contextPath}/assets/${m.imageUrl}"
                       alt="${m.title}" style="width:100%; height:190px; object-fit:cover; display:block;">
                </div>
              </c:if>
            </c:forEach>
          </div>
        </div>

        <!-- CONTACT -->
        <div style="margin-top:16px; background:#fff; border-radius:16px; padding:22px; box-shadow:0 2px 10px rgba(0,0,0,0.04);">
          <h2 style="margin:0 0 14px 0; font-size:20px;">Contact</h2>

          <div style="display:grid; grid-template-columns: 1fr 1fr; gap:12px; color:#666; line-height:1.8;">
            <div>
              <div><b>Email:</b> ${about['about_email']}</div>
              <div><b>Phone:</b> ${about['about_phone']}</div>
              <div><b>Address:</b> ${about['about_address']}</div>
            </div>
            <div>
              <div><b>Facebook:</b> ${about['about_facebook']}</div>
              <div><b>Instagram:</b> ${about['about_instagram']}</div>
              <div style="margin-top:10px; font-size:13px; color:#888;">
                We usually respond within 24 hours.
              </div>
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>