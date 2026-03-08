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
    avatar VARCHAR(255),
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

-- 8. Software Version
CREATE TABLE Software_Version (
    version_id INT AUTO_INCREMENT PRIMARY KEY,
    software_id INT NOT NULL,
    version_name VARCHAR(50),      -- ví dụ 1.0.0
    file_url VARCHAR(255),         -- link file zip/app
    release_note LONGTEXT,
    file_size BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_active TINYINT(1) DEFAULT 1,
    FOREIGN KEY (software_id) REFERENCES Software(software_id)
        ON DELETE CASCADE
);

-- 9. Cart
CREATE TABLE Cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Users(user_id)
);

-- 10. Cart Detail
CREATE TABLE Cart_Detail (
    cart_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT,
    software_id INT,
    FOREIGN KEY (cart_id) REFERENCES Cart(cart_id),
    FOREIGN KEY (software_id) REFERENCES Software(software_id)
);

-- 11. Orders
CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    payment_status_id INT,
    total_amount DECIMAL(10,2),
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Users(user_id),
    FOREIGN KEY (payment_status_id) REFERENCES Payment_Status(payment_status_id)
);

-- 12. Order Detail
CREATE TABLE Order_Detail (
    order_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    software_id INT,
    price DECIMAL(10,2),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (software_id) REFERENCES Software(software_id)
);

-- 13. License
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

-- 14. Software Review Process
CREATE TABLE Software_Review_Process (
    review_process_id INT AUTO_INCREMENT PRIMARY KEY,
    software_id INT,
    reviewer_id INT,
    test_result LONGTEXT,
    reviewed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (software_id) REFERENCES Software(software_id),
    FOREIGN KEY (reviewer_id) REFERENCES Users(user_id)
);

-- 15. Software Approval
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

-- 16. Review
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

-- 17. Report
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

-- 18. Notification
CREATE TABLE Notification (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    title VARCHAR(150),
    content LONGTEXT,
    is_read TINYINT(1) DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- 19. Vendor Payout
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

-- 24. pending review
CREATE TABLE Review_Score (
    review_score_id INT AUTO_INCREMENT PRIMARY KEY,
    software_id INT NOT NULL,
    reviewer_id INT NOT NULL,
    
    no_malware TINYINT(1) DEFAULT 0,
    no_copyright_violation TINYINT(1) DEFAULT 0,
    no_spam_content TINYINT(1) DEFAULT 0,

    ui_ux_score INT,
    technical_score INT,
    performance_score INT,
    documentation_score INT,

    total_score DECIMAL(4,2),

    decision VARCHAR(20), -- APPROVED / REJECTED
    review_note LONGTEXT,

    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (software_id) REFERENCES Software(software_id),
    FOREIGN KEY (reviewer_id) REFERENCES Users(user_id)
);

-- 25.my reviews


-- ===== Review Guidelines =====
CREATE TABLE Review_Guideline (
    guideline_id INT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(50) NOT NULL,              -- Security/Legal/Technical/Performance/Functionality
    priority VARCHAR(20) NOT NULL,              -- Critical/High/Medium/Low
    title NVARCHAR(150) NOT NULL,
    description LONGTEXT,
    icon VARCHAR(50),                           -- Shield / Code / Bolt ...
    color VARCHAR(30),                          -- Red / Blue / Purple ...
    created_by INT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES Users(user_id)
);

-- 26.Review_Guideline_Item
CREATE TABLE Review_Guideline_Item (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    guideline_id INT NOT NULL,
    item_text NVARCHAR(255) NOT NULL,
    sort_order INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (guideline_id) REFERENCES Review_Guideline(guideline_id) ON DELETE CASCADE
);

-- Index để search/filter nhanh
CREATE INDEX idx_guideline_category ON Review_Guideline(category);
CREATE INDEX idx_guideline_title ON Review_Guideline(title);
CREATE INDEX idx_item_guideline ON Review_Guideline_Item(guideline_id);

