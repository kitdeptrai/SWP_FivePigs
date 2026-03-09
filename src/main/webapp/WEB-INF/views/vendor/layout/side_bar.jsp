<%-- 
    Document   : side_bar
    Created on : Feb 16, 2026, 4:22:45 PM
    Author     : MinhPD
--%>
<aside class="sidebar">
    <div class="logo">
        <img src="/assets/images/pig.png" alt="Pig Logo">
        <div>
            <h2>FivePigs</h2>
            <span>Software Market</span>
        </div>
    </div>

    <ul class="menu">
        <li>
            <a href="/vendor/dashboard" class="${pageContext.request.requestURI.contains('dashboard') ? 'active' : ''}">
                <i class="fa-solid fa-chart-line"></i> Dashboard
            </a>
        </li>

        <li>
            <a href="/vendor/my_products" class="${pageContext.request.requestURI.contains('my_products') ? 'active' : ''}">
                <i class="fa-solid fa-box"></i> My Products
            </a>
        </li>

        <li>
            <a href="/vendor/license_management" class="${pageContext.request.requestURI.contains('license_management') ? 'active' : ''}">
                <i class="fa-solid fa-key"></i> License Management
            </a>
        </li>

        <li>
            <a href="/vendor/payout" class="${pageContext.request.requestURI.contains('payout') ? 'active' : ''}">
                <i class="fa-solid fa-dollar-sign"></i> Payout
            </a>
        </li>

        <li>
            <a href="/vendor/notification" class="${pageContext.request.requestURI.contains('notification') ? 'active' : ''}">
                <i class="fa-regular fa-star"></i> Notification
            </a>
        </li>
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