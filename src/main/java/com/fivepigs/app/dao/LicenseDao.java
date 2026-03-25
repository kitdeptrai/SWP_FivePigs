/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.License;
import com.fivepigs.app.model.SoftwarePricing;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author MinhPD
 */
public class LicenseDao {

    public Map<Integer, License> getOwnedLicenseMapByOwner(int ownerId) throws SQLException {
        String sql = """
                SELECT l.license_id,
                       l.software_id,
                       l.owner_id,
                       l.max_users,
                       l.license_key,
                       l.purchase_date,
                       l.expire_date,
                       l.status,
                       sp.plan_name,
                       COUNT(CASE WHEN lu.status IS NULL OR UPPER(lu.status) = 'ACTIVE' THEN 1 END) AS assigned_count
                FROM License l
                LEFT JOIN Software_Pricing sp ON l.pricing_id = sp.pricing_id
                LEFT JOIN License_User lu ON l.license_id = lu.license_id
                WHERE l.owner_id = ?
                  AND (l.status IS NULL OR UPPER(l.status) <> 'REVOKED')
                  AND (l.expire_date IS NULL OR l.expire_date >= NOW())
                GROUP BY l.license_id, l.software_id, l.owner_id, l.max_users, l.license_key, l.purchase_date, l.expire_date, l.status, sp.plan_name
                ORDER BY l.purchase_date DESC, l.license_id DESC
                """;

        Map<Integer, License> map = new LinkedHashMap<>();
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    License license = new License();
                    license.setLicenseId(rs.getInt("license_id"));
                    license.setSoftwareId(rs.getInt("software_id"));
                    license.setOwnerId(rs.getInt("owner_id"));
                    Number maxUsersValue = (Number) rs.getObject("max_users");
                    license.setMaxUsers(maxUsersValue == null ? null : maxUsersValue.intValue());
                    license.setLicenseKey(rs.getString("license_key"));
                    license.setPurchaseDate(rs.getObject("purchase_date", LocalDateTime.class));
                    license.setExpireDate(rs.getObject("expire_date", LocalDateTime.class));
                    license.setStatus(rs.getString("status"));
                    license.setPlanName(rs.getString("plan_name"));
                    Number assignedCountValue = (Number) rs.getObject("assigned_count");
                    license.setAssignedCount(assignedCountValue == null ? 0 : assignedCountValue.intValue());
                    map.put(license.getSoftwareId(), license);
                }
            }
        }

        if (!map.isEmpty()) {
            String memberSql = """
                    SELECT l.software_id, u.user_id, u.full_name, u.email, lu.status
                    FROM License l
                    JOIN License_User lu ON l.license_id = lu.license_id
                    JOIN Users u ON lu.user_id = u.user_id
                    WHERE l.owner_id = ?
                      AND (l.status IS NULL OR UPPER(l.status) <> 'REVOKED')
                      AND (l.expire_date IS NULL OR l.expire_date >= NOW())
                      AND (lu.status IS NULL OR UPPER(lu.status) = 'ACTIVE')
                    ORDER BY l.software_id, lu.assigned_at ASC, lu.license_user_id ASC
                    """;
            try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(memberSql)) {
                ps.setInt(1, ownerId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        License license = map.get(rs.getInt("software_id"));
                        if (license == null) {
                            continue;
                        }
                        User member = new User();
                        member.setUserId(rs.getInt("user_id"));
                        member.setFullName(rs.getString("full_name"));
                        member.setEmail(rs.getString("email"));
                        member.setStatus(rs.getString("status"));
                        license.getAssignedUsers().add(member);
                    }
                }
            }
        }

        return map;
    }

    public String shareOwnedLicenseByEmail(int ownerId, int softwareId, String email) throws SQLException {
        try (Connection c = Db.getConnection()) {
            c.setAutoCommit(false);
            try {
                License license = getOwnedLicenseForUpdate(c, ownerId, softwareId);
                if (license == null) {
                    c.commit();
                    return "not_owner";
                }

                User targetUser = findUserByEmail(c, email);
                if (targetUser == null || targetUser.getUserId() == null) {
                    c.commit();
                    return "user_not_found";
                }

                if (targetUser.getUserId().equals(ownerId)) {
                    c.commit();
                    return "already_assigned";
                }

                if (hasActiveLicenseUser(c, license.getLicenseId(), targetUser.getUserId())) {
                    c.commit();
                    return "already_assigned";
                }

                int activeCount = countActiveLicenseUsers(c, license.getLicenseId());
                int maxUsers = license.getMaxUsers() == null ? 1 : license.getMaxUsers();
                if (activeCount >= maxUsers) {
                    c.commit();
                    return "slot_full";
                }

                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO License_User(license_id, user_id, status) VALUES(?, ?, 'ACTIVE')")) {
                    ps.setInt(1, license.getLicenseId());
                    ps.setInt(2, targetUser.getUserId());
                    ps.executeUpdate();
                }

                c.commit();
                return "shared";
            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public String removeSharedUser(int ownerId, int softwareId, int targetUserId) throws SQLException {
        try (Connection c = Db.getConnection()) {
            c.setAutoCommit(false);
            try {
                License license = getOwnedLicenseForUpdate(c, ownerId, softwareId);
                if (license == null) {
                    c.commit();
                    return "not_owner";
                }

                if (targetUserId == ownerId) {
                    c.commit();
                    return "cannot_remove_owner";
                }

                try (PreparedStatement ps = c.prepareStatement(
                        "DELETE FROM License_User WHERE license_id = ? AND user_id = ?")) {
                    ps.setInt(1, license.getLicenseId());
                    ps.setInt(2, targetUserId);
                    int affected = ps.executeUpdate();
                    c.commit();
                    return affected > 0 ? "removed" : "not_found";
                }
            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public String startDemoTrial(int userId, int softwareId) throws SQLException {
        try (Connection c = Db.getConnection()) {
            c.setAutoCommit(false);
            try {
                SoftwarePricing demoPricing = getDemoPricingForUpdate(c, softwareId);
                if (demoPricing == null || demoPricing.getPricingId() == null) {
                    c.commit();
                    return "unavailable";
                }

                if (hasAnyActiveAccess(c, userId, softwareId)) {
                    c.commit();
                    return "already_owned";
                }

                if (hasUsedDemoTrial(c, userId, softwareId)) {
                    c.commit();
                    return "already_used";
                }

                int maxUsers = demoPricing.getMaxUsers() == null || demoPricing.getMaxUsers() <= 0 ? 1 : demoPricing.getMaxUsers();
                LocalDateTime expireAt = LocalDateTime.now().plusDays(3);

                int licenseId;
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO License(license_key, pricing_id, software_id, owner_id, max_users, purchase_date, expire_date, status) " +
                                "VALUES (?, ?, ?, ?, ?, NOW(), ?, 'ACTIVE')",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, generateLicenseKey());
                    ps.setInt(2, demoPricing.getPricingId());
                    ps.setInt(3, softwareId);
                    ps.setInt(4, userId);
                    ps.setInt(5, maxUsers);
                    if (expireAt == null) {
                        ps.setNull(6, java.sql.Types.TIMESTAMP);
                    } else {
                        ps.setObject(6, expireAt);
                    }
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Cannot create demo license id");
                        }
                        licenseId = keys.getInt(1);
                    }
                }

                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO License_User(license_id, user_id, status) VALUES(?, ?, 'ACTIVE')")) {
                    ps.setInt(1, licenseId);
                    ps.setInt(2, userId);
                    ps.executeUpdate();
                }

                c.commit();
                return "started";
            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    private License getOwnedLicenseForUpdate(Connection c, int ownerId, int softwareId) throws SQLException {
        String sql = """
                SELECT license_id, software_id, owner_id, max_users, license_key, purchase_date, expire_date, status
                FROM License
                WHERE owner_id = ? AND software_id = ?
                  AND (status IS NULL OR UPPER(status) <> 'REVOKED')
                  AND (expire_date IS NULL OR expire_date >= NOW())
                ORDER BY purchase_date DESC, license_id DESC
                LIMIT 1
                """;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ps.setInt(2, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    License license = new License();
                    license.setLicenseId(rs.getInt("license_id"));
                    license.setSoftwareId(rs.getInt("software_id"));
                    license.setOwnerId(rs.getInt("owner_id"));
                    Number maxUsersValue = (Number) rs.getObject("max_users");
                    license.setMaxUsers(maxUsersValue == null ? null : maxUsersValue.intValue());
                    license.setLicenseKey(rs.getString("license_key"));
                    license.setPurchaseDate(rs.getObject("purchase_date", LocalDateTime.class));
                    license.setExpireDate(rs.getObject("expire_date", LocalDateTime.class));
                    license.setStatus(rs.getString("status"));
                    return license;
                }
            }
        }
        return null;
    }

    private User findUserByEmail(Connection c, String email) throws SQLException {
        String sql = "SELECT user_id, full_name, email, status FROM Users WHERE LOWER(email) = LOWER(?) LIMIT 1";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setStatus(rs.getString("status"));
                    return user;
                }
            }
        }
        return null;
    }

    private boolean hasActiveLicenseUser(Connection c, int licenseId, int userId) throws SQLException {
        String sql = "SELECT 1 FROM License_User WHERE license_id = ? AND user_id = ? AND (status IS NULL OR UPPER(status) = 'ACTIVE') LIMIT 1";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, licenseId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private int countActiveLicenseUsers(Connection c, int licenseId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM License_User WHERE license_id = ? AND (status IS NULL OR UPPER(status) = 'ACTIVE')";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, licenseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

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

    private boolean hasOwnedLicense(int userId, int softwareId) throws SQLException {
        String sql = "SELECT 1 " +
                "FROM fivepigs.license_user lu " +
                "JOIN fivepigs.license l ON lu.license_id = l.license_id " +
                "WHERE lu.user_id = ? AND l.software_id = ? " +
                "AND (lu.status IS NULL OR UPPER(lu.status) = 'ACTIVE') " +
                "AND (l.status IS NULL OR UPPER(l.status) <> 'REVOKED') " +
                "AND (l.expire_date IS NULL OR l.expire_date >= NOW()) LIMIT 1";

        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, userId);
            st.setInt(2, softwareId);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        }
    }

    private SoftwarePricing getDemoPricingForUpdate(Connection c, int softwareId) throws SQLException {
        String sql = """
                SELECT pricing_id, software_id, plan_name, max_users, price, is_active, created_at
                FROM Software_Pricing
                WHERE software_id = ?
                  AND is_active = 1
                  AND UPPER(COALESCE(plan_name, '')) = 'DEMO'
                ORDER BY pricing_id ASC
                LIMIT 1
                """;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SoftwarePricing pricing = new SoftwarePricing();
                    Number pricingIdValue = (Number) rs.getObject("pricing_id");
                    pricing.setPricingId(pricingIdValue == null ? null : pricingIdValue.intValue());
                    Number softwareIdValue = (Number) rs.getObject("software_id");
                    pricing.setSoftwareId(softwareIdValue == null ? null : softwareIdValue.intValue());
                    pricing.setPlanName(rs.getString("plan_name"));
                    Number maxUsersValue = (Number) rs.getObject("max_users");
                    pricing.setMaxUsers(maxUsersValue == null ? null : maxUsersValue.intValue());
                    pricing.setPrice(rs.getDouble("price"));
                    Object isActiveValue = rs.getObject("is_active");
                    if (isActiveValue instanceof Boolean booleanValue) {
                        pricing.setIsActive(booleanValue ? 1 : 0);
                    } else if (isActiveValue instanceof Number numberValue) {
                        pricing.setIsActive(numberValue.intValue());
                    } else {
                        pricing.setIsActive(null);
                    }
                    pricing.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                    return pricing;
                }
            }
        }
        return null;
    }

    private boolean hasAnyActiveAccess(Connection c, int userId, int softwareId) throws SQLException {
        String sql = """
                SELECT 1
                FROM License_User lu
                JOIN License l ON lu.license_id = l.license_id
                WHERE lu.user_id = ?
                  AND l.software_id = ?
                  AND (lu.status IS NULL OR UPPER(lu.status) = 'ACTIVE')
                  AND (l.status IS NULL OR UPPER(l.status) <> 'REVOKED')
                  AND (l.expire_date IS NULL OR l.expire_date >= NOW())
                LIMIT 1
                """;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean hasUsedDemoTrial(Connection c, int userId, int softwareId) throws SQLException {
        String sql = """
                SELECT 1
                FROM License_User lu
                JOIN License l ON lu.license_id = l.license_id
                JOIN Software_Pricing sp ON l.pricing_id = sp.pricing_id
                WHERE lu.user_id = ?
                  AND l.software_id = ?
                  AND UPPER(COALESCE(sp.plan_name, '')) = 'DEMO'
                LIMIT 1
                """;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, softwareId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String generateLicenseKey() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
