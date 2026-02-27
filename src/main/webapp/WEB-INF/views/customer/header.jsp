<%@page pageEncoding="UTF-8"%>

<div class="header">
    <div class="search-group">
        <i class="fa-solid fa-magnifying-glass" style="color: #b2bec3;"></i>
        <input type="text" placeholder="Search apps, games...">
    </div>

    <div class="user-actions">
        <div class="icon-btn"><i class="fa-regular fa-bell"></i></div>

        <div class="user-profile-container" onclick="toggleUserDropdown()">
            <div class="user-info">
                <div class="avatar">
                    <img src="https://ui-avatars.com/api/?name=Kiet&background=6c5ce7&color=fff" alt="User">
                </div>
                <i class="fa-solid fa-caret-down" style="font-size: 12px; color: #636e72;"></i>
            </div>

            <div id="userDropdown" class="dropdown-menu">
                <a href="#" class="dropdown-item"><i class="fa-regular fa-user"></i> My Profile</a>
                <a href="#" class="dropdown-item"><i class="fa-solid fa-gear"></i> Settings</a>
                <div class="divider"></div>
                <a href="${pageContext.request.contextPath}/logout" class="dropdown-item logout">
                    <i class="fa-solid fa-arrow-right-from-bracket"></i> Logout
                </a>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/script.js"></script>