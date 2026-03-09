package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.AboutMedia;
import java.sql.*;
import java.util.*;

public class AboutMediaDao {
    private static final String SQL =
            "SELECT media_id, title, image_url, media_type, sort_order " +
                    "FROM about_media ORDER BY sort_order ASC, media_id ASC";

    public List<AboutMedia> getAll() throws SQLException {
        List<AboutMedia> list = new ArrayList<>();
        try (Connection c = Db.getConnection();
             PreparedStatement st = c.prepareStatement(SQL);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                AboutMedia m = new AboutMedia();
                m.setMediaId(rs.getInt("media_id"));
                m.setTitle(rs.getString("title"));
                m.setImageUrl(rs.getString("image_url"));
                m.setMediaType(rs.getString("media_type"));
                m.setSortOrder(rs.getInt("sort_order"));
                list.add(m);
            }
        }
        return list;
    }
}