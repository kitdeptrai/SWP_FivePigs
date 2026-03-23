package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.Software;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CartDao {

    public List<Software> getCartItems(int userId) throws SQLException {
        String sql = "SELECT s.software_id, s.name, s.is_free, s.avg_rating, img.image_url AS icon_url, " +
                "       COALESCE(cd.pricing_id, " +
                "           (SELECT sp.pricing_id FROM fivepigs.software_pricing sp WHERE sp.software_id = s.software_id AND sp.is_active = 1 AND UPPER(COALESCE(sp.plan_name, '')) <> 'DEMO' ORDER BY sp.price ASC, sp.pricing_id ASC LIMIT 1)" +
                "       ) AS pricing_id, " +
                "       COALESCE(selected_pricing.plan_name, " +
                "           (SELECT sp.plan_name FROM fivepigs.software_pricing sp WHERE sp.software_id = s.software_id AND sp.is_active = 1 AND UPPER(COALESCE(sp.plan_name, '')) <> 'DEMO' ORDER BY sp.price ASC, sp.pricing_id ASC LIMIT 1)" +
                "       ) AS plan_name, " +
                "       COALESCE(selected_pricing.max_users, " +
                "           (SELECT sp.max_users FROM fivepigs.software_pricing sp WHERE sp.software_id = s.software_id AND sp.is_active = 1 AND UPPER(COALESCE(sp.plan_name, '')) <> 'DEMO' ORDER BY sp.price ASC, sp.pricing_id ASC LIMIT 1)" +
                "       ) AS max_users, " +
                "       COALESCE(selected_pricing.duration_days, " +
                "           (SELECT sp.duration_days FROM fivepigs.software_pricing sp WHERE sp.software_id = s.software_id AND sp.is_active = 1 AND UPPER(COALESCE(sp.plan_name, '')) <> 'DEMO' ORDER BY sp.price ASC, sp.pricing_id ASC LIMIT 1)" +
                "       ) AS duration_days, " +
                "       COALESCE(selected_pricing.price, " +
                "           (SELECT MIN(sp.price) FROM fivepigs.software_pricing sp WHERE sp.software_id = s.software_id AND sp.is_active = 1 AND UPPER(COALESCE(sp.plan_name, '')) <> 'DEMO'), " +
                "           0" +
                "       ) AS display_price " +
                "FROM fivepigs.cart c " +
                "JOIN fivepigs.cart_detail cd ON c.cart_id = cd.cart_id " +
                "JOIN fivepigs.software s ON s.software_id = cd.software_id " +
                "LEFT JOIN fivepigs.software_pricing selected_pricing ON cd.pricing_id = selected_pricing.pricing_id " +
                "LEFT JOIN fivepigs.software_image img " +
                "  ON s.software_id = img.software_id AND img.is_thumbnail = 1 " +
                "WHERE c.customer_id = ? " +
                "ORDER BY cd.cart_detail_id DESC";

        List<Software> items = new ArrayList<>();
        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, userId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Software sw = new Software();
                    sw.setSoftwareId(rs.getInt("software_id"));
                    sw.setName(rs.getString("name"));
                    Number pricingIdValue = (Number) rs.getObject("pricing_id");
                    sw.setPricingId(pricingIdValue == null ? null : pricingIdValue.intValue());
                    sw.setPlanName(rs.getString("plan_name"));
                    Number maxUsersValue = (Number) rs.getObject("max_users");
                    sw.setPlanMaxUsers(maxUsersValue == null ? null : maxUsersValue.intValue());
                    Number durationDaysValue = (Number) rs.getObject("duration_days");
                    sw.setDurationDays(durationDaysValue == null ? null : durationDaysValue.intValue());
                    sw.setPrice(rs.getDouble("display_price"));
                    sw.setIsFree(rs.getInt("is_free"));
                    sw.setAvgRating(rs.getDouble("avg_rating"));
                    sw.setIconUrl(rs.getString("icon_url"));
                    items.add(sw);
                }
            }
        }
        return items;
    }

    public boolean addToCart(int userId, int softwareId, Integer requestedPricingId) throws SQLException {
        try (Connection c = Db.getConnection()) {
            c.setAutoCommit(false);
            try {
                int cartId = getOrCreateCartId(c, userId);
                if (hasActivePaidLicense(c, userId, softwareId)) {
                    c.commit();
                    return false;
                }

                Integer pricingId = resolveSelectedPricingId(c, softwareId, requestedPricingId);

                if (hasCartItem(c, cartId, softwareId)) {
                    try (PreparedStatement st = c.prepareStatement(
                            "UPDATE fivepigs.cart_detail SET pricing_id = ? WHERE cart_id = ? AND software_id = ?")) {
                        if (pricingId == null) {
                            st.setNull(1, Types.INTEGER);
                        } else {
                            st.setInt(1, pricingId);
                        }
                        st.setInt(2, cartId);
                        st.setInt(3, softwareId);
                        st.executeUpdate();
                    }
                    c.commit();
                    return true;
                }

                try (PreparedStatement st = c.prepareStatement(
                        "INSERT INTO fivepigs.cart_detail(cart_id, software_id, pricing_id) VALUES(?, ?, ?)")) {
                    st.setInt(1, cartId);
                    st.setInt(2, softwareId);
                    if (pricingId == null) {
                        st.setNull(3, Types.INTEGER);
                    } else {
                        st.setInt(3, pricingId);
                    }
                    st.executeUpdate();
                }
                c.commit();
                return true;
            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public void removeFromCart(int userId, int softwareId) throws SQLException {
        String sql = "DELETE cd FROM fivepigs.cart_detail cd " +
                "JOIN fivepigs.cart c ON c.cart_id = cd.cart_id " +
                "WHERE c.customer_id = ? AND cd.software_id = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, userId);
            st.setInt(2, softwareId);
            st.executeUpdate();
        }
    }

    public int checkout(int userId) throws SQLException {
        try (Connection c = Db.getConnection()) {
            c.setAutoCommit(false);
            try {
                int cartId = getOrCreateCartId(c, userId);
                List<Software> items = getCartItemsTx(c, cartId);
                if (items.isEmpty()) {
                    c.commit();
                    return 0;
                }

                int paidStatusId = getOrCreatePaidStatusId(c);
                double total = items.stream()
                        .mapToDouble(i -> i.getIsFree() != null && i.getIsFree() == 1 ? 0.0 : safePrice(i.getPrice()))
                        .sum();

                int orderId;
                try (PreparedStatement st = c.prepareStatement(
                        "INSERT INTO fivepigs.orders(customer_id, payment_status_id, total_amount) VALUES(?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    st.setInt(1, userId);
                    st.setInt(2, paidStatusId);
                    st.setDouble(3, total);
                    st.executeUpdate();

                    try (ResultSet keys = st.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Cannot create order id");
                        }
                        orderId = keys.getInt(1);
                    }
                }

                for (Software item : items) {
                    double price = item.getIsFree() != null && item.getIsFree() == 1 ? 0.0 : safePrice(item.getPrice());

                    try (PreparedStatement st = c.prepareStatement(
                            "INSERT INTO fivepigs.order_detail(order_id, software_id, price, pricing_id) VALUES(?, ?, ?, ?)")) {
                        st.setInt(1, orderId);
                        st.setInt(2, item.getSoftwareId());
                        st.setDouble(3, price);
                        if (item.getPricingId() == null) {
                            st.setNull(4, Types.INTEGER);
                        } else {
                            st.setInt(4, item.getPricingId());
                        }
                        st.executeUpdate();
                    }

                    if (!hasActivePaidLicense(c, userId, item.getSoftwareId())) {
                        Integer maxUsers = resolveMaxUsers(c, item.getPricingId());
                        Timestamp expireAt = resolveExpireAt(item.getDurationDays());
                        int licenseId;
                        try (PreparedStatement st = c.prepareStatement(
                                "INSERT INTO fivepigs.license(license_key, pricing_id, software_id, owner_id, max_users, purchase_date, expire_date, status) " +
                                        "VALUES(?, ?, ?, ?, ?, NOW(), ?, 'ACTIVE')",
                                Statement.RETURN_GENERATED_KEYS)) {
                            st.setString(1, generateLicenseKey());
                            if (item.getPricingId() == null) {
                                st.setNull(2, Types.INTEGER);
                            } else {
                                st.setInt(2, item.getPricingId());
                            }
                            st.setInt(3, item.getSoftwareId());
                            st.setInt(4, userId);
                            st.setInt(5, maxUsers == null ? 1 : maxUsers);
                            if (expireAt == null) {
                                st.setNull(6, Types.TIMESTAMP);
                            } else {
                                st.setTimestamp(6, expireAt);
                            }
                            st.executeUpdate();

                            try (ResultSet keys = st.getGeneratedKeys()) {
                                if (!keys.next()) {
                                    throw new SQLException("Cannot create license id");
                                }
                                licenseId = keys.getInt(1);
                            }
                        }

                        try (PreparedStatement st = c.prepareStatement(
                                "INSERT INTO fivepigs.license_user(license_id, user_id, status) VALUES(?, ?, 'ACTIVE')")) {
                            st.setInt(1, licenseId);
                            st.setInt(2, userId);
                            st.executeUpdate();
                        }
                    }
                }

                try (PreparedStatement st = c.prepareStatement("DELETE FROM fivepigs.cart_detail WHERE cart_id = ?")) {
                    st.setInt(1, cartId);
                    st.executeUpdate();
                }

                c.commit();
                return items.size();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public double getCartTotal(int userId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(CASE WHEN s.is_free = 1 THEN 0 ELSE COALESCE(selected_pricing.price, " +
                "           (SELECT MIN(sp.price) FROM fivepigs.software_pricing sp WHERE sp.software_id = s.software_id AND sp.is_active = 1 AND UPPER(COALESCE(sp.plan_name, '')) <> 'DEMO'), 0) END), 0) AS total " +
                "FROM fivepigs.cart c " +
                "JOIN fivepigs.cart_detail cd ON c.cart_id = cd.cart_id " +
                "JOIN fivepigs.software s ON s.software_id = cd.software_id " +
                "LEFT JOIN fivepigs.software_pricing selected_pricing ON cd.pricing_id = selected_pricing.pricing_id " +
                "WHERE c.customer_id = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, userId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }
        return 0;
    }

    private int getOrCreateCartId(Connection c, int userId) throws SQLException {
        try (PreparedStatement st = c.prepareStatement("SELECT cart_id FROM fivepigs.cart WHERE customer_id = ? LIMIT 1")) {
            st.setInt(1, userId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cart_id");
                }
            }
        }

        try (PreparedStatement st = c.prepareStatement(
                "INSERT INTO fivepigs.cart(customer_id) VALUES(?)", Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, userId);
            st.executeUpdate();
            try (ResultSet keys = st.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Cannot create cart");
    }

    private boolean hasCartItem(Connection c, int cartId, int softwareId) throws SQLException {
        try (PreparedStatement st = c.prepareStatement(
                "SELECT 1 FROM fivepigs.cart_detail WHERE cart_id = ? AND software_id = ? LIMIT 1")) {
            st.setInt(1, cartId);
            st.setInt(2, softwareId);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean hasActivePaidLicense(Connection c, int userId, int softwareId) throws SQLException {
        try (PreparedStatement st = c.prepareStatement(
                "SELECT 1 " +
                        "FROM fivepigs.license_user lu " +
                        "JOIN fivepigs.license l ON lu.license_id = l.license_id " +
                        "LEFT JOIN fivepigs.software_pricing sp ON l.pricing_id = sp.pricing_id " +
                        "WHERE lu.user_id = ? AND l.software_id = ? " +
                        "AND (lu.status IS NULL OR UPPER(lu.status) = 'ACTIVE') " +
                        "AND (l.status IS NULL OR UPPER(l.status) <> 'REVOKED') " +
                        "AND (l.expire_date IS NULL OR l.expire_date >= NOW()) " +
                        "AND UPPER(COALESCE(sp.plan_name, 'PAID')) <> 'DEMO' " +
                        "LIMIT 1")) {
            st.setInt(1, userId);
            st.setInt(2, softwareId);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        }
    }

    private List<Software> getCartItemsTx(Connection c, int cartId) throws SQLException {
        String sql = "SELECT s.software_id, s.name, s.is_free, " +
                "       COALESCE(cd.pricing_id, " +
                "           (SELECT sp.pricing_id FROM fivepigs.software_pricing sp WHERE sp.software_id = s.software_id AND sp.is_active = 1 AND UPPER(COALESCE(sp.plan_name, '')) <> 'DEMO' ORDER BY sp.price ASC, sp.pricing_id ASC LIMIT 1)" +
                "       ) AS pricing_id, " +
                "       COALESCE(selected_pricing.plan_name, " +
                "           (SELECT sp.plan_name FROM fivepigs.software_pricing sp WHERE sp.software_id = s.software_id AND sp.is_active = 1 AND UPPER(COALESCE(sp.plan_name, '')) <> 'DEMO' ORDER BY sp.price ASC, sp.pricing_id ASC LIMIT 1)" +
                "       ) AS plan_name, " +
                "       COALESCE(selected_pricing.max_users, " +
                "           (SELECT sp.max_users FROM fivepigs.software_pricing sp WHERE sp.software_id = s.software_id AND sp.is_active = 1 AND UPPER(COALESCE(sp.plan_name, '')) <> 'DEMO' ORDER BY sp.price ASC, sp.pricing_id ASC LIMIT 1)" +
                "       ) AS max_users, " +
                "       COALESCE(selected_pricing.duration_days, " +
                "           (SELECT sp.duration_days FROM fivepigs.software_pricing sp WHERE sp.software_id = s.software_id AND sp.is_active = 1 AND UPPER(COALESCE(sp.plan_name, '')) <> 'DEMO' ORDER BY sp.price ASC, sp.pricing_id ASC LIMIT 1)" +
                "       ) AS duration_days, " +
                "       COALESCE(selected_pricing.price, " +
                "           (SELECT MIN(sp.price) FROM fivepigs.software_pricing sp WHERE sp.software_id = s.software_id AND sp.is_active = 1 AND UPPER(COALESCE(sp.plan_name, '')) <> 'DEMO'), " +
                "           0" +
                "       ) AS display_price " +
                "FROM fivepigs.cart_detail cd " +
                "JOIN fivepigs.software s ON s.software_id = cd.software_id " +
                "LEFT JOIN fivepigs.software_pricing selected_pricing ON cd.pricing_id = selected_pricing.pricing_id " +
                "WHERE cd.cart_id = ?";

        List<Software> items = new ArrayList<>();
        try (PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, cartId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Software sw = new Software();
                    sw.setSoftwareId(rs.getInt("software_id"));
                    sw.setName(rs.getString("name"));
                    Number pricingIdValue = (Number) rs.getObject("pricing_id");
                    sw.setPricingId(pricingIdValue == null ? null : pricingIdValue.intValue());
                    sw.setPlanName(rs.getString("plan_name"));
                    Number maxUsersValue = (Number) rs.getObject("max_users");
                    sw.setPlanMaxUsers(maxUsersValue == null ? null : maxUsersValue.intValue());
                    Number durationDaysValue = (Number) rs.getObject("duration_days");
                    sw.setDurationDays(durationDaysValue == null ? null : durationDaysValue.intValue());
                    sw.setPrice(rs.getDouble("display_price"));
                    sw.setIsFree(rs.getInt("is_free"));
                    items.add(sw);
                }
            }
        }
        return items;
    }

    public boolean addToCart(int userId, int softwareId) throws SQLException {
        return addToCart(userId, softwareId, null);
    }

    private Integer resolveActivePricingId(Connection c, int softwareId) throws SQLException {
        String sql = "SELECT pricing_id FROM fivepigs.software_pricing " +
                "WHERE software_id = ? AND is_active = 1 " +
                "AND UPPER(COALESCE(plan_name, '')) <> 'DEMO' " +
                "ORDER BY price ASC, pricing_id ASC LIMIT 1";
        try (PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, softwareId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("pricing_id");
                }
            }
        }
        return null;
    }

    private Integer resolveSelectedPricingId(Connection c, int softwareId, Integer requestedPricingId) throws SQLException {
        if (requestedPricingId != null) {
            try (PreparedStatement st = c.prepareStatement(
                    "SELECT pricing_id FROM fivepigs.software_pricing WHERE pricing_id = ? AND software_id = ? AND is_active = 1 LIMIT 1")) {
                st.setInt(1, requestedPricingId);
                st.setInt(2, softwareId);
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("pricing_id");
                    }
                }
            }
        }
        return resolveActivePricingId(c, softwareId);
    }

    private Integer resolveMaxUsers(Connection c, Integer pricingId) throws SQLException {
        if (pricingId == null) {
            return 1;
        }
        try (PreparedStatement st = c.prepareStatement(
                "SELECT max_users FROM fivepigs.software_pricing WHERE pricing_id = ? LIMIT 1")) {
            st.setInt(1, pricingId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("max_users");
                }
            }
        }
        return 1;
    }

    private int getOrCreatePaidStatusId(Connection c) throws SQLException {
        try (PreparedStatement st = c.prepareStatement(
                "SELECT payment_status_id FROM fivepigs.payment_status WHERE UPPER(status_name) = 'PAID' LIMIT 1")) {
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("payment_status_id");
                }
            }
        }

        try (PreparedStatement st = c.prepareStatement(
                "INSERT INTO fivepigs.payment_status(status_name) VALUES('PAID')", Statement.RETURN_GENERATED_KEYS)) {
            st.executeUpdate();
            try (ResultSet keys = st.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Cannot create payment status PAID");
    }

    private String generateLicenseKey() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    private double safePrice(Double price) {
        return price == null ? 0.0 : price;
    }

    private Timestamp resolveExpireAt(Integer durationDays) {
        if (durationDays == null || durationDays <= 0) {
            return null;
        }
        return Timestamp.valueOf(LocalDateTime.now().plusDays(durationDays));
    }
}
