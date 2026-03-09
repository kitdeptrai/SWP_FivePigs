<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : product_detail
    Created on : Feb 16, 2026, 10:19:45 PM
    Author     : MinhPD
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Product Detail</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/vendor/vendor.css">
        <style>


            /* Product Header */
            .product-header{
                background:linear-gradient(180deg, #1f2937, #111827);
                padding:25px;
                border:1px solid #1e293b;
                border-radius:16px;
                display:flex;
                gap:20px;
                align-items:center;
                margin-bottom:30px;
            }
            .product-header img{
                width:130px;
                height:130px;
                border-radius:12px;
                object-fit:cover;
            }
            .product-info{
                flex:1
            }
            .product-title{
                font-size:30px;
                font-weight:750;
                margin-bottom:8px
            }
            .product-meta{
                color:#9ca3af;
                font-size:14px;
                margin-bottom:10px
            }
            .product-price{
                display:flex;
                gap:20px;
                font-size:18px
            }
            .badge{
                background:#064e3b;
                color:#22c55e;
                padding:6px 12px;
                border-radius:20px;
                font-size:12px;
            }

            /* Charts */
            .charts{
                display:grid;
                grid-template-columns:1fr 1fr;
                gap:25px;
                margin-bottom:30px;
            }
            .chart-card{
                background:#1e293b;
                padding:20px;
                border-radius:16px;
            }

            /* Rating */
            .rating-box{
                background:#1e293b;
                padding:25px;
                border-radius:16px;
            }
            .rating-row{
                display:flex;
                align-items:center;
                gap:15px;
                margin-bottom:10px;
            }
            .bar{
                flex:1;
                height:8px;
                background:#334155;
                border-radius:5px;
                overflow:hidden;
            }
            .bar-fill{
                height:100%;
                background:#6366f1;
            }

            /* Details */
            .details {
                margin-top: 40px;
            }

            /* Card chung */
            .detail-card {
                background: linear-gradient(145deg, #0f1b2d, #0c1626);
                border: 1px solid #1f2a3a;
                border-radius: 18px;
                padding: 28px 32px;
                margin-bottom: 28px;
                box-shadow: 0 10px 25px rgba(0, 0, 0, 0.35);
                transition: 0.3s ease;
            }

            .detail-card:hover {
                transform: translateY(-3px);
                border-color: #6366f1;
                box-shadow: 0 15px 35px rgba(99, 102, 241, 0.15);
            }

            .detail-card h3 {
                font-size: 18px;
                font-weight: 600;
                margin-bottom: 25px;
                color: #f1f5f9;
            }

            /* Grid thông tin */
            .details-grid {
                display: grid;
                grid-template-columns: repeat(2, 1fr);
                gap: 28px 60px;
            }

            .info-item span {
                display: block;
                font-size: 13px;
                color: #94a3b8;
                margin-bottom: 6px;
            }

            .info-item p {
                font-size: 16px;
                font-weight: 600;
                color: #ffffff;
            }

            /* Description + Requirement text */
            .detail-text {
                font-size: 15px;
                line-height: 1.8;
                color: #cbd5e1;
            }
            .details-grid{
                display:grid;
                grid-template-columns:1fr 1fr;
                gap:20px;
                margin-bottom:20px;
            }
            .details span{
                color:#9ca3af;
                font-size:14px
            }
            .features li{
                margin-bottom:8px;
            }

            .tabs {
                display: flex;
                gap: 20px;
                margin: 25px 0;
                border-bottom: 1px solid #2e2e3e;
            }

            .tab {
                padding: 10px 18px;
                cursor: pointer;
                color: #9ca3af;
                font-weight: 500;
                transition: 0.3s;
            }

            .tab:hover {
                color: white;
            }

            .tab.active {
                color: white;
                border-bottom: 3px solid #6366f1;
            }

            .tab-content {
                display: none;
            }

            .tab-content.active {
                display: block;
            }

            .image-grid {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
                gap: 20px;
                margin-top: 20px;
            }

            .image-grid img {
                width: 100%;
                border-radius: 12px;
                transition: 0.3s;
                cursor: pointer;
            }

            .image-grid img:hover {
                transform: scale(1.05);
            }

            .review-panel {
                background: #1f2937;
                padding: 20px;
                border-radius: 12px;
            }

            .review-title {
                color: #fff;
                margin-bottom: 20px;
            }

            .review-item {
                display: flex;
                gap: 15px;
                padding: 20px 0;
                border-bottom: 1px solid #374151;
            }

            .avatar {
                width: 45px;
                height: 45px;
                border-radius: 50%;
                background: #374151;
                display: flex;
                align-items: center;
                justify-content: center;
                color: #9ca3af;
                font-size: 18px;
            }

            .review-user strong {
                color: #fff;
                margin-right: 10px;
            }

            .verified {
                background: #065f46;
                color: #10b981;
                padding: 3px 8px;
                border-radius: 20px;
                font-size: 12px;
            }

            .review-meta {
                margin-top: 5px;
                font-size: 13px;
                color: #9ca3af;
                display: flex;
                align-items: center;
                gap: 8px;
            }

            .stars {
                color: #facc15;
                font-size: 14px;
            }

            .review-content {
                margin-top: 10px;
                color: #e5e7eb;
            }

            .review-actions {
                margin-top: 10px;
                display: flex;
                gap: 20px;
                font-size: 13px;
                color: #9ca3af;
            }

            .review-actions span {
                cursor: pointer;
            }

            .review-actions span:hover {
                color: #fff;
            }

        </style>
    </head>

    <body>
        <div class="layout">

            <jsp:include page="layout/side_bar.jsp"/>

            <div class="main">

                <!-- HEADER -->
                <div class="product-header">
                    <img src="/${infoSoftware.softwareImage.imageUrl}">
                    <div class="product-info">
                        <div class="product-title">${infoSoftware.name}</div>
                        <div class="product-meta">v${infoSoftware.softwareVersion.versionName} ? ${infoSoftware.category.categoryName} 
                            ?${createdDateFormatted}</div>
                        <div class="product-price">
                            <div>$${infoSoftware.price}</div>
                            <div style="color:#22c55e">$${infoSoftware.price*85/100} (Your earning)</div>
                        </div>
                    </div>
                    <div class="badge">Approved</div>
                </div>

                <!-- STATS -->
                <div class="cards">
                    <div class="card">
                        <div class="card-icon success">
                            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <line x1="12" y1="1" x2="12" y2="23"></line>
                            <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path>
                            </svg>
                        </div>
                        <h2>$${revenue}</h2>
                        <p class="card-title">Total Revenue</p>
                    </div>
                    <div class="card">
                        <div class="card-icon info">
                            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v4"></path>
                            <polyline points="7 10 12 15 17 10"></polyline>
                            <line x1="12" y1="15" x2="12" y2="3"></line>
                            </svg>
                        </div>
                        <h2>${downloadCount}</h2>
                        <p class="card-title">Total Downloads</p>
                    </div>
                    <div class="card">
                        <div class="card-icon warning">
                            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"></polygon>
                            </svg>
                        </div>
                        <h2>${avgRating}/5.0</h2>
                        <p class="card-title">Average Rating</p>
                    </div>
                    <div class="card">
                        <div class="card-icon purple">
                            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#b38add" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M21 2l-2 2m-7.61 7.61a5.5 5.5 0 1 1-7.778 7.778 5.5 5.5 0 0 1 7.777-7.777zm0 0L15.5 7.5m0 0l3 3L22 7l-3-3-3.5 3.5z"></path>
                            </svg>
                        </div>
                        <h2>${totalLicense}</h2>
                        <p class="card-title">Active Licenses</p>
                    </div>
                </div>

                <div class="tabs">
                    <div class="tab active" onclick="switchTab('details', this)">Details</div>
                    <div class="tab" onclick="switchTab('images', this)">Images</div>
                    <div class="tab" onclick="switchTab('reviews', this)">Reviews</div>
                </div>

                <!-- DETAILS -->
                <div id="details" class="tab-content active">

                    <div class="details">

                        <!-- PRODUCT INFO -->
                        <div class="detail-card">
                            <h3>Product Information</h3>

                            <div class="details-grid">
                                <div class="info-item">
                                    <span>Product Name</span>
                                    <p>${infoSoftware.name}</p>
                                </div>

                                <div class="info-item">
                                    <span>Version</span>
                                    <p>v${infoSoftware.softwareVersion.versionName}</p>
                                </div>

                                <div class="info-item">
                                    <span>Category</span>
                                    <p>${infoSoftware.category.categoryName}</p>
                                </div>

                                <div class="info-item">
                                    <span>Created At</span>
                                    <p>${infoSoftware.createdAt.toLocalDate()}</p>
                                </div>
                            </div>
                        </div>

                        <!-- DESCRIPTION -->
                        <div class="detail-card">
                            <h3>Description</h3>
                            <p class="detail-text">
                                ${infoSoftware.softwareDetail.description}
                            </p>
                        </div>

                        <!-- SYSTEM REQUIREMENT -->
                        <div class="detail-card">
                            <h3>System Requirements</h3>
                            <p class="detail-text">
                                ${infoSoftware.softwareDetail.sysRequirement}
                            </p>
                        </div>

                    </div>

                </div>

                <div id="images" class="tab-content">

                    <div class="panel">
                        <h3>Product Images</h3>

                        <div class="image-grid">
                            <c:forEach var="item" items="${listImage}">
                                <img src="/${item.imageUrl}">
                            </c:forEach>
                            
                            
                        </div>

                    </div>


                </div>
                <div id="reviews" class="tab-content">

                    <div class="review-panel">
                        <h3 class="review-title">Customer Reviews</h3>

                        <c:forEach var="item" items="${listReview}">

                            <!-- Review Item -->
                            <div class="review-item">

                                <div class="review-left">
                                    <div class="avatar">
                                        <i class="fa fa-user"></i>
                                    </div>
                                </div>

                                <div class="review-body">

                                    <div class="review-top">
                                        <div class="review-user">
                                            <strong>${item.user.fullName}</strong>

                                        </div>
                                    </div>

                                    <div class="review-meta">
                                        <div class="stars">
                                            ${item.rating}★
                                        </div>
                                        <span class="dot">•</span>
                                        <span>${item.createdAt.toLocalDate()}</span>
                                        <span class="dot">•</span>
                                    </div>

                                    <div class="review-content">
                                        ${item.comment}
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                        <!-- Repeat Review -->

                    </div>
                </div>
            </div>
        </div>

        <script>
            function switchTab(tabId, element) {

                document.querySelectorAll('.tab').forEach(tab => {
                    tab.classList.remove('active');
                });

                document.querySelectorAll('.tab-content').forEach(content => {
                    content.classList.remove('active');
                });

                element.classList.add('active');
                document.getElementById(tabId).classList.add('active');
            }
        </script>



    </body>
</html>
