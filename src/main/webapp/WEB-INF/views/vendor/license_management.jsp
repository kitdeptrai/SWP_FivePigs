<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>License Management</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/vendor/vendor.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
        <style>

            /* ===== STATS ===== */
            .stats-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
                gap: 20px;
                margin-bottom: 30px;
            }

            .stat-card {
                background: #1e293b;
                padding: 25px;
                border-radius: 16px;
            }

            .stat-card h2 {
                margin: 10px 0;
            }

            /* ===== TOOLBAR ===== */
            .toolbar {
                display: flex;
                justify-content: space-between;
                margin:20px 0;
            }

            .toolbar input {
                width: 350px;
                padding: 10px;
                border-radius: 10px;
                border: none;
                background: #1e293b;
                color: white;
            }

            .filter-btn {
                padding: 8px 15px;
                margin-left: 8px;
                background: #1e293b;
                border-radius: 8px;
                border: none;
                color: white;
                cursor: pointer;
            }

            .filter-btn.active,
            .filter-btn:hover {
                background: #3b82f6;
            }

            /* ===== TABLE ===== */
            .table-container {
                background: #1e293b;
                padding: 24px;
                border-radius: 20px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.3);
            }

            .license-table {
                width: 100%;
                border-collapse: collapse;
            }

            .license-table thead {
                border-bottom: 1px solid #334155;
            }

            .license-table th {
                padding: 14px 12px;
                font-weight: 600;
                font-size: 14px;
                color: #94a3b8;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            .license-table td {
                padding: 16px 12px;
                border-bottom: 1px solid #334155;
                font-size: 14px;
                color: #e2e8f0;
            }

            .license-table tbody tr {
                transition: all 0.2s ease;
            }

            .license-table tbody tr:hover {
                background: #334155;
                transform: scale(1.01);
            }

            /* License Key */
            .license-key {
                background: #0f172a;
                padding: 6px 12px;
                border-radius: 8px;
                font-weight: 600;
                letter-spacing: 0.5px;
                display: inline-block;
            }

            /* Product */
            .product-name {
                font-weight: 500;
            }

            /* Customer */
            .customer-email {
                font-size: 14px;
                color: #cbd5e1;
            }

            /* STATUS */
            .status {
                padding: 6px 14px;
                border-radius: 30px;
                font-size: 12px;
                font-weight: 600;
                text-transform: uppercase;
                display: inline-block;
            }

            .status.active {
                background: rgba(34,197,94,0.15);
                color: #22c55e;
            }

            .status.expired {
                background: rgba(250,204,21,0.15);
                color: #facc15;
            }

            .status.revoked {
                background: rgba(239,68,68,0.15);
                color: #ef4444;
            }

            /* ACTIONS */
            .actions {
                white-space: nowrap;
            }

            .action-btn {
                width: 32px;
                height: 32px;
                border-radius: 50%;
                background: #0f172a;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                margin-right: 6px;
                cursor: pointer;
                transition: 0.2s;
            }

            .action-btn:hover {
                background: #475569;
            }

            .action-btn.revoke:hover {
                background: #7f1d1d;
            }
            .license-box {
                display: inline-flex;
                align-items: center;
                background: #0f172a;
                padding: 8px 14px;
                border-radius: 10px;
                gap: 12px;
                font-weight: 600;
                letter-spacing: 0.5px;
                color: #e2e8f0;
            }

            .license-text {
                font-size: 14px;
            }

            .copy-icon {
                background: transparent;
                border: none;
                color: #94a3b8;
                cursor: pointer;
                padding: 4px;
                border-radius: 6px;
                transition: 0.2s;
            }

            .copy-icon:hover {
                background: #1e293b;
                color: #ffffff;
            }
            .pagination{
                display:flex;
                justify-content:center;
                gap:8px;
                margin-top:20px;
            }

            .pagination button{
                background:#0f172a;
                border:none;
                color:#e2e8f0;
                padding:6px 12px;
                border-radius:6px;
                cursor:pointer;
            }

            .pagination button.active{
                background:#3b82f6;
            }

            .pagination button:hover{
                background:#475569;
            }
        </style>
    </head>

    <body>
        <div class="layout">
            <jsp:include page="layout/side_bar.jsp"/>
            <div class="main">
                <h1>License Management</h1>
                <p class="subtitle">Monitor and manage all license keys for your products</p>

                <!-- ===== STATS ===== -->
                <div class="cards">
                    <div class="card">
                        <div class="card-icon info">
                            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#0ea5e9" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M21 2l-2 2m-7.61 7.61a5.5 5.5 0 1 1-7.778 7.778 5.5 5.5 0 0 1 7.777-7.777zm0 0L15.5 7.5m0 0l3 3L22 7l-3-3-3.5 3.5z"></path>
                            </svg>
                        </div>
                        <h2>${totalLicense}</h2>
                        <p>Total Licenses</p>
                    </div>
                    <div class="card">
                        <div class="card-icon success">
                            <svg width="22" height="22" viewBox="0 0 24 24"
                                 fill="none"
                                 stroke="currentColor"
                                 stroke-width="2"
                                 stroke-linecap="round"
                                 stroke-linejoin="round">
                            <polyline points="20 6 9 17 4 12"></polyline>
                            </svg>
                        </div>
                        <h2>${totalLicenseActive}</h2>
                        <p>Active Licenses</p>
                    </div>
                    <div class="card">
                        <div class="card-icon warning">
                            <svg width="22" height="22" viewBox="0 0 24 24" 
                                 fill="none" 
                                 stroke="currentColor" 
                                 stroke-width="2" 
                                 stroke-linecap="round" 
                                 stroke-linejoin="round">
                            <circle cx="12" cy="12" r="10"></circle>
                            <polyline points="12 6 12 12 16 14"></polyline>
                            </svg>
                        </div>
                        <h2>${totalLicenseExpire}</h2>
                        <p>Expired Licenses</p>
                    </div>
                    <div class="card">
                        <div class="card-icon danger">
                            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#ff4d4f" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <circle cx="12" cy="12" r="10"></circle>
                            <line x1="4.93" y1="4.93" x2="19.07" y2="19.07"></line>
                            </svg>
                        </div>
                        <h2>${totalLicenseRevoke}</h2>
                        <p>Revoked Licenses</p>
                    </div>
                </div>

                <!-- ===== TOOLBAR ===== -->
                <div class="toolbar">
                    <input type="text" id="searchInput"
                           placeholder="Search by license key, email, or product..."
                           onkeyup="applyFilter()">

                    <div>
                        <button class="filter-btn active" onclick="setStatus('all', this)">All</button>
                        <button class="filter-btn" onclick="setStatus('active', this)">Active</button>
                        <button class="filter-btn" onclick="setStatus('expired', this)">Expired</button>
                        <button class="filter-btn" onclick="setStatus('revoked', this)">Revoked</button>
                    </div>
                </div>

                <!-- ===== TABLE ===== -->
                <div class="table-card">

                    <table class="dashboard-table">
                        <thead>
                            <tr>
                                <th>License Key</th>
                                <th>Product</th>
                                <th>Plan</th>
                                <th>Usage</th>
                                <th>Owner</th>
                                <th>Activation</th>
                                <th>Expiry</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>

                        <tbody>

                            <c:forEach var="l" items="${listLicense}">
                                <tr data-status="${l.status}">

                                    <!-- License Key -->
                                    <td>
                                        <div class="license-box">
                                            <span class="license-text">${l.licenseKey}</span>
                                            <button class="copy-icon"
                                                    onclick="copyLicense('${l.licenseKey}', this)">
                                                <i class="fa-regular fa-copy"></i>
                                            </button>
                                        </div>
                                    </td>

                                    <!-- Product -->
                                    <td class="product-name">
                                        ${l.software.name}
                                    </td>

                                    <!-- PLAN -->
                                    <td>
                                        <span style="font-weight:600; color:#60a5fa;">
                                            ${l.softwarePricing.planName}
                                        </span>
                                    </td>

                                    <!-- USAGE -->
                                    <td>
                                        <span>
                                            ${l.usedUsers} / ${l.softwarePricing.maxUsers}
                                        </span>
                                    </td>

                                    <!-- PRICE -->


                                    <!-- CUSTOMER -->
                                    <td class="customer-cell">
                                        <div>${l.user.fullName}</div>
                                        <div class="customer-email">${l.user.email}</div>
                                    </td>

                                    <!-- PURCHASE -->
                                    <td>
                                        ${l.purchaseDate.toLocalDate()}
                                    </td>

                                    <!-- EXPIRE -->
                                    <td>
                                        ${l.expireDate.toLocalDate()}
                                    </td>

                                    <!-- STATUS -->
                                    <td>
                                        <span class="status ${l.status.toLowerCase()}">
                                            ${l.status}
                                        </span>
                                    </td>

                                    <!-- ACTION -->
                                    <td class="actions">
                                        <c:if test="${l.status != 'REVOKED'}">
                                            <form action="${pageContext.request.contextPath}/change_status_license"
                                                  method="post" style="display:inline;">

                                                <input type="hidden" name="licenseId" value="${l.licenseId}">
                                                <input type="hidden" name="status" value="REVOKED">

                                                <button type="submit" class="action-btn revoke">
                                                    <i class="fa-solid fa-ban"></i>
                                                </button>
                                            </form>
                                        </c:if>
                                    </td>

                                </tr>
                            </c:forEach>

                        </tbody>
                    </table>
                    <div class="pagination" id="pagination"></div>

                </div>
            </div>
        </div>
        <!-- ===== JAVASCRIPT FILTER ===== -->
        <script>

            let currentStatus = "all";
            function setStatus(status, btn) {

                currentStatus = status;
                document.querySelectorAll(".filter-btn")
                        .forEach(b => b.classList.remove("active"));
                btn.classList.add("active");
                applyFilter();
            }

            function applyFilter() {

                const keyword = document
                        .getElementById("searchInput")
                        .value
                        .toLowerCase();

                const rows = document.querySelectorAll("tbody tr");

                rows.forEach(row => {

                    const textMatch =
                            row.innerText.toLowerCase().includes(keyword);

                    const statusMatch =
                            currentStatus === "all" ||
                            row.dataset.status.toLowerCase() === currentStatus;

                    if (textMatch && statusMatch) {
                        row.classList.remove("filtered-out");
                    } else {
                        row.classList.add("filtered-out");
                    }

                });

                currentPage = 1;
                createPagination();
            }

            function copyLicense(key, btn) {
                navigator.clipboard.writeText(key);
                btn.style.color = "#22c55e";
                setTimeout(() => {
                    btn.style.color = "#94a3b8";
                }, 1000);
            }

            const rowsPerPage = 10;
            let currentPage = 1;

            function showPage(page) {

                const rows = Array.from(document.querySelectorAll("tbody tr"))
                        .filter(row => !row.classList.contains("filtered-out"));

                const start = (page - 1) * rowsPerPage;
                const end = start + rowsPerPage;

                document.querySelectorAll("tbody tr").forEach(row => {
                    row.style.display = "none";
                });

                rows.forEach((row, index) => {
                    if (index >= start && index < end) {
                        row.style.display = "";
                    }
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
                        .filter(row => !row.classList.contains("filtered-out"));

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
            createPagination();
        </script>

    </body>
</html>
