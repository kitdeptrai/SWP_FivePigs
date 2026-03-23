package com.fivepigs.app.dao;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.News;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class NewsDao {

    public List<News> getPublishedNews(String type, int limit) throws SQLException {
            List<News> dbNews = getPublishedNewsFromTable(type, limit);
            if (!dbNews.isEmpty()) {
                return dbNews;
            }
        return getFallbackNews(type, limit);
    }

    private List<News> getPublishedNewsFromTable(String type, int limit) throws SQLException {
        StringBuilder sql = new StringBuilder("""
                SELECT n.news_id,
                       n.software_id,
                       n.title,
                       n.summary,
                       n.content,
                       n.news_type,
                       n.cover_image,
                       n.published_at,
                       s.name AS software_name,
                       img.image_url AS software_icon
                FROM news n
                LEFT JOIN software s ON s.software_id = n.software_id
                LEFT JOIN software_image img
                       ON img.software_id = s.software_id
                      AND img.is_thumbnail = 1
                WHERE n.is_published = 1
                """);
        if (type != null && !type.isBlank()) {
            sql.append(" AND UPPER(n.news_type) = ? ");
        }
        sql.append(" ORDER BY n.published_at DESC, n.news_id DESC LIMIT ? ");

        List<News> list = new ArrayList<>();
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            int idx = 1;
            if (type != null && !type.isBlank()) {
                ps.setString(idx++, type.trim().toUpperCase());
            }
            ps.setInt(idx, Math.max(1, limit));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapNewsRow(rs));
                }
            }
        }
        return list;
    }

    private List<News> getFallbackNews(String type, int limit) throws SQLException {
        String sql = """
                SELECT s.software_id,
                       s.name,
                       s.short_description,
                       s.download_count,
                       s.avg_rating,
                       s.created_at,
                       c.category_name,
                       sd.release_note,
                       sv.version_name,
                       img.image_url AS icon_url
                FROM software s
                LEFT JOIN category c ON c.category_id = s.category_id
                LEFT JOIN software_detail sd ON sd.software_id = s.software_id
                LEFT JOIN software_version sv
                       ON sv.software_id = s.software_id
                      AND sv.is_active = 1
                LEFT JOIN software_image img
                       ON img.software_id = s.software_id
                      AND img.is_thumbnail = 1
                WHERE UPPER(COALESCE(s.status, 'APPROVED')) = 'APPROVED'
                ORDER BY s.created_at DESC, s.software_id DESC
                LIMIT ?
                """;

        List<News> list = new ArrayList<>();
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, Math.max(1, limit * 2));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    News news = new News();
                    int softwareId = rs.getInt("software_id");
                    String name = rs.getString("name");
                    String releaseNote = rs.getString("release_note");
                    String versionName = rs.getString("version_name");
                    String categoryName = rs.getString("category_name");
                    String shortDescription = rs.getString("short_description");
                    String normalizedType = deriveFallbackType(releaseNote, versionName);

                    if (type != null && !type.isBlank() && !type.equalsIgnoreCase(normalizedType)) {
                        continue;
                    }

                    news.setNewsId(softwareId);
                    news.setSoftwareId(softwareId);
                    news.setSoftwareName(name);
                    news.setSoftwareIconUrl(rs.getString("icon_url"));
                    news.setCoverImage(rs.getString("icon_url"));
                    news.setNewsType(normalizedType);
                    news.setTitle(buildFallbackTitle(name, normalizedType, versionName));
                    news.setSummary(buildFallbackSummary(shortDescription, categoryName, normalizedType));
                    news.setContent(buildFallbackContent(releaseNote, shortDescription, categoryName, rs.getInt("download_count"), rs.getDouble("avg_rating")));

                    Timestamp publishedAt = rs.getTimestamp("created_at");
                    if (publishedAt != null) {
                        news.setPublishedAt(publishedAt.toLocalDateTime());
                    }

                    list.add(news);
                    if (list.size() >= Math.max(1, limit)) {
                        break;
                    }
                }
            }
        }
        return list;
    }

    private News mapNewsRow(ResultSet rs) throws SQLException {
        News news = new News();
        news.setNewsId(rs.getInt("news_id"));
        int softwareId = rs.getInt("software_id");
        news.setSoftwareId(rs.wasNull() ? null : softwareId);
        news.setTitle(rs.getString("title"));
        news.setSummary(rs.getString("summary"));
        news.setContent(rs.getString("content"));
        news.setNewsType(rs.getString("news_type"));
        news.setCoverImage(rs.getString("cover_image"));
        news.setSoftwareName(rs.getString("software_name"));
        news.setSoftwareIconUrl(rs.getString("software_icon"));
        Timestamp publishedAt = rs.getTimestamp("published_at");
        if (publishedAt != null) {
            news.setPublishedAt(publishedAt.toLocalDateTime());
        }
        return news;
    }

    private String deriveFallbackType(String releaseNote, String versionName) {
        if ((releaseNote != null && !releaseNote.isBlank()) || (versionName != null && !versionName.isBlank())) {
            return "UPDATE";
        }
        return "NEW_RELEASE";
    }

    private String buildFallbackTitle(String name, String type, String versionName) {
        if ("UPDATE".equalsIgnoreCase(type) && versionName != null && !versionName.isBlank()) {
            return name + " received update " + versionName;
        }
        if ("SALE".equalsIgnoreCase(type)) {
            return name + " is on sale now";
        }
        return "Now on FIVEPIGS: " + name;
    }

    private String buildFallbackSummary(String shortDescription, String categoryName, String type) {
        if (shortDescription != null && !shortDescription.isBlank()) {
            return shortDescription;
        }
        if ("UPDATE".equalsIgnoreCase(type)) {
            return "A new build is now available on FIVEPIGS.";
        }
        if (categoryName != null && !categoryName.isBlank()) {
            return "Fresh " + categoryName + " content is now available in the store.";
        }
        return "Discover the latest apps and games now live on FIVEPIGS.";
    }

    private String buildFallbackContent(String releaseNote,
                                        String shortDescription,
                                        String categoryName,
                                        int downloadCount,
                                        double avgRating) {
        if (releaseNote != null && !releaseNote.isBlank()) {
            return releaseNote;
        }
        StringBuilder content = new StringBuilder();
        if (shortDescription != null && !shortDescription.isBlank()) {
            content.append(shortDescription.trim());
        }
        if (categoryName != null && !categoryName.isBlank()) {
            appendSentence(content, "Category: " + categoryName + ".");
        }
        if (downloadCount > 0) {
            appendSentence(content, "Downloads: " + downloadCount + ".");
        }
        if (avgRating > 0) {
            appendSentence(content, "Average rating: " + avgRating + ".");
        }
        if (content.length() == 0) {
            content.append("More details will be available soon on the FIVEPIGS store page.");
        }
        return content.toString();
    }

    private void appendSentence(StringBuilder builder, String sentence) {
        if (builder.length() > 0) {
            builder.append(' ');
        }
        builder.append(sentence);
    }
}
