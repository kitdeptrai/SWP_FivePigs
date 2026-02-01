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
//    order_detail_id INT PRIMARY KEY IDENTITY(1,1),
//order_id INT,
//software_id INT,
//price DECIMAL(10,2),
    private Integer orderDetailId;
    private Integer orderId;
    private Integer softwareId;
    private Double price;

    public OrderDetail() {
    }

    public OrderDetail(Integer orderDetailId, Integer orderId, Integer softwareId, Double price) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.softwareId = softwareId;
        this.price = price;
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
