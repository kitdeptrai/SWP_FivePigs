<%-- 
    Document   : approval_pending
    Created on : Feb 20, 2026, 11:00:44 AM
    Author     : thanh
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Pending Approvals</title>
        <link rel="stylesheet" href="/assets/css/Approval/approval.css">
    </head>

    <body>

        <div class="app">
            <jsp:include page="./layout/sidebar.jsp"/>
            <div class="content-pending">
                <div class="pending-wrap">
                    <h1 class="pending-title">Pending Approvals</h1>
                    <div class="pending-sub">Apps with completed technical reviews</div>
                    <div class="pending-filters">

                        <input type="text" id="searchApp" placeholder="Search App Name">

                        <input type="text" id="searchReviewer" placeholder="Reviewer">

                        <select id="searchCategory">
                            <option value="">All Category</option>
                            <option value="APP">APP</option>
                            <option value="GAME">GAME</option>
                        </select>

                        <input type="date" id="searchDate">

                    </div>
                    <div class="pending-panel" id="pending-container">
                        <c:if test="${empty listpending}">
                            <div class="empty">No applications are currently pending approval.</div>
                        </c:if>

                        <c:forEach var="it" items="${listpending}">
                            <div class="pending-card"
                                 data-name="${it.appName}"
                                 data-reviewer="${it.user.fullName}"
                                 data-category="${it.category.categoryName}"
                                 data-date="${it.formattedCreatedAt}"
                                 style="margin-bottom:18px;">
                                <div class="pending-card-inner">

                                    <!-- Hiển thị ảnh thumbnail -->
                                    <div class="thumb">
                                        <img src="${it.softwareImage.imageUrl}" alt="thumbnail">       
                                    </div>

                                    <div class="card-body">
                                        <!-- Hiển thị tên ứng dụng -->
                                        <h2 class="app-name">
                                            <c:out value="${it.appName}"/>
                                        </h2>

                                        <!-- Hiển thị mô tả ngắn -->
                                        <div class="app-desc">
                                            <c:out value="${it.shortDescription}"/>
                                        </div>

                                        <!-- Hiển thị tên danh mục -->
                                        <span class="pill">
                                            <c:out value="${empty it.category.categoryName ? 'Uncategorized' : it.category.categoryName}"/>
                                        </span>

                                        <!-- Hiển thị tên người đánh giá -->
                                        <span class="meta-text">
                                            Reviewed by <b><c:out value="${it.user.fullName}"/></b>
                                        </span>

                                        <!-- Hiển thị ngày thêm (created_at) -->
                                        <div class="submitted">
                                            Added on
                                            <c:choose>
                                                <c:when test="${it.createdAt != null}">
                                                    <c:out value="${it.formattedCreatedAt}"/>
                                                </c:when>
                                                <c:otherwise>—</c:otherwise>
                                            </c:choose>
                                        </div>

                                        <!-- Nút xem chi tiết -->
                                        <a class="btn"
                                           href="${pageContext.request.contextPath}/approval_pending_detail?softwareId=${it.softwareId}">
                                            Review &amp; Decide
                                        </a>
                                    </div>

                                </div>
                            </div>
                        </c:forEach>

                        <div class="pagination" id="pending-pagination"></div>
                    </div>
                </div>
            </div>
        </div>
        <script>
            const itemsPerPage = 4;

            const cards = Array.from(document.querySelectorAll(".pending-card"));
            const pagination = document.getElementById("pending-pagination");

            const searchApp = document.getElementById("searchApp");
            const searchReviewer = document.getElementById("searchReviewer");
            const searchCategory = document.getElementById("searchCategory");
            const searchDate = document.getElementById("searchDate");


            function getFilteredCards() {

                const appValue = searchApp.value.toLowerCase();
                const reviewerValue = searchReviewer.value.toLowerCase();
                const categoryValue = searchCategory.value;
                const dateValue = searchDate.value;

                return cards.filter(card => {

                    const name = card.dataset.name.toLowerCase();
                    const reviewer = card.dataset.reviewer.toLowerCase();
                    const category = card.dataset.category;
                    const date = card.dataset.date;

                    const matchName = name.includes(appValue);
                    const matchReviewer = reviewer.includes(reviewerValue);
                    const matchCategory = categoryValue === "" || category === categoryValue;
                    const matchDate = dateValue === "" || (date && date.includes(dateValue));

                    return matchName && matchReviewer && matchCategory && matchDate;

                });

            }


            function showPage(page, filteredCards) {

                const start = (page - 1) * itemsPerPage;
                const end = start + itemsPerPage;

                cards.forEach(card => card.style.display = "none");

                filteredCards.slice(start, end).forEach(card => {
                    card.style.display = "block";
                });

            }


            function createPagination(filteredCards) {

                pagination.innerHTML = "";

                const totalPages = Math.ceil(filteredCards.length / itemsPerPage);

                if (totalPages === 0)
                    return;

                for (let i = 1; i <= totalPages; i++) {

                    const btn = document.createElement("button");
                    btn.innerText = i;

                    btn.onclick = () => {

                        showPage(i, filteredCards);

                        document.querySelectorAll(".pagination button")
                                .forEach(b => b.classList.remove("active"));

                        btn.classList.add("active");

                    };

                    pagination.appendChild(btn);

                }

                pagination.querySelector("button").click();

            }


            function filterCards() {

                const filteredCards = getFilteredCards();

                cards.forEach(card => card.style.display = "none");

                if (filteredCards.length === 0) {
                    pagination.innerHTML = "";
                    return;
                }

                createPagination(filteredCards);

            }


            searchApp.addEventListener("input", filterCards);
            searchReviewer.addEventListener("input", filterCards);
            searchCategory.addEventListener("change", filterCards);
            searchDate.addEventListener("change", filterCards);


            filterCards();
        </script>
    </body>
</html>