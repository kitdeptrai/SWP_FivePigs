<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Đăng nhập</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />
</head>
<body>
  <div class="container">
    <div class="card">
      <h1>Đăng nhập</h1>
      <p class="subtitle">Nhập thông tin để truy cập hệ thống</p>

      <c:if test="${param.reset == 'success'}">
        <div class="alert success">Đặt lại mật khẩu thành công. Vui lòng đăng nhập.</div>
      </c:if>

      <c:if test="${not empty error}">
        <div class="alert danger">${error}</div>
      </c:if>

      <form method="post" action="${pageContext.request.contextPath}/login" autocomplete="off">
        <div class="field">
          <label for="email">Email</label>
          <input id="email" name="email" type="email" required maxlength="100"
                 value="<c:out value='${email}'/>" />
        </div>

        <div class="field">
          <label for="password">Mật khẩu</label>
          <input id="password" name="password" type="password" required maxlength="72" />
        </div>

        <div class="actions">
          <button type="submit">Đăng nhập</button>
        </div>

        <div style="display: flex; justify-content: space-between; margin-top: 15px;">
            <a href="${pageContext.request.contextPath}/forgot-password" class="small">Quên mật khẩu?</a>
            <a href="${pageContext.request.contextPath}/register" class="small">Chưa có tài khoản? Đăng ký</a>
        </div>
      </form>
    </div>
  </div>
</body>
</html>
