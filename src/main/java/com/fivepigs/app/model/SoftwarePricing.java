<<<<<<< HEAD
=======
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
package com.fivepigs.app.model;

import java.time.LocalDateTime;

<<<<<<< HEAD
public class SoftwarePricing {
=======
/**
 *
 * @author MinhPD
 */
public class SoftwarePricing {
//    CREATE TABLE Software_Pricing (
//    pricing_id INT AUTO_INCREMENT PRIMARY KEY,
//    software_id INT NOT NULL,
//    plan_name VARCHAR(50), -- Basic / Team / Pro
//    max_users INT NOT NULL, -- 1 / 2 / 4 / 10
//    price DECIMAL(10,2) NOT NULL,
//    is_active TINYINT(1) DEFAULT 1,
//    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
//    
//    FOREIGN KEY (software_id) REFERENCES Software(software_id)
//);
    
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
    private Integer pricingId;
    private Integer softwareId;
    private String planName;
    private Integer maxUsers;
    private Double price;
<<<<<<< HEAD
    private Integer durationDays;
    private Integer isActive;
    private LocalDateTime createdAt;
=======
    private Integer isActive;
    private LocalDateTime createdDate;
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2

    public Integer getPricingId() {
        return pricingId;
    }

    public void setPricingId(Integer pricingId) {
        this.pricingId = pricingId;
    }

    public Integer getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(Integer softwareId) {
        this.softwareId = softwareId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Integer getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

<<<<<<< HEAD
    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

=======
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

<<<<<<< HEAD
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
=======
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    
>>>>>>> 297f69ecf976d61b608f7e7aa424e93bcd05b3f2
}
