<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

<aside class="sidebar">
    <div style="display:flex; flex-direction:column; align-items:flex-start; gap:8px; padding: 0 12px; margin-bottom: 20px;">
        <img src="${pageContext.request.contextPath}/assets/css/images/logo.png" alt="FivePigs Logo" style="width:72px; height:72px; object-fit:contain; border-radius:10px;" />
        <h2 style="margin:0; font-size:24px;">FivePigs Admin</h2>
    </div>
    <nav>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="menu-item ${activeMenu == 'dashboard' ? 'active' : ''}">
            <i class="fa-solid fa-gauge"></i>
            <span>Dashboard</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin/notifications" class="menu-item ${activeMenu == 'notifications' ? 'active' : ''}">
            <i class="fa-solid fa-bell"></i>
            <span>Notifications</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin/employees" class="menu-item ${activeMenu == 'employees' ? 'active' : ''}">
            <i class="fa-solid fa-users"></i>
            <span>Employees</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin/vendors" class="menu-item ${activeMenu == 'vendors' ? 'active' : ''}">
            <i class="fa-solid fa-store"></i>
            <span>Vendors</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin/products" class="menu-item ${activeMenu == 'products' ? 'active' : ''}">
            <i class="fa-solid fa-box"></i>
            <span>Products</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin/orders" class="menu-item ${activeMenu == 'orders' ? 'active' : ''}">
            <i class="fa-solid fa-cart-shopping"></i>
            <span>Orders</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin/payouts" class="menu-item ${activeMenu == 'payouts' ? 'active' : ''}">
            <i class="fa-solid fa-wallet"></i>
            <span>Payouts</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin/reports" class="menu-item ${activeMenu == 'reports' ? 'active' : ''}">
            <i class="fa-solid fa-triangle-exclamation"></i>
            <span>Reports</span>
        </a>
    </nav>

    <div style="margin-top: auto; padding: 12px;">
        <p style="font-size: 12px; color: #64748b; margin-bottom: 8px;">Admin: <c:out value="${user.fullName}"/></p>
        <a href="${pageContext.request.contextPath}/logout" class="menu-item logout-btn">Logout</a>
    </div>
</aside>
