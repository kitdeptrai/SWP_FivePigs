<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Approval Dashboard</title>
        <link rel="stylesheet" href="assets/css/Approval/approval.css">
    </head>
    <body>

        <div class="app">
            <!-- SIDEBAR -->
            <aside class="sidebar">
                <div class="logo">
                    <img src="assets/css/images/logo.png" alt="Pig Logo">
                    <div>
                        <h2 class="prjName">Fivepigs</h2>
                        <small class="subti">Software Market</small>
                    </div>
                </div>

                <ul class="menu">
                    <li class="active">Dashboard</li>
                    <li>Pending Approval</li>
                    <li>Approval History</li>
                    <li>Reports</li>
                </ul>

                <div class="user">
                    <div class="avatar">AP</div>
                    <div>
                        <strong>Tuan Thanh</strong>
                        <br><small>Approval</small></br>
                    </div>
                </div>

                <form action="<%= request.getContextPath() %>/logout" method="get">
                    <button type="submit" class="logout">Logout</button>
                </form>

            </aside>

            <!-- MAIN -->
            <main class="main">
                <h1>Approval Dashboard</h1>
                <p class="subtitle">Monitor software submissions and marketplace health metrics</p>

                <!-- STATS -->
                <div class="stats">
                    <div class="card">
                        <span class="icon yellow">⏳</span>
                        <h2>0</h2>
                        <p>Pending Approval</p>
                        <small class="warn">Awaiting decision</small>
                    </div>

                    <div class="card">
                        <span class="icon green">✔</span>
                        <h2>1</h2>
                        <p>Approved</p>
                        <small class="ok">Today</small>
                    </div>

                    <div class="card">
                        <span class="icon red">✖</span>
                        <h2>1</h2>
                        <p>Rejected</p>
                        <small class="danger">Today</small>
                    </div>
                </div>

                <!-- TABLE -->
                <section class="panel">
                    <div class="panel-header">
                        <h3>Software Awaiting Approval</h3>
                        <button class="btn">View All</button>
                    </div>
                    <p class="empty">No pending software</p>
                </section>



                <section class="panel">
                    <h3>Recent Approval History</h3>


                    <table>
                        <thead>
                            <tr>
                                <th>App Name</th>
                                <th>Status</th>
                                <th>Approved Date</th>
                                <th>Recommendation</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${approvedApp}">
                                <tr>
                                    <td class="col-app">${item.appName}</td>
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

                                    <td class="col-date">${item.reviewDate}</td>

                                    <td class="col-recommend">${item.recommendation}</td>
                                </tr>
                            </c:forEach>
                        </tbody>

                    </table>
                </section>
            </main>
        </div>

    </body>
</html>
