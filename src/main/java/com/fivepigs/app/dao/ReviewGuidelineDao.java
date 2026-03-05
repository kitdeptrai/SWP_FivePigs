/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.ReviewGuideline;
import com.fivepigs.app.model.ReviewGuidelineItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

            // keyword binds (4)
            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);
            ps.setString(4, keyword);

            // category binds (3)
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
            while (rs.next()) categories.add(rs.getString(1));
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
    
   // ===== 4bis) Items by guideline for JSON =====
public List<ReviewGuidelineItem> getItemsByGuidelineId(int guidelineId) {
    List<ReviewGuidelineItem> items = new ArrayList<>();
    String sql = "SELECT item_id, guideline_id, item_text, sort_order, created_at " +
                 "FROM Review_Guideline_Item " +
                 "WHERE guideline_id=? " +
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

// ===== 5) Create guideline + items (transaction) =====
public int createGuideline(ReviewGuideline g, List<String> itemTexts) {
    String insertGuideline =
        "INSERT INTO Review_Guideline(category, priority, title, description, icon, color, created_by) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    String insertItem =
        "INSERT INTO Review_Guideline_Item(guideline_id, item_text, sort_order) VALUES (?, ?, ?)";

    try (Connection con = Db.getConnection()) {
        con.setAutoCommit(false);

        int newId;
        try (PreparedStatement ps = con.prepareStatement(insertGuideline, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, g.getCategory());
            ps.setString(2, g.getPriority());
            ps.setString(3, g.getTitle());
            ps.setString(4, g.getDescription());
            ps.setString(5, g.getIcon());
            ps.setString(6, g.getColor());

            if (g.getCreatedBy() == null) ps.setNull(7, Types.INTEGER);
            else ps.setInt(7, g.getCreatedBy());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) throw new RuntimeException("Cannot get generated guideline_id");
                newId = rs.getInt(1);
            }
        }

        // insert items
        if (itemTexts != null && !itemTexts.isEmpty()) {
            try (PreparedStatement psItem = con.prepareStatement(insertItem)) {
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
        }

        con.commit();
        return newId;

    } catch (Exception e) {
        e.printStackTrace();
    }

    return -1;
}
    // ===== 4) Items by guideline =====
    public List<ReviewGuidelineItem> getItems(int guidelineId) {
        List<ReviewGuidelineItem> items = new ArrayList<>();
        String sql = "SELECT * FROM Review_Guideline_Item WHERE guideline_id=? ORDER BY sort_order ASC, item_id ASC";
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
    
    // ===== Delete guideline  =====
public boolean deleteGuideline(int guidelineId) {
    String sql = "DELETE FROM Review_Guideline WHERE guideline_id=?";
    try (Connection con = Db.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, guidelineId);
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}
    
    
    

    
     
    
    // ===== Simple stats class =====
    public static class GuidelineStats {
        public int criticalCount;
        public int highCount;
        public int totalItems;
        public int categoryCount;

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

