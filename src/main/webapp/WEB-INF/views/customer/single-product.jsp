<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/single-product.css">

    </head>
    <body>
        <c:if test="${param.msg == 'added' || param.msg == 'exists'}">
            <div id="cartModalOverlay" class="cart-modal-overlay">
                <div class="cart-modal">
                    <button type="button" class="cart-modal-close" onclick="closeCartModal()">
                        <i class="fa-solid fa-xmark"></i>
                    </button>
                    <h3>
                        <c:choose>
                            <c:when test="${param.msg == 'added'}">ADDED TO CART</c:when>
                            <c:otherwise>ALREADY IN CART</c:otherwise>
                        </c:choose>
                    </h3>
                    <p>
                        <c:choose>
                            <c:when test="${param.msg == 'added'}">${detail.name} was added to cart.</c:when>
                            <c:otherwise>${detail.name} is already in cart or owned in library.</c:otherwise>
                        </c:choose>
                    </p>
                    <div class="cart-modal-actions">
                        <button type="button" class="cart-modal-btn ghost" onclick="closeCartModal()">Continue Shopping</button>
                        <a href="${pageContext.request.contextPath}/cart" class="cart-modal-btn primary">Open Cart</a>
                    </div>
                </div>
            </div>
        </c:if>

                <jsp:include page="/WEB-INF/views/customer/sidebar.jsp">
                    <jsp:param name="activePage" value="home" />
                </jsp:include>

                <div class="main-content">
                    <jsp:include page="/WEB-INF/views/customer/header.jsp"></jsp:include>

                <div class="content-scroll-area"> <div class="back-btn" onclick="history.back()">
                        <i class="fa-solid fa-arrow-left"></i> Back to Store
                    </div>

                    <div class="product-hero">
                        <c:choose>
                          <c:when test="${not empty icon}">
                            <img src="${pageContext.request.contextPath}/assets/${icon.imageUrl}"
                                 class="hero-icon" alt="${detail.name} Icon">
                          </c:when>
                        </c:choose>

                    <div class="hero-info">
                        <h1>${detail.name}</h1>
                        <div class="hero-dev">Mojang Studios • Action & Adventure</div>

                        <div class="hero-stats">
                            <span><i class="fa-solid fa-star"></i> 4.5 (12M Đánh giá)</span>
                            <span><i class="fa-solid fa-download"></i> 100M+ Tải xuống</span>
                            <span><i class="fa-solid fa-check-circle"></i> Editors' Choice</span>
                        </div>

                        <!-- ADD TO CART -->
                        <form action="${pageContext.request.contextPath}/cart" method="POST" style="display:inline;">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="softwareId" value="${detail.softwareId}">
                            <input type="hidden" name="redirect" value="${pageContext.request.contextPath}/product?pid=${detail.softwareId}">
                            <button type="submit" class="install-btn">
                                Add To Cart
                            </button>
                        </form>

                        <a href="${pageContext.request.contextPath}/cart" class="install-btn" style="text-decoration:none;background:#f1f2f6;color:#333;box-shadow:none;margin-left:10px;padding:12px 15px;display:inline-flex;align-items:center;gap:8px;">
                            <i class="fa-solid fa-cart-shopping"></i> Go To Cart
                        </a>

                    </div>
                </div>

                <h3 style="margin-bottom: 15px; font-size: 20px;">Screenshots</h3>
                <div class="screenshot-scroller">
                  <c:forEach var="img" items="${screenshots}">
                    <img src="${pageContext.request.contextPath}/assets/${img.imageUrl}"
                         alt="${detail.name} screenshot">
                  </c:forEach>


                </div>

                <div class="product-details-grid">

                    <div class="desc-box">
                        <h3>Description</h3>
                        <p class="desc-text">
                            Explore infinite worlds and build everything from the simplest of homes to the grandest of castles. Play in creative mode with unlimited resources or mine deep into the world in survival mode, crafting weapons and armor to fend off dangerous mobs.
                            <br><br>
                            <strong>Features:</strong><br>
                            - Market Place: Discover the latest community creations in the marketplace!<br>
                            - Slash Commands: Tweak how the game plays: you can give items away, summon mobs, change the time of day, and more.<br>
                            - Add-Ons: Customize your experience even further with free Add-Ons!
                        </p>

                        <h3 style="margin-top: 30px;">Ratings and reviews</h3>
                        <div style="background: white; padding: 20px; border-radius: 12px; margin-bottom: 15px; box-shadow: 0 2px 5px rgba(0,0,0,0.02);">
                            <div style="display:flex; justify-content:space-between; margin-bottom:5px;">
                                <div style="display:flex; gap:10px; align-items:center;">
                                    <img src="https://ui-avatars.com/api/?name=John+Doe&background=random" style="width:30px; height:30px; border-radius:50%;">
                                    <strong>John Doe</strong>
                                </div>
                                <span style="color:gold;"><i class="fa-solid fa-star"></i> 5.0</span>
                            </div>
                            <p style="font-size:14px; color:#555; margin-top: 5px;">Game tuyệt vời, chơi từ hồi bé đến giờ vẫn không chán!</p>
                        </div>
                    </div>

                    <div class="specs-box">
                        <h4 style="margin-bottom: 20px;">Information</h4>
                        <div class="spec-row">
                            <span class="spec-label">Version</span>
                            <span class="spec-value">1.21.0</span>
                        </div>
                        <div class="spec-row">
                            <span class="spec-label">Update Date</span>
                            <span class="spec-value">Feb 02, 2026</span>
                        </div>
                        <div class="spec-row">
                            <span class="spec-label">Size</span>
                            <span class="spec-value">1.5 GB</span>
                        </div>
                        <div class="spec-row">
                            <span class="spec-label">Developer</span>
                            <span class="spec-value" style="color:var(--primary-color)">Mojang Studios</span>
                        </div>
                        <div class="spec-row">
                            <span class="spec-label">Category</span>
                            <span class="spec-value">Adventure</span>
                        </div>
                        <div class="spec-row">
                            <span class="spec-label">Language</span>
                            <span class="spec-value">English, Vietnamese</span>
                        </div>
                    </div>
                                </div> </div> </div>

        <script>
            function closeCartModal() {
                var modal = document.getElementById("cartModalOverlay");
                if (modal) modal.style.display = "none";
            }
        </script>
    </body>
</html>





