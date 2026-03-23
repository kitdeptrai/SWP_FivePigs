package com.fivepigs.app.service;

import com.fivepigs.app.dao.AdminDao;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class AdminService {
    private static final Set<String> EMPLOYEE_ROLES = Set.of("reviewer", "approval", "aproval");
    private static final Set<String> ACTIVE_STATUSES = Set.of("ACTIVE", "INACTIVE");
    private static final Set<String> PAYOUT_STATUSES = Set.of("PENDING", "PAID");
    private static final Set<String> REPORT_STATUSES = Set.of("PENDING", "ERROR_REVIEW", "ERROR_APPROVAL", "REJECTED", "ERROR_REJECTED");

    private final AdminDao adminDao;

    public AdminService() {
        this.adminDao = new AdminDao();
    }

    public String updateEmployee(String userIdStr, String fullName, String phone, String status, String roleName) throws SQLException {
        return updateUserByRoleScope(userIdStr, fullName, phone, status, roleName, EMPLOYEE_ROLES);
    }

    public String updateVendor(String userIdStr, String fullName, String phone, String status, String roleName) throws SQLException {
        return updateUserByRoleScope(userIdStr, fullName, phone, status, roleName, Set.of("vendor"));
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

    public PageResult<AdminDao.AdminProductReportRow> getProductsPage(String pageParam, int pageSize, String keyword, String status) {
        return getProductsPage(parsePage(pageParam), pageSize, keyword, status);
    }

    public PageResult<AdminDao.AdminOrderRow> getSuccessfulOrdersPage(String pageParam,
                                                                      int pageSize,
                                                                      String keyword,
                                                                      String fromDate,
                                                                      String toDate) {
        return getSuccessfulOrdersPage(parsePage(pageParam), pageSize, keyword, fromDate, toDate);
    }

    public PageResult<AdminDao.AdminOrderRow> getSuccessfulOrdersPage(int page,
                                                                      int pageSize,
                                                                      String keyword,
                                                                      String fromDate,
                                                                      String toDate) {
        int safePageSize = pageSize <= 0 ? 10 : pageSize;
        String normalizedKeyword = normalizeKeyword(keyword);
        java.sql.Date normalizedFromDate = parseSqlDate(fromDate);
        java.sql.Date normalizedToDate = parseSqlDate(toDate);
        if (normalizedFromDate != null && normalizedToDate != null && normalizedFromDate.after(normalizedToDate)) {
            java.sql.Date tmp = normalizedFromDate;
            normalizedFromDate = normalizedToDate;
            normalizedToDate = tmp;
        }

        int totalItems = adminDao.countSuccessfulOrders(normalizedKeyword, normalizedFromDate, normalizedToDate);
        int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / safePageSize));
        int currentPage = Math.max(1, Math.min(page, totalPages));
        int offset = (currentPage - 1) * safePageSize;

        List<AdminDao.AdminOrderRow> items = adminDao.listSuccessfulOrdersPaged(
                safePageSize,
                offset,
                normalizedKeyword,
                normalizedFromDate,
                normalizedToDate
        );
        return new PageResult<>(items, currentPage, safePageSize, totalItems, totalPages);
    }

    public List<AdminDao.AdminOrderDetailRow> getOrderDetails(String orderIdStr) {
        Integer orderId = parseId(orderIdStr);
        if (orderId == null) {
            return java.util.Collections.emptyList();
        }
        return adminDao.listOrderDetails(orderId);
    }

    public PageResult<AdminDao.VendorPayoutRow> getVendorPayoutsPage(String pageParam,
                                                                     int pageSize,
                                                                     String status,
                                                                     String keyword,
                                                                     String fromDate,
                                                                     String toDate,
                                                                     String sortById) {
        return getVendorPayoutsPage(parsePage(pageParam), pageSize, status, keyword, fromDate, toDate, sortById);
    }

    public PageResult<AdminDao.VendorPayoutRow> getVendorPayoutsPage(int page,
                                                                     int pageSize,
                                                                     String status,
                                                                     String keyword,
                                                                     String fromDate,
                                                                     String toDate,
                                                                     String sortById) {
        int safePageSize = pageSize <= 0 ? 10 : pageSize;
        String normalizedStatus = normalizePayoutStatusFilter(status);
        String normalizedKeyword = normalizeKeyword(keyword);
        java.sql.Date normalizedFromDate = parseSqlDate(fromDate);
        java.sql.Date normalizedToDate = parseSqlDate(toDate);
        if (normalizedFromDate != null && normalizedToDate != null && normalizedFromDate.after(normalizedToDate)) {
            java.sql.Date tmp = normalizedFromDate;
            normalizedFromDate = normalizedToDate;
            normalizedToDate = tmp;
        }
        String normalizedSortById = normalizePayoutSort(sortById);

        int totalItems = adminDao.countVendorPayouts(normalizedStatus, normalizedKeyword, normalizedFromDate, normalizedToDate);
        int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / safePageSize));
        int currentPage = Math.max(1, Math.min(page, totalPages));
        int offset = (currentPage - 1) * safePageSize;

        List<AdminDao.VendorPayoutRow> items = adminDao.listVendorPayoutsPaged(
                safePageSize,
                offset,
                normalizedStatus,
                normalizedKeyword,
                normalizedFromDate,
                normalizedToDate,
                normalizedSortById
        );
        return new PageResult<>(items, currentPage, safePageSize, totalItems, totalPages);
    }

    public String approveVendorPayout(String payoutIdStr, Integer adminUserId) throws SQLException {
        Integer payoutId = parseId(payoutIdStr);
        if (payoutId == null) {
            return "invalid_id";
        }

        AdminDao.ApprovePayoutResult result = adminDao.approveVendorPayout(payoutId, adminUserId);
        if (!result.isApproved()) {
            return result.getErrorCode() == null ? "invalid_state" : result.getErrorCode();
        }

        return null;
    }

    public PageResult<AdminDao.AdminProductReportRow> getProductsPage(int page, int pageSize, String keyword, String status) {
        int safePageSize = pageSize <= 0 ? 10 : pageSize;
        String normalizedKeyword = normalizeKeyword(keyword);
        String normalizedStatus = normalizeReportStatusFilter(status);

        int totalItems = adminDao.countProducts(normalizedKeyword, normalizedStatus);
        int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / safePageSize));
        int currentPage = Math.max(1, Math.min(page, totalPages));
        int offset = (currentPage - 1) * safePageSize;

        List<AdminDao.AdminProductReportRow> items = adminDao.listProductsPaged(safePageSize, offset, normalizedKeyword, normalizedStatus);
        return new PageResult<>(items, currentPage, safePageSize, totalItems, totalPages);
    }

    public String approveReport(String reportIdStr) throws SQLException {
        Integer reportId = parseId(reportIdStr);
        if (reportId == null) {
            return "invalid_id";
        }
        boolean updated = adminDao.updateReportStatus(reportId, "PENDING", "ERROR_REVIEW");
        return updated ? null : "invalid_state";
    }

    public String rejectReport(String reportIdStr) throws SQLException {
        Integer reportId = parseId(reportIdStr);
        if (reportId == null) {
            return "invalid_id";
        }
        boolean updated = adminDao.updateReportStatus(reportId, "PENDING", "REJECTED");
        return updated ? null : "invalid_state";
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

    private String normalizeReportStatusFilter(String status) {
        if (isBlank(status)) {
            return null;
        }
        String value = status.trim().toUpperCase();
        if (!REPORT_STATUSES.contains(value)) {
            return null;
        }
        return value;
    }

    private String normalizePayoutStatusFilter(String status) {
        if (isBlank(status)) {
            return null;
        }
        String value = status.trim().toUpperCase();
        if (!PAYOUT_STATUSES.contains(value)) {
            return null;
        }
        return value;
    }

    private String normalizePayoutSort(String sortById) {
        if ("asc".equalsIgnoreCase(sortById)) {
            return "asc";
        }
        return "desc";
    }

    private java.sql.Date parseSqlDate(String value) {
        if (isBlank(value)) {
            return null;
        }
        try {
            return java.sql.Date.valueOf(value.trim());
        } catch (IllegalArgumentException ex) {
            return null;
        }
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

