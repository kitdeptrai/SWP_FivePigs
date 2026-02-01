/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.dao;

/**
 *
 * @author thanh
 */
import com.fivepigs.app.config.DbApproval;
import com.fivepigs.app.model.Software;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApprovalDao {

    /* SAVE APPROVAL DECISION */
    public void saveApproval(
            int softwareId,
            int approverId,
            String decision,
            String note
    ) throws SQLException {

        String sql = """
            INSERT INTO Software_Approval
                (software_id, approver_id, decision, approval_note)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection c = DbApproval.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, softwareId);
            ps.setInt(2, approverId);
            ps.setString(3, decision);
            ps.setString(4, note);

            ps.executeUpdate();
        }
    }

    /*update softwarestatus*/
    public void updateSoftwareStatus(int softwareId, String status)
            throws SQLException {

        String sql
                = "UPDATE Software SET status = ? WHERE software_id = ?";

        try (Connection c = DbApproval.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, softwareId);

            ps.executeUpdate();
        }
    }

    public Integer countStatusApps(String status) throws SQLException {
        String sql = "SELECT COUNT(*) AS COUNT FROM software WHERE status = ?";
        try (Connection c = DbApproval.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("COUNT");
            }

        }
        return null;
    }

    public List<Software> approvedApp(int approval_id) throws SQLException {
        List<Software> list = new ArrayList<>();

        String sql = """
                SELECT
                s.name,
                sa.decision AS status,
                sa.approval_date,
                sa.approval_note
            FROM Software s
            JOIN Software_Approval sa
                ON s.software_id = sa.software_id
            ORDER BY sa.approval_date DESC;
    """;

        try (Connection c = DbApproval.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

//            ps.setInt(1, approval_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Software s = new Software();
                s.setAppName(rs.getString("name"));
                s.setStatus(rs.getString("status"));
                s.setReviewDate(rs.getDate("approval_date"));
                s.setRecommendation(rs.getString("approval_note"));

                list.add(s);
            }

        }
        return list;
    }

}
