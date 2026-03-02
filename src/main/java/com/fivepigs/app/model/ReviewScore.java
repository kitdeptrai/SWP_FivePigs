/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.model;

import java.util.Date;

public class ReviewScore {

    private int reviewScoreId;
    private int softwareId;
    private int reviewerId;

    private boolean noMalware;
    private boolean noCopyrightViolation;
    private boolean noSpamContent;

    private int uiUxScore;
    private int technicalScore;
    private int performanceScore;
    private int documentationScore;

    private double totalScore;
    private String decision;
    private String reviewNote;

    private Date createdAt;

    public ReviewScore() {
    }

    // Getter & Setter đầy đủ

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

    public int getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(int reviewerId) {
        this.reviewerId = reviewerId;
    }

    public boolean isNoMalware() {
        return noMalware;
    }

    public void setNoMalware(boolean noMalware) {
        this.noMalware = noMalware;
    }

    public boolean isNoCopyrightViolation() {
        return noCopyrightViolation;
    }

    public void setNoCopyrightViolation(boolean noCopyrightViolation) {
        this.noCopyrightViolation = noCopyrightViolation;
    }

    public boolean isNoSpamContent() {
        return noSpamContent;
    }

    public void setNoSpamContent(boolean noSpamContent) {
        this.noSpamContent = noSpamContent;
    }

    public int getUiUxScore() {
        return uiUxScore;
    }

    public void setUiUxScore(int uiUxScore) {
        this.uiUxScore = uiUxScore;
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

    public String getReviewNote() {
        return reviewNote;
    }

    public void setReviewNote(String reviewNote) {
        this.reviewNote = reviewNote;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}