<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="en_US" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin - Successful Orders</title>
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

        .main { padding: 32px; }
        .subtitle { color: #64748b; }

        .card {
            max-width: 100%;
            margin: 0;
            background: white;
            border-radius: 16px;
            padding: 20px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.08);
            border: 1px solid #e5e7eb;
        }

        .order-filter {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
            gap: 12px;
            align-items: end;
            margin-bottom: 12px;
        }

        .order-filter label {
            display: block;
            font-size: 12px;
            color: #64748b;
            margin-bottom: 6px;
        }

        .order-filter input {
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
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            justify-content: center;
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
            background: #fff;
        }

        .order-table { width: 100%; border-collapse: collapse; margin-top: 16px; }
        .order-table th {
            text-align: left;
            color: #666;
            font-weight: 600;
            padding: 10px;
            background: transparent;
            font-size: 14px;
        }
        .order-table td {
            padding: 10px;
            border-top: 1px solid #eee;
            font-size: 14px;
            vertical-align: top;
        }
        .order-table tr:hover { background: #f9fafb; }

        .badge { padding: 4px 8px; border-radius: 999px; font-size: 12px; font-weight: 600; }
        .paid { background: rgba(34,197,94,0.15); color: #15803d; }

        .order-modal-backdrop {
            position: fixed;
            inset: 0;
            background: rgba(15, 23, 42, 0.5);
            display: flex;
            align-items: center;
            justify-content: center;
            z-index: 1000;
        }

        .order-modal {
            width: min(900px, 92vw);
            max-height: 85vh;
            overflow: auto;
            background: #fff;
            border-radius: 14px;
            border: 1px solid #e2e8f0;
            box-shadow: 0 20px 50px rgba(2, 6, 23, 0.3);
            padding: 20px;
        }

        .order-modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 12px;
        }

        .close-btn {
            text-decoration: none;
            border: 1px solid #cbd5e1;
            border-radius: 8px;
            padding: 6px 10px;
            color: #334155;
            font-weight: 600;
        }

        .detail-table { width: 100%; border-collapse: collapse; }
        .detail-table th, .detail-table td {
            border-top: 1px solid #e2e8f0;
            padding: 10px;
            font-size: 14px;
            text-align: left;
        }
    </style>
</head>
<body>
<div class="layout">
    <c:set var="activeMenu" value="orders"/>
    <jsp:include page="sidebar.jsp"/>

    <main class="main">
        <h1 style="margin-top: 0;">Successful Customer Orders</h1>
        <p class="subtitle">List of completed orders (payment status = PAID).</p>

        <div class="card">
            <form method="get" action="${pageContext.request.contextPath}/admin/orders" class="order-filter">
                <div>
                    <label>Search</label>
                    <input type="text" name="keyword" value="${keyword}" placeholder="Order ID / customer name / email" />
                </div>
                <div>
                    <label>From Date</label>
                    <input type="date" name="fromDate" value="${fromDate}" />
                </div>
                <div>
                    <label>To Date</label>
                    <input type="date" name="toDate" value="${toDate}" />
                </div>
                <button type="submit" class="btn-inline">Filter</button>
                <a href="${pageContext.request.contextPath}/admin/orders" class="btn-reset">Reset</a>
            </form>

            <table class="order-table">
                <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Customer</th>
                    <th>Total</th>
                    <th>Commission %</th>
                    <th>Admin Receive</th>
                    <th>Order Date</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="o" items="${orders}">
                    <tr>
                        <td>#${o.orderId}</td>
                        <td>
                            <div><strong><c:out value="${o.customerName}"/></strong></div>
                            <div style="color:#64748b;"><c:out value="${o.customerEmail}"/></div>
                        </td>
                        <td><fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td><fmt:formatNumber value="${o.commissionPercent}" minFractionDigits="0" maxFractionDigits="2"/>%</td>
                        <td><fmt:formatNumber value="${o.adminReceivedAmount}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td><fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                        <td><span class="badge paid"><c:out value="${o.paymentStatus}"/></span></td>
                        <td>
                            <a class="btn-inline" href="${pageContext.request.contextPath}/admin/orders?page=${currentPage}&keyword=${keyword}&fromDate=${fromDate}&toDate=${toDate}&orderId=${o.orderId}">View Detail</a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty orders}">
                    <tr>
                        <td colspan="8" style="text-align:center; color:#94a3b8;">No successful orders found.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>

            <c:if test="${totalPages > 1}">
                <div style="display:flex; justify-content:center; gap:8px; margin-top:16px; flex-wrap:wrap;">
                    <c:if test="${currentPage > 1}">
                        <a href="${pageContext.request.contextPath}/admin/orders?page=${currentPage - 1}&keyword=${keyword}&fromDate=${fromDate}&toDate=${toDate}" class="btn-reset" style="width:auto;">Previous</a>
                    </c:if>
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <c:choose>
                            <c:when test="${i == currentPage}">
                                <a href="${pageContext.request.contextPath}/admin/orders?page=${i}&keyword=${keyword}&fromDate=${fromDate}&toDate=${toDate}" class="btn-inline" style="width:auto; min-height:unset; padding:8px 12px;">${i}</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/admin/orders?page=${i}&keyword=${keyword}&fromDate=${fromDate}&toDate=${toDate}" class="btn-reset" style="width:auto;">${i}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <c:if test="${currentPage < totalPages}">
                        <a href="${pageContext.request.contextPath}/admin/orders?page=${currentPage + 1}&keyword=${keyword}&fromDate=${fromDate}&toDate=${toDate}" class="btn-reset" style="width:auto;">Next</a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </main>
</div>

<c:if test="${not empty selectedOrderId}">
    <div class="order-modal-backdrop">
        <div class="order-modal">
            <div class="order-modal-header">
                <h3 style="margin:0;">Order Detail #${selectedOrderId}</h3>
                <a class="close-btn" href="${pageContext.request.contextPath}/admin/orders?page=${currentPage}&keyword=${keyword}&fromDate=${fromDate}&toDate=${toDate}">Close</a>
            </div>

            <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap:10px; margin-bottom:14px;">
                <div style="background:#f8fafc; border:1px solid #e2e8f0; border-radius:10px; padding:10px 12px;">
                    <div style="font-size:12px; color:#64748b;">Order ID</div>
                    <div style="font-weight:700; color:#0f172a;">#${selectedOrderId}</div>
                </div>
                <div style="background:#f8fafc; border:1px solid #e2e8f0; border-radius:10px; padding:10px 12px;">
                    <div style="font-size:12px; color:#64748b;">Items</div>
                    <div style="font-weight:700; color:#0f172a;">${orderDetailsCount}</div>
                </div>
                <div style="background:#f8fafc; border:1px solid #e2e8f0; border-radius:10px; padding:10px 12px;">
                    <div style="font-size:12px; color:#64748b;">Total Detail Value</div>
                    <div style="font-weight:700; color:#0f172a;">
                        <fmt:formatNumber value="${orderDetailsTotal}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/>
                    </div>
                </div>
            </div>

            <table class="detail-table">
                <thead>
                <tr>
                    <th>Detail ID</th>
                    <th>Software ID</th>
                    <th>Software Name</th>
                    <th>Price</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="d" items="${orderDetails}">
                    <tr>
                        <td>#${d.orderDetailId}</td>
                        <td>#${d.softwareId}</td>
                        <td><c:out value="${d.softwareName}"/></td>
                        <td><fmt:formatNumber value="${d.price}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/></td>
                    </tr>
                </c:forEach>
                <c:if test="${empty orderDetails}">
                    <tr>
                        <td colspan="4" style="text-align:center; color:#94a3b8;">No order details found.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</c:if>
</body>
</html>
