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

public class ReportDao {
    public boolean hasActiveAccess(int userId, int softwareId) throws SQLException {
        String sql = "SELECT 1 " +
                "FROM fivepigs.license_user lu " +
                "JOIN fivepigs.license l ON lu.license_id = l.license_id " +
                "WHERE lu.user_id = ? AND l.software_id = ? " +
                "AND (lu.status IS NULL OR UPPER(lu.status) = 'ACTIVE') " +
                "AND (l.status IS NULL OR UPPER(l.status) <> 'REVOKED') " +
                "AND (l.expire_date IS NULL OR l.expire_date >= NOW()) " +
                "LIMIT 1";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void reportFromLicense(int softwareId, int reporterId, String reason, String status) throws SQLException {
        String sql = "INSERT INTO fivepigs.report (software_id, reporter_id, reason, status, created_at) " +
                "VALUES (?, ?, ?, ?, NOW())";

        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, softwareId);
            ps.setInt(2, reporterId);
            ps.setString(3, reason);
            ps.setString(4, status);
            ps.executeUpdate();
        }
    }

}
