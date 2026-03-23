INSERT INTO Role(role_name) VALUES
('ADMIN'),
('VENDOR'),
('CUSTOMER'),
('REVIEWER');

INSERT INTO Users(full_name, email, password, avatar, role_id, phone, status) VALUES
('Admin System', 'admin@market.com', '123456', NULL, 1, NULL, 'ACTIVE'),
('Vendor Alpha', 'vendor@market.com', '123456', NULL, 2, NULL, 'ACTIVE'),
('Customer One', 'cus1@market.com', '123456', NULL, 3, NULL, 'ACTIVE'),
('Reviewer One', 'reviewer@market.com', '123456', NULL, 4, NULL, 'ACTIVE');

INSERT INTO Users(full_name, email, password, avatar, role_id, phone, status) VALUES
('Tuan Kiet', 'kietyangho2005@gmail.com', '123456', NULL, 3, NULL, 'ACTIVE');

INSERT INTO Payment_Status(status_name) VALUES
('PENDING'),
('PAID'),
('FAILED'),
('CANCELLED');

INSERT INTO Category(category_name) VALUES
('Games'),
('Apps');

SET SQL_SAFE_UPDATES = 0;
UPDATE fivepigs.software
SET download_count = 0;

INSERT INTO Software(name, short_description, vendor_id, category_id, is_free, status, download_count, avg_rating) VALUES
('Call of Duty Warzone', 'Battle royale shooter experience', 2, 1, 0, 'APPROVED', 0, 4.6),
('Fable Rift', 'Fantasy action RPG adventure', 2, 1, 0, 'APPROVED', 0, 4.4),
('LinguaStep Italian', 'Learn Italian with daily practice', 2, 2, 0, 'APPROVED', 0, 4.3),
('Retro Pixel Theme', 'Retro UI pack for your desktop', 2, 2, 1, 'APPROVED', 0, 4.1),
('Blaze Strike', 'Fast-paced multiplayer combat game', 2, 1, 0, 'APPROVED', 0, 4.2),
('Design Canvas Pro', 'Creative toolkit for designers', 2, 2, 0, 'APPROVED', 0, 4.5);

INSERT INTO Software(name, short_description, vendor_id, category_id, is_free, status, download_count, avg_rating) VALUES
('Sky Frontier', 'Open-world air combat adventure', 2, 1, 0, 'APPROVED', 0, 0),
('Dungeon Whisper', 'Dark fantasy dungeon crawler', 2, 1, 0, 'APPROVED', 0, 0),
('Orbit Racers', 'Arcade racing across space tracks', 2, 1, 0, 'APPROVED', 0, 0),
('Empire Tactics', 'Turn-based strategy conquest game', 2, 1, 0, 'APPROVED', 0, 0),
('Mystic Valley', 'Story-rich exploration RPG', 2, 1, 0, 'APPROVED', 0, 0),
('Steel Horizon', 'Sci-fi squad shooter', 2, 1, 0, 'APPROVED', 0, 0),
('Pixel Farm Story', 'Relaxing farming simulation', 2, 1, 1, 'APPROVED', 0, 0),
('Shadow Runoff', 'Fast-paced parkour action game', 2, 1, 0, 'APPROVED', 0, 0),
('Crown of Ashes', 'Kingdom management strategy game', 2, 1, 0, 'APPROVED', 0, 0),
('Neon Drift X', 'High-speed neon racing challenge', 2, 1, 0, 'APPROVED', 0, 0),

('LinguaStep German', 'Learn German with guided lessons', 2, 2, 0, 'APPROVED', 0, 0),
('LinguaStep Spanish', 'Learn Spanish with daily practice', 2, 2, 0, 'APPROVED', 0, 0),
('FocusBoard', 'Task planning and focus management app', 2, 2, 0, 'APPROVED', 0, 0),
('NoteFlow', 'Minimal note-taking and organization app', 2, 2, 1, 'APPROVED', 0, 0),
('Budget Beam', 'Personal finance and expense tracker', 2, 2, 0, 'APPROVED', 0, 0),
('Design Spark', 'Creative starter toolkit for visuals', 2, 2, 0, 'APPROVED', 0, 0),
('CodeLite Studio', 'Lightweight coding workspace app', 2, 2, 0, 'APPROVED', 0, 0),
('Mockup Deck Pro', 'Mockup and presentation design app', 2, 2, 0, 'APPROVED', 0, 0),
('Translate Hub', 'Quick translation workspace', 2, 2, 1, 'APPROVED', 0, 0),
('Calendar Nest', 'Smart calendar and schedule assistant', 2, 2, 0, 'APPROVED', 0, 0);


INSERT INTO Software_Detail(software_id, description, system_requirement, release_note) VALUES
(1, 'Call of Duty Warzone detailed description', 'Windows 10, 8GB RAM, GTX 1060', 'Initial release version'),
(2, 'Fable Rift detailed description', 'Windows 10, 8GB RAM, RX 580', 'Initial release version'),
(3, 'LinguaStep Italian detailed description', 'Windows 10 or macOS, 4GB RAM', 'Initial release version'),
(4, 'Retro Pixel Theme detailed description', 'Windows 10, theme engine installed', 'Initial release version'),
(5, 'Blaze Strike detailed description', 'Windows 10, 8GB RAM, GTX 1050 Ti', 'Initial release version'),
(6, 'Design Canvas Pro detailed description', 'Windows 10, 8GB RAM', 'Initial release version');

INSERT INTO Software_Image(software_id, image_url, is_thumbnail) VALUES
(1, 'images/products/warzone-thumb.jpg', 1),
(1, 'images/products/warzone-1.jpg', 0),
(1, 'images/products/warzone-2.jpg', 0),

(2, 'images/products/fable-thumb.jpg', 1),
(2, 'images/products/fable-1.jpg', 0),

(3, 'images/products/italian-thumb.jpg', 1),
(3, 'images/products/italian-1.jpg', 0),

(4, 'images/products/retro-thumb.jpg', 1),

(5, 'images/products/blaze-thumb.jpg', 1),
(5, 'images/products/blaze-1.jpg', 0),

(6, 'images/products/canvas-thumb.jpg', 1),
(6, 'images/products/canvas-1.jpg', 0);


INSERT INTO Software_Version(software_id, version_name, file_url, release_note, file_size, is_active) VALUES
(1, '1.0.0', 'downloads/warzone.zip', 'Stable release', 524288000, 1),
(2, '1.0.0', 'downloads/fable-rift.zip', 'Stable release', 314572800, 1),
(3, '1.0.0', 'downloads/linguastep-italian.zip', 'Stable release', 104857600, 1),
(4, '1.0.0', 'downloads/retro-theme.zip', 'Stable release', 52428800, 1),
(5, '1.0.0', 'downloads/blaze-strike.zip', 'Stable release', 262144000, 1),
(6, '1.0.0', 'downloads/design-canvas-pro.zip', 'Stable release', 157286400, 1);

ALTER TABLE Software_Pricing
ADD COLUMN duration_days INT NULL;

INSERT INTO Software_Pricing(software_id, plan_name, max_users, price, duration_days, is_active) VALUES
(1, 'Basic', 1, 19.99, 30, 1),
(1, 'Pro', 4, 49.99, 365, 1),

(2, 'Basic', 1, 14.99, 30, 1),

(3, 'Basic', 1, 5.99, 30, 1),
(3, 'Team', 4, 17.99, 365, 1),

(4, 'Free', 1, 0.00, NULL, 1),

(5, 'Basic', 1, 12.99, 30, 1),
(5, 'Team', 4, 36.99, 365, 1),

(6, 'Basic', 1, 24.99, 30, 1),
(6, 'Pro', 4, 69.99, 365, 1);

INSERT INTO Software_Genre(software_id, genre_id) VALUES
(1, 1), -- Action
(1, 2), -- Adventure

(2, 2), -- Adventure
(2, 3), -- RPG

(3, 6), -- Language

(4, 7), -- Design and Creative

(5, 1), -- Action
(5, 5), -- Strategy

(6, 7), -- Design and Creative
(6, 8); -- Productivity

INSERT INTO Software_Genre(software_id, genre_id) VALUES
(7, 2),  -- Sky Frontier -> Adventure
(7, 1),  -- Sky Frontier -> Action

(8, 3),  -- Dungeon Whisper -> RPG

(9, 4),  -- Orbit Racers -> Simulation

(10, 5), -- Empire Tactics -> Strategy

(11, 3), -- Mystic Valley -> RPG
(11, 2), -- Mystic Valley -> Adventure

(12, 1), -- Steel Horizon -> Action

(13, 4), -- Pixel Farm Story -> Simulation

(14, 1), -- Shadow Runoff -> Action

(15, 5), -- Crown of Ashes -> Strategy

(16, 4), -- Neon Drift X -> Simulation

(17, 6), -- LinguaStep German -> Language

(18, 6), -- LinguaStep Spanish -> Language

(19, 8), -- FocusBoard -> Productivity

(20, 8), -- NoteFlow -> Productivity

(21, 8), -- Budget Beam -> Productivity

(22, 7), -- Design Spark -> Design and Creative
(22, 8), -- Design Spark -> Productivity

(23, 8), -- CodeLite Studio -> Productivity
(23, 7), -- CodeLite Studio -> Design and Creative

(24, 7), -- Mockup Deck Pro -> Design and Creative

(25, 6), -- Translate Hub -> Language

(26, 8); -- Calendar Nest -> Productivity




INSERT INTO Review(software_id, customer_id, rating, comment) VALUES
(1, 3, 5, 'Great gameplay and solid performance.'),
(1, 3, 4, 'Fun experience, but updates could be smoother.'),
(3, 3, 4, 'Useful for daily language practice.'),
(6, 3, 5, 'Excellent toolkit for design work.');


UPDATE Software s
SET avg_rating = (
    SELECT ROUND(AVG(r.rating), 1)
    FROM Review r
    WHERE r.software_id = s.software_id
);


INSERT INTO software_demo_version(software_id, version_name, demo_file_url, release_note, file_size, is_active) VALUES
(1, 'Demo 1.0', 'downloads_demo/warzone-demo.zip', 'Playable demo build', 104857600, 1),
(3, 'Demo 1.0', 'downloads_demo/italian-demo.zip', 'Playable demo build', 52428800, 1);


INSERT INTO License(license_key, pricing_id, software_id, owner_id, max_users, purchase_date, expire_date, status) VALUES
('ABCDE12345FGHIJ67890', 1, 1, 3, 1, NOW(), NULL, 'ACTIVE'),
('KLMNO12345PQRST67890', 4, 3, 3, 1, NOW(), NULL, 'ACTIVE');

INSERT INTO License_User(license_id, user_id, status) VALUES
(1, 3, 'ACTIVE'),
(2, 3, 'ACTIVE');