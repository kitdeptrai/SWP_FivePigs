<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Review History</title>

    <!-- GLOBAL -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/reviewer/reviewer.css">

    <!-- PAGE STYLE -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/reviewer/reviewHistory.css">

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<div class="layout">

    <!-- ================= SIDEBAR ================= -->
    <aside class="sidebar">

        <div>
            <div class="logo">
                <img src="${pageContext.request.contextPath}/assets/images/pig.png" alt="Pig Logo">
                <div>
                    <h2>FivePigs</h2>
                    <span>Software Market</span>
                </div>
            </div>

            <ul class="menu">
                <li>
                    <a href="${pageContext.request.contextPath}/reviewer_dashboard" class="menu-link">
                        <span class="icon">📊</span> Dashboard
                    </a>
                </li>

                <li>
                    <a href="${pageContext.request.contextPath}/reviewer_pending" class="menu-link">
                        <span class="icon">🕒</span> Pending Reviews
                    </a>
                </li>

                <li>
                    <a href="${pageContext.request.contextPath}/reviewer_my_reviews" class="menu-link">
                        <span class="icon">📄</span> My Reviews
                    </a>
                </li>

                <li>
                    <a href="${pageContext.request.contextPath}/reviewer_history"
                       class="menu-link active">
                        <span class="icon">⏱</span> Review History
                    </a>
                </li>

                <li>
                    <a href="${pageContext.request.contextPath}/reviewer_guidelines"
                       class="menu-link">
                        <span class="icon">📖</span> Review Guidelines
                    </a>
                </li>

                <li>
                    <a href="${pageContext.request.contextPath}/reviewer_notifications"
                       class="menu-link">
                        <span class="icon">🔔</span> Notifications
                    </a>
                </li>
            </ul>
        </div>

        <div class="user-box">
            <div class="avatar">AJ</div>
            <div>
                <p class="name">${user.fullName}</p>
                <p class="role">Reviewer</p>
            </div>
        </div>

        <a class="logout"
           href="${pageContext.request.contextPath}/logout">Logout</a>

    </aside>

    <!-- ================= MAIN ================= -->
    <main class="main">

        <h1 class="page-title">Review History</h1>
        <p class="subtitle">
            Completed software reviews
        </p>

        <!-- SEARCH -->
        <form class="search-wrapper" onsubmit="return false;">
            <div class="search-box">
                <i class="fa-solid fa-magnifying-glass"></i>
                <input type="text"
                       id="searchInput"
                       placeholder="Search by software name, vendor, category...">
            </div>
        </form>

        <!-- HISTORY LIST -->
        <div class="history-container"> 
            <!-- ================= PAGINATION ================= -->


            <c:forEach var="h" items="${historyList}">

                <div class="history-card">

                    <!-- LEFT -->
                    <div class="card-left">

                        <img src="${pageContext.request.contextPath}/${h.imageUrl}"
                             class="software-thumb">

                        <div class="software-info">
                            <h3>${h.softwareName}</h3>
                            <p class="meta">Version ${h.version}</p>
                            <p class="meta">
                                Reviewed at: ${h.createdAt}
                            </p>
                            <p class="meta score">
                                Total Score: ${h.totalScore}
                            </p>
                        </div>
                    </div>

                    <!-- RIGHT -->
                    <div class="card-right">

                        <c:choose>
                            <c:when test="${h.decision == 'APPROVED'}">
                                <span class="badge approved">
                                    <i class="fa-solid fa-circle-check"></i>
                                    Approved
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge rejected">
                                    <i class="fa-solid fa-circle-xmark"></i>
                                    Rejected
                                </span>
                            </c:otherwise>
                        </c:choose>

                        <button class="view-btn"
                                onclick="openModal(
                                    '${h.softwareName}',
                                    '${h.version}',
                                    '${h.totalScore}',
                                    '${h.decision}',
                                    '${h.createdAt}'
                                )">
                            <i class="fa-solid fa-eye"></i>
                        </button>

                    </div>

                </div>

            </c:forEach>

        </div>
         <c:if test="${totalPages > 1}">
    <div class="pagination">

        <!-- Previous -->
        <c:if test="${currentPage > 1}">
            <a href="${pageContext.request.contextPath}/reviewer_history?page=${currentPage - 1}"
               class="page-btn">
                ← Previous
            </a>
        </c:if>

        <!-- Page Numbers -->
        <c:forEach begin="1" end="${totalPages}" var="i">
            <a href="${pageContext.request.contextPath}/reviewer_history?page=${i}"
               class="page-number ${i == currentPage ? 'active' : ''}">
                ${i}
            </a>
        </c:forEach>

        <!-- Next -->
        <c:if test="${currentPage < totalPages}">
            <a href="${pageContext.request.contextPath}/reviewer_history?page=${currentPage + 1}"
               class="page-btn">
                Next →
            </a>
        </c:if>

    </div>
</c:if>


    </main>

</div>

          <!-- ================= MODAL ================= -->
<div id="reviewModal" class="modal">
    <div class="modal-content">

        <span class="close-btn" onclick="closeModal()">&times;</span>

        <h2 id="modalName"></h2>

        <div class="modal-info">
            <p><strong>Version:</strong> <span id="modalVersion"></span></p>
            <p><strong>Total Score:</strong> <span id="modalScore"></span></p>
            <p><strong>Decision:</strong> <span id="modalDecision"></span></p>
            <p><strong>Reviewed At:</strong> <span id="modalDate"></span></p>
        </div>

    </div>
</div> 


<script>
// ================= MODAL =================
function openModal(name, version, score, decision, date) {
    document.getElementById("modalName").innerText = name;
    document.getElementById("modalVersion").innerText = version;
    document.getElementById("modalScore").innerText = score;
    document.getElementById("modalDecision").innerText = decision;
    document.getElementById("modalDate").innerText = date;
    document.getElementById("reviewModal").style.display = "flex";
}

function closeModal() {
    document.getElementById("reviewModal").style.display = "none";
}

window.onclick = function(event) {
    const modal = document.getElementById("reviewModal");
    if (event.target === modal) {
        modal.style.display = "none";
    }
};

// ================= SEARCH =================
const searchInput = document.getElementById("searchInput");

searchInput.addEventListener("keyup", function () {

    const keyword = this.value.toLowerCase();
    const cards = document.querySelectorAll(".history-card");

    cards.forEach(card => {
        const text = card.innerText.toLowerCase();
        card.style.display = text.includes(keyword) ? "" : "none";
    });
});
</script>

</body>
</html>