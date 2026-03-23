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

        <c:if test="${param.msg == 'updated'}"><div class="msg-box msg-success">Profile updated successfully.</div></c:if>
        <c:if test="${param.msg == 'invalid_name'}"><div class="msg-box msg-error">Full name must be 2-100 characters.</div></c:if>
        <c:if test="${param.msg == 'invalid_avatar'}"><div class="msg-box msg-error">Avatar must be a PNG, JPG, JPEG or WEBP image.</div></c:if>
        <c:if test="${param.msg == 'update_failed'}"><div class="msg-box msg-error">Cannot update profile right now.</div></c:if>

        <div class="profile-card">
            <form method="post" action="${pageContext.request.contextPath}/profile" enctype="multipart/form-data">
                <div class="profile-avatar-panel">
                    <div class="profile-avatar-preview">
                        <c:choose>
                            <c:when test="${not empty profileUser.avatar}">
                                <img src="${pageContext.request.contextPath}/assets/${profileUser.avatar}" alt="Avatar preview">
                            </c:when>
                            <c:otherwise>
                                <img src="https://ui-avatars.com/api/?name=${profileUser.fullName}&background=6c5ce7&color=fff" alt="Avatar preview">
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="profile-avatar-copy">
                        <div class="profile-avatar-title">Profile picture</div>
                        <p>Upload a new avatar for your account. Recommended square image.</p>
                        <input type="file" name="avatarFile" accept=".png,.jpg,.jpeg,.webp" class="text-input file-input">
                    </div>
                </div>

                <div class="field-grid profile-field-grid">
                    <div>
                        <label class="field-label">Full Name</label>
                        <input type="text" name="fullName" value="${profileUser.fullName}" maxlength="100" required class="text-input">
                    </div>

                    <div>
                        <label class="field-label">Email</label>
                        <input type="email" value="${profileUser.email}" readonly class="text-input read-only-input">
                    </div>

                    <div>
                        <label class="field-label">Member Since</label>
                        <input type="text" value="${profileUser.createdAt}" readonly class="text-input read-only-input">
                    </div>
                </div>

                <div class="settings-actions">
                    <button type="submit" class="install-btn settings-btn">Save Changes</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
