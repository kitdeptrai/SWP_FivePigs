package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.Category;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.SoftwareDetail;
import com.fivepigs.app.model.SoftwareImage;
import com.fivepigs.app.model.SoftwareVersion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public Integer completeReviewApp() throws SQLException {
        String sql = "SELECT COUNT(DISTINCT software_id) AS count FROM Software_Review_Process";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
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
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
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

    public void updateSoftwareStatus(int softwareId, String status) throws SQLException {
        String sql = "UPDATE Software SET status = ? WHERE software_id = ?";

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, softwareId);
            ps.executeUpdate();
        }
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

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ps.setString(4, kw);
            ps.setString(5, kw);
            ps.setString(6, kw);

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
                    s.setCategoryName(rs.getString("category_name"));
                    s.setImageUrl(rs.getString("image_url"));
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
                    list.add(sw);
                }
            }
        }
        return list;
    }

    public Integer totalProductsByVendor(Integer vendorId) throws SQLException {
        String sql = "SELECT COUNT(*) AS total_apps FROM Software WHERE vendor_id = ?";
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
        String sql = "SELECT SUM(download_count) AS total_download_count FROM Software WHERE vendor_id = ?";
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
        String sql = "SELECT COUNT(*) AS total_apps FROM Software WHERE vendor_id = ? AND status LIKE ?";
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
        String sql = """
            SELECT SUM(od.price) AS total_revenue
            FROM Order_Detail od
            JOIN Orders o ON od.order_id = o.order_id
            JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id
            JOIN Software s ON od.software_id = s.software_id
            WHERE ps.status_name = 'PAID'
              AND s.vendor_id = ?
        """;
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
        String sql = """
            SELECT FLOOR(DATEDIFF(CURDATE(), l.purchase_date) / 7) AS week_index,
                   COUNT(*) AS downloads
            FROM License l
            JOIN Software s ON l.software_id = s.software_id
            WHERE s.vendor_id = ?
              AND l.purchase_date >= DATE_SUB(CURDATE(), INTERVAL 28 DAY)
            GROUP BY week_index
            HAVING week_index BETWEEN 0 AND 3
        """;

        Map<Integer, Double> downloadMap = new LinkedHashMap<>();
        for (int i = 0; i < 4; i++) {
            downloadMap.put(i, 0.0);
        }

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, vendorId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int weekIndex = rs.getInt("week_index");
                    double downloads = rs.getDouble("downloads");
                    downloadMap.put(weekIndex, downloadMap.get(weekIndex) + downloads);
                }
            }
        }
        return downloadMap;
    }

    public Double avgRatingByVendorId(int vendorId) throws SQLException {
        String sql = """
            SELECT s.vendor_id, ROUND(AVG(r.rating), 2) AS avg_rating
            FROM Software s
            JOIN Review r ON s.software_id = r.software_id
            WHERE s.vendor_id = ?
            GROUP BY s.vendor_id
        """;
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
        String sql = """
            SELECT s.software_id,
                   s.name,
                   s.short_description,
                   s.status,
                   s.download_count,
                   s.avg_rating,
                   COALESCE(SUM(
                        CASE
                            WHEN ps.status_name = 'PAID' THEN od.price
                            ELSE 0
                        END
                   ),0) AS revenue,
                   si.image_url AS thumbnail
            FROM Software s
            LEFT JOIN Order_Detail od ON s.software_id = od.software_id
            LEFT JOIN Orders o ON od.order_id = o.order_id
            LEFT JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id
            LEFT JOIN Software_Image si ON s.software_id = si.software_id AND si.is_thumbnail = 1
            WHERE s.vendor_id = ?
            GROUP BY s.software_id, s.name, s.short_description, s.status, s.download_count, s.avg_rating, si.image_url
        """;
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
        String sql = "SELECT download_count FROM Software WHERE software_id = ?";
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
        String sql = "SELECT avg_rating FROM Software WHERE software_id = ?";
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
        String sql = "SELECT COUNT(license_id) AS total_license FROM License WHERE software_id = ?";
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
        String sql = """
            SELECT SUM(od.price) AS total_revenue
            FROM Order_Detail od
            JOIN Orders o ON od.order_id = o.order_id
            JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id
            WHERE ps.status_name = 'PAID'
              AND od.software_id = ?
        """;
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
            SELECT s.name,
                   sv.version_name AS version,
                   c.category_name,
                   s.created_at,
                   COALESCE(p.base_price, 0) AS price,
                   sd.description,
                   sd.system_requirement,
                   si.image_url AS thumbnail
            FROM Software s
            LEFT JOIN Software_Version sv
                   ON s.software_id = sv.software_id
                  AND sv.is_active = 1
            LEFT JOIN Software_Detail sd
                   ON s.software_id = sd.software_id
            LEFT JOIN Category c
                   ON s.category_id = c.category_id
            LEFT JOIN Software_Image si
                   ON s.software_id = si.software_id
                  AND si.is_thumbnail = 1
            LEFT JOIN (
                SELECT software_id, MIN(price) AS base_price
                FROM Software_Pricing
                WHERE is_active = 1
                GROUP BY software_id
            ) p
                   ON p.software_id = s.software_id
            WHERE s.software_id = ?
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

                    sw.setName(rs.getString("name"));
                    swVersion.setVersionName(rs.getString("version"));
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

    public int countPendingReviewSoftware() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Software WHERE status = 'PENDING_REVIEW'";
        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Upload Software: KHÔNG insert price vào Software nữa
    public int createSoftware(Software software) throws Exception {
        String sql = """
            INSERT INTO Software(name, short_description, vendor_id, category_id, is_free, status, download_count, avg_rating, created_at)
            VALUES (?, ?, ?, ?, ?, ?, 0, 0, NOW())
        """;

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, software.getName());
            ps.setString(2, software.getShortDescription());
            ps.setInt(3, software.getVendorId());
            ps.setInt(4, software.getCategoryId());
            ps.setBoolean(5, software.isFree());
            ps.setString(6, software.getStatus() != null ? software.getStatus() : "PENDING_REVIEW");

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new RuntimeException("Cannot create software");
    }

    public void createSoftwareDetail(int softwareId, String description, String systemRequire, String releaseNote) throws Exception {
        String sql = """
            INSERT INTO Software_Detail(software_id, description, system_requirement, release_note)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, softwareId);
            ps.setString(2, description);
            ps.setString(3, systemRequire);
            ps.setString(4, releaseNote);

            ps.executeUpdate();
        }
    }

    public void addSoftwareImage(int softwareId, String imageUrl, boolean isThumbnail) throws Exception {
        String sql = "INSERT INTO Software_Image(software_id, image_url, is_thumbnail) VALUES (?, ?, ?)";

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, softwareId);
            ps.setString(2, imageUrl);
            ps.setBoolean(3, isThumbnail);

            ps.executeUpdate();
        }
    }

    public void addSoftwareVersion(int softwareId, String versionName, String fileUrl, String releaseNote, long fileSize) throws Exception {
        String sql = """
            INSERT INTO Software_Version(software_id, version_name, file_url, release_note, file_size)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, softwareId);
            ps.setString(2, versionName);
            ps.setString(3, fileUrl);
            ps.setString(4, releaseNote);
            ps.setLong(5, fileSize);

            ps.executeUpdate();
        }
    }

    // MỚI: thêm pricing cho software
    public void addSoftwarePricing(int softwareId, String planName, int maxUsers, double price, boolean isActive) throws Exception {
        String sql = """
            INSERT INTO Software_Pricing(software_id, plan_name, max_users, price, is_active)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, softwareId);
            ps.setString(2, planName);
            ps.setInt(3, maxUsers);
            ps.setDouble(4, price);
            ps.setBoolean(5, isActive);

            ps.executeUpdate();
        }
    }
}
