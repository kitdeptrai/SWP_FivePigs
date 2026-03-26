<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Registration Successful</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />
</head>
<body>
  <div class="container">
    <div class="card">
      <h1>Registration Successful</h1>
      <p class="subtitle">Hello <strong><c:out value="${fullName}"/></strong>, your account has been created.</p>
      <div class="alert success">You can now continue to the login page.</div>
      <p class="small">
        <a href="${pageContext.request.contextPath}/login">Login now</a> | 
        <a href="${pageContext.request.contextPath}/register">Create another account</a>
      </p>
    </div>
  </div>
</body>
</html>
