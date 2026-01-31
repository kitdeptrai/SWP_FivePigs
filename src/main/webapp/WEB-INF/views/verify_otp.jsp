<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/31/2026
  Time: 11:04 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h2>Nhập mã OTP</h2>

<form method="post">
    <input name="otp" placeholder="Nhập OTP">
    <button>Xác nhận</button>
</form>

<c:if test="${not empty error}">
    <p style="color:red">${error}</p>
</c:if>

