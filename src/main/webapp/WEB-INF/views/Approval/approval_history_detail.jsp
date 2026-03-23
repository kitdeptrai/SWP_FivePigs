<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Approval Details</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Approval/approval.css">
</head>
<body>

<div class="app">
    <jsp:include page="./layout/sidebar.jsp"/>

    <div class="content-history-detail">
        <div class="history-detail-wrap">

            <!-- HEADER -->
            <div class="history-detail-header">
                <div>
                    <h1>Approval Details: ${historyDetail.appName}</h1>
                    <p>Review of approval decision</p>
                </div>

                <a href="approval_history" class="btn-back">
                    ← Back to History
                </a>
            </div>

            <!-- TIMELINE -->
            <div class="history-card">
                <h3>Timeline</h3>

                <div class="timeline">
                    <div class="timeline-item">
                        <div class="dot blue"></div>
                        <div>
                            <b>Submitted</b>
                            
                        </div>
                    </div>

                    <div class="timeline-item">
                        <div class="dot yellow"></div>
                        <div>
                            <b>Technical Review Completed</b>
                            
                        </div>
                    </div>

                    <div class="timeline-item">
                        <div class="dot green"></div>
                        <div>
                            <b>Approved</b>
                        </div>
                    </div>
                </div>
            </div>

            <!-- APPLICATION DETAILS -->
            <div class="history-card">
                <h3>Application Details</h3>

                <div class="app-banner">
                    <img src="https://cellphones.com.vn/sforum/wp-content/uploads/2023/08/hinh-nen-desktop-19.jpg" alt="">
                </div>

                <div class="app-grid">
                    <div>
                        <label>App Name</label>
                        <span>${historyDetail.appName}</span>
                    </div>
                    <div>
                        <label>Vendor</label>
                        <span>${historyDetail.user.fullName}</span>
                    </div>
                    <div>
                        <label>Category</label>
                        <span>${historyDetail.category.categoryName}</span>
                    </div>
                    <div>
                        <label>Version</label>
                        <span>${historyDetail.softwareVersion.versionName}</span>
                    </div>
                    <div>
                        <label>Price</label>
                        
                    </div>

                    <div class="full">
                        <label>Description</label>
                        <span>${historyDetail.softwareDetail.description}</span>
                    </div>
                </div>
            </div>

            <!-- TECHNICAL REVIEW -->
            <div class="history-card">
                <h3>Technical Review Report</h3>

                <div class="review-grid">
                    <div class="review-box">
                        <span>Security Check</span>
                        <b class="badge-pass">PASS</b>
                    </div>

                    <div class="review-box">
                        <span>Malware Scan</span>
                        <b class="badge-pass">PASS</b>
                    </div>

                    <div class="review-box">
                        <span>Legal Check</span>
                        <b class="badge-pass">PASS</b>
                    </div>
                </div>

                <!-- Progress -->
                <div class="progress-section">
                    <div class="progress-item">
                        <label>UI/UX Quality</label>
                        <div class="progress-bar">
                            <div class="progress green" style="width:90%"></div>
                        </div>
                        <span>9/10</span>
                    </div>

                    <div class="progress-item">
                        <label>Performance</label>
                        <div class="progress-bar">
                            <div class="progress yellow" style="width:80%"></div>
                        </div>
                        <span>8/10</span>
                    </div>
                </div>

                <div class="review-notes-box">
                    Excellent code quality and security implementation.
                </div>
            </div>

            <!-- FINAL DECISION -->
            <div class="history-card">
                <h3>Approval Decision</h3>
                <div class="decision-approved">
                    <span>${historyDetail.approvalProcess.decision}</span>
                </div>

                <div class="review-notes-box">
                    Application approved for marketplace publication.
                </div>
            </div>

        </div>
    </div>
</div>

</body>
</html>