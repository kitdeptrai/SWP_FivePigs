<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Review Details</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/reviewer/reviewer.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/reviewer/submit.css">
    </head>
    <body class="review-submit-page">

        <div class="review-layout">
            <jsp:include page="layout/sidebar.jsp" />

            <main class="review-content">
                <div class="review-page-header">
                    <div>
                        <h1>Review Details</h1>
                        <p>View the detailed result of a completed review</p>
                    </div>

                    <a href="${pageContext.request.contextPath}/reviewer_history" class="back-btn">
                        Back to Review History
                    </a>
                </div>

                <div class="software-review-card">
                    <div class="software-overview">
                        <div class="software-thumb">
                            <img src="${pageContext.request.contextPath}/${review.imageUrl}"
                                 alt="${review.softwareName}"
                                 onerror="this.src='${pageContext.request.contextPath}/assets/images/default.png'">
                        </div>

                        <div class="software-main-info">
                            <h2>${review.softwareName}</h2>
                            <p class="software-desc">${review.shortDescription}</p>

                            <div class="software-meta-grid">
                                <div class="meta-box">
                                    <span class="meta-label">Version</span>
                                    <span class="meta-value">${review.version}</span>
                                </div>

                                <div class="meta-box">
                                    <span class="meta-label">Category</span>
                                    <span class="meta-value">${review.categoryName}</span>
                                </div>

                                <div class="meta-box">
                                    <span class="meta-label">Price</span>
                                    <span class="meta-value">$${review.price}</span>
                                </div>

                                <div class="meta-box">
                                    <span class="meta-label">Total Score</span>
                                    <span class="meta-value">${review.totalScore}</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="review-section">
                        <h3>Compliance Checklist</h3>
                        <div class="checklist-grid">
                            <label class="check-item">
                                <input type="checkbox" disabled <c:if test="${review.noMalware}">checked</c:if>>
                                    <span>No malware or malicious code detected</span>
                                </label>

                                <label class="check-item">
                                    <input type="checkbox" disabled <c:if test="${review.noCopyrightViolation}">checked</c:if>>
                                    <span>No copyright or legal violations</span>
                                </label>

                                <label class="check-item">
                                    <input type="checkbox" disabled <c:if test="${review.noSpamContent}">checked</c:if>>
                                    <span>No spam or inappropriate content</span>
                                </label>
                            </div>
                        </div>

                        <div class="review-section">
                            <h3>Scoring Criteria</h3>
                            <div class="score-grid">
                                <div class="form-group">
                                    <label>UI/UX Design</label>
                                    <input type="number" value="${review.uiUxScore}" readonly>
                            </div>

                            <div class="form-group">
                                <label>Technical Quality</label>
                                <input type="number" value="${review.technicalScore}" readonly>
                            </div>

                            <div class="form-group">
                                <label>Performance</label>
                                <input type="number" value="${review.performanceScore}" readonly>
                            </div>

                            <div class="form-group">
                                <label>Documentation</label>
                                <input type="number" value="${review.documentationScore}" readonly>
                            </div>
                        </div>
                    </div>

                    <div class="review-section">
                        <h3>Reviewer Comment</h3>
                        <div class="form-group">
                            <textarea rows="6" readonly>${review.reviewNote}</textarea>
                        </div>
                    </div>
                </div>
            </main>
        </div>

    </body>
</html>