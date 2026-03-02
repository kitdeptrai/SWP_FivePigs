package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.ReviewHistoryDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReviewHistoryDao {

    public List<ReviewHistoryDTO> getHistoryByReviewer(
        int reviewerId, int offset, int pageSize) {

    List<ReviewHistoryDTO> list = new ArrayList<>();

    String sql = """
        SELECT rs.review_score_id,
               s.software_id,
               s.name,
               s.version,
               COALESCE(si.image_url, 'assets/images/default.png') AS image_url,
               rs.ui_ux_score,
               rs.technical_score,
               rs.performance_score,
               rs.documentation_score,
               rs.total_score,
               rs.decision,
               rs.created_at
        FROM Review_Score rs
        JOIN Software s 
            ON rs.software_id = s.software_id
        LEFT JOIN Software_Image si 
            ON s.software_id = si.software_id 
            AND si.is_thumbnail = 1
        WHERE rs.reviewer_id = ?
        ORDER BY rs.created_at DESC
        LIMIT ? OFFSET ?
    """;

    try (Connection con = Db.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, reviewerId);
        ps.setInt(2, pageSize);
        ps.setInt(3, offset);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ReviewHistoryDTO dto = new ReviewHistoryDTO();
            dto.setReviewScoreId(rs.getInt("review_score_id"));
            dto.setSoftwareId(rs.getInt("software_id"));
            dto.setSoftwareName(rs.getString("name"));
            dto.setVersion(rs.getString("version"));
            dto.setImageUrl(rs.getString("image_url"));
            dto.setUiScore(rs.getInt("ui_ux_score"));
            dto.setTechnicalScore(rs.getInt("technical_score"));
            dto.setPerformanceScore(rs.getInt("performance_score"));
            dto.setDocumentationScore(rs.getInt("documentation_score"));
            dto.setTotalScore(rs.getDouble("total_score"));
            dto.setDecision(rs.getString("decision"));
            dto.setCreatedAt(rs.getTimestamp("created_at"));
            list.add(dto);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

    public int countHistory(int reviewerId) {

        String sql = """
            SELECT COUNT(*) 
            FROM Review_Score 
            WHERE reviewer_id = ?
        """;

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reviewerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}