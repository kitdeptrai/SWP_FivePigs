<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Reviewer Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/reviewer/reviewer.css">
</head>
<body>

<div class="layout">

    <!-- SIDEBAR -->
    <aside class="sidebar">
        <div class="logo">
     
    <img src="assets/images/pig.png" alt="Pig Logo">
    <div>
        <h2>FivePigs</h2>
        <span>Software Market</span>
    </div>
</div>

        <ul class="menu">
            <li class="active">Dashboard</li>
            <li>Pending Reviews</li>
            <li>My Reviews</li>
            <li>Review History</li>
            <li>Performance</li>
        </ul>

        <div class="user-box">
            <div class="avatar">AJ</div>
            <div>
                <p class="name">${user.fullName}</p>
                <p class="role">Reviewer</p>
            </div>
        </div>

        <a class="logout" href="${pageContext.request.contextPath}/logout">Logout</a>
    </aside>

    <!-- MAIN CONTENT -->
    <main class="main">
        <h1>Reviewer Dashboard</h1>
        <p class="subtitle">Monitor your review performance and quality metrics</p>

        <!-- TOP CARDS -->
        <div class="cards">

    <!-- Card 1 -->
    <div class="card">
        <div class="card-icon warning">
            <!-- clock icon -->
            <svg width="22" height="22" fill="none" stroke="currentColor" stroke-width="2"
                 stroke-linecap="round" stroke-linejoin="round"
                 viewBox="0 0 24 24">
                <circle cx="12" cy="12" r="10"/>
                <path d="M12 6v6l4 2"/>
            </svg>
        </div>

        <p class="card-title">Pending Reviews</p>
        <h2>${pendingReviewApp}</h2>
        <span class="warning-text">Requires attention</span>
    </div>

    <!-- Card 2 -->
    <div class="card">
        <div class="card-icon success">
            <!-- check icon -->
            <svg width="22" height="22" fill="none" stroke="currentColor" stroke-width="2"
                 stroke-linecap="round" stroke-linejoin="round"
                 viewBox="0 0 24 24">
                <path d="M20 6L9 17l-5-5"/>
            </svg>
        </div>

        <p class="card-title">Completed Reviews</p>
        <h2>1</h2>
        <span class="success-text">50% completion rate</span>
    </div>

    <!-- Card 3 -->
    <div class="card">
        <div class="card-icon info">
            <!-- award icon -->
            <svg width="22" height="22" fill="none" stroke="currentColor" stroke-width="2"
                 stroke-linecap="round" stroke-linejoin="round"
                 viewBox="0 0 24 24">
                <circle cx="12" cy="8" r="7"/>
                <path d="M8.21 13.89L7 23l5-3 5 3-1.21-9.11"/>
            </svg>
        </div>

        <p class="card-title">Quality Score</p>
        <h2>92%</h2>
        <span class="info-text">Above average</span>
    </div>

    <!-- Card 4 -->
    <div class="card">
        <div class="card-icon purple">
            <!-- trending icon -->
            <svg width="22" height="22" fill="none" stroke="currentColor" stroke-width="2"
                 stroke-linecap="round" stroke-linejoin="round"
                 viewBox="0 0 24 24">
                <polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/>
                <polyline points="17 6 23 6 23 12"/>
            </svg>
        </div>

        <p class="card-title">Avg. Review Time</p>
        <h2>2.5h</h2>
        <span class="purple-text">Per software</span>
    </div>

</div>

        <!-- PROGRESS + QUALITY -->
<div class="grid-2">

    <!-- Monthly Target Progress -->
    <div class="panel">
        <div class="panel-header">
            <h3>Monthly Target Progress</h3>
            <span class="target">5 / 20</span>
        </div>

        <p class="label">Reviews Completed</p>
        <div class="progress-bar">
            <div class="progress-fill" style="width:25%"></div>
        </div>

        <div class="stats">
            <div>
                <h2>5</h2>
                <p>This Week</p>
            </div>
            <div>
                <h2 class="green">1</h2>
                <p>This Month</p>
            </div>
            <div>
                <h2 class="blue">2</h2>
                <p>All Time</p>
            </div>
        </div>
    </div>

    <!-- Review Quality Breakdown -->
    <div class="panel">
        <h3>Review Quality Breakdown</h3>

        <div class="quality">
            <span>Technical Assessment</span>
            <span>95%</span>
        </div>
        <div class="progress-bar">
            <div class="progress-fill" style="width:95%"></div>
        </div>

        <div class="quality">
            <span>Security Checks</span>
            <span>98%</span>
        </div>
        <div class="progress-bar">
            <div class="progress-fill" style="width:98%"></div>
        </div>

        <div class="quality">
            <span>UI/UX Evaluation</span>
            <span>88%</span>
        </div>
        <div class="progress-bar">
            <div class="progress-fill" style="width:88%"></div>
        </div>

        <div class="quality">
            <span>Documentation Review</span>
            <span>92%</span>
        </div>
        <div class="progress-bar">
            <div class="progress-fill" style="width:92%"></div>
        </div>
    </div>
    
</div>
<div class="recent-review">
  <h2>Recent Review Activity</h2>

  <!-- Item 1 : Pending -->
  <div class="review-item">
    <div class="left">
      <img src="${pageContext.request.contextPath}/assets/images/img2.png" class="review-logo">


      <div class="info">
        <div class="name">Study Buddy</div>
        <div class="meta">EduTech Solutions • 10/21/2023</div>
      </div>
    </div>

    <div class="right pending">
      <!-- CLOCK ICON -->
      <svg width="22" height="22" viewBox="0 0 24 24" fill="none">
        <circle cx="12" cy="12" r="9" stroke="#facc15" stroke-width="2"/>
        <path d="M12 7v5l3 2" stroke="#facc15" stroke-width="2" stroke-linecap="round"/>
      </svg>
    </div>
  </div>

  <!-- Item 2 : Completed -->
  <div class="review-item">
    <div class="left">
     <img src="${pageContext.request.contextPath}/assets/images/img3.png" class="review-logo">


      <div class="info">
        <div class="name">PyCalc Pro</div>
        <div class="meta">TechSoft Inc • 10/24/2023</div>
      </div>
    </div>

    <div class="right done">
      <span class="badge green">Security ✓</span>
      <span class="badge green">Legal ✓</span>

      <!-- CHECK ICON -->
      <svg width="22" height="22" viewBox="0 0 24 24" fill="none">
        <circle cx="12" cy="12" r="9" stroke="#22c55e" stroke-width="2"/>
        <path d="M8 12l3 3 5-6" stroke="#22c55e" stroke-width="2" stroke-linecap="round"/>
      </svg>
    </div>
  </div>
</div>


</body>
</html>
