<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Approval Profile</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Approval/approval.css">
        <style>
            .profile-panel {
                background: #fff;
                padding: 24px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                margin-bottom: 24px;
            }
            .profile-panel h3 { margin-top: 0; margin-bottom: 16px; color: #333;}
            .form-group { margin-bottom: 16px; }
            .form-group label { display: block; margin-bottom: 6px; font-weight: bold; color: #555;}
            .form-group input {
                width: 100%; max-width: 500px; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; font-size: 14px;
            }
            .btn-primary {
                background: #4caf50; color: white; border: none; padding: 10px 20px; border-radius: 4px; cursor: pointer; font-size: 15px; font-weight: bold;
            }
            .btn-primary:hover { background: #43a047; }
            .alert-success { color: #155724; background-color: #d4edda; border: 1px solid #c3e6cb; padding: 12px; margin-bottom: 16px; border-radius: 4px; max-width: 800px;}
            .alert-danger { color: #721c24; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 12px; margin-bottom: 16px; border-radius: 4px; max-width: 800px;}
        </style>
    </head>
    <body>

        <div class="app">
            <!-- SIDEBAR -->
            <jsp:include page="./layout/sidebar.jsp"/>

            <!-- MAIN -->
            <main class="main">
                <h1>Profile Settings</h1>
                <p class="subtitle">Manage your account information and password</p>

                <c:if test="${not empty success}">
                    <div class="alert-success">${success}</div>
                </c:if>

                <c:if test="${not empty error}">
                    <div class="alert-danger">${error}</div>
                </c:if>

                <!-- Profile Info -->
                <section class="profile-panel" style="max-width: 800px;">
                    <h3>Personal Information</h3>
                    <div class="form-group">
                        <label>Full Name</label>
                        <input type="text" value="${profileUser.fullName}" readonly style="background-color: #f1f3f5; color: #555;"/>
                    </div>
                    <div class="form-group">
                        <label>Email Address</label>
                        <input type="email" value="${profileUser.email}" readonly style="background-color: #f1f3f5; color: #555;"/>
                    </div>
                    <div class="form-group">
                        <label>Role</label>
                        <input type="text" value="Approval" readonly style="background-color: #f1f3f5; color: #555;"/>
                    </div>
                </section>

                <!-- Change Password -->
                <section class="profile-panel" style="max-width: 800px;">
                    <h3>Change Password</h3>
                    <form action="${pageContext.request.contextPath}/approval_profile" method="post">
                        
                        <div class="form-group">
                            <label>New Password</label>
                            <input type="password" name="newPassword" required minlength="6" maxlength="72" placeholder="Enter new password"/>
                        </div>
                        <div class="form-group">
                            <label>Confirm New Password</label>
                            <input type="password" name="confirmPassword" required minlength="6" maxlength="72" placeholder="Confirm new password"/>
                        </div>
                        
                        <button type="submit" class="btn-primary">Update Password</button>
                    </form>
                </section>

            </main>
        </div>
    </body>
</html>
