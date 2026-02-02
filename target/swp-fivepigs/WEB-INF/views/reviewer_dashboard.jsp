<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Reviewer Dashboard</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />
</head>
<body>
  <div class="container">
    <div class="card">
      <h1>Reviewer Dashboard</h1>
      <p class="subtitle">Chào mừng, <c:out value="${user.fullName}"/>!</p>

      <div class="alert">
        <p><strong>User ID:</strong> <c:out value="${user.userId}"/></p>
        <p><strong>Ngày tham gia:</strong> 
          <fmt:parseDate value="${user.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" type="both" />
          <fmt:formatDate value="${parsedDate}" pattern="HH:mm:ss dd/MM/yyyy" />
        </p>
      </div>

      <p class="small">Role: <c:out value="${roleName}"/></p>
      <p class="small"><a href="${pageContext.request.contextPath}/logout">Đăng xuất</a></p>
    </div>
  </div>
</body>
</html>
