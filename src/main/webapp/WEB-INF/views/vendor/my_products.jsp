
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>


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
            .pagination{
                display:flex;
                justify-content:center;
                gap:10px;
                margin-top:30px;
            }

            .pagination button{
                padding:6px 12px;
                border:none;
                background:#1e293b;
                color:white;
                border-radius:6px;
                cursor:pointer;
            }

            .pagination button.active{
                background:#4f46e5;
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
                                    <div class="status ${fn:toLowerCase(item.status)}">${item.status}</div>

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
                                    <c:choose>

                                        
                                        <c:when test="${item.status == 'ACTIVE'}">
                                            <form action="change_status_product" method="post">
                                                <input type="hidden" name="softwareId" value="${item.softwareId}">
                                                <input type="hidden" name="status" value="DEACTIVE">

                                                <button type="submit" class="icon-btn" title="Deactivate">
                                                    <svg width="24" height="24" viewBox="0 0 24 24"
                                                         fill="none" stroke="#ff4d4d" stroke-width="2"
                                                         stroke-linecap="round" stroke-linejoin="round">
                                                    <circle cx="12" cy="12" r="10"></circle>
                                                    <line x1="8" y1="12" x2="16" y2="12"></line>
                                                    </svg>
                                                </button>
                                            </form>
                                        </c:when>

                                        
                                        <c:when test="${item.status == 'DEACTIVE'}">
                                            <form action="change_status_product" method="post">
                                                <input type="hidden" name="softwareId" value="${item.softwareId}">
                                                <input type="hidden" name="status" value="ACTIVE">

                                                <button type="submit" class="icon-btn" title="Activate">
                                                    <svg width="24" height="24" viewBox="0 0 24 24"
                                                         fill="none" stroke="#22c55e" stroke-width="2"
                                                         stroke-linecap="round" stroke-linejoin="round">
                                                    <circle cx="12" cy="12" r="10"></circle>
                                                    <polyline points="9 12 12 15 16 9"></polyline>
                                                    </svg>
                                                </button>
                                            </form>
                                        </c:when>

                                    </c:choose>

                                </div>
                            </div>
                        </div>
                    </c:forEach>

                </div>
                <div class="pagination" id="pagination"></div>

            </div>
        </div>
        <script>
            const itemsPerPage = 8;

            const products = document.querySelectorAll(".product-card");
            const pagination = document.getElementById("pagination");

            const totalPages = Math.ceil(products.length / itemsPerPage);

            function showPage(page) {

                const start = (page - 1) * itemsPerPage;
                const end = start + itemsPerPage;

                products.forEach((card, index) => {
                    if (index >= start && index < end) {
                        card.style.display = "flex";
                    } else {
                        card.style.display = "none";
                    }
                });

                document.querySelectorAll(".pagination button").forEach(btn => {
                    btn.classList.remove("active");
                });

                document.getElementById("page-" + page).classList.add("active");
            }

            function createPagination() {

                for (let i = 1; i <= totalPages; i++) {

                    const btn = document.createElement("button");
                    btn.innerText = i;
                    btn.id = "page-" + i;

                    btn.onclick = () => showPage(i);

                    pagination.appendChild(btn);
                }
            }

            createPagination();
            showPage(1);
        </script>
    </body>
</html>
