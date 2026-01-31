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
                            <p>Total Sales</p>
                            <h2>1,284</h2>
                        </div>
                        <div class="card">
                            <p>Total Revenue</p>
                            <h2>$42,500</h2>
                        </div>
                        <div class="card">
                            <p>Active Users</p>
                            <h2>856</h2>
                        </div>
                        <div class="card">
                            <p>Avg Rating</p>
                            <h2>4.85</h2>
                        </div>
                    </div>

                    <!-- Chart -->
                    <div class="box">
                        <h3>Revenue Performance</h3>
                        <div id="chart"></div>
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
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${top3revenue}">
                                    <tr>
                                        <td class="product-name">${item.getName()}</td>
                                        <td>${item.getDownloadCount()}</td>
                                        <td class="revenue">${item.getRevenue()}</td>
                                        <td>
                                             ${item.getAvg_rating()}
                                        </td>
                                        <td>
                                            <span class="status approved">APPROVED</span>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                </section>
            </div>
        </div>

        <!-- React Chart -->
        <script type="text/babel" src="${pageContext.request.contextPath}/assets/vendor/js/chart.jsx"></script>
    </body>
</html>
