<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Review Software</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/reviewer/reviewer.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/reviewer/submit.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    </head>
    <body class="review-submit-page">

        <div class="review-layout">
            <jsp:include page="layout/sidebar.jsp" />

            <main class="review-content">
                <div class="review-page-header">
                    <div>
                        <h1>Review Software</h1>
                        <p>Evaluate software quality and submit your review result</p>
                    </div>

                    <a href="${pageContext.request.contextPath}/reviewer_pending" class="back-btn">
                        <i class="fa-solid fa-arrow-left"></i> Back to Pending Reviews
                    </a>
                </div>

                <c:if test="${not empty error}">
                    <div class="review-alert error-alert">
                        ${error}
                    </div>
                </c:if>

                <div class="software-review-card">
                    <div class="software-overview">
                        <div class="software-thumb">
                            <img src="${pageContext.request.contextPath}/${software.imageUrl}" 
                                 alt="${software.name}"
                                 onerror="this.src='${pageContext.request.contextPath}/assets/images/default.png'">
                        </div>

                        <div class="software-main-info">
                            <h2>${software.name}</h2>
                            <p class="software-desc">${software.shortDescription}</p>

                            <div class="software-meta-grid">
                                <div class="meta-box">
                                    <span class="meta-label">Version</span>
                                    <span class="meta-value">${software.version}</span>
                                </div>

                                <div class="meta-box">
                                    <span class="meta-label">Category</span>
                                    <span class="meta-value">${software.categoryName}</span>
                                </div>

                                <div class="meta-box">
                                    <span class="meta-label">Price</span>
                                    <span class="meta-value">$${software.price}</span>
                                </div>

                                <div class="meta-box">
                                    <span class="meta-label">Review Type</span>
                                    <span class="meta-value">Technical Review</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <form method="post" action="${pageContext.request.contextPath}/review_software" class="review-form">
                        <input type="hidden" name="softwareId" value="${software.softwareId}">

                        <div class="review-section">
                            <h3>Compliance Checklist</h3>
                            <div class="checklist-grid">
                                <label class="check-item">
                                    <input type="checkbox" name="no_malware" value="1">
                                    <span>No malware or malicious code detected</span>
                                </label>

                                <label class="check-item">
                                    <input type="checkbox" name="no_copyright_violation" value="1">
                                    <span>No copyright or legal violations</span>
                                </label>

                                <label class="check-item">
                                    <input type="checkbox" name="no_spam_content" value="1">
                                    <span>No spam or inappropriate content</span>
                                </label>
                            </div>
                        </div>

                        <div class="review-section">
                            <h3>Scoring Criteria</h3>
                            <div class="score-grid">
                                <div class="form-group">
                                    <label for="ui_ux_score">UI/UX Design</label>
                                    <input type="number" id="ui_ux_score" name="ui_ux_score" min="0" max="10" required>
                                </div>

                                <div class="form-group">
                                    <label for="technical_score">Technical Quality</label>
                                    <input type="number" id="technical_score" name="technical_score" min="0" max="10" required>
                                </div>

                                <div class="form-group">
                                    <label for="performance_score">Performance</label>
                                    <input type="number" id="performance_score" name="performance_score" min="0" max="10" required>
                                </div>

                                <div class="form-group">
                                    <label for="documentation_score">Documentation</label>
                                    <input type="number" id="documentation_score" name="documentation_score" min="0" max="10" required>
                                </div>
                            </div>
                        </div>

                        <div class="review-section">
                            <h3>Detailed Feedback</h3>
                            <div class="form-group">
                                <label for="review_note">Review Note</label>
                                <textarea id="review_note" name="review_note" rows="6"
                                          placeholder="Provide detailed feedback about the software..." required></textarea>
                            </div>
                        </div>

                        <div class="review-actions">
                            <a href="${pageContext.request.contextPath}/reviewer_pending" class="cancel-btn">Cancel</a>
                            <button type="submit" class="submit-review-btn">
                                <i class="fa-solid fa-paper-plane"></i> Submit Review
                            </button>
                        </div>
                    </form>
                </div>
            </main>
        </div>

    </body>
</html>