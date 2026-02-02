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
public class License {
//    license_id INT PRIMARY KEY IDENTITY(1,1),
//license_key VARCHAR(100) UNIQUE,
//software_id INT,
//customer_id INT,
//purchase_date DATETIME,
//expire_date DATETIME,
//status VARCHAR(20),
//-- ACTIVE, EXPIRED, REVOKED
//
//FOREIGN KEY (software_id) REFERENCES Software(software_id),
//FOREIGN KEY (customer_id) REFERENCES Users(user_id)
    private Integer licenseId;
    private String licenseKey;
    private Integer softwareId;
    private Integer customerId;
    private LocalDateTime purchaseDate;
    private LocalDateTime expireDate;
    private String status;

    public License() {
    }

    public License(Integer licenseId, String licenseKey, Integer softwareId, Integer customerId, LocalDateTime purchaseDate, LocalDateTime expireDate, String status) {
        this.licenseId = licenseId;
        this.licenseKey = licenseKey;
        this.softwareId = softwareId;
        this.customerId = customerId;
        this.purchaseDate = purchaseDate;
        this.expireDate = expireDate;
        this.status = status;
    }

    public Integer getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(Integer licenseId) {
        this.licenseId = licenseId;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public Integer getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(Integer softwareId) {
        this.softwareId = softwareId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}
