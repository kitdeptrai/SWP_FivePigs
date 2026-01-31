<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page isELIgnored="false" %>

<h2>Nhập mã OTP</h2>

<form method="post">
    <label for="otp">Mã OTP:</label>
    <input id="otp" name="otp" placeholder="Nhập OTP" required>
    <button type="submit">Xác nhận</button>
</form>

<c:if test="${not empty error}">
    <p style="color:red">${error}</p>
</c:if>

