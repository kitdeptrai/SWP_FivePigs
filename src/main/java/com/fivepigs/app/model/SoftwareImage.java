/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.model;

import java.sql.Date;

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
    
    private int imageId,softwareId,imageUrl;
    private int isThumbnail;
    private Date createdTime;

    public SoftwareImage() {
    }

    public SoftwareImage(int imageId, int softwareId, int imageUrl, int isThumbnail, Date createdTime) {
        this.imageId = imageId;
        this.softwareId = softwareId;
        this.imageUrl = imageUrl;
        this.isThumbnail = isThumbnail;
        this.createdTime = createdTime;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(int softwareId) {
        this.softwareId = softwareId;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getIsThumbnail() {
        return isThumbnail;
    }

    public void setIsThumbnail(int isThumbnail) {
        this.isThumbnail = isThumbnail;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
    
    
}
