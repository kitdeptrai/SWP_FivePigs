<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
    <head>
        <title>Approval Review ${pendingDetail.appName}</title>
        <link rel="stylesheet" href="/assets/css/Approval/approval.css">
        <style>
            /* ── Review Report từ Reviewer ── */
            .reviewer-report-section {
                background: #14182b;
                border: 1px solid #1f2538;
                border-radius: 16px;
                padding: 22px;
                margin-bottom: 22px;
            }
            .reviewer-report-section h3 {
                font-size: 14px;
                font-weight: 800;
                margin-bottom: 16px;
                color: #ffffff;
                display: flex;
                align-items: center;
                gap: 8px;
            }
            .reviewer-meta {
                display: flex;
                gap: 20px;
                margin-bottom: 18px;
                flex-wrap: wrap;
            }
            .reviewer-meta-item {
                background: #0f1220;
                border: 1px solid #1f2538;
                border-radius: 10px;
                padding: 10px 16px;
                font-size: 12px;
                color: #94a3b8;
            }
            .reviewer-meta-item strong {
                display: block;
                color: #e2e8f0;
                font-size: 14px;
                margin-top: 2px;
            }
            .check-grid {
                display: grid;
                grid-template-columns: repeat(3, 1fr);
                gap: 12px;
                margin-bottom: 18px;
            }
            .check-item {
                background: #0f1220;
                border: 1px solid #1f2538;
                border-radius: 12px;
                padding: 14px 16px;
                display: flex;
                align-items: center;
                gap: 10px;
            }
            .check-icon-pass {
                width: 28px;
                height: 28px;
                border-radius: 50%;
                background: rgba(34,197,94,0.15);
                color: #22c55e;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 14px;
                flex-shrink: 0;
            }
            .check-icon-fail {
                width: 28px;
                height: 28px;
                border-radius: 50%;
                background: rgba(239,68,68,0.15);
                color: #ef4444;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 14px;
                flex-shrink: 0;
            }
            .check-label {
                font-size: 13px;
                font-weight: 600;
                color: #e2e8f0;
            }
            .check-status-pass {
                margin-left: auto;
                background: rgba(34,197,94,0.15);
                color: #22c55e;
                padding: 4px 10px;
                border-radius: 6px;
                font-size: 11px;
                font-weight: 700;
            }
            .check-status-fail {
                margin-left: auto;
                background: rgba(239,68,68,0.15);
                color: #ef4444;
                padding: 4px 10px;
                border-radius: 6px;
                font-size: 11px;
                font-weight: 700;
            }
            .score-grid {
                display: grid;
                grid-template-columns: repeat(2, 1fr);
                gap: 14px;
                margin-bottom: 18px;
            }
            .score-item {
                background: #0f1220;
                border: 1px solid #1f2538;
                border-radius: 12px;
                padding: 14px 16px;
            }
            .score-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 8px;
            }
            .score-label {
                font-size: 13px;
                font-weight: 600;
                color: #e2e8f0;
            }
            .score-value {
                font-size: 13px;
                font-weight: 700;
                color: #4f6cff;
            }
            .score-bar {
                height: 6px;
                background: #1f2538;
                border-radius: 999px;
                overflow: hidden;
            }
            .score-fill {
                height: 100%;
                background: linear-gradient(90deg, #4f6cff, #7c9cff);
                border-radius: 999px;
                transition: width 0.5s ease;
            }
            .total-score-box {
                display: flex;
                align-items: center;
                justify-content: space-between;
                background: #0f1220;
                border: 1px solid #1f2538;
                border-radius: 12px;
                padding: 16px 20px;
                margin-bottom: 18px;
            }
            .total-score-label {
                font-size: 14px;
                font-weight: 700;
                color: #e2e8f0;
            }
            .total-score-number {
                font-size: 28px;
                font-weight: 900;
                background: linear-gradient(135deg, #4f6cff, #7c9cff);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
                background-clip: text;
            }
            .reviewer-decision-badge {
                padding: 8px 18px;
                border-radius: 999px;
                font-size: 13px;
                font-weight: 800;
                letter-spacing: 0.5px;
            }
            .badge-approved {
                background: rgba(34,197,94,0.15);
                color: #22c55e;
                border: 1px solid rgba(34,197,94,0.3);
            }
            .badge-rejected {
                background: rgba(239,68,68,0.15);
                color: #ef4444;
                border: 1px solid rgba(239,68,68,0.3);
            }
            .review-note-box {
                background: #0f1220;
                border: 1px solid #1f2538;
                border-radius: 12px;
                padding: 14px 16px;
                color: #cbd5e1;
                font-size: 13px;
                line-height: 1.6;
            }
            .no-review-notice {
                background: #0f1220;
                border: 1px dashed #1f2538;
                border-radius: 12px;
                padding: 24px;
                text-align: center;
                color: #94a3b8;
                font-size: 14px;
            }
            .section-badge {
                background: rgba(79,108,255,0.12);
                color: #4f6cff;
                border: 1px solid rgba(79,108,255,0.3);
                padding: 3px 10px;
                border-radius: 999px;
                font-size: 11px;
                font-weight: 700;
            }
        </style>
    </head>
    <body>
        <div class="app">
            <jsp:include page="./layout/sidebar.jsp"/>
            <div class="content-review">
                <div class="review-wrap">
                    <h1 class="review-title">Approval Review: ${pendingDetail.appName}</h1>
                    <div class="review-sub">Final decision on app publication</div>

                    <%-- ── Application Details ── --%>
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

                    <%-- ── Technical Review Report từ Reviewer ── --%>
                    <div class="reviewer-report-section">
                        <h3>
                            📋 Reviewer's Technical Report
                            <span class="section-badge">From Reviewer</span>
                        </h3>

                        <c:choose>
                            <c:when test="${reviewScore != null}">

                                <%-- Meta info: reviewer & date --%>
                                <div class="reviewer-meta">
                                    <div class="reviewer-meta-item">
                                        👤 Reviewed by
                                        <strong><c:out value="${reviewScore.reviewerName != null ? reviewScore.reviewerName : 'Unknown Reviewer'}"/></strong>
                                    </div>
                                    <div class="reviewer-meta-item">
                                        📅 Review Date
                                        <strong>
                                            <c:choose>
                                                <c:when test="${reviewScore.createdAt != null}">
                                                    <fmt:formatDate value="${reviewScore.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                                </c:when>
                                                <c:otherwise>N/A</c:otherwise>
                                            </c:choose>
                                        </strong>
                                    </div>
                                    <div class="reviewer-meta-item">
                                        🏁 Reviewer's Decision
                                        <strong>
                                            <c:choose>
                                                <c:when test="${reviewScore.decision == 'APPROVED'}">
                                                    <span class="reviewer-decision-badge badge-approved">✅ APPROVED</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="reviewer-decision-badge badge-rejected">❌ REJECTED</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </strong>
                                    </div>
                                </div>

                                <%-- Safety checks --%>
                                <div style="margin-bottom:10px; font-size:12px; color:#94a3b8; font-weight:600; text-transform:uppercase; letter-spacing:0.5px;">
                                    Safety Checks
                                </div>
                                <div class="check-grid">
                                    <div class="check-item">
                                        <div class="${reviewScore.noMalware ? 'check-icon-pass' : 'check-icon-fail'}">
                                            ${reviewScore.noMalware ? '✓' : '✗'}
                                        </div>
                                        <span class="check-label">No Malware</span>
                                        <span class="${reviewScore.noMalware ? 'check-status-pass' : 'check-status-fail'}">
                                            ${reviewScore.noMalware ? 'PASS' : 'FAIL'}
                                        </span>
                                    </div>
                                    <div class="check-item">
                                        <div class="${reviewScore.noCopyrightViolation ? 'check-icon-pass' : 'check-icon-fail'}">
                                            ${reviewScore.noCopyrightViolation ? '✓' : '✗'}
                                        </div>
                                        <span class="check-label">No Copyright Violation</span>
                                        <span class="${reviewScore.noCopyrightViolation ? 'check-status-pass' : 'check-status-fail'}">
                                            ${reviewScore.noCopyrightViolation ? 'PASS' : 'FAIL'}
                                        </span>
                                    </div>
                                    <div class="check-item">
                                        <div class="${reviewScore.noSpamContent ? 'check-icon-pass' : 'check-icon-fail'}">
                                            ${reviewScore.noSpamContent ? '✓' : '✗'}
                                        </div>
                                        <span class="check-label">No Spam Content</span>
                                        <span class="${reviewScore.noSpamContent ? 'check-status-pass' : 'check-status-fail'}">
                                            ${reviewScore.noSpamContent ? 'PASS' : 'FAIL'}
                                        </span>
                                    </div>
                                </div>

                                <%-- Score breakdown --%>
                                <div style="margin-bottom:10px; font-size:12px; color:#94a3b8; font-weight:600; text-transform:uppercase; letter-spacing:0.5px;">
                                    Quality Scores (out of 10)
                                </div>
                                <div class="score-grid">
                                    <div class="score-item">
                                        <div class="score-header">
                                            <span class="score-label">🎨 UI/UX Quality</span>
                                            <span class="score-value">${reviewScore.uiUxScore}/10</span>
                                        </div>
                                        <div class="score-bar">
                                            <div class="score-fill" style="width: ${reviewScore.uiUxScore * 10}%;"></div>
                                        </div>
                                    </div>
                                    <div class="score-item">
                                        <div class="score-header">
                                            <span class="score-label">⚙️ Technical Quality</span>
                                            <span class="score-value">${reviewScore.technicalScore}/10</span>
                                        </div>
                                        <div class="score-bar">
                                            <div class="score-fill" style="width: ${reviewScore.technicalScore * 10}%;"></div>
                                        </div>
                                    </div>
                                    <div class="score-item">
                                        <div class="score-header">
                                            <span class="score-label">🚀 Performance</span>
                                            <span class="score-value">${reviewScore.performanceScore}/10</span>
                                        </div>
                                        <div class="score-bar">
                                            <div class="score-fill" style="width: ${reviewScore.performanceScore * 10}%;"></div>
                                        </div>
                                    </div>
                                    <div class="score-item">
                                        <div class="score-header">
                                            <span class="score-label">📚 Documentation</span>
                                            <span class="score-value">${reviewScore.documentationScore}/10</span>
                                        </div>
                                        <div class="score-bar">
                                            <div class="score-fill" style="width: ${reviewScore.documentationScore * 10}%;"></div>
                                        </div>
                                    </div>
                                </div>

                                <%-- Total score --%>
                                <div class="total-score-box">
                                    <span class="total-score-label">📊 Overall Score (Average)</span>
                                    <span class="total-score-number">
                                        <fmt:formatNumber value="${reviewScore.totalScore}" maxFractionDigits="1"/>/10
                                    </span>
                                </div>

                                <%-- Reviewer notes --%>
                                <div style="margin-bottom:10px; font-size:12px; color:#94a3b8; font-weight:600; text-transform:uppercase; letter-spacing:0.5px;">
                                    Reviewer's Notes
                                </div>
                                <div class="review-note-box">
                                    <c:choose>
                                        <c:when test="${not empty reviewScore.reviewNote}">
                                            <c:out value="${reviewScore.reviewNote}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <em style="color:#64748b;">No additional notes from reviewer.</em>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                            </c:when>
                            <c:otherwise>
                                <div class="no-review-notice">
                                    ⚠️ No reviewer report found for this software yet.<br>
                                    <small style="margin-top:6px; display:block;">The reviewer may not have submitted their evaluation yet.</small>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <%-- ── Your Decision Form ── --%>
                    <form method="post" action="${pageContext.request.contextPath}/approval_pending_detail">

                        <input type="hidden" name="softwareId" value="${softwareId}" />

                        <div class="your-decision">
                            <h3>Your Decision</h3>
                            <div class="decision-options">
                                <input type="radio" id="approve" name="decision" value="APPROVED" hidden>
                                <label for="approve" class="decision-btn approve-btn">
                                    ✅ Approve
                                </label>

                                <input type="radio" id="reject" name="decision" value="REJECTED" hidden>
                                <label for="reject" class="decision-btn reject-btn">
                                    ❌ Reject
                                </label>
                            </div>

                            <textarea name="note" placeholder="Decision Notes (optional)"></textarea>
                        </div>

                        <div class="action-buttons">
                            <button type="button" class="btn-cancel" onclick="window.location.href = '${pageContext.request.contextPath}/approval_pending'">← Back to Pending</button>
                            <button type="submit" class="btn-submit">Submit Decision</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>