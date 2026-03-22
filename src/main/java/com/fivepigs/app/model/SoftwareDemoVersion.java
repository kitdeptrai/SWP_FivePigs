package com.fivepigs.app.model;

import java.time.LocalDateTime;

public class SoftwareDemoVersion {

    private Integer demoVersionId;
    private Integer softwareId;
    private String versionName;
    private String demoFileUrl;
    private String releaseNote;
    private Integer fileSize;
    private LocalDateTime createdAt;
    private Integer isActive;

    public SoftwareDemoVersion() {
    }

    public Integer getDemoVersionId() {
        return demoVersionId;
    }

    public void setDemoVersionId(Integer demoVersionId) {
        this.demoVersionId = demoVersionId;
    }

    public Integer getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(Integer softwareId) {
        this.softwareId = softwareId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDemoFileUrl() {
        return demoFileUrl;
    }

    public void setDemoFileUrl(String demoFileUrl) {
        this.demoFileUrl = demoFileUrl;
    }

    public String getReleaseNote() {
        return releaseNote;
    }

    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
}
