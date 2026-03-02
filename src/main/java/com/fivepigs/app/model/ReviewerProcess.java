/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.model;

import java.time.LocalDateTime;

/**
 *
 * @author thanh
 */
public class ReviewerProcess {
    private Integer review_process_id;
    private Integer reviewer_id;
    private String test_result;
    private LocalDateTime reviewed_at;
    private String recommendation;
    
    public ReviewerProcess() {
    }

    public ReviewerProcess(Integer review_process_id, Integer reviewer_id, String test_result, LocalDateTime reviewed_at,String recommendation) {
        this.review_process_id = review_process_id;
        this.reviewer_id = reviewer_id;
        this.test_result = test_result;
        this.reviewed_at = reviewed_at;
        this.recommendation = recommendation;
    }

    public Integer getReview_process_id() {
        return review_process_id;
    }

    public void setReview_process_id(Integer review_process_id) {
        this.review_process_id = review_process_id;
    }

    public Integer getReviewer_id() {
        return reviewer_id;
    }

    public void setReviewer_id(Integer reviewer_id) {
        this.reviewer_id = reviewer_id;
    }

    public String getTest_result() {
        return test_result;
    }

    public void setTest_result(String test_result) {
        this.test_result = test_result;
    }

    public LocalDateTime getReviewed_at() {
        return reviewed_at;
    }

    public void setReviewed_at(LocalDateTime reviewed_at) {
        this.reviewed_at = reviewed_at;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }



    
    
}
