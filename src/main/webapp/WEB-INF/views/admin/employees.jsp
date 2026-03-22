<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
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
    <c:set var="activeMenu" value="employees"/>
    <jsp:include page="sidebar.jsp"/>

    <main class="main">
        <h1 style="margin-top: 0;">Employees Management</h1>
        <p class="subtitle">Manage the system employee list.</p>

        <div class="card" style="max-width: 100%;">
            <div style="display:flex; justify-content: space-between; align-items:center; gap: 16px;">
                <div>
                    <h2 style="margin:0; font-size: 18px; color: var(--dark-blue);">Employee list</h2>
                    <p style="margin:6px 0 0; color:#64748b; font-size: 13px;">Roles: reviewer / approval</p>
                </div>
                <a href="#add-employee" style="padding: 10px 14px; border-radius: 10px; background: var(--accent); color: #fff; text-decoration:none; font-weight: 600;">+ Add Employee</a>
            </div>

            <c:if test="${param.success == '1'}">
                <div class="alert success">Employee created successfully. The default password has been sent by email.</div>
            </c:if>
            <c:if test="${param.error == 'email_exists'}">
                <div class="alert danger">Email already exists in the system.</div>
            </c:if>
            <c:if test="${param.error == 'missing_fields'}">
                <div class="alert danger">Please fill in full name, email, and role.</div>
            </c:if>
            <c:if test="${param.error == 'db_error'}">
                <div class="alert danger">Unable to create employee (database error).</div>
            </c:if>

            <form method="get" action="${pageContext.request.contextPath}/admin/employees" style="margin-top:16px; display:grid; grid-template-columns: 2fr 1fr 1fr auto auto; gap:10px; align-items:end;">
                <div>
                    <label style="display:block; font-size:12px; color:#64748b; margin-bottom:6px;">Search</label>
                    <input type="text" name="keyword" value="${keyword}" placeholder="Name, email, phone..." style="width:100%; padding:10px 12px; border:1px solid #cbd5e1; border-radius:8px;" />
                </div>
                <div>
                    <label style="display:block; font-size:12px; color:#64748b; margin-bottom:6px;">Role</label>
                    <select name="role" style="width:100%; padding:10px 12px; border:1px solid #cbd5e1; border-radius:8px;">
                        <option value="">All</option>
                        <option value="reviewer" ${role == 'reviewer' ? 'selected' : ''}>reviewer</option>
                        <option value="approval" ${role == 'approval' || role == 'aproval' ? 'selected' : ''}>approval</option>
                    </select>
                </div>
                <div>
                    <label style="display:block; font-size:12px; color:#64748b; margin-bottom:6px;">Status</label>
                    <select name="status" style="width:100%; padding:10px 12px; border:1px solid #cbd5e1; border-radius:8px;">
                        <option value="">All</option>
                        <option value="ACTIVE" ${status == 'ACTIVE' ? 'selected' : ''}>ACTIVE</option>
                        <option value="INACTIVE" ${status == 'INACTIVE' ? 'selected' : ''}>INACTIVE</option>
                    </select>
                </div>
                <button type="submit" style="padding:10px 14px; border-radius:8px; border:none; background:#3b82f6; color:#fff; font-weight:600;">Filter</button>
                <a href="${pageContext.request.contextPath}/admin/employees" style="padding:10px 14px; border-radius:8px; border:1px solid #cbd5e1; color:#334155; text-decoration:none; font-weight:600; text-align:center;">Reset</a>
            </form>

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
                    <c:forEach var="e" items="${employees}">
                        <tr>
                            <td>#<c:out value="${e.userId}"/></td>
                            <td><c:out value="${e.fullName}"/></td>
                            <td><c:out value="${e.email}"/></td>
                            <td><c:out value="${e.phone}"/></td>
                            <td><c:out value="${e.roleName}"/></td>
                            <td>
                                <span class="badge ${e.status == 'ACTIVE' ? 'success' : 'danger'}" style="padding: 4px 8px; border-radius: 6px; font-size: 12px; font-weight: 600; background: ${e.status == 'ACTIVE' ? 'rgba(34, 197, 94, 0.2)' : 'rgba(239, 68, 68, 0.2)'}; color: ${e.status == 'ACTIVE' ? '#22c55e' : '#ef4444'};">
                                    <c:out value="${e.status}"/>
                                </span>
                            </td>
                            <td><fmt:formatDate value="${e.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                            <td>
                                <div style="display: flex; gap: 8px;">
                                    <a href="#edit-emp-${e.userId}" style="color: var(--accent); text-decoration: none; font-size: 13px; font-weight: 600;">Edit</a>
                                    <c:if test="${e.status == 'ACTIVE'}">
                                        <a href="#disable-emp-${e.userId}" style="color: var(--danger); text-decoration: none; font-size: 13px; font-weight: 600;" onclick="return confirm('Are you sure you want to disable this employee?');">Disable</a>
                                    </c:if>
                                    <c:if test="${e.status == 'INACTIVE'}">
                                        <a href="#enable-emp-${e.userId}" style="color: #22c55e; text-decoration: none; font-size: 13px; font-weight: 600;">Enable</a>
                                    </c:if>
                                </div>

                                <!-- Modal Edit Employee -->
                                <div id="edit-emp-${e.userId}" class="modal">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h2>Edit Employee #<c:out value="${e.userId}"/></h2>
                                            <a class="close-modal" href="${pageContext.request.contextPath}/admin/employees">×</a>
                                        </div>
                                        <form method="post" action="${pageContext.request.contextPath}/admin/employees/update">
                                            <input type="hidden" name="userId" value="${e.userId}" />
                                            <div class="field">
                                                <label>Full name</label>
                                                <input name="fullName" type="text" value="<c:out value='${e.fullName}'/>" required maxlength="100" />
                                            </div>
                                            <div class="field">
                                                <label>Phone</label>
                                                <input name="phone" type="text" value="<c:out value='${e.phone}'/>" maxlength="20" />
                                            </div>
                                            <div class="field">
                                                <label>Role</label>
                                                <select name="roleName" required style="width:100%; padding:12px; border-radius:12px; border:1px solid var(--border); background: rgba(0,0,0,0.22); color: var(--text);">
                                                    <option value="reviewer" ${e.roleName == 'reviewer' ? 'selected' : ''}>reviewer</option>
                                                    <option value="Approval" ${e.roleName == 'Approval' ? 'selected' : ''}>Approval</option>
                                                </select>
                                            </div>
                                            <div class="field">
                                                <label>Status</label>
                                                <select name="status" required style="width:100%; padding:12px; border-radius:12px; border:1px solid var(--border); background: rgba(0,0,0,0.22); color: var(--text);">
                                                    <option value="ACTIVE" ${e.status == 'ACTIVE' ? 'selected' : ''}>ACTIVE</option>
                                                    <option value="INACTIVE" ${e.status == 'INACTIVE' ? 'selected' : ''}>INACTIVE</option>
                                                </select>
                                            </div>
                                            <div class="actions">
                                                <button type="submit">Update</button>
                                                <a class="small" href="${pageContext.request.contextPath}/admin/employees">Cancel</a>
                                            </div>
                                        </form>
                                    </div>
                                </div>

                                <!-- Modal Confirm Disable -->
                                <div id="disable-emp-${e.userId}" class="modal">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h2>Lock account</h2>
                                            <a class="close-modal" href="${pageContext.request.contextPath}/admin/employees">×</a>
                                        </div>
                                        <p>Are you sure you want to lock the account of <strong><c:out value="${e.fullName}"/></strong>?</p>
                                        <form method="post" action="${pageContext.request.contextPath}/admin/employees/disable">
                                            <input type="hidden" name="userId" value="${e.userId}" />
                                            <div class="actions">
                                                <button type="submit" style="background: var(--danger);">Confirm lock</button>
                                                <a class="small" href="${pageContext.request.contextPath}/admin/employees">Cancel</a>
                                            </div>
                                        </form>
                                    </div>
                                </div>

                                <!-- Modal Confirm Enable -->
                                <div id="enable-emp-${e.userId}" class="modal">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h2>Unlock account</h2>
                                            <a class="close-modal" href="${pageContext.request.contextPath}/admin/employees">×</a>
                                        </div>
                                        <p>Are you sure you want to unlock the account of <strong><c:out value="${e.fullName}"/></strong>?</p>
                                        <form method="post" action="${pageContext.request.contextPath}/admin/employees/enable">
                                            <input type="hidden" name="userId" value="${e.userId}" />
                                            <div class="actions">
                                                <button type="submit" style="background: #22c55e;">Confirm unlock</button>
                                                <a class="small" href="${pageContext.request.contextPath}/admin/employees">Cancel</a>
                                            </div>
                                        </form>
                                    </div>
                                </div>

                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty employees}">
                        <tr>
                            <td colspan="7" style="text-align:center; color:#94a3b8; padding: 16px;">No employees found.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>

            <c:if test="${totalPages > 1}">
                <div style="display:flex; justify-content:center; align-items:center; gap:8px; margin-top:16px; flex-wrap:wrap;">
                    <c:if test="${currentPage > 1}">
                        <a href="${pageContext.request.contextPath}/admin/employees?page=${currentPage - 1}&keyword=${keyword}&role=${role}&status=${status}" style="padding:8px 12px; border:1px solid #cbd5e1; border-radius:8px; text-decoration:none; color:#334155;">Previous</a>
                    </c:if>

                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <a href="${pageContext.request.contextPath}/admin/employees?page=${i}&keyword=${keyword}&role=${role}&status=${status}"
                           style="padding:8px 12px; border-radius:8px; text-decoration:none; border:1px solid #cbd5e1; background:${i == currentPage ? '#3b82f6' : '#fff'}; color:${i == currentPage ? '#fff' : '#334155'}; font-weight:${i == currentPage ? '600' : '500'};">
                                ${i}
                        </a>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <a href="${pageContext.request.contextPath}/admin/employees?page=${currentPage + 1}&keyword=${keyword}&role=${role}&status=${status}" style="padding:8px 12px; border:1px solid #cbd5e1; border-radius:8px; text-decoration:none; color:#334155;">Next</a>
                    </c:if>
                </div>
            </c:if>
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

                    <div class="alert" style="margin-top: 14px;">Default password: <b>123456</b> (sent via email)</div>

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
