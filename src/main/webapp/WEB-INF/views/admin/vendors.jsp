<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin - Vendors</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <style>
        :root {
            --dark-blue: #1e293b;
            --sidebar-bg: #0f172a;
            --card-bg: #ffffff;
            --text-main: #334155;
            --bg-gray: #f5f7fb;
            --accent: #3b82f6;
        }

        body {
            background-color: var(--bg-gray);
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
            background: white;
            border-radius: 16px;
            padding: 20px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.08);
            transition: all 0.3s ease;
            border: 1px solid #e5e7eb;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 12px 30px rgba(0,0,0,0.12);
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
    <c:set var="activeMenu" value="vendors"/>
    <jsp:include page="sidebar.jsp"/>

    <main class="main">
        <h1 style="margin-top: 0;">Vendors Management</h1>
        <p class="subtitle">Manage the vendor list.</p>

        <div class="card" style="max-width: 100%;">
            <div style="display:flex; justify-content: space-between; align-items:center; gap: 16px;">
                <div>
                    <h2 style="margin:0; font-size: 18px; color: var(--dark-blue);">Vendor list</h2>
                    <p style="margin:6px 0 0; color:#64748b; font-size: 13px;">Role: Vendor</p>
                </div>
            </div>

            <c:if test="${param.success == 'updated'}">
                <div class="alert success">Vendor updated successfully.</div>
            </c:if>
            <c:if test="${param.success == 'disabled'}">
                <div class="alert success">Vendor account locked successfully.</div>
            </c:if>

            <c:if test="${param.error == 'email_exists'}">
                <div class="alert danger">Email already exists in the system.</div>
            </c:if>
            <c:if test="${param.error == 'missing_fields'}">
                <div class="alert danger">Please fill in full name, email, and role.</div>
            </c:if>
            <c:if test="${param.error == 'invalid_id'}">
                <div class="alert danger">Invalid user ID.</div>
            </c:if>
            <c:if test="${param.error == 'invalid_role'}">
                <div class="alert danger">Invalid role (vendor only).</div>
            </c:if>
            <c:if test="${param.error == 'invalid_status'}">
                <div class="alert danger">Invalid status (ACTIVE/INACTIVE).</div>
            </c:if>
            <c:if test="${param.error == 'db_error'}">
                <div class="alert danger">Database error. Please try again.</div>
            </c:if>

            <form method="get" action="${pageContext.request.contextPath}/admin/vendors" style="margin-top:16px; display:grid; grid-template-columns: 2fr 1fr auto auto; gap:10px; align-items:end;">
                <div>
                    <label style="display:block; font-size:12px; color:#64748b; margin-bottom:6px;">Search</label>
                    <input type="text" name="keyword" value="${keyword}" placeholder="Name, email, phone..." style="width:100%; padding:10px 12px; border:1px solid #cbd5e1; border-radius:8px;" />
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
                <a href="${pageContext.request.contextPath}/admin/vendors" style="padding:10px 14px; border-radius:8px; border:1px solid #cbd5e1; color:#334155; text-decoration:none; font-weight:600; text-align:center;">Reset</a>
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
                <c:forEach var="u" items="${users}">
                    <tr>
                        <td>#<c:out value="${u.userId}"/></td>
                        <td><c:out value="${u.fullName}"/></td>
                        <td><c:out value="${u.email}"/></td>
                        <td><c:out value="${u.phone}"/></td>
                        <td><c:out value="${u.roleName}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${u.status == 'ACTIVE'}">
                                    <span style="padding: 4px 8px; border-radius: 6px; font-size: 12px; font-weight: 600; background: rgba(34, 197, 94, 0.2); color: #22c55e;">
                                        <c:out value="${u.status}"/>
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span style="padding: 4px 8px; border-radius: 6px; font-size: 12px; font-weight: 600; background: rgba(239, 68, 68, 0.2); color: #ef4444;">
                                        <c:out value="${u.status}"/>
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td><fmt:formatDate value="${u.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                        <td>
                            <div style="display:flex; gap: 8px;">
                                <a href="#edit-user-${u.userId}" style="color: var(--accent); text-decoration: none; font-size: 13px; font-weight: 600;">Edit</a>
                                <c:if test="${u.status == 'ACTIVE'}">
                                    <a href="#disable-user-${u.userId}" style="color: var(--danger); text-decoration: none; font-size: 13px; font-weight: 600;" onclick="return confirm('Are you sure you want to disable this vendor?');">Disable</a>
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
                                        <a class="close-modal" href="${pageContext.request.contextPath}/admin/vendors">×</a>
                                    </div>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/vendors/update">
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
                                                <option value="Vendor" selected>Vendor</option>
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
                                            <a class="small" href="${pageContext.request.contextPath}/admin/vendors">Cancel</a>
                                        </div>
                                    </form>
                                </div>
                            </div>

                            <!-- Modal Confirm Disable -->
                            <div id="disable-user-${u.userId}" class="modal">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h2>Lock account</h2>
                                        <a class="close-modal" href="${pageContext.request.contextPath}/admin/vendors">×</a>
                                    </div>
                                    <p>Are you sure you want to lock the account of <strong><c:out value="${u.fullName}"/></strong>?</p>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/vendors/disable">
                                        <input type="hidden" name="userId" value="${u.userId}" />
                                        <div class="actions">
                                            <button type="submit" style="background: var(--danger);">Confirm lock</button>
                                            <a class="small" href="${pageContext.request.contextPath}/admin/vendors">Cancel</a>
                                        </div>
                                    </form>
                                </div>
                            </div>

                            <!-- Modal Confirm Enable -->
                            <div id="enable-user-${u.userId}" class="modal">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h2>Unlock account</h2>
                                        <a class="close-modal" href="${pageContext.request.contextPath}/admin/vendors">×</a>
                                    </div>
                                    <p>Are you sure you want to unlock the account of <strong><c:out value="${u.fullName}"/></strong>?</p>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/vendors/enable">
                                        <input type="hidden" name="userId" value="${u.userId}" />
                                        <div class="actions">
                                            <button type="submit" style="background: #22c55e;">Confirm unlock</button>
                                            <a class="small" href="${pageContext.request.contextPath}/admin/vendors">Cancel</a>
                                        </div>
                                    </form>
                                </div>
                            </div>

                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty users}">
                    <tr>
                        <td colspan="8" style="text-align:center; color:#94a3b8; padding: 16px;">No users found.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>

            <c:if test="${totalPages > 1}">
                <div style="display:flex; justify-content:center; align-items:center; gap:8px; margin-top:16px; flex-wrap:wrap;">
                    <c:if test="${currentPage > 1}">
                        <a href="${pageContext.request.contextPath}/admin/vendors?page=${currentPage - 1}&keyword=${keyword}&status=${status}" style="padding:8px 12px; border:1px solid #cbd5e1; border-radius:8px; text-decoration:none; color:#334155;">Previous</a>
                    </c:if>

                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <c:choose>
                            <c:when test="${i == currentPage}">
                                <a href="${pageContext.request.contextPath}/admin/vendors?page=${i}&keyword=${keyword}&status=${status}"
                                   style="padding:8px 12px; border-radius:8px; text-decoration:none; border:1px solid #3b82f6; background:#3b82f6; color:#fff; font-weight:600;">
                                        ${i}
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/admin/vendors?page=${i}&keyword=${keyword}&status=${status}"
                                   style="padding:8px 12px; border-radius:8px; text-decoration:none; border:1px solid #cbd5e1; background:#fff; color:#334155; font-weight:500;">
                                        ${i}
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <a href="${pageContext.request.contextPath}/admin/vendors?page=${currentPage + 1}&keyword=${keyword}&status=${status}" style="padding:8px 12px; border:1px solid #cbd5e1; border-radius:8px; text-decoration:none; color:#334155;">Next</a>
                    </c:if>
                </div>
            </c:if>
        </div>

    </main>
</div>

</body>
</html>
