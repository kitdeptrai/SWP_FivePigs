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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <style>
        :root {
            --dark-blue: #1e293b;
            --sidebar-bg: #0f172a;
            --card-bg: #ffffff;
            --text-main: #334155;
            --bg-gray: #f5f7fb;
            --accent: #3b82f6;
        }

        * {
            box-sizing: border-box;
            border-radius: 12px;
        }

        body {
            background: #f5f7fb;
            color: var(--text-main);
            margin: 0;
            font-family: 'Inter', sans-serif;
        }

        .layout {
            display: grid;
            grid-template-columns: 260px 1fr;
            min-height: 100vh;
        }

        .sidebar {
            background-color: var(--sidebar-bg);
            color: white;
            padding: 24px 16px;
            display: flex;
            flex-direction: column;
            border-radius: 0;
        }

        .main {
            padding: 32px;
            overflow-y: auto;
        }

        .main h1 {
            margin: 0 0 24px;
            font-size: 28px;
            color: var(--dark-blue);
        }

        .kpi-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
            gap: 24px;
            margin-bottom: 32px;
        }

        .kpi-card,
        .section-card {
            background: white;
            border-radius: 16px;
            padding: 20px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.08);
            transition: all 0.3s ease;
            border: 1px solid #e5e7eb;
            margin-bottom: 24px;
        }

        .kpi-card:hover,
        .section-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 12px 30px rgba(0,0,0,0.12);
        }

        .kpi-title {
            font-size: 14px;
            color: #64748b;
            font-weight: 600;
            text-transform: uppercase;
            margin-bottom: 8px;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .kpi-value {
            font-size: 28px;
            font-weight: bold;
            color: #111;
        }

        .kpi-sub { color: #888; }
        .revenue { color: #22c55e; }
        .users { color: #3b82f6; }
        .apps { color: #f59e0b; }
        .downloads { color: #ef4444; }

        .bar {
            height: 8px;
            background: #eee;
            border-radius: 10px;
            overflow: hidden;
        }

        .bar-fill,
        .progress-bar {
            height: 100%;
            background: linear-gradient(90deg, #4facfe, #00f2fe);
            border-radius: 10px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th {
            text-align: left;
            color: #666;
            font-weight: 600;
            padding: 10px;
            background: transparent;
        }

        td {
            padding: 10px;
            border-top: 1px solid #eee;
        }

        tr:hover {
            background: #f9fafb;
        }

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

        <div class="section-card" style="margin-bottom:16px;">
            <h3 style="margin-top:0; margin-bottom:12px;">System Commission</h3>
            <form method="post" action="${pageContext.request.contextPath}/admin/dashboard" style="display:flex; gap:10px; align-items:end; flex-wrap:wrap;">
                <div>
                    <label style="display:block; font-size:12px; color:#64748b; margin-bottom:6px;">Commission percent (max 20%)</label>
                    <input type="number" step="0.01" min="0" max="20" name="commissionPercent" value="${commissionPercent}" style="padding:10px 12px; border:1px solid #cbd5e1; border-radius:8px; min-width:220px;"/>
                </div>
                <button type="submit" style="padding:10px 14px; border:none; border-radius:8px; background:#3b82f6; color:#fff; font-weight:600; cursor:pointer;">Save</button>
                <div style="font-size:13px; color:#64748b;">Current: <strong><fmt:formatNumber value="${commissionPercent}" minFractionDigits="0" maxFractionDigits="2"/>%</strong></div>
            </form>

            <c:if test="${param.success == 'commission_updated'}">
                <div style="margin-top:10px; padding:10px 12px; border-radius:8px; background:#ecfdf3; color:#15803d; border:1px solid #bbf7d0; font-size:13px;">Commission updated successfully.</div>
            </c:if>
            <c:if test="${param.error == 'missing_commission' || param.error == 'invalid_commission' || param.error == 'commission_out_of_range' || param.error == 'db_error'}">
                <div style="margin-top:10px; padding:10px 12px; border-radius:8px; background:#fef2f2; color:#b91c1c; border:1px solid #fecaca; font-size:13px;">Cannot update commission. Value must be from 0 to 20.</div>
            </c:if>
        </div>

        <c:if test="${pendingReports > 0}">
            <div class="alert">
                ⚠️ You have <b>${pendingReports}</b> pending reports
            </div>
        </c:if>

        <div class="kpi-grid">
            <div class="kpi-card">
                <div class="kpi-title"><i class="fa-solid fa-dollar-sign"></i> Total revenue</div>
                <div class="kpi-value revenue">
                    <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                </div>
                <div class="kpi-trend up">↑ this month</div>
            </div>

            <div class="kpi-card">
                <div class="kpi-title"><i class="fa-solid fa-users"></i> Users</div>
                <div class="kpi-value users"><c:out value="${totalUsers}"/></div>
                <div class="kpi-sub">+<c:out value="${newUsers}"/> today</div>
            </div>

            <div class="kpi-card">
                <div class="kpi-title"><i class="fa-solid fa-box"></i> Apps</div>
                <div class="kpi-value apps"><c:out value="${totalApps}"/></div>
            </div>

            <div class="kpi-card">
                <div class="kpi-title"><i class="fa-solid fa-download"></i> Downloads</div>
                <div class="kpi-value downloads"><c:out value="${totalDownloads}"/></div>
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
