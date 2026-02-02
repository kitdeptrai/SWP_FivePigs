<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Đăng ký</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />
</head>
<body>
  <div class="container">
    <div class="card">
      <h1>Tạo tài khoản</h1>
      <p class="subtitle">Đăng ký để bắt đầu sử dụng hệ thống</p>

      <c:if test="${not empty error}">
        <div class="alert danger">${error}</div>
      </c:if>

      <form method="post" action="${pageContext.request.contextPath}/register" autocomplete="off">
        <div class="field">
          <label for="fullName">Họ và tên</label>
          <input id="fullName" name="fullName" type="text" required minlength="2" maxlength="100"
                 value="<c:out value='${fullName}'/>" />
        </div>

        <div class="field">
          <label for="email">Email</label>
          <input id="email" name="email" type="email" required maxlength="120"
                 value="<c:out value='${email}'/>" />
        </div>

        <div class="row">
          <div class="field">
            <label for="password">Mật khẩu</label>
            <input id="password" name="password" type="password" required minlength="6" maxlength="72" />
          </div>

          <div class="field">
            <label for="confirmPassword">Nhập lại mật khẩu</label>
            <input id="confirmPassword" name="confirmPassword" type="password" required minlength="6" maxlength="72" />
          </div>
        </div>

        <div class="actions">
          <button type="submit">Đăng ký</button>
        </div>

        <p class="small">Bằng việc đăng ký, bạn đồng ý với điều khoản sử dụng.</p>
      </form>
    </div>
  </div>

  <script>
    (function () {
      const form = document.querySelector('form');
      const pw = document.getElementById('password');
      const cpw = document.getElementById('confirmPassword');

      function check() {
        if (pw.value !== cpw.value) {
          cpw.setCustomValidity('Mật khẩu nhập lại không khớp');
        } else {
          cpw.setCustomValidity('');
        }
      }

      pw.addEventListener('input', check);
      cpw.addEventListener('input', check);
      form.addEventListener('submit', check);
    })();
  </script>
</body>
</html>
