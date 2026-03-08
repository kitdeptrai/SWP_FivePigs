<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : my_products
    Created on : Feb 16, 2026, 4:17:45 PM
    Author     : MinhPD
--%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>My Products</title>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/vendor/vendor.css">
        <style>
            .header{
                display:flex;
                justify-content:space-between;
                align-items:center;
                margin-bottom:30px;
            }



            .btn-primary{
                background:#4f46e5;
                padding:10px 18px;
                border-radius:10px;
                text-decoration:none;
                color:white;
                font-weight:500;
                transition:0.2s;
            }

            .btn-primary:hover{
                background:#4338ca;
            }

            /* ================= PRODUCT CARD ================= */

            .products {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 30px;
                padding: 30px 0;
            }

            /* CARD */
            .product-card {
                background: linear-gradient(145deg, #1e293b, #0f172a);
                padding: 24px;
                border-radius: 20px;
                display: flex;
                gap: 24px;
                position: relative;
                border: 1px solid rgba(255,255,255,0.05);
                transition: all 0.35s ease;
            }

            /* hover effect */
            .product-card:hover {
                transform: translateY(-6px);
                box-shadow: 0 15px 40px rgba(0,0,0,0.5);
                border: 1px solid rgba(99,102,241,0.4);
            }

            /* IMAGE */
            .product-card img {
                width: 130px;
                height: 110px;
                object-fit: cover;
                border-radius: 14px;
                box-shadow: 0 8px 20px rgba(0,0,0,0.4);
                transition: 0.3s;
            }

            .product-card:hover img {
                transform: scale(1.05);
            }

            /* INFO */
            .product-info {
                flex: 1;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
            }

            .product-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .product-title {
                font-size: 19px;
                font-weight: 600;
                color: #f3f4f6;
            }

            /* DESCRIPTION */
            .product-desc {
                font-size: 14px;
                color: #94a3b8;
                margin: 8px 0 16px 0;
            }

            /* STATS */
            .product-stats {
                display: flex;
                gap:40px;
                font-size: 14px;
                color: #cbd5e1;
                line-height: 25px;
            }

            .product-stats div{
                width:30%;
            }
            /* BUTTON */

            .product-desc{
                font-size:14px;
                color:#9ca3af;
                margin:8px 0 15px 0;
            }

            .product-stats{
                display:flex;
                gap:25px;
                font-size:14px;
                margin-bottom:15px;
            }

            .product-stats span{
                color:#9ca3af;
            }

            .revenue{
                color:#22c55e;
                font-weight:600;
            }

            .actions{
                display:flex;
                gap:10px;
            }

            .btn-outline{
                flex:1;
                text-align:center;
                padding:8px;
                border:1px solid #334155;
                border-radius:10px;
                text-decoration:none;
                color:#fff;
                font-size:14px;
                transition:0.2s;
            }

            .btn-outline:hover{
                background:#334155;
            }

            .icon-btn{
                width:36px;
                height:36px;
                border-radius:8px;
                background:#0f172a;
                border:1px solid #334155;
                display:flex;
                justify-content:center;
                align-items:center;
                cursor:pointer;
            }

            .icon-btn:hover{
                background:#334155;
            }
        </style>
    </head>

    <body>
        <div class="layout">
            <!-- SIDEBAR -->
            <jsp:include page="layout/side_bar.jsp"/>

            <!-- MAIN -->
            <div class="main">

                <div class="header">
                    <div>
                        <h1>My Products</h1>
                        <p class="subtitle">Manage your software products and uploads</p>
                    </div>                   
                    <a href="/vendor/upload_product" class="btn-primary">+ Upload New Product</a>
                </div>

                <!-- Stats -->
                <div class="cards">
                    <div class="card">
                        <div class="card-icon info">
                            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                            <polyline points="14 2 14 8 20 8"></polyline>
                            <polyline points="8 13 6 15 8 17"></polyline>
                            <polyline points="16 13 18 15 16 17"></polyline>
                            </svg>
                        </div>
                        <h2>${totalApps}</h2>
                        <p class="card-title">Total Products</p>                        
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
                        <h2>${activeApps}</h2>   
                        <p class="card-title">Active Products</p>                                  
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
                        <h2>${pendingApps}</h2>   
                        <p class="card-title">Pending Review</p>      
                    </div>

                    <div class="card">
                        <div class="card-icon success">
                            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <line x1="12" y1="1" x2="12" y2="23"></line>
                            <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path>
                            </svg>
                        </div> 
                        <h2>$${revenueByVendor}</h2>                        
                        <p class="card-title">Total Revenue</p>                      
                    </div>
                </div>

                <!-- Products -->
                <div class="products">
                    <c:forEach var="item" items="${softwareCardList}">
                        <div class="product-card">
                            <img src="/${item.softwareImage.imageUrl}" />
                            <div class="product-info">
                                <div class="product-header">
                                    <div class="product-title">${item.name}</div>
                                    <div class="status approved">${item.status}</div>
                                </div>
                                <div class="product-desc">
                                    ${item.shortDescription}
                                </div>
                                <div class="product-stats">
                                    <div><span>Downloads</span><br>

                                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#ffffff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                        <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                                        <polyline points="7 10 12 15 17 10"></polyline>
                                        <line x1="12" y1="15" x2="12" y2="3"></line>
                                        </svg>
                                        ${item.downloadCount}

                                    </div>
                                    <div><span>Rating</span><br>

                                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#f1c40f" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                        <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"></polygon>
                                        </svg>
                                        ${item.avgRating}

                                    </div>
                                    <div><span>Revenue</span><div class="revenue">$${item.revenue}</div></div>
                                </div>
                                <div class="actions">
                                    <a href="/vendor/product_detail?softwareId=${item.softwareId}" class="btn-outline">View Details</a>
                                    <div class="icon-btn"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#ffffff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                        <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                                        <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                                        </svg></div>
                                    <div class="icon-btn"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#ff4d4d" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                        <polyline points="3 6 5 6 21 6"></polyline>
                                        <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                                        <line x1="10" y1="11" x2="10" y2="17"></line>
                                        <line x1="14" y1="11" x2="14" y2="17"></line>
                                        </svg></div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                </div>

            </div>
        </div>
    </body>
</html>
