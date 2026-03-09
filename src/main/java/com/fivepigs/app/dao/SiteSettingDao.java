package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import java.sql.*;
import java.util.*;

public class SiteSettingDao {
    private static final String SQL = "SELECT setting_key, setting_value FROM site_setting";

    public Map<String, String> getAllAsMap() throws SQLException {
        Map<String, String> map = new HashMap<>();
        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(SQL);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getString("setting_key"), rs.getString("setting_value"));
            }
        }
        return map;
    }
}