<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Pending Reviews</title>

        <!-- GLOBAL LAYOUT -->
        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/assets/reviewer/reviewer.css">

        <!-- PAGE STYLE -->
        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/assets/reviewer/pending.css">

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
                            <a href="${pageContext.request.contextPath}/reviewer_dashboard"
                               class="menu-link">
                                <span class="icon">📊</span> Dashboard
                            </a>
                        </li>

                        <li>
                            <a href="${pageContext.request.contextPath}/reviewer_pending"
                               class="menu-link active">
                                <span class="icon">🕒</span> Pending Reviews
                            </a>
                        </li>

                        <li>
                            <a href="${pageContext.request.contextPath}/reviewer_my_reviews"
                               class="menu-link ">
                                <span class="icon">📄</span> My Reviews
                            </a>
                        </li>

                        <li>
                            <a href="${pageContext.request.contextPath}/reviewer_history"
                               class="menu-link ">
                                <span class="icon">⏱</span> Review History
                            </a>
                        </li>

                        <li>
                            <a href="${pageContext.request.contextPath}/reviewer_guidelines"
                               class="menu-link ">
                                <span class="icon">📖</span> Review Guidelines
                            </a>
                        </li>

                        <li>
                            <a href="${pageContext.request.contextPath}/reviewer_notifications"
                               class="menu-link ">
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

                <a class="logout" href="${pageContext.request.contextPath}/logout">Logout</a>
            </aside>



            <!-- ================= MAIN ================= -->
            <main class="main">

                <h1 class="page-title">Pending Reviews</h1>
                <p class="subtitle">
                    Software awaiting technical review and quality assessment
                </p>
                <!-- SEARCH FORM -->
                <form class="search-wrapper" onsubmit="return false;">

                    <div class="search-box">
                        <i class="fa-solid fa-magnifying-glass"></i>
                        <input type="text"
                               id="searchInput"
                               placeholder="Search by software name, vendor, category...">
                    </div>

                </form>

                <div class="pending-container">

                    <c:forEach var="s" items="${pendingList}">
                        <div class="software-card">

                            <div class="card-left">
                                <img src="${pageContext.request.contextPath}/assets/images/img2.png"
                                     class="software-img">

                                <div class="software-info">
                                    <h3>${s.name}</h3>

                                    <p class="meta">
                                        Version ${s.version}
                                    </p>

                                    <p class="desc">
                                        ${s.short_description}
                                    </p>

                                    <div class="tags">
                                        <span class="tag">
                                            <c:choose>
                                                <c:when test="${not empty s.categoryName}">${s.categoryName}</c:when>
                                                <c:otherwise>N/A</c:otherwise>
                                            </c:choose>
                                        </span>
                                        <span class="tag">$${s.price}</span>
                                    </div>

                                    <button class="start-btn"
                                            onclick="openModal(
                        '${s.version}',
                        '${s.categoryName}',
                        '${s.formattedCreatedAt}'
                        )">
                                        👁 Start Review
                                    </button>
                                </div>
                            </div>

                            <div class="status-badge">
                                Pending Review
                            </div>

                        </div>
                    </c:forEach>

                </div>

            </main>

        </div>
        <!-- ================= REVIEW MODAL ================= -->
        <div id="reviewModal" class="modal">
            <div class="modal-content">

                <span class="close-btn" onclick="closeModal()">&times;</span>

                <div class="modal-grid">
                    <div> 
                        <p><strong>Version:</strong> <span id="modalVersion"></span></p>
                    </div>

                    <div> 
                        <p><strong>Category:</strong> <span id="modalCategory"></span></p>
                        <p><strong>Upload Date:</strong> <span id="modalDate"></span></p>
                    </div>
                </div>

                <button class="download-btn">
                    ⬇ Download Software Package
                </button>

                <div class="checklist">
                    <label><input type="checkbox"> No malware or malicious code detected</label>
                    <label><input type="checkbox"> No copyright or legal violations</label>
                    <label><input type="checkbox"> No spam or inappropriate content</label>
                </div>

                <div class="score-grid">
                    <div>
                        <label>UI/UX Design</label>
                        <input type="number" min="0" max="10" value="0">
                    </div>

                    <div>
                        <label>Technical Quality</label>
                        <input type="number" min="0" max="10" value="0">
                    </div>

                    <div>
                        <label>Performance</label>
                        <input type="number" min="0" max="10" value="0">
                    </div>

                    <div>
                        <label>Documentation</label>
                        <input type="number" min="0" max="10" value="0">
                    </div>
                </div>
                <div class="comment-box">
                    <label>Detailed Feedback</label>
                    <textarea name="comment" 
                              placeholder="Provide detailed feedback about the software..."
                              required></textarea>
                </div>

                <button class="submit-review">Submit Review</button>

            </div>
        </div>
        <script>
            function openModal(version, category, date) {

                document.getElementById("modalVersion").innerText = version;
                document.getElementById("modalCategory").innerText = category;
                document.getElementById("modalDate").innerText = date;

                document.getElementById("reviewModal").style.display = "flex";
            }
            function closeModal() {
                document.getElementById("reviewModal").style.display = "none";
            }

            window.onclick = function (event) {
                const modal = document.getElementById("reviewModal");
                if (event.target === modal) {
                    modal.style.display = "none";
                }
            }
        </script>

        <script>
            const searchInput = document.getElementById("searchInput");

            searchInput.addEventListener("keyup", function () {

                const keyword = this.value.toLowerCase();
                const cards = document.querySelectorAll(".software-card");

                cards.forEach(card => {
                    const name = card.querySelector("h3").innerText.toLowerCase();

                    if (name.includes(keyword)) {
                        card.style.display = "flex";
                    } else {
                        card.style.display = "none";
                    }
                });
            });
        </script>
    </body>
</html>