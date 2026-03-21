CREATE TABLE user_payment_method (
    payment_method_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    provider VARCHAR(50) NOT NULL,
    card_holder VARCHAR(100) NULL,
    masked_number VARCHAR(30) NULL,
    expiry_month INT NULL,
    expiry_year INT NULL,
    is_default TINYINT(1) DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE redeem_code (
    redeem_code_id INT PRIMARY KEY AUTO_INCREMENT,
    software_id INT NOT NULL,
    code VARCHAR(100) NOT NULL UNIQUE,
    source_type VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'UNUSED',
    created_by INT NULL,
    redeemed_by INT NULL,
    redeemed_at DATETIME NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (software_id) REFERENCES software(software_id),
    FOREIGN KEY (created_by) REFERENCES users(user_id),
    FOREIGN KEY (redeemed_by) REFERENCES users(user_id)
);

ALTER TABLE fivepigs.users
ADD COLUMN date_of_birth DATE NULL,
ADD COLUMN address VARCHAR(255) NULL,
ADD COLUMN bio TEXT NULL;

ALTER TABLE fivepigs.users
ADD COLUMN gender VARCHAR(20) NULL;

CREATE TABLE user_feedback (
    feedback_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    subject VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'NEW',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE redeem_code (
    redeem_code_id INT PRIMARY KEY AUTO_INCREMENT,
    software_id INT NOT NULL,
    code VARCHAR(100) NOT NULL UNIQUE,
    source_type VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'UNUSED',
    created_by INT NULL,
    redeemed_by INT NULL,
    redeemed_at DATETIME NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (software_id) REFERENCES software(software_id),
    FOREIGN KEY (created_by) REFERENCES users(user_id),
    FOREIGN KEY (redeemed_by) REFERENCES users(user_id)
);
