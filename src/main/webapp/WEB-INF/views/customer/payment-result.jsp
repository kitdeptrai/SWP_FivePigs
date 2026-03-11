<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <body>
        <h2>${message}</h2>

<c:if test="${not empty amount}">
    <p>Số tiền: ${amount} VND</p>
</c:if>

<a href="${pageContext.request.contextPath}/cart">Quay lại giỏ hàng</a>
    </body>
</html>
