<aside class="sidebar">
    <h2>FivePigs Admin</h2>
    <nav>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="menu-item ${activeMenu == 'dashboard' ? 'active' : ''}">
            <span>Dashboard</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin/notifications" class="menu-item ${activeMenu == 'notifications' ? 'active' : ''}">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path><path d="M13.73 21a2 2 0 0 1-3.46 0"></path></svg>
            <span>Notifications</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin/employees" class="menu-item ${activeMenu == 'employees' ? 'active' : ''}">
            <span>Employees</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin/vendors" class="menu-item ${activeMenu == 'vendors' ? 'active' : ''}">
            <span>Vendors</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin/products" class="menu-item ${activeMenu == 'products' ? 'active' : ''}">
            <span>Products</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin/orders" class="menu-item ${activeMenu == 'orders' ? 'active' : ''}">
            <span>Orders</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin/payouts" class="menu-item ${activeMenu == 'payouts' ? 'active' : ''}">
            <span>Payouts</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin/reports" class="menu-item ${activeMenu == 'reports' ? 'active' : ''}">
            <span>Reports</span>
        </a>
    </nav>

    <div style="margin-top: auto; padding: 12px;">
        <p style="font-size: 12px; color: #64748b; margin-bottom: 8px;">Admin: <c:out value="${user.fullName}"/></p>
        <a href="${pageContext.request.contextPath}/logout" class="menu-item logout-btn">Logout</a>
    </div>
</aside>
