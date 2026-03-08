package com.fivepigs.app.dao;
import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.Software;
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
                s.setLanguage(rs.getString("language"));
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
                list.add(s);
            }
        }
    }
    return list;
}


    // Top3RevenueByVendor: OK (không dùng cột bị xóa)

    
    public List<Software> Top3RevenueByVendor(Integer vendorId) throws SQLException {
        List<Software> list = new ArrayList<>();
        String sql = "SELECT s.name AS app_name,SUM(od.price) AS revenue,s.avg_rating  AS rating,s.status,download_count,vendor_id,s.software_id FROM Software s\n"
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
                + "AND status LIKE ?;";
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
        String sql = "SELECT s.name,sv.version_name AS version,c.category_name,s.created_at,s.price,sd.description,sd.system_requirement,si.image_url AS thumbnail\n"
                + "                FROM Software s\n"
                + "                LEFT JOIN Software_Version sv ON s.software_id = sv.software_id AND sv.is_active = 1\n"
                + "                LEFT JOIN Software_Detail sd ON s.software_id = sd.software_id\n"
                + "                LEFT JOIN Category c ON s.category_id = c.category_id\n"
                + "                LEFT JOIN Software_Image si ON s.software_id = si.software_id AND si.is_thumbnail = 1\n"
                + "                WHERE s.software_id = ?;";

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

    public void addSoftwareImage(int softwareId, String imageUrl, boolean isThumbnail) throws Exception {

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
    ) throws Exception {

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
}
