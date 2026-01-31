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
    private int softwareId,vendorId,categoryId,is_free;
    private String name,shortDescription,status,downloadCount;
    private double price,avg_rating,revenue;
    private Date createdTime;
    
    public Software() {
    }

    public Software(int vendorId,String name, String status, String downloadCount, double avg_rating, double revenue) {
        this.vendorId=vendorId;
        this.name = name;
        this.status = status;
        this.downloadCount = downloadCount;
        this.avg_rating = avg_rating;
        this.revenue = revenue;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
    
    
    public Software(int softwareId, int vendorId, int categoryId, int is_free, String name, String shortDescription, String status, String downloadCount, double price, double avg_rating, Date createdTime) {
        this.softwareId = softwareId;
        this.vendorId = vendorId;
        this.categoryId = categoryId;
        this.is_free = is_free;
        this.name = name;
        this.shortDescription = shortDescription;
        this.status = status;
        this.downloadCount = downloadCount;
        this.price = price;
        this.avg_rating = avg_rating;
        this.createdTime = createdTime;
    }

    public int getSoftwareId() {
        return softwareId;
    }
    
    public void setSoftwareId(int softwareId) {
        this.softwareId = softwareId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getIs_free() {
        return is_free;
    }

    public void setIs_free(int is_free) {
        this.is_free = is_free;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(String downloadCount) {
        this.downloadCount = downloadCount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(double avg_rating) {
        this.avg_rating = avg_rating;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
    
    
}
