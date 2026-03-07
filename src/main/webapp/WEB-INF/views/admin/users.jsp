<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin - Users</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <style>
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
            vertical-align: top;
        }
    </style>
</head>
<body>

<div class="layout">
    <c:set var="activeMenu" value="users"/>
    <jsp:include page="sidebar.jsp"/>

    <main class="main">
        <h1 style="margin-top: 0;">Users Management</h1>
        <p class="subtitle">Quản lý danh sách user (Customer / Vendor).</p>

        <div class="card" style="max-width: 100%;">
            <div style="display:flex; justify-content: space-between; align-items:center; gap: 16px;">
                <div>
                    <h2 style="margin:0; font-size: 18px; color: var(--dark-blue);">Danh sách Users</h2>
                    <p style="margin:6px 0 0; color:#64748b; font-size: 13px;">Role: Customer / Vendor</p>
                </div>
                <a href="#add-user" style="padding: 10px 14px; border-radius: 10px; background: var(--accent); color: #fff; text-decoration:none; font-weight: 600;">+ Add User</a>
            </div>

            <c:if test="${param.success == '1'}">
                <div class="alert success">Tạo user thành công. Mật khẩu mặc định đã được gửi qua email.</div>
            </c:if>
            <c:if test="${param.success == 'updated'}">
                <div class="alert success">Cập nhật user thành công.</div>
            </c:if>
            <c:if test="${param.success == 'disabled'}">
                <div class="alert success">Khóa tài khoản user thành công.</div>
            </c:if>

            <c:if test="${param.error == 'email_exists'}">
                <div class="alert danger">Email đã tồn tại trong hệ thống.</div>
            </c:if>
            <c:if test="${param.error == 'missing_fields'}">
                <div class="alert danger">Vui lòng nhập đầy đủ Họ tên, Email và Role.</div>
            </c:if>
            <c:if test="${param.error == 'invalid_id'}">
                <div class="alert danger">User ID không hợp lệ.</div>
            </c:if>
            <c:if test="${param.error == 'invalid_role'}">
                <div class="alert danger">Role không hợp lệ (chỉ Customer/Vendor).</div>
            </c:if>
            <c:if test="${param.error == 'invalid_status'}">
                <div class="alert danger">Status không hợp lệ (ACTIVE/INACTIVE).</div>
            </c:if>
            <c:if test="${param.error == 'db_error'}">
                <div class="alert danger">Có lỗi database. Vui lòng thử lại.</div>
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
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="u" items="${users}">
                    <tr>
                        <td>#<c:out value="${u.userId}"/></td>
                        <td><c:out value="${u.fullName}"/></td>
                        <td><c:out value="${u.email}"/></td>
                        <td><c:out value="${u.phone}"/></td>
                        <td><c:out value="${u.roleName}"/></td>
                        <td>
                            <span style="padding: 4px 8px; border-radius: 6px; font-size: 12px; font-weight: 600; background: ${u.status == 'ACTIVE' ? 'rgba(34, 197, 94, 0.2)' : 'rgba(239, 68, 68, 0.2)'}; color: ${u.status == 'ACTIVE' ? '#22c55e' : '#ef4444'};">
                                <c:out value="${u.status}"/>
                            </span>
                        </td>
                        <td><fmt:formatDate value="${u.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                        <td>
                                <div style="display:flex; gap: 8px;">
                                    <a href="#edit-user-${u.userId}" style="color: var(--accent); text-decoration: none; font-size: 13px; font-weight: 600;">Edit</a>
                                    <c:if test="${u.status == 'ACTIVE'}">
                                        <a href="#disable-user-${u.userId}" style="color: var(--danger); text-decoration: none; font-size: 13px; font-weight: 600;">Disable</a>
                                    </c:if>
                                    <c:if test="${u.status == 'INACTIVE'}">
                                        <a href="#enable-user-${u.userId}" style="color: #22c55e; text-decoration: none; font-size: 13px; font-weight: 600;">Enable</a>
                                    </c:if>
                                </div>

                            <!-- Modal Edit User -->
                            <div id="edit-user-${u.userId}" class="modal">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h2>Edit User #<c:out value="${u.userId}"/></h2>
                                        <a class="close-modal" href="${pageContext.request.contextPath}/admin/users">×</a>
                                    </div>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/users/update">
                                        <input type="hidden" name="userId" value="${u.userId}" />
                                        <div class="field">
                                            <label>Full name</label>
                                            <input name="fullName" type="text" value="<c:out value='${u.fullName}'/>" required maxlength="100" />
                                        </div>
                                        <div class="field">
                                            <label>Phone</label>
                                            <input name="phone" type="text" value="<c:out value='${u.phone}'/>" maxlength="20" />
                                        </div>
                                        <div class="field">
                                            <label>Role</label>
                                            <select name="roleName" required style="width:100%; padding:12px; border-radius:12px; border:1px solid var(--border); background: rgba(0,0,0,0.22); color: var(--text);">
                                                <option value="Customer" ${u.roleName == 'Customer' ? 'selected' : ''}>Customer</option>
                                                <option value="Vendor" ${u.roleName == 'Vendor' ? 'selected' : ''}>Vendor</option>
                                            </select>
                                        </div>
                                        <div class="field">
                                            <label>Status</label>
                                            <select name="status" required style="width:100%; padding:12px; border-radius:12px; border:1px solid var(--border); background: rgba(0,0,0,0.22); color: var(--text);">
                                                <option value="ACTIVE" ${u.status == 'ACTIVE' ? 'selected' : ''}>ACTIVE</option>
                                                <option value="INACTIVE" ${u.status == 'INACTIVE' ? 'selected' : ''}>INACTIVE</option>
                                            </select>
                                        </div>
                                        <div class="actions">
                                            <button type="submit">Update</button>
                                            <a class="small" href="${pageContext.request.contextPath}/admin/users">Cancel</a>
                                        </div>
                                    </form>
                                </div>
                            </div>

                            <!-- Modal Confirm Disable -->
                            <div id="disable-user-${u.userId}" class="modal">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h2>Khóa tài khoản</h2>
                                        <a class="close-modal" href="${pageContext.request.contextPath}/admin/users">×</a>
                                    </div>
                                    <p>Bạn có chắc chắn muốn khóa tài khoản của <strong><c:out value="${u.fullName}"/></strong>?</p>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/users/disable">
                                        <input type="hidden" name="userId" value="${u.userId}" />
                                        <div class="actions">
                                            <button type="submit" style="background: var(--danger);">Xác nhận khóa</button>
                                            <a class="small" href="${pageContext.request.contextPath}/admin/users">Hủy</a>
                                        </div>
                                    </form>
                                </div>
                            </div>

                            <!-- Modal Confirm Enable -->
                            <div id="enable-user-${u.userId}" class="modal">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h2>Mở khóa tài khoản</h2>
                                        <a class="close-modal" href="${pageContext.request.contextPath}/admin/users">×</a>
                                    </div>
                                    <p>Bạn có chắc chắn muốn mở khóa tài khoản của <strong><c:out value="${u.fullName}"/></strong>?</p>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/users/enable">
                                        <input type="hidden" name="userId" value="${u.userId}" />
                                        <div class="actions">
                                            <button type="submit" style="background: #22c55e;">Xác nhận mở khóa</button>
                                            <a class="small" href="${pageContext.request.contextPath}/admin/users">Hủy</a>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty users}">
                    <tr>
                        <td colspan="8" style="text-align:center; color:#94a3b8; padding: 16px;">Chưa có user.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>

        <!-- Modal Add User (CSS-only :target) -->
        <div id="add-user" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h2>Add User</h2>
                    <a class="close-modal" href="${pageContext.request.contextPath}/admin/users">×</a>
                </div>

                <form method="post" action="${pageContext.request.contextPath}/admin/users/create">
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
                            <option value="Customer">Customer</option>
                            <option value="Vendor">Vendor</option>
                        </select>
                    </div>

                    <div class="alert" style="margin-top: 14px;">Mật khẩu mặc định: <b>123456</b> (sẽ gửi qua email)</div>

                    <div class="actions">
                        <button type="submit">Create</button>
                        <a class="small" href="${pageContext.request.contextPath}/admin/users">Cancel</a>
                    </div>
                </form>
            </div>
        </div>

    </main>
</div>

</body>
</html>
