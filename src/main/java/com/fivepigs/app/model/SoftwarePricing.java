
package com.fivepigs.app.model;

import java.time.LocalDateTime;


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
    

    private Integer pricingId;
    private Integer softwareId;
    private String planName;
    private Integer maxUsers;
    private Double price;

    private Integer durationDays;
    private Integer isActive;
    private LocalDateTime createdAt;

    


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


    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }


    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

 
    

}
