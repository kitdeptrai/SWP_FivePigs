package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.SoftwareDemoVersion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class SoftwareDemoDao {

    public SoftwareDemoVersion getActiveDemoBySoftwareId(int softwareId) throws SQLException {
        String sql = """
                SELECT demo_version_id,
                       software_id,
                       version_name,
                       demo_file_url,
                       release_note,
                       file_size,
                       created_at,
                       is_active
                FROM fivepigs.software_demo_version
                WHERE software_id = ? AND is_active = 1
                ORDER BY created_at DESC, demo_version_id DESC
                LIMIT 1
                """;

        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                SoftwareDemoVersion demo = new SoftwareDemoVersion();
                demo.setDemoVersionId(rs.getInt("demo_version_id"));
                demo.setSoftwareId(rs.getInt("software_id"));
                demo.setVersionName(rs.getString("version_name"));
                demo.setDemoFileUrl(rs.getString("demo_file_url"));
                demo.setReleaseNote(rs.getString("release_note"));

                Number fileSizeValue = (Number) rs.getObject("file_size");
                demo.setFileSize(fileSizeValue == null ? null : fileSizeValue.intValue());
                demo.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));

                Object isActiveValue = rs.getObject("is_active");
                if (isActiveValue instanceof Boolean booleanValue) {
                    demo.setIsActive(booleanValue ? 1 : 0);
                } else if (isActiveValue instanceof Number numberValue) {
                    demo.setIsActive(numberValue.intValue());
                } else {
                    demo.setIsActive(null);
                }
                return demo;
            }
        }
    }

    public boolean hasActiveDemo(int softwareId) throws SQLException {
        return getActiveDemoBySoftwareId(softwareId) != null;
    }
}
