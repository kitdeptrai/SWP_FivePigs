<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

<div class="layout">

    <div class="sidebar">
        <h2 style="margin-bottom: 24px; padding: 0 12px;">FivePigs</h2>
        <div class="menu-item active">Dashboard</div>
        <a href="#" class="menu-item" style="display: flex; align-items: center; gap: 8px;">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path><path d="M13.73 21a2 2 0 0 1-3.46 0"></path></svg>
            Notifications
        </a>
        <a href="#" class="menu-item" style="display: block;">Employees</a>
        <a href="#" class="menu-item" style="display: block;">User</a>
        <a href="#" class="menu-item" style="display: block;">Products</a>
        <a href="#" class="menu-item" style="display: block;">Orders</a>
        <div style="margin-top: auto; padding-top: 24px;">
            <div class="small" style="padding: 0 12px; margin-bottom: 8px;">Admin: <c:out value="${user.fullName}"/></div>
            <a href="${pageContext.request.contextPath}/logout" class="menu-item" style="display: block; color: var(--danger); font-weight: 600;">Logout</a>
        </div>
    </div>

    <div class="main">

        <!-- KPI -->
        <div class="kpi-grid">
            <div class="kpi-card">
                <div class="kpi-title">Revenue</div>
                <div class="kpi-value">
                    <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                </div>
            </div>
            <div class="kpi-card">
                <div class="kpi-title">Downloads</div>
                <div class="kpi-value">
                    <fmt:formatNumber value="${totalDownloads}" type="number"/>
                </div>
            </div>
            <div class="kpi-card">
                <div class="kpi-title">Products</div>
                <div class="kpi-value"><c:out value="${totalProducts}"/></div>
            </div>
            <div class="kpi-card">
                <div class="kpi-title">Employees</div>
                <div class="kpi-value"><c:out value="${totalEmployees}"/></div>
            </div>
        </div>

        <!-- Revenue Bar Chart -->
        <div class="card" style="max-width: 100%; margin-bottom: 24px;">
            <h3 style="margin-top: 0;">Revenue by Month (6 Months)</h3>
            <c:choose>
                <c:when test="${empty revenueByMonth}">
                    <p class="muted">No data available.</p>
                </c:when>
                <c:otherwise>
                    <div class="bar-chart">
                        <c:forEach var="r" items="${revenueByMonth}" varStatus="loop">
                            <div class="bar-item">
                                <c:set var="barHeight" value="${r / 1000000}"/>
                                <c:set var="h" value="${barHeight > 150 ? 150 : (barHeight < 5 ? 5 : barHeight)}"/>
                                <div class="bar" style="--h: <c:out value='${h}'/>px;" 
                                     title="<fmt:formatNumber value='${r}' type='number'/> ₫">
                                </div>
                                <span><c:out value="${months[loop.index]}"/></span>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Top Downloads Progress -->
        <div class="card" style="max-width: 100%;">
            <h3 style="margin-top: 0;">Top Performing Apps</h3>
            <c:choose>
                <c:when test="${empty topAppsDownloads}">
                    <p class="muted">No download data available.</p>
                </c:when>
                <c:otherwise>
                    <c:forEach var="appData" items="${topAppsDownloads}" varStatus="loop">
                        <div class="progress-group">
                            <div class="progress-title" style="display: flex; justify-content: space-between; margin-bottom: 6px;">
                                <span><c:out value="${appData.name}"/></span>
                                <span class="small"><fmt:formatNumber value="${appData.downloads}" type="number"/> downloads</span>
                            </div>
                            <div class="progress">
                                <c:set var="pct" value="${topDownloads[loop.index]}"/>
                                <div class="progress-bar" style="--w: <c:out value='${pct}'/>%;"></div>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

    </div>
</div>

</body>
</html>
