<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.fivepigs.app.model.User" %>
<%
    String activePage = request.getParameter("activePage");
    if (activePage == null || activePage.isBlank()) {
        activePage = (String) request.getAttribute("activePage");
    }
    if (activePage == null) activePage = "";

    User currentUser = (User) session.getAttribute("user");
    boolean isGuest = (currentUser == null);

    String roleName = (String) session.getAttribute("roleName");
    boolean isVendor = roleName != null && roleName.equalsIgnoreCase("vendor");

    String libraryHref = isGuest
            ? (request.getContextPath() + "/login?redirect=/library")
            : (request.getContextPath() + "/library");

    String cartHref = isGuest
            ? (request.getContextPath() + "/login?redirect=/cart")
            : (request.getContextPath() + "/cart");
%>

<div class="sidebar">
    <div class="logo">
        <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Logo">
        <span class="logo-text">FIVEPIGS</span>
    </div>

    <a href="${pageContext.request.contextPath}/customer_dashboard" class="menu-item <%= activePage.equals("home") ? "active" : "" %>">
        <i class="fa-solid fa-house"></i> Home
    </a>

    <a href="${pageContext.request.contextPath}/game" class="menu-item <%= activePage.equals("games") ? "active" : "" %>">
        <i class="fa-solid fa-gamepad"></i> Games
    </a>

    <a href="${pageContext.request.contextPath}/app" class="menu-item <%= activePage.equals("apps") ? "active" : "" %>">
        <i class="fa-solid fa-cubes"></i> Apps
    </a>

   <% if(!isGuest) { %>
    <a href="<%= libraryHref %>" class="menu-item <%= activePage.equals("library") ? "active" : "" %>" title="<%= isGuest ? "Login to open Library" : "Library" %>">
        <i class="fa-solid fa-book-open"></i> Library
    </a>

    <a href="<%= cartHref %>" class="menu-item <%= activePage.equals("cart") ? "active" : "" %>" title="<%= isGuest ? "Login to open Cart" : "Cart" %>">
        <i class="fa-solid fa-cart-shopping"></i> Cart
    </a>
   <% } %>

    <% if (isVendor) { %>
    <a href="${pageContext.request.contextPath}/vendor/dashboard" class="menu-item" title="Vendor dashboard">
        <i class="fa-solid fa-store"></i> Vendor Dashboard
    </a>
    <% } else { %>
    <a href="${pageContext.request.contextPath}/vendor-apply" class="menu-item <%= activePage.equals("vendorApply") ? "active" : "" %>" title="<%= isGuest ? "Login to apply vendor" : "Apply become vendor" %>">
        <i class="fa-solid fa-store"></i> Apply Become Vendor
    </a>
    <% } %>

    <div class="sidebar-footer">
        <a href="${pageContext.request.contextPath}/aboutus" class="menu-item <%= activePage.equals("about") ? "active" : "" %>">
            <i class="fa-solid fa-circle-info"></i> About
        </a>
    </div>
</div>
