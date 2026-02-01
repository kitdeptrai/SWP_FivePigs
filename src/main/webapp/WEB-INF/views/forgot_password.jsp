<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Quên mật khẩu</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />
</head>
<body>
  <div class="container">
    <div class="card">
      <h1>Tìm lại mật khẩu</h1>
      <p class="subtitle">Vui lòng nhập email đã đăng ký. Chúng tôi sẽ gửi một mã OTP để bạn có thể đặt lại mật khẩu.</p>

      <c:if test="${not empty error}">
        <div class="alert danger">${error}</div>
      </c:if>
      <c:if test="${not empty success}">
        <div class="alert success">${success}</div>
      </c:if>

      <form method="post" action="${pageContext.request.contextPath}/forgot-password" autocomplete="off">
        <div class="field">
          <label for="email">Email</label>
          <input id="email" name="email" type="email" required maxlength="100"
                 value="<c:out value='${email}'/>" />
        </div>

        <div class="actions">
          <button type="submit">Gửi mã OTP</button>
        </div>

        <p class="small"><a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a></p>
      </form>
    </div>
  </div>
</body>
</html>
