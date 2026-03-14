<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FIVEPIGS - Settings</title>
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
    <c:set var="currentTab" value="${empty selectedTab ? 'store_settings' : selectedTab}"/>

    <div class="content-section active-section settings-page">
        <h2 class="page-title">Settings</h2>

        <div class="settings-shell">
            <div class="settings-nav">
                <a href="${pageContext.request.contextPath}/settings?tab=payment_methods" class="settings-nav-item ${currentTab == 'payment_methods' ? 'active' : ''}"><i class="fa-regular fa-credit-card"></i>Payment methods</a>
                <a href="${pageContext.request.contextPath}/settings?tab=redeem_code" class="settings-nav-item ${currentTab == 'redeem_code' ? 'active' : ''}"><i class="fa-solid fa-gift"></i>Redeem code or gift cards</a>
                <a href="${pageContext.request.contextPath}/settings?tab=payment_help" class="settings-nav-item ${currentTab == 'payment_help' ? 'active' : ''}"><i class="fa-regular fa-circle-question"></i>Help with payment and refunds</a>
                <a href="${pageContext.request.contextPath}/settings?tab=devices" class="settings-nav-item ${currentTab == 'devices' ? 'active' : ''}"><i class="fa-solid fa-laptop"></i>Manage account and devices</a>
                <a href="${pageContext.request.contextPath}/settings?tab=feedback" class="settings-nav-item ${currentTab == 'feedback' ? 'active' : ''}"><i class="fa-regular fa-message"></i>Send feedback</a>
                <a href="${pageContext.request.contextPath}/settings?tab=store_settings" class="settings-nav-item ${currentTab == 'store_settings' ? 'active' : ''}"><i class="fa-solid fa-gear"></i>Store settings</a>
            </div>

            <div class="settings-panel">
                <c:if test="${param.msg == 'password_updated'}">
                    <div class="msg-box msg-success">Password updated successfully.</div>
                </c:if>
                <c:if test="${param.msg == 'wrong_current_password'}">
                    <div class="msg-box msg-error">Current password is incorrect.</div>
                </c:if>
                <c:if test="${param.msg == 'confirm_not_match'}">
                    <div class="msg-box msg-error">Confirm password does not match.</div>
                </c:if>
                <c:if test="${param.msg == 'invalid_password'}">
                    <div class="msg-box msg-error">New password must be 6-72 characters.</div>
                </c:if>
                <c:if test="${param.msg == 'missing_fields' || param.msg == 'user_not_found' || param.msg == 'update_failed'}">
                    <div class="msg-box msg-error">Cannot update settings right now.</div>
                </c:if>

                <c:choose>
                    <c:when test="${currentTab == 'payment_methods'}">
                        <h3 class="settings-title">Payment methods</h3>
                        <p class="settings-desc">Manage your saved cards and payment options.</p>
                        <div class="settings-card muted">
                            <i class="fa-regular fa-credit-card"></i>
                            <div>
                                <strong>No payment methods yet</strong>
                                <p>Add card/bank integration in next step to support real checkout.</p>
                            </div>
                        </div>
                    </c:when>

                    <c:when test="${currentTab == 'redeem_code'}">
                        <h3 class="settings-title">Redeem code or gift cards</h3>
                        <p class="settings-desc">Enter a promo code or gift card code to add credit.</p>
                        <div class="settings-card">
                            <label class="field-label" for="redeemCode">Code</label>
                            <input id="redeemCode" type="text" class="text-input" placeholder="XXXXX-XXXXX-XXXXX">
                            <button type="button" class="install-btn settings-btn">Redeem</button>
                        </div>
                    </c:when>

                    <c:when test="${currentTab == 'payment_help'}">
                        <h3 class="settings-title">Help with payment and refunds</h3>
                        <p class="settings-desc">Quick answers for payment and refund support.</p>
                        <div class="settings-list-card">
                            <div class="settings-list-item"><i class="fa-solid fa-receipt"></i>How to check order history</div>
                            <div class="settings-list-item"><i class="fa-solid fa-arrow-rotate-left"></i>Refund policy and timeline</div>
                            <div class="settings-list-item"><i class="fa-solid fa-circle-exclamation"></i>Payment failed troubleshooting</div>
                        </div>
                    </c:when>

                    <c:when test="${currentTab == 'devices'}">
                        <h3 class="settings-title">Manage account and devices</h3>
                        <p class="settings-desc">Track devices that used this account recently.</p>
                        <div class="settings-list-card">
                            <div class="settings-list-item"><i class="fa-solid fa-desktop"></i>Windows PC - Last active: Today</div>
                            <div class="settings-list-item"><i class="fa-solid fa-laptop"></i>Laptop - Last active: 2 days ago</div>
                            <div class="settings-list-item"><i class="fa-solid fa-mobile-screen-button"></i>Mobile Browser - Last active: 5 days ago</div>
                        </div>
                    </c:when>

                    <c:when test="${currentTab == 'feedback'}">
                        <h3 class="settings-title">Send feedback</h3>
                        <p class="settings-desc">Tell us what to improve in FIVEPIGS Store.</p>
                        <div class="settings-card">
                            <label class="field-label" for="feedbackText">Your feedback</label>
                            <textarea id="feedbackText" class="text-input textarea-input" placeholder="Write your feedback..."></textarea>
                            <button type="button" class="install-btn settings-btn">Send feedback</button>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <h3 class="settings-title">Store settings</h3>
                        <p class="settings-desc">Security and account-level store preferences.</p>

                        <div class="settings-card">
                            <h4 style="padding-bottom: 10px">Change Password</h4>

                            <form method="post" action="${pageContext.request.contextPath}/settings">
                                <input type="hidden" name="tab" value="store_settings">
                                <div class="field-grid">
                                    <div>
                                        <label class="field-label">Current Password</label>
                                        <input type="password" name="currentPassword" class="text-input" required maxlength="72">
                                    </div>

                                    <div>
                                        <label class="field-label">New Password</label>
                                        <input type="password" name="newPassword" class="text-input" required maxlength="72">
                                    </div>

                                    <div>
                                        <label class="field-label">Confirm New Password</label>
                                        <input type="password" name="confirmPassword" class="text-input" required maxlength="72">
                                    </div>

                                    <div class="settings-actions">
                                        <button type="submit" class="install-btn settings-btn">Save Password</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
