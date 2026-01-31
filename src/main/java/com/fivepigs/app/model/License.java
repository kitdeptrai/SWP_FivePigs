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
    private int licenseId,softwareId,customerId;
    private String licenseKey,status;
    private Date purchaseDate,expireDate;

    public License() {
    }

    public License(int licenseId, int softwareId, int customerId, String licenseKey, String status, Date purchaseDate, Date expireDate) {
        this.licenseId = licenseId;
        this.softwareId = softwareId;
        this.customerId = customerId;
        this.licenseKey = licenseKey;
        this.status = status;
        this.purchaseDate = purchaseDate;
        this.expireDate = expireDate;
    }

    public int getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(int licenseId) {
        this.licenseId = licenseId;
    }

    public int getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(int softwareId) {
        this.softwareId = softwareId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
    
    
}
