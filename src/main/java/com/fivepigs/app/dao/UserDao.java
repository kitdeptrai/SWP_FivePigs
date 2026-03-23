package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UserDao {

    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT 1 FROM Users WHERE email = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void insertUser(User user) throws SQLException {
        String sql = "INSERT INTO Users(full_name, email, password, role_id, status) VALUES(?,?,?,?,?)";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getRoleId());
            ps.setString(5, user.getStatus() != null ? user.getStatus() : "ACTIVE");
            ps.executeUpdate();
        }
    }

    public Integer getRoleIdByName(String roleName) throws SQLException {
        String sql = "SELECT role_id FROM Role WHERE role_name = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, roleName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("role_id");
                }
            }
        }
        return null;
    }

    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT u.user_id, u.full_name, u.email, u.password, u.role_id, u.status, u.created_at, r.role_name " +
                "FROM Users u " +
                "INNER JOIN Role r ON u.role_id = r.role_id " +
                "WHERE u.email = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setRoleId(rs.getInt("role_id"));
                    user.setStatus(rs.getString("status"));
                    if (rs.getTimestamp("created_at") != null) {
                        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    }
                    return user;
                }
            }
        }
        return null;
    }

    public String getRoleNameById(Integer roleId) throws SQLException {
        String sql = "SELECT role_name FROM Role WHERE role_id = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role_name");
                }
            }
        }
        return null;
    }

    public void updatePassword(String email, String newPassword) throws SQLException {
        String sql = "UPDATE Users SET password = ? WHERE email = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, email);
            ps.executeUpdate();
        }
    }

    public void updateRoleByUserId(int userId, int roleId) throws SQLException {
        String sql = "UPDATE Users SET role_id = ? WHERE user_id = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, roleId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    public boolean updateProfile(Integer userId,
                                 String fullName,
                                 String phone,
                                 String avatar,
                                 LocalDate dateOfBirth,
                                 String gender,
                                 String address,
                                 String bio) throws SQLException {
        String sql = "UPDATE Users " +
                "SET full_name = ?, phone = ?, avatar = ?, date_of_birth = ?, gender = ?, address = ?, bio = ? " +
                "WHERE user_id = ?";

        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setString(3, avatar);
            if (dateOfBirth != null) {
                ps.setDate(4, java.sql.Date.valueOf(dateOfBirth));
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }
            ps.setString(5, gender);
            ps.setString(6, address);
            ps.setString(7, bio);
            ps.setInt(8, userId);
            return ps.executeUpdate() > 0;
        }
    }
}
