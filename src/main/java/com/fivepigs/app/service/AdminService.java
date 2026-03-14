package com.fivepigs.app.service;

import com.fivepigs.app.dao.AdminDao;
import com.fivepigs.app.util.EmailService;

import java.sql.SQLException;
import java.util.List;
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

    public PageResult<AdminDao.UserRow> getEmployeesPage(String pageParam, int pageSize, String keyword, String role, String status) {
        return getEmployeesPage(parsePage(pageParam), pageSize, keyword, role, status);
    }

    public PageResult<AdminDao.UserRow> getEmployeesPage(int page, int pageSize, String keyword, String role, String status) {
        int safePageSize = pageSize <= 0 ? 10 : pageSize;
        String normalizedKeyword = normalizeKeyword(keyword);
        String normalizedRole = normalizeRoleFilter(role);
        String normalizedStatus = normalizeStatusFilter(status);

        int totalItems = adminDao.countEmployees(normalizedKeyword, normalizedRole, normalizedStatus);
        int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / safePageSize));
        int currentPage = Math.max(1, Math.min(page, totalPages));
        int offset = (currentPage - 1) * safePageSize;

        List<AdminDao.UserRow> items = adminDao.listEmployeesPaged(safePageSize, offset, normalizedKeyword, normalizedRole, normalizedStatus);
        return new PageResult<>(items, currentPage, safePageSize, totalItems, totalPages);
    }

    public PageResult<AdminDao.UserRow> getVendorsPage(String pageParam, int pageSize, String keyword, String status) {
        return getVendorsPage(parsePage(pageParam), pageSize, keyword, status);
    }

    public PageResult<AdminDao.UserRow> getVendorsPage(int page, int pageSize, String keyword, String status) {
        int safePageSize = pageSize <= 0 ? 10 : pageSize;
        String normalizedKeyword = normalizeKeyword(keyword);
        String normalizedStatus = normalizeStatusFilter(status);

        int totalItems = adminDao.countVendors(normalizedKeyword, normalizedStatus);
        int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / safePageSize));
        int currentPage = Math.max(1, Math.min(page, totalPages));
        int offset = (currentPage - 1) * safePageSize;

        List<AdminDao.UserRow> items = adminDao.listVendorsPaged(safePageSize, offset, normalizedKeyword, normalizedStatus);
        return new PageResult<>(items, currentPage, safePageSize, totalItems, totalPages);
    }

    public PageResult<AdminDao.AdminProductRow> getProductsPage(String pageParam, int pageSize, String keyword, String status) {
        return getProductsPage(parsePage(pageParam), pageSize, keyword, status);
    }

    public PageResult<AdminDao.AdminProductRow> getProductsPage(int page, int pageSize, String keyword, String status) {
        int safePageSize = pageSize <= 0 ? 10 : pageSize;
        String normalizedKeyword = normalizeKeyword(keyword);
        String normalizedStatus = normalizeProductStatusFilter(status);

        int totalItems = adminDao.countProducts(normalizedKeyword, normalizedStatus);
        int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / safePageSize));
        int currentPage = Math.max(1, Math.min(page, totalPages));
        int offset = (currentPage - 1) * safePageSize;

        List<AdminDao.AdminProductRow> items = adminDao.listProductsPaged(safePageSize, offset, normalizedKeyword, normalizedStatus);
        return new PageResult<>(items, currentPage, safePageSize, totalItems, totalPages);
    }

    public AdminDao.AdminProductDetailRow getProductDetail(String softwareIdStr) {
        Integer softwareId = parseId(softwareIdStr);
        if (softwareId == null) {
            return null;
        }
        return adminDao.getProductDetail(softwareId);
    }

    public String updateProductStatus(String softwareIdStr, String status) throws SQLException {
        Integer softwareId = parseId(softwareIdStr);
        if (softwareId == null) {
            return "invalid_id";
        }
        if (isBlank(status)) {
            return "invalid_status";
        }
        adminDao.updateProductStatus(softwareId, status.trim().toUpperCase());
        return null;
    }

    public String updateProduct(String softwareIdStr, String name, String shortDescription, String categoryIdStr, String priceStr, String isFreeStr, String status) throws SQLException {
        Integer softwareId = parseId(softwareIdStr);
        if (softwareId == null || isBlank(name)) {
            return "invalid_id";
        }

        Integer categoryId = parseOptionalId(categoryIdStr);
        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            return "invalid_price";
        }
        int isFree = "1".equals(isFreeStr) ? 1 : 0;
        String normalizedStatus = isBlank(status) ? "ACTIVE" : status.trim().toUpperCase();

        adminDao.updateProduct(softwareId, name.trim(), shortDescription, categoryId, price, isFree, normalizedStatus);
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

    private int parsePage(String pageParam) {
        if (isBlank(pageParam)) {
            return 1;
        }
        try {
            int page = Integer.parseInt(pageParam.trim());
            return Math.max(page, 1);
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private Integer parseId(String value) {
        if (isBlank(value)) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseOptionalId(String value) {
        if (isBlank(value)) {
            return null;
        }
        return parseId(value);
    }

    private String normalizeKeyword(String keyword) {
        if (isBlank(keyword)) {
            return null;
        }
        return keyword.trim();
    }

    private String normalizeRoleFilter(String role) {
        if (isBlank(role)) {
            return null;
        }
        String value = role.trim().toLowerCase();
        if (!value.equals("reviewer") && !value.equals("approval") && !value.equals("aproval")) {
            return null;
        }
        return value;
    }

    private String normalizeStatusFilter(String status) {
        if (isBlank(status)) {
            return null;
        }
        String value = status.trim().toUpperCase();
        if (!ACTIVE_STATUSES.contains(value)) {
            return null;
        }
        return value;
    }

    private String normalizeProductStatusFilter(String status) {
        if (isBlank(status)) {
            return null;
        }
        return status.trim().toUpperCase();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public static class PageResult<T> {
        private final List<T> items;
        private final int currentPage;
        private final int pageSize;
        private final int totalItems;
        private final int totalPages;

        public PageResult(List<T> items, int currentPage, int pageSize, int totalItems, int totalPages) {
            this.items = items;
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalItems = totalItems;
            this.totalPages = totalPages;
        }

        public List<T> getItems() {
            return items;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getTotalItems() {
            return totalItems;
        }

        public int getTotalPages() {
            return totalPages;
        }
    }
}

