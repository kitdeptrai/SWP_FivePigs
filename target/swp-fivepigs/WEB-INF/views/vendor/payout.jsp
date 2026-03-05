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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/vendor.css">
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

                    <button class="btn-primary">
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
                            <tr>
                                <td><span class="badge-id">p1</span></td>
                                <td class="amount">$1250.50</td>
                                <td>2023-10-25</td>
                                <td>-</td>
                                <td>
                                    <span class="status pending">
                                        <i class="fa-regular fa-clock"></i> Pending
                                    </span>
                                </td>
                            </tr>
                        </tbody>
                    </table>

                </div>

            </div>

        </div>

    </body>
</html>