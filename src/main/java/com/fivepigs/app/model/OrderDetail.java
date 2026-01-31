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
    private int orderDetailId,orderId,softwareId;
    private double price;

    public OrderDetail() {
    }

    public OrderDetail(int orderDetailId, int orderId, int softwareId, double price) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.softwareId = softwareId;
        this.price = price;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(int softwareId) {
        this.softwareId = softwareId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    
}
