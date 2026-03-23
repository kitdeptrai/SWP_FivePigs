<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Forgot password</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />
</head>
<body>
<div class="container">
    <div class="card">
        <h1>Recover your password</h1>
        <p class="subtitle">Enter your registered email. We will send an OTP so you can reset your password.</p>

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
                <button type="submit">Send OTP</button>
            </div>

            <p class="small"><a href="${pageContext.request.contextPath}/login">Back to login</a></p>
        </form>
    </div>
</div>
</body>
</html>
