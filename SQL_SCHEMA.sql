-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS fivepigs;

-- Use the database
USE fivepigs;

-- 1. Role Table
CREATE TABLE IF NOT EXISTS Role (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) UNIQUE
);

-- 2. Users Table
CREATE TABLE IF NOT EXISTS Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    role_id INT,
    status VARCHAR(20), -- ACTIVE, BLOCKED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES Role(role_id)
);

-- 3. Payment_Status Table
CREATE TABLE IF NOT EXISTS Payment_Status (
    payment_status_id INT PRIMARY KEY AUTO_INCREMENT,
    status_name VARCHAR(50)
);

-- 4. Category Table
CREATE TABLE IF NOT EXISTS Category (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100)
);

-- 5. Software Table
CREATE TABLE IF NOT EXISTS Software (
    software_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150),
    vendor_id INT,
    category_id INT,
    price DECIMAL(10,2),
    status VARCHAR(30),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vendor_id) REFERENCES Users(user_id),
    FOREIGN KEY (category_id) REFERENCES Category(category_id)
);

-- 6. Software_Detail Table
CREATE TABLE IF NOT EXISTS Software_Detail (
    detail_id INT PRIMARY KEY AUTO_INCREMENT,
    software_id INT,
    description TEXT,
    version VARCHAR(50),
    system_requirement TEXT,
    release_note TEXT,
    FOREIGN KEY (software_id) REFERENCES Software(software_id)
);

-- 7. Cart Table
CREATE TABLE IF NOT EXISTS Cart (
    cart_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Users(user_id)
);

-- 8. Cart_Detail Table
CREATE TABLE IF NOT EXISTS Cart_Detail (
    cart_detail_id INT PRIMARY KEY AUTO_INCREMENT,
    cart_id INT,
    software_id INT,
    FOREIGN KEY (cart_id) REFERENCES Cart(cart_id),
    FOREIGN KEY (software_id) REFERENCES Software(software_id)
);

-- 9. Orders Table
CREATE TABLE IF NOT EXISTS Orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    payment_status_id INT,
    total_amount DECIMAL(10,2),
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Users(user_id),
    FOREIGN KEY (payment_status_id) REFERENCES Payment_Status(payment_status_id)
);

-- 10. Order_Detail Table
CREATE TABLE IF NOT EXISTS Order_Detail (
    order_detail_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    software_id INT,
    price DECIMAL(10,2),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (software_id) REFERENCES Software(software_id)
);

-- 11. License Table
CREATE TABLE IF NOT EXISTS License (
    license_id INT PRIMARY KEY AUTO_INCREMENT,
    license_key VARCHAR(100) UNIQUE,
    software_id INT,
    customer_id INT,
    purchase_date DATETIME,
    expire_date DATETIME,
    status VARCHAR(20),
    FOREIGN KEY (software_id) REFERENCES Software(software_id),
    FOREIGN KEY (customer_id) REFERENCES Users(user_id)
);

-- 12. Software_Review_Process Table
CREATE TABLE IF NOT EXISTS Software_Review_Process (
    review_process_id INT PRIMARY KEY AUTO_INCREMENT,
    software_id INT,
    reviewer_id INT,
    test_result TEXT,
    reviewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (software_id) REFERENCES Software(software_id),
    FOREIGN KEY (reviewer_id) REFERENCES Users(user_id)
);

-- 13. Software_Approval Table
CREATE TABLE IF NOT EXISTS Software_Approval (
    approval_id INT PRIMARY KEY AUTO_INCREMENT,
    software_id INT,
    approver_id INT,
    decision VARCHAR(20),
    approval_note TEXT,
    approval_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (software_id) REFERENCES Software(software_id),
    FOREIGN KEY (approver_id) REFERENCES Users(user_id)
);

-- 14. Review Table
CREATE TABLE IF NOT EXISTS Review (
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    software_id INT,
    customer_id INT,
    rating INT,
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (software_id) REFERENCES Software(software_id),
    FOREIGN KEY (customer_id) REFERENCES Users(user_id)
);

-- 15. Report Table
CREATE TABLE IF NOT EXISTS Report (
    report_id INT PRIMARY KEY AUTO_INCREMENT,
    software_id INT,
    reporter_id INT,
    reason TEXT,
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (software_id) REFERENCES Software(software_id),
    FOREIGN KEY (reporter_id) REFERENCES Users(user_id)
);

-- 16. Notification Table
CREATE TABLE IF NOT EXISTS Notification (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    title VARCHAR(150),
    content TEXT,
    is_read TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- 17. Vendor_Payout Table
CREATE TABLE IF NOT EXISTS Vendor_Payout (
    payout_id INT PRIMARY KEY AUTO_INCREMENT,
    vendor_id INT,
    amount DECIMAL(12,2),
    period_start DATE,
    period_end DATE,
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vendor_id) REFERENCES Users(user_id)
);

-- 18. Vendor_Payout_Detail Table
CREATE TABLE IF NOT EXISTS Vendor_Payout_Detail (
    payout_detail_id INT PRIMARY KEY AUTO_INCREMENT,
    payout_id INT,
    order_detail_id INT,
    amount DECIMAL(12,2),
    FOREIGN KEY (payout_id) REFERENCES Vendor_Payout(payout_id),
    FOREIGN KEY (order_detail_id) REFERENCES Order_Detail(order_detail_id)
);

-- Insert default roles if they don't exist
INSERT IGNORE INTO Role (role_name) VALUES ('User');
INSERT IGNORE INTO Role (role_name) VALUES ('Admin');
INSERT IGNORE INTO Role (role_name) VALUES ('Vendor');
INSERT IGNORE INTO Role (role_name) VALUES ('Reviewer');
INSERT IGNORE INTO Role (role_name) VALUES ('Approval');

-- Script to create an admin account for MySQL
-- Password for this account will be: admin123

USE fivepigs;

-- Set user-defined variables for the session
SET @admin_email = 'admin@fivepigs.com';
SET @admin_role_id = (SELECT role_id FROM Role WHERE role_name = 'Admin');

-- Check if the admin user already exists
-- Script to create an admin account for MySQL (Simplified)
-- Password for this account will be: admin123


-- This will insert the admin user ONLY IF the email 'admin@fivepigs.com' does not already exist.
-- It uses INSERT IGNORE, which is safe to run multiple times.
INSERT IGNORE INTO Users (full_name, email, password, role_id, status)
SELECT
    'Administrator',
    'admin@fivepigs.com',
    '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', -- SHA-256 hash for "admin123"
    (SELECT role_id FROM Role WHERE role_name = 'Admin'),
    'ACTIVE';

-- Check if the user was inserted or already existed
SELECT
    CASE
        WHEN ROW_COUNT() > 0 THEN 'Admin account created successfully!'
        ELSE 'Admin account already exists or role "Admin" not found.'
        END AS message;