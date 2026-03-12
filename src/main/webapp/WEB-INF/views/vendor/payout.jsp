<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : payout
    Created on : Mar 2, 2026, 4:02:42 PM
    Author     : MinhPD
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Payouts</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/vendor/vendor.css">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" rel="stylesheet">
        <style>
            /* ===== HEADER ===== */

            .page-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 40px;
            }

            .page-header h1 {
                margin: 0;
                font-size: 28px;
            }

            .page-header p {
                margin: 5px 0 0;
                color: #94a3b8;
            }

            .btn-primary {
                background: #6366f1;
                border: none;
                padding: 12px 20px;
                border-radius: 10px;
                color: white;
                cursor: pointer;
                font-weight: 500;
                transition: 0.3s;
            }

            .btn-primary:hover {
                background: #4f46e5;
            }



            /* ===== TABLE ===== */



            .table-header {
                margin-bottom: 20px;
            }

            .table-header h3 {
                margin: 0;
            }

            table {
                width: 100%;
                border-collapse: collapse;
            }

            thead {
                color: #94a3b8;
                font-size: 14px;
            }

            th, td {
                padding: 14px 10px;
                text-align: left;
            }

            tbody tr {
                border-top: 1px solid #334155;
            }

            .amount {
                color: #22c55e;
                font-weight: 600;
            }

            .badge-id {
                background: #0f172a;
                padding: 6px 12px;
                border-radius: 6px;
                font-size: 13px;
            }

            .status {
                padding: 6px 12px;
                border-radius: 20px;
                font-size: 13px;
                display: inline-flex;
                align-items: center;
                gap: 6px;
            }

            .pending {
                background: rgba(234,179,8,0.2);
                color: #eab308;
            }

            /* ===== MODAL ===== */

            .modal{
                display:none;
                position:fixed;
                top:0;
                left:0;
                width:100%;
                height:100%;
                background:rgba(0,0,0,0.4);
                justify-content:center;
                align-items:center;
            }

            .modal-content{
                background:#f8fafc;
                width:420px;
                padding:25px;
                border-radius:10px;
                position:relative;
            }

            .close-btn{
                position:absolute;
                right:15px;
                top:10px;
                font-size:20px;
                cursor:pointer;
            }

            .modal-text{
                font-size:14px;
                margin-bottom:20px;
            }

            .input-field{
                width:100%;
                padding:12px;
                margin-top:10px;
                border-radius:6px;
                border:1px solid #ccc;
            }

            .balance{
                font-size:13px;
                margin-top:6px;
                color:#64748b;
            }

            .submit-btn{
                margin-top:15px;
                width:100%;
                padding:12px;
                background:#0f172a;
                color:white;
                border:none;
                border-radius:8px;
                cursor:pointer;
            }
        </style>
    </head>
    <body>

        <div class="layout">

            <!-- IMPORT SIDEBAR -->
            <jsp:include page="layout/side_bar.jsp"/>

            <!-- MAIN CONTENT -->
            <div class="main">

                <!-- HEADER -->
                <div class="page-header">
                    <div>
                        <h1>Payouts</h1>
                        <p>Manage your earnings and payout requests</p>
                    </div>

                    <button class="btn-primary" id="openPayout">
                        <i class="fa-solid fa-plus"></i> Request Payout
                    </button>
                </div>



                <!-- TABLE -->
                <div class="table-card">

                    <div class="table-header">
                        <h3>Payout Requests</h3>
                    </div>

                    <table class="dashboard-table">
                        <thead>
                            <tr>
                                <th>Request ID</th>
                                <th>Amount</th>
                                <th>Request Date</th>
                                <th>Processed Date</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${list}">
                                <tr>
                                    <td><span class="badge-id">${item.payoutId}</span></td>
                                    <td class="amount">$${item.amount}</td>
                                    <td>${item.createdAt.toLocalDate()}</td>
                                    <td><c:choose>
                                            <c:when test="${not empty item.processedAt.toLocalDate()}">
                                                ${item.processedAt.toLocalDate()}
                                            </c:when>

                                            <c:otherwise>
                                                -
                                            </c:otherwise>
                                        </c:choose></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${item.status == 'PAID'}">
                                                <span class="status paid">
                                                    <i class="fa-solid fa-circle-check"></i> Paid
                                                </span>
                                            </c:when>

                                            <c:otherwise>
                                                <span class="status pending">
                                                    <i class="fa-regular fa-clock"></i> Pending
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>

                        </tbody>
                    </table>

                </div>

            </div>

        </div>
        <!-- Payout Modal -->
        <div class="modal" id="payoutModal">

            <div class="modal-content">

                <span class="close-btn" id="closePayout">&times;</span>

                <form action="/vendor/payout" method="post">

                    <p class="modal-text">
                        Minimum payout amount is $50. Funds will be transferred within 5-7 business days.
                    </p>

                    <!-- Amount -->
                    <input 
                        type="number"
                        name="amount"
                        class="input-field"
                        placeholder="Enter payout amount"
                        step="0.01"
                        min="50"
                        required>

                    <!-- Balance -->
                    <p class="balance">
                        Available balance: $${wallet.balance}
                    </p>

                    <!-- Payment Method -->
                    <select name="paymentMethod" class="input-field" required>
                        <option value="">Select payment method</option>
                        <option value="BANK">Bank Transfer</option>
                        <option value="PAYPAL">Paypal</option>
                    </select>

                    <!-- Payment Account -->
                    <input 
                        type="text"
                        name="paymentAccount"
                        class="input-field"
                        placeholder="Account number, email, or IBAN"
                        required>

                    <!-- Note -->

                    <button type="submit" class="submit-btn">
                        Submit Request
                    </button>

                </form>

            </div>

        </div>
        <script>

            const modal = document.getElementById("payoutModal");
            const openBtn = document.getElementById("openPayout");
            const closeBtn = document.getElementById("closePayout");

            openBtn.onclick = function () {
                modal.style.display = "flex";
            }

            closeBtn.onclick = function () {
                modal.style.display = "none";
            }

            window.onclick = function (event) {
                if (event.target === modal) {
                    modal.style.display = "none";
                }
            }

        </script>
    </body>

</html>