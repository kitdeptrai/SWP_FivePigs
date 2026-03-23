package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.Report;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDao {

    public void reportFromLicense(int softwareId, int reporterId, String reason, String status) throws SQLException {
        String sql = "INSERT INTO fivepigs.report (software_id, reporter_id, reason, status, created_at) "
                + "VALUES (?, ?, ?, ?, NOW())";

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, softwareId);
            ps.setInt(2, reporterId);
            ps.setString(3, reason);
            ps.setString(4, status);
            ps.executeUpdate();
        }
    }

    public List<Report> getReportsForReviewer(int reviewerId) throws SQLException {
        String sql = """
            SELECT DISTINCT
                r.report_id,
                r.software_id,
                r.reporter_id,
                r.reviewer_id,
                r.reason,
                r.status,
                r.bug_confirmed,
                r.reviewer_note,
                r.reproduce_steps,
                r.created_at,
                r.processed_at,
                s.name AS software_name,
                u.full_name AS reporter_name,
                sv.file_url
            FROM Report r
            JOIN Software s ON s.software_id = r.software_id
            JOIN Users u ON u.user_id = r.reporter_id
            LEFT JOIN Software_Version sv
                ON sv.software_id = s.software_id AND sv.is_active = 1
            WHERE r.status = 'ERROR_REVIEW'
              AND EXISTS (
                  SELECT 1
                  FROM Review_Score rs
                  WHERE rs.software_id = r.software_id
                    AND rs.reviewer_id = ?
              )
            ORDER BY r.created_at DESC
        """;

        List<Report> list = new ArrayList<>();
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, reviewerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Report r = mapReport(rs);
                    list.add(r);
                }
            }
        }
        return list;
    }

    public Report getReportDetailForReviewer(int reportId, int reviewerId) throws SQLException {
        String sql = """
            SELECT
                r.report_id,
                r.software_id,
                r.reporter_id,
                r.reviewer_id,
                r.reason,
                r.status,
                r.bug_confirmed,
                r.reviewer_note,
                r.reproduce_steps,
                r.created_at,
                r.processed_at,
                s.name AS software_name,
                u.full_name AS reporter_name,
                sv.file_url
            FROM Report r
            JOIN Software s ON s.software_id = r.software_id
            JOIN Users u ON u.user_id = r.reporter_id
            LEFT JOIN Software_Version sv
                ON sv.software_id = s.software_id AND sv.is_active = 1
            WHERE r.report_id = ?
              AND EXISTS (
                  SELECT 1
                  FROM Review_Score rs
                  WHERE rs.software_id = r.software_id
                    AND rs.reviewer_id = ?
              )
            LIMIT 1
        """;

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, reportId);
            ps.setInt(2, reviewerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapReport(rs);
                }
            }
        }
        return null;
    }

    public void submitReviewerReportResult(
            int reportId,
            int reviewerId,
            boolean bugConfirmed,
            String reviewerNote,
            String reproduceSteps
    ) throws SQLException {
        String nextStatus = bugConfirmed ? "ERROR_APPROVAL" : "ERROR_REJECTED";

        String sql = """
            UPDATE Report
            SET reviewer_id = ?,
                bug_confirmed = ?,
                reviewer_note = ?,
                reproduce_steps = ?,
                status = ?,
                processed_at = NOW()
            WHERE report_id = ?
              AND status = 'ERROR_REVIEW'
        """;

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, reviewerId);
            ps.setBoolean(2, bugConfirmed);
            ps.setString(3, reviewerNote);
            ps.setString(4, reproduceSteps);
            ps.setString(5, nextStatus);
            ps.setInt(6, reportId);
            ps.executeUpdate();
        }
    }

    private Report mapReport(ResultSet rs) throws SQLException {
        Report r = new Report();
        r.setReportId(rs.getInt("report_id"));
        r.setSoftwareId(rs.getInt("software_id"));
        r.setReporterId(rs.getInt("reporter_id"));
        r.setReviewerId((Integer) rs.getObject("reviewer_id"));
        r.setReason(rs.getString("reason"));
        r.setStatus(rs.getString("status"));
        r.setBugConfirmed((Boolean) rs.getObject("bug_confirmed"));
        r.setReviewerNote(rs.getString("reviewer_note"));
        r.setReproduceSteps(rs.getString("reproduce_steps"));
        r.setSoftwareName(rs.getString("software_name"));
        r.setReporterName(rs.getString("reporter_name"));
        r.setFileUrl(rs.getString("file_url"));

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) {
            r.setCreatedAt(created.toLocalDateTime());
        }

        Timestamp processed = rs.getTimestamp("processed_at");
        if (processed != null) {
            r.setProcessedAt(processed.toLocalDateTime());
        }

        return r;
    }
}
