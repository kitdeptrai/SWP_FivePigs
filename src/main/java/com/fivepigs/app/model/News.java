package com.fivepigs.app.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class News {
    private static final DateTimeFormatter DATE_LABEL_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private Integer newsId;
    private Integer softwareId;
    private String title;
    private String summary;
    private String content;
    private String newsType;
    private String coverImage;
    private LocalDateTime publishedAt;
    private String softwareName;
    private String softwareIconUrl;

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public Integer getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(Integer softwareId) {
        this.softwareId = softwareId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getPublishedDateLabel() {
        return publishedAt == null ? "" : publishedAt.format(DATE_LABEL_FORMAT);
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    public String getSoftwareIconUrl() {
        return softwareIconUrl;
    }

    public void setSoftwareIconUrl(String softwareIconUrl) {
        this.softwareIconUrl = softwareIconUrl;
    }
}
