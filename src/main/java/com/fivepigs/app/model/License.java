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
//CREATE TABLE License (
//    license_id INT AUTO_INCREMENT PRIMARY KEY,
//    license_key VARCHAR(100) UNIQUE,
//	pricing_id INT,
//    software_id INT,
//    owner_id INT, -- người mua license 
//    max_users INT DEFAULT 1, -- giới hạn user
//    purchase_date DATETIME,
//    expire_date DATETIME,
//    status VARCHAR(20),
//	FOREIGN KEY (pricing_id) REFERENCES Software_Pricing(pricing_id),
//    FOREIGN KEY (software_id) REFERENCES Software(software_id),
//    FOREIGN KEY (owner_id) REFERENCES Users(user_id)
//);

    private Integer licenseId;
    private String licenseKey;
    private Integer softwareId;
    private Integer pricingId;
    private Integer maxUsers;
    private Integer customerId;
    private LocalDateTime purchaseDate;
    private LocalDateTime expireDate;
    private String status;
    private String ownerId;
    private Software software;
    private User user;
    private Integer usedUsers;
    private SoftwarePricing softwarePricing;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getUsedUsers() {
        return usedUsers;
    }

    public void setUsedUsers(Integer usedUsers) {
        this.usedUsers = usedUsers;
    }

    public SoftwarePricing getSoftwarePricing() {
        return softwarePricing;
    }

    public void setSoftwarePricing(SoftwarePricing softwarePricing) {
        this.softwarePricing = softwarePricing;
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

    public Integer getPricingId() {
        return pricingId;
    }

    public void setPricingId(Integer pricingId) {
        this.pricingId = pricingId;
    }

    public Integer getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }

}
