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
import java.sql.Timestamp;
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
    // Pending reviews
    public List<Software> getPendingSoftware() throws SQLException {

    List<Software> list = new ArrayList<>();

    String sql = """
        SELECT s.software_id,
               s.name,
               s.short_description,
               s.price,
               s.status,
               s.created_at,
               s.version,
               s.language,
               c.category_name
        FROM Software s
        LEFT JOIN Category c
            ON s.category_id = c.category_id
        WHERE s.status = 'PENDING_REVIEW'
    """;

    try (Connection conn = Db.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Software s = new Software();
            s.setSoftwareId(rs.getInt("software_id"));
            s.setName(rs.getString("name"));
            s.setShort_description(rs.getString("short_description"));
            s.setPrice(rs.getDouble("price"));
            s.setStatus(rs.getString("status"));
            Timestamp ts = rs.getTimestamp("created_at");
                 if (ts != null) {
                     s.setCreatedAt(ts.toLocalDateTime());
              }
            s.setVersion(rs.getString("version"));
            s.setLanguage(rs.getString("language"));     
            s.setCategoryName(rs.getString("category_name")); // QUAN TRỌNG

            list.add(s);
        }
    }

    return list;
}
    // update status trong pending reviews
    public boolean updateStatus(int softwareId, String status) throws SQLException {

    String sql = "UPDATE Software SET status = ? WHERE software_id = ?";

    try (Connection conn = Db.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, status);
        ps.setInt(2, softwareId);

        return ps.executeUpdate() > 0;
    }
}


    public List<Software> searchPendingSoftware(String keyword) throws SQLException {

    List<Software> list = new ArrayList<>();

    String sql = "SELECT * FROM software " +
                 "WHERE status = 'pending' " +
                 "AND name LIKE ?";

    try (Connection conn = Db.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, "%" + keyword + "%");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Software s = new Software();
            s.setSoftwareId(rs.getInt("software_id"));
            s.setName(rs.getString("name"));
            s.setPrice(rs.getDouble("price"));
            list.add(s);
        }
    }

    return list;
}
    
    // My Reviews (apps được assign cho reviewer)
public List<Software> getMyReviews(Integer reviewerId) throws SQLException {

    List<Software> list = new ArrayList<>();

    String sql = """
        SELECT s.software_id,
               s.name,
               s.short_description,
               s.price,
               s.status,
               s.created_at,
               s.version,
               s.language,
               c.category_name,
               si.image_url,
               rp.quality_score
        FROM Software_Review_Process rp
        JOIN Software s 
             ON rp.software_id = s.software_id
        LEFT JOIN Category c
             ON s.category_id = c.category_id
        LEFT JOIN Software_Image si
             ON s.software_id = si.software_id
             AND si.is_thumbnail = 1
        WHERE rp.reviewer_id = ?
        ORDER BY s.created_at DESC
    """;

    try (Connection conn = Db.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, reviewerId);

        try (ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Software s = new Software();

                s.setSoftwareId(rs.getInt("software_id"));
                s.setName(rs.getString("name"));
                s.setShort_description(rs.getString("short_description"));
                s.setPrice(rs.getDouble("price"));
                s.setStatus(rs.getString("status"));

                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) {
                    s.setCreatedAt(ts.toLocalDateTime());
                }

                s.setVersion(rs.getString("version"));
                s.setLanguage(rs.getString("language"));
                s.setCategoryName(rs.getString("category_name"));
                s.setImageUrl(rs.getString("image_url"));

              double score = rs.getDouble("quality_score");
                       if (!rs.wasNull()) {
                     s.setQualityScore(score);
                                     }

                list.add(s);
            }
        }
    }

    return list;
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
    
     public Software getSoftwareById(int softwareId) throws SQLException {
        String sql = """
            SELECT
                s.software_id,
                s.name AS app_name,
                s.short_description,
                s.status,
                c.category_name,
                u.full_name AS reviewer_name,
                rp.reviewed_at,
                (
                    SELECT si.image_url
                    FROM Software_Image si
                    WHERE si.software_id = s.software_id
                    ORDER BY si.is_thumbnail DESC, si.created_at ASC
                    LIMIT 1
                ) AS thumbnail_url
            FROM Software s
            JOIN Software_Review_Process rp ON rp.software_id = s.software_id
            JOIN Users u ON u.user_id = rp.reviewer_id
            LEFT JOIN Category c ON c.category_id = s.category_id
            WHERE s.software_id = ?
        """;

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Software s = new Software();
                    Category c = new Category();
                    s.setSoftwareId(rs.getInt("software_id"));
                    s.setAppName(rs.getString("app_name"));
                    s.setShort_description(rs.getString("short_description"));
                    s.setStatus(rs.getString("status"));
                    
                    // Thiết lập category
                    c.setCategoryName(rs.getString("category_name"));
                    
                    // Thiết lập reviewer
                    s.setName(rs.getString("reviewer_name"));

                    // Thiết lập thumbnail image
                    SoftwareImage si = new SoftwareImage();
                    si.setImageUrl(rs.getString("thumbnail_url"));
                    s.setSoftwareImage(si);

                    // Thiết lập thời gian review
                    Timestamp ts = rs.getTimestamp("reviewed_at");
                    ReviewerProcess rp = new ReviewerProcess();
                    rp.setReviewed_at(ts != null ? ts.toLocalDateTime() : null);
                    s.setReviewerProcess(rp);

                    return s; // Trả về đối tượng phần mềm
                }
            }
        }

        return null; // Nếu không có phần mềm với ID này
    }
}

