<%-- 
    Document   : version_management
    Created on : Mar 9, 2026, 1:59:19 AM
    Author     : MinhPD
--%>


<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
    <head>

        <meta charset="UTF-8">
        <title>Version Management</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/vendor/vendor.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

        <style>

            .page-header{
                display:flex;
                justify-content:space-between;
                align-items:center;
                margin-bottom:30px;
            }

            .page-title{
                font-size:26px;
                font-weight:700;
            }

            .btn-upload{
                background:#6366f1;
                padding:10px 16px;
                border-radius:8px;
                color:white;
                text-decoration:none;
            }

            .version-list{
                display:flex;
                flex-direction:column;
                gap:25px;
            }

            .version-card{
                background:#1f2937;
                padding:25px;
                border-radius:16px;
                border:1px solid #374151;
                display:flex;
                gap:20px;
            }

            .version-icon{
                width:60px;
                height:60px;
                background:#111827;
                border-radius:12px;
                display:flex;
                align-items:center;
                justify-content:center;
                font-size:22px;
                color:#6366f1;
            }

            .version-info{
                flex:1;
            }

            .version-title{
                font-size:20px;
                font-weight:600;
                margin-bottom:5px;
            }

            .version-meta{
                font-size:13px;
                color:#9ca3af;
                margin-bottom:15px;
            }

            .badge-current{
                background:#1e3a8a;
                color:#60a5fa;
                padding:3px 10px;
                border-radius:20px;
                font-size:12px;
            }

            .badge-deprecated{
                background:#374151;
                color:#9ca3af;
                padding:3px 10px;
                border-radius:20px;
                font-size:12px;
            }

            .changelog{
                margin-top:10px;
            }

            .changelog li{
                color:#cbd5f5;
                margin-bottom:6px;
            }

            .version-actions{
                display:flex;
                flex-direction:column;
                gap:10px;
            }

            .btn{
                border:none;
                padding:8px 12px;
                border-radius:6px;
                cursor:pointer;
            }

            .btn-edit{
                background:#2563eb;
                color:white;
            }

            .btn-delete{
                background:#dc2626;
                color:white;
            }

        </style>

    </head>

    <body>

        <div class="layout">

            <jsp:include page="layout/side_bar.jsp"/>

            <div class="main">

                <div class="page-header">

                    <div class="page-title">
                        Version Management
                    </div>

                    <a href="upload-version?softwareId=${softwareId}" class="btn-upload">
                        <i class="fa fa-plus"></i>
                        Upload New Version
                    </a>

                </div>

                <div class="version-list">

                    <c:forEach var="v" items="${listVersion}">

                        <div class="version-card">

                            <div class="version-icon">
                                <i class="fa-solid fa-code-branch"></i>
                            </div>

                            <div class="version-info">

                                <div class="version-title">
                                    ${infoSoftware.name} v${v.versionName}

                                    <c:choose>
                                        <c:when test="${v.isCurrent}">
                                            <span class="badge-current">Current</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge-deprecated">Deprecated</span>
                                        </c:otherwise>
                                    </c:choose>

                                </div>

                                <div class="version-meta">
                                    ${v.releaseDate} • ${v.downloadCount} downloads • ${v.size} MB
                                </div>

                                <ul class="changelog">
                                    <c:forEach var="log" items="${v.changelog}">
                                        <li>${log}</li>
                                        </c:forEach>
                                </ul>

                            </div>

                            <div class="version-actions">

                                <button class="btn btn-edit">
                                    Edit
                                </button>

                                <button class="btn btn-delete">
                                    Delete
                                </button>

                            </div>

                        </div>

                    </c:forEach>

                </div>

            </div>

        </div>

    </body>

</html>
