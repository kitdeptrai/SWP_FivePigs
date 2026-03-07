<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FIVEPIGS - My Profile</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/customer/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/customer/header.jsp"></jsp:include>

    <div class="content-section active-section profile-page">
        <h2 class="page-title">My Profile</h2>

        <c:if test="${param.msg == 'updated'}">
            <div class="msg-box msg-success">Profile updated successfully.</div>
        </c:if>
        <c:if test="${param.msg == 'invalid_name'}">
            <div class="msg-box msg-error">Full name must be 2-100 characters.</div>
        </c:if>
        <c:if test="${param.msg == 'update_failed'}">
            <div class="msg-box msg-error">Cannot update profile right now.</div>
        </c:if>

        <div class="profile-card">
            <form method="post" action="${pageContext.request.contextPath}/profile">
                <div class="field-grid">
                    <div>
                        <label class="field-label">Full Name</label>
                        <input type="text" name="fullName" value="${profileUser.fullName}" maxlength="100" required class="text-input">
                    </div>

                    <div>
                        <label class="field-label">Email</label>
                        <input type="email" value="${profileUser.email}" readonly class="text-input read-only-input">
                    </div>

                    <div class="settings-actions">
                        <button type="submit" class="install-btn settings-btn">Save Changes</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
