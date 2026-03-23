<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="activeMenu" value="errorReports" />

<!DOCTYPE html>
<html>
    <head>
        <title>Report Details</title>

        <!-- CSS chung cho toàn bộ reviewer -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/reviewer/reviewer.css">

        <!-- CSS riêng cho màn error -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/reviewer/error.css">
    </head>
    <body>
        <div class="layout">
            <%@ include file="layout/sidebar.jsp" %>

            <main class="main">
                <h1 class="page-title">Report Detail</h1>

                <div class="review-card">
                    <div class="report-info">
                        <h2>${report.softwareName}</h2>
                        <p><strong>Customer:</strong> ${report.reporterName}</p>
                        <p><strong>Reported reason:</strong> ${report.reason}</p>
                        <p><strong>Status:</strong> ${report.status}</p>
                    </div>

                    <div class="report-actions">
                        <a class="download-btn"
                           href="${pageContext.request.contextPath}/reviewer/report-download?softwareId=${report.softwareId}">
                            Download App For Testing
                        </a>
                    </div>
                </div>

                <div class="review-form-container" style="margin-top:24px;">
                    <form method="post" action="${pageContext.request.contextPath}/reviewer_error_report_detail">
                        <input type="hidden" name="reportId" value="${report.reportId}">

                        <div class="form-group">
                            <label>Bug confirmation</label><br>
                            <label>
                                <input type="radio" name="bugConfirmed" value="true" required>
                                Confirmed bug is correct
                            </label>
                            <label style="margin-left:20px;">
                                <input type="radio" name="bugConfirmed" value="false" required>
                                Report is not valid
                            </label>
                        </div>

                        <div class="form-group">
                            <label>Reproduce steps</label>
                            <textarea name="reproduceSteps" rows="5" required
                                      placeholder="Describe steps to verify the issue..."></textarea>
                        </div>

                        <div class="form-group">
                            <label>Reviewer note</label>
                            <textarea name="reviewerNote" rows="6" required
                                      placeholder="Write your testing result..."></textarea>
                        </div>

                        <div class="review-actions">
                            <a href="${pageContext.request.contextPath}/reviewer_error_reports" class="cancel-btn">Back</a>
                            <button type="submit" class="submit-review-btn">Submit To Approval</button>
                        </div>
                    </form>
                </div>
            </main>
        </div>
    </body>
</html>