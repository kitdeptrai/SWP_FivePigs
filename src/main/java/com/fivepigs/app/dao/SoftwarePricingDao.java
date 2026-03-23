/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.SoftwarePricing;
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
public class SoftwarePricingDao {

    public List<SoftwarePricing> getPlanBySoftwareId(int softwareId) throws SQLException {

        List<SoftwarePricing> list = new ArrayList<>();

        String sql = """
            SELECT 
            pricing_id,
            plan_name,
            max_users,
            price,
            is_active,
            created_at
        FROM Software_Pricing
        WHERE software_id = ?
        ORDER BY created_at ASC;
    """;

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, softwareId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                SoftwarePricing swp = new SoftwarePricing();

                swp.setPricingId(rs.getInt("pricing_id"));
                swp.setPlanName(rs.getString("plan_name"));
                swp.setMaxUsers(rs.getInt("max_users"));
                swp.setPrice(rs.getDouble("price"));
                swp.setCreatedDate(rs.getObject("created_at", LocalDateTime.class));
                swp.setIsActive(rs.getInt("is_active"));
                list.add(swp);
            }
        }

        return list;
    }

    public void togglePlanStatus(int pricingId) throws SQLException {

        String sql = "UPDATE Software_Pricing "
                + "SET is_active = CASE "
                + "WHEN is_active = 1 THEN 0 ELSE 1 END "
                + "WHERE pricing_id = ? "
                + "AND plan_name NOT IN ('BASIC', 'DEMO')";

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, pricingId);
            ps.executeUpdate();
        }
    }

    public void createPlan(int softwareId, String planName, int maxUsers, double price) throws SQLException {

        String sql = "INSERT INTO Software_Pricing "
                + "(software_id, plan_name, max_users, price) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, softwareId);
            ps.setString(2, planName);
            ps.setInt(3, maxUsers);
            ps.setDouble(4, price);

            ps.executeUpdate();
        }
    }

    public boolean isPlanNameExist(int softwareId, String planName) throws SQLException {

        String sql = "SELECT COUNT(*) FROM Software_Pricing "
                + "WHERE software_id = ? AND plan_name = ?";

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, softwareId);
            ps.setString(2, planName);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }

        return false;
    }
}
