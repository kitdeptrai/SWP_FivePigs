<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin - Notifications</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <style>
        /* Same sidebar style as admin dashboard */
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

        .main {
            padding: 32px;
            overflow-y: auto;
        }

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
    <aside class="sidebar">
        <h2>FivePigs Admin</h2>
        <nav>
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="menu-item">
                <span>Dashboard</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/notifications" class="menu-item active">
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

    <main class="main">
        <h1 style="margin-top: 0;">Notifications</h1>
        <p class="subtitle">Danh sách thông báo hệ thống (demo).</p>

        <div class="card" style="max-width: 100%;">
            <div class="alert">Chưa có dữ liệu thật. Bạn có thể nối DB sau.</div>
            <ul style="margin: 12px 0 0; padding-left: 18px; color: var(--muted);">
                <li>New user registered</li>
                <li>New software submitted for approval</li>
                <li>Payment completed</li>
            </ul>
        </div>
    </main>
</div>

</body>
</html>
