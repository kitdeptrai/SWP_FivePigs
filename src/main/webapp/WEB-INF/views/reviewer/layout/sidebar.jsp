<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

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
                   class="menu-link ${activeMenu == 'dashboard' ? 'active' : ''}">
                    <span class="icon">📊</span> Dashboard
                </a>
            </li>

            <li>
                <a href="${pageContext.request.contextPath}/reviewer_pending"
                   class="menu-link ${activeMenu == 'pending' ? 'active' : ''}">
                    <span class="icon">🕒</span> Pending Reviews
                </a>
            </li>

           

            <li>
                <a href="${pageContext.request.contextPath}/reviewer_history"
                   class="menu-link ${activeMenu == 'history' ? 'active' : ''}">
                    <span class="icon">⏱</span> Review History
                </a>
            </li>

            <li>
                <a href="${pageContext.request.contextPath}/reviewer_guidelines"
                   class="menu-link ${activeMenu == 'guidelines' ? 'active' : ''}">
                    <span class="icon">📖</span> Review Guidelines
                </a>
            </li>

            <li>
                <a href="${pageContext.request.contextPath}/reviewer_notifications"
                   class="menu-link ${activeMenu == 'notifications' ? 'active' : ''}">
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