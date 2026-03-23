/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.model;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private Integer ownerId;
    private Integer maxUsers;
    private Integer assignedCount;
    private String planName;
    private LocalDateTime purchaseDate;
    private LocalDateTime expireDate;
    private String status;
    private User user;
    private Software software;
    private List<User> assignedUsers = new ArrayList<>();

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

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
    }

    public Integer getAssignedCount() {
        return assignedCount;
    }

    public void setAssignedCount(Integer assignedCount) {
        this.assignedCount = assignedCount;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }

    public List<User> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(List<User> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    
}
