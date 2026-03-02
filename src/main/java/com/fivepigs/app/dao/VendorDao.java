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

        // luôn đảm bảo đủ 4 tuần
        for (int i = 1; i <= 4; i++) {
            revenueMap.put(i, 0.0);
        }

        String sql = """
                SELECT 
                    CEIL((DATEDIFF(CURDATE(), o.order_date) + 1) / 7)AS week_index,
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
                    int weekIndex = rs.getInt("week_index"); // 1 = gần nhất
                    double revenue = rs.getDouble("revenue");

                    // 🔁 đảo chiều: 1->4, 2->3, 3->2, 4->1
                    int weekDisplay = 5 - Math.min(weekIndex, 4);

                    revenueMap.put(
                            weekDisplay,
                            revenueMap.get(weekDisplay) + revenue
                    );
                }
            }
        }
        return revenueMap;
    }
}
