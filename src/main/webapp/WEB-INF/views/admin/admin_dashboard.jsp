<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

<div class="layout">

    <div class="sidebar">
        <h2>Admin</h2>
        <div class="menu-item active">Dashboard</div>
        <div class="menu-item">Employees</div>
        <div class="menu-item">Products</div>
    </div>

    <div class="main">

        <!-- KPI -->
        <div class="kpi-grid">
            <div class="kpi-card">
                <div class="kpi-title">Revenue</div>
                <div class="kpi-value">${totalRevenue} đ</div>
            </div>
            <div class="kpi-card">
                <div class="kpi-title">Downloads</div>
                <div class="kpi-value">${totalDownloads}</div>
            </div>
            <div class="kpi-card">
                <div class="kpi-title">Products</div>
                <div class="kpi-value">${totalProducts}</div>
            </div>
            <div class="kpi-card">
                <div class="kpi-title">Employees</div>
                <div class="kpi-value">${totalEmployees}</div>
            </div>
        </div>

        <!-- Revenue Bar Chart CSS -->
        <div class="chart-large">
            <h3>Revenue by Month</h3>

            <div class="bar-chart">
                <c:forEach var="r" items="${revenueByMonth}" varStatus="loop">
                    <div class="bar-item">
                        <div class="bar" style="height: <c:out value='${r / 1000000}'/>px;"></div>
                        <span><c:out value="${months[loop.index]}"/></span>
                    </div>
                </c:forEach>
            </div>
        </div>

        <!-- Top Downloads Progress -->
        <div class="chart-large">
            <h3>Top Apps</h3>

            <c:forEach var="app" items="${topApps}" varStatus="loop">
                <div class="progress-group">
                    <div class="progress-title">
                        ${app}
                        (${topDownloads[loop.index]}%)
                    </div>
                    <div class="progress">
                        <div class="progress-bar" style="width: <c:out value='${topDownloads[loop.index]}'/>%;"></div>
                    </div>
                </div>
            </c:forEach>
        </div>

    </div>
</div>

</body>
</html>
