<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Reset password</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />
</head>
<body>
<div class="container">
    <div class="card">
        <h1>Reset password</h1>
        <p class="subtitle">Please set a new password for account <strong><c:out value="${sessionScope.reset_email}"/></strong>.</p>

        <c:if test="${not empty error}">
            <div class="alert danger">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert success">${success}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/reset-password" autocomplete="off">
            <div class="actions" style="justify-content: space-between;">
                <a class="small" href="${pageContext.request.contextPath}/forgot-password">Change email</a>
                <a class="small" href="${pageContext.request.contextPath}/verify-reset-otp">Back to OTP verification</a>
            </div>

            <div class="field">
                <label for="password">New password</label>
                <input id="password" name="password" type="password" required minlength="6" maxlength="72" />
            </div>

            <div class="field">
                <label for="confirmPassword">Confirm new password</label>
                <input id="confirmPassword" name="confirmPassword" type="password" required minlength="6" maxlength="72" />
            </div>

            <div class="actions">
                <button type="submit">Reset password</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
