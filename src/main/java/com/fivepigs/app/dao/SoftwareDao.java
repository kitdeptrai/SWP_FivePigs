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
import java.util.List;



public class SoftwareDao {

    public Integer pendingReviewApp() throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM Software WHERE status = 'PENDING_REVIEW';";

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        }
        return 0;
    }

    // Đếm số app đã được review (completed)
    public Integer completeReviewApp() throws SQLException {
        String sql = """
                    SELECT COUNT(DISTINCT software_id) AS count
                    FROM Software_Review_Process
                """;

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        return 0;
    }

    // Đếm số app được review hôm nay
    public Integer reviewedToday() throws SQLException {
        String sql = """
                    SELECT COUNT(DISTINCT software_id) AS count
                    FROM Software_Review_Process
                    WHERE CONVERT_TZ(reviewed_at, '+00:00', '+07:00') >= CURDATE()
                      AND CONVERT_TZ(reviewed_at, '+00:00', '+07:00') < CURDATE() + INTERVAL 1 DAY
                """;

        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        return 0;
    }

    // Tính Quality Score trung bình (%)
    public Integer getQualityScore() throws SQLException {
        String sql = """
                    SELECT ROUND(AVG(quality_score)) AS score
                    FROM Software_Review_Process
                    WHERE quality_score > 0
                """;

        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("score");
            }
        }
        return 0;
    }


    /**
     *
     * @author MinhPD
     */
    public List<Software> Top3RevenueByVendor(Integer vendorId) throws SQLException {
        List<Software> list = new ArrayList<>();
        String sql = "SELECT s.name AS app_name,SUM(od.price) AS revenue,s.avg_rating  AS rating,s.status,download_count,vendor_id FROM Software s\n"
                + "JOIN Order_Detail od ON s.software_id = od.software_id\n"
                + "JOIN Orders o ON od.order_id = o.order_id\n"
                + "JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id\n"
                + "WHERE s.vendor_id = ? AND ps.status_name = 'PAID'\n"
                + "GROUP BY s.software_id,s.name,s.avg_rating,s.status\n"
                + "ORDER BY revenue DESC\n"
                + "LIMIT 3;";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vendorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Software sw = new Software();
                    sw.setName(rs.getString("app_name"));
                    sw.setRevenue(rs.getDouble("revenue"));
                    sw.setStatus(rs.getString("status"));
                    sw.setAvgRating(rs.getDouble("rating"));
                    sw.setDownloadCount(rs.getInt("download_count"));
                    sw.setVendorId(rs.getInt("vendor_id"));
                    list.add(sw);
                }
            }
        }
        return list;
    }
}

