<%-- 
    Document   : plan_management
    Created on : Mar 22, 2026, 10:15:52 PM
    Author     : MinhPD
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>Plan Management</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/vendor/vendor.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">


        <style>
            .header{
                display:flex;
                justify-content:space-between;
                align-items:center;
                margin-bottom:20px;
            }

            .btn-primary{
                background:#6366f1;
                color:white;
                padding:10px 16px;
                border-radius:8px;
                cursor:pointer;
                border:none;
            }

            .btn-primary:hover{
                background:#4f46e5;
            }







            /* MODAL */
            .modal{
                display:none;
                position:fixed;
                top:0;
                left:0;
                width:100%;
                height:100%;
                background:rgba(0,0,0,0.6);
                justify-content:center;
                align-items:center;
            }

            .modal-content{
                background:#1e293b;
                padding:25px;
                border-radius:12px;
                width:400px;
            }

            input{
                width:100%;
                padding:10px;
                margin-bottom:12px;
                border-radius:6px;
                border:none;
                background:#0f172a;
                color:white;
            }
            .status.active {
                background: rgba(34,197,94,0.15);
                color: #22c55e;
            }
            .status.inactive {
                background: rgba(239,68,68,0.15);
                color: #ef4444;
            }
        </style>


    </head>

    <body>

        <div class="layout">
            <jsp:include page="layout/side_bar.jsp"/>

            <div class="main">

                <div class="header">
                    <h1>Plan Management</h1>

                    <button class="btn-primary" onclick="openModal()">
                        + New Plan
                    </button>
                </div>
                <c:if test="${param.error == 'duplicate'}">
                    <p style="color:red;">Plan name already exists</p>
                </c:if>

                <c:if test="${param.error == 'reserved'}">
                    <p style="color:red;">Cannot use BASIC or DEMO</p>
                </c:if>

                <c:if test="${param.error == 'invalid'}">
                    <p style="color:red;">Invalid input</p>
                </c:if>
                <div class="table-card">
                    <table class="dashboard-table">
                        <thead>
                            <tr>
                                <th>Plan</th>
                                <th>Max Users</th>
                                <th>Price</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach var="p" items="${list}">
                                <tr>
                                    <td>${p.planName}</td>
                                    <td>${p.maxUsers}</td>
                                    <td>$${p.price}</td>

                                    <td>
                                        <c:choose>
                                            <c:when test="${p.isActive == 1}">
                                                <span class="status active">ACTIVE</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status inactive">INACTIVE</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td>
                                        <form action="/vendor/toggle_plan" method="post">
                                            <input type="hidden" name="pricingId" value="${p.pricingId}"/>
                                            <input type="hidden" name="softwareId" value="${softwareId}"/>

                                            <button class="btn-primary" type="submit">
                                                <c:choose>
                                                    <c:when test="${p.isActive == 1}">
                                                        Deactivate
                                                    </c:when>
                                                    <c:otherwise>
                                                        Activate
                                                    </c:otherwise>
                                                </c:choose>
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

            </div>


        </div>

        <!-- MODAL -->

        <div class="modal" id="modal">
            <div class="modal-content">

                ```
                <h3>Create New Plan</h3>

                <form action="/vendor/create_plan" method="post">

                    <input type="hidden" name="softwareId" value="${softwareId}"/>

                    <input type="text" name="planName" placeholder="Plan name (TEAM / PRO)" required/>
                    <input type="number" name="maxUsers" placeholder="Max users" required/>
                    <input type="number" step="0.01" name="price" placeholder="Price" required/>

                    <button class="btn-primary" type="submit">Create</button>
                </form>

            </div>


        </div>

        <script>
            function openModal() {
                document.getElementById("modal").style.display = "flex";
            }

            window.onclick = function (e) {
                if (e.target.id === "modal") {
                    document.getElementById("modal").style.display = "none";
                }
            }
        </script>

    </body>
</html>
