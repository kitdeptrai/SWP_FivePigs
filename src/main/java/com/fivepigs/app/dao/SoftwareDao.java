package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.Software;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SoftwareDao {

    public Integer pendingReviewApp() throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM Software WHERE status = 'PENDING_REVIEW'";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        return 0;
    }

    // Đếm số app đã được review (completed)
    public Integer completeReviewApp() throws SQLException {
        String sql = "SELECT COUNT(DISTINCT software_id) AS count FROM Software_Review_Process";
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
            WHERE reviewed_at >= CURDATE()
              AND reviewed_at < CURDATE() + INTERVAL 1 DAY
        """;
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        return 0;
    }

    /**
     * Quality score (thay cho quality_score cũ): - Dùng
     * Review_Score.total_score (0..10) - Quy đổi ra %: AVG(total_score) * 10
     *
     * @return
     * @throws java.sql.SQLException
     */
    public Integer getQualityScore() throws SQLException {
        String sql = """
        SELECT ROUND(AVG(total_score) * 10) AS score
        FROM Review_Score
        WHERE total_score IS NOT NULL
    """;

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("score");
            }
        }
        return 0;
    }

    // Pending reviews (đã sửa: lấy version từ Software_Version, bỏ language)
    public List<Software> getPendingSoftware() throws SQLException {
        List<Software> list = new ArrayList<>();

        String sql = """
        SELECT s.software_id,
               s.name,
               s.short_description,
               s.price,
               s.status,
               s.created_at,
               sv.version_name AS version,
               c.category_name
        FROM Software s
        LEFT JOIN Category c ON s.category_id = c.category_id
        LEFT JOIN Software_Version sv
               ON sv.software_id = s.software_id
              AND sv.is_active = 1
        WHERE s.status = 'PENDING_REVIEW'
        ORDER BY s.created_at DESC
    """;

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

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
                s.setCategoryName(rs.getString("category_name"));
                s.setLanguage(null); // schema mới không có

                list.add(s);
            }
        }
        return list;
    }

    // update status trong pending reviews
    public boolean updateStatus(int softwareId, String status) throws SQLException {
        String sql = "UPDATE Software SET status = ? WHERE software_id = ?";
        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, softwareId);
            return ps.executeUpdate() > 0;
        }
    }

    // Search pending software (đã sửa: đúng table + đúng status)
    public List<Software> searchPendingSoftware(String keyword) throws SQLException {
        List<Software> list = new ArrayList<>();

        String sql = """
        SELECT s.software_id, s.name, s.price
        FROM Software s
        WHERE s.status = 'PENDING_REVIEW'
          AND s.name LIKE ?
        ORDER BY s.created_at DESC
    """;

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Software s = new Software();
                    s.setSoftwareId(rs.getInt("software_id"));
                    s.setName(rs.getString("name"));
                    s.setPrice(rs.getDouble("price"));
                    list.add(s);
                }
            }
        }
        return list;
    }

    // My Reviews (apps đã có record trong Software_Review_Process)
    // Đã sửa: lấy version từ Software_Version, bỏ quality_score
    public List<Software> getMyReviews(Integer reviewerId) throws SQLException {
        List<Software> list = new ArrayList<>();

        String sql = """
            SELECT s.software_id,
                   s.name,
                   s.short_description,
                   s.price,
                   s.status,
                   s.created_at,
                   sv.version_name AS version,
                   c.category_name,
                   si.image_url
            FROM Software_Review_Process rp
            JOIN Software s
                 ON rp.software_id = s.software_id
            LEFT JOIN Category c
                 ON s.category_id = c.category_id
            LEFT JOIN Software_Image si
                 ON s.software_id = si.software_id
                AND si.is_thumbnail = 1
            LEFT JOIN Software_Version sv
                 ON sv.software_id = s.software_id
                AND sv.is_active = 1
            WHERE rp.reviewer_id = ?
            ORDER BY rp.reviewed_at DESC
        """;

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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
                    s.setLanguage(null);
                    s.setCategoryName(rs.getString("category_name"));
                    s.setImageUrl(rs.getString("image_url"));
                    s.setQualityScore(null); // schema mới không có quality_score

                    list.add(s);
                }
            }
        }
        return list;
    }

    public List<Software> getMyAssignedPendingReviews(int reviewerId) throws SQLException {
    List<Software> list = new ArrayList<>();

    String sql = """
        SELECT s.software_id,
               s.name,
               s.short_description,
               s.price,
               s.status,
               s.created_at,
               ra.assigned_at,
               ra.due_at,
               ra.status AS assignment_status,
               sv.version_name AS version,
               c.category_name,
               si.image_url
        FROM Reviewer_Assignment ra
        JOIN Software s
             ON ra.software_id = s.software_id
        LEFT JOIN Category c
             ON s.category_id = c.category_id
        LEFT JOIN Software_Image si
             ON s.software_id = si.software_id
            AND si.is_thumbnail = 1
        LEFT JOIN Software_Version sv
             ON sv.software_id = s.software_id
            AND sv.is_active = 1
        LEFT JOIN Software_Review_Process rp
             ON rp.software_id = s.software_id
            AND rp.reviewer_id = ra.reviewer_id
        WHERE ra.reviewer_id = ?
          AND ra.status IN ('ASSIGNED','IN_PROGRESS')
          AND rp.review_process_id IS NULL
        ORDER BY ra.assigned_at DESC
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
                s.setVersion(rs.getString("version"));
                s.setCategoryName(rs.getString("category_name"));
                s.setImageUrl(rs.getString("image_url"));
                list.add(s);
            }
        }
    }
    return list;
}

    // Top3RevenueByVendor: OK (không dùng cột bị xóa)
    public List<Software> Top3RevenueByVendor(Integer vendorId) throws SQLException {
        List<Software> list = new ArrayList<>();
        String sql = """
            SELECT s.name AS app_name,
                   SUM(od.price) AS revenue,
                   s.avg_rating AS rating,
                   s.status,
                   s.download_count,
                   s.vendor_id
            FROM Software s
            JOIN Order_Detail od ON s.software_id = od.software_id
            JOIN Orders o ON od.order_id = o.order_id
            JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id
            WHERE s.vendor_id = ? AND ps.status_name = 'PAID'
            GROUP BY s.software_id, s.name, s.avg_rating, s.status, s.download_count, s.vendor_id
            ORDER BY revenue DESC
            LIMIT 3
        """;
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
