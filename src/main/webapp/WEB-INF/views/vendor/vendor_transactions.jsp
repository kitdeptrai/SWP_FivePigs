<%-- 
    Document   : vendor_transactions.jsp
    Created on : Mar 26, 2026, 11:46:31 AM
    Author     : MinhPD
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/vendor/vendor.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
        <style>
            .pagination {
                display:flex;
                justify-content:center;
                gap:8px;
                margin-top:20px;
            }

            .pagination button {
                background:#0f172a;
                border:none;
                color:#e2e8f0;
                padding:6px 12px;
                border-radius:6px;
                cursor:pointer;
            }

            .pagination button.active {
                background:#3b82f6;
            }

            .pagination button:hover {
                background:#475569;
            }
        </style>
    </head>
    <body>
        <div class="layout">

            <jsp:include page="layout/side_bar.jsp"/>

            <div class="main">

                <h1>Transaction History</h1>
                <p class="subtitle">Track your earnings and commissions</p>

                <div class="table-card">

                    <table class="dashboard-table">
                        <thead>
                            <tr>
                                <th>Software</th>
                                <th>Customer</th>
                                <th>Price</th>
                                <th>Commission</th>
                                <th>Your Revenue</th>
                                <th>Date</th>
                            </tr>
                        </thead>

                        <tbody>

                            <c:forEach var="t" items="${transactions}">
                                <tr>

                                    <!-- SOFTWARE -->
                                    <td>
                                        <strong>${t.software.name}</strong>
                                    </td>

                                    <!-- CUSTOMER -->
                                    <td>
                                        <div>${t.user.fullName}</div>
                                        <div class="customer-email">${t.user.email}</div>
                                    </td>

                                    <td>
                                        $${t.orderDetail.price}
                                    </td>

                                    <!-- COMMISSION -->
                                    <td>
                                        <span style="
                                              background: rgba(99,102,241,0.15);
                                              color:#6366f1;
                                              padding:5px 10px;
                                              border-radius:999px;
                                              font-size:12px;">
                                            ${t.systemConfig.configValue}% 
                                        </span>
                                    </td>

                                    <!-- REVENUE -->
                                    <td>
                                        <strong style="color:#22c55e;">
                                            $${t.amount}
                                        </strong>
                                    </td>

                                    <!-- DATE -->
                                    <td>
                                        ${t.order.orderDate.toLocalDate()}
                                    </td>

                                </tr>
                            </c:forEach>

                            <c:if test="${empty transactions}">
                                <tr class="empty-row">
                                    <td colspan="6" style="text-align:center; color:#94a3b8;">
                                        No transactions yet
                                    </td>
                                </tr>

                            </c:if>

                        </tbody>
                    </table>
                    <div class="pagination" id="pagination"></div>

                </div>

            </div>
        </div>
        <script>
            const rowsPerPage = 10;
            let currentPage = 1;

            function showPage(page) {
                const rows = Array.from(document.querySelectorAll("tbody tr"))
                        .filter(row => !row.classList.contains("empty-row"));

                const start = (page - 1) * rowsPerPage;
                const end = start + rowsPerPage;

                rows.forEach((row, index) => {
                    row.style.display = (index >= start && index < end) ? "" : "none";
                });

                document.querySelectorAll(".pagination button")
                        .forEach(btn => btn.classList.remove("active"));

                const activeBtn = document.getElementById("page-" + page);
                if (activeBtn)
                    activeBtn.classList.add("active");
            }

            function createPagination() {
                const pagination = document.getElementById("pagination");
                pagination.innerHTML = "";

                const rows = Array.from(document.querySelectorAll("tbody tr"))
                        .filter(row => !row.classList.contains("empty-row"));
                const totalPages = Math.ceil(rows.length / rowsPerPage);

                for (let i = 1; i <= totalPages; i++) {
                    const btn = document.createElement("button");
                    btn.innerText = i;
                    btn.id = "page-" + i;

                    btn.onclick = () => {
                        currentPage = i;
                        showPage(i);
                    };

                    pagination.appendChild(btn);
                }

                showPage(currentPage);
            }

            // chạy khi load
            createPagination();
        </script>
    </body>
</html>
