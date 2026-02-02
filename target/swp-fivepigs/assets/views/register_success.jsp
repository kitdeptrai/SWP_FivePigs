<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Đăng ký thành công</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />
</head>
<body>
  <div class="container">
    <div class="card">
      <h1>Đăng ký thành công</h1>
      <p class="subtitle">Chào <strong><c:out value="${fullName}"/></strong>, tài khoản của bạn đã được tạo.</p>
      <div class="alert success">Bạn có thể tiếp tục xây dựng trang đăng nhập ở bước tiếp theo.</div>
      <p class="small">
        <a href="${pageContext.request.contextPath}/login">Đăng nhập ngay</a> | 
        <a href="${pageContext.request.contextPath}/register">Tạo tài khoản khác</a>
      </p>
    </div>
  </div>
</body>
</html>
