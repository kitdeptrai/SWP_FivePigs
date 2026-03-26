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
public class VendorEarning {
//    CREATE TABLE Vendor_Earning (
//    earning_id INT AUTO_INCREMENT PRIMARY KEY,
//    vendor_id INT NOT NULL,
//    software_id INT,
//    order_id INT,
//    amount DECIMAL(12,2), -- tiền vendor nhận được (sau fee)
//
//    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
//
//    FOREIGN KEY (vendor_id) REFERENCES Users(user_id),
//    FOREIGN KEY (software_id) REFERENCES Software(software_id),
//    FOREIGN KEY (order_id) REFERENCES Orders(order_id)
//);

    private Integer earningId;
    private Integer vendorId;
    private Integer softwareId;
    private Integer orderId;
    private Double amount;
    private LocalDateTime createdAt;
    private Order order;
    private OrderDetail orderDetail;
    private SystemConfig systemConfig;
    private User user;
    private Software software;

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }
    
    

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public SystemConfig getSystemConfig() {
        return systemConfig;
    }

    public void setSystemConfig(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
    }

    public Integer getEarningId() {
        return earningId;
    }

    public void setEarningId(Integer earningId) {
        this.earningId = earningId;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public Integer getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(Integer softwareId) {
        this.softwareId = softwareId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

}
