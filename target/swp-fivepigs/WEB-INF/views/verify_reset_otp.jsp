<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Xác thực OTP</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />
</head>
<body>
  <div class="container">
    <div class="card">
      <h1>Xác thực OTP</h1>
      <p class="subtitle">Một mã OTP đã được gửi đến email <strong><c:out value="${sessionScope.reset_email}"/></strong>. Vui lòng nhập mã để tiếp tục.</p>

      <c:if test="${not empty error}">
        <div class="alert danger">${error}</div>
      </c:if>
      <c:if test="${not empty success}">
        <div class="alert success">${success}</div>
      </c:if>

      <form method="post" action="${pageContext.request.contextPath}/verify-reset-otp" autocomplete="off">
        <div class="actions" style="justify-content: space-between;">
          <a class="small" href="${pageContext.request.contextPath}/forgot-password">Đổi email</a>
          <a class="small" href="${pageContext.request.contextPath}/verify-reset-otp?resend=true">Gửi lại OTP</a>
        </div>

        <div class="field">
          <label for="otp">Mã OTP</label>
          <input id="otp" name="otp" type="text" required inputmode="numeric" maxlength="6" />
        </div>

        <div class="actions">
          <button type="submit">Xác thực</button>
        </div>
      </form>
    </div>
  </div>
</body>
</html>
