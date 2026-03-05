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
                    <tbody>
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
                    <tbody>
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
            </c:if>
        </section>
    </main>
</div>

</body>
</html>