/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.model;

import java.sql.Date;

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
    
    private int orderId,customerId,paymentStatusId;
    private double totalAmount;
    private Date orderDate;
}
