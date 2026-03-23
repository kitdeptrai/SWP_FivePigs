<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="en_US" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin - Product Detail</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <style>
        :root {
            --dark-blue: #1e293b;
            --sidebar-bg: #0f172a;
            --card-bg: #ffffff;
            --text-main: #334155;
            --bg-gray: #f5f7fb;
            --accent: #3b82f6;
        }

        body {
            background-color: var(--bg-gray);
            color: var(--text-main);
            margin: 0;
            font-family: 'Inter', sans-serif;
        }

        .layout {
            display: grid;
            grid-template-columns: 260px 1fr;
            min-height: 100vh;
        }

        .sidebar {
            background-color: var(--sidebar-bg);
            color: white;
            padding: 24px 16px;
            display: flex;
            flex-direction: column;
        }

        .sidebar h2 {
            font-size: 24px;
            margin-bottom: 32px;
            color: #fff;
            padding: 0 12px;
        }

        .menu-item {
            padding: 12px 16px;
            border-radius: 8px;
            color: #94a3b8;
            text-decoration: none;
            margin-bottom: 4px;
            display: flex;
            align-items: center;
            gap: 12px;
            transition: all 0.2s;
        }

        .menu-item:hover {
            background-color: rgba(255, 255, 255, 0.1);
            color: white;
        }

        .menu-item.active {
            background-color: var(--accent);
            color: white;
        }

        .logout-btn {
            margin-top: auto;
            color: #ef4444;
            font-weight: 600;
        }

        .logout-btn:hover {
            background-color: rgba(239, 68, 68, 0.1);
        }

        .main {
            padding: 32px;
            overflow-y: auto;
        }

        .card {
            background: var(--card-bg);
            padding: 24px;
            border-radius: 12px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            border: 1px solid #e2e8f0;
            margin-bottom: 20px;
        }

        .badge {
            padding: 4px 8px;
            border-radius: 6px;
            font-size: 12px;
            font-weight: 600;
            display: inline-block;
        }

        .badge.active {
            background: rgba(34, 197, 94, 0.2);
            color: #22c55e;
        }

        .badge.inactive {
            background: rgba(239, 68, 68, 0.2);
            color: #ef4444;
        }

        .badge.pending {
            background: rgba(234, 179, 8, 0.2);
            color: #ca8a04;
        }

        .image-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
            gap: 16px;
        }

        .image-grid img {
            width: 100%;
            border-radius: 12px;
            object-fit: cover;
        }

        .kv {
            display: grid;
            grid-template-columns: 200px 1fr;
            gap: 12px;
            margin-bottom: 10px;
        }

        .actions {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
        }

        .btn {
            padding: 10px 14px;
            border-radius: 10px;
            text-decoration: none;
            font-weight: 600;
            font-size: 14px;
        }

        .btn.primary {
            background: var(--accent);
            color: white;
        }

        .btn.secondary {
            border: 1px solid #cbd5e1;
            color: #334155;
            background: white;
        }

        .btn.danger {
            background: #ef4444;
            color: #fff;
        }
    </style>
</head>
<body>

<div class="layout">
    <c:set var="activeMenu" value="products"/>
    <jsp:include page="sidebar.jsp"/>

    <main class="main">
        <h1 style="margin-top:0;">Product Detail</h1>
        <p class="subtitle">Product details.</p>

        <div class="card">
            <div style="display:flex; align-items:center; gap:16px;">
                <c:if test="${not empty product.imageUrl}">
                    <img src="/${product.imageUrl}" alt="thumbnail" style="width:90px; height:90px; border-radius:12px; object-fit:cover;" />
                </c:if>
                <div>
                    <h2 style="margin:0;">${product.name}</h2>
                    <p style="margin:6px 0; color:#64748b;">${product.categoryName} • ${product.vendorName}</p>
                    <c:choose>
                        <c:when test="${product.status == 'ACTIVE'}">
                            <span class="badge active">ACTIVE</span>
                        </c:when>
                        <c:when test="${product.status == 'INACTIVE'}">
                            <span class="badge inactive">INACTIVE</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge pending">${product.status}</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div style="margin-top:20px;" class="actions">
                <a class="btn secondary" href="${pageContext.request.contextPath}/admin/products">Back</a>
            </div>
        </div>

        <div class="card">
            <h3>Basic information</h3>
            <div class="kv"><strong>ID</strong><span>#${product.softwareId}</span></div>
            <div class="kv"><strong>Version</strong><span>${product.versionName}</span></div>
            <div class="kv"><strong>Price</strong>
                <span>
                    <c:choose>
                        <c:when test="${product.isFree == 1}">Free</c:when>
                        <c:otherwise><fmt:formatNumber value="${product.price}" type="currency" currencySymbol="$"/></c:otherwise>
                    </c:choose>
                </span>
            </div>
            <div class="kv"><strong>Downloads</strong><span>${product.downloadCount}</span></div>
            <div class="kv"><strong>Rating</strong><span><fmt:formatNumber value="${product.avgRating}" minFractionDigits="1" maxFractionDigits="1"/></span></div>
            <div class="kv"><strong>Created At</strong><span><fmt:formatDate value="${product.createdAt}" pattern="dd/MM/yyyy HH:mm"/></span></div>
            <div class="kv"><strong>Short Description</strong><span>${product.shortDescription}</span></div>
        </div>

        <div class="card">
            <h3>Description</h3>
            <p>${product.description}</p>
        </div>

        <div class="card">
            <h3>System Requirement</h3>
            <p>${product.systemRequirement}</p>
        </div>

        <div class="card">
            <h3>Images</h3>
            <div class="image-grid">
                <c:forEach var="img" items="${images}">
                    <img src="/${img.imageUrl}" alt="product image" />
                </c:forEach>
            </div>
        </div>
    </main>
</div>

</body>
</html>
