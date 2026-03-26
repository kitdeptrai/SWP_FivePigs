<%-- 
    Document   : sidebar
    Created on : Feb 21, 2026, 5:44:04 PM
    Author     : thanh
--%>
        <aside class="sidebar">
            <div class="logo">
                <img src="assets/css/images/logo.png" alt="Pig Logo">
                <div>
                    <h2 class="prjName">Fivepigs</h2>
                    <small class="subti">Software Market</small>
                </div>
            </div>

            <ul class="menu">
                <ul class="menu">
                    <li><a href="approval_dashboard?page=dashboard" class="${param.page eq 'dashboard' ? 'active' : ''}">Dashboard</a></li>
                    <li><a href="approval_pending?page=approval_pending" class="${param.page eq 'approval_pending' ? 'active' : ''}">Pending Approval</a></li>
                    <li><a href="approval_history?page=approval_history" class="${param.page eq 'approval_history' ? 'active' : ''}">Approval History</a></li>
                    <li><a href="ApprovalErrorReports?page=error_reports" class="${param.page eq 'error_reports' ? 'active' : ''}">Error Reports</a></li>
                    <li><a href="approval_notifications?page=notifications" class="${param.page eq 'notifications' ? 'active' : ''}">Notifications</a></li>
                    <li><a href="approval_profile?page=profile" class="${param.page eq 'profile' ? 'active' : ''}">Profile</a></li>
                </ul>
                <!--<li><a>Approval History</a></li>-->
            </ul>            <div class="sidebar-bottom" style="margin-top: auto;">
                <div class="user">
                    <div class="avatar">AP</div>
                    <div>
                        <strong>Tuan Thanh</strong>
                        <br><small>Approval</small></br>
                    </div>
                </div>

                <form action="<%= request.getContextPath() %>/logout" method="get">
                    <button type="submit" class="logout" style="width: 100%;">Logout</button>
                </form>
            </div>

        </aside>
