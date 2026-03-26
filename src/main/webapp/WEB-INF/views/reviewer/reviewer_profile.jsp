<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="activeMenu" value="profile" />

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Reviewer Profile</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/reviewer/reviewer.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/reviewer/profile.css">
    </head>
    <body>
        <div class="layout">

            <%@ include file="layout/sidebar.jsp" %>

            <main class="main">
                <h1>Reviewer Profile</h1>
                <p class="subtitle">Cập nhật thông tin cá nhân, ảnh đại diện và mật khẩu</p>

                <c:if test="${param.msg == 'updated'}">
                    <div class="alert success-alert">Cập nhật thành công.</div>
                </c:if>
                <c:if test="${param.msg == 'invalid_name'}">
                    <div class="alert error-alert">Tên phải từ 2 đến 100 ký tự.</div>
                </c:if>
                <c:if test="${param.msg == 'invalid_email'}">
                    <div class="alert error-alert">Email không hợp lệ.</div>
                </c:if>
                <c:if test="${param.msg == 'email_exists'}">
                    <div class="alert error-alert">Email này đã tồn tại.</div>
                </c:if>
                <c:if test="${param.msg == 'invalid_avatar'}">
                    <div class="alert error-alert">Ảnh phải là PNG, JPG, JPEG hoặc WEBP.</div>
                </c:if>
                <c:if test="${param.msg == 'password_missing'}">
                    <div class="alert error-alert">Muốn đổi mật khẩu thì phải nhập đủ 3 ô mật khẩu.</div>
                </c:if>
                <c:if test="${param.msg == 'current_password_wrong'}">
                    <div class="alert error-alert">Mật khẩu hiện tại không đúng.</div>
                </c:if>
                <c:if test="${param.msg == 'password_too_short'}">
                    <div class="alert error-alert">Mật khẩu mới phải có ít nhất 6 ký tự.</div>
                </c:if>
                <c:if test="${param.msg == 'password_not_match'}">
                    <div class="alert error-alert">Xác nhận mật khẩu mới không khớp.</div>
                </c:if>
                <c:if test="${param.msg == 'update_failed'}">
                    <div class="alert error-alert">Không thể cập nhật lúc này.</div>
                </c:if>

                <div class="profile-wrapper">
                    <form method="post"
                          action="${pageContext.request.contextPath}/reviewer_profile"
                          enctype="multipart/form-data"
                          class="profile-form-card">

                        <div class="profile-top">
                            <div class="profile-avatar-box">
                                <c:choose>
                                    <c:when test="${not empty profileUser.avatar}">
                                        <img id="avatarPreview"
                                             class="profile-avatar-preview"
                                             src="${pageContext.request.contextPath}/assets/${profileUser.avatar}"
                                             alt="Avatar"
                                             data-default-src="${pageContext.request.contextPath}/assets/images/img2.png"
                                             onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/images/img2.png';">
                                    </c:when>
                                    <c:otherwise>
                                        <img id="avatarPreview"
                                             class="profile-avatar-preview"
                                             src="${pageContext.request.contextPath}/assets/images/img2.png"
                                             alt="Avatar"
                                             data-default-src="${pageContext.request.contextPath}/assets/images/img2.png">
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="profile-upload-box">
                                <label class="field-label" for="avatarFile">Ảnh đại diện</label>
                                <input type="file"
                                       id="avatarFile"
                                       name="avatarFile"
                                       accept="image/png,image/jpeg,image/webp"
                                       class="text-input"
                                       autocomplete="off">
                                <p class="helper-text">
                                    Chọn ảnh từ bất kỳ thư mục nào trong máy. Ảnh sẽ được xem trước ngay sau khi chọn.
                                </p>
                            </div>
                        </div>

                        <div class="profile-grid">
                            <div>
                                <label class="field-label">Họ và tên</label>
                                <input type="text"
                                       name="fullName"
                                       value="${profileUser.fullName}"
                                       maxlength="100"
                                       required
                                       class="text-input">
                            </div>

                            <div>
                                <label class="field-label">Email</label>
                                <input type="email"
                                       name="email"
                                       value="${profileUser.email}"
                                       maxlength="100"
                                       required
                                       class="text-input">
                            </div>
                        </div>

                        <div class="password-card">
                            <h3>Đổi mật khẩu</h3>

                            <div class="profile-grid">
                                <div>
                                    <label class="field-label">Mật khẩu hiện tại</label>
                                    <input type="password" name="currentPassword" class="text-input">
                                </div>

                                <div>
                                    <label class="field-label">Mật khẩu mới</label>
                                    <input type="password" name="newPassword" class="text-input">
                                </div>

                                <div>
                                    <label class="field-label">Xác nhận mật khẩu mới</label>
                                    <input type="password" name="confirmPassword" class="text-input">
                                </div>
                            </div>
                        </div>

                        <div class="profile-actions">
                            <button type="submit" class="save-btn">Lưu thay đổi</button>
                        </div>
                    </form>
                </div>
            </main>
        </div>

        <script>
            (function () {
                const avatarInput = document.getElementById("avatarFile");
                const avatarPreview = document.getElementById("avatarPreview");

                if (!avatarInput || !avatarPreview)
                    return;

                const defaultSrc = avatarPreview.getAttribute("data-default-src");
                const currentSrc = avatarPreview.getAttribute("src");

                avatarInput.addEventListener("change", function (event) {
                    const file = event.target.files && event.target.files[0];
                    if (!file)
                        return;

                    const validTypes = ["image/png", "image/jpeg", "image/webp"];
                    if (!validTypes.includes(file.type)) {
                        alert("Chỉ hỗ trợ file PNG, JPG, JPEG hoặc WEBP.");
                        avatarInput.value = "";
                        avatarPreview.src = currentSrc || defaultSrc;
                        return;
                    }

                    const objectUrl = URL.createObjectURL(file);
                    avatarPreview.src = objectUrl;

                    avatarPreview.onload = function () {
                        URL.revokeObjectURL(objectUrl);
                    };
                });
            })();
        </script>
    </body>
</html>