INSERT INTO Role (role_name) VALUES
('ADMIN'),
('VENDOR'),
('CUSTOMER');
-- Admin
INSERT INTO Users (full_name, email, password, role_id)
VALUES ('Admin System', 'admin@market.com', '123456',
        (SELECT role_id FROM Role WHERE role_name='ADMIN'));

-- Vendor (CHỈ 1 VENDOR)
INSERT INTO Users (full_name, email, password, role_id)
VALUES ('Vendor Alpha', 'vendor@market.com', '123456',
        (SELECT role_id FROM Role WHERE role_name='VENDOR'));

-- Customers
INSERT INTO Users (full_name, email, password, role_id)
VALUES
('Customer One', 'cus1@market.com', '123456',
 (SELECT role_id FROM Role WHERE role_name='CUSTOMER')),
('Customer Two', 'cus2@market.com', '123456',
 (SELECT role_id FROM Role WHERE role_name='CUSTOMER'));
INSERT INTO Category (category_name)
VALUES ('Productivity');
INSERT INTO Payment_Status (status_name)
VALUES ('PENDING'), ('PAID'), ('FAILED'), ('REFUNDED');
INSERT INTO Software
(name, vendor_id, category_id, price, status, download_count, avg_rating)
VALUES
('Alpha Project Manager',
 (SELECT user_id FROM Users WHERE email='vendor@market.com'),
 1, 25.00, 'APPROVED', 200, 4.6),

('Alpha Time Tracker',
 (SELECT user_id FROM Users WHERE email='vendor@market.com'),
 1, 15.00, 'APPROVED', 150, 4.3),

('Alpha Invoice Tool',
 (SELECT user_id FROM Users WHERE email='vendor@market.com'),
 1, 10.00, 'APPROVED', 120, 4.1);
INSERT INTO Orders (customer_id, payment_status_id, total_amount)
VALUES (
 (SELECT user_id FROM Users WHERE email='cus1@market.com'),
 (SELECT payment_status_id FROM Payment_Status WHERE status_name='PAID'),
 25.00
);

INSERT INTO Order_Detail (order_id, software_id, price)
VALUES (
 LAST_INSERT_ID(),
 (SELECT software_id FROM Software WHERE name='Alpha Project Manager'),
 25.00
);
INSERT INTO Orders (customer_id, payment_status_id, total_amount)
VALUES (
 (SELECT user_id FROM Users WHERE email='cus2@market.com'),
 (SELECT payment_status_id FROM Payment_Status WHERE status_name='PAID'),
 15.00
);

INSERT INTO Order_Detail (order_id, software_id, price)
VALUES (
 LAST_INSERT_ID(),
 (SELECT software_id FROM Software WHERE name='Alpha Time Tracker'),
 15.00
);
INSERT INTO Orders (customer_id, payment_status_id, total_amount)
VALUES (
 (SELECT user_id FROM Users WHERE email='cus1@market.com'),
 (SELECT payment_status_id FROM Payment_Status WHERE status_name='PAID'),
 10.00
);

INSERT INTO Order_Detail (order_id, software_id, price)
VALUES (
 LAST_INSERT_ID(),
 (SELECT software_id FROM Software WHERE name='Alpha Invoice Tool'),
 10.00
);
INSERT INTO Review (software_id, customer_id, rating, comment)
VALUES
((SELECT software_id FROM Software WHERE name='Alpha Project Manager'),
 (SELECT user_id FROM Users WHERE email='cus1@market.com'),
 5, 'Excellent app'),

((SELECT software_id FROM Software WHERE name='Alpha Time Tracker'),
 (SELECT user_id FROM Users WHERE email='cus2@market.com'),
 4, 'Very useful'),

((SELECT software_id FROM Software WHERE name='Alpha Invoice Tool'),
 (SELECT user_id FROM Users WHERE email='cus1@market.com'),
 4, 'Good for small business');
INSERT INTO Vendor_Payout
(vendor_id, amount, period_start, period_end, status)
VALUES
(
 (SELECT user_id FROM Users WHERE email='vendor@market.com'),
 45.00,   -- giả sử hệ thống giữ 5$ commission
 '2025-01-01',
 '2025-01-31',
 'PAID'
);
SELECT 
    s.name AS app_name,
    COUNT(od.order_detail_id) AS total_sales,
    SUM(od.price) AS revenue
FROM Software s
JOIN Order_Detail od ON s.software_id = od.software_id
JOIN Orders o ON od.order_id = o.order_id
JOIN Payment_Status ps ON o.payment_status_id = ps.payment_status_id
WHERE s.vendor_id = (SELECT user_id FROM Users WHERE email='vendor@market.com')
  AND ps.status_name = 'PAID'
GROUP BY s.software_id
ORDER BY revenue DESC;
INSERT INTO Users(full_name, email, password, role_id, status)
VALUES (
    'Test User',
    'test@gmail.com',
    '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',
    1,
    'ACTIVE'
);