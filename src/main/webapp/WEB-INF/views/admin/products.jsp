<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="en_US" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin - Reported Products</title>
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
            color: #666;
            font-weight: 600;
            padding: 10px;
            background: transparent;
            font-size: 14px;
        }

        td {
            padding: 10px;
            border-top: 1px solid #eee;
            font-size: 14px;
            vertical-align: top;
        }

        tr:hover {
            background: #f9fafb;
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
        <h1 style="margin-top: 0;">Reported Products</h1>
        <p class="subtitle">Review customer reports and route them for bug review or reject them.</p>

        <div class="card" style="max-width: 100%;">
            <div style="display:flex; justify-content: space-between; align-items:center; gap: 16px;">
                <div>
                    <h2 style="margin:0; font-size: 18px; color: var(--dark-blue);">Report queue</h2>
                    <p style="margin:6px 0 0; color:#64748b; font-size: 13px;">Reports pulled from the <code>Report</code> table</p>
                </div>
            </div>

            <c:if test="${param.success == 'approved'}">
                <div style="margin-top:16px; padding:12px 14px; border-radius:10px; background:#dcfce7; color:#166534; border:1px solid #86efac;">
                    Report approved. Status changed to <strong>ERROR_REVIEW</strong>.
                </div>
            </c:if>
            <c:if test="${param.success == 'rejected'}">
                <div style="margin-top:16px; padding:12px 14px; border-radius:10px; background:#fee2e2; color:#991b1b; border:1px solid #fca5a5;">
                    Report rejected. Status changed to <strong>REJECTED</strong>.
                </div>
            </c:if>
            <c:if test="${not empty param.error}">
                <div style="margin-top:16px; padding:12px 14px; border-radius:10px; background:#fff7ed; color:#9a3412; border:1px solid #fdba74;">
                    Unable to process this report. Error: <c:out value="${param.error}"/>.
                </div>
            </c:if>

            <form method="get" action="${pageContext.request.contextPath}/admin/products" style="margin-top:16px; display:grid; grid-template-columns: 2fr 1fr auto auto; gap:10px; align-items:end;">
                <div>
                    <label style="display:block; font-size:12px; color:#64748b; margin-bottom:6px;">Search</label>
                    <input type="text" name="keyword" value="${keyword}" placeholder="Report ID, product, vendor, reporter, reason..." style="width:100%; padding:10px 12px; border:1px solid #cbd5e1; border-radius:8px;" />
                </div>
                <div>
                    <label style="display:block; font-size:12px; color:#64748b; margin-bottom:6px;">Report Status</label>
                    <select name="status" style="width:100%; padding:10px 12px; border:1px solid #cbd5e1; border-radius:8px;">
                        <option value="">All</option>
                        <option value="PENDING" ${status == 'PENDING' ? 'selected' : ''}>PENDING</option>
                        <option value="ERROR_REVIEW" ${status == 'ERROR_REVIEW' ? 'selected' : ''}>ERROR_REVIEW</option>
                        <option value="ERROR_APPROVAL" ${status == 'ERROR_APPROVAL' ? 'selected' : ''}>ERROR_APPROVAL</option>
                        <option value="REJECTED" ${status == 'REJECTED' ? 'selected' : ''}>REJECTED</option>
                        <option value="ERROR_REJECTED" ${status == 'ERROR_REJECTED' ? 'selected' : ''}>ERROR_REJECTED</option>
                    </select>
                </div>
                <button type="submit" style="padding:10px 14px; border-radius:8px; border:none; background:#3b82f6; color:#fff; font-weight:600;">Filter</button>
                <a href="${pageContext.request.contextPath}/admin/products" style="padding:10px 14px; border-radius:8px; border:1px solid #cbd5e1; color:#334155; text-decoration:none; font-weight:600; text-align:center;">Reset</a>
            </form>

            <table>
                <thead>
                <tr>
                    <th>Report ID</th>
                    <th>Product name</th>
                    <th>Vendor</th>
                    <th>Reporter</th>
                    <th>Reason</th>
                    <th>Report Status</th>
                    <th>Product Status</th>
                    <th>Created At</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="p" items="${products}">
                    <tr>
                        <td>#<c:out value="${p.reportId}"/></td>
                        <td><c:out value="${p.name}"/></td>
                        <td><c:out value="${p.vendorName}"/></td>
                        <td><c:out value="${p.reporterName}"/></td>
                        <td style="max-width:320px; white-space:normal;"><c:out value="${p.reason}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${p.reportStatus == 'PENDING'}">
                                    <span class="badge pending">PENDING</span>
                                </c:when>
                                <c:when test="${p.reportStatus == 'ERROR_REVIEW' || p.reportStatus == 'ERROR_APPROVAL'}">
                                    <span class="badge active"><c:out value="${p.reportStatus}"/></span>
                                </c:when>
                                <c:when test="${p.reportStatus == 'REJECTED' || p.reportStatus == 'ERROR_REJECTED'}">
                                    <span class="badge inactive"><c:out value="${p.reportStatus}"/></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge pending"><c:out value="${p.reportStatus}"/></span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${p.softwareStatus == 'ACTIVE'}">
                                    <span class="badge active">ACTIVE</span>
                                </c:when>
                                <c:when test="${p.softwareStatus == 'INACTIVE'}">
                                    <span class="badge inactive">INACTIVE</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge pending"><c:out value="${p.softwareStatus}"/></span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td><fmt:formatDate value="${p.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                        <td>
                            <div style="display:flex; gap:8px; flex-wrap:wrap;">
                                <a href="${pageContext.request.contextPath}/admin/products/detail?softwareId=${p.softwareId}" style="padding:6px 10px; border-radius:6px; border:1px solid #cbd5e1; text-decoration:none; color:#334155; font-weight:600;">Detail</a>
                                <c:if test="${p.reportStatus == 'PENDING'}">
                                    <a href="#approve-report-${p.reportId}" style="padding:6px 10px; border-radius:6px; border:none; background:#22c55e; color:#fff; font-weight:600; text-decoration:none;">Approve</a>
                                    <a href="#reject-report-${p.reportId}" style="padding:6px 10px; border-radius:6px; border:none; background:#ef4444; color:#fff; font-weight:600; text-decoration:none;">Reject</a>

                                    <div id="approve-report-${p.reportId}" class="modal">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h2>Approve report</h2>
                                                <a class="close-modal" href="${pageContext.request.contextPath}/admin/products">×</a>
                                            </div>
                                            <p>Approve this report and move it to <strong>ERROR_REVIEW</strong>?</p>
                                            <form method="post" action="${pageContext.request.contextPath}/admin/products/approve">
                                                <input type="hidden" name="reportId" value="${p.reportId}" />
                                                <div class="actions">
                                                    <button type="submit" style="background:#22c55e;">Approve</button>
                                                    <a class="small" href="${pageContext.request.contextPath}/admin/products">Cancel</a>
                                                </div>
                                            </form>
                                        </div>
                                    </div>

                                    <div id="reject-report-${p.reportId}" class="modal">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h2>Reject report</h2>
                                                <a class="close-modal" href="${pageContext.request.contextPath}/admin/products">×</a>
                                            </div>
                                            <p>Reject this report?</p>
                                            <form method="post" action="${pageContext.request.contextPath}/admin/products/reject">
                                                <input type="hidden" name="reportId" value="${p.reportId}" />
                                                <div class="actions">
                                                    <button type="submit" style="background:#ef4444;">Reject</button>
                                                    <a class="small" href="${pageContext.request.contextPath}/admin/products">Cancel</a>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty products}">
                    <tr>
                        <td colspan="9" style="text-align:center; color:#94a3b8; padding: 16px;">No reported products found.</td>
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
