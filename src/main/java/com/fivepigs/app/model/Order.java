/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.model;

import java.sql.Date;
import java.time.LocalDateTime;

/**
 *
 * @author MinhPD
 */
public class Order {
//    CREATE TABLE Orders (
//    order_id INT AUTO_INCREMENT PRIMARY KEY,
//    customer_id INT,
//    payment_status_id INT,
//    total_amount DECIMAL(10,2),
//    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
//    FOREIGN KEY (customer_id) REFERENCES Users(user_id),
//    FOREIGN KEY (payment_status_id) REFERENCES Payment_Status(payment_status_id)
//);
    
    private Integer orderId;
    private Integer customerId;
    private Integer paymentStatusId;
    private Double totalAmount;
    private LocalDateTime orderDatel;

    public Order() {
    }

    public Order(Integer orderId, Integer customerId, Integer paymentStatusId, Double totalAmount, LocalDateTime orderDatel) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.paymentStatusId = paymentStatusId;
        this.totalAmount = totalAmount;
        this.orderDatel = orderDatel;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getPaymentStatusId() {
        return paymentStatusId;
    }

    public void setPaymentStatusId(Integer paymentStatusId) {
        this.paymentStatusId = paymentStatusId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getOrderDatel() {
        return orderDatel;
    }

    public void setOrderDatel(LocalDateTime orderDatel) {
        this.orderDatel = orderDatel;
    }
    
    
}
