/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.model;

/**
 *
 * @author MinhPD
 */
public class VendorPayoutDetail {
//    payout_detail_id INT AUTO_INCREMENT PRIMARY KEY,
//    payout_id INT,
//    order_detail_id INT,
//    amount DECIMAL(12,2),
//    FOREIGN KEY (payout_id) REFERENCES Vendor_Payout(payout_id),
//    FOREIGN KEY (order_detail_id) REFERENCES Order_Detail(order_detail_id)
    
    private Integer payoutDetailId;
    private Integer payoutId;
    private Integer orderDetailId;
    private Double amount;

    public Integer getPayoutDetailId() {
        return payoutDetailId;
    }

    public void setPayoutDetailId(Integer payoutDetailId) {
        this.payoutDetailId = payoutDetailId;
    }

    public Integer getPayoutId() {
        return payoutId;
    }

    public void setPayoutId(Integer payoutId) {
        this.payoutId = payoutId;
    }

    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    
}
