<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="activeMenu" value="pending" />

<!DOCTYPE html>
<html>
    <head>
        <title>Pending Reviews</title>

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/assets/reviewer/reviewer.css">

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/assets/reviewer/pending.css">

        <link rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

        <style>
            .filter-form {
                display: flex;
                flex-wrap: wrap;
                gap: 12px;
                margin-bottom: 20px;
                align-items: center;
            }

            .filter-form .search-box {
                flex: 1 1 280px;
                display: flex;
                align-items: center;
                gap: 8px;
                
               
                padding: 10px 14px;
            }

            .filter-form .search-box input {
                border: none;
                outline: none;
                width: 100%;
            }

            .filter-form select,
            .filter-form button,
            .filter-form a {
                padding: 10px 14px;
                border-radius: 10px;
                border: 1px solid #ddd;
                text-decoration: none;
                background: #fff;
                color: #333;
            }

            .filter-form button {
                cursor: pointer;
                background: #2563eb;
                color: #fff;
                border: none;
            }

            .empty-state {
                padding: 24px;
                background: #fff;
                border-radius: 12px;
                text-align: center;
                color: #666;
            }
        </style>
    </head>
    <body>

        <div class="layout">

            <%@ include file="layout/sidebar.jsp" %>

            <main class="main">

                <h1 class="page-title">Pending Reviews</h1>
                <p class="subtitle">
                    Software awaiting technical review and quality assessment
                </p>

                <form class="filter-form" method="get"
                      action="${pageContext.request.contextPath}/reviewer_pending">

                    <div class="search-box">
                        <i class="fa-solid fa-magnifying-glass"></i>
                        <input type="text"
                               name="keyword"
                               value="${selectedKeyword}"
                               placeholder="Search by software name, description, category, version...">
                    </div>

                    <select name="categoryId">
                        <option value="">All Categories</option>
                        <c:forEach var="c" items="${categories}">
                            <option value="${c.categoryId}"
                                    ${selectedCategoryId == c.categoryId ? 'selected' : ''}>
                                ${c.categoryName}
                            </option>
                        </c:forEach>
                    </select>

                    <select name="priceType">
                        <option value="all" ${selectedPriceType == 'all' ? 'selected' : ''}>All Prices</option>
                        <option value="free" ${selectedPriceType == 'free' ? 'selected' : ''}>Free</option>
                        <option value="paid" ${selectedPriceType == 'paid' ? 'selected' : ''}>Paid</option>
                    </select>

                    <button type="submit">
                        <i class="fa-solid fa-filter"></i> Filter
                    </button>

                    <a href="${pageContext.request.contextPath}/reviewer_pending">
                        Reset
                    </a>
                </form>

                <div class="pending-container">
                    <c:choose>
                        <c:when test="${empty pendingList}">
                            <div class="empty-state">
                                No pending software matches your filters.
                            </div>
                        </c:when>

                        <c:otherwise>
                            <c:forEach var="s" items="${pendingList}">
                                <div class="software-card">

                                    <div class="card-left">
                                        <img src="${pageContext.request.contextPath}/${s.imageUrl}"
                                             class="software-img"
                                             onerror="this.src='${pageContext.request.contextPath}/assets/images/img2.png'">

                                        <div class="software-info">
                                            <h3>${s.name}</h3>

                                            <p class="meta">
                                                Version ${empty s.version ? 'N/A' : s.version}
                                            </p>

                                            <p class="desc">
                                                ${s.shortDescription}
                                            </p>

                                            <div class="tags">
                                                <span class="tag">
                                                    <c:choose>
                                                        <c:when test="${not empty s.categoryName}">
                                                            ${s.categoryName}
                                                        </c:when>
                                                        <c:otherwise>N/A</c:otherwise>
                                                    </c:choose>
                                                </span>

                                                <span class="tag">
                                                    <c:choose>
                                                        <c:when test="${s.isFree == 1}">
                                                            Free
                                                        </c:when>
                                                        <c:otherwise>
                                                            $${s.price}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </div>

                                            <a class="review-btn"
                                               href="${pageContext.request.contextPath}/review_software?softwareId=${s.softwareId}">
                                                <i class="fa-solid fa-eye"></i> Start Review
                                            </a>
                                        </div>
                                    </div>

                                    <div class="status-badge">
                                        Pending Review
                                    </div>

                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>

            </main>

        </div>
    </body>
</html>