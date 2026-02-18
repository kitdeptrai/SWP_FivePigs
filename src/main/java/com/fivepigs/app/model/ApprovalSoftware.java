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
public class ApprovalSoftware {
//    CREATE TABLE Software_Approval (
//    approval_id INT AUTO_INCREMENT PRIMARY KEY,
//    software_id INT UNIQUE NOT NULL,
//    approver_id INT NOT NULL,
//    decision VARCHAR(20),
//    approval_note LONGTEXT,
//    approval_date DATETIME DEFAULT CURRENT_TIMESTAMP,
//    FOREIGN KEY (software_id) REFERENCES Software(software_id),
//    FOREIGN KEY (approver_id) REFERENCES Users(user_id)
//);
    private Integer  approvalId;
    private Integer  softwareId;
    private Integer  approverId;
    
    private String decision;
    private String approvalNote;
    private LocalDateTime approvalDate;

    public ApprovalSoftware() {
    }

    public ApprovalSoftware(Integer approvalId, Integer softwareId, Integer approverId, String decision, String approvalNote, LocalDateTime approvalDate) {
        this.approvalId = approvalId;
        this.softwareId = softwareId;
        this.approverId = approverId;
        this.decision = decision;
        this.approvalNote = approvalNote;
        this.approvalDate = approvalDate;
    }

    public Integer getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Integer approvalId) {
        this.approvalId = approvalId;
    }

    public Integer getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(Integer softwareId) {
        this.softwareId = softwareId;
    }

    public Integer getApproverId() {
        return approverId;
    }

    public void setApproverId(Integer approverId) {
        this.approverId = approverId;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getApprovalNote() {
        return approvalNote;
    }

    public void setApprovalNote(String approvalNote) {
        this.approvalNote = approvalNote;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }
    
    
}

