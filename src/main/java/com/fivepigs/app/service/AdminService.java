package com.fivepigs.app.service;

import com.fivepigs.app.dao.AdminDao;
import com.fivepigs.app.dao.NotificationDao;

import com.fivepigs.app.util.EmailService;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class AdminService {
    // Tập giá trị hợp lệ để kiểm tra input từ tầng Servlet trước khi gọi DAO.
    private static final Set<String> EMPLOYEE_ROLES = Set.of("reviewer", "approval", "aproval");
    private static final Set<String> ACTIVE_STATUSES = Set.of("ACTIVE", "INACTIVE");
    private static final Set<String> PAYOUT_STATUSES = Set.of("PENDING", "PAID");
    private static final Set<String> REPORT_STATUSES = Set.of("PENDING", "ERROR_REVIEW", "ERROR_APPROVAL", "REJECTED", "ERROR_REJECTED");
    private static final Set<String> FEEDBACK_STATUSES = Set.of("NEW", "READ");

    private final AdminDao adminDao;

    // Service admin dùng chung một DAO để xử lý toàn bộ nghiệp vụ back-office.
    public AdminService() {
        this.adminDao = new AdminDao();
    }

    // Cập nhật hồ sơ nhân viên (reviewer/approval) trong phạm vi role cho phép.
    public String updateEmployee(String userIdStr, String fullName, String phone, String status, String roleName) throws SQLException {
        return updateUserByRoleScope(userIdStr, fullName, phone, status, roleName, EMPLOYEE_ROLES);
    }

    // Đổi trạng thái ACTIVE/INACTIVE của user và phát thông báo nội bộ cho admin.
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

        // Broadcast to admins
        NotificationDao notifDao = new NotificationDao();
        notifDao.broadcastToAdmins(
            "User Status Updated",
            "User ID " + userId + " status was changed to " + normalizedStatus + ".",
            "USER_UPDATE",
            "MEDIUM",
            "/admin/employees"
        );

        return null;
    }

    // Overload nhận page dạng String từ query param.
    public PageResult<AdminDao.UserRow> getEmployeesPage(String pageParam, int pageSize, String keyword, String role, String status) {
        return getEmployeesPage(parsePage(pageParam), pageSize, keyword, role, status);
    }

    // Lấy danh sách nhân viên có lọc + phân trang.
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

    // Overload nhận page dạng String từ query param.
    public PageResult<AdminDao.UserRow> getVendorsPage(String pageParam, int pageSize, String keyword, String status) {
        return getVendorsPage(parsePage(pageParam), pageSize, keyword, status);
    }

    // Lấy danh sách vendor có lọc + phân trang.
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

    // Overload nhận page dạng String từ query param.
    public PageResult<AdminDao.AdminProductReportRow> getProductsPage(String pageParam, int pageSize, String keyword, String status) {
        return getProductsPage(parsePage(pageParam), pageSize, keyword, status);
    }

    // Overload nhận page dạng String từ query param.
    public PageResult<AdminDao.AdminUserFeedbackRow> getUserFeedbackPage(String pageParam, int pageSize, String keyword, String status) {
        return getUserFeedbackPage(parsePage(pageParam), pageSize, keyword, status);
    }

    // Overload nhận page dạng String từ query param.
    public PageResult<AdminDao.AdminOrderRow> getSuccessfulOrdersPage(String pageParam,
                                                                      int pageSize,
                                                                      String keyword,
                                                                      String fromDate,
                                                                      String toDate) {
        return getSuccessfulOrdersPage(parsePage(pageParam), pageSize, keyword, fromDate, toDate);
    }

    // Danh sách đơn hàng đã thanh toán, có lọc theo keyword và khoảng ngày.
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

    // Trả về chi tiết sản phẩm trong đơn; nếu id không hợp lệ thì trả list rỗng.
    public List<AdminDao.AdminOrderDetailRow> getOrderDetails(String orderIdStr) {
        Integer orderId = parseId(orderIdStr);
        if (orderId == null) {
            return java.util.Collections.emptyList();
        }
        return adminDao.listOrderDetails(orderId);
    }

    // Overload nhận page dạng String từ query param.
    public PageResult<AdminDao.VendorPayoutRow> getVendorPayoutsPage(String pageParam,
                                                                     int pageSize,
                                                                     String status,
                                                                     String keyword,
                                                                     String fromDate,
                                                                     String toDate,
                                                                     String sortById) {
        return getVendorPayoutsPage(parsePage(pageParam), pageSize, status, keyword, fromDate, toDate, sortById);
    }

    // Danh sách yêu cầu rút tiền của vendor có lọc + sắp xếp + phân trang.
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

    // Duyệt payout: validate id và trả mã lỗi theo trạng thái xử lý của DAO.
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

    // Danh sách report sản phẩm cho trang admin/products.
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

    // Danh sách feedback user cho admin/reports.
    public PageResult<AdminDao.AdminUserFeedbackRow> getUserFeedbackPage(int page, int pageSize, String keyword, String status) {
        int safePageSize = pageSize <= 0 ? 10 : pageSize;
        String normalizedKeyword = normalizeKeyword(keyword);
        String normalizedStatus = normalizeFeedbackStatusFilter(status);

        int totalItems = adminDao.countUserFeedback(normalizedKeyword, normalizedStatus);
        int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / safePageSize));
        int currentPage = Math.max(1, Math.min(page, totalPages));
        int offset = (currentPage - 1) * safePageSize;

        List<AdminDao.AdminUserFeedbackRow> items = adminDao.listUserFeedbackPaged(safePageSize, offset, normalizedKeyword, normalizedStatus);
        return new PageResult<>(items, currentPage, safePageSize, totalItems, totalPages);
    }

    // Duyệt report sản phẩm: chuyển từ PENDING sang ERROR_REVIEW.
    public String approveReport(String reportIdStr) throws SQLException {
        Integer reportId = parseId(reportIdStr);
        if (reportId == null) {
            return "invalid_id";
        }
        boolean updated = adminDao.updateReportStatus(reportId, "PENDING", "ERROR_REVIEW");
        return updated ? null : "invalid_state";
    }

    // Từ chối report sản phẩm: chuyển từ PENDING sang REJECTED.
    public String rejectReport(String reportIdStr) throws SQLException {
        Integer reportId = parseId(reportIdStr);
        if (reportId == null) {
            return "invalid_id";
        }
        boolean updated = adminDao.updateReportStatus(reportId, "PENDING", "REJECTED");
        return updated ? null : "invalid_state";
    }

    // Lấy chi tiết feedback để hiển thị popup/trang chi tiết.
    public AdminDao.AdminUserFeedbackDetailRow getFeedbackDetail(String feedbackIdStr) {
        Integer feedbackId = parseId(feedbackIdStr);
        if (feedbackId == null) {
            return null;
        }
        return adminDao.getUserFeedbackDetail(feedbackId);
    }

    // Đánh dấu feedback đã đọc và gửi email xác nhận cho người dùng (nếu có email).
    public String markFeedbackAsRead(String feedbackIdStr) throws SQLException {
        Integer feedbackId = parseId(feedbackIdStr);
        if (feedbackId == null) {
            return "invalid_id";
        }

        AdminDao.AdminUserFeedbackDetailRow detail = adminDao.getUserFeedbackDetail(feedbackId);
        if (detail == null) {
            return "not_found";
        }

        boolean updated = adminDao.markUserFeedbackAsRead(feedbackId);
        if (!updated) {
            return "invalid_state";
        }

        String toEmail = detail.getUserEmail();
        if (toEmail != null && !toEmail.isBlank()) {
            String subject = "Your feedback has been received by FivePigs Admin";
            String htmlBody = "<p>Hello " + escapeHtml(detail.getUserName()) + ",</p>"
                    + "<p>We have received your feedback and marked it as read.</p>"
                    + "<p><strong>Feedback ID:</strong> #" + detail.getFeedbackId() + "<br/>"
                    + "<strong>Subject:</strong> " + escapeHtml(detail.getSubject()) + "<br/>"
                    + "<strong>Status:</strong> READ</p>"
                    + "<p>Thank you for helping us improve FivePigs.</p>"
                    + "<p>Best regards,<br/>FivePigs Admin Team</p>";
            try {
                EmailService.sendHtmlEmail(toEmail, subject, htmlBody);
            } catch (RuntimeException ignored) {
                // Do not fail the admin action if email sending fails.
            }
        }

        return null;
    }

    // Lấy thông tin chi tiết sản phẩm cho trang admin/product_detail.
    public AdminDao.AdminProductDetailRow getProductDetail(String softwareIdStr) {
        Integer softwareId = parseId(softwareIdStr);
        if (softwareId == null) {
            return null;
        }
        return adminDao.getProductDetail(softwareId);
    }

    // Lấy chi tiết report theo reportId.
    public AdminDao.AdminReportDetailRow getProductReportDetail(String reportIdStr) {
        Integer reportId = parseId(reportIdStr);
        if (reportId == null) {
            return null;
        }
        return adminDao.getReportDetail(reportId);
    }

    // Cập nhật nhanh trạng thái sản phẩm từ trang quản trị.
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

    // Cập nhật thông tin sản phẩm với validate dữ liệu nhập từ form admin.
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

    // Hàm dùng chung cho nghiệp vụ sửa user trong phạm vi role được phép.
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

        NotificationDao notifDao = new NotificationDao();
        notifDao.broadcastToAdmins(
            "User Profile Updated",
            "User ID " + userId + " (" + fullName.trim() + ") profile/status was updated.",
            "USER_UPDATE",
            "LOW",
            "/admin/employees"
        );

        return null;
    }

    // Kiểm tra role có thuộc whitelist nghiệp vụ hiện tại hay không.
    private boolean isValidRole(String roleName, Set<String> allowedRoles) {
        return allowedRoles.contains(roleName.toLowerCase());
    }

    // Parse trang hiện tại, luôn đảm bảo >= 1 để tránh lỗi phân trang.
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

    // Parse id từ request; trả null nếu sai định dạng.
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

    // Parse id optional, hỗ trợ trường có thể bỏ trống.
    private Integer parseOptionalId(String value) {
        if (isBlank(value)) {
            return null;
        }
        return parseId(value);
    }

    // Chuẩn hóa keyword tìm kiếm; chuỗi rỗng được coi như không lọc.
    private String normalizeKeyword(String keyword) {
        if (isBlank(keyword)) {
            return null;
        }
        return keyword.trim();
    }

    // Chuẩn hóa bộ lọc role, chặn các giá trị ngoài reviewer/approval.
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

    // Chuẩn hóa bộ lọc trạng thái ACTIVE/INACTIVE.
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

    // Chuẩn hóa trạng thái report để bảo vệ truy vấn lọc.
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

    // Chuẩn hóa trạng thái payout (PENDING/PAID).
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

    // Chuẩn hóa trạng thái feedback (NEW/READ).
    private String normalizeFeedbackStatusFilter(String status) {
        if (isBlank(status)) {
            return null;
        }
        String value = status.trim().toUpperCase();
        if (!FEEDBACK_STATUSES.contains(value)) {
            return null;
        }
        return value;
    }

    // Chỉ cho phép sort id theo asc/desc, mặc định desc.
    private String normalizePayoutSort(String sortById) {
        if ("asc".equalsIgnoreCase(sortById)) {
            return "asc";
        }
        return "desc";
    }

    // Parse ngày dạng yyyy-MM-dd; sai định dạng thì bỏ qua filter ngày.
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

    // Helper kiểm tra chuỗi null hoặc rỗng sau trim.
    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    // Escape HTML để chèn dữ liệu user vào email an toàn.
    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    // DTO phân trang dùng chung cho toàn bộ màn admin.
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

