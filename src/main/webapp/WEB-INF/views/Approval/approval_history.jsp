<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Approval History</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Approval/approval.css">
    </head>
    <body>

        <div class="app">
            <!-- SIDEBAR -->
            <jsp:include page="./layout/sidebar.jsp"/>

            <!-- CONTENT -->
            <div class="content-history">
                <div class="history-wrap">
                    <h1 class="history-title">Approval History</h1>
                    <p class="history-sub">Your past approval decisions</p>

                    <div class="history-card">
                        <div class="history-filters">

                            <input type="text" id="searchName" placeholder="Search App Name">

                            <input type="text" id="searchVendor" placeholder="Search Vendor">

                            <input type="date" id="searchDate">

                            <select id="searchDecision">
                                <option value="">All Decision</option>
                                <option value="APPROVED">Approved</option>
                                <option value="REJECTED">Rejected</option>
                            </select>

                        </div>
                        <table class="history-table">
                            <thead>
                                <tr>
                                    <th>App Name</th>
                                    <th>Vendor</th>
                                    <th>Decision Date</th>
                                    <th>Decision</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody id="history-body">
                                <c:forEach var="it" items="${approvalHistory}">
                                    <c:if test="${it.approvalProcess.decision == 'APPROVED' || it.approvalProcess.decision == 'REJECTED'}">
                                        <tr>
                                            <td class="col-app"><c:out value="${it.appName}" /></td>
                                            <td><c:out value="${it.user.fullName}" /></td>
                                            <td class="col-date">
                                                <c:out value="${it.approvalProcess.approval_date}" />
                                            </td>
                                            <td class="col-decision">
                                                <c:out value="${it.approvalProcess.decision}" />
                                            </td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/approval_history_detail?softwareId=${it.softwareId}" class="btn-details">View Details</a>
                                            </td>
                                        </tr>
                                    </c:if>
                                </c:forEach>

                                <c:if test="${empty approvalHistory}">
                                    <tr>
                                        <td colspan="5" class="empty">No approval history.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                        <div class="pagination" id="history-pagination"></div>
                    </div>
                </div>
            </div>
        </div>
        <script>

            const itemsPerPage = 10;

            const rows = Array.from(document.querySelectorAll("#history-body tr"));
            const pagination = document.getElementById("history-pagination");

            const searchName = document.getElementById("searchName");
            const searchVendor = document.getElementById("searchVendor");
            const searchDate = document.getElementById("searchDate");
            const searchDecision = document.getElementById("searchDecision");


            function getFilteredRows() {

                const nameValue = searchName.value.toLowerCase();
                const vendorValue = searchVendor.value.toLowerCase();
                const dateValue = searchDate.value;
                const decisionValue = searchDecision.value;

                return rows.filter(row => {

                    const appName = row.cells[0].innerText.toLowerCase();
                    const vendor = row.cells[1].innerText.toLowerCase();
                    const date = row.cells[2].innerText;
                    const decision = row.cells[3].innerText;

                    const matchName = appName.includes(nameValue);
                    const matchVendor = vendor.includes(vendorValue);
                    const matchDate = dateValue === "" || date.includes(dateValue);
                    const matchDecision = decisionValue === "" || decision === decisionValue;

                    return matchName && matchVendor && matchDate && matchDecision;

                });

            }


            function showPage(page, filteredRows) {

                const start = (page - 1) * itemsPerPage;
                const end = start + itemsPerPage;

                rows.forEach(row => row.style.display = "none");

                filteredRows.slice(start, end).forEach(row => {
                    row.style.display = "";
                });

            }


            function createPagination(filteredRows) {

                pagination.innerHTML = "";

                const totalPages = Math.ceil(filteredRows.length / itemsPerPage);

                if (totalPages === 0)
                    return;

                for (let i = 1; i <= totalPages; i++) {

                    const btn = document.createElement("button");
                    btn.innerText = i;

                    btn.onclick = () => {

                        showPage(i, filteredRows);

                        document.querySelectorAll(".pagination button").forEach(b => b.classList.remove("active"));
                        btn.classList.add("active");

                    };

                    pagination.appendChild(btn);

                }

                pagination.querySelector("button").click();

            }


            function filterTable() {

                const filteredRows = getFilteredRows();

                rows.forEach(row => row.style.display = "none");

                if (filteredRows.length === 0) {

                    pagination.innerHTML = "";
                    return;

                }

                createPagination(filteredRows);

            }


            searchName.addEventListener("input", filterTable);
            searchVendor.addEventListener("input", filterTable);
            searchDate.addEventListener("change", filterTable);
            searchDecision.addEventListener("change", filterTable);


            filterTable();
        </script>
    </body>
</html>