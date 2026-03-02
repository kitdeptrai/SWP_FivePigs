/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.model;

import java.sql.Timestamp;

/**
 *
 * @author Admin
 */
public class ReviewHistoryDTO {

    private int reviewScoreId;
    private int softwareId;

    private String softwareName;
    private String version;
    private String imageUrl;

    private int uiScore;
    private int technicalScore;
    private int performanceScore;
    private int documentationScore;

    private double totalScore;
    private String decision;

    private Timestamp createdAt;

    // getter & setter

    public int getReviewScoreId() {
        return reviewScoreId;
    }

    public void setReviewScoreId(int reviewScoreId) {
        this.reviewScoreId = reviewScoreId;
    }

    public int getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(int softwareId) {
        this.softwareId = softwareId;
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getUiScore() {
        return uiScore;
    }

    public void setUiScore(int uiScore) {
        this.uiScore = uiScore;
    }

    public int getTechnicalScore() {
        return technicalScore;
    }

    public void setTechnicalScore(int technicalScore) {
        this.technicalScore = technicalScore;
    }

    public int getPerformanceScore() {
        return performanceScore;
    }

    public void setPerformanceScore(int performanceScore) {
        this.performanceScore = performanceScore;
    }

    public int getDocumentationScore() {
        return documentationScore;
    }

    public void setDocumentationScore(int documentationScore) {
        this.documentationScore = documentationScore;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
}