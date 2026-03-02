<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
    <head>
        <title>Approval Review ${pendingDetail.appName}</title>
        <link rel="stylesheet" href="/assets/css/Approval/approval.css">
    </head>
    <body>
        <div class="app">
            <jsp:include page="./layout/sidebar.jsp"/>
            <div class="content-review">
                <div class="review-wrap">
                    <h1 class="review-title">Approval Review: ${pendingDetail.appName}</h1>
                    <div class="review-sub">Final decision on app publication</div>

                    <div class="app-details">
                        <h3>Application Details</h3>
                        <ul>
                            <li><b>App Name:</b> <c:out value="${pendingDetail.appName}"/></li>
                            <li><b>Category:</b> <c:out value="${pendingDetail.category.categoryName}"/></li>
                            <li><b>Description:</b> <c:out value="${pendingDetail.softwareDetail.description}"/></li>
                            <li><b>Vendor:</b> <c:out value="${pendingDetail.user.fullName}"/></li>
                            <li><b>Version:</b> <c:out value="${pendingDetail.softwareVersion.versionName}"/></li>
                        </ul>
                    </div>

                    <div class="technical-review">
                        <h3>Technical Review Report</h3>
                        <div class="review-report">
                            <div class="review-item">
                                <span class="review-label">Security Check</span>
                                <span class="status-pass">Pass</span>
                            </div>
                            <div class="review-item">
                                <span class="review-label">Malware Scan</span>
                                <span class="status-pass">Pass</span>
                            </div>
                            <div class="review-item">
                                <span class="review-label">UI/UX Quality</span>
                                <div class="progress-bar">
                                    <div class="progress" style="width: 80%;"></div>
                                </div>
                                <span class="score">8/10</span>
                            </div>
                            <div class="review-item">
                                <span class="review-label">Performance</span>
                                <div class="progress-bar">
                                    <div class="progress" style="width: 90%;"></div>
                                </div>
                                <span class="score">9/10</span>
                            </div>
                        </div>
                    </div>

                    <div class="review-notes">
                        <h3>Reviewer Notes</h3>
                        <p>Well-structured application with good security practices. UI is intuitive.</p>
                    </div>

                    <div class="review-recommendation">
                        <h3>Reviewer Recommendation</h3>
                        <button class="btn-approve">APPROVE</button>
                    </div>

                    <form method="post" action="${pageContext.request.contextPath}/approval_pending_detail">

                        <input type="hidden" name="softwareId"
                               value="${softwareId}" />


                        <div class="your-decision">
                            <h3>Your Decision</h3>
                            <div class="decision-options">
                                <input type="radio" id="approve" name="decision" value="APPROVED" hidden>
                                <label for="approve" class="decision-btn approve-btn">
                                    Approve
                                </label>

                                <input type="radio" id="reject" name="decision" value="REJECTED" hidden>
                                <label for="reject" class="decision-btn reject-btn">
                                    Reject
                                </label>
                            </div>

                            <textarea name="note" placeholder="Decision Notes (optional)"></textarea>
                        </div>

                        <div class="action-buttons">
                            <button type="button" class="btn-cancel" onclick="window.location.href = '${pageContext.request.contextPath}/approval_history'">Cancel</button>
                            <button type="submit" class="btn-submit">Submit Decision</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>