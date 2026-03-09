<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="activeMenu" value="dashboard" />

<!DOCTYPE html>
<html>
<head>
    <title>Review Progress Details</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/reviewer/reviewer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/reviewer/submit.css">
</head>
<body>

<div class="layout">
    <%@ include file="layout/sidebar.jsp" %>

    <main class="main">
        <div class="review-page-header">
            <div>
                <h1>Review Progress Details</h1>
                <p>Overview of apps in review and completed reviews</p>
            </div>

            <a href="${pageContext.request.contextPath}/reviewer_dashboard" class="back-btn">
                Back to Dashboard
            </a>
        </div>

        <div class="software-review-card">
            <div class="software-meta-grid">
                <div class="meta-box">
                    <span class="meta-label">Apps In Review</span>
                    <span class="meta-value">${inReviewCount}</span>
                </div>

                <div class="meta-box">
                    <span class="meta-label">Apps Reviewed</span>
                    <span class="meta-value">${reviewedCount}</span>
                </div>
            </div>
        </div>
    </main>
</div>

</body>
</html>