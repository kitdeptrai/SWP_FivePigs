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
 * @author MinhPD
 */
public class PayoutDao {

    public double getAvailableBalance(int vendorId) throws SQLException {

        double earning = 0;
        double payout = 0;

        try (Connection c = Db.getConnection()) {

            // total earning
            String sql1 = "SELECT COALESCE(SUM(amount),0) FROM Vendor_Earning WHERE vendor_id = ?";
            try (PreparedStatement ps = c.prepareStatement(sql1)) {
                ps.setInt(1, vendorId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    earning = rs.getDouble(1);
                }
            }

            // total payout
            String sql2 = "SELECT COALESCE(SUM(amount),0) FROM Vendor_Payout "
                    + "WHERE vendor_id = ? AND status IN ('PENDING','PAID')";
            try (PreparedStatement ps = c.prepareStatement(sql2)) {
                ps.setInt(1, vendorId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    payout = rs.getDouble(1);
                }
            }
        }

        return earning - payout;
    }
}
