
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>FIVEPIGS - Corrected Layout</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

        <!-- font Noto Sans -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap"
              rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css">

                    </head>

                    <body>

                <jsp:include page="/WEB-INF/views/customer/sidebar.jsp">
                    <jsp:param name="activePage" value="home" />
                </jsp:include>

                <div class="main-content">
                    <jsp:include page="/WEB-INF/views/customer/header.jsp"></jsp:include>

            <div id="News" class="content-section active-section">
                <div class="fixed-layout-grid-news">
                    <div class="left-column">
                        <div class="featured-banner"
                             style="background-image: linear-gradient(to right, rgba(0,0,0,0.7), transparent), url('https://images.unsplash.com/photo-1513364776144-60967b0f800f?q=80&w=2071&auto=format&fit=crop');">
                            <h1 style="font-size: 32px; margin-bottom: 10px; line-height: 1.2;">ZOMBIE AND SURVIVE</h1>
                            <p style="opacity: 0.9; margin-bottom: 20px;">Sale of 90% for resident evil series</p>
                            <button
                                style="padding: 10px 25px; border: none; background: white; color: var(--primary-color);
                                font-weight: 700; border-radius: 8px; cursor: pointer; width: fit-content;">Visit now</button>
                        </div>
                        <div class="info-product">
                           <div class="info-header">
                            <p>Last updated date on Store product page</p>
                            <div>new</div>
                        </div>

                           <div class="info-body"><p>Microsoft Store product page now displays Last updated date in Additional Info section, making it easy for you to get visibility into a product's maintenance and release cadence. 
                            This date reflects when most recent package update became available 
                            to Microsoft Store users.</p></div>

                           <div class="info-footer">
                            <button>View Details</button>
                           </div>
                        </div> 
                        
                        <div class="info-product">
                           <div class="info-header">
                            <p>Last updated date on Store product page</p>
                            <div>new</div>
                        </div>

                           <div class="info-body"><p>Microsoft Store product page now displays Last updated date in Additional Info section, making it easy for you to get visibility into a product's maintenance and release cadence. 
                            This date reflects when most recent package update became available 
                            to Microsoft Store users.</p></div>

                           <div class="info-footer">
                            <button>View Details</button>
                           </div>
                        </div> 
                        
                        <div class="info-product">
                           <div class="info-header">
                            <p>Last updated date on Store product page</p>
                            <div>new</div>
                        </div>

                           <div class="info-body"><p>Microsoft Store product page now displays Last updated date in Additional Info section, making it easy for you to get visibility into a product's maintenance and release cadence. 
                            This date reflects when most recent package update became available 
                            to Microsoft Store users.</p></div>

                           <div class="info-footer">
                            <button>View Details</button>
                           </div>
                        </div> 
                        
                        <div class="info-product">
                           <div class="info-header">
                            <p>Last updated date on Store product page</p>
                            <div>new</div>
                        </div>

                           <div class="info-body"><p>Microsoft Store product page now displays Last updated date in Additional Info section, making it easy for you to get visibility into a product's maintenance and release cadence. 
                            This date reflects when most recent package update became available 
                            to Microsoft Store users.</p></div>

                           <div class="info-footer">
                            <button>View Details</button>
                           </div>
                        </div> 
                        
                        <div class="info-product">
                           <div class="info-header">
                            <p>Last updated date on Store product page</p>
                            <div>new</div>
                        </div>

                           <div class="info-body"><p>Microsoft Store product page now displays Last updated date in Additional Info section, making it easy for you to get visibility into a product's maintenance and release cadence. 
                            This date reflects when most recent package update became available 
                            to Microsoft Store users.</p></div>

                           <div class="info-footer">
                            <button>View Details</button>
                           </div>
                        </div> 
                        
                    </div>
                </div>
            </div>
            </div>

        <script type="text/javascript" src="/js/script.js"></script>
    </body>

</html>