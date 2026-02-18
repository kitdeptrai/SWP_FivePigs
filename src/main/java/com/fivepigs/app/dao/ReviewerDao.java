package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReviewerDao {

    // 1. Lấy reviewer theo email
    public int getReviewerIdByEmail(String email) {
        String sql = """
            SELECT u.user_id
            FROM Users u
            JOIN Role r ON u.role_id = r.role_id
            WHERE u.email = ? AND r.role_name = 'REVIEWER'
        """;

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 2. Lấy danh sách software đang chờ review
    public List<String> getPendingSoftware() {
        List<String> list = new ArrayList<>();

        String sql = """
            SELECT name
            FROM Software
            WHERE status = 'PENDING_REVIEW'
        """;

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 3. Lưu kết quả review
    public boolean submitReview(int softwareId, int reviewerId, String testResult) {
        String sql = """
            INSERT INTO Software_Review_Process
            (software_id, reviewer_id, test_result)
            VALUES (?, ?, ?)
        """;

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, softwareId);
            ps.setInt(2, reviewerId);
            ps.setString(3, testResult);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // 4. Đổi trạng thái software sau khi review
    public boolean updateSoftwareStatus(int softwareId, String status) {
        String sql = """
            UPDATE Software
            SET status = ?
            WHERE software_id = ?
        """;

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, softwareId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}