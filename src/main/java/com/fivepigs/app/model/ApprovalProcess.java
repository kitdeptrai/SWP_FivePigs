/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.model;

import java.sql.Date;
import java.time.LocalDateTime;

/**
 *
 * @author thanh
 */
public class ApprovalProcess {
    private Integer approval_id;
    private Integer approver_id;
    private String decision;
    private String approval_note;
    private LocalDateTime approval_date;

    public ApprovalProcess() {
    }

    public ApprovalProcess(Integer approval_id, Integer approver_id, String decision, String approval_note, LocalDateTime approval_date) {
        this.approval_id = approval_id;
        this.approver_id = approver_id;
        this.decision = decision;
        this.approval_note = approval_note;
        this.approval_date = approval_date;
    }


    public Integer getApproval_id() {
        return approval_id;
    }

    public void setApproval_id(Integer approval_id) {
        this.approval_id = approval_id;
    }

    public Integer getApprover_id() {
        return approver_id;
    }

    public void setApprover_id(Integer approver_id) {
        this.approver_id = approver_id;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getApproval_note() {
        return approval_note;
    }

    public void setApproval_note(String approval_note) {
        this.approval_note = approval_note;
    }

    public LocalDateTime getApproval_date() {
        return approval_date;
    }

    public void setApproval_date(LocalDateTime approval_date) {
        this.approval_date = approval_date;
    }
   
}
