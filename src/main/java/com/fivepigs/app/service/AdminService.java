package com.fivepigs.app.service;

import com.fivepigs.app.dao.AdminDao;
import com.fivepigs.app.util.EmailService;

import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminService {
    private static final String DEFAULT_PASSWORD = "123456";

    private static final Set<String> EMPLOYEE_ROLES = Set.of("reviewer", "approval", "aproval");
    private static final Set<String> USER_ROLES = Set.of("customer", "vendor");
    private static final Set<String> ACTIVE_STATUSES = Set.of("ACTIVE", "INACTIVE");

    private static final ExecutorService EMAIL_EXECUTOR = Executors.newFixedThreadPool(2);

    private final AdminDao adminDao;

    public AdminService() {
        this.adminDao = new AdminDao();
    }

    public String createEmployee(String fullName, String email, String phone, String roleName) throws SQLException {
        return createUserByRoleScope(fullName, email, phone, roleName, EMPLOYEE_ROLES, true);
    }

    public String createUser(String fullName, String email, String phone, String roleName) throws SQLException {
        return createUserByRoleScope(fullName, email, phone, roleName, USER_ROLES, false);
    }

    public String updateEmployee(String userIdStr, String fullName, String phone, String status, String roleName) throws SQLException {
        return updateUserByRoleScope(userIdStr, fullName, phone, status, roleName, EMPLOYEE_ROLES);
    }

    public String updateUser(String userIdStr, String fullName, String phone, String status, String roleName) throws SQLException {
        return updateUserByRoleScope(userIdStr, fullName, phone, status, roleName, USER_ROLES);
    }

    public String setUserStatus(String userIdStr, String status) throws SQLException {
        if (isBlank(userIdStr)) {
            return "missing_id";
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdStr.trim());
        } catch (NumberFormatException e) {
            return "invalid_id";
        }

        String normalizedStatus = status == null ? null : status.trim().toUpperCase();
        if (normalizedStatus == null || !ACTIVE_STATUSES.contains(normalizedStatus)) {
            return "invalid_status";
        }

        adminDao.setUserStatus(userId, normalizedStatus);
        return null;
    }

    private String createUserByRoleScope(String fullName,
                                         String email,
                                         String phone,
                                         String roleName,
                                         Set<String> allowedRoles,
                                         boolean employeeAccount) throws SQLException {
        if (isBlank(fullName) || isBlank(email) || isBlank(roleName)) {
            return "missing_fields";
        }

        String emailTrimmed = email.trim();
        if (adminDao.emailExists(emailTrimmed)) {
            return "email_exists";
        }

        String roleTrimmed = roleName.trim();
        if (!isValidRole(roleTrimmed, allowedRoles)) {
            return "invalid_role";
        }

        String normalizedPhone = phone == null ? null : phone.trim();
        if (normalizedPhone != null && normalizedPhone.isEmpty()) {
            normalizedPhone = null;
        }

        String fullNameTrimmed = fullName.trim();
        adminDao.createUser(fullNameTrimmed, emailTrimmed, normalizedPhone, roleTrimmed);
        sendWelcomeEmailAsync(fullNameTrimmed, emailTrimmed, employeeAccount);

        return null;
    }

    private String updateUserByRoleScope(String userIdStr,
                                         String fullName,
                                         String phone,
                                         String status,
                                         String roleName,
                                         Set<String> allowedRoles) throws SQLException {
        if (isBlank(userIdStr) || isBlank(fullName) || isBlank(status) || isBlank(roleName)) {
            return "missing_fields";
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdStr.trim());
        } catch (NumberFormatException e) {
            return "invalid_id";
        }

        String roleTrimmed = roleName.trim();
        if (!isValidRole(roleTrimmed, allowedRoles)) {
            return "invalid_role";
        }

        String normalizedStatus = status.trim().toUpperCase();
        if (!ACTIVE_STATUSES.contains(normalizedStatus)) {
            return "invalid_status";
        }

        String normalizedPhone = phone == null ? null : phone.trim();
        if (normalizedPhone != null && normalizedPhone.isEmpty()) {
            normalizedPhone = null;
        }

        adminDao.updateUser(userId, fullName.trim(), normalizedPhone, normalizedStatus, roleTrimmed);
        return null;
    }

    private void sendWelcomeEmailAsync(String fullName, String email, boolean employeeAccount) {
        String subject = employeeAccount
                ? "Tài khoản nhân viên FivePigs của bạn đã được tạo"
                : "Tài khoản FivePigs của bạn đã được tạo";

        String accountLabel = employeeAccount ? "Tài khoản nhân viên" : "Tài khoản";

        String body = "<div style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                + "<h2>Chào mừng " + fullName + "!</h2>"
                + "<p>" + accountLabel + " của bạn trên hệ thống FivePigs đã được tạo thành công.</p>"
                + "<p><strong>Email đăng nhập:</strong> " + email + "</p>"
                + "<p><strong>Mật khẩu mặc định:</strong> " + DEFAULT_PASSWORD + "</p>"
                + "<p>Vui lòng đăng nhập và đổi mật khẩu ngay để bảo mật tài khoản.</p>"
                + "<p>Trân trọng,<br/>Đội ngũ Quản trị FivePigs</p>"
                + "</div>";

        EMAIL_EXECUTOR.submit(() -> EmailService.sendHtmlEmail(email, subject, body));
    }

    private boolean isValidRole(String roleName, Set<String> allowedRoles) {
        return allowedRoles.contains(roleName.toLowerCase());
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}

