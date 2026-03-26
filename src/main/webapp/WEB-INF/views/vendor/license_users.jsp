<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>License Users</title>

        <!-- dùng lại CSS có sẵn -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/vendor/vendor.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
        <style>
            .status.revoked {
                background: rgba(239,68,68,0.15);
                color: #ef4444;
            }

            /* ACTIONS */
            .actions {
                white-space: nowrap;
            }

            .action-btn {
                width: 32px;
                height: 32px;
                border-radius: 50%;
                background: #0f172a;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                margin-right: 6px;
                cursor: pointer;
                transition: 0.2s;
            }

            .action-btn:hover {
                background: #475569;
            }

            .action-btn.revoke:hover {
                background: #7f1d1d;
            }
            .btn-primary{
                background:#4f46e5;
                padding:10px 18px;
                border-radius:10px;
                text-decoration:none;
                color:white;
                font-weight:500;
                transition:0.2s;
            }

            .btn-primary:hover{
                background:#4338ca;
            }
        </style>
    </head>

    <body>
        <div class="layout">

            <!-- SIDEBAR -->
            <jsp:include page="layout/side_bar.jsp"/>

            <div class="main">

                <!-- HEADER -->
                <h1>License Users</h1>
                <p class="subtitle">Manage users assigned to this license</p>

                <!-- ===== MAX USERS CARD ===== -->
                <div class="table-container" style="margin-bottom:20px;">

                    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:15px;">
                        <div>
                            <h3>Usage</h3>
                            <c:if test="${not empty param.error}">
                                <div style="
                                     background: rgba(239,68,68,0.15);
                                     color:#ef4444;
                                     padding:10px;
                                     border-radius:10px;
                                     margin:10px 0;
                                     font-size:14px;
                                     ">
                                    ${param.error}
                                </div>
                            </c:if>
                            <p style="color:#94a3b8;">
                                ${listUsers.size()} / ${license.maxUsers} users
                            </p>
                        </div>
                    </div>

                    <!-- UPDATE MAX USERS -->
                    <form action="${pageContext.request.contextPath}/vendor/update_maxuser"
                          method="post"
                          style="display:flex; gap:10px; align-items:center;">

                        <input type="hidden" name="licenseId" value="${license.licenseId}">

                        <input type="number"
                               name="maxUsers"
                               value="${license.maxUsers}"
                               min="1"
                               style="padding:10px;
                               border-radius:10px;
                               border:1px solid #334155;
                               background:#020617;
                               color:white;
                               outline:none;">

                        <button type="submit" class="btn-primary">
                            <i class="fa-solid fa-pen"></i> Update MaxUsers
                        </button>
                    </form>

                </div>

                <!-- ===== TABLE ===== -->
                <div class="table-card">

                    <table class="dashboard-table">
                        <thead>
                            <tr>
                                <th>User</th>
                                <th>Email</th>

                                <th>Action</th>
                            </tr>
                        </thead>

                        <tbody>

                            <c:forEach var="u" items="${listUsers}">
                                <tr>

                                    <!-- USER -->
                                    <td>
                                        <div>${u.fullName}</div>
                                    </td>

                                    <!-- EMAIL -->
                                    <td class="customer-email">
                                        ${u.email}
                                    </td>

                                    <!-- ASSIGNED -->


                                    <!-- ACTION -->
                                    <td class="actions">

                                        <c:choose>
                                            <c:when test="${u.userId == license.ownerId}">
                                                <span style="
                                                      background: rgba(59,130,246,0.15);
                                                      color:#3b82f6;
                                                      padding:4px 10px;
                                                      border-radius:999px;
                                                      font-size:12px;
                                                      font-weight:600;">
                                                    OWNER
                                                </span>
                                            </c:when>


                                            <c:otherwise>
                                                <form action="${pageContext.request.contextPath}/vendor/remove-user-license"
                                                      method="post"
                                                      style="display:inline;">

                                                    <input type="hidden" name="licenseId" value="${license.licenseId}">
                                                    <input type="hidden" name="userId" value="${u.userId}">

                                                    <button type="submit"
                                                            class="action-btn revoke"
                                                            onclick="return confirm('Remove this user?')">
                                                        <i class="fa-solid fa-trash"></i>
                                                    </button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                </tr>
                            </c:forEach>

                            <c:if test="${empty listUsers}">
                                <tr>
                                    <td colspan="4" style="text-align:center; color:#94a3b8;">
                                        No users in this license
                                    </td>
                                </tr>
                            </c:if>

                        </tbody>
                    </table>

                </div>

            </div>
        </div>
    </body>
</html>