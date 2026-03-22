package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NotificationDao {

    // 1. Lấy tất cả notification của user
    public List<Notification> getByUser(int userId) {
        List<Notification> list = new ArrayList<>();

        String sql = "SELECT notification_id, user_id, title, content, is_read, type, priority, related_url, created_at "
                + "FROM Notification "
                + "WHERE user_id = ? "
                + "ORDER BY created_at DESC";

        try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notification n = mapNotification(rs);
                    list.add(n);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 2. Lấy 1 notification theo id
    public Notification getById(int notificationId) {
        String sql = "SELECT notification_id, user_id, title, content, is_read, type, priority, related_url, created_at "
                + "FROM Notification "
                + "WHERE notification_id = ?";

        try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, notificationId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapNotification(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // 3. Toggle read/unread
    public void toggleRead(int notificationId) {
        String sql = "UPDATE Notification SET is_read = NOT is_read WHERE notification_id = ?";

        try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, notificationId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 4. Mark 1 notification as read
    public void markRead(int notificationId) {
        String sql = "UPDATE Notification SET is_read = 1 WHERE notification_id = ?";

        try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, notificationId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 5. Delete 1 notification
    public void delete(int notificationId) {
        String sql = "DELETE FROM Notification WHERE notification_id = ?";

        try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, notificationId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 6. Mark all read theo user
    public void markAllRead(int userId) {
        String sql = "UPDATE Notification SET is_read = 1 WHERE user_id = ?";

        try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 7. Delete all theo user
    public void deleteAll(int userId) {
        String sql = "DELETE FROM Notification WHERE user_id = ?";

        try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 8. Đếm unread theo user
    public int countUnreadByUser(int userId) {
        String sql = "SELECT COUNT(*) FROM Notification WHERE user_id = ? AND is_read = 0";

        try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // 9. Insert 1 notification
    public void insert(Notification n) {
        String sql = "INSERT INTO Notification "
                + "(user_id, title, content, is_read, type, priority, related_url, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";

        try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, n.getUserId());
            ps.setString(2, n.getTitle());
            ps.setString(3, n.getContent());
            ps.setBoolean(4, n.isRead());

            ps.setString(5, safeValue(n.getType(), "UPDATE"));
            ps.setString(6, safeValue(n.getPriority(), "MEDIUM"));
            ps.setString(7, n.getRelatedUrl());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 10. Insert notification cho 1 user cụ thể
    public void insertForUser(int userId, String title, String content,
            String type, String priority, String relatedUrl) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setTitle(title);
        n.setContent(content);
        n.setRead(false);
        n.setType(type);
        n.setPriority(priority);
        n.setRelatedUrl(relatedUrl);

        insert(n);
    }

    // 11. Insert notification cho tất cả user theo role
    public void insertForRole(int roleId, String title, String content,
            String type, String priority, String relatedUrl) {
        String sql = "INSERT INTO Notification "
                + "(user_id, title, content, is_read, type, priority, related_url, created_at) "
                + "SELECT user_id, ?, ?, 0, ?, ?, ?, NOW() "
                + "FROM Users "
                + "WHERE role_id = ? AND status = 'ACTIVE'";

        try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, content);
            ps.setString(3, safeValue(type, "UPDATE"));
            ps.setString(4, safeValue(priority, "MEDIUM"));
            ps.setString(5, relatedUrl);
            ps.setInt(6, roleId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 12. Helper map ResultSet -> Notification
    private Notification mapNotification(ResultSet rs) throws Exception {
        Notification n = new Notification();
        n.setNotificationId(rs.getInt("notification_id"));
        n.setUserId(rs.getInt("user_id"));
        n.setTitle(rs.getString("title"));
        n.setContent(rs.getString("content"));
        n.setRead(rs.getBoolean("is_read"));
        n.setType(rs.getString("type"));
        n.setPriority(rs.getString("priority"));
        n.setRelatedUrl(rs.getString("related_url"));
        n.setCreatedAt(rs.getTimestamp("created_at"));
        return n;
    }

    // 13. Helper set default value nếu null hoặc rỗng
    private String safeValue(String value, String defaultValue) {
        return (value == null || value.trim().isEmpty()) ? defaultValue : value;
    }
}
