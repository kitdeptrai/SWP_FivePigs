package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FeedbackDao {

    public void insertFeedback(int userId, String subject, String message) throws SQLException {
        String sql = "INSERT INTO fivepigs.user_feedback(user_id, subject, message, status, created_at) VALUES(?, ?, ?, 'NEW', NOW())";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, subject);
            ps.setString(3, message);
            ps.executeUpdate();
        }
    }
}
