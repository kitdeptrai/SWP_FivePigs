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

                    <div class="pending-panel" id="pending-container">
                        <c:if test="${empty listpending}">
                            <div class="empty">Không có ứng dụng nào đang chờ duyệt.</div>
                        </c:if>

                        <c:forEach var="it" items="${listpending}">
                            <div class="pending-card" style="margin-bottom: 18px;">
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

                                        <!-- Hiển thị thời gian đánh giá -->
                                        <div class="submitted">
                                            Submitted
                                            <c:choose>
                                                <c:when test="${it.reviewerProcess != null && it.reviewerProcess.reviewed_at != null}">
                                                    <c:out value="${it.reviewerProcess.reviewed_at}"/>
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

            function setupPagination(containerId, paginationId, itemsPerPage = 4) {

                const container = document.getElementById(containerId);
                const items = container.querySelectorAll(".pending-card");
                const pagination = document.getElementById(paginationId);

                let currentPage = 1;
                const totalPages = Math.ceil(items.length / itemsPerPage);

                function showPage(page) {
                    currentPage = page;

                    items.forEach(item => {
                        item.style.display = "none";
                    });

                    const start = (page - 1) * itemsPerPage;
                    const end = start + itemsPerPage;

                    for (let i = start; i < end && i < items.length; i++) {
                        items[i].style.display = "block";
                    }

                    const buttons = pagination.querySelectorAll("button");
                    buttons.forEach(btn => btn.classList.remove("active"));
                    buttons[page - 1].classList.add("active");
                }

                function createPagination() {
                    pagination.innerHTML = "";

                    for (let i = 1; i <= totalPages; i++) {
                        const btn = document.createElement("button");
                        btn.innerText = i;

                        btn.onclick = () => showPage(i);

                        pagination.appendChild(btn);
                    }
                }

                if (items.length > 0) {
                    createPagination();
                    showPage(1);
            }
            }

            document.addEventListener("DOMContentLoaded", function () {

                setupPagination("pending-container", "pending-pagination", 4);
            });

        </script>
    </body>
</html>