package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.ReviewGuideline;
import com.fivepigs.app.model.ReviewGuidelineItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewGuidelineDao {

    // ===== 1) List guidelines with search + filter category =====
    public List<ReviewGuideline> findAll(String keyword, String category) {
        List<ReviewGuideline> list = new ArrayList<>();

        String sql =
                "SELECT g.*, " +
                " (SELECT COUNT(*) FROM Review_Guideline_Item i WHERE i.guideline_id = g.guideline_id) AS item_count " +
                "FROM Review_Guideline g " +
                "WHERE (? IS NULL OR ? = '' OR g.title LIKE CONCAT('%', ?, '%') OR g.description LIKE CONCAT('%', ?, '%')) " +
                "  AND (? IS NULL OR ? = '' OR g.category = ?) " +
                "ORDER BY g.updated_at DESC";

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);
            ps.setString(4, keyword);

            ps.setString(5, category);
            ps.setString(6, category);
            ps.setString(7, category);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReviewGuideline g = new ReviewGuideline();
                    g.setGuidelineId(rs.getInt("guideline_id"));
                    g.setCategory(rs.getString("category"));
                    g.setPriority(rs.getString("priority"));
                    g.setTitle(rs.getString("title"));
                    g.setDescription(rs.getString("description"));
                    g.setIcon(rs.getString("icon"));
                    g.setColor(rs.getString("color"));

                    int createdBy = rs.getInt("created_by");
                    g.setCreatedBy(rs.wasNull() ? null : createdBy);

                    g.setCreatedAt(rs.getTimestamp("created_at"));
                    g.setUpdatedAt(rs.getTimestamp("updated_at"));
                    g.setItemCount(rs.getInt("item_count"));

                    list.add(g);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ===== 2) Get categories for dropdown =====
    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM Review_Guideline ORDER BY category";

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
    }

    // ===== 3) Stats for top cards =====
    public GuidelineStats getStats(String category) {
        String sql =
                "SELECT " +
                "  SUM(CASE WHEN priority='Critical' THEN 1 ELSE 0 END) AS critical_count, " +
                "  SUM(CASE WHEN priority='High' THEN 1 ELSE 0 END) AS high_count, " +
                "  COUNT(*) AS total_items, " +
                "  COUNT(DISTINCT category) AS category_count " +
                "FROM Review_Guideline " +
                "WHERE (? IS NULL OR ? = '' OR category = ?)";

        GuidelineStats st = new GuidelineStats();

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, category);
            ps.setString(2, category);
            ps.setString(3, category);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    st.setCriticalCount(rs.getInt("critical_count"));
                    st.setHighCount(rs.getInt("high_count"));
                    st.setTotalItems(rs.getInt("total_items"));
                    st.setCategoryCount(rs.getInt("category_count"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return st;
    }

    // ===== 4) Items by guideline for JSON / edit modal =====
    public List<ReviewGuidelineItem> getItemsByGuidelineId(int guidelineId) {
        List<ReviewGuidelineItem> items = new ArrayList<>();

        String sql = "SELECT item_id, guideline_id, item_text, sort_order, created_at " +
                     "FROM Review_Guideline_Item " +
                     "WHERE guideline_id = ? " +
                     "ORDER BY sort_order ASC, item_id ASC";

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, guidelineId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReviewGuidelineItem it = new ReviewGuidelineItem();
                    it.setItemId(rs.getInt("item_id"));
                    it.setGuidelineId(rs.getInt("guideline_id"));
                    it.setItemText(rs.getString("item_text"));
                    it.setSortOrder(rs.getInt("sort_order"));
                    it.setCreatedAt(rs.getTimestamp("created_at"));
                    items.add(it);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    // ===== 5) Alias method if old code still calls getItems(...) =====
    public List<ReviewGuidelineItem> getItems(int guidelineId) {
        return getItemsByGuidelineId(guidelineId);
    }

    // ===== 6) Create guideline + items =====
    public int createGuideline(ReviewGuideline g, List<String> itemTexts) {
        String insertGuideline =
                "INSERT INTO Review_Guideline(category, priority, title, description, icon, color, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String insertItem =
                "INSERT INTO Review_Guideline_Item(guideline_id, item_text, sort_order) VALUES (?, ?, ?)";

        Connection con = null;
        PreparedStatement psGuideline = null;
        PreparedStatement psItem = null;
        ResultSet rs = null;

        try {
            con = Db.getConnection();
            con.setAutoCommit(false);

            int newId;

            psGuideline = con.prepareStatement(insertGuideline, Statement.RETURN_GENERATED_KEYS);
            psGuideline.setString(1, g.getCategory());
            psGuideline.setString(2, g.getPriority());
            psGuideline.setString(3, g.getTitle());
            psGuideline.setString(4, g.getDescription());
            psGuideline.setString(5, g.getIcon());
            psGuideline.setString(6, g.getColor());

            if (g.getCreatedBy() == null) {
                psGuideline.setNull(7, Types.INTEGER);
            } else {
                psGuideline.setInt(7, g.getCreatedBy());
            }

            psGuideline.executeUpdate();

            rs = psGuideline.getGeneratedKeys();
            if (!rs.next()) {
                throw new RuntimeException("Cannot get generated guideline_id");
            }
            newId = rs.getInt(1);

            if (itemTexts != null && !itemTexts.isEmpty()) {
                psItem = con.prepareStatement(insertItem);

                int order = 1;
                for (String t : itemTexts) {
                    if (t == null) continue;
                    t = t.trim();
                    if (t.isEmpty()) continue;

                    psItem.setInt(1, newId);
                    psItem.setString(2, t);
                    psItem.setInt(3, order++);
                    psItem.addBatch();
                }

                psItem.executeBatch();
            }

            con.commit();
            return newId;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (con != null) con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (psItem != null) psItem.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (psGuideline != null) psGuideline.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return -1;
    }

    // ===== 7) Update guideline + replace old items =====
    public boolean updateGuideline(ReviewGuideline g, List<String> itemTexts) {
        String updateGuidelineSql =
                "UPDATE Review_Guideline " +
                "SET category = ?, priority = ?, title = ?, description = ?, icon = ?, color = ? " +
                "WHERE guideline_id = ?";

        String deleteItemsSql =
                "DELETE FROM Review_Guideline_Item WHERE guideline_id = ?";

        String insertItemSql =
                "INSERT INTO Review_Guideline_Item(guideline_id, item_text, sort_order) VALUES (?, ?, ?)";

        Connection con = null;
        PreparedStatement psUpdate = null;
        PreparedStatement psDelete = null;
        PreparedStatement psInsert = null;

        try {
            con = Db.getConnection();
            con.setAutoCommit(false);

            psUpdate = con.prepareStatement(updateGuidelineSql);
            psUpdate.setString(1, g.getCategory());
            psUpdate.setString(2, g.getPriority());
            psUpdate.setString(3, g.getTitle());
            psUpdate.setString(4, g.getDescription());
            psUpdate.setString(5, g.getIcon());
            psUpdate.setString(6, g.getColor());
            psUpdate.setInt(7, g.getGuidelineId());

            int updated = psUpdate.executeUpdate();
            if (updated <= 0) {
                con.rollback();
                return false;
            }

            psDelete = con.prepareStatement(deleteItemsSql);
            psDelete.setInt(1, g.getGuidelineId());
            psDelete.executeUpdate();

            if (itemTexts != null && !itemTexts.isEmpty()) {
                psInsert = con.prepareStatement(insertItemSql);

                int sortOrder = 1;
                for (String text : itemTexts) {
                    if (text == null) continue;

                    text = text.trim();
                    if (text.isEmpty()) continue;

                    psInsert.setInt(1, g.getGuidelineId());
                    psInsert.setString(2, text);
                    psInsert.setInt(3, sortOrder++);
                    psInsert.addBatch();
                }

                psInsert.executeBatch();
            }

            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (con != null) con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            try { if (psInsert != null) psInsert.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (psDelete != null) psDelete.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (psUpdate != null) psUpdate.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return false;
    }

    // ===== 8) Delete guideline =====
    public boolean deleteGuideline(int guidelineId) {
        String sql = "DELETE FROM Review_Guideline WHERE guideline_id = ?";

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, guidelineId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ===== 9) Simple stats class =====
    public static class GuidelineStats {
        private int criticalCount;
        private int highCount;
        private int totalItems;
        private int categoryCount;

        public int getCriticalCount() {
            return criticalCount;
        }

        public void setCriticalCount(int criticalCount) {
            this.criticalCount = criticalCount;
        }

        public int getHighCount() {
            return highCount;
        }

        public void setHighCount(int highCount) {
            this.highCount = highCount;
        }

        public int getTotalItems() {
            return totalItems;
        }

        public void setTotalItems(int totalItems) {
            this.totalItems = totalItems;
        }

        public int getCategoryCount() {
            return categoryCount;
        }

        public void setCategoryCount(int categoryCount) {
            this.categoryCount = categoryCount;
        }
    }
}