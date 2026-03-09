<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Vendor Dashboard</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/vendor/vendor.css">
    </head>
    <body>

        <div class="layout">

            <!-- SIDEBAR -->
            <jsp:include page="layout/side_bar.jsp"/>

            <!-- MAIN CONTENT -->
            <div class="main">
                <h1>Vendor Dashboard</h1>
                <p class="subtitle">Welcome back, here’s what’s happening with your apps today.</p>

                <!-- TOP CARDS -->
                <div class="cards">

                    <!-- Card 1 -->
                    <div class="card">
                        <div class="card-icon success">
                            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <line x1="12" y1="1" x2="12" y2="23"></line>
                            <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path>
                            </svg>
                        </div>
                        <p class="card-title">Revenue</p>
                        <h2>${sumRevenue*85/100}</h2>
                        <span class="success-text">After 15% commission</span>
                    </div>

                    <div class="card">
                        <div class="card-icon info">
                            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v4"></path>
                            <polyline points="7 10 12 15 17 10"></polyline>
                            <line x1="12" y1="15" x2="12" y2="3"></line>
                            </svg>
                        </div>
                        <p class="card-title">Total Download</p>
                        <h2>${sumDownloadApps}</h2>
                        <span class="info-text">Across all product</span>
                    </div>

                    <div class="card">
                        <div class="card-icon warning">
                            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"></polygon>
                            </svg>
                        </div>
                        <p class="card-title">Average Rating</p>
                        <h2>${avgRating}</h2>
                        <span class="warning-text">⭐⭐⭐⭐⭐</span>
                    </div>

                    <div class="card">
                        <div class="card-icon purple">
                            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"></path>
                            <polyline points="3.27 6.96 12 12.01 20.73 6.96"></polyline>
                            <line x1="12" y1="22.08" x2="12" y2="12"></line>
                            </svg>
                        </div>
                        <p class="card-title">Total Active Apps</p>
                        <h2>${sumApprovedApps}</h2>
                        <span class="purple-text">${sumApprovedApps} total</span>
                    </div>

                </div>

                <!-- PROGRESS + QUALITY -->
                <div class="grid-2">

                    <!-- Revenue Chart -->
                    <div class="panel">
                        <div class="box">
                            <h3>Revenue Performance (Last 4 Weeks)</h3>
                            <canvas id="revenueChart" height="140"></canvas>
                        </div>
                    </div>

                    <!-- Download Chart -->
                    <div class="panel">
                        <div class="box">
                            <h3>Download Performance (Last 4 Weeks)</h3>
                            <canvas id="downloadChart" height="140"></canvas>
                        </div>
                    </div>

                </div>
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
                                        <span class="status ${fn:toLowerCase(item.status)}">${item.status}</span>
                                    </td>
                                    <td>
                                        <a 
                                            href="product_detail?softwareId=${item.softwareId}" 
                                            class="btn-action">
                                            View detail
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>

                    </table>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script>
            const revenueLabels = [];
            const revenueData = [];

            <c:forEach var="e" items="${revenueByWeek}">
            revenueLabels.push("Week ${e.key}");
            revenueData.push(${e.value});
            </c:forEach>

            new Chart(document.getElementById('revenueChart'), {
                type: 'line',
                data: {
                    labels: revenueLabels,
                    datasets: [{
                            label: 'Revenue ($)',
                            data: revenueData,
                            borderWidth: 1,
                            tension: 0.4,
                        }]
                },
                options: {
                    scales: {
                        y: {beginAtZero: true}
                    }
                }
            });
        </script>

        <script>
            const downloadLabels = [];
            const downloadData = [];

            <c:forEach var="e" items="${downloadByWeek}">
            downloadLabels.push("Week ${e.key+1}");
            downloadData.push(${e.value});
            </c:forEach>

            new Chart(document.getElementById('downloadChart'), {
                type: 'bar',
                data: {
                    labels: downloadLabels,
                    datasets: [{
                            label: 'Downloads',
                            data: downloadData,
                            borderWidth: 2,
                            tension: 0.3,
                        }]
                },
                options: {
                    scales: {
                        y: {beginAtZero: true}
                    }
                }
            });
        </script>

    </body>
</html>
