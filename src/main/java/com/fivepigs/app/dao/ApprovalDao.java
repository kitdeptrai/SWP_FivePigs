/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.dao;

/**
 *
 * @author thanh
 */
import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.ApprovalProcess;
import com.fivepigs.app.model.Category;
import com.fivepigs.app.model.ReviewerProcess;
import com.fivepigs.app.model.SoftwareDetail;
import com.fivepigs.app.model.SoftwareImage;
import com.fivepigs.app.model.SoftwareVersion;
import com.fivepigs.app.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApprovalDao {

    /* SAVE APPROVAL DECISION */
    public void saveApproval(
            int softwareId,
            int approverId,
            String decision,
            String note
    ) throws SQLException {

        String sql = """
            INSERT INTO Software_Approval
                (software_id, approver_id, decision, approval_note)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, softwareId);
            ps.setInt(2, approverId);
            ps.setString(3, decision);
            ps.setString(4, note);

            ps.executeUpdate();
        }
    }

    /*update softwarestatus*/
    public void updateSoftwareStatus(int softwareId, String status)
            throws SQLException {

        String sql
                = "UPDATE Software SET status = ? WHERE software_id = ?";

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, softwareId);

            ps.executeUpdate();
        }
    }

    public Integer countStatusApps(String status) throws SQLException {
        String sql = "SELECT COUNT(*) AS COUNT FROM software WHERE status = ?";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("COUNT");
            }

        }
        return null;
    }

    public List<Software> getApprovalApp(int approval_id) throws SQLException {
        List<Software> list = new ArrayList<>();

        String sql = """
                SELECT
                    s.name,
                    sa.decision AS status,
                    sa.approval_date,
                    sa.approval_note
                FROM Software s
                JOIN Software_Approval sa
                    ON s.software_id = sa.software_id
                WHERE sa.decision = 'APPROVED' OR sa.decision = 'REJECTED'
                ORDER BY sa.approval_date DESC;
    """;

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

//            ps.setInt(1, approval_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Software s = new Software();
                ApprovalProcess ap = new ApprovalProcess();
                ReviewerProcess rp = new ReviewerProcess();
                s.setAppName(rs.getString("name"));
                s.setStatus(rs.getString("status"));
                ap.setApproval_date(rs.getTimestamp("approval_date").toLocalDateTime());
                rp.setRecommendation(rs.getString("approval_note"));
                s.setApprovalProcess(ap);
                s.setReviewerProcess(rp);
                list.add(s);
            }

        }
        return list;
    }

    public List<Software> getPendingApp(int approval_id) throws SQLException {
        List<Software> list = new ArrayList<>();

        String sql = """
                SELECT
                    s.name,
                    s.status,
                    sa.approval_date,
                    sa.approval_note
                FROM Software s
                LEFT JOIN Software_Approval sa
                    ON s.software_id = sa.software_id
                WHERE s.status = 'PENDING_APPROVAL'
                ORDER BY sa.approval_date DESC;
    """;

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Software s = new Software();
                ApprovalProcess ap = new ApprovalProcess();
                ReviewerProcess rp = new ReviewerProcess();
                s.setAppName(rs.getString("name"));
                s.setStatus(rs.getString("status"));
                if (rs.getTimestamp("approval_date") != null) {
                    ap.setApproval_date(rs.getTimestamp("approval_date").toLocalDateTime());
                }

                ap.setApproval_note(rs.getString("approval_note"));
                s.setApprovalProcess(ap);
                list.add(s);
            }

        }
        return list;
    }

    public Map<String, Integer> getDashboardCounts() throws SQLException {

        String sql = """
        SELECT
          (SELECT COUNT(*) FROM Software WHERE status = 'PENDING_APPROVAL') AS pending_count,
          (SELECT COUNT(*) FROM Software_Approval WHERE decision = 'APPROVED') AS approved_count,
          (SELECT COUNT(*) FROM Software_Approval WHERE decision = 'REJECTED') AS rejected_count
    """;

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            Map<String, Integer> map = new HashMap<>();

            if (rs.next()) {
                map.put("pending", rs.getInt("pending_count"));
                map.put("approved", rs.getInt("approved_count"));
                map.put("rejected", rs.getInt("rejected_count"));
            }

            return map;
        }
    }

    public List<Software> getPendingApprovals() throws SQLException {
        String sql = """
            SELECT
            s.software_id,
            s.name AS app_name,
            s.short_description,
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
          LEFT JOIN (
            SELECT software_id, MAX(reviewed_at) AS max_reviewed_at
            FROM Software_Review_Process
            WHERE reviewed_at IS NOT NULL
            GROUP BY software_id
          ) last_rp ON last_rp.software_id = s.software_id
          LEFT JOIN Software_Review_Process rp
            ON rp.software_id = s.software_id
           AND rp.reviewed_at = last_rp.max_reviewed_at
          LEFT JOIN Users u ON u.user_id = rp.reviewer_id
          LEFT JOIN Category c ON c.category_id = s.category_id
          WHERE s.status = 'PENDING_APPROVAL'
          ORDER BY rp.reviewed_at DESC, s.created_at DESC;        
    """;

        List<Software> list = new ArrayList<>();

        try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Software s = new Software();
                User u = new User();
                Category c = new Category();
                SoftwareImage si = new SoftwareImage();
                ReviewerProcess rp = new ReviewerProcess();

                s.setSoftwareId(rs.getInt("software_id"));
                s.setAppName(rs.getString("app_name"));
                s.setShortDescription(rs.getString("short_description"));

                c.setCategoryName(rs.getString("category_name"));
                u.setFullName(rs.getString("reviewer_name"));

                si.setImageUrl(rs.getString("thumbnail_url"));
                s.setSoftwareImage(si);

                Timestamp ts = rs.getTimestamp("reviewed_at");
                rp.setReviewed_at(ts != null ? ts.toLocalDateTime() : null);

                s.setReviewerProcess(rp);
                s.setUser(u);
                s.setCategory(c);

                list.add(s);
            }
        }

        return list;
    }

    public List<Software> getApprovalHistory() throws SQLException {
        List<Software> approvalHistoryList = new ArrayList<>();

        String sql = """
        SELECT
            s.software_id,
            s.name AS app_name,
            u.full_name AS vendor_name,
            sa.approval_date AS decision_date,
            sa.decision AS decision
        FROM Software s
        JOIN Users u ON u.user_id = s.vendor_id
        JOIN Software_Approval sa ON sa.software_id = s.software_id
        WHERE sa.decision IS NOT NULL
        ORDER BY sa.approval_date DESC
    """;

        try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Software s = new Software();
                User u = new User();
                ApprovalProcess ap = new ApprovalProcess();

                // set app name
                s.setAppName(rs.getString("app_name"));
                s.setSoftwareId(rs.getInt("software_id"));
                // set vendor name
                u.setFullName(rs.getString("vendor_name"));
                s.setUser(u);

                // set decision + date
                Timestamp ts = rs.getTimestamp("decision_date");
                ap.setApproval_date(ts != null ? ts.toLocalDateTime() : null);
                ap.setDecision(rs.getString("decision"));
                s.setApprovalProcess(ap);

                approvalHistoryList.add(s);
            }
        }

        return approvalHistoryList;
    }

    public Software getPendingDetail(int softwareId) throws SQLException {
        String sql = """
                        SELECT
                         s.software_id,
                         s.name,
                         c.category_name,
                         u.full_name,
                         sd.description,
                         sv.version_name
                     FROM Software s
                     JOIN Users u 
                         ON u.user_id = s.vendor_id
                     LEFT JOIN Category c
                         ON c.category_id = s.category_id
                     LEFT JOIN Software_Detail sd 
                         ON sd.software_id = s.software_id
                     LEFT JOIN Software_Version sv 
                         ON sv.software_id = s.software_id
                         AND sv.is_active = 1
                     WHERE s.software_id = ?;
                     """;

        try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Software s = new Software();
                    SoftwareDetail sd = new SoftwareDetail();
                    SoftwareVersion sv = new SoftwareVersion();
                    Category c = new Category();
                    User u = new User();

                    s.setAppName(rs.getString("name"));
                    c.setCategoryName(rs.getString("category_name"));
                    u.setFullName(rs.getString("full_name"));
                    sd.setDescription(rs.getString("description"));
                    sv.setVersionName(rs.getString("version_name"));

                    s.setUser(u);
                    s.setCategory(c);
                    s.setSoftwareDetail(sd);
                    s.setSoftwareVersion(sv);

                    return s;
                }

            }

        }
        return null;
    }

    public void submitDecision(int softwareId, int approverId, String decision, String note) throws SQLException {

        Connection c = Db.getConnection();
        try {
            c.setAutoCommit(false);

            // 1️⃣ Insert hoặc Update Software_Approval
            String approvalSql = """
            INSERT INTO Software_Approval (software_id, approver_id, decision, approval_note)
            VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
            decision = VALUES(decision),
            approval_note = VALUES(approval_note),
            approval_date = CURRENT_TIMESTAMP
        """;

            try (PreparedStatement ps = c.prepareStatement(approvalSql)) {
                ps.setInt(1, softwareId);
                ps.setInt(2, approverId);
                ps.setString(3, decision);
                ps.setString(4, note);
                ps.executeUpdate();
            }

            // 2️⃣ Update Software.status
            String statusSql = """
            UPDATE Software
            SET status = ?
            WHERE software_id = ?
        """;

            try (PreparedStatement ps = c.prepareStatement(statusSql)) {
                ps.setString(1, decision); // APPROVED hoặc REJECTED
                ps.setInt(2, softwareId);
                ps.executeUpdate();
            }

            c.commit();

        } catch (Exception e) {
            c.rollback();
            throw e;
        } finally {
            c.close();
        }
    }

}
