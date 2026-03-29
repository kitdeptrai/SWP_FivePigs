<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FIVEPIGS - My Library</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css?v=20260322report">
</head>
<body>

<jsp:include page="/WEB-INF/views/customer/sidebar.jsp">
    <jsp:param name="activePage" value="library"/>
</jsp:include>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/customer/header.jsp"></jsp:include>

    <div id="library" class="content-section active-section">
        <div style="display:flex;justify-content:space-between;align-items:flex-end;gap:16px;flex-wrap:wrap;margin-bottom:20px;">
            <h2 style="margin:0; font-size: 44px; font-weight: 800;">My Library</h2>
            <form method="get" action="${pageContext.request.contextPath}/library" style="display:flex;gap:10px;align-items:center;flex-wrap:wrap;background:#fff;padding:10px 12px;border-radius:14px;box-shadow:0 4px 16px rgba(15,23,42,0.05);">
                <label for="librarySort" style="font-weight:700;color:#374151;">Sort</label>
                <select id="librarySort" name="sort" style="border:1px solid #dbe1ec;border-radius:10px;padding:8px 10px;">
                    <option value="date" ${selectedSort == 'date' ? 'selected' : ''}>Purchase date</option>
                    <option value="name" ${selectedSort == 'name' ? 'selected' : ''}>Name</option>
                </select>
                <select name="order" style="border:1px solid #dbe1ec;border-radius:10px;padding:8px 10px;">
                    <option value="desc" ${selectedOrder == 'desc' ? 'selected' : ''}>Descending</option>
                    <option value="asc" ${selectedOrder == 'asc' ? 'selected' : ''}>Ascending</option>
                </select>
                <button type="submit" class="install-btn" style="padding:8px 14px;box-shadow:none;">Apply</button>
            </form>
        </div>

        <c:if test="${param.msg == 'not_owned'}">
            <div style="background:#ffecec;color:#9a2c2c;border-radius:10px;padding:10px 12px;margin-bottom:14px;">You do not own this software.</div>
        </c:if>
        <c:if test="${param.msg == 'trial_started'}">
            <div style="background:#ecfdf3;color:#166534;border-radius:10px;padding:10px 12px;margin-bottom:14px;">Your demo trial has started. The product is now in your Library.</div>
        </c:if>
        <c:if test="${param.reportMsg == 'not_owned'}">
            <div style="background:#ffecec;color:#9a2c2c;border-radius:10px;padding:10px 12px;margin-bottom:14px;">You can only report products you currently have access to.</div>
        </c:if>
        <c:if test="${param.reportMsg == 'submitted'}">
            <div style="background:#ecfdf3;color:#166534;border-radius:10px;padding:10px 12px;margin-bottom:14px;">Your report has been sent successfully.</div>
        </c:if>
        <c:if test="${param.reportMsg == 'invalid_reason'}">
            <div style="background:#ffecec;color:#9a2c2c;border-radius:10px;padding:10px 12px;margin-bottom:14px;">Please describe the issue before sending your report.</div>
        </c:if>
        <c:if test="${param.reportMsg == 'invalid_software' || param.reportMsg == 'failed'}">
            <div style="background:#ffecec;color:#9a2c2c;border-radius:10px;padding:10px 12px;margin-bottom:14px;">We could not send your report right now.</div>
        </c:if>
        <c:if test="${param.shareMsg == 'shared'}">
            <div class="msg-box msg-success">Access shared successfully.</div>
        </c:if>
        <c:if test="${param.shareMsg == 'removed'}">
            <div class="msg-box msg-success">User removed from this license.</div>
        </c:if>
        <c:if test="${param.shareMsg == 'slot_full'}">
            <div class="msg-box msg-error">This plan has no seats left.</div>
        </c:if>
        <c:if test="${param.shareMsg == 'already_assigned'}">
            <div class="msg-box msg-error">That user already has access.</div>
        </c:if>
        <c:if test="${param.shareMsg == 'user_not_found' || param.shareMsg == 'invalid_email' || param.shareMsg == 'invalid_user'}">
            <div class="msg-box msg-error">We could not find that user.</div>
        </c:if>
        <c:if test="${param.shareMsg == 'not_owner'}">
            <div class="msg-box msg-error">You do not manage this license.</div>
        </c:if>
        <c:if test="${param.shareMsg == 'cannot_remove_owner'}">
            <div class="msg-box msg-error">Owner access cannot be removed.</div>
        </c:if>

        <c:choose>
            <c:when test="${empty libraryList}">
                <div style="background:#fff;border-radius:16px;padding:30px;color:#6f7691;">
                    Library is empty. Go to store and checkout to own products.
                </div>
            </c:when>

            <c:otherwise>
                <div class="library-grid">
                    <c:forEach var="sw" items="${libraryList}" varStatus="loop">
                        <div class="lib-card">
                            <div class="lib-thumb ${loop.index % 3 == 0 ? 'bg-mc' : (loop.index % 3 == 1 ? 'bg-cod' : 'bg-music')}">
                                <c:choose>
                                    <c:when test="${not empty sw.iconUrl}">
                                        <img src="${pageContext.request.contextPath}/${sw.iconUrl}" alt="${sw.name}"
                                             style="width:68px;height:68px;border-radius:14px;object-fit:cover;box-shadow:0 8px 18px rgba(0,0,0,.15);">
                                    </c:when>
                                    <c:otherwise>
                                        <i class="${loop.index % 3 == 0 ? 'fa-solid fa-cube' : (loop.index % 3 == 1 ? 'fa-solid fa-gun' : 'fa-brands fa-apple')}"
                                           style="font-size:20px;"></i>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="lib-info">
                                <h4 style="font-size:25px; line-height:1.0; margin-bottom:6px; font-weight:800;">${sw.name}</h4>
                                <c:choose>
                                    <c:when test="${downloadedMap[sw.softwareId]}">
                                        <p style="font-size:15px; color:#2f855a; font-weight:700;">Downloaded</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p style="font-size:15px; color:#5a67d8; font-weight:700;">Owned</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div style="display:flex; gap:8px; padding:0 12px 12px;">
                                <form method="post" action="${pageContext.request.contextPath}/library/download" style="margin:0;">
                                    <input type="hidden" name="softwareId" value="${sw.softwareId}">
                                    <button type="submit" class="install-btn" style="padding:8px 14px; font-size:14px; box-shadow:none;">
                                        <c:choose>
                                            <c:when test="${downloadedMap[sw.softwareId]}">Download Again</c:when>
                                            <c:otherwise>Download</c:otherwise>
                                        </c:choose>
                                    </button>
                                </form>
                                <a href="${pageContext.request.contextPath}/product?pid=${sw.softwareId}" class="install-btn"
                                   style="padding:8px 14px; font-size:14px; box-shadow:none; background:#eceff7; color:#2d3748; text-decoration:none;">
                                    Detail
                                </a>
                            </div>

                            <div class="library-card-support">
                                <button type="button" class="library-report-trigger"
                                        data-software-id="${sw.softwareId}"
                                        data-software-name="${sw.name}">
                                    Report an issue
                                </button>
                            </div>

                            <c:if test="${not empty ownedLicenseMap[sw.softwareId]}">
                                <c:set var="licenseInfo" value="${ownedLicenseMap[sw.softwareId]}"/>
                                <details class="library-share-panel" ${param.shareSoftwareId == sw.softwareId ? 'open' : ''} ${licenseInfo.maxUsers gt 1 ? '' : 'style="display:none;"'}>
                                    <summary>Manage access</summary>
                                    <div class="library-share-box">
                                        <div class="library-share-header">
                                            <strong>${empty licenseInfo.planName ? 'Shared license' : licenseInfo.planName}</strong>
                                            <span>${licenseInfo.assignedCount}/${licenseInfo.maxUsers} seats used</span>
                                        </div>

                                        <form method="post" action="${pageContext.request.contextPath}/license/share" class="library-share-form">
                                            <input type="hidden" name="softwareId" value="${sw.softwareId}">
                                            <label for="share-email-${sw.softwareId}">Share with email</label>
                                            <div class="library-share-form-row">
                                                <input id="share-email-${sw.softwareId}" type="email" name="shareEmail" placeholder="customer@example.com" required>
                                                <button type="submit">Share</button>
                                            </div>
                                        </form>

                                        <div class="library-share-members">
                                            <c:choose>
                                                <c:when test="${not empty licenseInfo.assignedUsers}">
                                                    <c:forEach var="member" items="${licenseInfo.assignedUsers}">
                                                        <div class="library-share-member">
                                                            <div>
                                                                <strong>${member.fullName}</strong>
                                                                <span>${member.email}</span>
                                                            </div>
                                                            <c:choose>
                                                                <c:when test="${member.userId == sessionScope.user.userId}">
                                                                    <span class="library-share-owner">Owner</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <form method="post" action="${pageContext.request.contextPath}/license/unshare">
                                                                        <input type="hidden" name="softwareId" value="${sw.softwareId}">
                                                                        <input type="hidden" name="targetUserId" value="${member.userId}">
                                                                        <button type="submit" class="library-share-remove">Remove</button>
                                                                    </form>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="library-share-empty">No active members yet.</div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </details>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<div id="libraryReportOverlay" class="library-report-overlay"></div>
<div id="libraryReportModal" class="library-report-modal" aria-hidden="true">
    <button type="button" class="library-report-close" data-close-report-modal>
        <i class="fa-solid fa-xmark"></i>
    </button>
    <h3>Report an issue</h3>
    <p id="libraryReportSubtitle" class="library-report-subtitle">Tell us what is wrong with this product.</p>

    <form method="post" action="${pageContext.request.contextPath}/report-product" class="library-report-form">
        <input type="hidden" id="libraryReportSoftwareId" name="softwareId" value="">
        <input type="hidden" name="source" value="library">

        <label for="libraryReportReason">Reason</label>
        <textarea id="libraryReportReason" name="reason" rows="5" placeholder="Describe the problem..." required></textarea>

        <div class="library-report-actions">
            <button type="submit" class="install-btn" style="padding:10px 16px;box-shadow:none;">Send report</button>
            <button type="button" class="library-report-cancel" data-close-report-modal>Cancel</button>
        </div>
    </form>
</div>

<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>




