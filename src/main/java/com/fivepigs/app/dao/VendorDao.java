/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author MinhPD
 */
public class VendorDao {

    public Double sumRevenue(int vendorId) throws SQLException {
        String sql = "SELECT s.vendor_id,SUM(od.price) AS total_revenue FROM Orders o \n"
                + "JOIN Order_Detail od ON o.order_id = od.order_id\n"
                + "JOIN Software s ON od.software_id = s.software_id\n"
                + "JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id\n"
                + "WHERE ps.status_name = 'PAID' AND s.vendor_id = ?\n"
                + "GROUP BY s.vendor_id;";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vendorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total_revenue");
                }
            }
        }
        return null;
    }

    public Map<Integer, Double> revenueMap(int vendorId) throws SQLException {

        Map<Integer, Double> revenueMap = new HashMap<>();

        for (int i = 1; i <= 4; i++) {
            revenueMap.put(i, 0.0);
        }

        String sql = """
        SELECT 
            FLOOR(DATEDIFF(CURDATE(), o.order_date) / 7) AS week_index,
            SUM(od.price) AS revenue
        FROM Orders o
        JOIN Order_Detail od ON o.order_id = od.order_id
        JOIN Software s ON od.software_id = s.software_id
        JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id
        WHERE ps.status_name = 'PAID'
          AND s.vendor_id = ?
          AND o.order_date >= DATE_SUB(CURDATE(), INTERVAL 28 DAY)
        GROUP BY week_index
    """;

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, vendorId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    int weekIndex = rs.getInt("week_index");
                    double revenue = rs.getDouble("revenue");

                    if (weekIndex >= 0 && weekIndex <= 3) {

                        int weekDisplay = 4 - weekIndex;

                        revenueMap.put(
                                weekDisplay,
                                revenueMap.getOrDefault(weekDisplay, 0.0) + revenue
                        );
                    }
                }
            }
        }

        return revenueMap;
    }

    public List<VendorPayout> getPayoutByVendorId(int vendorId) throws SQLException {
        List<VendorPayout> list = new ArrayList<>();
        String sql = "SELECT * FROM Vendor_Payout\n"
                + "WHERE vendor_id=?;";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vendorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VendorPayout vp=new VendorPayout();
                    vp.setPayoutId(rs.getInt("payout_id"));
                    vp.setAmount(rs.getDouble("amount"));
                    vp.setPeriodStart(rs.getObject("period_start", LocalDateTime.class));
                    vp.setPeriodEnd(rs.getObject("period_end", LocalDateTime.class));
                    vp.setStatus(rs.getString("status"));
                    list.add(vp);
                }
            }
        }
        return list;
    }
}
