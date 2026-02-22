package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdminDao {

    // ===== Dashboard =====

    public double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM orders WHERE payment_status_id = 1";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0;
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
        String sql = "SELECT MONTH(order_date) AS month, COALESCE(SUM(total_amount), 0) AS revenue " +
                     "FROM orders " +
                     "WHERE payment_status_id = 1 " +
                     "GROUP BY MONTH(order_date) " +
                     "ORDER BY MONTH(order_date)";
        List<RevenueByMonthRow> rows = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new RevenueByMonthRow(rs.getInt("month"), rs.getDouble("revenue")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public List<TopAppRow> getTop5AppsBestSeller() {
        String sql = "SELECT s.name AS app_name, COUNT(od.order_detail_id) AS purchase_count, COALESCE(SUM(od.price), 0) AS total_revenue " +
                     "FROM order_detail od " +
                     "JOIN software s ON s.software_id = od.software_id " +
                     "JOIN orders o ON o.order_id = od.order_id " +
                     "WHERE o.payment_status_id = 1 " +
                     "GROUP BY s.software_id, s.name " +
                     "ORDER BY purchase_count DESC " +
                     "LIMIT 5";
        List<TopAppRow> rows = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new TopAppRow(
                        rs.getString("app_name"),
                        rs.getInt("purchase_count"),
                        rs.getDouble("total_revenue")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
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
        // match role_name không phân biệt hoa thường
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
        // fix typo phổ biến
        if (r.equalsIgnoreCase("aproval")) return "Approval";
        return r;
    }

    public int createUser(String fullName, String email, String phone, String roleName) throws SQLException {
        String normalizedRoleName = normalizeRoleName(roleName);
        Integer roleId = getRoleIdByName(normalizedRoleName);
        if (roleId == null) {
            // Fallback thử tìm chính xác roleName nếu normalize không ra
            roleId = getRoleIdByName(roleName);
        }
        
        if (roleId == null) {
            throw new SQLException("Role không tồn tại: " + roleName);
        }

        String sql = "INSERT INTO users(full_name, email, password, role_id, phone, status) VALUES (?,?,?,?,?,?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, "123456");
            ps.setInt(4, roleId);
            ps.setString(5, phone);
            ps.setString(6, "ACTIVE");

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return 0;
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

    // ===== Helper data rows =====

    public static class RevenueByMonthRow {
        private final int month;
        private final double revenue;

        public RevenueByMonthRow(int month, double revenue) {
            this.month = month;
            this.revenue = revenue;
        }

        public int getMonth() {
            return month;
        }

        public double getRevenue() {
            return revenue;
        }
    }

    public static class TopAppRow {
        private final String appName;
        private final int purchaseCount;
        private final double totalRevenue;

        public TopAppRow(String appName, int purchaseCount, double totalRevenue) {
            this.appName = appName;
            this.purchaseCount = purchaseCount;
            this.totalRevenue = totalRevenue;
        }

        public String getAppName() {
            return appName;
        }

        public int getPurchaseCount() {
            return purchaseCount;
        }

        public double getTotalRevenue() {
            return totalRevenue;
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
