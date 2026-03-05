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
public class Review {
//    review_id INT AUTO_INCREMENT PRIMARY KEY,
//    software_id INT,
//    customer_id INT,
//    rating INT,
//    comment LONGTEXT,
//    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
//    FOREIGN KEY (software_id) REFERENCES Software(software_id),
//    FOREIGN KEY (customer_id) REFERENCES Users(user_id)
    
    private Integer reviewId;
    private Integer softwareId;
    private Integer customerId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private User user;

    public Review() {
    }

    public Review(Integer reviewId, Integer softwareId, Integer customerId, Integer rating, String comment, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.softwareId = softwareId;
        this.customerId = customerId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
}
