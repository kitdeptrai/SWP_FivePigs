/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.Category;
import com.fivepigs.app.model.Review;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.SoftwareDetail;
import com.fivepigs.app.model.SoftwareVersion;
import com.fivepigs.app.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MinhPD
 */
public class ReviewDao {

    public List<Review> getReviewListBySoftwareId(int softwareId) throws SQLException {
        List<Review> list=new ArrayList<>();
        String sql = "SELECT r.review_id,u.full_name,r.rating,r.comment,r.created_at\n"
                + "FROM Review r\n"
                + "JOIN Users u ON r.customer_id = u.user_id\n"
                + "WHERE r.software_id = ?\n"
                + "ORDER BY r.created_at DESC;";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review rv=new Review();
                    User user=new User();
                    rv.setReviewId(rs.getInt("review_id"));
                    user.setFullName(rs.getString("full_name"));
                    rv.setRating(rs.getInt("rating"));
                    rv.setComment(rs.getString("comment"));
                    rv.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                    rv.setUser(user);
                    list.add(rv);
                }
            }
        }
        return list;
    }
}
