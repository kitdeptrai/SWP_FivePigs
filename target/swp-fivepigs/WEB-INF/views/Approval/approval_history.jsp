<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Approval History</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Approval/approval.css">
    </head>
    <body>

        <div class="app">
            <!-- SIDEBAR -->
            <jsp:include page="./layout/sidebar.jsp"/>

            <!-- CONTENT -->
            <div class="content-history">
                <div class="history-wrap">
                    <h1 class="history-title">Approval History</h1>
                    <p class="history-sub">Your past approval decisions</p>

                    <div class="history-card">
                        <table class="history-table">
                            <thead>
                                <tr>
                                    <th>App Name</th>
                                    <th>Vendor</th>
                                    <th>Decision Date</th>
                                    <th>Decision</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="it" items="${approvalHistory}">
                                    <c:if test="${it.approvalProcess.decision == 'APPROVED' || it.approvalProcess.decision == 'REJECTED'}">
                                        <tr>
                                            <td class="col-app"><c:out value="${it.appName}" /></td>
                                            <td><c:out value="${it.user.fullName}" /></td>
                                            <td class="col-date">
                                                <c:out value="${it.approvalProcess.approval_date}" />
                                            </td>
                                            <td class="col-decision">
                                                <c:out value="${it.approvalProcess.decision}" />
                                            </td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/approval_history_detail?softwareId=${it.softwareId}" class="btn-details">View Details</a>
                                            </td>
                                        </tr>
                                    </c:if>
                                </c:forEach>

                                <c:if test="${empty approvalHistory}">
                                    <tr>
                                        <td colspan="5" class="empty">No approval history.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>