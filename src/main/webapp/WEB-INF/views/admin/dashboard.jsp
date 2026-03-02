<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - FivePigs</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <style>
        /* UI màu: Xám – Trắng – Dark Blue */
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
    <aside class="sidebar">
        <h2>FivePigs Admin</h2>
        <nav>
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="menu-item active">
                <span>Dashboard</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/notifications" class="menu-item">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path><path d="M13.73 21a2 2 0 0 1-3.46 0"></path></svg>
                <span>Notifications</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/employees" class="menu-item">
                <span>Employees</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/users" class="menu-item">
                <span>Users</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/products" class="menu-item">
                <span>Products</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/orders" class="menu-item">
                <span>Orders</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/reports" class="menu-item">
                <span>Reports</span>
            </a>
        </nav>
        
        <div style="margin-top: auto; padding: 12px;">
            <p style="font-size: 12px; color: #64748b; margin-bottom: 8px;">Admin: <c:out value="${user.fullName}"/></p>
            <a href="${pageContext.request.contextPath}/logout" class="menu-item logout-btn">Logout</a>
        </div>
    </aside>

    <!-- Main Content -->
    <main class="main">
        <h1>Dashboard Overview</h1>

        <!-- 1️⃣ System Overview Cards -->
        <div class="kpi-grid">
            <div class="kpi-card">
                <div class="kpi-title">Tổng doanh thu (PAID)</div>
                <div class="kpi-value">
                    <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                </div>
            </div>
            <div class="kpi-card">
                <div class="kpi-title">Tổng số ứng dụng</div>
                <div class="kpi-value"><c:out value="${totalApps}"/></div>
            </div>
            <div class="kpi-card">
                <div class="kpi-title">Tổng số User</div>
                <div class="kpi-value"><c:out value="${totalUsers}"/></div>
            </div>
            <div class="kpi-card">
                <div class="kpi-title">Report chưa xử lý</div>
                <div class="kpi-value" style="display: flex; justify-content: space-between; align-items: center;">
                    <c:out value="${pendingReports}"/>
                    <a href="${pageContext.request.contextPath}/admin/reports" style="font-size: 12px; color: var(--accent); text-decoration: none;">Chi tiết →</a>
                </div>
            </div>
        </div>

        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 24px;">
            <!-- 2️⃣ Doanh thu theo tháng -->
            <div class="section-card">
                <h3>Doanh thu theo tháng</h3>
                <table>
                    <thead>
                        <tr>
                            <th>Tháng</th>
                            <th class="text-right">Doanh thu</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="row" items="${revenueByMonth}">
                            <tr>
                                <td>Tháng <c:out value="${row.month}"/></td>
                                <td class="text-right">
                                    <fmt:formatNumber value="${row.revenue}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty revenueByMonth}">
                            <tr><td colspan="2" style="text-align: center; color: #94a3b8;">Chưa có dữ liệu</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <!-- 3️⃣ Top 5 app bán chạy -->
            <div class="section-card">
                <h3>Top 5 ứng dụng bán chạy</h3>
                <table>
                    <thead>
                        <tr>
                            <th>Tên App</th>
                            <th class="text-right">Lượt mua</th>
                            <th class="text-right">Doanh thu</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="app" items="${topAppsBestSeller}">
                            <tr>
                                <td style="font-weight: 500;"><c:out value="${app.appName}"/></td>
                                <td class="text-right"><c:out value="${app.purchaseCount}"/></td>
                                <td class="text-right">
                                    <fmt:formatNumber value="${app.totalRevenue}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty topAppsBestSeller}">
                            <tr><td colspan="3" style="text-align: center; color: #94a3b8;">Chưa có dữ liệu</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>

    </main>
</div>

</body>
</html>
