<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title>Vendor Dashboard</title>

        <!-- CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/vendor_dashboard.css">

        <!-- React CDN -->
        <script src="https://unpkg.com/react@18/umd/react.development.js"></script>
        <script src="https://unpkg.com/react-dom@18/umd/react-dom.development.js"></script>
        <script src="https://unpkg.com/babel-standalone@6/babel.min.js"></script>
    </head>

    <body>
        <div class="layout">

            <%@ include file="./layout/sidebar.jsp" %>

            <div class="main">
                <%@ include file="./layout/header.jsp" %>

                <section class="content">

                    <!-- Cards -->
                    <div class="cards">
                        <div class="card">
                            <p>Approved Apps</p>
                            <h2>${sumApprovedApps}</h2>
                        </div>
                        <div class="card">
                            <p>Pending Apps</p>
                            <h2>${sumPendingApps}</h2>
                        </div>
                        <div class="card">
                            <p>Total Revenue</p>
                            <h2>$${sumRevenue}</h2>
                        </div>

                        <div class="card">
                            <p>Avg Rating</p>
                            <h2>${avgRating}</h2>
                        </div>
                    </div>

                    <!-- Chart -->
                    <div class="box">
                        <h3>Revenue Performance (Last 4 Weeks)</h3>

                        <!-- Chart container -->
                        <canvas id="revenueChart" height="120"></canvas>
                    </div>

                    <!-- Table -->
                    <div class="table-card">
                        <div class="table-header">
                            <h3>Top Performing Apps</h3>
                            <a href="#" class="view-all">View All Products</a>
                        </div>

                        <table class="dashboard-table">
                            <thead>
                                <tr>
                                    <th>Product</th>
                                    <th>Downloads</th>
                                    <th>Revenue</th>
                                    <th>Rating</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${top3revenue}">
                                    <tr>
                                        <td class="product-name">${item.name}</td>
                                        <td>${item.downloadCount}</td>
                                        <td class="revenue">$${item.revenue}</td>
                                        <td>${item.avgRating}</td>
                                        <td>
                                            <span class="status approved">APPROVED</span>
                                        </td>
                                        <td>
                                            <a 
                                                href="#" 
                                                class="btn-action">
                                                View detail
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>

                        </table>
                    </div>

                </section>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script>
            const labels = [];
            const revenues = [];

            <c:forEach var="entry" items="${revenueByWeek}">
            labels.push("Week ${entry.key}");
            revenues.push(${entry.value});
            </c:forEach>

            const ctx = document.getElementById('revenueChart').getContext('2d');
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                            label: 'Revenue ($)',
                            data: revenues,
                            borderWidth: 1
                        }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        </script>

        <!-- React Chart -->

        <script type="text/babel" src="${pageContext.request.contextPath}/assets/vendor/js/chart.jsx"></script>
    </body>
</html>
