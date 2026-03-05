/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.ReviewScore;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ReviewScoreDao {

    public boolean saveReviewScore(ReviewScore rs) {

        String sql = "INSERT INTO Review_Score "
                + "(software_id, reviewer_id, "
                + "no_malware, no_copyright_violation, no_spam_content, "
                + "ui_ux_score, technical_score, performance_score, documentation_score, "
                + "total_score, decision, review_note) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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
}