<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Dang nhap</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />
</head>
<body>
  <div class="container">
    <div class="card">
      <h1>Dang nhap</h1>
      <p class="subtitle">Nhap thong tin de truy cap he thong</p>

      <c:if test="${param.reset == 'success'}">
        <div class="alert success">Dat lai mat khau thanh cong. Vui long dang nhap.</div>
      </c:if>

      <c:if test="${not empty error}">
        <div class="alert danger">${error}</div>
      </c:if>

      <form method="post" action="${pageContext.request.contextPath}/login" autocomplete="off">
        <input type="hidden" name="redirect" value="${redirect != null ? redirect : param.redirect}" />

        <div class="field">
          <label for="email">Email</label>
          <input id="email" name="email" type="email" required maxlength="100"
                 value="<c:out value='${email}'/>" />
        </div>

        <div class="field">
          <label for="password">Mat khau</label>
          <input id="password" name="password" type="password" required maxlength="72" />
        </div>

        <div class="actions">
          <button type="submit">Dang nhap</button>
        </div>

        <div style="display: flex; justify-content: space-between; margin-top: 15px;">
            <a href="${pageContext.request.contextPath}/forgot-password" class="small">Quen mat khau?</a>
            <a href="${pageContext.request.contextPath}/register" class="small">Chua co tai khoan? Dang ky</a>
        </div>
      </form>
    </div>
  </div>
</body>
</html>
