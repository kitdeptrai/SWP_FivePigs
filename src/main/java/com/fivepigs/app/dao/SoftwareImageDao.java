/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.SoftwareImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MinhPD
 */
public class SoftwareImageDao {

    public List<SoftwareImage> getImagesBySoftwareId(int softwareId) throws SQLException {
        List<SoftwareImage> list = new ArrayList<>();

        String sql = "SELECT * FROM Software_Image WHERE software_id = ? ORDER BY is_thumbnail DESC, created_at ASC";

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, softwareId);
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    SoftwareImage img = new SoftwareImage();
                    img.setImageId(rs.getInt("image_id"));
                    img.setImageUrl(rs.getString("image_url"));
                    img.setIsThumbnail(rs.getInt("is_thumbnail"));
                    list.add(img);
                }

            }
        }

        return list;
    }
}
