package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
<<<<<<< HEAD
import com.fivepigs.app.model.Software;
=======

import com.fivepigs.app.model.*;

import java.sql.*;

>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
import java.sql.*;
import com.fivepigs.app.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SoftwareDao {

<<<<<<< HEAD
=======
    private static final String PRICE_JOIN = """
        LEFT JOIN (
            SELECT software_id, MIN(price) AS min_price
            FROM Software_Pricing
            WHERE is_active = 1
            GROUP BY software_id
        ) sp_min ON sp_min.software_id = s.software_id
        """;

    private static final String PRICE_SELECT = """
        CASE
            WHEN s.is_free = 1 THEN 0
            ELSE COALESCE(sp_min.min_price, 0)
        END AS final_price
        """;

>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
    public Integer pendingReviewApp() throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM Software WHERE status = 'PENDING_REVIEW'";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        return 0;
    }

<<<<<<< HEAD
    // Đếm số app đã được review (completed)
    public int countPendingReviewSoftware() throws SQLException {
=======
    public int countPendingReviewSoftware() throws SQLException {

>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
        String sql = "SELECT COUNT(*) AS total FROM Software WHERE status = 'PENDING_REVIEW'";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    public Integer completeReviewApp() throws SQLException {
<<<<<<< HEAD
        String sql = "SELECT COUNT(DISTINCT software_id) AS count FROM Software_Review_Process";
=======
        String sql = "SELECT COUNT(DISTINCT software_id) AS count FROM Review_Score";
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
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
<<<<<<< HEAD
            SELECT COUNT(DISTINCT software_id) AS count
            FROM Software_Review_Process
            WHERE reviewed_at >= CURDATE()
              AND reviewed_at < CURDATE() + INTERVAL 1 DAY
        """;
=======
                    SELECT COUNT(DISTINCT software_id) AS count
                    FROM Review_Score
                    WHERE created_at >= CURDATE()
                      AND created_at < CURDATE() + INTERVAL 1 DAY
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
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
<<<<<<< HEAD
        SELECT ROUND(AVG(total_score) * 10) AS score
        FROM Review_Score
        WHERE total_score IS NOT NULL
    """;

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

=======
                    SELECT ROUND(AVG(total_score) * 10) AS score
                    FROM Review_Score
                    WHERE total_score IS NOT NULL
                """;
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
            if (rs.next()) {
                return rs.getInt("score");
            }
        }
        return 0;
    }

<<<<<<< HEAD
    // Pending reviews (đã sửa: lấy version từ Software_Version, bỏ language)
=======
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
    public List<Software> getPendingSoftware() throws SQLException {

        List<Software> list = new ArrayList<>();

        String sql = """
<<<<<<< HEAD
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

    """;
=======
    SELECT s.software_id,
           s.name,
           s.short_description,
           CASE 
               WHEN s.is_free = 1 THEN 0
               ELSE COALESCE(sp_min.min_price, 0)
           END AS price,
           s.status,
           s.created_at,
           sv.version_name AS version,
           c.category_name
    FROM Software s
    LEFT JOIN Category c ON s.category_id = c.category_id
    LEFT JOIN Software_Version sv
           ON sv.software_id = s.software_id
          AND sv.is_active = 1
    LEFT JOIN (
        SELECT software_id, MIN(price) AS min_price
        FROM Software_Pricing
        WHERE is_active = 1
        GROUP BY software_id
    ) sp_min ON sp_min.software_id = s.software_id
    WHERE s.status = 'PENDING_REVIEW'
""";
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Software s = new Software();
                s.setSoftwareId(rs.getInt("software_id"));
                s.setName(rs.getString("name"));
                s.setShortDescription(rs.getString("short_description"));
                s.setPrice(rs.getDouble("price"));
                s.setStatus(rs.getString("status"));
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) {
                    s.setCreatedAt(ts.toLocalDateTime());
                }
                s.setVersion(rs.getString("version"));
                s.setCategoryName(rs.getString("category_name")); // QUAN TRỌNG

                list.add(s);
            }
        }

    return list;
}




    
    // My Reviews (apps được assign cho reviewer)


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
<<<<<<< HEAD
        SELECT s.software_id, s.name, s.price
        FROM Software s
        WHERE s.status = 'PENDING_REVIEW'
          AND s.name LIKE ?
        ORDER BY s.created_at DESC
    """;
=======
                SELECT s.software_id,
                       s.name,
                       """ + PRICE_SELECT + """
                FROM Software s
                """ + PRICE_JOIN + """
                    WHERE s.status = 'PENDING_REVIEW'
                      AND s.name LIKE ?
                    ORDER BY s.created_at DESC
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

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
<<<<<<< HEAD
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
=======
                SELECT s.software_id,
                       s.name,
                       s.short_description,
                       """ + PRICE_SELECT + """
                       ,
                       s.status,
                       s.created_at,
                       sv.version_name AS version,
                       c.category_name,
                       si.image_url
                FROM Review_Score rs
                JOIN Software s
                     ON rs.software_id = s.software_id
                LEFT JOIN Category c
                     ON s.category_id = c.category_id
                LEFT JOIN Software_Image si
                     ON s.software_id = si.software_id
                    AND si.is_thumbnail = 1
                LEFT JOIN Software_Version sv
                     ON sv.software_id = s.software_id
                    AND sv.is_active = 1
                """ + PRICE_JOIN + """
                    WHERE rs.reviewer_id = ?
                    ORDER BY rs.created_at DESC
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reviewerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Software s = new Software();
                    s.setSoftwareId(rs.getInt("software_id"));
                    s.setName(rs.getString("name"));
                    s.setShortDescription(rs.getString("short_description"));
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

<<<<<<< HEAD
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
=======
        String sql = """
                SELECT s.software_id,
                       s.name,
                       s.short_description,
                       """ + PRICE_SELECT + """
                       ,
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
                """ + PRICE_JOIN + """
                    LEFT JOIN Review_Score rs
                         ON rs.software_id = ra.software_id
                        AND rs.reviewer_id = ra.reviewer_id
                    WHERE ra.reviewer_id = ?
                      AND ra.status IN ('ASSIGNED', 'IN_PROGRESS')
                      AND s.status = 'PENDING_REVIEW'
                      AND rs.review_score_id IS NULL
                    ORDER BY ra.assigned_at DESC
                """;

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

        ps.setInt(1, reviewerId);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Software s = new Software();
                s.setSoftwareId(rs.getInt("software_id"));
                s.setName(rs.getString("name"));
                s.setShortDescription(rs.getString("short_description"));
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
<<<<<<< HEAD
            SELECT s.name AS app_name,
                   SUM(od.price) AS revenue,
                   s.avg_rating AS rating,
                   s.status,
                   s.download_count,
                   s.vendor_id,
                   s.software_id
            FROM Software s
            JOIN Order_Detail od ON s.software_id = od.software_id
            JOIN Orders o ON od.order_id = o.order_id
            JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id
            WHERE s.vendor_id = ? AND ps.status_name = 'PAID'
            GROUP BY s.software_id, s.name, s.avg_rating, s.status, s.download_count, s.vendor_id
            ORDER BY revenue DESC
            LIMIT 3
        """;
=======
                    SELECT s.name AS app_name,
                           SUM(od.price) AS revenue,
                           s.avg_rating AS rating,
                           s.status,
                           s.download_count,
                           s.vendor_id,
                           s.software_id
                    FROM Software s
                    JOIN Order_Detail od ON s.software_id = od.software_id
                    JOIN Orders o ON od.order_id = o.order_id
                    JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id
                    WHERE s.vendor_id = ? AND UPPER(ps.status_name) = 'PAID'
                    GROUP BY s.software_id, s.name, s.avg_rating, s.status, s.download_count, s.vendor_id
                    ORDER BY revenue DESC
                    LIMIT 3
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vendorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Software sw = new Software();
                    sw.setSoftwareId(rs.getInt("software_id"));
                    sw.setName(rs.getString("app_name"));
                    sw.setRevenue(rs.getDouble("revenue"));
                    sw.setStatus(rs.getString("status"));
                    sw.setAvgRating(rs.getDouble("rating"));
                    sw.setDownloadCount(rs.getInt("download_count"));
                    sw.setVendorId(rs.getInt("vendor_id"));
                    sw.setSoftwareId(rs.getInt("software_id"));
                    list.add(sw);
                }
            }
        }
        return list;
    }


    public Integer totalProductsByVendor(Integer vendorId) throws SQLException {
<<<<<<< HEAD
        String sql = "SELECT \n"
                + "    COUNT(*) AS total_apps\n"
                + "FROM Software\n"
                + "WHERE vendor_id = ?;";
=======
        String sql = """
                    SELECT COUNT(*) AS total_apps
                    FROM Software
                    WHERE vendor_id = ?
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vendorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_apps");
                }
            }
        }
        return null;
    }

    public Integer totalDownloadByVendor(Integer vendorId) throws SQLException {
<<<<<<< HEAD
        String sql = "SELECT \n"
                + "    SUM(download_count) AS total_download_count\n"
                + "FROM Software\n"
                + "WHERE vendor_id = ?;";
=======
        String sql = """
                    SELECT SUM(download_count) AS total_download_count
                    FROM Software
                    WHERE vendor_id = ?
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vendorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_download_count");
                }
            }
        }
        return null;
    }

    public Integer totalAppByStatusAndVendor(Integer vendorId, String status) throws SQLException {
<<<<<<< HEAD
        String sql = "SELECT \n"
                + "    COUNT(*) AS total_apps\n"
                + "FROM Software\n"
                + "WHERE vendor_id = ?\n"
                + "AND status = ?;";
=======
        String sql = """
                    SELECT COUNT(*) AS total_apps
                    FROM Software
                    WHERE vendor_id = ?
                      AND status = ?
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vendorId);
            ps.setString(2, status);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_apps");
                }
            }
        }
        return null;
    }

    public Double totalRevenueByVendor(Integer vendorId) throws SQLException {
<<<<<<< HEAD
        String sql = "SELECT \n"
                + "SUM(od.price) AS total_revenue\n"
                + "FROM Order_Detail od\n"
                + "JOIN Orders o ON od.order_id = o.order_id\n"
                + "JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id\n"
                + "JOIN Software s ON od.software_id = s.software_id\n"
                + "WHERE ps.status_name = 'PAID'\n"
                + "AND s.vendor_id = ?;";
=======
        String sql = """
                    SELECT SUM(od.price) AS total_revenue
                    FROM Order_Detail od
                    JOIN Orders o ON od.order_id = o.order_id
                    JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id
                    JOIN Software s ON od.software_id = s.software_id
                    WHERE UPPER(ps.status_name) = 'PAID'
                      AND s.vendor_id = ?
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
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

<<<<<<< HEAD
    public Map<Integer, Double> downloadByWeek(int vendorId) throws SQLException {
        String sql = "SELECT \n"
                + "    FLOOR(DATEDIFF(CURDATE(), l.purchase_date) / 7) AS week_index,\n"
                + "    COUNT(*) AS downloads\n"
                + "FROM License l\n"
                + "JOIN Software s ON l.software_id = s.software_id\n"
                + "WHERE s.vendor_id = ?\n"
                + "  AND l.purchase_date >= DATE_SUB(CURDATE(), INTERVAL 28 DAY)\n"
                + "GROUP BY week_index\n"
                + "HAVING week_index BETWEEN 0 AND 3";
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

    public Double avgRatingByVendorId(int vendorId) throws SQLException {
        String sql = "SELECT s.vendor_id,ROUND(AVG(r.rating), 2) AS avg_rating FROM Software s\n"
                + "JOIN Review r ON s.software_id = r.software_id\n"
                + "WHERE s.vendor_id =?\n"
                + "GROUP BY s.vendor_id;";
=======
    public Double avgRatingByVendorId(int vendorId) throws SQLException {
        String sql = """
                    SELECT s.vendor_id, ROUND(AVG(r.rating), 2) AS avg_rating
                    FROM Software s
                    JOIN Review r ON s.software_id = r.software_id
                    WHERE s.vendor_id = ?
                    GROUP BY s.vendor_id
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
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

    public List<Software> getSoftwareCardListByVendorID(int vendorId) throws SQLException {
        List<Software> list = new ArrayList<>();
<<<<<<< HEAD
        String sql = "SELECT s.software_id,s.name,s.short_description,s.status,s.download_count,s.avg_rating,\n"
                + "COALESCE(SUM(\n"
                + "        CASE \n"
                + "            WHEN ps.status_name = 'PAID' THEN od.price \n"
                + "            ELSE 0 \n"
                + "        END\n"
                + "    ),0) AS revenue,si.image_url AS thumbnail\n"
                + "FROM Software s\n"
                + "LEFT JOIN Order_Detail od ON s.software_id = od.software_id\n"
                + "LEFT JOIN Orders o ON od.order_id = o.order_id\n"
                + "LEFT JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id\n"
                + "LEFT JOIN Software_Image si ON s.software_id = si.software_id AND si.is_thumbnail = 1\n"
                + "WHERE s.vendor_id = ? \n"
                + "GROUP BY s.software_id,s.name,s.short_description,s.status,s.download_count,s.avg_rating,si.image_url;";
=======
        String sql = """
                    SELECT s.software_id,
                           s.name,
                           s.short_description,
                           s.status,
                           s.download_count,
                           s.avg_rating,
                           COALESCE(SUM(
                               CASE
                                   WHEN UPPER(ps.status_name) = 'PAID' THEN od.price
                                   ELSE 0
                               END
                           ), 0) AS revenue,
                           si.image_url AS thumbnail
                    FROM Software s
                    LEFT JOIN Order_Detail od ON s.software_id = od.software_id
                    LEFT JOIN Orders o ON od.order_id = o.order_id
                    LEFT JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id
                    LEFT JOIN Software_Image si ON s.software_id = si.software_id AND si.is_thumbnail = 1
                    WHERE s.vendor_id = ?
                    GROUP BY s.software_id, s.name, s.short_description, s.status, s.download_count, s.avg_rating, si.image_url
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vendorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Software sw = new Software();
                    SoftwareImage swimg = new SoftwareImage();
                    sw.setSoftwareId(rs.getInt("software_id"));
                    sw.setName(rs.getString("name"));
                    sw.setRevenue(rs.getDouble("revenue"));
                    sw.setStatus(rs.getString("status"));
                    sw.setAvgRating(rs.getDouble("avg_rating"));
                    sw.setDownloadCount(rs.getInt("download_count"));
                    sw.setShortDescription(rs.getString("short_description"));
                    swimg.setImageUrl(rs.getString("thumbnail"));
                    sw.setSoftwareImage(swimg);
                    list.add(sw);
                }
            }
        }
        return list;
    }

    public Integer getDownloadBySoftwareId(int softwareId) throws SQLException {
<<<<<<< HEAD
        String sql = "SELECT download_count FROM Software\n"
                + "WHERE software_id=?;";
=======
        String sql = "SELECT download_count FROM Software WHERE software_id = ?";
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("download_count");
                }
            }
        }
        return null;
    }

    public Double getRatingBySoftwareId(int softwareId) throws SQLException {
<<<<<<< HEAD
        String sql = "SELECT avg_rating FROM Software\n"
                + "WHERE software_id=?;";
=======
        String sql = "SELECT avg_rating FROM Software WHERE software_id = ?";
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("avg_rating");
                }
            }
        }
        return null;
    }

    public Integer getTotalLicenseBySoftwareId(int softwareId) throws SQLException {
<<<<<<< HEAD
        String sql = "SELECT COUNT(license_id) AS total_license FROM License\n"
                + "WHERE software_id=?;";
=======
        String sql = "SELECT COUNT(license_id) AS total_license FROM License WHERE software_id = ?";
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_license");
                }
            }
        }
        return null;
    }

    public Double getRevenueBySoftwareId(int softwareId) throws SQLException {
<<<<<<< HEAD
        String sql = "SELECT \n"
                + "    SUM(od.price) AS total_revenue\n"
                + "FROM Order_Detail od\n"
                + "JOIN Orders o ON od.order_id = o.order_id\n"
                + "JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id\n"
                + "WHERE ps.status_name = 'PAID'\n"
                + "AND od.software_id = ?;";
=======
        String sql = """
                    SELECT SUM(od.price) AS total_revenue
                    FROM Order_Detail od
                    JOIN Orders o ON od.order_id = o.order_id
                    JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id
                    WHERE UPPER(ps.status_name) = 'PAID'
                      AND od.software_id = ?
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total_revenue");
                }
            }
        }
        return null;
    }

    public Software getSoftwareDetailBySoftwareIdVendor(int softwareId) throws SQLException {
        String sql = "SELECT s.status, s.name, s.short_description, "
                + "sv.release_note, sv.version_name AS version, "
                + "c.category_name, s.created_at, "
                + "sp.price, "
                + "sd.description, sd.system_requirement, "
                + "si.image_url AS thumbnail "
                + "FROM Software s "
                + "LEFT JOIN Software_Version sv ON s.software_id = sv.software_id AND sv.is_active = 1 "
                + "LEFT JOIN Software_Detail sd ON s.software_id = sd.software_id "
                + "LEFT JOIN Category c ON s.category_id = c.category_id "
                + "LEFT JOIN Software_Image si ON s.software_id = si.software_id AND si.is_thumbnail = 1 "
                + "LEFT JOIN Software_Pricing sp ON s.software_id = sp.software_id "
                + "    AND sp.plan_name = 'BASIC' AND sp.is_active = 1 "
                + "WHERE s.software_id = ?";

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Software sw = new Software();
                    SoftwareDetail swDetail = new SoftwareDetail();
                    SoftwareVersion swVersion = new SoftwareVersion();
                    SoftwareImage simg = new SoftwareImage();

                    Category cat = new Category();
                    sw.setStatus(rs.getString("status"));
                    sw.setShortDescription(rs.getString("short_description"));
                    sw.setName(rs.getString("name"));
                    swVersion.setVersionName(rs.getString("version"));
                    swVersion.setReleaseNote(rs.getString("release_note"));
                    cat.setCategoryName(rs.getString("category_name"));
                    sw.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                    sw.setPrice(rs.getDouble("price"));
                    swDetail.setDescription(rs.getString("description"));
                    swDetail.setSysRequirement(rs.getString("system_requirement"));
                    simg.setImageUrl(rs.getString("thumbnail"));
                    sw.setSoftwareImage(simg);
                    sw.setSoftwareVersion(swVersion);
                    sw.setSoftwareDetail(swDetail);
                    sw.setCategory(cat);
                    return sw;
                }
            }
        }
        return null;
    }

    public Software getSoftwareDetailBySoftwareId(int softwareId) throws SQLException {
        String sql = """
                SELECT s.software_id,
                       s.name,
                       s.short_description,
                       COALESCE((SELECT MIN(sp.price)
                                 FROM fivepigs.software_pricing sp
                                 WHERE sp.software_id = s.software_id
                                   AND sp.is_active = 1
                                   AND UPPER(COALESCE(sp.plan_name, '')) <> 'DEMO'), 0) AS price,
                       s.is_free,
                       s.download_count,
                       s.avg_rating,
                       s.created_at,
                       c.category_id,
                       c.category_name,
                       u.user_id AS vendor_user_id,
                       u.full_name AS vendor_name,
                       sd.detail_id,
                       sd.description,
                       sd.system_requirement,
                       sd.release_note,
                       sv.version_id,
                       sv.version_name,
                       sv.file_url,
                       sv.release_note AS version_release_note,
                       sv.file_size,
                       sv.created_at AS version_created_at,
                       sv.is_active,
                       si.image_url AS thumbnail
                FROM Software s
                LEFT JOIN Category c ON s.category_id = c.category_id
                LEFT JOIN Users u ON s.vendor_id = u.user_id
                LEFT JOIN Software_Detail sd ON s.software_id = sd.software_id
                LEFT JOIN Software_Version sv ON s.software_id = sv.software_id AND sv.is_active = 1
                LEFT JOIN Software_Image si ON s.software_id = si.software_id AND si.is_thumbnail = 1
                WHERE s.software_id = ?
                LIMIT 1
                """;

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, softwareId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Software sw = new Software();
                    SoftwareDetail swDetail = new SoftwareDetail();
                    SoftwareVersion swVersion = new SoftwareVersion();
                    SoftwareImage simg = new SoftwareImage();
                    Category cat = new Category();
                    User vendor = new User();

                    sw.setSoftwareId(rs.getInt("software_id"));
                    sw.setName(rs.getString("name"));
                    sw.setShortDescription(rs.getString("short_description"));
                    sw.setPrice(rs.getDouble("price"));
                    sw.setIsFree(rs.getInt("is_free"));
                    sw.setDownloadCount(rs.getInt("download_count"));
                    sw.setAvgRating(rs.getDouble("avg_rating"));
sw.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));

                    cat.setCategoryId((Integer) rs.getObject("category_id"));
                    cat.setCategoryName(rs.getString("category_name"));
                    sw.setCategory(cat);
                    sw.setCategoryName(rs.getString("category_name"));

                    vendor.setUserId((Integer) rs.getObject("vendor_user_id"));
                    vendor.setFullName(rs.getString("vendor_name"));
                    sw.setUser(vendor);

                    swDetail.setDetailId((Integer) rs.getObject("detail_id"));
                    swDetail.setSoftwareId(rs.getInt("software_id"));
                    swDetail.setDescription(rs.getString("description"));
                    swDetail.setSysRequirement(rs.getString("system_requirement"));
                    swDetail.setReleaseNote(rs.getString("release_note"));
                    sw.setSoftwareDetail(swDetail);

                    swVersion.setVersionId((Integer) rs.getObject("version_id"));
                    swVersion.setSoftwareId(rs.getInt("software_id"));
                    swVersion.setVersionName(rs.getString("version_name"));
                    swVersion.setFileUrl(rs.getString("file_url"));
                    swVersion.setReleaseNote(rs.getString("version_release_note"));
                    Number fileSizeValue = (Number) rs.getObject("file_size");
                    swVersion.setFileSize(fileSizeValue == null ? null : fileSizeValue.longValue());
                    swVersion.setCreatedAt(rs.getObject("version_created_at", LocalDateTime.class));
                    Object isActiveValue = rs.getObject("is_active");
                    if (isActiveValue instanceof Boolean booleanValue) {
                        swVersion.setIsActive(booleanValue ? 1 : 0);
                    } else if (isActiveValue instanceof Number numberValue) {
                        swVersion.setIsActive(numberValue.intValue());
                    } else {
                        swVersion.setIsActive(null);
                    }
                    sw.setSoftwareVersion(swVersion);
                    sw.setVersion(rs.getString("version_name"));

                    simg.setImageUrl(rs.getString("thumbnail"));
                    sw.setSoftwareImage(simg);
                    sw.setImageUrl(rs.getString("thumbnail"));
                    return sw;
                }
            }
        }
        return null;
    }
<<<<<<< HEAD


    //Upload Software
    public int createSoftware(Software software) throws Exception {

        String sql = "INSERT INTO Software(name, short_description, vendor_id, category_id, price) "
                + "VALUES (?, ?, ?, ?, ?)";

=======
    public int createSoftware(Software software) throws Exception {
        String sql = """
                    INSERT INTO Software(name, short_description, vendor_id, category_id, is_free, status)
                    VALUES (?, ?, ?, ?, ?, ?)
                """;

>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, software.getName());
            ps.setString(2, software.getShortDescription());
            ps.setInt(3, software.getVendorId());
            ps.setInt(4, software.getCategoryId());
            ps.setDouble(5, software.getPrice());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        throw new RuntimeException("Cannot create software");
    }

<<<<<<< HEAD
    //Software of Customer (Create by TKiet)

    private final String CUSTOMER_PRICE_SQL =
            "COALESCE((SELECT MIN(sp.price) " +
                    "FROM fivepigs.software_pricing sp " +
                    "WHERE sp.software_id = s.software_id " +
                    "  AND sp.is_active = 1 " +
                    "  AND UPPER(COALESCE(sp.plan_name, '')) <> 'DEMO'), 0) AS price";
    private final String Get_All_Product = "SELECT s.*, " + CUSTOMER_PRICE_SQL + " FROM fivepigs.software s";
    private final String GET_SOFTWARE_BY_ID = "SELECT s.*, " + CUSTOMER_PRICE_SQL + " FROM fivepigs.software s\n" +
            "WHERE software_id = ?";
    private final String Get_SoftwareImage_By_Id = "SELECT * FROM fivepigs.software_image\n" +
            "WHERE software_id = ?";

    public List<Software> GET_ALL_SOFTWARE() throws SQLException {
        List<Software> list = new ArrayList<>();
        try(Connection c = Db.getConnection();
            PreparedStatement st = c.prepareStatement(Get_All_Product)) {
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                Software sw = new Software();
                sw.setSoftwareId(rs.getInt("software_id"));
                sw.setName(rs.getString("name"));
                sw.setShortDescription(rs.getString("short_description"));
                sw.setVendorId(rs.getInt("vendor_id"));
                sw.setCategoryId(rs.getInt("category_id"));
                sw.setPrice(rs.getDouble("price"));
                sw.setIsFree(rs.getInt("is_free"));
                sw.setStatus(rs.getString("status"));
                sw.setDownloadCount(rs.getInt("download_count"));
                sw.setAvgRating(rs.getDouble("avg_rating"));
                list.add(sw);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Software> getSoftwareByCategory(int categoryId) throws SQLException {
        List<Software> list = new ArrayList<>();
        String sql = "SELECT s.*, " + CUSTOMER_PRICE_SQL + " FROM fivepigs.software s WHERE category_id = ?";

        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {

            st.setInt(1, categoryId);
            ResultSet rs = st.executeQuery();
=======
    private final String Get_All_Product = """
            SELECT s.*,
                   CASE
                       WHEN s.is_free = 1 THEN 0
                       ELSE COALESCE(sp.price, 0)
                   END AS price
            FROM fivepigs.software s
            LEFT JOIN (
                SELECT software_id, MIN(price) AS price
                FROM fivepigs.software_pricing
                WHERE is_active = 1
                GROUP BY software_id
            ) sp ON sp.software_id = s.software_id
            """;

    private final String GET_SOFTWARE_BY_ID = """
            SELECT s.*,
                   CASE
                       WHEN s.is_free = 1 THEN 0
                       ELSE COALESCE(sp.price, 0)
                   END AS price
            FROM fivepigs.software s
            LEFT JOIN (
                SELECT software_id, MIN(price) AS price
                FROM fivepigs.software_pricing
                WHERE is_active = 1
                GROUP BY software_id
            ) sp ON sp.software_id = s.software_id
            WHERE s.software_id = ?
            """;

    private final String Get_SoftwareImage_By_Id = """
            SELECT * FROM fivepigs.software_image
            WHERE software_id = ?
            """;

    public List<Software> GET_ALL_SOFTWARE() throws SQLException {
        List<Software> list = new ArrayList<>();
        try (Connection c = Db.getConnection(); PreparedStatement st = c.prepareStatement(Get_All_Product); ResultSet rs = st.executeQuery()) {
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

            while (rs.next()) {
                Software sw = new Software();
                sw.setSoftwareId(rs.getInt("software_id"));
                sw.setName(rs.getString("name"));
                sw.setShortDescription(rs.getString("short_description"));
                sw.setVendorId(rs.getInt("vendor_id"));
                sw.setCategoryId(rs.getInt("category_id"));
                sw.setPrice(rs.getDouble("price"));
                sw.setIsFree(rs.getInt("is_free"));
                sw.setStatus(rs.getString("status"));
                sw.setDownloadCount(rs.getInt("download_count"));
                sw.setAvgRating(rs.getDouble("avg_rating"));
                list.add(sw);
            }
        }
        return list;
    }

<<<<<<< HEAD
    public Software GETALLSOFTWAREBYID(String cid) {

        try(Connection c = Db.getConnection();
            PreparedStatement st = c.prepareStatement(GET_SOFTWARE_BY_ID)) {
=======
    public List<Software> getSoftwareByCategory(int categoryId) throws SQLException {
        List<Software> list = new ArrayList<>();
        String sql = """
                    SELECT s.*,
                           CASE
                               WHEN s.is_free = 1 THEN 0
                               ELSE COALESCE(sp.price, 0)
                           END AS price
                    FROM fivepigs.software s
                    LEFT JOIN (
                        SELECT software_id, MIN(price) AS price
                        FROM fivepigs.software_pricing
                        WHERE is_active = 1
                        GROUP BY software_id
                    ) sp ON sp.software_id = s.software_id
                    WHERE s.category_id = ?
                """;

        try (Connection c = Db.getConnection(); PreparedStatement st = c.prepareStatement(sql)) {

            st.setInt(1, categoryId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Software sw = new Software();
                    sw.setSoftwareId(rs.getInt("software_id"));
                    sw.setName(rs.getString("name"));
                    sw.setShortDescription(rs.getString("short_description"));
                    sw.setVendorId(rs.getInt("vendor_id"));
                    sw.setCategoryId(rs.getInt("category_id"));
                    sw.setPrice(rs.getDouble("price"));
                    sw.setIsFree(rs.getInt("is_free"));
                    sw.setStatus(rs.getString("status"));
                    sw.setDownloadCount(rs.getInt("download_count"));
                    sw.setAvgRating(rs.getDouble("avg_rating"));
                    list.add(sw);
                }
            }
        }
        return list;
    }

    public Software GETALLSOFTWAREBYID(String cid) {
        try (Connection c = Db.getConnection(); PreparedStatement st = c.prepareStatement(GET_SOFTWARE_BY_ID)) {
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
            st.setString(1, cid);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Software sw = new Software();
                sw.setSoftwareId(rs.getInt("software_id"));
                sw.setName(rs.getString("name"));
                sw.setShortDescription(rs.getString("short_description"));
                sw.setVendorId(rs.getInt("vendor_id"));
                sw.setCategoryId(rs.getInt("category_id"));
                sw.setPrice(rs.getDouble("price"));
                sw.setIsFree(rs.getInt("is_free"));
                sw.setStatus(rs.getString("status"));
                sw.setDownloadCount(rs.getInt("download_count"));
                sw.setAvgRating(rs.getDouble("avg_rating"));
                return sw;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public SoftwareImage GetImageById(String cid) {
<<<<<<< HEAD
        try(Connection c = Db.getConnection();
            PreparedStatement st = c.prepareStatement(Get_SoftwareImage_By_Id)) {
=======
        try (Connection c = Db.getConnection(); PreparedStatement st = c.prepareStatement(Get_SoftwareImage_By_Id)) {
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
            st.setString(1, cid);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                SoftwareImage sw = new SoftwareImage();
                sw.setImageId(rs.getInt("image_id"));
                sw.setSoftwareId(rs.getInt("software_id"));
                sw.setImageUrl(rs.getString("image_url"));
                return sw;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public SoftwareImage getThumbnailBySoftwareId(String softwareId) throws SQLException {
<<<<<<< HEAD
        String sql = "SELECT * FROM fivepigs.software_image \n" +
                "                WHERE software_id = ? AND is_thumbnail = 1 \n" +
                "                ORDER BY image_id DESC LIMIT 1";

        try(Connection c = Db.getConnection();
            PreparedStatement st = c.prepareStatement(sql)) {
=======
        String sql = """
                    SELECT * FROM fivepigs.software_image
                    WHERE software_id = ? AND is_thumbnail = 1
                    ORDER BY image_id DESC
                    LIMIT 1
                """;

        try (Connection c = Db.getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

            st.setString(1, softwareId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    SoftwareImage img = new SoftwareImage();
                    img.setImageId(rs.getInt("image_id"));
                    img.setSoftwareId(rs.getInt("software_id"));
                    img.setImageUrl(rs.getString("image_url"));
                    img.setIsThumbnail(rs.getInt("is_thumbnail"));
                    return img;
                }
            }
        }
        return null;
    }

    public List<SoftwareImage> getScreenshotsBySoftwareId(String softwareId) throws SQLException {
        List<SoftwareImage> list = new ArrayList<>();

<<<<<<< HEAD
        String sql = "SELECT * " +
                "FROM fivepigs.software_image " +
                "WHERE software_id = ? AND is_thumbnail = 0 " +
                "ORDER BY image_id ASC";

        try(Connection c = Db.getConnection();
            PreparedStatement st = c.prepareStatement(sql)) {
=======
        String sql = """
                    SELECT *
                    FROM fivepigs.software_image
                    WHERE software_id = ? AND is_thumbnail = 0
                    ORDER BY image_id ASC
                """;

        try (Connection c = Db.getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

            st.setString(1, softwareId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    SoftwareImage img = new SoftwareImage();
                    img.setImageId(rs.getInt("image_id"));
                    img.setSoftwareId(rs.getInt("software_id"));
                    img.setImageUrl(rs.getString("image_url"));
                    img.setIsThumbnail(rs.getInt("is_thumbnail"));

                    list.add(img);
                }
            }
        }
        return list;
    }

    public List<Software> getSoftwareByCategoryWithIcon(String categoryId) throws SQLException {
<<<<<<< HEAD
        String sql =
                "SELECT s.*, " + CUSTOMER_PRICE_SQL + ", si.image_url AS icon_url " +
                        "FROM fivepigs.software s " +
                        "LEFT JOIN fivepigs.software_image si " +
                        "  ON s.software_id = si.software_id AND si.is_thumbnail = 1 " +
                        "WHERE s.category_id = ? " +
                        "ORDER BY s.software_id DESC";
=======
        String sql = """
                    SELECT s.*,
                           CASE
                               WHEN s.is_free = 1 THEN 0
                               ELSE COALESCE(sp.price, 0)
                           END AS price,
                           si.image_url AS icon_url
                    FROM fivepigs.software s
                    LEFT JOIN (
                        SELECT software_id, MIN(price) AS price
                        FROM fivepigs.software_pricing
                        WHERE is_active = 1
                        GROUP BY software_id
                    ) sp ON sp.software_id = s.software_id
                    LEFT JOIN fivepigs.software_image si
                      ON s.software_id = si.software_id AND si.is_thumbnail = 1
                    WHERE s.category_id = ?
                    ORDER BY s.software_id DESC
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

        List<Software> list = new ArrayList<>();

        try (Connection c = Db.getConnection(); PreparedStatement st = c.prepareStatement(sql)) {

            st.setString(1, categoryId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Software sw = new Software();
                sw.setSoftwareId(rs.getInt("software_id"));
                sw.setName(rs.getString("name"));
                sw.setPrice(rs.getDouble("price"));
                sw.setIsFree(rs.getInt("is_free"));
                sw.setAvgRating(rs.getDouble("avg_rating"));
                sw.setIconUrl(rs.getString("icon_url"));
                list.add(sw);
            }
        }
        return list;
    }

    public List<Software> getTopDownloadWithIcon(int limit) throws SQLException {
<<<<<<< HEAD
        String sql =
                "SELECT s.*, " + CUSTOMER_PRICE_SQL + ", img.image_url AS icon_url " +
                        "FROM fivepigs.software s " +
                        "LEFT JOIN fivepigs.software_image img " +
                        "  ON s.software_id = img.software_id AND img.is_thumbnail = 1 " +
                        "ORDER BY s.download_count DESC " +
                        "LIMIT ?";
=======
        String sql = """
                    SELECT s.*,
                           CASE
                               WHEN s.is_free = 1 THEN 0
                               ELSE COALESCE(sp.price, 0)
                           END AS price,
                           img.image_url AS icon_url
                    FROM fivepigs.software s
                    LEFT JOIN (
                        SELECT software_id, MIN(price) AS price
                        FROM fivepigs.software_pricing
                        WHERE is_active = 1
                        GROUP BY software_id
                    ) sp ON sp.software_id = s.software_id
                    LEFT JOIN fivepigs.software_image img
                      ON s.software_id = img.software_id AND img.is_thumbnail = 1
                    ORDER BY s.download_count DESC
                    LIMIT ?
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

        List<Software> list = new ArrayList<>();
        try (Connection c = Db.getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, limit);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Software sw = new Software();
                    sw.setSoftwareId(rs.getInt("software_id"));
                    sw.setName(rs.getString("name"));
                    sw.setAvgRating(rs.getDouble("avg_rating"));
                    sw.setDownloadCount(rs.getInt("download_count"));
                    sw.setIsFree(rs.getInt("is_free"));
                    sw.setPrice(rs.getDouble("price"));
<<<<<<< HEAD
                    sw.setIconUrl(rs.getString("icon_url"));
=======

>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
                    list.add(sw);
                }
            }
        }
        return list;
    }

    public List<Software> getBestSellingWithIcon(int limit) throws SQLException {
<<<<<<< HEAD
        String sql =
                "SELECT s.*, " + CUSTOMER_PRICE_SQL + ", img.image_url AS icon_url, COALESCE(sales.sold_count, 0) AS sold_count " +
                        "FROM fivepigs.software s " +
                        "LEFT JOIN fivepigs.software_image img " +
                        "  ON s.software_id = img.software_id AND img.is_thumbnail = 1 " +
                        "LEFT JOIN ( " +
                        "  SELECT od.software_id, COUNT(*) AS sold_count " +
                        "  FROM fivepigs.order_detail od " +
                        "  JOIN fivepigs.orders o ON od.order_id = o.order_id " +
                        "  JOIN fivepigs.payment_status ps ON o.payment_status_id = ps.payment_status_id " +
                        "  WHERE UPPER(ps.status_name) = 'PAID' " +
                        "  GROUP BY od.software_id " +
                        ") sales ON sales.software_id = s.software_id " +
                        "ORDER BY sold_count DESC, s.download_count DESC, s.software_id DESC " +
                        "LIMIT ?";
=======
        String sql = """
                    SELECT s.*,
                           CASE
                               WHEN s.is_free = 1 THEN 0
                               ELSE COALESCE(sp.price, 0)
                           END AS price,
                           img.image_url AS icon_url,
                           COALESCE(sales.sold_count, 0) AS sold_count
                    FROM fivepigs.software s
                    LEFT JOIN (
                        SELECT software_id, MIN(price) AS price
                        FROM fivepigs.software_pricing
                        WHERE is_active = 1
                        GROUP BY software_id
                    ) sp ON sp.software_id = s.software_id
                    LEFT JOIN fivepigs.software_image img
                      ON s.software_id = img.software_id AND img.is_thumbnail = 1
                    LEFT JOIN (
                      SELECT od.software_id, COUNT(*) AS sold_count
                      FROM fivepigs.order_detail od
                      JOIN fivepigs.orders o ON od.order_id = o.order_id
                      JOIN fivepigs.payment_status ps ON o.payment_status_id = ps.payment_status_id
                      WHERE UPPER(ps.status_name) = 'PAID'
                      GROUP BY od.software_id
                    ) sales ON sales.software_id = s.software_id
                    ORDER BY sold_count DESC, s.download_count DESC, s.software_id DESC
                    LIMIT ?
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

        List<Software> list = new ArrayList<>();
        try (Connection c = Db.getConnection(); PreparedStatement st = c.prepareStatement(sql)) {

            st.setInt(1, Math.max(1, limit));
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Software sw = new Software();
                    sw.setSoftwareId(rs.getInt("software_id"));
                    sw.setName(rs.getString("name"));
                    sw.setAvgRating(rs.getDouble("avg_rating"));
                    sw.setIsFree(rs.getInt("is_free"));
                    sw.setPrice(rs.getDouble("price"));
<<<<<<< HEAD
                    sw.setIconUrl(rs.getString("icon_url"));
=======

>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
                    list.add(sw);
                }
            }
        }
        return list;
    }

    public List<Software> GetDownloadDemo(int a, int b) throws SQLException {
<<<<<<< HEAD
        String sql =
                "SELECT s.*, " + CUSTOMER_PRICE_SQL + "\n" +
                        "FROM fivepigs.software s\n" +
                        "WHERE s.download_count BETWEEN ? AND ?";
=======
        String sql = """
                    SELECT s.*,
                           CASE
                               WHEN s.is_free = 1 THEN 0
                               ELSE COALESCE(sp.price, 0)
                           END AS price
                    FROM fivepigs.software s
                    LEFT JOIN (
                        SELECT software_id, MIN(price) AS price
                        FROM fivepigs.software_pricing
                        WHERE is_active = 1
                        GROUP BY software_id
                    ) sp ON sp.software_id = s.software_id
                    WHERE s.download_count BETWEEN ? AND ?
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

        List<Software> list = new ArrayList<>();
        try (Connection c = Db.getConnection(); PreparedStatement st = c.prepareStatement(sql)) {

            st.setInt(1, a);
            st.setInt(2, b);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Software sw = new Software();
                    sw.setSoftwareId(rs.getInt("software_id"));
                    sw.setName(rs.getString("name"));
                    sw.setIsFree(rs.getInt("is_free"));
                    sw.setPrice(rs.getDouble("price"));
                    sw.setDownloadCount(rs.getInt("download_count"));
                    sw.setAvgRating(rs.getDouble("avg_rating"));
<<<<<<< HEAD
=======

>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
                    list.add(sw);
                }
            }
        }
        return list;
    }


    public List<Software> getLibraryByUserIdWithIcon(int userId) throws SQLException {
<<<<<<< HEAD
        return getLibraryByUserIdWithIcon(userId, "date", "desc");
    }

    public List<Software> getLibraryByUserIdWithIcon(int userId, String sortBy, String sortDir) throws SQLException {
        String orderBy = "lib.last_purchase DESC, s.software_id DESC";
        if ("name".equalsIgnoreCase(sortBy)) {
            orderBy = "LOWER(s.name) " + ("desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC") + ", s.software_id DESC";
        } else if ("date".equalsIgnoreCase(sortBy)) {
            orderBy = "lib.last_purchase " + ("asc".equalsIgnoreCase(sortDir) ? "ASC" : "DESC") + ", s.software_id DESC";
        }

        String sql =
                "SELECT s.*, " + CUSTOMER_PRICE_SQL + ", img.image_url AS icon_url, lib.last_purchase AS access_date " +
                        "FROM fivepigs.software s " +
                        "JOIN ( " +
                          "  SELECT l.software_id, MAX(l.purchase_date) AS last_purchase " +
                          "  FROM fivepigs.license_user lu " +
                          "  JOIN fivepigs.license l ON lu.license_id = l.license_id " +
                         "  WHERE lu.user_id = ? " +
                         "    AND (lu.status IS NULL OR UPPER(lu.status) = 'ACTIVE') " +
                         "    AND (l.status IS NULL OR UPPER(l.status) <> 'REVOKED') " +
                         "    AND (l.expire_date IS NULL OR l.expire_date >= NOW()) " +
                         "  GROUP BY l.software_id " +
                        ") lib ON lib.software_id = s.software_id " +
                        "LEFT JOIN fivepigs.software_image img " +
                        "  ON s.software_id = img.software_id AND img.is_thumbnail = 1 " +
                        "ORDER BY " + orderBy;
=======
        String sql = """
                    SELECT s.*,
                           CASE
                               WHEN s.is_free = 1 THEN 0
                               ELSE COALESCE(sp.price, 0)
                           END AS price,
                           img.image_url AS icon_url
                    FROM fivepigs.software s
                    JOIN (
                      SELECT l.software_id, MAX(l.purchase_date) AS last_purchase
                      FROM fivepigs.license l
                      WHERE l.owner_id = ?
                        AND (l.status IS NULL OR UPPER(l.status) <> 'REVOKED')
                      GROUP BY l.software_id
                    ) lib ON lib.software_id = s.software_id
                    LEFT JOIN (
                        SELECT software_id, MIN(price) AS price
                        FROM fivepigs.software_pricing
                        WHERE is_active = 1
                        GROUP BY software_id
                    ) sp ON sp.software_id = s.software_id
                    LEFT JOIN fivepigs.software_image img
                      ON s.software_id = img.software_id AND img.is_thumbnail = 1
                    ORDER BY lib.last_purchase DESC, s.software_id DESC
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

        List<Software> list = new ArrayList<>();
        try (Connection c = Db.getConnection(); PreparedStatement st = c.prepareStatement(sql)) {

            st.setInt(1, userId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Software sw = new Software();
                    sw.setSoftwareId(rs.getInt("software_id"));
                    sw.setName(rs.getString("name"));
                    sw.setIsFree(rs.getInt("is_free"));
                    sw.setPrice(rs.getDouble("price"));
                    sw.setAvgRating(rs.getDouble("avg_rating"));
<<<<<<< HEAD
                    sw.setIconUrl(rs.getString("icon_url"));
                    sw.setCreatedAt(rs.getObject("access_date", LocalDateTime.class));
=======

>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
                    list.add(sw);
                }
            }
        }
        return list;
    }

    public Map<String, List<Software>> getSoftwareSectionsByCategory(int categoryId) throws SQLException {
        Map<String, List<Software>> sections = new LinkedHashMap<>();

<<<<<<< HEAD
        String sql =
                "SELECT g.name AS genre_name, s.*, " + CUSTOMER_PRICE_SQL + ", si.image_url AS icon_url " +
                        "FROM fivepigs.software s " +
                        "JOIN fivepigs.software_genre sg ON s.software_id = sg.software_id " +
                        "JOIN fivepigs.genre g ON sg.genre_id = g.genre_id " +
                        "LEFT JOIN fivepigs.software_image si " +
                        "ON s.software_id = si.software_id AND si.is_thumbnail = 1 " +
                        "WHERE s.category_id = ? " +
                        "ORDER BY g.name, s.download_count DESC, s.software_id DESC";
=======
        String sql = """
                    SELECT g.name AS genre_name,
                           s.*,
                           CASE
                               WHEN s.is_free = 1 THEN 0
                               ELSE COALESCE(sp.price, 0)
                           END AS price,
                           si.image_url AS icon_url
                    FROM fivepigs.software s
                    JOIN fivepigs.software_genre sg ON s.software_id = sg.software_id
                    JOIN fivepigs.genre g ON sg.genre_id = g.genre_id
                    LEFT JOIN (
                        SELECT software_id, MIN(price) AS price
                        FROM fivepigs.software_pricing
                        WHERE is_active = 1
                        GROUP BY software_id
                    ) sp ON sp.software_id = s.software_id
                    LEFT JOIN fivepigs.software_image si
                      ON s.software_id = si.software_id AND si.is_thumbnail = 1
                    WHERE s.category_id = ?
                    ORDER BY g.name, s.download_count DESC, s.software_id DESC
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

        try (Connection c = Db.getConnection(); PreparedStatement st = c.prepareStatement(sql)) {

            st.setInt(1, categoryId);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    String genreName = rs.getString("genre_name");

                    Software sw = new Software();
                    sw.setSoftwareId(rs.getInt("software_id"));
                    sw.setName(rs.getString("name"));
                    sw.setShortDescription(rs.getString("short_description"));
                    sw.setVendorId(rs.getInt("vendor_id"));
                    sw.setCategoryId(rs.getInt("category_id"));
                    sw.setPrice(rs.getDouble("price"));
                    sw.setIsFree(rs.getInt("is_free"));
                    sw.setStatus(rs.getString("status"));
                    sw.setDownloadCount(rs.getInt("download_count"));
                    sw.setAvgRating(rs.getDouble("avg_rating"));
<<<<<<< HEAD
                    sw.setIconUrl(rs.getString("icon_url"));
=======
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

                    sections.computeIfAbsent(genreName, k -> new ArrayList<>()).add(sw);
                }
            }
        }

        return sections;
    }

    //DAO cho Genre
    public List<String> getGenresByCategory(int categoryId) throws SQLException {
        List<String> list = new ArrayList<>();

<<<<<<< HEAD
        String sql =
                "SELECT DISTINCT g.name " +
                        "FROM fivepigs.genre g " +
                        "JOIN fivepigs.software_genre sg ON g.genre_id = sg.genre_id " +
                        "JOIN fivepigs.software s ON sg.software_id = s.software_id " +
                        "WHERE s.category_id = ? " +
                        "ORDER BY g.name";
=======
        String sql = """
                    SELECT DISTINCT g.name
                    FROM fivepigs.genre g
                    JOIN fivepigs.software_genre sg ON g.genre_id = sg.genre_id
                    JOIN fivepigs.software s ON sg.software_id = s.software_id
                    WHERE s.category_id = ?
                    ORDER BY g.name
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

        try (Connection c = Db.getConnection(); PreparedStatement st = c.prepareStatement(sql)) {

            st.setInt(1, categoryId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        }

        return list;
    }

    public List<Software> getSoftwareByCategoryAndGenre(int categoryId, String genreName) throws SQLException {
        List<Software> list = new ArrayList<>();

<<<<<<< HEAD
        String sql =
                "SELECT s.*, " + CUSTOMER_PRICE_SQL + ", si.image_url AS icon_url " +
                        "FROM fivepigs.software s " +
                        "JOIN fivepigs.software_genre sg ON s.software_id = sg.software_id " +
                        "JOIN fivepigs.genre g ON sg.genre_id = g.genre_id " +
                        "LEFT JOIN fivepigs.software_image si " +
                        "ON s.software_id = si.software_id AND si.is_thumbnail = 1 " +
                        "WHERE s.category_id = ? AND g.name = ? " +
                        "ORDER BY s.download_count DESC";
=======
        String sql = """
                    SELECT s.*,
                           CASE
                               WHEN s.is_free = 1 THEN 0
                               ELSE COALESCE(sp.price, 0)
                           END AS price,
                           si.image_url AS icon_url
                    FROM fivepigs.software s
                    JOIN fivepigs.software_genre sg ON s.software_id = sg.software_id
                    JOIN fivepigs.genre g ON sg.genre_id = g.genre_id
                    LEFT JOIN (
                        SELECT software_id, MIN(price) AS price
                        FROM fivepigs.software_pricing
                        WHERE is_active = 1
                        GROUP BY software_id
                    ) sp ON sp.software_id = s.software_id
                    LEFT JOIN fivepigs.software_image si
                      ON s.software_id = si.software_id AND si.is_thumbnail = 1
                    WHERE s.category_id = ? AND g.name = ?
                    ORDER BY s.download_count DESC
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

        try (Connection c = Db.getConnection(); PreparedStatement st = c.prepareStatement(sql)) {

            st.setInt(1, categoryId);
            st.setString(2, genreName);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Software sw = new Software();
                sw.setSoftwareId(rs.getInt("software_id"));
                sw.setName(rs.getString("name"));
                sw.setPrice(rs.getDouble("price"));
                sw.setIsFree(rs.getInt("is_free"));
                sw.setAvgRating(rs.getDouble("avg_rating"));
                sw.setIconUrl(rs.getString("icon_url"));
                list.add(sw);
            }
        }

        return list;
    }



    public String getSoftwareNameById(int softwareId) throws SQLException {
        String sql = "SELECT name FROM fivepigs.software WHERE software_id = ?";
        try (Connection c = Db.getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, softwareId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        }
        return null;
    }

    public List<SoftwarePricing> getActivePricingBySoftwareId(int softwareId) throws SQLException {
        String sql = "SELECT pricing_id, software_id, plan_name, max_users, price, duration_days, is_active, created_at " +
                "FROM fivepigs.software_pricing " +
                "WHERE software_id = ? AND is_active = 1 " +
                "  AND UPPER(COALESCE(plan_name, '')) <> 'DEMO' " +
                "ORDER BY price ASC, pricing_id ASC";

        List<SoftwarePricing> list = new ArrayList<>();
        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, softwareId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    SoftwarePricing pricing = new SoftwarePricing();
                    Number pricingIdValue = (Number) rs.getObject("pricing_id");
                    pricing.setPricingId(pricingIdValue == null ? null : pricingIdValue.intValue());
                    Number softwareIdValue = (Number) rs.getObject("software_id");
                    pricing.setSoftwareId(softwareIdValue == null ? null : softwareIdValue.intValue());
                    pricing.setPlanName(rs.getString("plan_name"));
                    Number maxUsersValue = (Number) rs.getObject("max_users");
                    pricing.setMaxUsers(maxUsersValue == null ? null : maxUsersValue.intValue());
                    pricing.setPrice(rs.getDouble("price"));
                    Number durationDaysValue = (Number) rs.getObject("duration_days");
                    pricing.setDurationDays(durationDaysValue == null ? null : durationDaysValue.intValue());
                    Object isActiveValue = rs.getObject("is_active");
                    if (isActiveValue instanceof Boolean booleanValue) {
                        pricing.setIsActive(booleanValue ? 1 : 0);
                    } else if (isActiveValue instanceof Number numberValue) {
                        pricing.setIsActive(numberValue.intValue());
                    } else {
                        pricing.setIsActive(null);
                    }
                    pricing.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                    list.add(pricing);
                }
            }
        }
        return list;
    }

    public SoftwarePricing getDemoPricingBySoftwareId(int softwareId) throws SQLException {
        String sql = "SELECT pricing_id, software_id, plan_name, max_users, price, duration_days, is_active, created_at " +
                "FROM fivepigs.software_pricing " +
                "WHERE software_id = ? AND is_active = 1 AND UPPER(COALESCE(plan_name, '')) = 'DEMO' " +
                "ORDER BY pricing_id ASC LIMIT 1";

        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, softwareId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    SoftwarePricing pricing = new SoftwarePricing();
                    Number pricingIdValue = (Number) rs.getObject("pricing_id");
                    pricing.setPricingId(pricingIdValue == null ? null : pricingIdValue.intValue());
                    Number softwareIdValue = (Number) rs.getObject("software_id");
                    pricing.setSoftwareId(softwareIdValue == null ? null : softwareIdValue.intValue());
                    pricing.setPlanName(rs.getString("plan_name"));
                    Number maxUsersValue = (Number) rs.getObject("max_users");
                    pricing.setMaxUsers(maxUsersValue == null ? null : maxUsersValue.intValue());
                    pricing.setPrice(rs.getDouble("price"));
                    Number durationDaysValue = (Number) rs.getObject("duration_days");
                    pricing.setDurationDays(durationDaysValue == null ? null : durationDaysValue.intValue());
                    Object isActiveValue = rs.getObject("is_active");
                    if (isActiveValue instanceof Boolean booleanValue) {
                        pricing.setIsActive(booleanValue ? 1 : 0);
                    } else if (isActiveValue instanceof Number numberValue) {
                        pricing.setIsActive(numberValue.intValue());
                    } else {
                        pricing.setIsActive(null);
                    }
                    pricing.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                    return pricing;
                }
            }
        }
        return null;
    }

    public String getActiveFileUrlBySoftwareId(int softwareId) throws SQLException {
<<<<<<< HEAD
        String sql = "SELECT file_url FROM fivepigs.software_version " +
                "WHERE software_id = ? AND is_active = 1 " +
                "ORDER BY version_id DESC LIMIT 1";
        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {
=======
        String sql = """
                    SELECT file_url
                    FROM fivepigs.software_version
                    WHERE software_id = ? AND is_active = 1
                    ORDER BY version_id DESC
                    LIMIT 1
                """;
        try (Connection c = Db.getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
            st.setInt(1, softwareId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("file_url");
                }
            }
        }
        return null;
    }

    public void increaseDownloadCount(int softwareId) throws SQLException {
        String sql = "UPDATE fivepigs.software SET download_count = COALESCE(download_count, 0) + 1 WHERE software_id = ?";
        try (Connection c = Db.getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, softwareId);
            st.executeUpdate();
        }
    }

    public List<Software> searchSoftwareWithIcon(String keyword, int limit) throws SQLException {
        return searchSoftwareWithIcon(keyword, null, null, limit);
    }

    public List<Software> searchSoftwareWithIcon(String keyword, Integer categoryId, int limit) throws SQLException {
        return searchSoftwareWithIcon(keyword, categoryId, null, limit);
    }

<<<<<<< HEAD
    public List<Software> searchSoftwareWithIcon(String keyword, Integer categoryId, String genreName, int limit) throws SQLException {
        String sql = "SELECT DISTINCT s.*, " + CUSTOMER_PRICE_SQL + ", img.image_url AS icon_url " +
                "FROM fivepigs.software s " +
                "LEFT JOIN fivepigs.software_image img " +
                "  ON s.software_id = img.software_id AND img.is_thumbnail = 1 " +
                "LEFT JOIN fivepigs.software_genre sg ON s.software_id = sg.software_id " +
                "LEFT JOIN fivepigs.genre g ON sg.genre_id = g.genre_id " +
                "WHERE ((? = '') OR LOWER(s.name) LIKE ? OR LOWER(COALESCE(s.short_description, '')) LIKE ?) " +
                "  AND (? IS NULL OR s.category_id = ?) " +
                "  AND (? IS NULL OR LOWER(g.name) = LOWER(?)) " +
                "ORDER BY s.download_count DESC, s.avg_rating DESC, s.software_id DESC " +
                "LIMIT ?";
=======
    public List<Software> searchSoftwareWithIcon(String keyword, Integer categoryId, String genreName, int limit)
            throws SQLException {
        String sql = """
                    SELECT DISTINCT s.*,
                           CASE
                               WHEN s.is_free = 1 THEN 0
                               ELSE COALESCE(sp.price, 0)
                           END AS price,
                           img.image_url AS icon_url
                    FROM fivepigs.software s
                    LEFT JOIN (
                        SELECT software_id, MIN(price) AS price
                        FROM fivepigs.software_pricing
                        WHERE is_active = 1
                        GROUP BY software_id
                    ) sp ON sp.software_id = s.software_id
                    LEFT JOIN fivepigs.software_image img
                      ON s.software_id = img.software_id AND img.is_thumbnail = 1
                    LEFT JOIN fivepigs.software_genre sg ON s.software_id = sg.software_id
                    LEFT JOIN fivepigs.genre g ON sg.genre_id = g.genre_id
                    WHERE ((? = '') OR LOWER(s.name) LIKE ? OR LOWER(COALESCE(s.short_description, '')) LIKE ?)
                      AND (? IS NULL OR s.category_id = ?)
                      AND (? IS NULL OR LOWER(g.name) = LOWER(?))
                    ORDER BY s.download_count DESC, s.avg_rating DESC, s.software_id DESC
                    LIMIT ?
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

        List<Software> list = new ArrayList<>();
        String q = keyword == null ? "" : keyword.trim().toLowerCase();
        String like = "%" + q + "%";
        String normalizedGenre = (genreName == null || genreName.trim().isEmpty()) ? null : genreName.trim();

        try (Connection c = Db.getConnection(); PreparedStatement st = c.prepareStatement(sql)) {

            st.setString(1, q);
            st.setString(2, like);
            st.setString(3, like);
            if (categoryId == null) {
                st.setNull(4, Types.INTEGER);
                st.setNull(5, Types.INTEGER);
            } else {
                st.setInt(4, categoryId);
                st.setInt(5, categoryId);
            }
            if (normalizedGenre == null) {
                st.setNull(6, Types.VARCHAR);
                st.setNull(7, Types.VARCHAR);
            } else {
                st.setString(6, normalizedGenre);
                st.setString(7, normalizedGenre);
            }
            st.setInt(8, Math.max(1, limit));

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Software sw = new Software();
                    sw.setSoftwareId(rs.getInt("software_id"));
                    sw.setName(rs.getString("name"));
                    sw.setShortDescription(rs.getString("short_description"));
                    sw.setCategoryId(rs.getInt("category_id"));
                    sw.setPrice(rs.getDouble("price"));
                    sw.setIsFree(rs.getInt("is_free"));
                    sw.setAvgRating(rs.getDouble("avg_rating"));
                    sw.setDownloadCount(rs.getInt("download_count"));
<<<<<<< HEAD
                    sw.setIconUrl(rs.getString("icon_url"));
=======

>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
                    list.add(sw);
                }
            }
        }
        return list;
    }

    public void createSoftwareDetail(
            int softwareId,
            String description,
            String systemRequire,
            String releaseNote) throws Exception {

<<<<<<< HEAD
        String sql = "INSERT INTO Software_Detail(software_id, description, system_requirement, release_note) "
                + "VALUES (?, ?, ?, ?)";
=======
        String sql = """
                    INSERT INTO Software_Detail(software_id, description, system_requirement, release_note)
                    VALUES (?, ?, ?, ?)
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, softwareId);
            ps.setString(2, description);
            ps.setString(3, systemRequire);
            ps.setString(4, releaseNote);

            ps.executeUpdate();
        }
    }

    public void addSoftwareImage(int softwareId, String imageUrl, boolean isThumbnail) throws SQLException {

        String sql = "INSERT INTO Software_Image(software_id, image_url, is_thumbnail) VALUES (?, ?, ?)";

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, softwareId);
            ps.setString(2, imageUrl);
            ps.setBoolean(3, isThumbnail);

            ps.executeUpdate();
        }
    }

    public void addSoftwareVersion(
            int softwareId,
            String versionName,
            String fileUrl,
            String releaseNote,
            long fileSize) throws SQLException {

        String deactivateSql = """
                    UPDATE Software_Version
                    SET is_active = 0
                    WHERE software_id = ?
                """;

        String insertSql = """
                    INSERT INTO Software_Version
                    (software_id, version_name, file_url, release_note, file_size)
                    VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = Db.getConnection()) {

            conn.setAutoCommit(false); // start transaction

            try (
                    PreparedStatement ps1 = conn.prepareStatement(deactivateSql); PreparedStatement ps2 = conn.prepareStatement(insertSql);) {

                // deactivate old versions
                ps1.setInt(1, softwareId);
                ps1.executeUpdate();

                // insert new version
                ps2.setInt(1, softwareId);
                ps2.setString(2, versionName);
                ps2.setString(3, fileUrl);
                ps2.setString(4, releaseNote);
                ps2.setLong(5, fileSize);

                ps2.executeUpdate();

                conn.commit(); // success

            } catch (Exception e) {

                conn.rollback(); // nếu insert lỗi thì rollback
                throw e;

            }
        }
    }

    public void changeStatusSoftware(int softwareId, String status) throws SQLException {

        String sql = "UPDATE Software\n"
                + "SET status = ?\n"
                + "WHERE software_id = ?\n";

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(2, softwareId);
            ps.setString(1, status);
            ps.executeUpdate();
        }
    }

    public List<Software> getSoftwareVersionBySoftwareId(int softwareId) throws SQLException {
        List<Software> list = new ArrayList<>();
        String sql = "SELECT s.name,sv.version_id,sv.version_name,sv.release_note,sv.file_size,sv.created_at,sv.is_active\n"
                + "FROM software s\n"
                + "JOIN software_version sv ON s.software_id=sv.software_id\n"
                + "WHERE s.software_id=?";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Software sw = new Software();
                    SoftwareVersion swVersion = new SoftwareVersion();
                    sw.setName(rs.getString("name"));
                    swVersion.setReleaseNote(rs.getString("release_note"));
                    swVersion.setFileSize(rs.getLong("file_size"));
                    swVersion.setVersionId(rs.getInt("version_id"));
                    swVersion.setVersionName(rs.getString("version_name"));
                    swVersion.setIsActive(rs.getInt("is_active"));
                    swVersion.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                    sw.setSoftwareVersion(swVersion);
                    list.add(sw);
                }
            }
        }
        return list;
    }

    public List<Software> getTopDownloads(int vendorId) throws SQLException {

        List<Software> list = new ArrayList<>();

<<<<<<< HEAD
        String sql = "INSERT INTO Software_Version(software_id, version_name, file_url, release_note, file_size) "
                + "VALUES (?, ?, ?, ?, ?)";
=======
        String sql = """
                    SELECT name, download_count
                    FROM Software
                    WHERE vendor_id = ?
                    ORDER BY download_count DESC
                    LIMIT 5
                """;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vendorId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Software sw = new Software();

                sw.setName(rs.getString("name"));
                sw.setDownloadCount(rs.getInt("download_count"));

                list.add(sw);
            }
        }

        return list;
    }

    public void updateSoftware(int id, String name,
            String shortDesc, int categoryId, double price) throws SQLException {

        String sql = """
                    UPDATE Software
                    SET name=?, short_description=?, category_id=?, price=?, status='PENDING_REVIEW'
                    WHERE software_id=?
                """;

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, shortDesc);
            ps.setInt(3, categoryId);
            ps.setDouble(4, price);
            ps.setInt(5, id);

            ps.executeUpdate();
        }
    }

<<<<<<< HEAD
    public void changeStatusSoftware(int vendorId, int softwareId,String status) throws SQLException {

        String sql = "UPDATE Software\n"
                + "SET status = ?\n"
                + "WHERE software_id = ?\n"
                + "AND vendor_id = ?;";

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(3, vendorId);
            ps.setInt(2, softwareId);
            ps.setString(1, status);

=======
    public void updateSoftwareDetail(int id,
            String description,
            String systemRequire,
            String releaseNote) throws SQLException {

        String sql = """
                    UPDATE Software_Detail
                    SET description=?, system_requirement=?, release_note=?
                    WHERE software_id=?
                """;

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, description);
            ps.setString(2, systemRequire);
            ps.setString(3, releaseNote);
            ps.setInt(4, id);

>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
            ps.executeUpdate();
        }
    }

    public void updateThumbnail(int softwareId, String imageUrl) throws SQLException {

        String removeOld = """
                    UPDATE Software_Image
                    SET is_thumbnail=0
                    WHERE software_id=?
                """;

        String insertNew = """
                    INSERT INTO Software_Image(software_id,image_url,is_thumbnail)
                    VALUES(?,?,1)
                """;

        try (Connection conn = Db.getConnection()) {

            conn.setAutoCommit(false);

            try (
                    PreparedStatement ps1 = conn.prepareStatement(removeOld); PreparedStatement ps2 = conn.prepareStatement(insertNew)) {

                ps1.setInt(1, softwareId);
                ps1.executeUpdate();

                ps2.setInt(1, softwareId);
                ps2.setString(2, imageUrl);
                ps2.executeUpdate();

                conn.commit();

            } catch (Exception e) {

                conn.rollback();
                throw e;
            }
        }
    }

    public void activateVersion(int softwareId, int versionId) throws SQLException {
        String reset = "UPDATE Software_Version SET is_active = 0 WHERE software_id = ?";
        String set = "UPDATE Software_Version SET is_active = 1 WHERE version_id = ?";

        try (Connection c = Db.getConnection()) {
            c.setAutoCommit(false);

            try (
                    PreparedStatement ps1 = c.prepareStatement(reset); PreparedStatement ps2 = c.prepareStatement(set)) {
                ps1.setInt(1, softwareId);
                ps1.executeUpdate();

                ps2.setInt(1, versionId);
                ps2.executeUpdate();

                c.commit();
            } catch (Exception e) {
                c.rollback();
                throw e;
            }
        }
    }

    public void insertSoftwareGenres(int softwareId, String[] genreIds) throws SQLException {

        String sql = "INSERT INTO Software_Genre(software_id, genre_id) VALUES (?, ?)";

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            for (String gid : genreIds) {
                ps.setInt(1, softwareId);
                ps.setInt(2, Integer.parseInt(gid));
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    public Software getSoftwareById(int softwareId) throws SQLException {
<<<<<<< HEAD
        String sql = """
        SELECT s.software_id,
               s.name,
               s.short_description,
               s.price,
               s.created_at,
               c.category_name,
               u.full_name AS vendor_name,
               sv.version_name AS version,
               si.image_url
        FROM Software s
        LEFT JOIN Category c
               ON s.category_id = c.category_id
        LEFT JOIN Users u
               ON s.vendor_id = u.user_id
        LEFT JOIN Software_Version sv
               ON sv.software_id = s.software_id
              AND sv.is_active = 1
        LEFT JOIN Software_Image si
               ON s.software_id = si.software_id
              AND si.is_thumbnail = 1
        WHERE s.software_id = ?
        LIMIT 1
    """;

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
=======
    String sql = """
            SELECT s.software_id,
                   s.name,
                   s.short_description,
                   s.vendor_id,
                   s.created_at,
                   c.category_name,
                   u.full_name AS vendor_name,
                   sv.version_name AS version,
                   si.image_url
            FROM Software s
            LEFT JOIN Category c
                   ON s.category_id = c.category_id
            LEFT JOIN Users u
                   ON s.vendor_id = u.user_id
            LEFT JOIN Software_Version sv
                   ON sv.software_id = s.software_id
                  AND sv.is_active = 1
            LEFT JOIN Software_Image si
                   ON s.software_id = si.software_id
                  AND si.is_thumbnail = 1
            WHERE s.software_id = ?
            LIMIT 1
            """;

    try (Connection conn = Db.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

        ps.setInt(1, softwareId);

<<<<<<< HEAD
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Software s = new Software();
                    s.setSoftwareId(rs.getInt("software_id"));
                    s.setName(rs.getString("name"));
                    s.setShortDescription(rs.getString("short_description"));
                    s.setPrice(rs.getDouble("price"));
                    s.setCategoryName(rs.getString("category_name"));
                    s.setVersion(rs.getString("version"));
                    s.setImageUrl(rs.getString("image_url"));
=======
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Software s = new Software();
                s.setSoftwareId(rs.getInt("software_id"));
                s.setName(rs.getString("name"));
                s.setShortDescription(rs.getString("short_description"));
                s.setVendorId(rs.getInt("vendor_id"));
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

                // ❌ bỏ price hoàn toàn
                // s.setPrice(...);

                s.setCategoryName(rs.getString("category_name"));
                s.setVersion(rs.getString("version"));
                s.setImageUrl(rs.getString("image_url"));

                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) {
                    s.setCreatedAt(ts.toLocalDateTime());
                }

                return s;
            }
        }
    }

<<<<<<< HEAD
    // update status trong pending reviews
=======
    return null;
}

>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
    public void updateSoftwareStatus(int softwareId, String status) throws SQLException {
        String sql = "UPDATE Software SET status = ? WHERE software_id = ?";

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, softwareId);
            ps.executeUpdate();
        }
    }
<<<<<<< HEAD



}










=======
}
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
