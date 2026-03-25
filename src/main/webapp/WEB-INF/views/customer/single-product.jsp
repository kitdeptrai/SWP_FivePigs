<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${detail.name} - FIVEPIGS</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/single-product.css?v=20260315a">
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

<c:if test="${not empty demoMsg}">
    <div style="margin:0 0 16px 0;padding:12px 14px;border-radius:12px;${demoMsg == 'already_owned' || demoMsg == 'already_used' ? 'background:#fff7e6;color:#9a6700;' : 'background:#ffecec;color:#9a2c2c;'}">
        <c:choose>
            <c:when test="${demoMsg == 'unavailable'}">Demo trial is not available right now for this product.</c:when>
            <c:when test="${demoMsg == 'already_owned'}">You already have access to this product, so there is no need to start a demo.</c:when>
            <c:when test="${demoMsg == 'already_used'}">You already used the demo trial for this product.</c:when>
            <c:otherwise>We could not start the demo trial right now.</c:otherwise>
        </c:choose>
    </div>
</c:if>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/customer/header.jsp"></jsp:include>

    <div class="content-scroll-area">
        <div class="back-btn" onclick="history.back()">
            <i class="fa-solid fa-arrow-left"></i> Back to Store
        </div>

        <div class="product-hero">
            <c:choose>
                <c:when test="${not empty icon}">
                    <img src="${pageContext.request.contextPath}/assets/${icon.imageUrl}" class="hero-icon" alt="${detail.name} Icon">
                </c:when>
                <c:otherwise>
                    <div class="hero-icon" style="display:flex;align-items:center;justify-content:center;background:#f5f6fb;">
                        <i class="fa-solid fa-cube" style="font-size:34px;color:#6b70ff;"></i>
                    </div>
                </c:otherwise>
            </c:choose>

            <div class="hero-info">
                <h1>${detail.name}</h1>
                <div class="hero-dev">
                    <c:choose>
                        <c:when test="${not empty detail.user.fullName}">${detail.user.fullName}</c:when>
                        <c:otherwise>Updating</c:otherwise>
                    </c:choose>
                    �
                    <c:choose>
                        <c:when test="${not empty detail.category.categoryName}">${detail.category.categoryName}</c:when>
                        <c:otherwise>Uncategorized</c:otherwise>
                    </c:choose>
                </div>

                <div class="hero-stats">
                    <span><i class="fa-solid fa-star"></i> ${avgRatingLabel}</span>
                    <span><i class="fa-solid fa-download"></i> ${downloadCountLabel} downloads</span>
                    <c:if test="${not empty detail.softwareVersion.versionName}">
                        <span><i class="fa-solid fa-code-branch"></i> v${detail.softwareVersion.versionName}</span>
                    </c:if>
                </div>

                <c:if test="${not empty pricingOptions}">
                    <form action="${pageContext.request.contextPath}/cart" method="POST" class="pricing-form">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="softwareId" value="${detail.softwareId}">
                        <input type="hidden" name="redirect" value="${pageContext.request.contextPath}/product?pid=${detail.softwareId}">

                        <div class="pricing-plan-grid">
                            <c:forEach var="plan" items="${pricingOptions}" varStatus="status">
                                <label class="pricing-plan-card">
                                    <input type="radio" name="pricingId" value="${plan.pricingId}" ${status.first ? 'checked' : ''}>
                                    <div class="pricing-plan-copy">
                                        <div class="pricing-plan-top">
                                            <strong>${plan.planName}</strong>
                                            <span>
                                                <c:choose>
                                                    <c:when test="${detail.isFree == 1 || plan.price == 0}">Free</c:when>
                                                    <c:otherwise>$${plan.price}</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </div>
                                        <div class="pricing-plan-meta">
                                            <span>${plan.maxUsers} user<c:if test="${plan.maxUsers != 1}">s</c:if></span>
                                            <span>
                                                <c:choose>
                                                    <c:when test="${empty plan.durationDays}">No expiry</c:when>

                                                </c:choose>
                                            </span>
                                        </div>
                                    </div>
                                </label>
                            </c:forEach>
                        </div>

                        <button type="submit" class="install-btn">Add To Cart</button>
                    </form>
                </c:if>

                <c:if test="${empty pricingOptions}">
                <form action="${pageContext.request.contextPath}/cart" method="POST" style="display:inline;">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="softwareId" value="${detail.softwareId}">
                    <input type="hidden" name="redirect" value="${pageContext.request.contextPath}/product?pid=${detail.softwareId}">
                    <button type="submit" class="install-btn">Add To Cart</button>
                </form>
                </c:if>

                <c:if test="${hasDemoPlan}">
                    <form action="${pageContext.request.contextPath}/trial/start" method="post" style="display:inline;">
                        <input type="hidden" name="softwareId" value="${detail.softwareId}">
                        <button type="submit" class="install-btn" style="background:#f1f2f6;color:#333;box-shadow:none;margin-left:10px;padding:12px 15px;display:inline-flex;align-items:center;gap:8px;">
                            <i class="fa-solid fa-play"></i> Try Demo
                        </button>
                    </form>
                </c:if>
            </div>

<%--            <div class="hero-report">--%>
<%--                <button type="submit" class="report-btn">Report </button>--%>
<%--            </div>--%>
        </div>

        <h3 style="margin-bottom: 15px; font-size: 20px;">Screenshots</h3>
        <div class="screenshot-scroller">
            <c:choose>
                <c:when test="${not empty screenshots}">
                    <c:forEach var="img" items="${screenshots}">
                        <img src="${pageContext.request.contextPath}/assets/${img.imageUrl}" alt="${detail.name} screenshot">
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div style="padding:24px;border-radius:18px;background:#fff;color:#666;min-width:280px;">No screenshots yet.</div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="product-details-grid">
            <div class="desc-box">
                <h3>Description</h3>
                <p class="desc-text">
                    <c:choose>
                        <c:when test="${not empty detail.softwareDetail.description}">${detail.softwareDetail.description}</c:when>
                        <c:when test="${not empty detail.shortDescription}">${detail.shortDescription}</c:when>
                        <c:otherwise>Product description is being updated.</c:otherwise>
                    </c:choose>
                </p>

                <c:if test="${not empty featureLines}">
                    <h4 style="margin-top: 22px; margin-bottom: 12px;">Features</h4>
                    <ul style="padding-left: 18px; color:#4b5563; line-height:1.8;">
                        <c:forEach var="feature" items="${featureLines}">
                            <li>${feature}</li>
                        </c:forEach>
                    </ul>
                </c:if>

                <div class="review-summary-card">
                    <div class="review-summary-header">
                        <h3>Ratings and reviews</h3>
                        <span>${reviewCount} ratings</span>
                    </div>
                    <div class="review-summary-body">
                        <div class="review-score-main">
                            <div class="review-score-value">${avgRatingLabel}</div>
                            <div class="review-score-count">${reviewCount} ratings</div>
                        </div>
                        <div class="review-breakdown">
                            <c:forEach var="entry" items="${ratingBreakdown}">
                                <c:set var="percent" value="${reviewCount == 0 ? 0 : (entry.value * 100 / reviewCount)}" />
                                <div class="review-breakdown-row">
                                    <span>${entry.key} <i class="fa-solid fa-star"></i></span>
                                    <div class="review-breakdown-bar">
                                        <div class="review-breakdown-fill" style="width:${percent}%;"></div>
                                    </div>
                                    <strong>${entry.value}</strong>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="review-cta-row">
                        <div class="review-stars-preview">
                            <i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i>
                            <span>
                                <c:choose>
                                    <c:when test="${canReview and not alreadyReviewed}">You own this product. Share your experience.</c:when>
                                    <c:when test="${alreadyReviewed}">You already reviewed this product.</c:when>
                                    <c:otherwise>Purchase this product before leaving a review.</c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                    </div>
                </div>

                <c:if test="${not empty reviewMsg}">
                    <div class="review-feedback ${reviewMsg == 'added' ? 'success' : 'error'}">
                        <c:choose>
                            <c:when test="${reviewMsg == 'added'}">Your review was submitted successfully.</c:when>
                            <c:when test="${reviewMsg == 'exists'}">You already reviewed this product.</c:when>
                            <c:when test="${reviewMsg == 'not_owned'}">You need to own this product before reviewing it.</c:when>
                            <c:when test="${reviewMsg == 'invalid_rating'}">Please choose a rating from 1 to 5 stars.</c:when>
                            <c:when test="${reviewMsg == 'empty_comment'}">Please enter a short review comment.</c:when>
                            <c:otherwise>Unable to submit review.</c:otherwise>
                        </c:choose>
                    </div>
                </c:if>

                <c:if test="${canReview and not alreadyReviewed}">
                    <form class="review-form" action="${pageContext.request.contextPath}/review" method="post">
                        <input type="hidden" name="softwareId" value="${detail.softwareId}">
                        <div class="review-form-row">
                            <label for="rating">Your rating</label>
                            <select id="rating" name="rating" required>
                                <option value="">Select stars</option>
                                <option value="5">5 stars</option>
                                <option value="4">4 stars</option>
                                <option value="3">3 stars</option>
                                <option value="2">2 stars</option>
                                <option value="1">1 star</option>
                            </select>
                        </div>
                        <div class="review-form-row">
                            <label for="comment">Your review</label>
                            <textarea id="comment" name="comment" rows="4" maxlength="1000" placeholder="Write what you liked, what could be better, and whether you would recommend it." required></textarea>
                        </div>
                        <button type="submit" class="review-submit-btn">Write a review</button>
                    </form>
                </c:if>

                <div class="review-list">
                    <c:choose>
                        <c:when test="${not empty reviews}">
                            <c:forEach var="review" items="${reviews}">
                                <div class="review-card-item">
                                    <div class="review-card-header">
                                        <div class="review-user-chip">
                                            <div class="review-user-avatar">${fn:substring(review.user.fullName, 0, 1)}</div>
                                            <div>
                                                <strong>${review.user.fullName}</strong>
                                            </div>
                                        </div>
                                        <span class="review-card-rating"><i class="fa-solid fa-star"></i> ${review.rating}.0</span>
                                    </div>
                                    <p>${review.comment}</p>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="review-card-item review-empty">No reviews yet.</div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="specs-box">
                <h4 style="margin-bottom: 20px;">Information</h4>
                <div class="spec-row">
                    <span class="spec-label">Version</span>
                    <span class="spec-value">
                        <c:choose>
                            <c:when test="${not empty detail.softwareVersion.versionName}">${detail.softwareVersion.versionName}</c:when>
                            <c:when test="${not empty detail.softwareDetail.version}">${detail.softwareDetail.version}</c:when>
                            <c:otherwise>Updating</c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <div class="spec-row">
                    <span class="spec-label">Update Date</span>
                    <span class="spec-value">${updateDateLabel}</span>
                </div>
                <div class="spec-row">
                    <span class="spec-label">Size</span>
                    <span class="spec-value">${fileSizeLabel}</span>
                </div>
                <div class="spec-row">
                    <span class="spec-label">Developer</span>
                    <span class="spec-value" style="color:var(--primary-color)">
                        <c:choose>
                            <c:when test="${not empty detail.user.fullName}">${detail.user.fullName}</c:when>
                            <c:otherwise>Updating</c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <div class="spec-row">
                    <span class="spec-label">Category</span>
                    <span class="spec-value">
                        <c:choose>
                            <c:when test="${not empty detail.category.categoryName}">${detail.category.categoryName}</c:when>
                            <c:otherwise>Uncategorized</c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <div class="spec-row">
                    <span class="spec-label">System Requirements</span>
                    <span class="spec-value">
                        <c:choose>
                            <c:when test="${not empty detail.softwareDetail.sysRequirement}">${detail.softwareDetail.sysRequirement}</c:when>
                            <c:otherwise>Updating</c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    function closeCartModal() {
        var modal = document.getElementById("cartModalOverlay");
        if (modal) modal.style.display = "none";
    }
</script>
</body>
</html>



