<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="en_US" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - FivePigs</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <style>
        /* UI colors: Gray – White – Dark Blue */
        :root {
            --dark-blue: #1e293b;
            --sidebar-bg: #0f172a;
            --card-bg: #ffffff;
            --text-main: #334155;
            --bg-gray: #f1f5f9;
            --accent: #3b82f6;
        }

        body {
            background-color: var(--bg-gray);
            color: var(--text-main);
            margin: 0;
            font-family: 'Inter', system-ui, -apple-system, sans-serif;
        }

        .layout {
            display: grid;
            grid-template-columns: 260px 1fr;
            min-height: 100vh;
        }

        /* Sidebar */
        .sidebar {
            background-color: var(--sidebar-bg);
            color: white;
            padding: 24px 16px;
            display: flex;
            flex-direction: column;
        }

        .sidebar h2 {
            font-size: 24px;
            margin-bottom: 32px;
            color: #fff;
            padding: 0 12px;
        }

        .menu-item {
            padding: 12px 16px;
            border-radius: 8px;
            color: #94a3b8;
            text-decoration: none;
            margin-bottom: 4px;
            display: flex;
            align-items: center;
            gap: 12px;
            transition: all 0.2s;
        }

        .menu-item:hover {
            background-color: rgba(255, 255, 255, 0.1);
            color: white;
        }

        .menu-item.active {
            background-color: var(--accent);
            color: white;
        }

        /* Main Content */
        .main {
            padding: 32px;
            overflow-y: auto;
        }

        .main h1 {
            margin: 0 0 24px;
            font-size: 28px;
            color: var(--dark-blue);
        }

        /* KPI Grid */
        .kpi-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
            gap: 24px;
            margin-bottom: 32px;
        }

        .kpi-card {
            background: var(--card-bg);
            padding: 24px;
            border-radius: 12px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            border: 1px solid #e2e8f0;
        }

        .kpi-title {
            font-size: 14px;
            color: #64748b;
            font-weight: 600;
            text-transform: uppercase;
            margin-bottom: 8px;
        }

        .kpi-value {
            font-size: 24px;
            font-weight: 700;
            color: var(--dark-blue);
        }

        /* Sections */
        .section-card {
            background: var(--card-bg);
            padding: 24px;
            border-radius: 12px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            border: 1px solid #e2e8f0;
            margin-bottom: 32px;
        }

        .section-card h3 {
            margin: 0 0 20px;
            font-size: 18px;
            color: var(--dark-blue);
            border-bottom: 2px solid var(--bg-gray);
            padding-bottom: 12px;
        }

        /* Table Style */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 8px;
        }

        th {
            text-align: left;
            padding: 12px;
            background-color: var(--bg-gray);
            color: #475569;
            font-weight: 600;
            font-size: 14px;
        }

        td {
            padding: 12px;
            border-bottom: 1px solid #e2e8f0;
            font-size: 14px;
        }

        tr:last-child td {
            border-bottom: none;
        }

        /* Utilities */
        .text-right { text-align: right; }
        
        .logout-btn {
            margin-top: auto;
            color: #ef4444;
            font-weight: 600;
        }
        .logout-btn:hover {
            background-color: rgba(239, 68, 68, 0.1);
        }
    </style>
</head>
<body>

<div class="layout">
    <!-- Sidebar -->
    <c:set var="activeMenu" value="dashboard"/>
    <jsp:include page="sidebar.jsp"/>

    <!-- Main Content -->
    <main class="main">
        <h1>Dashboard Overview</h1>

        <c:if test="${pendingReports > 0}">
            <div class="alert">
                ⚠️ You have <b>${pendingReports}</b> pending reports
            </div>
        </c:if>

        <div class="kpi-grid">
            <div class="kpi-card">
                <div class="kpi-title">Total revenue</div>
                <div class="kpi-value">
                    <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                </div>
                <div class="kpi-trend up">↑ this month</div>
            </div>

            <div class="kpi-card">
                <div class="kpi-title">Users</div>
                <div class="kpi-value"><c:out value="${totalUsers}"/></div>
                <div class="kpi-sub">+<c:out value="${newUsers}"/> today</div>
            </div>

            <div class="kpi-card">
                <div class="kpi-title">Apps</div>
                <div class="kpi-value"><c:out value="${totalApps}"/></div>
            </div>

            <div class="kpi-card">
                <div class="kpi-title">Downloads</div>
                <div class="kpi-value"><c:out value="${totalDownloads}"/></div>
            </div>
        </div>

        <div class="grid-2">
            <div>
                <div class="section-card">
                    <h3>Monthly revenue</h3>

                    <c:forEach var="row" items="${revenueByMonth}">
                        <div class="bar-row">
                            <span>Month ${row.month}</span>

                            <div class="bar">
                                <div class="bar-fill" style="width: ${row.percent}%"></div>
                            </div>

                            <span class="text-right">
                                <fmt:formatNumber value="${row.revenue}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                            </span>
                        </div>
                    </c:forEach>
                    <c:if test="${empty revenueByMonth}">
                        <div class="empty">No data available</div>
                    </c:if>
                </div>
            </div>

            <div>
                <div class="section-card">
                    <h3>Top 5 Apps</h3>

                    <table>
                        <thead>
                        <tr>
                            <th>App</th>
                            <th class="text-right">Sales</th>
                            <th class="text-right">Revenue</th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach var="app" items="${topAppsBestSeller}">
                            <tr>
                                <td><b><c:out value="${app.appName}"/></b></td>
                                <td class="text-right"><c:out value="${app.purchaseCount}"/></td>
                                <td class="text-right">
                                    <fmt:formatNumber value="${app.totalRevenue}" type="currency" currencySymbol="$"/>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty topAppsBestSeller}">
                            <tr><td colspan="3" class="empty">No data available</td></tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>

                <div class="section-card">
                    <h3>Recent Activities</h3>

                    <c:forEach var="log" items="${recentActivities}">
                        <div class="activity-item">
                            <div class="dot"></div>

                            <div>
                                <b><c:out value="${log.user}"/></b> <c:out value="${log.action}"/>
                                <div class="time"><fmt:formatDate value="${log.time}" pattern="dd/MM/yyyy HH:mm"/></div>
                            </div>
                        </div>
                    </c:forEach>

                    <c:if test="${empty recentActivities}">
                        <div class="empty">No recent activity</div>
                    </c:if>
                </div>
            </div>
        </div>

    </main>
</div>

</body>
</html>
