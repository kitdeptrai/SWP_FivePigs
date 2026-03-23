<%-- 
    Document   : version_management
    Created on : Mar 9, 2026, 1:59:19 AM
    Author     : MinhPD
--%>


<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
                gap:20px;
            }

            /* CARD */

            .version-card{
                background: linear-gradient(145deg,#1e293b,#0f172a);
                border-radius:16px;
                padding:22px;
                display:flex;
                align-items:center;
                gap:20px;
                border:1px solid rgba(255,255,255,0.05);
                transition:0.25s;
            }

            .version-card:hover{
                transform:translateY(-4px);
                border:1px solid rgba(99,102,241,0.4);
                box-shadow:0 12px 30px rgba(0,0,0,0.4);
            }

            /* ICON */

            .version-icon{
                width:56px;
                height:56px;
                background:#111827;
                border-radius:12px;
                display:flex;
                align-items:center;
                justify-content:center;
                font-size:22px;
                color:#6366f1;
            }

            /* INFO */

            .version-info{
                flex:1;
            }

            .version-title{
                font-size:18px;
                font-weight:600;
                color:#f1f5f9;
                display:flex;
                align-items:center;
                gap:10px;
            }

            /* META */

            .version-meta{
                font-size:13px;
                color:#94a3b8;
                margin-top:6px;
                display:flex;
                gap:20px;
            }

            /* BADGES */

            .badge-current{
                background:#1e3a8a;
                color:#60a5fa;
                padding:4px 10px;
                border-radius:20px;
                font-size:12px;
                font-weight:500;
            }

            .badge-deprecated{
                background:#374151;
                color:#9ca3af;
                padding:4px 10px;
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

            .release-note{
                margin-top:10px;
                font-size:14px;
                color:#cbd5f5;
                line-height:1.5;
                background:#0f172a;
                padding:10px 14px;
                border-radius:8px;
                border:1px solid rgba(255,255,255,0.05);
            }
            /* MODAL BACKGROUND */

            .modal{
                display:none;
                position:fixed;
                top:0;
                left:0;
                width:100%;
                height:100%;
                background:rgba(0,0,0,0.7);
                justify-content:center;
                align-items:center;
                z-index:999;
            }

            /* MODAL BOX */

            .modal-content{
                background:#ffffff;
                width:500px;
                border-radius:12px;
                padding:30px;
                color:#111827; /* thêm dòng này */
            }

            /* HEADER */

            .modal-header{
                display:flex;
                justify-content:space-between;
                align-items:center;
                margin-bottom:20px;
            }

            .modal-close{
                font-size:22px;
                cursor:pointer;
            }

            /* FORM */

            .form-group{
                margin-bottom:18px;
                display:flex;
                flex-direction:column;
            }

            .form-group label{
                font-weight:500;
                color:#374151;
                margin-bottom:6px;
            }

            .form-group input,
            .form-group select,
            .form-group textarea{
                padding:10px;
                border-radius:6px;
                border:1px solid #ddd;
                color:#111827;   /* thêm */
                background:#fff; /* thêm */
            }

            /* UPLOAD BOX */

            .upload-box{
                border:2px dashed #ccc;
                padding:25px;
                text-align:center;
                border-radius:10px;
                color:#374151;
            }

            .upload-icon{
                font-size:28px;
                margin-bottom:10px;
            }

            /* BUTTON */

            .btn-submit{
                width:100%;
                background:#020617;
                color:white;
                padding:12px;
                border:none;
                border-radius:8px;
                cursor:pointer;
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

                    <button class="btn-upload" onclick="openUploadModal()">
                        <i class="fa fa-plus"></i>
                        Upload New Version
                    </button>

                </div>

                <div class="version-list">

                    <c:forEach var="s" items="${listVersion}">

                        <div class="version-card">

                            <div class="version-icon">
                                <i class="fa-solid fa-code-branch"></i>
                            </div>

                            <div class="version-info">

                                <div class="version-title">

                                    ${s.name} v${s.softwareVersion.versionName}

                                    <!-- ACTIVE VERSION -->
                                    <c:if test="${s.softwareVersion.isActive == 1}">
                                        <span class="badge-current">Current</span>
                                    </c:if>

                                    <!-- NOT ACTIVE -->
                                    <c:if test="${s.softwareVersion.isActive == 0}">
                                        <span class="badge-deprecated">Deprecated</span>

                                        <!-- BUTTON ACTIVE -->
                                        <form action="${pageContext.request.contextPath}/vendor/activate_version"
                                              method="post"
                                              style="display:inline; margin-left:10px;">

                                            <input type="hidden" name="versionId" value="${s.softwareVersion.versionId}">
                                            <input type="hidden" name="softwareId" value="${softwareId}">

                                            <button type="submit"
                                                    class="btn"
                                                    style="background:#22c55e; color:white;"
                                                    onclick="return confirm('Set this version as active?')">

                                                <i class="fa-solid fa-check"></i> Set Active
                                            </button>

                                        </form>
                                    </c:if>

                                </div>

                                <div class="version-meta">
                                    <span>
                                        <i class="fa-regular fa-calendar"></i>
                                        ${s.softwareVersion.createdAt.toLocalDate()}
                                    </span>

                                    <span>
                                        <i class="fa-solid fa-hard-drive"></i>
                                        <c:set var="size" value="${s.softwareVersion.fileSize}" />

                                        <c:choose>

                                            <c:when test="${size < 1024 * 1024}">
                                                <fmt:formatNumber value="${size / 1024.0}" maxFractionDigits="1"/> KB
                                            </c:when>

                                            <c:when test="${size < 1024 * 1024 * 1024}">
                                                <fmt:formatNumber value="${size / (1024.0 * 1024)}" maxFractionDigits="1"/> MB
                                            </c:when>

                                            <c:otherwise>
                                                <fmt:formatNumber value="${size / (1024.0 * 1024 * 1024)}" maxFractionDigits="1"/> GB
                                            </c:otherwise>

                                        </c:choose>
                                    </span>
                                </div>
                                <div class="release-note">
                                    <i class="fa-solid fa-file-lines"></i>
                                    ${s.softwareVersion.releaseNote}
                                </div>

                            </div>

                        </div>

                    </c:forEach>
                </div>

            </div>

        </div>
        <!-- Upload Version Modal -->
        <div id="uploadModal" class="modal">

            <div class="modal-content">

                <div class="modal-header">
                    <h3>Upload a new version of your software product</h3>
                    <span class="modal-close" onclick="closeUploadModal()">×</span>
                </div>

                <form action="upload_version" method="post" enctype="multipart/form-data">

                    <input type="hidden" name="softwareId" value="${softwareId}">

                    <div class="form-group">
                        <label>Version</label>
                        <input type="text" name="versionName" placeholder="v2.0.0" required>
                    </div>

                    <div class="form-group">
                        <label>Release note</label>
                        <textarea name="releaseNote" rows="4"
                                  placeholder="Added new feature&#10;Fixed bug in module X"></textarea>
                    </div>

                    <div class="form-group upload-box">

                        <i class="fa-solid fa-cloud-arrow-up upload-icon"></i>

                        <input type="file"
                               name="softwareFile"
                               accept=".txt"
                               required>

                        <p>Accepted formats: txt (Max 500MB)</p>

                    </div>

                    <button class="btn-submit">
                        <i class="fa-solid fa-upload"></i>
                        Create Version
                    </button>

                </form>

            </div>

        </div>
        <script>

            function openUploadModal() {
                document.getElementById("uploadModal").style.display = "flex";
            }

            function closeUploadModal() {
                document.getElementById("uploadModal").style.display = "none";
            }

            /* click outside modal */

            window.onclick = function (e) {

                const modal = document.getElementById("uploadModal");

                if (e.target === modal) {
                    modal.style.display = "none";
                }

            }

        </script>

    </body>

</html>
