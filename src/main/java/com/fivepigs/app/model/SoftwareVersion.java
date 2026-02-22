/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.model;

import java.time.LocalDateTime;

/**
 *
 * @author MinhPD
 */
public class SoftwareVersion {
//    CREATE TABLE Software_Version (
//    version_id INT AUTO_INCREMENT PRIMARY KEY,
//    software_id INT NOT NULL,
//    version_name VARCHAR(50),      -- ví dụ 1.0.0
//    file_url VARCHAR(255),         -- link file zip/app
//    release_note LONGTEXT,
//    file_size BIGINT,
//    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
//    is_active TINYINT(1) DEFAULT 1,
//    FOREIGN KEY (software_id) REFERENCES Software(software_id)
//        ON DELETE CASCADE
//);
    
    private Integer versionId;
    private Integer softwareId;
    private String versionName;
    private String fileUrl;
    private String releaseNote;
    private Integer fileSize;
    private LocalDateTime createdAt;
    private Integer isActive;

    public SoftwareVersion() {
    }

    public SoftwareVersion(Integer versionId, Integer softwareId, String versionName, String fileUrl, String releaseNote, Integer fileSize, LocalDateTime createdAt, Integer isActive) {
        this.versionId = versionId;
        this.softwareId = softwareId;
        this.versionName = versionName;
        this.fileUrl = fileUrl;
        this.releaseNote = releaseNote;
        this.fileSize = fileSize;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
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
