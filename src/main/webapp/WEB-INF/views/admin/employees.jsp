<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin - Employees</title>
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

        .card {
            background: var(--card-bg);
            padding: 24px;
            border-radius: 12px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            border: 1px solid #e2e8f0;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 16px;
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
            <a href="${pageContext.request.contextPath}/admin/notifications" class="menu-item">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path><path d="M13.73 21a2 2 0 0 1-3.46 0"></path></svg>
                <span>Notifications</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/employees" class="menu-item active">
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
        <h1 style="margin-top: 0;">Employees Management</h1>
        <p class="subtitle">Quản lý danh sách nhân viên hệ thống.</p>

        <div class="card" style="max-width: 100%;">
            <div style="display:flex; justify-content: space-between; align-items:center; gap: 16px;">
                <div>
                    <h2 style="margin:0; font-size: 18px; color: var(--dark-blue);">Danh sách nhân viên</h2>
                    <p style="margin:6px 0 0; color:#64748b; font-size: 13px;">Role: reviewer / aproval</p>
                </div>
                <a href="#add-employee" style="padding: 10px 14px; border-radius: 10px; background: var(--accent); color: #fff; text-decoration:none; font-weight: 600;">+ Add Employee</a>
            </div>

            <c:if test="${param.success == '1'}">
                <div class="alert success">Tạo nhân viên thành công. Mật khẩu mặc định đã được gửi qua email.</div>
            </c:if>
            <c:if test="${param.error == 'email_exists'}">
                <div class="alert danger">Email đã tồn tại trong hệ thống.</div>
            </c:if>
            <c:if test="${param.error == 'missing_fields'}">
                <div class="alert danger">Vui lòng nhập đầy đủ Họ tên, Email và Role.</div>
            </c:if>
            <c:if test="${param.error == 'db_error'}">
                <div class="alert danger">Không thể tạo nhân viên (lỗi database).</div>
            </c:if>

            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Full Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Role</th>
                        <th>Status</th>
                        <th>Created At</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="e" items="${employees}">
                        <tr>
                            <td>#<c:out value="${e.userId}"/></td>
                            <td><c:out value="${e.fullName}"/></td>
                            <td><c:out value="${e.email}"/></td>
                            <td><c:out value="${e.phone}"/></td>
                            <td><c:out value="${e.roleName}"/></td>
                            <td><c:out value="${e.status}"/></td>
                            <td><c:out value="${e.createdAt}"/></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty employees}">
                        <tr>
                            <td colspan="7" style="text-align:center; color:#94a3b8; padding: 16px;">Chưa có nhân viên.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <!-- Modal Add Employee (CSS-only :target) -->
        <div id="add-employee" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h2>Add Employee</h2>
                    <a class="close-modal" href="${pageContext.request.contextPath}/admin/employees">×</a>
                </div>

                <form method="post" action="${pageContext.request.contextPath}/admin/employees/create">
                    <div class="field">
                        <label>Full name</label>
                        <input name="fullName" type="text" required maxlength="100" />
                    </div>
                    <div class="field">
                        <label>Email</label>
                        <input name="email" type="email" required maxlength="100" />
                    </div>
                    <div class="field">
                        <label>Phone (optional)</label>
                        <input name="phone" type="text" maxlength="20" />
                    </div>
                    <div class="field">
                        <label>Role</label>
                        <select name="roleName" required style="width:100%; padding:12px; border-radius:12px; border:1px solid var(--border); background: rgba(0,0,0,0.22); color: var(--text);">
                            <option value="reviewer">reviewer</option>
                            <option value="Approval">Approval</option>
                        </select>
                    </div>

                    <div class="alert" style="margin-top: 14px;">Mật khẩu mặc định: <b>123456</b> (sẽ gửi qua email)</div>

                    <div class="actions">
                        <button type="submit">Create</button>
                        <a class="small" href="${pageContext.request.contextPath}/admin/employees">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </main>
</div>
</body>
</html>
