<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Đặt lại mật khẩu</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />
</head>
<body>
  <div class="container">
    <div class="card">
      <h1>Đặt lại mật khẩu</h1>
      <p class="subtitle">Vui lòng đặt mật khẩu mới cho tài khoản <strong><c:out value="${sessionScope.reset_email}"/></strong>.</p>

      <c:if test="${not empty error}">
        <div class="alert danger">${error}</div>
      </c:if>
      <c:if test="${not empty success}">
        <div class="alert success">${success}</div>
      </c:if>

      <form method="post" action="${pageContext.request.contextPath}/reset-password" autocomplete="off">
        <div class="actions" style="justify-content: space-between;">
          <a class="small" href="${pageContext.request.contextPath}/forgot-password">Đổi email</a>
          <a class="small" href="${pageContext.request.contextPath}/verify-reset-otp">Quay lại xác thực OTP</a>
        </div>

        <div class="field">
          <label for="password">Mật khẩu mới</label>
          <input id="password" name="password" type="password" required minlength="6" maxlength="72" />
        </div>

        <div class="field">
          <label for="confirmPassword">Nhập lại mật khẩu mới</label>
          <input id="confirmPassword" name="confirmPassword" type="password" required minlength="6" maxlength="72" />
        </div>

        <div class="actions">
          <button type="submit">Đặt lại mật khẩu</button>
        </div>
      </form>
    </div>
  </div>
</body>
</html>
