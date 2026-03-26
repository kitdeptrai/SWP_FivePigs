package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AdminDao {

    // ===== Dashboard =====

    public List<Integer> getAdminUserIds() {
        String sql = "SELECT u.user_id FROM users u JOIN role r ON u.role_id = r.role_id WHERE LOWER(r.role_name) IN ('admin')";
        List<Integer> adminIds = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                adminIds.add(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminIds;
    }

    public double getTotalRevenue() {
        String sql = "SELECT " +
                "COALESCE((SELECT COUNT(*) * 5.0 " +
                "          FROM users u " +
                "          JOIN role r ON u.role_id = r.role_id " +
                "          WHERE LOWER(r.role_name) = 'vendor'), 0) " +
                "+ " +
                "COALESCE((SELECT SUM(od.price * 0.10) " +
                "          FROM order_detail od " +
                "          JOIN orders o ON o.order_id = od.order_id " +
                "          JOIN payment_status ps ON o.payment_status_id = ps.payment_status_id " +
                "          WHERE UPPER(ps.status_name) = 'PAID'), 0) AS total_revenue";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble("total_revenue") : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getTotalProducts() {
        String sql = "SELECT COUNT(*) FROM software";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getTotalUsers() {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getNewUsersToday() {
        String sql = "SELECT COUNT(*) FROM users WHERE DATE(created_at) = CURDATE()";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getTotalDownloads() {
        String sql = "SELECT COALESCE(SUM(download_count), 0) FROM software";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getUnreadOrPendingReports() {
        String sql = "SELECT COUNT(*) FROM report WHERE status IN ('UNREAD', 'PENDING')";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<RevenueByMonthRow> getRevenueByMonth() {
        String sql = "SELECT * FROM (" +
                "SELECT DATE_FORMAT(o.order_date, '%Y-%m') AS month_label, COALESCE(SUM(o.total_amount), 0) AS revenue " +
                "FROM orders o " +
                "JOIN payment_status ps ON o.payment_status_id = ps.payment_status_id " +
                "WHERE UPPER(ps.status_name) = 'PAID' " +
                "GROUP BY DATE_FORMAT(o.order_date, '%Y-%m') " +
                "ORDER BY month_label DESC " +
                "LIMIT 3" +
                ") recent_months ORDER BY month_label ASC";
        List<RevenueByMonthRow> rows = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new RevenueByMonthRow(rs.getString("month_label"), rs.getDouble("revenue")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public List<TopAppRow> getTop5AppsBestSeller() {
        String sql = "SELECT s.name AS app_name, s.download_count " +
                "FROM software s " +
                "ORDER BY s.download_count DESC, s.name ASC " +
                "LIMIT 5";
        List<TopAppRow> rows = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new TopAppRow(
                        rs.getString("app_name"),
                        rs.getInt("download_count")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public double getCommissionPercent() {
        String sql = "SELECT config_value FROM fivepigs.system_config WHERE config_key = 'commission_percent' LIMIT 1";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String raw = rs.getString("config_value");
                if (raw != null && !raw.isBlank()) {
                    try {
                        double percent = Double.parseDouble(raw.trim());
                        if (percent < 0) {
                            return 0;
                        }
                        return Math.min(percent, 20);
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 10.0;
    }

    public void setCommissionPercent(double commissionPercent, Integer userId) throws SQLException {
        double normalizedPercent = Math.max(0, Math.min(20, commissionPercent));

        String updateSql = "UPDATE fivepigs.system_config SET config_value = ? WHERE config_key = 'commission_percent'";
        String insertSql = "INSERT INTO fivepigs.system_config(config_key, config_value) VALUES('commission_percent', ?)";

        try (Connection conn = Db.getConnection()) {
            try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                updatePs.setString(1, Double.toString(normalizedPercent));
                int updatedRows = updatePs.executeUpdate();
                if (updatedRows > 0) {
                    return;
                }
            }

            try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                insertPs.setString(1, Double.toString(normalizedPercent));
                insertPs.executeUpdate();
            }
        }
    }

    // ===== Users/Employees Management =====

    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // fail-safe: treat as exists
        }
    }

    public Integer getRoleIdByName(String roleName) {
        // Match role_name case-insensitively
        String sql = "SELECT role_id FROM Role WHERE LOWER(role_name) = LOWER(?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roleName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("role_id");
                }
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String normalizeRoleName(String roleName) {
        if (roleName == null) return null;
        String r = roleName.trim();
        // Fix common typo
        if (r.equalsIgnoreCase("aproval")) return "Approval";
        return r;
    }

    public List<UserRow> listEmployees() {
        // Employees: reviewer + aproval
        String sql = "SELECT u.user_id, u.full_name, u.email, u.phone, u.status, u.created_at, r.role_name " +
                "FROM users u JOIN role r ON u.role_id = r.role_id " +
                "WHERE LOWER(r.role_name) IN ('reviewer', 'approval', 'aproval') " +
                "ORDER BY u.created_at DESC";
        List<UserRow> rows = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new UserRow(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("role_name"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public int countEmployees(String keyword, String role, String status) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM users u JOIN role r ON u.role_id = r.role_id " +
                        "WHERE LOWER(r.role_name) IN ('reviewer', 'approval', 'aproval')"
        );
        List<Object> params = new ArrayList<>();

        if (role != null) {
            sql.append(" AND LOWER(r.role_name) = ?");
            params.add(role.toLowerCase());
        }
        if (status != null) {
            sql.append(" AND u.status = ?");
            params.add(status);
        }
        if (keyword != null) {
            sql.append(" AND (LOWER(u.full_name) LIKE ? OR LOWER(u.email) LIKE ? OR LOWER(COALESCE(u.phone, '')) LIKE ?)");
            String kw = "%" + keyword.toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<UserRow> listEmployeesPaged(int limit, int offset, String keyword, String role, String status) {
        StringBuilder sql = new StringBuilder(
                "SELECT u.user_id, u.full_name, u.email, u.phone, u.status, u.created_at, r.role_name " +
                        "FROM users u JOIN role r ON u.role_id = r.role_id " +
                        "WHERE LOWER(r.role_name) IN ('reviewer', 'approval', 'aproval')"
        );
        List<Object> params = new ArrayList<>();

        if (role != null) {
            sql.append(" AND LOWER(r.role_name) = ?");
            params.add(role.toLowerCase());
        }
        if (status != null) {
            sql.append(" AND u.status = ?");
            params.add(status);
        }
        if (keyword != null) {
            sql.append(" AND (LOWER(u.full_name) LIKE ? OR LOWER(u.email) LIKE ? OR LOWER(COALESCE(u.phone, '')) LIKE ?)");
            String kw = "%" + keyword.toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }

        sql.append(" ORDER BY u.created_at DESC LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        List<UserRow> rows = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new UserRow(
                            rs.getInt("user_id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("role_name"),
                            rs.getString("status"),
                            rs.getTimestamp("created_at")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public List<UserRow> listUsers() {
        // Users: Customer + Vendor
        String sql = "SELECT u.user_id, u.full_name, u.email, u.phone, u.status, u.created_at, r.role_name " +
                "FROM users u JOIN role r ON u.role_id = r.role_id " +
                "WHERE r.role_name IN ('Customer', 'Vendor') " +
                "ORDER BY u.created_at DESC";
        List<UserRow> rows = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new UserRow(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("role_name"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public List<UserRow> listVendors() {
        String sql = "SELECT u.user_id, u.full_name, u.email, u.phone, u.status, u.created_at, r.role_name " +
                "FROM users u JOIN role r ON u.role_id = r.role_id " +
                "WHERE r.role_name = 'Vendor' " +
                "ORDER BY u.created_at DESC";
        List<UserRow> rows = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new UserRow(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("role_name"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public int countVendors(String keyword, String status) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM users u JOIN role r ON u.role_id = r.role_id " +
                        "WHERE r.role_name = 'Vendor'"
        );
        List<Object> params = new ArrayList<>();

        if (status != null) {
            sql.append(" AND u.status = ?");
            params.add(status);
        }
        if (keyword != null) {
            sql.append(" AND (LOWER(u.full_name) LIKE ? OR LOWER(u.email) LIKE ? OR LOWER(COALESCE(u.phone, '')) LIKE ?)");
            String kw = "%" + keyword.toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<UserRow> listVendorsPaged(int limit, int offset, String keyword, String status) {
        StringBuilder sql = new StringBuilder(
                "SELECT u.user_id, u.full_name, u.email, u.phone, u.status, u.created_at, r.role_name " +
                        "FROM users u JOIN role r ON u.role_id = r.role_id " +
                        "WHERE r.role_name = 'Vendor'"
        );
        List<Object> params = new ArrayList<>();

        if (status != null) {
            sql.append(" AND u.status = ?");
            params.add(status);
        }
        if (keyword != null) {
            sql.append(" AND (LOWER(u.full_name) LIKE ? OR LOWER(u.email) LIKE ? OR LOWER(COALESCE(u.phone, '')) LIKE ?)");
            String kw = "%" + keyword.toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }

        sql.append(" ORDER BY u.created_at DESC LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        List<UserRow> rows = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new UserRow(
                            rs.getInt("user_id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("role_name"),
                            rs.getString("status"),
                            rs.getTimestamp("created_at")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    // ===== Products Management =====

    public int countProducts(String keyword, String status) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) " +
                        "FROM report r " +
                        "JOIN software s ON r.software_id = s.software_id " +
                        "LEFT JOIN users vendor ON s.vendor_id = vendor.user_id " +
                        "LEFT JOIN users reporter ON r.reporter_id = reporter.user_id " +
                        "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (status != null) {
            sql.append(" AND UPPER(r.status) = ?");
            params.add(status);
        }
        if (keyword != null) {
            sql.append(" AND (CAST(r.report_id AS CHAR) LIKE ? OR LOWER(COALESCE(s.name, '')) LIKE ? OR LOWER(COALESCE(vendor.full_name, '')) LIKE ? OR LOWER(COALESCE(reporter.full_name, '')) LIKE ? OR LOWER(COALESCE(r.reason, '')) LIKE ?)");
            String kw = "%" + keyword.toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<AdminProductReportRow> listProductsPaged(int limit, int offset, String keyword, String status) {
        StringBuilder sql = new StringBuilder(
                "SELECT r.report_id, r.software_id, r.reason, r.status AS report_status, r.created_at AS report_created_at, " +
                        "s.name, s.status AS software_status, " +
                        "vendor.full_name AS vendor_name, reporter.full_name AS reporter_name " +
                        "FROM report r " +
                        "JOIN software s ON r.software_id = s.software_id " +
                        "LEFT JOIN users vendor ON s.vendor_id = vendor.user_id " +
                        "LEFT JOIN users reporter ON r.reporter_id = reporter.user_id " +
                        "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (status != null) {
            sql.append(" AND UPPER(r.status) = ?");
            params.add(status);
        }
        if (keyword != null) {
            sql.append(" AND (CAST(r.report_id AS CHAR) LIKE ? OR LOWER(COALESCE(s.name, '')) LIKE ? OR LOWER(COALESCE(vendor.full_name, '')) LIKE ? OR LOWER(COALESCE(reporter.full_name, '')) LIKE ? OR LOWER(COALESCE(r.reason, '')) LIKE ?)");
            String kw = "%" + keyword.toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }

        sql.append(" ORDER BY r.created_at DESC, r.report_id DESC LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        List<AdminProductReportRow> rows = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new AdminProductReportRow(
                            rs.getInt("report_id"),
                            rs.getInt("software_id"),
                            rs.getString("name"),
                            rs.getString("vendor_name"),
                            rs.getString("reporter_name"),
                            rs.getString("reason"),
                            rs.getString("report_status"),
                            rs.getString("software_status"),
                            rs.getTimestamp("report_created_at")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public boolean updateReportStatus(int reportId, String fromStatus, String toStatus) throws SQLException {
        String sql = "UPDATE report SET status = ? WHERE report_id = ? AND UPPER(status) = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, toStatus);
            ps.setInt(2, reportId);
            ps.setString(3, fromStatus.toUpperCase());
            return ps.executeUpdate() > 0;
        }
    }

    public AdminReportDetailRow getReportDetail(int reportId) {
        String sql = "SELECT r.report_id, r.software_id, r.reason, r.status AS report_status, r.created_at AS report_created_at, " +
                "s.name AS software_name, s.status AS software_status, " +
                "vendor.full_name AS vendor_name, vendor.email AS vendor_email, " +
                "reporter.full_name AS reporter_name, reporter.email AS reporter_email " +
                "FROM report r " +
                "JOIN software s ON r.software_id = s.software_id " +
                "LEFT JOIN users vendor ON s.vendor_id = vendor.user_id " +
                "LEFT JOIN users reporter ON r.reporter_id = reporter.user_id " +
                "WHERE r.report_id = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reportId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AdminReportDetailRow(
                            rs.getInt("report_id"),
                            rs.getInt("software_id"),
                            rs.getString("software_name"),
                            rs.getString("vendor_name"),
                            rs.getString("vendor_email"),
                            rs.getString("reporter_name"),
                            rs.getString("reporter_email"),
                            rs.getString("reason"),
                            rs.getString("report_status"),
                            rs.getString("software_status"),
                            rs.getTimestamp("report_created_at")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean markReportAsRead(int reportId) throws SQLException {
        String sql = "UPDATE report SET status = 'READ' WHERE report_id = ? AND UPPER(COALESCE(status, '')) <> 'READ'";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reportId);
            return ps.executeUpdate() > 0;
        }
    }

    public int countUserFeedback(String keyword, String status) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) " +
                        "FROM user_feedback uf " +
                        "JOIN users u ON uf.user_id = u.user_id " +
                        "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (status != null) {
            sql.append(" AND UPPER(COALESCE(uf.status, 'NEW')) = ?");
            params.add(status);
        }
        if (keyword != null) {
            sql.append(" AND (CAST(uf.feedback_id AS CHAR) LIKE ? OR LOWER(COALESCE(uf.subject, '')) LIKE ? OR LOWER(COALESCE(uf.message, '')) LIKE ? OR LOWER(COALESCE(u.full_name, '')) LIKE ? OR LOWER(COALESCE(u.email, '')) LIKE ?)");
            String kw = "%" + keyword.toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<AdminUserFeedbackRow> listUserFeedbackPaged(int limit, int offset, String keyword, String status) {
        StringBuilder sql = new StringBuilder(
                "SELECT uf.feedback_id, uf.subject, uf.message, COALESCE(uf.status, 'NEW') AS feedback_status, uf.created_at AS feedback_created_at, " +
                        "u.user_id, u.full_name AS user_name, u.email AS user_email " +
                        "FROM user_feedback uf " +
                        "JOIN users u ON uf.user_id = u.user_id " +
                        "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (status != null) {
            sql.append(" AND UPPER(COALESCE(uf.status, 'NEW')) = ?");
            params.add(status);
        }
        if (keyword != null) {
            sql.append(" AND (CAST(uf.feedback_id AS CHAR) LIKE ? OR LOWER(COALESCE(uf.subject, '')) LIKE ? OR LOWER(COALESCE(uf.message, '')) LIKE ? OR LOWER(COALESCE(u.full_name, '')) LIKE ? OR LOWER(COALESCE(u.email, '')) LIKE ?)");
            String kw = "%" + keyword.toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }

        sql.append(" ORDER BY uf.created_at DESC, uf.feedback_id DESC LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        List<AdminUserFeedbackRow> rows = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new AdminUserFeedbackRow(
                            rs.getInt("feedback_id"),
                            rs.getInt("user_id"),
                            rs.getString("user_name"),
                            rs.getString("user_email"),
                            rs.getString("subject"),
                            rs.getString("message"),
                            rs.getString("feedback_status"),
                            rs.getTimestamp("feedback_created_at")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public AdminUserFeedbackDetailRow getUserFeedbackDetail(int feedbackId) {
        String sql = "SELECT uf.feedback_id, uf.user_id, uf.subject, uf.message, COALESCE(uf.status, 'NEW') AS feedback_status, uf.created_at AS feedback_created_at, " +
                "u.full_name AS user_name, u.email AS user_email " +
                "FROM user_feedback uf " +
                "JOIN users u ON uf.user_id = u.user_id " +
                "WHERE uf.feedback_id = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, feedbackId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AdminUserFeedbackDetailRow(
                            rs.getInt("feedback_id"),
                            rs.getInt("user_id"),
                            rs.getString("user_name"),
                            rs.getString("user_email"),
                            rs.getString("subject"),
                            rs.getString("message"),
                            rs.getString("feedback_status"),
                            rs.getTimestamp("feedback_created_at")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean markUserFeedbackAsRead(int feedbackId) throws SQLException {
        String sql = "UPDATE user_feedback SET status = 'READ' WHERE feedback_id = ? AND UPPER(COALESCE(status, 'NEW')) <> 'READ'";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, feedbackId);
            return ps.executeUpdate() > 0;
        }
    }

    public AdminProductDetailRow getProductDetail(int softwareId) {
        String sql = "SELECT s.software_id, s.name, s.short_description, COALESCE(sp.price, 0) AS price, s.is_free, s.status, s.download_count, s.avg_rating, s.created_at, " +
                "u.full_name AS vendor_name, c.category_name, sv.version_name, sd.description, sd.system_requirement, si.image_url " +
                "FROM software s " +
                "LEFT JOIN users u ON s.vendor_id = u.user_id " +
                "LEFT JOIN category c ON s.category_id = c.category_id " +
                "LEFT JOIN software_version sv ON sv.software_id = s.software_id AND sv.is_active = 1 " +
                "LEFT JOIN software_detail sd ON sd.software_id = s.software_id " +
                "LEFT JOIN software_image si ON si.software_id = s.software_id AND si.is_thumbnail = 1 " +
                "LEFT JOIN software_pricing sp ON sp.software_id = s.software_id AND sp.is_active = 1 " +
                "WHERE s.software_id = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AdminProductDetailRow(
                            rs.getInt("software_id"),
                            rs.getString("name"),
                            rs.getString("short_description"),
                            rs.getString("vendor_name"),
                            rs.getString("category_name"),
                            rs.getString("version_name"),
                            rs.getString("description"),
                            rs.getString("system_requirement"),
                            rs.getDouble("price"),
                            rs.getInt("is_free"),
                            rs.getString("status"),
                            rs.getInt("download_count"),
                            rs.getDouble("avg_rating"),
                            rs.getTimestamp("created_at"),
                            rs.getString("image_url")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateProductStatus(int softwareId, String status) throws SQLException {
        String sql = "UPDATE software SET status = ? WHERE software_id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, softwareId);
            ps.executeUpdate();
        }
    }

    public void updateProduct(int softwareId, String name, String shortDescription, Integer categoryId, double price, int isFree, String status) throws SQLException {
        String sql = "UPDATE software SET name = ?, short_description = ?, category_id = ?, price = ?, is_free = ?, status = ? WHERE software_id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, shortDescription);
            if (categoryId == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, categoryId);
            }
            ps.setDouble(4, price);
            ps.setInt(5, isFree);
            ps.setString(6, status);
            ps.setInt(7, softwareId);
            ps.executeUpdate();
        }
    }

    // ===== Payout Management =====

    public int countVendorPayouts(String status, String keyword, java.sql.Date fromDate, java.sql.Date toDate) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM vendor_payout vp JOIN users u ON vp.vendor_id = u.user_id WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (status != null) {
            sql.append(" AND UPPER(vp.status) = ?");
            params.add(status.toUpperCase());
        }
        if (keyword != null) {
            sql.append(" AND (CAST(vp.payout_id AS CHAR) LIKE ? OR LOWER(u.full_name) LIKE ? OR LOWER(u.email) LIKE ?)");
            String kw = "%" + keyword.toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }
        if (fromDate != null) {
            sql.append(" AND DATE(vp.created_at) >= ?");
            params.add(fromDate);
        }
        if (toDate != null) {
            sql.append(" AND DATE(vp.created_at) <= ?");
            params.add(toDate);
        }

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<VendorPayoutRow> listVendorPayoutsPaged(int limit, int offset, String status, String keyword, java.sql.Date fromDate, java.sql.Date toDate, String sortById) {
        StringBuilder sql = new StringBuilder(
                "SELECT vp.payout_id, vp.vendor_id, u.full_name AS vendor_name, u.email AS vendor_email, " +
                        "vp.amount, vp.payment_method, vp.payment_account, vp.status, vp.created_at, vp.processed_at " +
                        "FROM vendor_payout vp " +
                        "JOIN users u ON vp.vendor_id = u.user_id " +
                        "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (status != null) {
            sql.append(" AND UPPER(vp.status) = ?");
            params.add(status.toUpperCase());
        }
        if (keyword != null) {
            sql.append(" AND (CAST(vp.payout_id AS CHAR) LIKE ? OR LOWER(u.full_name) LIKE ? OR LOWER(u.email) LIKE ?)");
            String kw = "%" + keyword.toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }
        if (fromDate != null) {
            sql.append(" AND DATE(vp.created_at) >= ?");
            params.add(fromDate);
        }
        if (toDate != null) {
            sql.append(" AND DATE(vp.created_at) <= ?");
            params.add(toDate);
        }

        String safeSort = "asc".equalsIgnoreCase(sortById) ? "ASC" : "DESC";
        sql.append(" ORDER BY vp.payout_id ").append(safeSort).append(" LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        List<VendorPayoutRow> rows = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new VendorPayoutRow(
                            rs.getInt("payout_id"),
                            rs.getInt("vendor_id"),
                            rs.getString("vendor_name"),
                            rs.getString("vendor_email"),
                            rs.getDouble("amount"),
                            rs.getString("payment_method"),
                            rs.getString("payment_account"),
                            rs.getString("status"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("processed_at")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public ApprovePayoutResult approveVendorPayout(int payoutId, Integer adminUserId) throws SQLException {
        String selectSql = "SELECT UPPER(status) AS status FROM vendor_payout WHERE payout_id = ?";
        String updateSql = "UPDATE vendor_payout SET status = 'PAID', processed_at = ? WHERE payout_id = ? AND UPPER(status) = 'PENDING'";

        try (Connection conn = Db.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String currentStatus = null;
                try (PreparedStatement selectPs = conn.prepareStatement(selectSql)) {
                    selectPs.setInt(1, payoutId);
                    try (ResultSet rs = selectPs.executeQuery()) {
                        if (rs.next()) {
                            currentStatus = rs.getString("status");
                        }
                    }
                }

                if (currentStatus == null) {
                    conn.rollback();
                    return new ApprovePayoutResult(false, "not_found", null);
                }

                if (!"PENDING".equalsIgnoreCase(currentStatus)) {
                    conn.rollback();
                    return new ApprovePayoutResult(false, "invalid_state", null);
                }

                Timestamp processedAt = new Timestamp(System.currentTimeMillis());
                int updatedRows;
                try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                    updatePs.setTimestamp(1, processedAt);
                    updatePs.setInt(2, payoutId);
                    updatedRows = updatePs.executeUpdate();
                }

                if (updatedRows <= 0) {
                    conn.rollback();
                    return new ApprovePayoutResult(false, "invalid_state", null);
                }

                conn.commit();
                return new ApprovePayoutResult(true, null, processedAt);
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    // ===== Orders Management =====

    public int countSuccessfulOrders(String keyword, java.sql.Date fromDate, java.sql.Date toDate) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) " +
                        "FROM orders o " +
                        "JOIN users u ON o.customer_id = u.user_id " +
                        "JOIN payment_status ps ON o.payment_status_id = ps.payment_status_id " +
                        "WHERE UPPER(ps.status_name) = 'PAID'"
        );
        List<Object> params = new ArrayList<>();

        if (keyword != null) {
            sql.append(" AND (CAST(o.order_id AS CHAR) LIKE ? OR LOWER(u.full_name) LIKE ? OR LOWER(u.email) LIKE ?)");
            String kw = "%" + keyword.toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }
        if (fromDate != null) {
            sql.append(" AND DATE(o.order_date) >= ?");
            params.add(fromDate);
        }
        if (toDate != null) {
            sql.append(" AND DATE(o.order_date) <= ?");
            params.add(toDate);
        }

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<AdminOrderRow> listSuccessfulOrdersPaged(int limit, int offset, String keyword, java.sql.Date fromDate, java.sql.Date toDate) {
        StringBuilder sql = new StringBuilder(
                "SELECT o.order_id, o.total_amount, o.order_date, ps.status_name AS payment_status, " +
                        "u.full_name AS customer_name, u.email AS customer_email, " +
                        "COALESCE(o.commission_percent, 10) AS commission_percent " +
                        "FROM orders o " +
                        "JOIN users u ON o.customer_id = u.user_id " +
                        "JOIN payment_status ps ON o.payment_status_id = ps.payment_status_id " +
                        "WHERE UPPER(ps.status_name) = 'PAID'"
        );
        List<Object> params = new ArrayList<>();

        if (keyword != null) {
            sql.append(" AND (CAST(o.order_id AS CHAR) LIKE ? OR LOWER(u.full_name) LIKE ? OR LOWER(u.email) LIKE ?)");
            String kw = "%" + keyword.toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }
        if (fromDate != null) {
            sql.append(" AND DATE(o.order_date) >= ?");
            params.add(fromDate);
        }
        if (toDate != null) {
            sql.append(" AND DATE(o.order_date) <= ?");
            params.add(toDate);
        }

        sql.append(" ORDER BY o.order_date DESC LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        List<AdminOrderRow> rows = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double totalAmount = rs.getDouble("total_amount");
                    double commissionPercent = rs.getDouble("commission_percent");
                    double adminReceivedAmount = totalAmount * (commissionPercent / 100.0);

                    rows.add(new AdminOrderRow(
                            rs.getInt("order_id"),
                            rs.getString("customer_name"),
                            rs.getString("customer_email"),
                            totalAmount,
                            rs.getTimestamp("order_date"),
                            rs.getString("payment_status"),
                            commissionPercent,
                            adminReceivedAmount
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public List<AdminOrderDetailRow> listOrderDetails(int orderId) {
        String sql = "SELECT od.order_detail_id, od.software_id, s.name AS software_name, od.price " +
                "FROM order_detail od " +
                "LEFT JOIN software s ON od.software_id = s.software_id " +
                "WHERE od.order_id = ? " +
                "ORDER BY od.order_detail_id ASC";

        List<AdminOrderDetailRow> rows = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new AdminOrderDetailRow(
                            rs.getInt("order_detail_id"),
                            rs.getInt("software_id"),
                            rs.getString("software_name"),
                            rs.getDouble("price")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    // ===== CRUD =====

    public void createEmployee(String fullName, String email, String phone, String roleName, String password) throws SQLException {
        Integer roleId = getRoleIdByName(normalizeRoleName(roleName));
        if (roleId == null) roleId = getRoleIdByName(roleName);
        if (roleId == null) throw new SQLException("Role not found: " + roleName);

        String sql = "INSERT INTO users(full_name, email, phone, password, role_id, status) VALUES(?,?,?,?,?,'ACTIVE')";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, phone == null || phone.isBlank() ? null : phone.trim());
            ps.setString(4, password);
            ps.setInt(5, roleId);
            ps.executeUpdate();
        }
    }

    public UserRow findUserById(int userId) {
        String sql = "SELECT u.user_id, u.full_name, u.email, u.phone, u.status, u.created_at, r.role_name " +
                "FROM users u JOIN role r ON u.role_id = r.role_id " +
                "WHERE u.user_id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserRow(
                            rs.getInt("user_id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("role_name"),
                            rs.getString("status"),
                            rs.getTimestamp("created_at")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setUserStatus(int userId, String status) throws SQLException {
        String sql = "UPDATE users SET status = ? WHERE user_id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    public void updateUser(int userId, String fullName, String phone, String status, String roleName) throws SQLException {
        String normalizedRoleName = normalizeRoleName(roleName);
        Integer roleId = getRoleIdByName(normalizedRoleName);
        if (roleId == null) {
            roleId = getRoleIdByName(roleName);
        }
        if (roleId == null) {
            throw new SQLException("Role does not exist: " + roleName);
        }

        String sql = "UPDATE users SET full_name = ?, phone = ?, status = ?, role_id = ? WHERE user_id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setString(3, status);
            ps.setInt(4, roleId);
            ps.setInt(5, userId);
            ps.executeUpdate();
        }
    }

    // ===== Helper data rows =====

    public static class RevenueByMonthRow {
        private final String month;
        private final double revenue;
        private int percent;

        public RevenueByMonthRow(String month, double revenue) {
            this.month = month;
            this.revenue = revenue;
            this.percent = 0;
        }

        public String getMonth() {
            return month;
        }

        public double getRevenue() {
            return revenue;
        }

        public int getPercent() {
            return percent;
        }

        public void setPercent(int percent) {
            this.percent = percent;
        }
    }

    public static class TopAppRow {
        private final String appName;
        private final int downloadCount;

        public TopAppRow(String appName, int downloadCount) {
            this.appName = appName;
            this.downloadCount = downloadCount;
        }

        public String getAppName() {
            return appName;
        }

        public int getDownloadCount() {
            return downloadCount;
        }
    }

    public static class AdminProductRow {
        private final int softwareId;
        private final String name;
        private final String vendorName;
        private final String categoryName;
        private final double price;
        private final int isFree;
        private final String status;
        private final int downloadCount;
        private final double avgRating;
        private final java.sql.Timestamp createdAt;

        public AdminProductRow(int softwareId, String name, String vendorName, String categoryName, double price, int isFree, String status, int downloadCount, double avgRating, java.sql.Timestamp createdAt) {
            this.softwareId = softwareId;
            this.name = name;
            this.vendorName = vendorName;
            this.categoryName = categoryName;
            this.price = price;
            this.isFree = isFree;
            this.status = status;
            this.downloadCount = downloadCount;
            this.avgRating = avgRating;
            this.createdAt = createdAt;
        }

        public int getSoftwareId() {
            return softwareId;
        }

        public String getName() {
            return name;
        }

        public String getVendorName() {
            return vendorName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public double getPrice() {
            return price;
        }

        public int getIsFree() {
            return isFree;
        }

        public String getStatus() {
            return status;
        }

        public int getDownloadCount() {
            return downloadCount;
        }

        public double getAvgRating() {
            return avgRating;
        }

        public java.sql.Timestamp getCreatedAt() {
            return createdAt;
        }
    }

    public static class AdminProductReportRow {
        private final int reportId;
        private final int softwareId;
        private final String name;
        private final String vendorName;
        private final String reporterName;
        private final String reason;
        private final String reportStatus;
        private final String softwareStatus;
        private final java.sql.Timestamp createdAt;

        public AdminProductReportRow(int reportId, int softwareId, String name, String vendorName, String reporterName, String reason, String reportStatus, String softwareStatus, java.sql.Timestamp createdAt) {
            this.reportId = reportId;
            this.softwareId = softwareId;
            this.name = name;
            this.vendorName = vendorName;
            this.reporterName = reporterName;
            this.reason = reason;
            this.reportStatus = reportStatus;
            this.softwareStatus = softwareStatus;
            this.createdAt = createdAt;
        }

        public int getReportId() {
            return reportId;
        }

        public int getSoftwareId() {
            return softwareId;
        }

        public String getName() {
            return name;
        }

        public String getVendorName() {
            return vendorName;
        }

        public String getReporterName() {
            return reporterName;
        }

        public String getReason() {
            return reason;
        }

        public String getReportStatus() {
            return reportStatus;
        }

        public String getSoftwareStatus() {
            return softwareStatus;
        }

        public java.sql.Timestamp getCreatedAt() {
            return createdAt;
        }
    }

    public static class AdminReportDetailRow {
        private final int reportId;
        private final int softwareId;
        private final String softwareName;
        private final String vendorName;
        private final String vendorEmail;
        private final String reporterName;
        private final String reporterEmail;
        private final String reason;
        private final String reportStatus;
        private final String softwareStatus;
        private final java.sql.Timestamp createdAt;

        public AdminReportDetailRow(int reportId, int softwareId, String softwareName, String vendorName, String vendorEmail, String reporterName, String reporterEmail, String reason, String reportStatus, String softwareStatus, java.sql.Timestamp createdAt) {
            this.reportId = reportId;
            this.softwareId = softwareId;
            this.softwareName = softwareName;
            this.vendorName = vendorName;
            this.vendorEmail = vendorEmail;
            this.reporterName = reporterName;
            this.reporterEmail = reporterEmail;
            this.reason = reason;
            this.reportStatus = reportStatus;
            this.softwareStatus = softwareStatus;
            this.createdAt = createdAt;
        }

        public int getReportId() {
            return reportId;
        }

        public int getSoftwareId() {
            return softwareId;
        }

        public String getSoftwareName() {
            return softwareName;
        }

        public String getVendorName() {
            return vendorName;
        }

        public String getVendorEmail() {
            return vendorEmail;
        }

        public String getReporterName() {
            return reporterName;
        }

        public String getReporterEmail() {
            return reporterEmail;
        }

        public String getReason() {
            return reason;
        }

        public String getReportStatus() {
            return reportStatus;
        }

        public String getSoftwareStatus() {
            return softwareStatus;
        }

        public java.sql.Timestamp getCreatedAt() {
            return createdAt;
        }
    }

    public static class AdminUserFeedbackRow {
        private final int feedbackId;
        private final int userId;
        private final String userName;
        private final String userEmail;
        private final String subject;
        private final String message;
        private final String feedbackStatus;
        private final java.sql.Timestamp createdAt;

        public AdminUserFeedbackRow(int feedbackId, int userId, String userName, String userEmail, String subject, String message, String feedbackStatus, java.sql.Timestamp createdAt) {
            this.feedbackId = feedbackId;
            this.userId = userId;
            this.userName = userName;
            this.userEmail = userEmail;
            this.subject = subject;
            this.message = message;
            this.feedbackStatus = feedbackStatus;
            this.createdAt = createdAt;
        }

        public int getFeedbackId() {
            return feedbackId;
        }

        public int getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public String getSubject() {
            return subject;
        }

        public String getMessage() {
            return message;
        }

        public String getFeedbackStatus() {
            return feedbackStatus;
        }

        public java.sql.Timestamp getCreatedAt() {
            return createdAt;
        }
    }

    public static class AdminUserFeedbackDetailRow {
        private final int feedbackId;
        private final int userId;
        private final String userName;
        private final String userEmail;
        private final String subject;
        private final String message;
        private final String feedbackStatus;
        private final java.sql.Timestamp createdAt;

        public AdminUserFeedbackDetailRow(int feedbackId, int userId, String userName, String userEmail, String subject, String message, String feedbackStatus, java.sql.Timestamp createdAt) {
            this.feedbackId = feedbackId;
            this.userId = userId;
            this.userName = userName;
            this.userEmail = userEmail;
            this.subject = subject;
            this.message = message;
            this.feedbackStatus = feedbackStatus;
            this.createdAt = createdAt;
        }

        public int getFeedbackId() {
            return feedbackId;
        }

        public int getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public String getSubject() {
            return subject;
        }

        public String getMessage() {
            return message;
        }

        public String getFeedbackStatus() {
            return feedbackStatus;
        }

        public java.sql.Timestamp getCreatedAt() {
            return createdAt;
        }
    }

    public static class AdminProductDetailRow {
        private final int softwareId;
        private final String name;
        private final String shortDescription;
        private final String vendorName;
        private final String categoryName;
        private final String versionName;
        private final String description;
        private final String systemRequirement;
        private final double price;
        private final int isFree;
        private final String status;
        private final int downloadCount;
        private final double avgRating;
        private final java.sql.Timestamp createdAt;
        private final String imageUrl;

        public AdminProductDetailRow(int softwareId, String name, String shortDescription, String vendorName, String categoryName, String versionName, String description, String systemRequirement, double price, int isFree, String status, int downloadCount, double avgRating, java.sql.Timestamp createdAt, String imageUrl) {
            this.softwareId = softwareId;
            this.name = name;
            this.shortDescription = shortDescription;
            this.vendorName = vendorName;
            this.categoryName = categoryName;
            this.versionName = versionName;
            this.description = description;
            this.systemRequirement = systemRequirement;
            this.price = price;
            this.isFree = isFree;
            this.status = status;
            this.downloadCount = downloadCount;
            this.avgRating = avgRating;
            this.createdAt = createdAt;
            this.imageUrl = imageUrl;
        }

        public int getSoftwareId() {
            return softwareId;
        }

        public String getName() {
            return name;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public String getVendorName() {
            return vendorName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public String getVersionName() {
            return versionName;
        }

        public String getDescription() {
            return description;
        }

        public String getSystemRequirement() {
            return systemRequirement;
        }

        public double getPrice() {
            return price;
        }

        public int getIsFree() {
            return isFree;
        }

        public String getStatus() {
            return status;
        }

        public int getDownloadCount() {
            return downloadCount;
        }

        public double getAvgRating() {
            return avgRating;
        }

        public java.sql.Timestamp getCreatedAt() {
            return createdAt;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    public static class AdminOrderRow {
        private final int orderId;
        private final String customerName;
        private final String customerEmail;
        private final double totalAmount;
        private final java.sql.Timestamp orderDate;
        private final String paymentStatus;
        private final double commissionPercent;
        private final double adminReceivedAmount;

        public AdminOrderRow(int orderId, String customerName, String customerEmail, double totalAmount, java.sql.Timestamp orderDate, String paymentStatus, double commissionPercent, double adminReceivedAmount) {
            this.orderId = orderId;
            this.customerName = customerName;
            this.customerEmail = customerEmail;
            this.totalAmount = totalAmount;
            this.orderDate = orderDate;
            this.paymentStatus = paymentStatus;
            this.commissionPercent = commissionPercent;
            this.adminReceivedAmount = adminReceivedAmount;
        }

        public int getOrderId() {
            return orderId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public String getCustomerEmail() {
            return customerEmail;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public java.sql.Timestamp getOrderDate() {
            return orderDate;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public double getCommissionPercent() {
            return commissionPercent;
        }

        public double getAdminReceivedAmount() {
            return adminReceivedAmount;
        }
    }

    public static class AdminOrderDetailRow {
        private final int orderDetailId;
        private final int softwareId;
        private final String softwareName;
        private final double price;

        public AdminOrderDetailRow(int orderDetailId, int softwareId, String softwareName, double price) {
            this.orderDetailId = orderDetailId;
            this.softwareId = softwareId;
            this.softwareName = softwareName;
            this.price = price;
        }

        public int getOrderDetailId() {
            return orderDetailId;
        }

        public int getSoftwareId() {
            return softwareId;
        }

        public String getSoftwareName() {
            return softwareName;
        }

        public double getPrice() {
            return price;
        }
    }

    public static class ApprovePayoutResult {
        private final boolean approved;
        private final String errorCode;
        private final java.sql.Timestamp processedAt;

        public ApprovePayoutResult(boolean approved, String errorCode, java.sql.Timestamp processedAt) {
            this.approved = approved;
            this.errorCode = errorCode;
            this.processedAt = processedAt;
        }

        public boolean isApproved() {
            return approved;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public java.sql.Timestamp getProcessedAt() {
            return processedAt;
        }
    }

    public static class VendorPayoutRow {
        private final int payoutId;
        private final int vendorId;
        private final String vendorName;
        private final String vendorEmail;
        private final double amount;
        private final String paymentMethod;
        private final String paymentAccount;
        private final String status;
        private final java.sql.Timestamp createdAt;
        private final java.sql.Timestamp processedAt;

        public VendorPayoutRow(int payoutId, int vendorId, String vendorName, String vendorEmail, double amount, String paymentMethod, String paymentAccount, String status, java.sql.Timestamp createdAt, java.sql.Timestamp processedAt) {
            this.payoutId = payoutId;
            this.vendorId = vendorId;
            this.vendorName = vendorName;
            this.vendorEmail = vendorEmail;
            this.amount = amount;
            this.paymentMethod = paymentMethod;
            this.paymentAccount = paymentAccount;
            this.status = status;
            this.createdAt = createdAt;
            this.processedAt = processedAt;
        }

        public int getPayoutId() {
            return payoutId;
        }

        public int getVendorId() {
            return vendorId;
        }

        public String getVendorName() {
            return vendorName;
        }

        public String getVendorEmail() {
            return vendorEmail;
        }

        public double getAmount() {
            return amount;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public String getPaymentAccount() {
            return paymentAccount;
        }

        public String getStatus() {
            return status;
        }

        public java.sql.Timestamp getCreatedAt() {
            return createdAt;
        }

        public java.sql.Timestamp getProcessedAt() {
            return processedAt;
        }
    }

    public static class UserRow {
        private final int userId;
        private final String fullName;
        private final String email;
        private final String phone;
        private final String roleName;
        private final String status;
        private final java.sql.Timestamp createdAt;

        public UserRow(int userId, String fullName, String email, String phone, String roleName, String status, java.sql.Timestamp createdAt) {
            this.userId = userId;
            this.fullName = fullName;
            this.email = email;
            this.phone = phone;
            this.roleName = roleName;
            this.status = status;
            this.createdAt = createdAt;
        }

        public int getUserId() {
            return userId;
        }

        public String getFullName() {
            return fullName;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getRoleName() {
            return roleName;
        }

        public String getStatus() {
            return status;
        }

        public java.sql.Timestamp getCreatedAt() {
            return createdAt;
        }
    }
}
