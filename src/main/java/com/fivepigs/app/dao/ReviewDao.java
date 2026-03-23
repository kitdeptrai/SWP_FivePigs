/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.Review;
import com.fivepigs.app.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author MinhPD
 */
public class ReviewDao {

    public List<Review> getReviewListBySoftwareId(int softwareId) throws SQLException {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT r.review_id,u.full_name,r.rating,r.comment,r.created_at\n"
                + "FROM Review r\n"
                + "JOIN Users u ON r.customer_id = u.user_id\n"
                + "WHERE r.software_id = ?\n"
                + "ORDER BY r.created_at DESC;";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review rv = new Review();
                    User user = new User();
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

    public boolean hasOwnedLicense(int userId, int softwareId) throws SQLException {
        String sql = "SELECT 1 " +
                "FROM fivepigs.license_user lu " +
                "JOIN fivepigs.license l ON lu.license_id = l.license_id " +
                "WHERE lu.user_id = ? AND l.software_id = ? " +
                "AND (lu.status IS NULL OR UPPER(lu.status) = 'ACTIVE') " +
                "AND (l.status IS NULL OR UPPER(l.status) <> 'REVOKED') " +
                "AND (l.expire_date IS NULL OR l.expire_date >= NOW()) " +
                "LIMIT 1";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean hasUserReviewed(int userId, int softwareId) throws SQLException {
        String sql = "SELECT 1 FROM fivepigs.review WHERE customer_id = ? AND software_id = ? LIMIT 1";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void addReview(int userId, int softwareId, int rating, String comment) throws SQLException {
        String sql = "INSERT INTO fivepigs.review(software_id, customer_id, rating, comment, created_at) VALUES (?, ?, ?, ?, NOW())";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, softwareId);
            ps.setInt(2, userId);
            ps.setInt(3, rating);
            ps.setString(4, comment);
            ps.executeUpdate();
        }
        refreshSoftwareRating(softwareId);
    }

    public void refreshSoftwareRating(int softwareId) throws SQLException {
        String sql = "UPDATE fivepigs.software s SET s.avg_rating = (SELECT ROUND(AVG(r.rating), 1) FROM fivepigs.review r WHERE r.software_id = s.software_id) WHERE s.software_id = ?";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, softwareId);
            ps.executeUpdate();
        }
    }

    public Map<Integer, Integer> getRatingBreakdown(int softwareId) throws SQLException {
        Map<Integer, Integer> breakdown = new LinkedHashMap<>();
        for (int star = 5; star >= 1; star--) {
            breakdown.put(star, 0);
        }

        String sql = "SELECT rating, COUNT(*) AS total FROM fivepigs.review WHERE software_id = ? GROUP BY rating";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    breakdown.put(rs.getInt("rating"), rs.getInt("total"));
                }
            }
        }
        return breakdown;
    }
}
