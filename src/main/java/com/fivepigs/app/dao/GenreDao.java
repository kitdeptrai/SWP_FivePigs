/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import java.sql.Connection;
import java.util.List;
import java.sql.SQLException;

import com.fivepigs.app.model.Genre;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author MinhPD
 */
public class GenreDao {

    public List<Genre> getAllGenre() throws SQLException {

        List<Genre> list = new ArrayList<>();
        String sql = "SELECT * FROM Genre";

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Genre g = new Genre();
                g.setGenreId(rs.getInt("genre_id"));
                g.setName(rs.getString("name"));
                list.add(g);
            }
        }

        return list;
    }

    public List<Genre> getGenresBySoftwareId(int softwareId) throws SQLException {
        List<Genre> list = new ArrayList<>();

        String sql = "SELECT g.genre_id, g.name "
                + "FROM Software_Genre sg "
                + "JOIN Genre g ON sg.genre_id = g.genre_id "
                + "WHERE sg.software_id = ?";

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, softwareId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Genre g = new Genre();
                g.setGenreId(rs.getInt("genre_id"));
                g.setName(rs.getString("name"));
                list.add(g);
            }
        }

        return list;
    }

    public void deleteSoftwareGenres(int softwareId) throws SQLException {

        String sql = "DELETE FROM Software_Genre WHERE software_id = ?";

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, softwareId);
            ps.executeUpdate();
        }
    }

    public void addSoftwareGenres(int softwareId, String[] genreIds) throws SQLException {

        String sql = """
        INSERT IGNORE INTO Software_Genre (software_id, genre_id)
        VALUES (?, ?)
    """;

        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            for (String gid : genreIds) {
                ps.setInt(1, softwareId);
                ps.setInt(2, Integer.parseInt(gid));
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

}
