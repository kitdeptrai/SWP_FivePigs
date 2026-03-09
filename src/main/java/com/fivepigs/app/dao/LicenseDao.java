/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.License;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.User;
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
public class LicenseDao {

    public List<License> getLicenseByVendorId(int vendorId) throws SQLException {
        List<License> list = new ArrayList<>();
        String sql = "SELECT \n"
                + "    l.license_id,\n"
                + "    l.license_key,\n"
                + "    s.name AS software_name,\n"
                + "    u.full_name AS customer_name,\n"
                + "    u.email,\n"
                + "    l.purchase_date,\n"
                + "    l.expire_date,\n"
                + "    l.status\n"
                + "FROM License l\n"
                + "JOIN Software s ON l.software_id = s.software_id\n"
                + "JOIN Users u ON l.customer_id = u.user_id\n"
                + "WHERE s.vendor_id = ?\n"
                + "ORDER BY l.license_id DESC;";

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vendorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    License li = new License();
                    User user = new User();
                    Software sw = new Software();
                    li.setLicenseId(rs.getInt("license_id"));
                    li.setLicenseKey(rs.getString("license_key"));
                    user.setEmail(rs.getString("email"));
                    user.setFullName(rs.getString("customer_name"));
                    sw.setName(rs.getString("software_name"));
                    li.setSoftware(sw);
                    li.setUser(user);
                    li.setPurchaseDate(rs.getObject("purchase_date", LocalDateTime.class));
                    li.setExpireDate(rs.getObject("expire_date", LocalDateTime.class));
                    li.setStatus(rs.getString("status"));
                    list.add(li);
                }
            }
        }
        return list;
    }

    public Integer getTotalLicenseByVendor(int vendorId) throws SQLException {
        String sql = "SELECT COUNT(*) "
                + "FROM License l "
                + "JOIN Software s ON l.software_id = s.software_id "
                + "WHERE s.vendor_id = ?";

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, vendorId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return null;
    }

    public Integer getTotalLicenseByVendorAndStatus(int vendorId, String status) throws SQLException {
        String sql = "SELECT COUNT(*) "
                + "FROM License l "
                + "JOIN Software s ON l.software_id = s.software_id "
                + "WHERE s.vendor_id = ? "
                + "AND l.status = ?";

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, vendorId);
            ps.setString(2, status);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return null;
    }

    public void changeStatusSoftware(String status, int licenseId) throws SQLException {

        String sql = "UPDATE License\n"
                + "SET status = ?\n"
                + "WHERE license_id = ?;";

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(2, licenseId);
            ps.setString(1, status);

            ps.executeUpdate();
        }
    }

    public void updateExpiredLicense() throws SQLException {
        String sql = """
                UPDATE license
                SET status = 'EXPIRED'
                WHERE status = 'ACTIVE'
                AND expire_date < NOW()
                """;

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.executeUpdate();
        }
    }
}
