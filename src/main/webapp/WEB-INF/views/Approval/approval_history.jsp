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

            function setupPagination(tbodyId, paginationId, rowsPerPage = 10) {

                const tbody = document.getElementById(tbodyId);
                const rows = tbody.querySelectorAll("tr");
                const pagination = document.getElementById(paginationId);

                let currentPage = 1;
                const totalPages = Math.ceil(rows.length / rowsPerPage);

                function showPage(page) {
                    currentPage = page;

                    rows.forEach((row) => {
                        row.style.display = "none";
                    });

                    const start = (page - 1) * rowsPerPage;
                    const end = start + rowsPerPage;

                    for (let i = start; i < end && i < rows.length; i++) {
                        rows[i].style.display = "";
                    }

                    // cập nhật active button
                    const buttons = pagination.querySelectorAll("button");
                    buttons.forEach(btn => btn.classList.remove("active"));
                    buttons[page - 1].classList.add("active");
                }

                function createPagination() {
                    pagination.innerHTML = "";

                    for (let i = 1; i <= totalPages; i++) {
                        const btn = document.createElement("button");
                        btn.innerText = i;

                        btn.onclick = () => {
                            showPage(i);
                        };

                        pagination.appendChild(btn);
                    }
                }

                if (rows.length > 0) {
                    createPagination();
                    showPage(1);
            }
            }

            document.addEventListener("DOMContentLoaded", function () {

                setupPagination("history-body", "history-pagination", 10);
                

            });

        </script>
    </body>
</html>