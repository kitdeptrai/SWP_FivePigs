/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.model;

import java.time.LocalDateTime;

/**
 *
 * @author Admin
 */
public class Software {
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
    public Software() {
    }

    public Software(Integer vendorId,String name, String status, Integer downloadCount,Double avg_rating, Double revenue) {
        this.vendorId=vendorId;
        this.name = name;
        this.status = status;
        this.downloadCount = downloadCount;
        this.avgRating = avgRating;
        this.revenue = revenue;
    }

    public Software(Integer softwareId, String name, String short_description, Integer vendorId, Integer categoryId, Double price, Integer isFree, String status, Integer downloadCount, Double avgRating, LocalDateTime createdAt) {
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

}
