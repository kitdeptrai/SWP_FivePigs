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

/**
 *
 * @author MinhPD
 */
public class SoftwareDao {

    public List<Software> Top3RevenueByVendor(Integer vendorId) throws SQLException{
        List<Software> list=new ArrayList<>();
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
