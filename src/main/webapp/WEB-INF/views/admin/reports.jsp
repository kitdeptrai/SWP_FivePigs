<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin - Reports</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <style>
        :root { --dark-blue:#1e293b; --sidebar-bg:#0f172a; --card-bg:#ffffff; --text-main:#334155; --bg-gray:#f5f7fb; --accent:#3b82f6; }
        body { background-color: var(--bg-gray); color: var(--text-main); margin: 0; font-family: 'Inter', sans-serif; }
        .layout { display: grid; grid-template-columns: 260px 1fr; min-height: 100vh; }

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

        .logout-btn {
            margin-top: auto;
            color: #ef4444;
            font-weight: 600;
        }

        .logout-btn:hover {
            background-color: rgba(239, 68, 68, 0.1);
        }

        .main { padding: 32px; overflow-y: auto; }
        .subtitle { color: #64748b; }
        .card { background: var(--card-bg); padding: 24px; border-radius: 12px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); border: 1px solid #e2e8f0; }
        .filter-grid { margin-top: 8px; display: grid; grid-template-columns: 2fr 1fr auto auto; gap: 10px; align-items: end; }
        table { width: 100%; border-collapse: collapse; margin-top: 16px; }
        th { text-align: left; padding: 12px; background-color: var(--bg-gray); color: #475569; font-weight: 600; font-size: 14px; }
        td { padding: 12px; border-bottom: 1px solid #e2e8f0; font-size: 14px; vertical-align: top; }
        .badge { padding: 4px 8px; border-radius: 6px; font-size: 12px; font-weight: 600; display: inline-block; }
        .badge.pending { background: #fef3c7; color: #92400e; }
        .badge.error-review, .badge.error-approval, .badge.error-rejected { background: #fee2e2; color: #991b1b; }
        .badge.rejected { background: #f1f5f9; color: #475569; }
        .badge.read { background: #dcfce7; color: #166534; }
        .input, .select { width: 100%; padding: 10px 12px; border: 1px solid #cbd5e1; border-radius: 8px; background: #fff; color: #334155; font-size: 14px; }
        .btn { padding: 10px 14px; border-radius: 8px; border: none; background: #3b82f6; color: #fff; font-weight: 600; text-decoration: none; display: inline-flex; align-items: center; justify-content: center; cursor: pointer; }
        .btn-reset { padding: 10px 14px; border-radius: 8px; border: 1px solid #cbd5e1; color: #334155; text-decoration: none; font-weight: 600; display: inline-flex; align-items: center; justify-content: center; background: #fff; }

        .btn-link {
            text-decoration: none;
            font-weight: 600;
            color: #3b82f6;
            margin-right: 10px;
            cursor: pointer;
            border: none;
            background: none;
            padding: 0;
            font-size: 14px;
        }

        .btn-link.mark-read {
            color: #15803d;
        }

        .modal-backdrop {
            position: fixed;
            inset: 0;
            background: rgba(15, 23, 42, 0.5);
            display: flex;
            align-items: center;
            justify-content: center;
            z-index: 1000;
        }

        .modal {
            width: min(840px, 92vw);
            max-height: 85vh;
            overflow: auto;
            background: #fff;
            border-radius: 14px;
            border: 1px solid #e2e8f0;
            box-shadow: 0 20px 50px rgba(2, 6, 23, 0.3);
            padding: 20px;
        }

        .modal-content {
            display: flex;
            flex-direction: column;
            gap: 12px;
        }

        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .close-modal {
            text-decoration: none;
            border: 1px solid #cbd5e1;
            border-radius: 8px;
            padding: 6px 10px;
            color: #334155;
            font-weight: 600;
        }

        .actions {
            display: flex;
            gap: 10px;
            justify-content: flex-end;
        }

        .actions button {
            border: none;
            border-radius: 8px;
            padding: 8px 14px;
            color: #fff;
            font-weight: 600;
        }

        .actions .small {
            text-decoration: none;
            border: 1px solid #cbd5e1;
            border-radius: 8px;
            padding: 6px 10px;
            color: #334155;
            font-weight: 600;
            display: inline-flex;
            align-items: center;
            justify-content: center;
        }

        .detail-grid {
            display: grid;
            grid-template-columns: 180px 1fr;
            gap: 12px;
            margin-top: 10px;
        }

        .detail-label { color: #64748b; font-weight: 600; }
        .detail-value { color: #0f172a; }
    </style>
</head>
<body>
<div class="layout">
    <c:set var="activeMenu" value="reports"/>
    <jsp:include page="sidebar.jsp"/>

    <main class="main">
        <h1 style="margin-top: 0;">Reports Management</h1>
        <p class="subtitle">Only two actions are available: View Detail and Mark as Read.</p>

        <div class="card" style="max-width: 100%;">
            <c:if test="${param.success == 'marked_read'}">
                <div class="alert success">Report has been marked as read and a notification email was sent to the reporter.</div>
            </c:if>
            <c:if test="${not empty param.error}">
                <div class="alert danger">Failed to process action: <c:out value="${param.error}"/>.</div>
            </c:if>

            <form method="get" action="${pageContext.request.contextPath}/admin/reports" class="filter-grid">
                <div>
                    <label style="display:block; font-size:12px; color:#64748b; margin-bottom:6px;">Search</label>
                    <input class="input" type="text" name="keyword" value="${keyword}" placeholder="Report ID, software, vendor, reporter, reason..."/>
                </div>
                <div>
                    <label style="display:block; font-size:12px; color:#64748b; margin-bottom:6px;">Status</label>
                    <select class="select" name="status">
                        <option value="">All</option>
                        <option value="PENDING" ${status == 'PENDING' ? 'selected' : ''}>PENDING</option>
                        <option value="READ" ${status == 'READ' ? 'selected' : ''}>READ</option>
                        <option value="ERROR_REVIEW" ${status == 'ERROR_REVIEW' ? 'selected' : ''}>ERROR_REVIEW</option>
                        <option value="ERROR_APPROVAL" ${status == 'ERROR_APPROVAL' ? 'selected' : ''}>ERROR_APPROVAL</option>
                        <option value="REJECTED" ${status == 'REJECTED' ? 'selected' : ''}>REJECTED</option>
                        <option value="ERROR_REJECTED" ${status == 'ERROR_REJECTED' ? 'selected' : ''}>ERROR_REJECTED</option>
                    </select>
                </div>
                <button type="submit" class="btn">Filter</button>
                <a href="${pageContext.request.contextPath}/admin/reports" class="btn-reset">Reset</a>
            </form>

            <table>
                <thead>
                <tr>
                    <th>Report ID</th>
                    <th>Software</th>
                    <th>Reporter</th>
                    <th>Status</th>
                    <th>Created At</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="r" items="${reports}">
                    <tr>
                        <td>#${r.reportId}</td>
                        <td>
                            <div><strong><c:out value="${r.name}"/></strong></div>
                            <div style="color:#64748b;">Vendor: <c:out value="${r.vendorName}"/></div>
                        </td>
                        <td><c:out value="${r.reporterName}"/></td>
                        <td>
                            <c:set var="statusClass" value="${r.reportStatus == 'PENDING' ? 'pending' : (r.reportStatus == 'READ' ? 'read' : (r.reportStatus == 'REJECTED' ? 'rejected' : (r.reportStatus == 'ERROR_REVIEW' ? 'error-review' : (r.reportStatus == 'ERROR_APPROVAL' ? 'error-approval' : 'error-rejected'))))}"/>
                            <span class="badge ${statusClass}"><c:out value="${r.reportStatus}"/></span>
                        </td>
                        <td><fmt:formatDate value="${r.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                        <td>
                            <a class="btn-link" href="${pageContext.request.contextPath}/admin/reports?page=${currentPage}&keyword=${keyword}&status=${status}&reportId=${r.reportId}">View Detail</a>
                            <c:if test="${r.reportStatus != 'READ'}">
                                <a class="btn-link mark-read" href="#mark-read-${r.reportId}">Mark as Read</a>

                                <div id="mark-read-${r.reportId}" class="modal">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h2>Mark report as read</h2>
                                            <a class="close-modal" href="${pageContext.request.contextPath}/admin/reports">×</a>
                                        </div>
                                        <p>Mark this report as read and notify the reporter by email?</p>
                                        <form method="post" action="${pageContext.request.contextPath}/admin/reports">
                                            <input type="hidden" name="action" value="markRead"/>
                                            <input type="hidden" name="reportId" value="${r.reportId}"/>
                                            <div class="actions">
                                                <button type="submit" style="background:#15803d;">Mark as Read</button>
                                                <a class="small" href="${pageContext.request.contextPath}/admin/reports">Cancel</a>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty reports}">
                    <tr>
                        <td colspan="6" style="text-align:center; color:#94a3b8;">No reports found.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>

            <c:if test="${totalPages > 1}">
                <div style="display:flex; justify-content:center; align-items:center; gap:8px; margin-top:16px; flex-wrap:wrap;">
                    <c:if test="${currentPage > 1}">
                        <a href="${pageContext.request.contextPath}/admin/reports?page=${currentPage - 1}&keyword=${keyword}&status=${status}" class="btn-reset">Previous</a>
                    </c:if>
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <c:choose>
                            <c:when test="${i == currentPage}"><a href="${pageContext.request.contextPath}/admin/reports?page=${i}&keyword=${keyword}&status=${status}" class="btn">${i}</a></c:when>
                            <c:otherwise><a href="${pageContext.request.contextPath}/admin/reports?page=${i}&keyword=${keyword}&status=${status}" class="btn-reset">${i}</a></c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <c:if test="${currentPage < totalPages}">
                        <a href="${pageContext.request.contextPath}/admin/reports?page=${currentPage + 1}&keyword=${keyword}&status=${status}" class="btn-reset">Next</a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </main>
</div>

<c:if test="${not empty selectedReport}">
    <div class="modal-backdrop">
        <div class="modal">
            <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom: 8px;">
                <h3 style="margin:0;">Report Detail #${selectedReport.reportId}</h3>
                <a class="btn-reset" href="${pageContext.request.contextPath}/admin/reports?page=${currentPage}&keyword=${keyword}&status=${status}">Close</a>
            </div>

            <div class="detail-grid">
                <div class="detail-label">Software</div>
                <div class="detail-value"><strong><c:out value="${selectedReport.softwareName}"/></strong> (ID #${selectedReport.softwareId})</div>

                <div class="detail-label">Vendor</div>
                <div class="detail-value"><c:out value="${selectedReport.vendorName}"/> - <c:out value="${selectedReport.vendorEmail}"/></div>

                <div class="detail-label">Reporter</div>
                <div class="detail-value"><c:out value="${selectedReport.reporterName}"/> - <c:out value="${selectedReport.reporterEmail}"/></div>

                <div class="detail-label">Report status</div>
                <div class="detail-value"><c:out value="${selectedReport.reportStatus}"/></div>

                <div class="detail-label">Software status</div>
                <div class="detail-value"><c:out value="${selectedReport.softwareStatus}"/></div>

                <div class="detail-label">Created at</div>
                <div class="detail-value"><fmt:formatDate value="${selectedReport.createdAt}" pattern="dd/MM/yyyy HH:mm"/></div>

                <div class="detail-label">Reason</div>
                <div class="detail-value"><c:out value="${selectedReport.reason}"/></div>
            </div>
        </div>
    </div>
</c:if>
</body>
</html>
