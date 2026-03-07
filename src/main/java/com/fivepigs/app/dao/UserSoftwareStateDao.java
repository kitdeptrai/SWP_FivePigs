package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserSoftwareStateDao {

    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS user_software_state (" +
                    "user_id INT NOT NULL," +
                    "software_id INT NOT NULL," +
                    "downloaded TINYINT(1) NOT NULL DEFAULT 0," +
                    "downloaded_at DATETIME NULL," +
                    "PRIMARY KEY (user_id, software_id)" +
                    ")";

    public Map<Integer, Boolean> getDownloadedMapByUser(int userId) throws SQLException {
        ensureTable();

        String sql = "SELECT software_id, downloaded FROM user_software_state WHERE user_id = ?";
        Map<Integer, Boolean> map = new HashMap<>();

        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, userId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getInt("software_id"), rs.getInt("downloaded") == 1);
                }
            }
        }

        return map;
    }

    public void markDownloaded(int userId, int softwareId) throws SQLException {
        ensureTable();

        String sql = "INSERT INTO user_software_state(user_id, software_id, downloaded, downloaded_at) " +
                "VALUES(?, ?, 1, NOW()) " +
                "ON DUPLICATE KEY UPDATE downloaded = 1, downloaded_at = NOW()";

        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, userId);
            st.setInt(2, softwareId);
            st.executeUpdate();
        }
    }

    private void ensureTable() throws SQLException {
        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(CREATE_TABLE_SQL)) {
            st.execute();
        }
    }
}
