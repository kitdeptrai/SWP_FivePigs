<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Approval Dashboard</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Approval/approval.css">
    </head>
    <body>

        <div class="app">
            <!-- SIDEBAR -->
            <jsp:include page="./layout/sidebar.jsp"/>

            <!-- MAIN -->
            <main class="main">
                <h1>Approval Dashboard</h1>
                <p class="subtitle">Monitor software submissions and marketplace health metrics</p>

                <!-- STATS -->
                <div class="stats">
                    <div class="card">
                        <span class="icon yellow">⏳</span>
                        <h2>${counts.pending}</h2>
                        <p>Pending Approval</p>
                        <small class="warn">Awaiting decision</small>
                    </div>

                    <div class="card">
                        <span class="icon green">✔</span>
                        <h2>${counts.approved}</h2>
                        <p>Approved</p>
                        <small class="ok">Today</small>
                    </div>

                    <div class="card">
                        <span class="icon red">✖</span>
                        <h2>${counts.rejected}</h2>
                        <p>Rejected</p>
                        <small class="danger">Today</small>
                    </div>
                </div>

                <!-- TABLE: Awaiting Approval -->
                <section class="panel">
                    <div class="panel-header">
                        <h3>Software Awaiting Approval</h3>

                        <!-- dùng <a> để khỏi lỗi underline -->
                        <a class="btn"
                           href="${pageContext.request.contextPath}/approval_pending?page=approval_pending">
                            View All
                        </a>
                    </div>

                    <c:if test="${empty pendingApp}">
                        <p class="empty">No pending software</p>
                    </c:if>

                    <c:if test="${not empty pendingApp}">
                        <table>
                            <thead>
                                <tr>
                                    <th>App Name</th>
                                    <th>Status</th>
                                    <th>Review Date</th>
                                </tr>
                            </thead>
                            <tbody id="pending-body">
                                <c:forEach var="item" items="${pendingApp}">
                                    <tr>
                                        <td class="col-app"><c:out value="${item.appName}"/></td>

                                        <td class="col-status">
                                            <span class="status pending">Pending</span>
                                        </td>

                                        <td class="col-date">
                                            <c:choose>
                                                <c:when test="${item.approvalProcess != null && item.approvalProcess.approval_date != null}">
                                                    <c:out value="${item.approvalProcess.approval_date}"/>
                                                </c:when>
                                                <c:otherwise>—</c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <div class="pagination" id="pending-pagination"></div>
                    </c:if>
                </section>

                <!-- TABLE: Recent Approval History -->
                <section class="panel">
                    <h3>Recent Approval History</h3>

                    <c:if test="${empty approvedApp}">
                        <p class="empty">No approval history</p>
                    </c:if>

                    <c:if test="${not empty approvedApp}">
                        <table>
                            <thead>
                                <tr>
                                    <th>App Name</th>
                                    <th>Status</th>
                                    <th>Approved Date</th>
                                </tr>
                            </thead>
                            <tbody id="approved-body">
                                <c:forEach var="item" items="${approvedApp}">
                                    <tr>
                                        <td class="col-app"><c:out value="${item.appName}"/></td>

                                        <td class="col-status">
                                            <c:choose>
                                                <c:when test="${item.status eq 'APPROVED'}">
                                                    <span class="status approved">Approved</span>
                                                </c:when>
                                                <c:when test="${item.status eq 'REJECTED'}">
                                                    <span class="status rejected">Rejected</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="status pending">Pending</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>

                                        <td class="col-date">
                                            <c:choose>
                                                <c:when test="${item.approvalProcess != null && item.approvalProcess.approval_date != null}">
                                                    <c:out value="${item.approvalProcess.approval_date}"/>
                                                </c:when>
                                                <c:otherwise>—</c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <div class="pagination" id="approved-pagination"></div>
                    </c:if>
                </section>
            </main>
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

                setupPagination("pending-body", "pending-pagination", 10);
                setupPagination("approved-body", "approved-pagination", 10);

            });

        </script>
    </body>
</html>