<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="en_US" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin - Vendor Payouts</title>
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

        .main { padding: 32px; }
        .subtitle { color: #64748b; }

        .logout-btn {
            margin-top: auto;
            color: #ef4444;
            font-weight: 600;
        }

        .logout-btn:hover {
            background-color: rgba(239, 68, 68, 0.1);
        }

        .card {
            max-width: 100%;
            margin: 0;
            background: var(--card-bg);
            padding: 24px;
            border-radius: 12px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            border: 1px solid #e2e8f0;
        }

        .payout-filter {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
            gap: 12px;
            align-items: end;
            margin-bottom: 12px;
        }

        .payout-filter label {
            display: block;
            font-size: 12px;
            color: #64748b;
            margin-bottom: 6px;
        }

        .payout-filter input,
        .payout-filter select {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid #cbd5e1;
            border-radius: 8px;
            background: #fff;
            color: #334155;
        }

        .btn-inline {
            width: 100%;
            min-height: 42px;
            padding: 10px 12px;
            border-radius: 8px;
            border: none;
            background: #3b82f6;
            color: #fff;
            font-weight: 600;
            cursor: pointer;
        }

        .btn-inline:disabled {
            opacity: .6;
            cursor: not-allowed;
        }

        .btn-reset {
            width: 100%;
            min-height: 42px;
            padding: 10px 12px;
            border: 1px solid #cbd5e1;
            border-radius: 8px;
            text-decoration: none;
            color: #334155;
            font-weight: 600;
            display: inline-flex;
            align-items: center;
            justify-content: center;
        }

        .payout-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 16px;
        }

        .payout-table th {
            text-align: left;
            padding: 12px;
            background: #f8fafc;
            color: #475569;
            font-size: 14px;
        }

        .payout-table td {
            padding: 12px;
            border-bottom: 1px solid #e2e8f0;
            font-size: 14px;
            vertical-align: top;
        }

        .badge {
            padding: 4px 8px;
            border-radius: 999px;
            font-size: 12px;
            font-weight: 600;
        }

        .pending { background: rgba(234,179,8,0.15); color: #a16207; }
        .paid { background: rgba(34,197,94,0.15); color: #15803d; }
    </style>
</head>
<body>
<div class="layout">
    <c:set var="activeMenu" value="payouts"/>
    <jsp:include page="sidebar.jsp"/>

    <main class="main">
        <h1 style="margin-top: 0;">Vendor Payout Approval</h1>
        <p class="subtitle">Approve vendor payout requests (PENDING → PAID).</p>

        <div class="card">
            <form method="get" action="${pageContext.request.contextPath}/admin/payouts" class="payout-filter">
                <div>
                    <label>Search</label>
                    <input type="text" name="keyword" value="${keyword}" placeholder="Payout ID / vendor name / email" />
                </div>
                <div>
                    <label>Status</label>
                    <select name="status">
                        <option value="" ${empty status ? 'selected' : ''}>All</option>
                        <option value="PENDING" ${status == 'PENDING' ? 'selected' : ''}>PENDING</option>
                        <option value="PAID" ${status == 'PAID' ? 'selected' : ''}>PAID</option>
                    </select>
                </div>
                <div>
                    <label>From Date</label>
                    <input type="date" name="fromDate" value="${fromDate}" />
                </div>
                <div>
                    <label>To Date</label>
                    <input type="date" name="toDate" value="${toDate}" />
                </div>
                <div>
                    <label>Sort ID</label>
                    <select name="sortById">
                        <option value="desc" ${sortById == 'desc' ? 'selected' : ''}>ID Descending</option>
                        <option value="asc" ${sortById == 'asc' ? 'selected' : ''}>ID Ascending</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-inline">Filter</button>
                <a href="${pageContext.request.contextPath}/admin/payouts" class="btn-reset">Reset</a>
            </form>

            <c:if test="${param.success == 'approved'}">
                <div class="alert success">Payout approved successfully.</div>
            </c:if>
            <c:if test="${param.error == 'invalid_token'}">
                <div class="alert danger">Your session token is invalid or expired. Please try again.</div>
            </c:if>
            <c:if test="${param.error == 'invalid_id' || param.error == 'invalid_state' || param.error == 'not_found' || param.error == 'db_error'}">
                <div class="alert danger">Cannot approve payout. Please try again.</div>
            </c:if>

            <table class="payout-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Vendor</th>
                    <th>Amount</th>
                    <th>Method</th>
                    <th>Account</th>
                    <th>Requested</th>
                    <th>Processed</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="p" items="${payouts}">
                    <tr>
                        <td>#${p.payoutId}</td>
                        <td>
                            <div><strong><c:out value="${p.vendorName}"/></strong></div>
                            <div style="color:#64748b;"><c:out value="${p.vendorEmail}"/></div>
                        </td>
                        <td><fmt:formatNumber value="${p.amount}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td><c:out value="${p.paymentMethod}"/></td>
                        <td><c:out value="${p.paymentAccount}"/></td>
                        <td><fmt:formatDate value="${p.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty p.processedAt}">
                                    <fmt:formatDate value="${p.processedAt}" pattern="dd/MM/yyyy HH:mm"/>
                                </c:when>
                                <c:otherwise>-</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <span class="badge ${p.status == 'PAID' ? 'paid' : 'pending'}">${p.status}</span>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${p.status == 'PENDING'}">
                                    <form method="post" action="${pageContext.request.contextPath}/admin/payouts" style="margin:0;" onsubmit="if(!confirm('Are you sure you want to approve this payout request?')){return false;} this.querySelector('button[type=\'submit\']').disabled=true; this.querySelector('button[type=\'submit\']').innerText='Approving...';">
                                        <input type="hidden" name="payoutId" value="${p.payoutId}"/>
                                        <input type="hidden" name="approveToken" value="${approveToken}"/>
                                        <button type="submit" class="btn btn-inline">Approve</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <button type="button" class="btn btn-inline" disabled>Approved</button>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty payouts}">
                    <tr>
                        <td colspan="9" style="text-align:center; color:#94a3b8;">No payout requests found.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>

            <c:if test="${totalPages > 1}">
                <div style="display:flex; justify-content:center; gap:8px; margin-top:16px; flex-wrap:wrap;">
                    <c:if test="${currentPage > 1}">
                        <a href="${pageContext.request.contextPath}/admin/payouts?page=${currentPage - 1}&status=${status}&keyword=${keyword}&fromDate=${fromDate}&toDate=${toDate}&sortById=${sortById}" style="padding:8px 12px; border:1px solid #cbd5e1; border-radius:8px; text-decoration:none; color:#334155;">Previous</a>
                    </c:if>
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <c:choose>
                            <c:when test="${i == currentPage}">
                                <a href="${pageContext.request.contextPath}/admin/payouts?page=${i}&status=${status}&keyword=${keyword}&fromDate=${fromDate}&toDate=${toDate}&sortById=${sortById}" style="padding:8px 12px; border-radius:8px; text-decoration:none; border:1px solid #3b82f6; background:#3b82f6; color:#fff; font-weight:600;">${i}</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/admin/payouts?page=${i}&status=${status}&keyword=${keyword}&fromDate=${fromDate}&toDate=${toDate}&sortById=${sortById}" style="padding:8px 12px; border-radius:8px; text-decoration:none; border:1px solid #cbd5e1; background:#fff; color:#334155; font-weight:500;">${i}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <c:if test="${currentPage < totalPages}">
                        <a href="${pageContext.request.contextPath}/admin/payouts?page=${currentPage + 1}&status=${status}&keyword=${keyword}&fromDate=${fromDate}&toDate=${toDate}&sortById=${sortById}" style="padding:8px 12px; border:1px solid #cbd5e1; border-radius:8px; text-decoration:none; color:#334155;">Next</a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </main>
</div>
</body>
</html>
