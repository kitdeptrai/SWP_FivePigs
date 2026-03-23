package com.fivepigs.app.model;

import java.time.LocalDateTime;

public class Report {

    private Integer reportId;
    private Integer softwareId;
    private Integer reporterId;
    private Integer reviewerId;
    private String softwareName;
    private String reporterName;
    private String reason;
    private String status;
    private Boolean bugConfirmed;
    private String reviewerNote;
    private String reproduceSteps;
    private String fileUrl;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(Integer softwareId) {
        this.softwareId = softwareId;
    }

    public Integer getReporterId() {
        return reporterId;
    }

    public void setReporterId(Integer reporterId) {
        this.reporterId = reporterId;
    }

    public Integer getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Integer reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getBugConfirmed() {
        return bugConfirmed;
    }

    public void setBugConfirmed(Boolean bugConfirmed) {
        this.bugConfirmed = bugConfirmed;
    }

    public String getReviewerNote() {
        return reviewerNote;
    }

    public void setReviewerNote(String reviewerNote) {
        this.reviewerNote = reviewerNote;
    }

    public String getReproduceSteps() {
        return reproduceSteps;
    }

    public void setReproduceSteps(String reproduceSteps) {
        this.reproduceSteps = reproduceSteps;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
}
