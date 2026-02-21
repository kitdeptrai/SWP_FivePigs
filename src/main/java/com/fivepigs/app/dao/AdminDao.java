package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDao {

    // 1️⃣ System Overview Cards
    
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

    // 2️⃣ Doanh thu theo tháng
    
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

    // 3️⃣ Top 5 app bán chạy
    
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

    // Helper classes for data rows
    
    public static class RevenueByMonthRow {
        private final int month;
        private final double revenue;
        public RevenueByMonthRow(int month, double revenue) { this.month = month; this.revenue = revenue; }
        public int getMonth() { return month; }
        public double getRevenue() { return revenue; }
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
        public String getAppName() { return appName; }
        public int getPurchaseCount() { return purchaseCount; }
        public double getTotalRevenue() { return totalRevenue; }
    }
}
