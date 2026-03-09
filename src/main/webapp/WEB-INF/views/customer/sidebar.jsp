<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.fivepigs.app.model.User" %>
<%
    String activePage = (String) request.getAttribute("activePage");
    if (activePage == null) activePage = "";

    User currentUser = (User) session.getAttribute("user");
    boolean isGuest = (currentUser == null);

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

    <a href="${pageContext.request.contextPath}/home" class="menu-item <%= activePage.equals("home") ? "active" : "" %>">
        <i class="fa-solid fa-house"></i> Home
    </a>

    <a href="${pageContext.request.contextPath}/game" class="menu-item <%= activePage.equals("games") ? "active" : "" %>">
        <i class="fa-solid fa-gamepad"></i> Games
    </a>

    <a href="${pageContext.request.contextPath}/app" class="menu-item <%= activePage.equals("apps") ? "active" : "" %>">
        <i class="fa-solid fa-cubes"></i> Apps
    </a>

    <a href="<%= libraryHref %>" class="menu-item <%= activePage.equals("library") ? "active" : "" %>" title="<%= isGuest ? "Login to open Library" : "Library" %>">
        <i class="fa-solid fa-book-open"></i> Library
    </a>

    <a href="<%= cartHref %>" class="menu-item <%= activePage.equals("cart") ? "active" : "" %>" title="<%= isGuest ? "Login to open Cart" : "Cart" %>">
        <i class="fa-solid fa-cart-shopping"></i> Cart
    </a>

    <div class="sidebar-footer">
        <a href="${pageContext.request.contextPath}/news" class="menu-item <%= activePage.equals("news") ? "active" : "" %>">
            <i class="fa-solid fa-book-open"></i> News
        </a>
        <a href="${pageContext.request.contextPath}/aboutus" class="menu-item <%= activePage.equals("about") ? "active" : "" %>">
            <i class="fa-solid fa-circle-info"></i> About
        </a>
    </div>
</div>