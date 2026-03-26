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
        String sql = "SELECT \n"
                + "    vendor_id,\n"
                + "    SUM(amount) AS total_revenue\n"
                + "FROM Vendor_Earning\n"
                + "WHERE vendor_id = ?\n"
                + "GROUP BY vendor_id;";
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

    public List<VendorEarning> getVendorTransactions(int vendorId) throws SQLException {

        List<VendorEarning> list = new ArrayList<>();

        String sql = """
        SELECT 
            ve.amount AS vendor_revenue,
            od.price AS original_price,
        
            o.commission_percent,
        
            o.order_id,
            o.order_date,
        
            u.full_name,
            u.email,
            s.name
        
        FROM Vendor_Earning ve
        JOIN Orders o ON ve.order_id = o.order_id
        JOIN Order_Detail od ON od.order_id = o.order_id 
                             AND od.software_id = ve.software_id
        JOIN Users u ON o.customer_id = u.user_id
        JOIN Software s ON ve.software_id = s.software_id
        
        WHERE ve.vendor_id = ?
        ORDER BY o.order_date DESC;
    """;

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vendorId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    VendorEarning ve = new VendorEarning();
                    Order o = new Order();
                    OrderDetail od = new OrderDetail();
                    SystemConfig sc = new SystemConfig();
                    Software s = new Software();
                    User u = new User();
                    o.setOrderId(rs.getInt("order_id"));
                    u.setFullName(rs.getString("full_name"));
                    u.setEmail(rs.getString("email"));
                    s.setName(rs.getString("name"));

                    od.setPrice(rs.getDouble("original_price"));
                    ve.setAmount(rs.getDouble("vendor_revenue"));
                    sc.setConfigValue(rs.getString("commission_percent"));

                    o.setOrderDate(rs.getObject("order_date", LocalDateTime.class));
                    ve.setOrder(o);
                    ve.setOrderDetail(od);
                    ve.setSystemConfig(sc);
                    ve.setUser(u);
                    ve.setSoftware(s);
                    list.add(ve);
                }
            }
        }

        return list;
    }
}
