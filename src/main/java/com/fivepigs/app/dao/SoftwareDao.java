/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class SoftwareDao {
    public Integer pendindReviewApp() throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM Software WHERE status = 'PENDING_REVIEW';";
        
          try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        }
        return null;
    }
    
    // Đếm số app đã được review (completed)
public Integer completeReviewApp() throws SQLException {
    String sql = """
        SELECT COUNT(DISTINCT software_id) AS count
        FROM Software_Review_Process
    """;

    try (Connection c = Db.getConnection();
         PreparedStatement ps = c.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            return rs.getInt("count");
        }
    }
    return 0;
}

// Đếm số app được review hôm nay
public Integer reviewedToday() throws SQLException {
    String sql = """
        SELECT COUNT(DISTINCT software_id) AS count
        FROM Software_Review_Process
        WHERE DATE(reviewed_at) = CURDATE()
    """;

    try (Connection c = Db.getConnection();
         PreparedStatement ps = c.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            return rs.getInt("count");
        }
    }
    return 0;
}

 
}
