<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin - Products</title>
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

        .badge {
            padding: 4px 8px;
            border-radius: 6px;
            font-size: 12px;
            font-weight: 600;
            display: inline-block;
        }

        .badge.active {
            background: rgba(34, 197, 94, 0.2);
            color: #22c55e;
        }

        .badge.inactive {
            background: rgba(239, 68, 68, 0.2);
            color: #ef4444;
        }

        .badge.pending {
            background: rgba(234, 179, 8, 0.2);
            color: #ca8a04;
        }
    </style>
</head>
<body>

<div class="layout">
    <c:set var="activeMenu" value="products"/>
    <jsp:include page="sidebar.jsp"/>

    <main class="main">
        <h1 style="margin-top: 0;">Products Management</h1>
        <p class="subtitle">Manage the product list (active/inactive).</p>

        <div class="card" style="max-width: 100%;">
            <div style="display:flex; justify-content: space-between; align-items:center; gap: 16px;">
                <div>
                    <h2 style="margin:0; font-size: 18px; color: var(--dark-blue);">Product list</h2>
                    <p style="margin:6px 0 0; color:#64748b; font-size: 13px;">All products in the system</p>
                </div>
            </div>

            <form method="get" action="${pageContext.request.contextPath}/admin/products" style="margin-top:16px; display:grid; grid-template-columns: 2fr 1fr auto auto; gap:10px; align-items:end;">
                <div>
                    <label style="display:block; font-size:12px; color:#64748b; margin-bottom:6px;">Search</label>
                    <input type="text" name="keyword" value="${keyword}" placeholder="Product name, vendor, category..." style="width:100%; padding:10px 12px; border:1px solid #cbd5e1; border-radius:8px;" />
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
                <a href="${pageContext.request.contextPath}/admin/products" style="padding:10px 14px; border-radius:8px; border:1px solid #cbd5e1; color:#334155; text-decoration:none; font-weight:600; text-align:center;">Reset</a>
            </form>

            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Product name</th>
                    <th>Vendor</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Status</th>
                    <th>Downloads</th>
                    <th>Rating</th>
                    <th>Created At</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="p" items="${products}">
                    <tr>
                        <td>#<c:out value="${p.softwareId}"/></td>
                        <td><c:out value="${p.name}"/></td>
                        <td><c:out value="${p.vendorName}"/></td>
                        <td><c:out value="${p.categoryName}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${p.isFree == 1}">
                                    Free
                                </c:when>
                                <c:otherwise>
                                    <fmt:formatNumber value="${p.price}" type="currency" currencySymbol="₫"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${p.status == 'ACTIVE'}">
                                    <span class="badge active">ACTIVE</span>
                                </c:when>
                                <c:when test="${p.status == 'INACTIVE'}">
                                    <span class="badge inactive">INACTIVE</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge pending"><c:out value="${p.status}"/></span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td><c:out value="${p.downloadCount}"/></td>
                        <td><fmt:formatNumber value="${p.avgRating}" minFractionDigits="1" maxFractionDigits="1"/></td>
                        <td><fmt:formatDate value="${p.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                        <td>
                            <div style="display:flex; gap:8px; flex-wrap:wrap;">
                                <a href="${pageContext.request.contextPath}/admin/products/detail?softwareId=${p.softwareId}" style="padding:6px 10px; border-radius:6px; border:1px solid #cbd5e1; text-decoration:none; color:#334155; font-weight:600;">Detail</a>
                                <form method="post" action="${pageContext.request.contextPath}/admin/products/${p.status == 'ACTIVE' ? 'disable' : 'enable'}" style="margin:0;">
                                    <input type="hidden" name="softwareId" value="${p.softwareId}" />
                                    <button type="submit" style="padding:6px 10px; border-radius:6px; border:none; background:${p.status == 'ACTIVE' ? '#f97316' : '#22c55e'}; color:#fff; font-weight:600;">
                                        ${p.status == 'ACTIVE' ? 'Disable' : 'Enable'}
                                    </button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty products}">
                    <tr>
                        <td colspan="9" style="text-align:center; color:#94a3b8; padding: 16px;">No products found.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>

            <c:if test="${totalPages > 1}">
                <div style="display:flex; justify-content:center; align-items:center; gap:8px; margin-top:16px; flex-wrap:wrap;">
                    <c:if test="${currentPage > 1}">
                        <a href="${pageContext.request.contextPath}/admin/products?page=${currentPage - 1}&keyword=${keyword}&status=${status}" style="padding:8px 12px; border:1px solid #cbd5e1; border-radius:8px; text-decoration:none; color:#334155;">Previous</a>
                    </c:if>

                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <c:choose>
                            <c:when test="${i == currentPage}">
                                <a href="${pageContext.request.contextPath}/admin/products?page=${i}&keyword=${keyword}&status=${status}"
                                   style="padding:8px 12px; border-radius:8px; text-decoration:none; border:1px solid #3b82f6; background:#3b82f6; color:#fff; font-weight:600;">
                                        ${i}
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/admin/products?page=${i}&keyword=${keyword}&status=${status}"
                                   style="padding:8px 12px; border-radius:8px; text-decoration:none; border:1px solid #cbd5e1; background:#fff; color:#334155; font-weight:500;">
                                        ${i}
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <a href="${pageContext.request.contextPath}/admin/products?page=${currentPage + 1}&keyword=${keyword}&status=${status}" style="padding:8px 12px; border:1px solid #cbd5e1; border-radius:8px; text-decoration:none; color:#334155;">Next</a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </main>
</div>

</body>
</html>
