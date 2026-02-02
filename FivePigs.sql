DROP DATABASE IF EXISTS fivepigs;
CREATE DATABASE fivepigs CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fivepigs;

-- 1. Role
CREATE TABLE Role (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE
);

-- 2. Users
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name NVARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    phone VARCHAR(20),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES Role(role_id)
);

-- 3. Payment Status
CREATE TABLE Payment_Status (
    payment_status_id INT AUTO_INCREMENT PRIMARY KEY,
    status_name VARCHAR(50)
);

-- 4. Category
CREATE TABLE Category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100)
);

-- 5. Software
CREATE TABLE Software (
    software_id INT AUTO_INCREMENT PRIMARY KEY,
    name NVARCHAR(150) NOT NULL,
    short_description NVARCHAR(255),
    vendor_id INT NOT NULL,
    category_id INT,
    price DECIMAL(10,2) DEFAULT 0,
    is_free TINYINT(1) DEFAULT 0,
    status VARCHAR(30) DEFAULT 'PENDING_REVIEW',
    download_count INT DEFAULT 0,
    avg_rating DECIMAL(2,1) DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vendor_id) REFERENCES Users(user_id),
    FOREIGN KEY (category_id) REFERENCES Category(category_id)
);

-- 6. Software Detail
CREATE TABLE Software_Detail (
    detail_id INT AUTO_INCREMENT PRIMARY KEY,
    software_id INT UNIQUE,
    description LONGTEXT,
    version VARCHAR(50),
    system_requirement LONGTEXT,
    release_note LONGTEXT,
    FOREIGN KEY (software_id) REFERENCES Software(software_id) ON DELETE CASCADE
);

-- 7. Software Image
CREATE TABLE Software_Image (
    image_id INT AUTO_INCREMENT PRIMARY KEY,
    software_id INT NOT NULL,
    image_url NVARCHAR(255) NOT NULL,
    is_thumbnail TINYINT(1) DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (software_id) REFERENCES Software(software_id) ON DELETE CASCADE
);

-- 8. Cart
CREATE TABLE Cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Users(user_id)
);

-- 9. Cart Detail
CREATE TABLE Cart_Detail (
    cart_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT,
    software_id INT,
    FOREIGN KEY (cart_id) REFERENCES Cart(cart_id),
    FOREIGN KEY (software_id) REFERENCES Software(software_id)
);

-- 10. Orders
CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    payment_status_id INT,
    total_amount DECIMAL(10,2),
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Users(user_id),
    FOREIGN KEY (payment_status_id) REFERENCES Payment_Status(payment_status_id)
);

-- 11. Order Detail
CREATE TABLE Order_Detail (
    order_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    software_id INT,
    price DECIMAL(10,2),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (software_id) REFERENCES Software(software_id)
);

-- 12. License
CREATE TABLE License (
    license_id INT AUTO_INCREMENT PRIMARY KEY,
    license_key VARCHAR(100) UNIQUE,
    software_id INT,
    customer_id INT,
    purchase_date DATETIME,
    expire_date DATETIME,
    status VARCHAR(20),
    FOREIGN KEY (software_id) REFERENCES Software(software_id),
    FOREIGN KEY (customer_id) REFERENCES Users(user_id)
);

-- 13. Software Review Process
CREATE TABLE Software_Review_Process (
    review_process_id INT AUTO_INCREMENT PRIMARY KEY,
    software_id INT,
    reviewer_id INT,
    test_result LONGTEXT,
    reviewed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (software_id) REFERENCES Software(software_id),
    FOREIGN KEY (reviewer_id) REFERENCES Users(user_id)
);

-- 14. Software Approval
CREATE TABLE Software_Approval (
    approval_id INT AUTO_INCREMENT PRIMARY KEY,
    software_id INT UNIQUE NOT NULL,
    approver_id INT NOT NULL,
    decision VARCHAR(20),
    approval_note LONGTEXT,
    approval_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (software_id) REFERENCES Software(software_id),
    FOREIGN KEY (approver_id) REFERENCES Users(user_id)
);

-- 15. Review
CREATE TABLE Review (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    software_id INT,
    customer_id INT,
    rating INT,
    comment LONGTEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (software_id) REFERENCES Software(software_id),
    FOREIGN KEY (customer_id) REFERENCES Users(user_id)
);

-- 16. Report
CREATE TABLE Report (
    report_id INT AUTO_INCREMENT PRIMARY KEY,
    software_id INT,
    reporter_id INT,
    reason LONGTEXT,
    status VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (software_id) REFERENCES Software(software_id),
    FOREIGN KEY (reporter_id) REFERENCES Users(user_id)
);

-- 17. Notification
CREATE TABLE Notification (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    title VARCHAR(150),
    content LONGTEXT,
    is_read TINYINT(1) DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- 18. Vendor Payout
CREATE TABLE Vendor_Payout (
    payout_id INT AUTO_INCREMENT PRIMARY KEY,
    vendor_id INT,
    amount DECIMAL(12,2),
    period_start DATE,
    period_end DATE,
    status VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vendor_id) REFERENCES Users(user_id)
);

-- 19. Vendor Payout Detail
CREATE TABLE Vendor_Payout_Detail (
    payout_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    payout_id INT,
    order_detail_id INT,
    amount DECIMAL(12,2),
    FOREIGN KEY (payout_id) REFERENCES Vendor_Payout(payout_id),
    FOREIGN KEY (order_detail_id) REFERENCES Order_Detail(order_detail_id)
);

-- 20. Wallet
CREATE TABLE Wallet (
    wallet_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE,
    balance DECIMAL(12,2) DEFAULT 0,
    status VARCHAR(20),
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- 21. Wallet Transaction
CREATE TABLE Wallet_Transaction (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    wallet_id INT,
    amount DECIMAL(12,2),
    transaction_type VARCHAR(20),
    reference_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (wallet_id) REFERENCES Wallet(wallet_id)
);

-- 22. Support Ticket
CREATE TABLE Support_Ticket (
    ticket_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    subject NVARCHAR(255) NOT NULL,
    description LONGTEXT NOT NULL,
    ticket_type VARCHAR(50),
    status VARCHAR(20) DEFAULT 'OPEN',
    priority VARCHAR(20) DEFAULT 'MEDIUM',
    assigned_admin_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (assigned_admin_id) REFERENCES Users(user_id)
);

-- 23. Support Ticket Message
CREATE TABLE Support_Ticket_Message (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT NOT NULL,
    sender_id INT NOT NULL,
    message LONGTEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ticket_id) REFERENCES Support_Ticket(ticket_id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES Users(user_id)
);

