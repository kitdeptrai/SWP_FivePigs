package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.Category;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.SoftwareDetail;
import com.fivepigs.app.model.SoftwareImage;
import com.fivepigs.app.model.SoftwareVersion;
import com.fivepigs.app.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SoftwareDao {

    public Integer pendingReviewApp() throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM Software WHERE status = 'PENDING_REVIEW'";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        return 0;
    }

    // Đếm số app đã được review (completed)
    public int countPendingReviewSoftware() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM Software WHERE status = 'PENDING_REVIEW'";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    public Integer completeReviewApp() throws SQLException {
        String sql = "SELECT COUNT(DISTINCT software_id) AS count FROM Software_Review_Process";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        return 0;
    }

    public Integer reviewedToday() throws SQLException {
        String sql = """
            SELECT COUNT(DISTINCT software_id) AS count
            FROM Software_Review_Process
            WHERE reviewed_at >= CURDATE()
              AND reviewed_at < CURDATE() + INTERVAL 1 DAY
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

    public List<Software> getPendingSoftware() throws SQLException {
        List<Software> list = new ArrayList<>();
        

        String sql = """
            SELECT s.software_id,
                   s.name,
                   s.short_description,
                   COALESCE(p.base_price, 0) AS price,
                   s.status,
                   s.created_at,
                   sv.version_name AS version,
                   c.category_name
            FROM Software s
            LEFT JOIN Category c
                   ON s.category_id = c.category_id
            LEFT JOIN (
                SELECT software_id, MIN(price) AS base_price
                FROM Software_Pricing
                WHERE is_active = 1
                GROUP BY software_id
            ) p
                   ON p.software_id = s.software_id
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
                s.setShortDescription(rs.getString("short_description"));
                s.setPrice(rs.getDouble("price"));
                s.setStatus(rs.getString("status"));


                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) {
                    s.setCreatedAt(ts.toLocalDateTime());
                }

                s.setVersion(rs.getString("version"));
                s.setCategoryName(rs.getString("category_name"));
                list.add(s);
            }
        }

        return list;
    }

    

    

    public List<Software> searchPendingSoftware(String keyword) throws SQLException {
        List<Software> list = new ArrayList<>();

        String sql = """
            SELECT s.software_id,
                   s.name,
                   s.short_description,
                   COALESCE(p.base_price, 0) AS price,
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
            LEFT JOIN (
                SELECT software_id, MIN(price) AS base_price
                FROM Software_Pricing
                WHERE is_active = 1
                GROUP BY software_id
            ) p
                   ON p.software_id = s.software_id
            LEFT JOIN Software_Version sv
                   ON sv.software_id = s.software_id
                  AND sv.is_active = 1
            LEFT JOIN Software_Image si
                   ON s.software_id = si.software_id
                  AND si.is_thumbnail = 1
            WHERE s.status = 'PENDING_REVIEW'
              AND (
                    s.name LIKE ?
                    OR s.short_description LIKE ?
                    OR c.category_name LIKE ?
                    OR u.full_name LIKE ?
                    OR sv.version_name LIKE ?
                    OR CAST(COALESCE(p.base_price, 0) AS CHAR) LIKE ?
                  )
            ORDER BY s.created_at DESC
        """;

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String likeKeyword = "%" + keyword + "%";
            ps.setString(1, likeKeyword);
            ps.setString(2, likeKeyword);
            ps.setString(3, likeKeyword);
            ps.setString(4, likeKeyword);
            ps.setString(5, likeKeyword);
            ps.setString(6, likeKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Software s = new Software();
                    s.setSoftwareId(rs.getInt("software_id"));
                    s.setName(rs.getString("name"));
                    s.setShortDescription(rs.getString("short_description"));
                    s.setPrice(rs.getDouble("price"));
                    s.setCategoryName(rs.getString("category_name"));
                    s.setVersion(rs.getString("version"));
                    s.setImageUrl(rs.getString("image_url"));

                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) {
                        s.setCreatedAt(ts.toLocalDateTime());
                    }

                    list.add(s);
                }
            }
        }
        return list;
    }

    public Software getSoftwareById(int softwareId) throws SQLException {
        String sql = """
            SELECT s.software_id,
                   s.name,
                   s.short_description,
                   COALESCE(p.base_price, 0) AS price,
                   s.created_at,
                   c.category_name,
                   u.full_name AS vendor_name,
                   sv.version_name AS version,
                   sv.file_url AS file_url,
                   sv.file_size AS file_size,
                   si.image_url
            FROM Software s
            LEFT JOIN Category c
                   ON s.category_id = c.category_id
            LEFT JOIN Users u
                   ON s.vendor_id = u.user_id
            LEFT JOIN (
                SELECT software_id, MIN(price) AS base_price
                FROM Software_Pricing
                WHERE is_active = 1
                GROUP BY software_id
            ) p
                   ON p.software_id = s.software_id
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

            ps.setInt(1, softwareId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Software s = new Software();
                    SoftwareVersion sv = new SoftwareVersion();

                    s.setSoftwareId(rs.getInt("software_id"));
                    s.setName(rs.getString("name"));
                    s.setShortDescription(rs.getString("short_description"));
                    s.setPrice(rs.getDouble("price"));
                    s.setCategoryName(rs.getString("category_name"));
                    s.setVersion(rs.getString("version"));
                    s.setImageUrl(rs.getString("image_url"));

                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) {
                        s.setCreatedAt(ts.toLocalDateTime());
                    }

                    sv.setSoftwareId(rs.getInt("software_id"));
                    sv.setVersionName(rs.getString("version"));
                    sv.setFileUrl(rs.getString("file_url"));
                    sv.setFileSize(rs.getLong("file_size"));

                    s.setSoftwareVersion(sv);
                    return s;
                }
            }
        }

        return null;
    }


    public List<Software> getMyReviews(Integer reviewerId) throws SQLException {
        List<Software> list = new ArrayList<>();

        String sql = """
            SELECT s.software_id,
                   s.name,
                   s.short_description,
                   COALESCE(p.base_price, 0) AS price,
                   COALESCE(p.base_price, 0) AS price,
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
            LEFT JOIN (
                SELECT software_id, MIN(price) AS base_price
                FROM Software_Pricing
                WHERE is_active = 1
                GROUP BY software_id
            ) p
                 ON p.software_id = s.software_id
            LEFT JOIN Software_Image si
                   ON s.software_id = si.software_id
                  AND si.is_thumbnail = 1
            LEFT JOIN Software_Version sv
                   ON sv.software_id = s.software_id
                  AND sv.is_active = 1
            WHERE rp.reviewer_id = ?
            ORDER BY rp.reviewed_at DESC
        """;

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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
                    s.setCategoryName(rs.getString("category_name"));
                    s.setImageUrl(rs.getString("image_url"));
                    s.setQualityScore(null);
                    s.setQualityScore(null);

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
                   COALESCE(p.base_price, 0) AS price,
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
            LEFT JOIN (
                SELECT software_id, MIN(price) AS base_price
                FROM Software_Pricing
                WHERE is_active = 1
                GROUP BY software_id
            ) p
                 ON p.software_id = s.software_id
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
                    s.setVersion(rs.getString("version"));
                    s.setCategoryName(rs.getString("category_name"));
                    s.setImageUrl(rs.getString("image_url"));

                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) {
                        s.setCreatedAt(ts.toLocalDateTime());
                    }

                    list.add(s);
                }
            }
        }
        return list;
    }


    public List<Software> Top3RevenueByVendor(Integer vendorId) throws SQLException {
        List<Software> list = new ArrayList<>();
        String sql = """
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
        String sql = "SELECT \n"
                + "    COUNT(*) AS total_apps\n"
                + "FROM Software\n"
                + "WHERE vendor_id = ?;";
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
        String sql = "SELECT \n"
                + "    SUM(download_count) AS total_download_count\n"
                + "FROM Software\n"
                + "WHERE vendor_id = ?;";
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
        String sql = "SELECT \n"
                + "    COUNT(*) AS total_apps\n"
                + "FROM Software\n"
                + "WHERE vendor_id = ?\n"
                + "AND status = ?;";
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
        String sql = "SELECT \n"
                + "SUM(od.price) AS total_revenue\n"
                + "FROM Order_Detail od\n"
                + "JOIN Orders o ON od.order_id = o.order_id\n"
                + "JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id\n"
                + "JOIN Software s ON od.software_id = s.software_id\n"
                + "WHERE ps.status_name = 'PAID'\n"
                + "AND s.vendor_id = ?;";
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
        String sql = "SELECT download_count FROM Software\n"
                + "WHERE software_id=?;";
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
        String sql = "SELECT avg_rating FROM Software\n"
                + "WHERE software_id=?;";
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
        String sql = "SELECT COUNT(license_id) AS total_license FROM License\n"
                + "WHERE software_id=?;";
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
        String sql = "SELECT \n"
                + "    SUM(od.price) AS total_revenue\n"
                + "FROM Order_Detail od\n"
                + "JOIN Orders o ON od.order_id = o.order_id\n"
                + "JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id\n"
                + "WHERE ps.status_name = 'PAID'\n"
                + "AND od.software_id = ?;";
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

    public Software getSoftwareDetailBySoftwareId(int softwareId) throws SQLException {
        String sql = """
                SELECT s.software_id,
                       s.name,
                       s.short_description,
                       s.price,
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
                       sd.language,
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
                    swDetail.setLanguage(rs.getString("language"));
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


    //Upload Software
    public int createSoftware(Software software) throws Exception {

        String sql = "INSERT INTO Software(name, short_description, vendor_id, category_id, price) "
                + "VALUES (?, ?, ?, ?, ?)";

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

    //Software of Customer (Create by TKiet)

    private final String Get_All_Product = "SELECT * FROM fivepigs.software";
    private final String GET_SOFTWARE_BY_ID = "SELECT * FROM fivepigs.software\n" +
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
        String sql = "SELECT * FROM fivepigs.software WHERE category_id = ?";

        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {

            st.setInt(1, categoryId);
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
                list.add(sw);
            }
        }
        return list;
    }

    public Software GETALLSOFTWAREBYID(String cid) {

        try(Connection c = Db.getConnection();
            PreparedStatement st = c.prepareStatement(GET_SOFTWARE_BY_ID)) {
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
        try(Connection c = Db.getConnection();
            PreparedStatement st = c.prepareStatement(Get_SoftwareImage_By_Id)) {
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
        String sql = "SELECT * FROM fivepigs.software_image \n" +
                "                WHERE software_id = ? AND is_thumbnail = 1 \n" +
                "                ORDER BY image_id DESC LIMIT 1";

        try(Connection c = Db.getConnection();
            PreparedStatement st = c.prepareStatement(sql)) {

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

        String sql = "SELECT * " +
                "FROM fivepigs.software_image " +
                "WHERE software_id = ? AND is_thumbnail = 0 " +
                "ORDER BY image_id ASC";

        try(Connection c = Db.getConnection();
            PreparedStatement st = c.prepareStatement(sql)) {

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
        String sql =
                "SELECT s.*, si.image_url AS icon_url " +
                        "FROM fivepigs.software s " +
                        "LEFT JOIN fivepigs.software_image si " +
                        "  ON s.software_id = si.software_id AND si.is_thumbnail = 1 " +
                        "WHERE s.category_id = ? " +
                        "ORDER BY s.software_id DESC";

        List<Software> list = new ArrayList<>();

        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {

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
        String sql =
                "SELECT s.*, img.image_url AS icon_url " +
                        "FROM fivepigs.software s " +
                        "LEFT JOIN fivepigs.software_image img " +
                        "  ON s.software_id = img.software_id AND img.is_thumbnail = 1 " +
                        "ORDER BY s.download_count DESC " +
                        "LIMIT ?";

        List<Software> list = new ArrayList<>();
        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {
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
                    sw.setIconUrl(rs.getString("icon_url"));
                    list.add(sw);
                }
            }
        }
        return list;
    }

    public List<Software> getBestSellingWithIcon(int limit) throws SQLException {
        String sql =
                "SELECT s.*, img.image_url AS icon_url, COALESCE(sales.sold_count, 0) AS sold_count " +
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

        List<Software> list = new ArrayList<>();
        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {

            st.setInt(1, Math.max(1, limit));
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Software sw = new Software();
                    sw.setSoftwareId(rs.getInt("software_id"));
                    sw.setName(rs.getString("name"));
                    sw.setAvgRating(rs.getDouble("avg_rating"));
                    sw.setIsFree(rs.getInt("is_free"));
                    sw.setPrice(rs.getDouble("price"));
                    sw.setIconUrl(rs.getString("icon_url"));
                    list.add(sw);
                }
            }
        }
        return list;
    }

    public List<Software> GetDownloadDemo(int a, int b) throws SQLException {
        String sql =
                "SELECT * FROM fivepigs.software\n" +
                        "Where download_count between ? and ?";

        List<Software> list = new ArrayList<>();
        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {

            st.setInt(1, a);
            st.setInt(1, b);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Software sw = new Software();
                    sw.setSoftwareId(rs.getInt("software_id"));
                    sw.setName(rs.getString("name"));
                    sw.setIsFree(rs.getInt("is_free"));
                    sw.setPrice(rs.getDouble("price"));
                    sw.setDownloadCount(rs.getInt("downloadCount"));
                    sw.setAvgRating(rs.getDouble("avg_rating"));
                    sw.setIconUrl(rs.getString("icon_url"));
                    list.add(sw);
                }
            }
        }
        return list;
    }


    public List<Software> getLibraryByUserIdWithIcon(int userId) throws SQLException {
        String sql =
                "SELECT s.*, img.image_url AS icon_url " +
                        "FROM fivepigs.software s " +
                        "JOIN ( " +
                        "  SELECT l.software_id, MAX(l.purchase_date) AS last_purchase " +
                        "  FROM fivepigs.license l " +
                        "  WHERE l.customer_id = ? " +
                        "    AND (l.status IS NULL OR UPPER(l.status) <> 'REVOKED') " +
                        "  GROUP BY l.software_id " +
                        ") lib ON lib.software_id = s.software_id " +
                        "LEFT JOIN fivepigs.software_image img " +
                        "  ON s.software_id = img.software_id AND img.is_thumbnail = 1 " +
                        "ORDER BY lib.last_purchase DESC, s.software_id DESC";

        List<Software> list = new ArrayList<>();
        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {

            st.setInt(1, userId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Software sw = new Software();
                    sw.setSoftwareId(rs.getInt("software_id"));
                    sw.setName(rs.getString("name"));
                    sw.setIsFree(rs.getInt("is_free"));
                    sw.setPrice(rs.getDouble("price"));
                    sw.setAvgRating(rs.getDouble("avg_rating"));
                    sw.setIconUrl(rs.getString("icon_url"));
                    list.add(sw);
                }
            }
        }
        return list;
    }

    public Map<String, List<Software>> getSoftwareSectionsByCategory(int categoryId) throws SQLException {
        Map<String, List<Software>> sections = new LinkedHashMap<>();

        String sql =
                "SELECT g.name AS genre_name, s.*, si.image_url AS icon_url " +
                        "FROM fivepigs.software s " +
                        "JOIN fivepigs.software_genre sg ON s.software_id = sg.software_id " +
                        "JOIN fivepigs.genre g ON sg.genre_id = g.genre_id " +
                        "LEFT JOIN fivepigs.software_image si " +
                        "ON s.software_id = si.software_id AND si.is_thumbnail = 1 " +
                        "WHERE s.category_id = ? " +
                        "ORDER BY g.name, s.download_count DESC, s.software_id DESC";

        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {

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
                    sw.setIconUrl(rs.getString("icon_url"));

                    sections.computeIfAbsent(genreName, k -> new ArrayList<>()).add(sw);
                }
            }
        }

        return sections;
    }

    //DAO cho Genre
    public List<String> getGenresByCategory(int categoryId) throws SQLException {
        List<String> list = new ArrayList<>();

        String sql =
                "SELECT DISTINCT g.name " +
                        "FROM fivepigs.genre g " +
                        "JOIN fivepigs.software_genre sg ON g.genre_id = sg.genre_id " +
                        "JOIN fivepigs.software s ON sg.software_id = s.software_id " +
                        "WHERE s.category_id = ? " +
                        "ORDER BY g.name";

        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {

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

        String sql =
                "SELECT s.*, si.image_url AS icon_url " +
                        "FROM fivepigs.software s " +
                        "JOIN fivepigs.software_genre sg ON s.software_id = sg.software_id " +
                        "JOIN fivepigs.genre g ON sg.genre_id = g.genre_id " +
                        "LEFT JOIN fivepigs.software_image si " +
                        "ON s.software_id = si.software_id AND si.is_thumbnail = 1 " +
                        "WHERE s.category_id = ? AND g.name = ? " +
                        "ORDER BY s.download_count DESC";

        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {

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
        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, softwareId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        }
        return null;
    }

    public String getActiveFileUrlBySoftwareId(int softwareId) throws SQLException {
        String sql = "SELECT file_url FROM fivepigs.software_version " +
                "WHERE software_id = ? AND is_active = 1 " +
                "ORDER BY version_id DESC LIMIT 1";
        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {
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
        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {
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

    public List<Software> searchSoftwareWithIcon(String keyword, Integer categoryId, String genreName, int limit) throws SQLException {
        String sql = "SELECT DISTINCT s.*, img.image_url AS icon_url " +
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

        List<Software> list = new ArrayList<>();
        String q = keyword == null ? "" : keyword.trim().toLowerCase();
        String like = "%" + q + "%";
        String normalizedGenre = (genreName == null || genreName.trim().isEmpty()) ? null : genreName.trim();

        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {

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
                    sw.setIconUrl(rs.getString("icon_url"));
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
            String releaseNote
    ) throws Exception {

        String sql = "INSERT INTO Software_Detail(software_id, description, system_requirement, release_note) "
                + "VALUES (?, ?, ?, ?)";

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
            long fileSize
    ) throws SQLException {

        String sql = "INSERT INTO Software_Version(software_id, version_name, file_url, release_note, file_size) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, softwareId);
            ps.setString(2, versionName);
            ps.setString(3, fileUrl);
            ps.setString(4, releaseNote);
            ps.setLong(5, fileSize);

            ps.executeUpdate();
        }
    }

    public void changeStatusSoftware(int vendorId, int softwareId,String status) throws SQLException {

        String sql = "UPDATE Software\n"
                + "SET status = ?\n"
                + "WHERE software_id = ?\n"
                + "AND vendor_id = ?;";

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(3, vendorId);
            ps.setInt(2, softwareId);
            ps.setString(1, status);

            ps.executeUpdate();
        }
    }

    // update status trong pending reviews
    public void updateSoftwareStatus(int softwareId, String status) throws SQLException {
        String sql = "UPDATE Software SET status = ? WHERE software_id = ?";

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, softwareId);
            ps.executeUpdate();
        }
    }



}