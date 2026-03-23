package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.Software;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CartDao {

    public List<Software> getCartItems(int userId) throws SQLException {
        String sql = "SELECT s.*, img.image_url AS icon_url " +
                "FROM fivepigs.cart c " +
                "JOIN fivepigs.cart_detail cd ON c.cart_id = cd.cart_id " +
                "JOIN fivepigs.software s ON s.software_id = cd.software_id " +
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
                    sw.setPrice(rs.getDouble("price"));
                    sw.setIsFree(rs.getInt("is_free"));
                    sw.setAvgRating(rs.getDouble("avg_rating"));
                    sw.setIconUrl(rs.getString("icon_url"));
                    items.add(sw);
                }
            }
        }
        return items;
    }

    public boolean addToCart(int userId, int softwareId) throws SQLException {
        try (Connection c = Db.getConnection()) {
            c.setAutoCommit(false);
            try {
                int cartId = getOrCreateCartId(c, userId);
                if (hasCartItem(c, cartId, softwareId)) {
                    c.commit();
                    return false;
                }
                if (hasActiveLicense(c, userId, softwareId)) {
                    c.commit();
                    return false;
                }

                try (PreparedStatement st = c.prepareStatement(
                        "INSERT INTO fivepigs.cart_detail(cart_id, software_id) VALUES(?, ?)")) {
                    st.setInt(1, cartId);
                    st.setInt(2, softwareId);
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
                ensureOrderCommissionColumn(c);
                int cartId = getOrCreateCartId(c, userId);
                List<Software> items = getCartItemsTx(c, cartId);
                if (items.isEmpty()) {
                    c.commit();
                    return 0;
                }

                int paidStatusId = getOrCreatePaidStatusId(c);
                double commissionRateSnapshot = getCommissionRateSnapshot(c);
                double total = items.stream().mapToDouble(i -> i.getIsFree() != null && i.getIsFree() == 1 ? 0.0 : safePrice(i.getPrice())).sum();

                int orderId;
                try (PreparedStatement st = c.prepareStatement(
                        "INSERT INTO fivepigs.orders(customer_id, payment_status_id, total_amount, commission_rate) VALUES(?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    st.setInt(1, userId);
                    st.setInt(2, paidStatusId);
                    st.setDouble(3, total);
                    st.setDouble(4, commissionRateSnapshot);
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
                            "INSERT INTO fivepigs.order_detail(order_id, software_id, price) VALUES(?, ?, ?)") ) {
                        st.setInt(1, orderId);
                        st.setInt(2, item.getSoftwareId());
                        st.setDouble(3, price);
                        st.executeUpdate();
                    }

                    if (!hasActiveLicense(c, userId, item.getSoftwareId())) {
                        try (PreparedStatement st = c.prepareStatement(
                                "INSERT INTO fivepigs.license(license_key, software_id, customer_id, purchase_date, expire_date, status) " +
                                        "VALUES(?, ?, ?, NOW(), NULL, 'ACTIVE')")) {
                            st.setString(1, generateLicenseKey());
                            st.setInt(2, item.getSoftwareId());
                            st.setInt(3, userId);
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
        String sql = "SELECT COALESCE(SUM(CASE WHEN s.is_free = 1 THEN 0 ELSE s.price END), 0) AS total " +
                "FROM fivepigs.cart c " +
                "JOIN fivepigs.cart_detail cd ON c.cart_id = cd.cart_id " +
                "JOIN fivepigs.software s ON s.software_id = cd.software_id " +
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

    private boolean hasActiveLicense(Connection c, int userId, int softwareId) throws SQLException {
        try (PreparedStatement st = c.prepareStatement(
                "SELECT 1 FROM fivepigs.license WHERE customer_id = ? AND software_id = ? AND (status IS NULL OR UPPER(status) <> 'REVOKED') LIMIT 1")) {
            st.setInt(1, userId);
            st.setInt(2, softwareId);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        }
    }

    private List<Software> getCartItemsTx(Connection c, int cartId) throws SQLException {
        String sql = "SELECT s.software_id, s.name, s.price, s.is_free " +
                "FROM fivepigs.cart_detail cd " +
                "JOIN fivepigs.software s ON s.software_id = cd.software_id " +
                "WHERE cd.cart_id = ?";

        List<Software> items = new ArrayList<>();
        try (PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, cartId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Software sw = new Software();
                    sw.setSoftwareId(rs.getInt("software_id"));
                    sw.setName(rs.getString("name"));
                    sw.setPrice(rs.getDouble("price"));
                    sw.setIsFree(rs.getInt("is_free"));
                    items.add(sw);
                }
            }
        }
        return items;
    }

    private void ensureOrderCommissionColumn(Connection c) throws SQLException {
        try (PreparedStatement st = c.prepareStatement(
                "ALTER TABLE fivepigs.orders ADD COLUMN commission_rate DECIMAL(5,4) NULL")) {
            st.executeUpdate();
        } catch (SQLException e) {
            String state = e.getSQLState();
            int code = e.getErrorCode();
            boolean duplicateColumn = "42S21".equals(state) || code == 1060;
            if (!duplicateColumn) {
                throw e;
            }
        }
    }

    private double getCommissionRateSnapshot(Connection c) throws SQLException {
        try (PreparedStatement st = c.prepareStatement(
                "SELECT config_value FROM fivepigs.system_config WHERE config_key = 'commission_percent' LIMIT 1")) {
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    String raw = rs.getString("config_value");
                    if (raw != null && !raw.isBlank()) {
                        try {
                            double percent = Double.parseDouble(raw.trim());
                            if (percent < 0) {
                                percent = 0;
                            }
                            if (percent > 20) {
                                percent = 20;
                            }
                            return percent / 100.0;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            }
        } catch (SQLException e) {
            String state = e.getSQLState();
            int code = e.getErrorCode();
            boolean tableMissing = "42S02".equals(state) || code == 1146;
            if (!tableMissing) {
                throw e;
            }
        }
        return 0.10;
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
}
