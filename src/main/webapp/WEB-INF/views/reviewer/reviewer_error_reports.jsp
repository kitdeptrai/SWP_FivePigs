<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="activeMenu" value="errorReports" />

<!DOCTYPE html>
<html>
<head>
    <title>Error Reports</title>

    <!-- CSS chung cho toàn bộ reviewer -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/reviewer/reviewer.css">

    <!-- CSS riêng cho màn error -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/reviewer/error.css">
</head>
<body>
<div class="layout">
    <%@ include file="layout/sidebar.jsp" %>

    <main class="main">
        <h1 class="page-title">Software Error Reports</h1>
        <p class="subtitle">Reports from customers for software you reviewed</p>

        <c:if test="${empty reportList}">
            <div class="empty-state">No report is waiting for your verification.</div>
        </c:if>

        <c:forEach var="r" items="${reportList}">
            <div class="software-card">
                <h3>${r.softwareName}</h3>
                <p><strong>Reporter:</strong> ${r.reporterName}</p>
                <p><strong>Reason:</strong> ${r.reason}</p>
                <p><strong>Status:</strong> ${r.status}</p>

                <a href="${pageContext.request.contextPath}/reviewer_error_report_detail?reportId=${r.reportId}">
                    View Detail
                </a>
            </div>
        </c:forEach>
    </main>
</div>
</body>
</html>