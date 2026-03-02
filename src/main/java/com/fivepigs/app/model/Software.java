/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.model;

import java.sql.Date;
import java.sql.Timestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author thanh
 */
public class Software {
//        software_id INT IDENTITY(1,1) PRIMARY KEY,
//    name NVARCHAR(150) NOT NULL,
//    short_description NVARCHAR(255),
//    vendor_id INT NOT NULL,
//    category_id INT,
//    price DECIMAL(10,2) DEFAULT 0,
//    is_free BIT DEFAULT 0,
//    status VARCHAR(30) DEFAULT 'PENDING_REVIEW',
//    -- PENDING_REVIEW / REVIEWED / APPROVED / REJECTED
//    download_count INT DEFAULT 0,
//    avg_rating DECIMAL(2,1) DEFAULT 0,
//    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    private Integer softwareId;
    private String name;
    private String short_description;
    private Integer vendorId;
    private Integer categoryId;
    private Double price;
    private Integer isFree;
    private String status;
    private Integer downloadCount;
    private Double avgRating;
    private LocalDateTime createdAt;
    private Double revenue;
    private String appName;
    private User user;
    private Category category;
    private SoftwareImage softwareImage;
    private ApprovalProcess approvalProcess;
    private ReviewerProcess reviewerProcess;
    private SoftwareVersion softwareVersion;
    private SoftwareDetail softwareDetail;

    private Date reviewDate;
    private String recommendation;
    private String categoryName;
    private String version;
    private String language;
    
    private String imageUrl;
     private Double qualityScore;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(Double qualityScore) {
        this.qualityScore = qualityScore;
    }
    
    
    
    public String getVersion() {
    return version;
}

public void setVersion(String version) {
    this.version = version;
}

public String getLanguage() {
    return language;
}

public void setLanguage(String language) {
    this.language = language;
}
   
public String getCategoryName() {
    return categoryName;
}

public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
}



  
    public Software() {
    }

    public Software(Integer softwareId, String name, String short_description, Integer vendorId, Integer categoryId, Double price, Integer isFree, String status, Integer downloadCount, Double avgRating, LocalDateTime createdAt, Double revenue, String appName, ApprovalProcess ap, ReviewerProcess rp) {
        this.softwareId = softwareId;
        this.name = name;
        this.short_description = short_description;
        this.vendorId = vendorId;
        this.categoryId = categoryId;
        this.price = price;
        this.isFree = isFree;
        this.status = status;
        this.downloadCount = downloadCount;
        this.avgRating = avgRating;
        this.createdAt = createdAt;
        this.revenue = revenue;
        this.appName = appName;
        this.approvalProcess = ap;
        this.reviewerProcess = rp;
    }

    public SoftwareDetail getSoftwareDetail() {
        return softwareDetail;
    }

    public void setSoftwareDetail(SoftwareDetail softwareDetail) {
        this.softwareDetail = softwareDetail;
    }

    public SoftwareVersion getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(SoftwareVersion softwareVersion) {
        this.softwareVersion = softwareVersion;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    

    public SoftwareImage getSoftwareImage() {
        return softwareImage;
    }

    public void setSoftwareImage(SoftwareImage softwareImage) {
        this.softwareImage = softwareImage;
    }

    
    
    public ApprovalProcess getApprovalProcess() {
        return approvalProcess;
    }

    public void setApprovalProcess(ApprovalProcess ap) {
        this.approvalProcess = ap;
    }

    public ReviewerProcess getReviewerProcess() {
        return reviewerProcess;
    }

    public void setReviewerProcess(ReviewerProcess rp) {
        this.reviewerProcess = rp;
    }
    
    public Integer getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(Integer softwareId) {
        this.softwareId = softwareId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getIsFree() {
        return isFree;
    }

    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }


   
public String getFormattedCreatedAt() {
    if (createdAt == null) return "";
    DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return createdAt.format(formatter);
}


}
