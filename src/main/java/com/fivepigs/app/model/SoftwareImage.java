/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.model;

import java.sql.Date;
import java.time.LocalDateTime;

/**
 *
 * @author MinhPD
 */
public class SoftwareImage {
//    CREATE TABLE Software_Image (
//    image_id INT AUTO_INCREMENT PRIMARY KEY,
//    software_id INT NOT NULL,
//    image_url NVARCHAR(255) NOT NULL,
//    is_thumbnail TINYINT(1) DEFAULT 0,
//    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
//    FOREIGN KEY (software_id) REFERENCES Software(software_id) ON DELETE CASCADE
//);
    
    private Integer imageId;
    private Integer softwareId;
    private String imageUrl;
    private Integer isThumbnail;
    private LocalDateTime createdTime;

    public SoftwareImage() {
    }

    public SoftwareImage(Integer imageId, Integer softwareId, String imageUrl, Integer isThumbnail, LocalDateTime createdTime) {
        this.imageId = imageId;
        this.softwareId = softwareId;
        this.imageUrl = imageUrl;
        this.isThumbnail = isThumbnail;
        this.createdTime = createdTime;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(Integer softwareId) {
        this.softwareId = softwareId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getIsThumbnail() {
        return isThumbnail;
    }

    public void setIsThumbnail(Integer isThumbnail) {
        this.isThumbnail = isThumbnail;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    
}
