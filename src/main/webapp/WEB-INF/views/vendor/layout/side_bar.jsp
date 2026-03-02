<%-- 
    Document   : side_bar
    Created on : Feb 16, 2026, 4:22:45 PM
    Author     : MinhPD
--%>
        <aside class="sidebar">
            <div class="logo">
                <img src="assets/images/pig.png" alt="Pig Logo">
                <div>
                    <h2>FivePigs</h2>
                    <span>Software Market</span>
                </div>
            </div>
            
            <ul class="menu">
                <li><a href="vendor_dashboard"><i class="fa-solid fa-chart-line"></i>  Dashboard</a></li>
                <li><a href="my_products"><i class="fa-solid fa-box"></i>  My Products</a></li>
                <li><a href="license_management"><i class="fa-solid fa-key"></i>  License Management</a></li>
                <li><a href="payout"><i class="fa-solid fa-dollar-sign"></i>  Payout</a></li>
                <li><a href="vendor_dashboard"><i class="fa-regular fa-star"></i>  Notification</a></li>
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