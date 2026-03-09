/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.ReviewScore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewScoreDao {

    public boolean saveReviewScore(ReviewScore rs) {

        String sql = "INSERT INTO Review_Score "
                + "(software_id, reviewer_id, "
                + "no_malware, no_copyright_violation, no_spam_content, "
                + "ui_ux_score, technical_score, performance_score, documentation_score, "
                + "total_score, decision, review_note) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rs.getSoftwareId());
            ps.setInt(2, rs.getReviewerId());

            ps.setBoolean(3, rs.isNoMalware());
            ps.setBoolean(4, rs.isNoCopyrightViolation());
            ps.setBoolean(5, rs.isNoSpamContent());

            ps.setInt(6, rs.getUiUxScore());
            ps.setInt(7, rs.getTechnicalScore());
            ps.setInt(8, rs.getPerformanceScore());
            ps.setInt(9, rs.getDocumentationScore());

            ps.setDouble(10, rs.getTotalScore());
            ps.setString(11, rs.getDecision());
            ps.setString(12, rs.getReviewNote());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertReviewScore(ReviewScore r) throws SQLException {
        String sql = """
        INSERT INTO Review_Score (
            software_id,
            reviewer_id,
            no_malware,
            no_copyright_violation,
            no_spam_content,
            ui_ux_score,
            technical_score,
            performance_score,
            documentation_score,
            total_score,
            decision,
            review_note
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, r.getSoftwareId());
            ps.setInt(2, r.getReviewerId());
            ps.setInt(3, r.isNoMalware() ? 1 : 0);
            ps.setInt(4, r.isNoCopyrightViolation() ? 1 : 0);
            ps.setInt(5, r.isNoSpamContent() ? 1 : 0);
            ps.setInt(6, r.getUiUxScore());
            ps.setInt(7, r.getTechnicalScore());
            ps.setInt(8, r.getPerformanceScore());
            ps.setInt(9, r.getDocumentationScore());
            ps.setDouble(10, r.getTotalScore());
            ps.setString(11, r.getDecision());
            ps.setString(12, r.getReviewNote());

            ps.executeUpdate();
        }
    }

    public ReviewScore getReviewDetailById(int reviewScoreId) throws Exception {
        String sql = """
        SELECT rs.review_score_id,
               rs.software_id,
               rs.reviewer_id,
               rs.no_malware,
               rs.no_copyright_violation,
               rs.no_spam_content,
               rs.ui_ux_score,
               rs.technical_score,
               rs.performance_score,
               rs.documentation_score,
               rs.total_score,
               rs.decision,
               rs.review_note,
               rs.created_at,
               s.name AS software_name,
               s.short_description,
               s.price,
               c.category_name,
               sv.version_name AS version,
               si.image_url
        FROM Review_Score rs
        JOIN Software s ON rs.software_id = s.software_id
        LEFT JOIN Category c ON s.category_id = c.category_id
        LEFT JOIN Software_Version sv
               ON sv.software_id = s.software_id AND sv.is_active = 1
        LEFT JOIN Software_Image si
               ON si.software_id = s.software_id AND si.is_thumbnail = 1
        WHERE rs.review_score_id = ?
        LIMIT 1
    """;

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reviewScoreId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ReviewScore r = new ReviewScore();

                    r.setReviewScoreId(rs.getInt("review_score_id"));
                    r.setSoftwareId(rs.getInt("software_id"));
                    r.setReviewerId(rs.getInt("reviewer_id"));
                    r.setNoMalware(rs.getInt("no_malware") == 1);
                    r.setNoCopyrightViolation(rs.getInt("no_copyright_violation") == 1);
                    r.setNoSpamContent(rs.getInt("no_spam_content") == 1);
                    r.setUiUxScore(rs.getInt("ui_ux_score"));
                    r.setTechnicalScore(rs.getInt("technical_score"));
                    r.setPerformanceScore(rs.getInt("performance_score"));
                    r.setDocumentationScore(rs.getInt("documentation_score"));
                    r.setTotalScore(rs.getDouble("total_score"));
                    r.setDecision(rs.getString("decision"));
                    r.setReviewNote(rs.getString("review_note"));
                    r.setSoftwareName(rs.getString("software_name"));
                    r.setShortDescription(rs.getString("short_description"));
                    r.setPrice(rs.getDouble("price"));
                    r.setCategoryName(rs.getString("category_name"));
                    r.setVersion(rs.getString("version"));
                    r.setImageUrl(rs.getString("image_url"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));

                    return r;
                }
            }
        }

        return null;
    }

    public int countReviewsByReviewer(int reviewerId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Review_Score WHERE reviewer_id = ?";

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reviewerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }
}
