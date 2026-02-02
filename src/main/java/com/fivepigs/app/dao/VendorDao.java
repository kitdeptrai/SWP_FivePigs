/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author MinhPD
 */
public class VendorDao {

    public Integer sumApprovedApps(int vendorId) throws SQLException {
        String sql = "SELECT COUNT(*) AS total_approved_apps FROM Software\n"
                + "WHERE vendor_id = ? AND status = 'APPROVED';";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vendorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_approved_apps");
                }
            }
        }
        return null;
    }

    public Integer sumPendingApps(int vendorId) throws SQLException {
        String sql = "SELECT COUNT(*) AS total_pending_apps FROM Software\n"
                + "WHERE vendor_id = ? AND status = 'PENDING';";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vendorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_pending_apps");
                }
            }
        }
        return null;
    }

    public Double sumRevenue(int vendorId) throws SQLException {
        String sql = "SELECT s.vendor_id,SUM(od.price) AS total_revenue FROM Orders o \n"
                + "JOIN Order_Detail od ON o.order_id = od.order_id\n"
                + "JOIN Software s ON od.software_id = s.software_id\n"
                + "JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id\n"
                + "WHERE ps.status_name = 'PAID' AND s.vendor_id = ?\n"
                + "GROUP BY s.vendor_id;";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vendorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total_revenue");
                }
            }
        }
        return null;
    }

    public Double avgRating(int vendorId) throws SQLException {
        String sql = "SELECT s.vendor_id,ROUND(AVG(r.rating), 2) AS avg_rating FROM Software s\n"
                + "JOIN Review r ON s.software_id = r.software_id\n"
                + "WHERE s.vendor_id =?\n"
                + "GROUP BY s.vendor_id;";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vendorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("avg_rating");
                }
            }
        }
        return null;
    }

    public Map<Integer, Double> revenueMap(int vendorId) throws SQLException {

        Map<Integer, Double> revenueMap = new HashMap<>();

        // luôn đảm bảo đủ 4 tuần
        for (int i = 1; i <= 4; i++) {
            revenueMap.put(i, 0.0);
        }

        String sql = """
        SELECT 
            CEIL(DATEDIFF(CURDATE(), o.order_date) / 7) AS week_index,
            SUM(od.price) AS revenue
        FROM Orders o
        JOIN Order_Detail od ON o.order_id = od.order_id
        JOIN Software s ON od.software_id = s.software_id
        JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id
        WHERE ps.status_name = 'PAID'
          AND s.vendor_id = ?
          AND o.order_date >= DATE_SUB(CURDATE(), INTERVAL 28 DAY)
        GROUP BY week_index
    """;

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, vendorId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int weekIndex = rs.getInt("week_index"); // 1 = gần nhất
                    double revenue = rs.getDouble("revenue");

                    // 🔁 đảo chiều: 1->4, 2->3, 3->2, 4->1
                    int weekDisplay = 5 - Math.min(weekIndex, 4);

                    revenueMap.put(
                            weekDisplay,
                            revenueMap.get(weekDisplay) + revenue
                    );
                }
            }
        }
        return revenueMap;
    }

    public Integer downloadCount(int vendorId) throws SQLException {
        String sql = "SELECT SUM(download_count) AS total_download FROM Software\n"
                + "WHERE vendor_id = ?;";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vendorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_download");
                }
            }
        }
        return null;
    }

    public Map<Integer, Double> downloadByWeek(int vendorId) throws SQLException {
        String sql = "SELECT \n"
                + "    FLOOR(DATEDIFF(CURDATE(), l.purchase_date) / 7) AS week_index,\n"
                + "    COUNT(*) AS downloads\n"
                + "FROM License l\n"
                + "JOIN Software s ON l.software_id = s.software_id\n"
                + "WHERE s.vendor_id = ?\n"
                + "  AND l.purchase_date >= DATE_SUB(CURDATE(), INTERVAL 28 DAY)\n"
                + "GROUP BY week_index\n"
                + "HAVING week_index BETWEEN 1 AND 4";
        Map<Integer, Double> downloadMap = new LinkedHashMap<>();
        for (int i = 0; i < 4; i++) {
            downloadMap.put(i, 0.0);
        }

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, vendorId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int weekIndex = rs.getInt("week_index"); // 0..3
                    double downloads = rs.getDouble("downloads");

                    downloadMap.put(
                            weekIndex,
                            downloadMap.get(weekIndex) + downloads
                    );
                }
            }
        }
        return downloadMap;
    }
}
