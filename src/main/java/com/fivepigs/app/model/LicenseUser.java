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
public class LicenseUser {
//    CREATE TABLE License_User (
//    license_user_id INT AUTO_INCREMENT PRIMARY KEY,
//    license_id INT,
//    user_id INT,
//    assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP,
//    status VARCHAR(20) DEFAULT 'ACTIVE',
//    UNIQUE KEY uq_license_user (license_id, user_id),
//    FOREIGN KEY (license_id) REFERENCES License(license_id) ON DELETE CASCADE,
//    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
//);
    
    private Integer licenseUserId;
    private Integer licenseId;
    private Integer userId;
    private LocalDateTime assignedAt;
    private String status;

    public Integer getLicenseUserId() {
        return licenseUserId;
    }

    public void setLicenseUserId(Integer licenseUserId) {
        this.licenseUserId = licenseUserId;
    }

    public Integer getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(Integer licenseId) {
        this.licenseId = licenseId;
    }

   

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
