/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.model;

/**
 *
 * @author MinhPD
 */
public class OrderDetail {
//CREATE TABLE Order_Detail (
//    order_detail_id INT AUTO_INCREMENT PRIMARY KEY,
//    order_id INT,
//    software_id INT,
//    price DECIMAL(10,2),
//    pricing_id INT NULL,
//    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
//    FOREIGN KEY (software_id) REFERENCES Software(software_id),
//    FOREIGN KEY (pricing_id) REFERENCES Software_Pricing(pricing_id)
//);

    private Integer orderDetailId;
    private Integer orderId;
    private Integer softwareId;
    private Double price;
    private Integer pricing_id;
    private SoftwarePricing softwarePricing;
    private SystemConfig systemConfig;

    public OrderDetail() {
    }

    public OrderDetail(Integer orderDetailId, Integer orderId, Integer softwareId, Double price) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.softwareId = softwareId;
        this.price = price;
    }

    public SystemConfig getSystemConfig() {
        return systemConfig;
    }

    public void setSystemConfig(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
    }

    public Integer getPricing_id() {
        return pricing_id;
    }

    public void setPricing_id(Integer pricing_id) {
        this.pricing_id = pricing_id;
    }

    public SoftwarePricing getSoftwarePricing() {
        return softwarePricing;
    }

    public void setSoftwarePricing(SoftwarePricing softwarePricing) {
        this.softwarePricing = softwarePricing;
    }

    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(Integer softwareId) {
        this.softwareId = softwareId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
