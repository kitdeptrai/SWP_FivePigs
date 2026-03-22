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
public class VendorPayout {
//CREATE TABLE Vendor_Payout (
//    payout_id INT AUTO_INCREMENT PRIMARY KEY,
//    vendor_id INT NOT NULL,
//    amount DECIMAL(12,2) NOT NULL,
//    payment_method VARCHAR(50),       -- BANK / MOMO / PAYPAL
//    payment_account VARCHAR(255),     -- số TK hoặc email paypal
//    status VARCHAR(20) DEFAULT 'PENDING',
//    processed_at DATETIME,
//    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
//    FOREIGN KEY (vendor_id) REFERENCES Users(user_id)
//);
    
   Integer payoutId;
   Integer vendorId;
   Double amount;
   String paymentMethod;
   String paymentAccount;
   String status;
   LocalDateTime processedAt;
   LocalDateTime createdAt;

    public Integer getPayoutId() {
        return payoutId;
    }

    public void setPayoutId(Integer payoutId) {
        this.payoutId = payoutId;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
   
   
}
