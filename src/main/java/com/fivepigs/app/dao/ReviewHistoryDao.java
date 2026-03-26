package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.ReviewHistoryDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReviewHistoryDao {

    public List<ReviewHistoryDTO> getFilteredHistoryByReviewer(
            int reviewerId,
            String keyword,
            String decision,
            String fromDate,
            String toDate,
            int offset,
            int pageSize) {

        List<ReviewHistoryDTO> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT rs.review_score_id,
                   s.software_id,
                   s.name,
                   sv.version_name AS version,
                   COALESCE(si.image_url, 'assets/images/default.png') AS image_url,
                   rs.ui_ux_score,
                   rs.technical_score,
                   rs.performance_score,
                   rs.documentation_score,
                   rs.total_score,
                   rs.decision,
                   rs.created_at
            FROM Review_Score rs
            JOIN Software s ON rs.software_id = s.software_id
            LEFT JOIN Software_Image si
                   ON s.software_id = si.software_id AND si.is_thumbnail = 1
            LEFT JOIN Software_Version sv
                   ON sv.software_id = s.software_id AND sv.is_active = 1
            WHERE rs.reviewer_id = ?
        """);

        List<Object> params = new ArrayList<>();
        params.add(reviewerId);

        appendFilters(sql, params, keyword, decision, fromDate, toDate);

        sql.append(" ORDER BY rs.created_at DESC LIMIT ? OFFSET ? ");
        params.add(pageSize);
        params.add(offset);

        try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ReviewHistoryDTO dto = mapRow(rs);
                list.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countFilteredHistory(
            int reviewerId,
            String keyword,
            String decision,
            String fromDate,
            String toDate) {

        StringBuilder sql = new StringBuilder("""
            SELECT COUNT(*)
            FROM Review_Score rs
            JOIN Software s ON rs.software_id = s.software_id
            LEFT JOIN Software_Version sv
                   ON sv.software_id = s.software_id AND sv.is_active = 1
            WHERE rs.reviewer_id = ?
        """);

        List<Object> params = new ArrayList<>();
        params.add(reviewerId);

        appendFilters(sql, params, keyword, decision, fromDate, toDate);

        try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private void appendFilters(StringBuilder sql, List<Object> params,
            String keyword, String decision, String fromDate, String toDate) {

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("""
                AND (
                    s.name LIKE ?
                    OR sv.version_name LIKE ?
                    OR CAST(rs.total_score AS CHAR) LIKE ?
                    OR rs.decision LIKE ?
                )
            """);
            String kw = "%" + keyword.trim() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }

        if (decision != null && !decision.trim().isEmpty() && !"all".equalsIgnoreCase(decision)) {
            sql.append(" AND rs.decision = ? ");
            params.add(decision.trim());
        }

        if (fromDate != null && !fromDate.trim().isEmpty()) {
            sql.append(" AND DATE(rs.created_at) >= ? ");
            params.add(fromDate.trim());
        }

        if (toDate != null && !toDate.trim().isEmpty()) {
            sql.append(" AND DATE(rs.created_at) <= ? ");
            params.add(toDate.trim());
        }
    }

    private ReviewHistoryDTO mapRow(ResultSet rs) throws Exception {
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
        return dto;
    }
}
