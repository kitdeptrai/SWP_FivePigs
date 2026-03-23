-- Bang insert Role

INSERT INTO Role (role_name)
VALUES 
('ADMIN'),
('CUSTOMER'),
('VENDOR'),
('APPROVAL'),
('REVIEWER');


-- Bang insert Users

INSERT INTO Users (full_name, email, password, role_id, phone)
VALUES 
('System Admin', 'admin@gmail.com', '123456', 1, '0900000001');

INSERT INTO Users (full_name, email, password, role_id, phone)
VALUES 
('Reviewer', 'reviewer@gmail.com', '123456', 5, '0900000002');

INSERT INTO Users (full_name, email, password, role_id, phone)
VALUES 
('Approval', 'approval@gmail.com', '123456', 4, '0900000003');

INSERT INTO Users (full_name, email, password, role_id, phone)
VALUES
('Vendor One', 'vendor1@gmail.com', '123456', 3, '0900000011'),
('Vendor Two', 'vendor2@gmail.com', '123456', 3, '0900000012'),
('Vendor Three', 'vendor3@gmail.com', '123456', 3, '0900000013');

INSERT INTO Users (full_name, email, password, role_id, phone)
VALUES
('Customer 1', 'customer1@gmail.com', '123456', 2, '0900000101'),
('Customer 2', 'customer2@gmail.com', '123456', 2, '0900000102'),
('Customer 3', 'customer3@gmail.com', '123456', 2, '0900000103'),
('Customer 4', 'customer4@gmail.com', '123456', 2, '0900000104'),
('Customer 5', 'customer5@gmail.com', '123456', 2, '0900000105'),
('Customer 6', 'customer6@gmail.com', '123456', 2, '0900000106'),
('Customer 7', 'customer7@gmail.com', '123456', 2, '0900000107'),
('Customer 8', 'customer8@gmail.com', '123456', 2, '0900000108'),
('Customer 9', 'customer9@gmail.com', '123456', 2, '0900000109'),
('Customer 10', 'customer10@gmail.com', '123456', 2, '0900000110');


-- Bang insert Category

INSERT INTO Category (category_name)
VALUES 
('APP'),
('GAME');


-- Bang insert Sofware



INSERT INTO Software
(name, short_description, vendor_id, category_id, is_free, status, download_count, avg_rating, created_at)
VALUES

-- ===== APPS =====
('Facebook', 'Social networking platform', 4, 1, 1, 'ACTIVE', 85, 4.3, NOW()),
('Instagram', 'Photo and video sharing app', 4, 1, 1, 'ACTIVE', 72, 4.4, NOW()),
('TikTok', 'Short-form video entertainment', 4, 1, 1, 'ACTIVE', 90, 4.5, NOW()),
('YouTube', 'Video streaming platform', 4, 1, 1, 'ACTIVE', 88, 4.6, NOW()),
('Spotify', 'Music streaming service', 4, 1, 1, 'ACTIVE', 64, 4.5, NOW()),
('Netflix', 'Movie and TV streaming app', 4, 1, 0, 'ACTIVE', 41, 4.4, NOW()),
('Zalo', 'Vietnam messaging app', 4, 1, 1, 'ACTIVE', 55, 4.2, NOW()),
('Telegram', 'Secure messaging platform', 4, 1, 1, 'ACTIVE', 60, 4.6, NOW()),

('Google Maps', 'Navigation and maps service', 6, 1, 1, 'ACTIVE', 78, 4.7, NOW()),
('WhatsApp', 'Global messaging app', 6, 1, 1, 'ACTIVE', 69, 4.5, NOW()),

-- ===== GAMES =====
('Resident Evil Requiem', 'Horror survival action game', 5, 2, 0, 'ACTIVE', 33, 4.8, NOW()),
('Grand Theft Auto V', 'Open-world action adventure game', 5, 2, 0, 'ACTIVE', 48, 4.9, NOW()),
('Call of Duty Warzone', 'Battle royale shooter game', 5, 2, 1, 'ACTIVE', 70, 4.6, NOW()),
('League of Legends', 'Multiplayer online battle arena game', 5, 2, 1, 'ACTIVE', 75, 4.7, NOW()),
('Dota 2', 'Competitive MOBA strategy game', 5, 2, 1, 'ACTIVE', 58, 4.6, NOW()),
('Minecraft', 'Sandbox building adventure game', 5, 2, 0, 'ACTIVE', 44, 4.8, NOW()),
('Fortnite', 'Online battle royale game', 5, 2, 1, 'ACTIVE', 67, 4.5, NOW()),
('Valorant', 'Tactical FPS shooter game', 5, 2, 1, 'ACTIVE', 62, 4.6, NOW()),

('PUBG Battlegrounds', 'Realistic battle royale game', 6, 2, 1, 'ACTIVE', 59, 4.4, NOW()),
('Elden Ring', 'Dark fantasy action RPG game', 6, 2, 0, 'ACTIVE', 37, 4.9, NOW()),

-- ===== NEW =====
('Discord','Gaming and community communication platform',4,1,1,'PENDING_REVIEW',0,0,NOW()),
('Snapchat','Multimedia messaging app',4,1,1,'PENDING_REVIEW',0,0,NOW()),
('Zoom','Online video conferencing and meeting platform',6,1,1,'PENDING_REVIEW',0,0,NOW()),
('Adobe Photoshop','Professional image editing software',6,1,0,'PENDING_REVIEW',0,0,NOW()),
('Notion','Productivity workspace for notes and collaboration',4,1,0,'PENDING_REVIEW',0,0,NOW()),

('Slack','Team collaboration and messaging platform',6,1,0,'PENDING_APPROVAL',0,0,NOW()),
('Twitch','Live streaming platform for gamers',4,1,1,'PENDING_APPROVAL',0,0,NOW()),
('Genshin Impact','Open world anime action RPG',5,2,1,'PENDING_APPROVAL',0,0,NOW()),
('Cyberpunk 2077','Futuristic open world RPG game',5,2,0,'PENDING_APPROVAL',0,0,NOW()),
('Among Us','Online multiplayer social deduction game',5,2,0,'PENDING_APPROVAL',0,0,NOW());

INSERT INTO Software_Pricing (software_id, plan_name, max_users, price)
VALUES

-- ===== FREE SOFTWARE (chỉ BASIC) =====
(1,'BASIC',1,0),(2,'BASIC',1,0),(3,'BASIC',1,0),(4,'BASIC',1,0),
(5,'BASIC',1,0),(7,'BASIC',1,0),(8,'BASIC',1,0),(9,'BASIC',1,0),
(10,'BASIC',1,0),(13,'BASIC',1,0),(14,'BASIC',1,0),(15,'BASIC',1,0),
(17,'BASIC',1,0),(18,'BASIC',1,0),(19,'BASIC',1,0),
(21,'BASIC',1,0),(22,'BASIC',1,0),(23,'BASIC',1,0),
(27,'BASIC',1,0),(28,'BASIC',1,0),

-- ===== PAID SOFTWARE =====

-- Netflix
(6,'BASIC',1,9.99),
(6,'TEAM',4,19.99),

-- Resident Evil
(11,'BASIC',1,59.99),

-- GTA V
(12,'BASIC',1,39.99),

-- Minecraft
(16,'BASIC',1,26.95),

-- Elden Ring
(20,'BASIC',1,59.99),

-- Photoshop
(24,'BASIC',1,29.99),
(24,'PRO',3,59.99),

-- Notion
(25,'BASIC',1,8.99),


-- Slack
(26,'BASIC',1,6.99),
(26,'TEAM',5,15.99),

-- Cyberpunk
(29,'BASIC',1,59.99),

-- Among Us
(30,'BASIC',1,4.99);

INSERT INTO Software_Detail 
(software_id, description, system_requirement, release_note)
VALUES

(1,
'Facebook is one of the largest social networking platforms in the world. It allows users to connect with friends, share posts, photos, and videos, join communities, follow public pages, and communicate through messaging features. Businesses and content creators can also promote products, run advertisements, and build online communities. The platform integrates news feeds, groups, marketplace, and live streaming features that allow users to interact and share information in real time.',
'Windows 10 / macOS 11 / Android 8 / iOS 13, 2GB RAM, Internet connection required',
'Improved news feed ranking algorithm, performance optimizations, and bug fixes.'
),

(2,
'Instagram is a social media application focused on photo and video sharing. Users can upload pictures, reels, and stories, apply filters, and interact with others through likes, comments, and direct messages. The platform is widely used by influencers, photographers, and brands for visual storytelling and marketing. Instagram also includes features like live streaming, reels, and shopping integrations that help users discover products and trends.',
'Windows 10 / macOS 11 / Android 8 / iOS 13, 2GB RAM, Internet connection required',
'Enhanced reels performance, improved story editor, and minor bug fixes.'
),

(3,
'TikTok is a short-form video entertainment platform where users create and share engaging videos with music, filters, and effects. The app uses a powerful recommendation algorithm that shows personalized content on the "For You" page. Creators can produce dance videos, tutorials, comedy clips, and educational content. TikTok has become one of the fastest-growing social platforms worldwide with millions of daily active users.',
'Windows 10 / macOS 11 / Android 8 / iOS 13, 3GB RAM recommended',
'Improved video editing tools and optimized recommendation engine.'
),

(4,
'YouTube is the world’s largest video streaming and sharing platform. It allows users to watch videos ranging from entertainment and music to educational tutorials and documentaries. Content creators can upload videos, build subscriber communities, and monetize through advertisements and memberships. The platform supports live streaming, playlists, recommendations, and cross-device viewing.',
'Windows 10 / macOS 11 / Android 8 / iOS 13, 4GB RAM recommended',
'Improved playback stability and new creator analytics features.'
),

(5,
'Spotify is a digital music streaming service that gives users access to millions of songs, albums, and podcasts. Users can create playlists, follow artists, and discover new music through personalized recommendations. Spotify provides both free ad-supported streaming and premium subscriptions with offline listening and higher audio quality.',
'Windows 10 / macOS 11 / Android 8 / iOS 13, 2GB RAM',
'Improved music recommendation system and faster playlist loading.'
),

(6,
'Netflix is a popular online streaming service that provides a large library of movies, TV series, and original productions. Users can watch content on demand across multiple devices and receive personalized recommendations based on their viewing habits. Netflix also offers offline downloads and high-definition streaming for premium users.',
'Windows 10 / macOS 11 / Android 9 / iOS 13, 4GB RAM recommended',
'Added new streaming optimization and improved subtitle support.'
),

(7,
'Zalo is a Vietnamese messaging and social networking application widely used for communication and business interactions. Users can send messages, make voice and video calls, share images, and create group chats. Zalo also integrates digital services such as payments, official accounts, and mini apps for local businesses.',
'Windows 10 / macOS 11 / Android 8 / iOS 12, 2GB RAM',
'Improved call stability and enhanced message synchronization.'
),

(8,
'Telegram is a cloud-based messaging application known for its speed, privacy, and security features. It allows users to send encrypted messages, share large files, create channels, and manage large community groups. Telegram supports bots and automation tools that extend the platform’s capabilities for developers and businesses.',
'Windows 10 / macOS 11 / Android 8 / iOS 12, 2GB RAM',
'Improved group management tools and enhanced encryption security.'
),

(9,
'Google Maps is a navigation and mapping service that helps users find locations, explore places, and get directions for driving, walking, cycling, or public transportation. It provides real-time traffic updates, street views, business listings, and location sharing features. Google Maps is widely used for travel planning and daily navigation.',
'Windows 10 / macOS 11 / Android 8 / iOS 12, GPS support recommended',
'Updated navigation accuracy and improved real-time traffic reporting.'
),

(10,
'WhatsApp is a globally popular messaging application that allows users to send text messages, voice notes, images, videos, and documents. It also supports voice and video calls, group chats, and end-to-end encryption to ensure secure communication. Businesses use WhatsApp Business features to interact with customers and provide support.',
'Windows 10 / macOS 11 / Android 8 / iOS 12, 2GB RAM',
'Enhanced privacy controls and improved voice call quality.'
),

(11,
'Resident Evil Requiem is a survival horror action game that continues the legacy of the Resident Evil franchise. Players explore dark environments filled with terrifying creatures while solving puzzles and managing limited resources. The game combines cinematic storytelling with intense gameplay and atmospheric horror elements.',
'Windows 10 64-bit, Intel i5, 8GB RAM, GTX 1060 or equivalent, 50GB storage',
'Optimized graphics performance and improved enemy AI behavior.'
),

(12,
'Grand Theft Auto V is an open-world action adventure game where players explore the city of Los Santos and complete missions across a massive interactive world. The game features multiple playable characters, a deep storyline, and online multiplayer modes that allow players to build criminal empires and participate in various activities.',
'Windows 10 64-bit, Intel i5, 8GB RAM, GTX 970, 80GB storage',
'Improved online stability and additional multiplayer events.'
),

(13,
'Call of Duty Warzone is a free-to-play battle royale shooter that drops players into large-scale combat zones where they must survive against dozens of opponents. The game includes intense firefights, tactical gameplay, and team-based strategies. Warzone features seasonal updates with new maps, weapons, and game modes.',
'Windows 10 64-bit, Intel i5, 12GB RAM, GTX 1060, 70GB storage',
'New weapons added and map performance optimizations.'
),

(14,
'League of Legends is a multiplayer online battle arena game where two teams compete to destroy the opposing base. Players choose champions with unique abilities and coordinate strategies with teammates. The game is known for its competitive esports scene and frequent updates introducing new champions and balance changes.',
'Windows 10 / macOS 11, Intel i3, 4GB RAM, integrated graphics supported',
'Champion balance updates and improved matchmaking system.'
),

(15,
'Dota 2 is a competitive multiplayer strategy game where two teams of five players battle to destroy the enemy’s Ancient. Each hero has unique abilities that require teamwork and strategic planning. The game has a deep gameplay system and a global esports community with major tournaments.',
'Windows 10 / macOS 11 / Linux, Intel i3, 4GB RAM, 15GB storage',
'Improved hero balance and optimized matchmaking performance.'
),

(16,
'Minecraft is a sandbox adventure game that allows players to build, explore, and survive in a procedurally generated block-based world. Players can gather resources, craft tools, build structures, and interact with creatures. The game supports both creative and survival modes and has a huge modding and community ecosystem.',
'Windows 10 / macOS 11, Intel i3, 4GB RAM, 10GB storage',
'Added new blocks, improved world generation, and bug fixes.'
),

(17,
'Fortnite is a popular online battle royale game where players compete to be the last person or team standing. The game is known for its building mechanics, colorful graphics, and frequent in-game events. Fortnite also features collaborations with popular franchises and seasonal updates.',
'Windows 10, Intel i5, 8GB RAM, GTX 960, 30GB storage',
'New season content and improved weapon balancing.'
),

(18,
'Valorant is a tactical first-person shooter that combines precise gunplay with character abilities. Teams compete in strategic matches where players must plant or defuse a spike while coordinating with teammates. The game emphasizes teamwork, communication, and competitive ranking systems.',
'Windows 10 64-bit, Intel i3, 4GB RAM, GTX 1050, 20GB storage',
'New agent released and gameplay balance adjustments.'
),

(19,
'PUBG Battlegrounds is a realistic battle royale game where players parachute onto a large island and search for weapons, equipment, and vehicles to survive. The game focuses on tactical shooting mechanics and strategic positioning as the playable area gradually shrinks.',
'Windows 10 64-bit, Intel i5, 8GB RAM, GTX 1060, 40GB storage',
'Improved anti-cheat system and map performance enhancements.'
),

(20,
'Elden Ring is an open-world action RPG set in a dark fantasy universe. Players explore vast landscapes, fight powerful enemies, and uncover hidden lore while customizing their character builds. The game is known for its challenging combat, deep world design, and immersive storytelling.',
'Windows 10 64-bit, Intel i5, 12GB RAM, GTX 1070, 60GB storage',
'Performance improvements and minor gameplay balancing.'
);

INSERT INTO Software_Detail (software_id, description, system_requirement, release_note)
VALUES

(21,
'Discord is a communication platform widely used by gaming communities and online groups. Users can create servers, voice channels, and text channels to communicate with friends or communities. The application supports voice calls, video calls, screen sharing, and integration with bots that automate server management. Discord has become a central hub for gaming communities and online collaboration.',
'Windows 10 / macOS 11 / Android 8 / iOS 13, 4GB RAM recommended',
'Initial submission for security and functionality review.'
),

(22,
'Snapchat is a multimedia messaging application that allows users to send photos and videos that disappear after being viewed. The platform also offers stories, augmented reality filters, and location-based features. Snapchat is popular among younger users due to its creative tools and social interaction features.',
'Windows 10 / Android 8 / iOS 13',
'Initial version submitted for platform review.'
),

(23,
'Zoom is a video conferencing application used for online meetings, webinars, and virtual collaboration. It provides screen sharing, chat messaging, breakout rooms, and meeting recording features. Zoom became widely adopted for remote work, online education, and global business meetings.',
'Windows 10 / macOS 11 / Android 8 / iOS 13, 4GB RAM',
'Submitted for marketplace security and performance testing.'
),

(24,
'Adobe Photoshop is a professional graphics editing software widely used by photographers, designers, and digital artists. It provides powerful tools for image manipulation, layer editing, color correction, and graphic design. Photoshop is considered an industry standard for digital image editing.',
'Windows 10 64-bit, Intel i5, 8GB RAM, GPU recommended',
'New version submitted for compatibility testing.'
),

(25,
'Notion is an all-in-one productivity workspace used for note taking, task management, documentation, and collaboration. Teams and individuals can organize projects, track tasks, and share knowledge through customizable pages and databases.',
'Windows 10 / macOS 11 / Android 8 / iOS 13',
'Initial build awaiting system validation.'
);	

INSERT INTO Software_Detail (software_id, description, system_requirement, release_note)
VALUES

(26,
'Slack is a professional communication platform designed for teams and workplaces. It allows users to organize conversations into channels, share files, integrate with third-party tools, and manage team collaboration efficiently.',
'Windows 10 / macOS 11 / Android 8 / iOS 13',
'Version passed technical testing and submitted for final approval.'
),

(27,
'Twitch is a live streaming platform focused primarily on gaming content but also supports creative streams, music, and real-life broadcasts. Viewers can interact with streamers through chat, subscriptions, and donations.',
'Windows 10 / macOS 11 / Android 8 / iOS 13',
'Platform features validated during technical review.'
),

(28,
'Genshin Impact is an open-world action role-playing game featuring anime-style graphics and elemental combat mechanics. Players explore a vast fantasy world, complete quests, collect characters, and engage in cooperative multiplayer gameplay.',
'Windows 10 64-bit, Intel i5, 8GB RAM, GTX 1060',
'Gameplay and network features verified during review process.'
),

(29,
'Cyberpunk 2077 is a futuristic open-world role-playing game set in Night City, a technologically advanced but chaotic metropolis. Players take on the role of a mercenary navigating story-driven missions and character upgrades.',
'Windows 10 64-bit, Intel i7, 12GB RAM, GTX 1070',
'Graphics performance and gameplay systems verified.'
),

(30,
'Among Us is a multiplayer party game where players work together to complete tasks while trying to identify impostors among the crew. The game emphasizes social deduction, teamwork, and deception.',
'Windows 10 / Android 8 / iOS 13',
'Multiplayer functionality and matchmaking tested.'
);

INSERT INTO Software_Version
(software_id, version_name, release_note, file_size, is_active)
VALUES

-- ===== ACTIVE APPS =====
(1,'1.0.0','Initial release',15000000,0),
(1,'1.1.0','Bug fixes and UI improvements',15200000,1),

(2,'1.0.0','First release',14000000,0),
(2,'1.2.0','Performance improvements',14500000,1),

(3,'1.0.0','Initial TikTok release',18000000,0),
(3,'1.1.0','Improved recommendation algorithm',18200000,0),
(3,'1.3.0','Security patch',18500000,1),

(4,'1.0.0','First YouTube release',20000000,0),
(4,'1.2.0','UI redesign',20500000,1),

(5,'1.0.0','Spotify initial version',17000000,0),
(5,'1.1.0','Improved music search',17500000,1),

(6,'1.0.0','Netflix first release',21000000,0),
(6,'1.2.0','Improved streaming quality',21500000,1),

(7,'1.0.0','Zalo initial version',16000000,1),

(8,'1.0.0','Telegram first version',15500000,0),
(8,'1.1.0','Security improvements',15800000,1),

(9,'1.0.0','Google Maps launch',22000000,0),
(9,'1.3.0','Navigation improvement',22500000,1),

(10,'1.0.0','WhatsApp initial version',16500000,0),
(10,'1.1.0','Voice call improvements',17000000,1),

-- ===== ACTIVE GAMES =====
(11,'1.0.0','Resident Evil launch version',45000000,0),
(11,'1.1.0','Gameplay fixes',46000000,1),

(12,'1.0.0','GTA V release',55000000,0),
(12,'1.2.0','New online features',57000000,1),

(13,'1.0.0','Warzone initial version',48000000,0),
(13,'1.3.0','New map update',50000000,1),

(14,'1.0.0','League of Legends first version',43000000,0),
(14,'1.1.0','Champion balance patch',43500000,0),
(14,'1.2.0','Client improvements',44000000,1),

(15,'1.0.0','Dota 2 release version',42000000,0),
(15,'1.1.0','Gameplay patch',42500000,1),

(16,'1.0.0','Minecraft initial release',39000000,0),
(16,'1.1.0','New blocks and mobs',40000000,1),

(17,'1.0.0','Fortnite launch version',47000000,0),
(17,'1.1.0','Battle pass system added',48000000,1),

(18,'1.0.0','Valorant first version',46000000,0),
(18,'1.2.0','Agent balance patch',47000000,1),

(19,'1.0.0','PUBG first version',50000000,0),
(19,'1.1.0','Weapon balance update',51000000,1),

(20,'1.0.0','Elden Ring launch',52000000,0),
(20,'1.1.0','Difficulty balance patch',53000000,1),

-- ===== PENDING_REVIEW =====
(21,'1.0.0','Initial submission',12000000,1),
(22,'1.0.0','Initial submission',11000000,1),
(23,'1.0.0','Initial submission',15000000,1),
(24,'1.0.0','Initial submission',25000000,1),
(25,'1.0.0','Initial submission',9000000,1),

-- ===== PENDING_APPROVAL =====
(26,'1.0.0','Initial submission',13000000,1),
(27,'1.0.0','Initial submission',14000000,1),
(28,'1.0.0','Initial submission',30000000,1),
(29,'1.0.0','Initial submission',52000000,1),
(30,'1.0.0','Initial submission',7000000,1);

INSERT INTO Software_Review_Process (software_id, reviewer_id, test_result)
VALUES
(26,2,'Team collaboration features tested successfully'),
(27,2,'Streaming and chat systems tested successfully'),
(28,2,'Gameplay and world exploration tested'),
(29,2,'Performance and gameplay mechanics verified'),
(30,2,'Multiplayer and matchmaking system tested');


<<<<<<< HEAD:FinalDataFivePigs.sql
=======

INSERT INTO Software_Image (software_id,image_url,is_thumbnail) VALUES
(11,'images/RE9_icon.jpg',1),
(11,'images/RE9_detail1.jpg',0),
(11,'images/RE9_detail2.jpg',0),
(11,'images/RE9_detail3.jpg',0),
(11,'images/RE9_detail4.jpg',0);

-- Warzone (ID = 13)
INSERT INTO Software_Image (software_id,image_url,is_thumbnail) VALUES
(13,'images/Warzone_icon.png',1),
(13,'images/Warzone_detail1.jpg',0),
(13,'images/Warzone_detail2.jpg',0),
(13,'images/Warzone_detail3.jpg',0),
(13,'images/Warzone_detail4.jpg',0);

-- PUBG (ID = 19)
INSERT INTO Software_Image (software_id,image_url,is_thumbnail) VALUES
(19,'images/pubg_icon.png',1),
(19,'images/pubg_detail1.png',0),
(19,'images/pubg_detail2.webp',0),
(19,'images/pubg_detail3.jpg',0),
(19,'images/pubg_detail4.jpg',0);

-- Minecraft (ID = 16)
INSERT INTO Software_Image (software_id,image_url,is_thumbnail) VALUES
(16,'images/minecraft.webp',1),
(16,'images/Minecraft_info1.jpg',0),
(16,'images/Minecraft_info2.jpg',0),
(16,'images/minecraft_info3.png',0),
(16,'images/minecraft_info4.jpg',0);

-- Facebook (ID = 1)
INSERT INTO Software_Image (software_id,image_url,is_thumbnail) VALUES
(1,'images/facebook_icon.png',1),
(1,'images/facebook_detail1.png',0),
(1,'images/facebook_detail2.webp',0),
(1,'images/facebook_detail3.jpg',0);

-- Instagram (ID = 2)
INSERT INTO Software_Image (software_id,image_url,is_thumbnail) VALUES
(2,'images/instagram_icon.jpg',1),
(2,'images/ins_detail1.webp',0),
(2,'images/ins_detail2.webp',0),
(2,'images/ins_detail3.webp',0),
(2,'images/ins_detail4.webp',0);

>>>>>>> daab0f27da907699b6d350bf9fc271ab7199585a:DataFivePigs.sql
-- Resident Evil Requiem (ID = 11)
INSERT INTO Software_Image (software_id,image_url,is_thumbnail) VALUES
(11,'uploads/images/RE9_icon.jpg',1),
(11,'uploads/images/RE9_detail1.jpg',0),
(11,'uploads/images/RE9_detail2.jpg',0),
(11,'uploads/images/RE9_detail3.jpg',0),
(11,'uploads/images/RE9_detail4.jpg',0);

-- Warzone (ID = 13)
INSERT INTO Software_Image (software_id,image_url,is_thumbnail) VALUES
(13,'uploads/images/Warzone_icon.png',1),
(13,'uploads/images/Warzone_detail1.jpg',0),
(13,'uploads/images/Warzone_detail2.jpg',0),
(13,'uploads/images/Warzone_detail3.jpg',0),
(13,'uploads/images/Warzone_detail4.jpg',0);

-- PUBG (ID = 19)
INSERT INTO Software_Image (software_id,image_url,is_thumbnail) VALUES
(19,'uploads/images/pubg_icon.png',1),
(19,'uploads/images/pubg_detail1.png',0),
(19,'uploads/images/pubg_detail2.webp',0),
(19,'uploads/images/pubg_detail3.jpg',0),
(19,'uploads/images/pubg_detail4.jpg',0);

-- Minecraft (ID = 16)
INSERT INTO Software_Image (software_id,image_url,is_thumbnail) VALUES
(16,'uploads/images/minecraft.webp',1),
(16,'uploads/images/Minecraft_info1.jpg',0),
(16,'uploads/images/Minecraft_info2.jpg',0),
(16,'uploads/images/minecraft_info3.png',0),
(16,'uploads/images/minecraft_info4.jpg',0);

-- Facebook (ID = 1)
INSERT INTO Software_Image (software_id,image_url,is_thumbnail) VALUES
(1,'uploads/images/facebook_icon.png',1),
(1,'uploads/images/facebook_detail1.png',0),
(1,'uploads/images/facebook_detail2.webp',0),
(1,'uploads/images/facebook_detail3.jpg',0);

-- Instagram (ID = 2)
INSERT INTO Software_Image (software_id,image_url,is_thumbnail) VALUES
(2,'uploads/images/instagram_icon.jpg',1),
(2,'uploads/images/ins_detail1.webp',0),
(2,'uploads/images/ins_detail2.webp',0),
(2,'uploads/images/ins_detail3.webp',0),
(2,'uploads/images/ins_detail4.webp',0);
<<<<<<< HEAD:FinalDataFivePigs.sql
=======

>>>>>>> daab0f27da907699b6d350bf9fc271ab7199585a:DataFivePigs.sql

INSERT INTO Software_Review_Process (software_id, reviewer_id, test_result)
VALUES
(1,2,'Application tested successfully, no malware detected'),
(2,2,'Photo sharing features tested and working'),
(3,2,'Video streaming functionality stable'),
(4,2,'Streaming performance acceptable'),
(5,2,'Music playback tested successfully'),
(6,2,'Subscription system tested'),
(7,2,'Messaging features working correctly'),
(8,2,'Secure communication verified'),
(9,2,'Map navigation accurate'),
(10,2,'Messaging service stable'),

(11,2,'Horror game build tested successfully'),
(12,2,'Open world gameplay verified'),
(13,2,'Battle royale gameplay stable'),
(14,2,'MOBA gameplay functional'),
(15,2,'Multiplayer gameplay verified'),
(16,2,'Sandbox mechanics tested'),
(17,2,'Battle royale servers stable'),
(18,2,'FPS gameplay tested'),
(19,2,'Realistic shooter gameplay tested'),
(20,2,'RPG gameplay mechanics verified');

INSERT INTO Review_Score
(software_id, reviewer_id,
no_malware, no_copyright_violation, no_spam_content,
ui_ux_score, technical_score, performance_score, documentation_score,
total_score, decision, review_note)
VALUES

(1,2,1,1,1,8,8,8,7,7.75,'APPROVED','Facebook security and privacy review passed.'),

(2,2,1,1,1,8,8,8,7,7.75,'APPROVED','Instagram interface and performance validated.'),

(3,2,1,1,1,9,8,8,7,8.00,'APPROVED','TikTok video system and algorithm verified.'),

(4,2,1,1,1,9,9,9,8,8.75,'APPROVED','YouTube streaming platform stable and secure.'),

(5,2,1,1,1,8,8,8,7,7.75,'APPROVED','Spotify music streaming system verified.'),

(6,2,1,1,1,8,8,8,7,7.75,'APPROVED','Netflix streaming and DRM protection verified.'),

(7,2,1,1,1,7,7,7,6,6.75,'APPROVED','Zalo messaging features verified.'),

(8,2,1,1,1,8,8,8,7,7.75,'APPROVED','Telegram encryption and chat system verified.'),

(9,2,1,1,1,9,9,9,8,8.75,'APPROVED','Google Maps navigation accuracy verified.'),

(10,2,1,1,1,8,8,8,7,7.75,'APPROVED','WhatsApp messaging system validated.');

INSERT INTO Review_Score
(software_id, reviewer_id,
no_malware, no_copyright_violation, no_spam_content,
ui_ux_score, technical_score, performance_score, documentation_score,
total_score, decision, review_note)
VALUES

(11,2,1,1,1,9,9,9,8,8.75,'APPROVED','Resident Evil Requiem gameplay and security validated.'),

(12,2,1,1,1,9,9,9,8,8.75,'APPROVED','GTA V open world mechanics verified.'),

(13,2,1,1,1,8,8,8,7,7.75,'APPROVED','Warzone multiplayer stability verified.'),

(14,2,1,1,1,9,9,8,8,8.50,'APPROVED','League of Legends competitive environment validated.'),

(15,2,1,1,1,9,9,8,8,8.50,'APPROVED','Dota 2 gameplay mechanics verified.'),

(16,2,1,1,1,9,8,8,7,8.00,'APPROVED','Minecraft sandbox engine stable.'),

(17,2,1,1,1,8,8,8,7,7.75,'APPROVED','Fortnite gameplay and server system validated.'),

(18,2,1,1,1,9,9,8,8,8.50,'APPROVED','Valorant anti-cheat and gameplay verified.'),

(19,2,1,1,1,8,8,8,7,7.75,'APPROVED','PUBG gameplay and matchmaking tested.'),

(20,2,1,1,1,9,9,9,8,8.75,'APPROVED','Elden Ring performance and gameplay verified.');

INSERT INTO Review_Score
(software_id, reviewer_id,
no_malware, no_copyright_violation, no_spam_content,
ui_ux_score, technical_score, performance_score, documentation_score,
total_score, decision, review_note)
VALUES

(26,2,1,1,1,8,8,8,7,7.75,'APPROVED','Slack passed security and collaboration feature testing.'),

(27,2,1,1,1,8,9,8,7,8.00,'APPROVED','Twitch streaming functionality and moderation system verified.'),

(28,2,1,1,1,9,9,9,8,8.75,'APPROVED','Genshin Impact gameplay and performance verified successfully.'),

(29,2,1,1,1,8,8,8,7,7.75,'APPROVED','Cyberpunk 2077 stability and graphics performance acceptable.'),

(30,2,1,1,1,9,8,8,7,8.00,'APPROVED','Among Us multiplayer and matchmaking system tested.');

INSERT INTO Software_Approval
(software_id, approver_id, decision, approval_note)
VALUES

(1,3,'APPROVED','Application meets platform requirements'),
(2,3,'APPROVED','No policy violations'),
(3,3,'APPROVED','Video features verified'),
(4,3,'APPROVED','Streaming performance excellent'),
(5,3,'APPROVED','Music streaming stable'),
(6,3,'APPROVED','Subscription system validated'),
(7,3,'APPROVED','Messaging application approved'),
(8,3,'APPROVED','Secure messaging verified'),
(9,3,'APPROVED','Navigation service accurate'),
(10,3,'APPROVED','Messaging features stable'),

(11,3,'APPROVED','AAA horror game approved'),
(12,3,'APPROVED','Open world game verified'),
(13,3,'APPROVED','Battle royale gameplay approved'),
(14,3,'APPROVED','MOBA game stable'),
(15,3,'APPROVED','Multiplayer mechanics validated'),
(16,3,'APPROVED','Sandbox game approved'),
(17,3,'APPROVED','Battle royale game verified'),
(18,3,'APPROVED','FPS game approved'),
(19,3,'REJECTED','Performance issues detected'),
(20,3,'APPROVED','RPG game high quality');

INSERT INTO Payment_Status (status_name) VALUES
('Pending'),
('Paid'),
('Failed'),
('Refunded');



INSERT INTO Review (software_id, customer_id, rating, comment) VALUES

-- 1 Facebook (avg 4.3)
(1,7,4,'Good social app'),
(1,8,4,'Easy to connect with friends'),
(1,9,5,'Very useful'),

-- 2 Instagram (avg 4.4)
(2,7,4,'Nice photo sharing'),
(2,10,4,'Simple and fun'),
(2,11,5,'Love the reels'),

-- 3 TikTok (avg 4.5)
(3,8,5,'Very entertaining'),
(3,12,4,'Good short videos'),
(3,13,5,'Addictive content'),

-- 4 YouTube (avg 4.6)
(4,7,5,'Best video platform'),
(4,9,4,'Great content'),
(4,14,5,'Very useful'),

-- 5 Spotify (avg 4.5)
(5,10,5,'Great music app'),
(5,11,4,'Nice playlists'),
(5,12,5,'Love it'),

-- 6 Netflix (avg 4.4)
(6,13,4,'Good movies'),
(6,14,4,'Nice series'),
(6,15,5,'Worth the price'),

-- 7 Zalo (avg 4.2)
(7,7,4,'Good local chat app'),
(7,8,4,'Easy to use'),
(7,9,5,'Works well'),

-- 8 Telegram (avg 4.6)
(8,10,5,'Very secure'),
(8,11,4,'Good messaging'),
(8,12,5,'Fast and clean'),

-- 9 Google Maps (avg 4.7)
(9,13,5,'Best navigation'),
(9,14,5,'Very accurate'),
(9,15,4,'Helpful app'),

-- 10 WhatsApp (avg 4.5)
(10,16,5,'Simple messaging'),
(10,7,4,'Works well'),
(10,8,5,'Reliable'),

-- 11 Resident Evil Requiem (avg 4.8)
(11,9,5,'Amazing horror game'),
(11,10,5,'Great graphics'),
(11,11,4,'Very immersive'),

-- 12 GTA V (avg 4.9)
(12,12,5,'Masterpiece game'),
(12,13,5,'Open world is huge'),
(12,14,5,'Still amazing'),

-- 13 Warzone (avg 4.6)
(13,15,5,'Great battle royale'),
(13,16,4,'Good gameplay'),
(13,7,5,'Fun multiplayer'),

-- 14 League of Legends (avg 4.7)
(14,8,5,'Very competitive'),
(14,9,4,'Fun with friends'),
(14,10,5,'Great MOBA'),

-- 15 Dota 2 (avg 4.6)
(15,11,5,'Deep gameplay'),
(15,12,4,'Hard but fun'),
(15,13,5,'Very strategic'),

-- 16 Minecraft (avg 4.8)
(16,14,5,'Creative freedom'),
(16,15,5,'Great sandbox'),
(16,16,4,'Very fun'),

-- 17 Fortnite (avg 4.5)
(17,7,5,'Fun battle royale'),
(17,8,4,'Nice graphics'),
(17,9,5,'Great gameplay'),

-- 18 Valorant (avg 4.6)
(18,10,5,'Great tactical shooter'),
(18,11,4,'Competitive'),
(18,12,5,'Very fun'),

-- 19 PUBG (avg 4.4)
(19,13,4,'Realistic gameplay'),
(19,14,4,'Good battle royale'),
(19,15,5,'Very intense'),

-- 20 Elden Ring (avg 4.9)
(20,16,5,'One of the best RPGs'),
(20,7,5,'Amazing world'),
(20,8,5,'Perfect gameplay');

INSERT INTO Vendor_Payout
(vendor_id, amount, payment_method, payment_account, status, processed_at)
VALUES
(4, 79.17, 'BANK', '123456789 - Vietcombank', 'PAID', NOW()),
(5, 359.94, 'MOMO', '0901234567', 'PAID', NOW()),
(6, 103.98, 'BANK', '987654321 - Techcombank', 'PENDING', NULL);


INSERT INTO License 
(license_key, pricing_id, software_id, owner_id, purchase_date, expire_date, status)
VALUES

-- ===== Netflix (BASIC) =====
('LIC-NF-001',21,6,7,NOW(),DATE_ADD(NOW(),INTERVAL 1 YEAR),'ACTIVE'),
('LIC-NF-002',21,6,8,NOW(),DATE_ADD(NOW(),INTERVAL 1 YEAR),'ACTIVE'),

-- ===== Netflix (TEAM) =====
('LIC-NF-T01',22,6,9,NOW(),DATE_ADD(NOW(),INTERVAL 1 YEAR),'ACTIVE'),

-- ===== Resident Evil =====
('LIC-RE-001',23,11,10,NOW(),DATE_ADD(NOW(),INTERVAL 1 YEAR),'ACTIVE'),
('LIC-RE-002',23,11,11,NOW(),DATE_ADD(NOW(),INTERVAL 1 YEAR),'ACTIVE'),

-- ===== GTA V =====
('LIC-GTA-001',24,12,12,NOW(),DATE_ADD(NOW(),INTERVAL 1 YEAR),'ACTIVE'),
('LIC-GTA-002',24,12,13,NOW(),DATE_ADD(NOW(),INTERVAL 1 YEAR),'ACTIVE'),

-- ===== Minecraft =====
('LIC-MC-001',25,16,14,NOW(),DATE_ADD(NOW(),INTERVAL 1 YEAR),'ACTIVE'),

-- ===== Elden Ring =====
('LIC-ER-001',26,20,15,NOW(),DATE_ADD(NOW(),INTERVAL 1 YEAR),'ACTIVE'),

-- ===== Photoshop =====
('LIC-PS-001',27,24,16,NOW(),DATE_ADD(NOW(),INTERVAL 1 YEAR),'ACTIVE'),
('LIC-PS-PRO01',28,24,7,NOW(),DATE_ADD(NOW(),INTERVAL 1 YEAR),'ACTIVE'),

-- ===== Slack =====
('LIC-SLK-001',30,26,10,NOW(),DATE_ADD(NOW(),INTERVAL 1 YEAR),'ACTIVE'),
('LIC-SLK-T01',31,26,11,NOW(),DATE_ADD(NOW(),INTERVAL 1 YEAR),'ACTIVE'),

-- ===== Cyberpunk =====
('LIC-CYB-001',32,29,12,NOW(),DATE_ADD(NOW(),INTERVAL 1 YEAR),'ACTIVE'),

-- ===== Among Us =====
('LIC-AM-001',33,30,13,NOW(),DATE_ADD(NOW(),INTERVAL 1 YEAR),'ACTIVE');


INSERT INTO Vendor_Payout 
(payout_id, vendor_id, amount, payment_method, payment_account, status, processed_at, created_at)
VALUES
(1001,4,120.50,'BANK','VCB-10001111','PAID','2026-02-01 10:05:00','2026-02-01 09:00:00'),
(1002,5,280.00,'MOMO','0900000012','PAID','2026-02-02 11:10:00','2026-02-02 09:20:00'),
(1003,6,95.75,'PAYPAL','vendor3@gmail.com','PAID','2026-02-03 15:00:00','2026-02-03 08:45:00'),
(1004,4,340.10,'BANK','ACB-10004444','PAID','2026-02-04 16:30:00','2026-02-04 10:00:00'),
(1005,5,150.25,'MOMO','0900000012','PAID','2026-02-05 14:20:00','2026-02-05 09:40:00'),
(1006,6,420.00,'BANK','TCB-10006666','PAID','2026-02-06 13:15:00','2026-02-06 09:10:00'),
(1007,4,78.90,'PAYPAL','vendor1@gmail.com','PAID','2026-02-07 12:00:00','2026-02-07 09:05:00'),
(1008,5,510.40,'BANK','BIDV-10008888','PAID','2026-02-08 17:45:00','2026-02-08 10:20:00'),
(1009,6,230.30,'MOMO','0900000013','PAID','2026-02-09 11:50:00','2026-02-09 09:30:00'),
(1010,4,199.99,'BANK','VIB-10001010','PAID','2026-02-10 15:35:00','2026-02-10 10:10:00'),
(1011,5,88.80,'MOMO','0900000012','PENDING',NULL,'2026-02-11 09:15:00'),
(1012,6,132.45,'PAYPAL','vendor3@gmail.com','PENDING',NULL,'2026-02-12 10:05:00'),
(1013,4,260.00,'BANK','VCB-10001111','PENDING',NULL,'2026-02-13 11:25:00'),
(1014,5,315.60,'BANK','BIDV-10008888','PENDING',NULL,'2026-02-14 08:55:00'),
(1015,6,44.20,'MOMO','0900000013','PENDING',NULL,'2026-02-15 09:40:00'),
(1016,4,505.00,'PAYPAL','vendor1@gmail.com','PENDING',NULL,'2026-02-16 13:00:00'),
(1017,5,72.15,'BANK','ACB-10005555','PENDING',NULL,'2026-02-17 14:45:00'),
(1018,6,189.00,'BANK','TCB-10006666','PENDING',NULL,'2026-02-18 10:35:00'),
(1019,4,640.70,'MOMO','0900000011','PENDING',NULL,'2026-02-19 15:25:00'),
(1020,5,54.99,'PAYPAL','vendor2@gmail.com','PENDING',NULL,'2026-02-20 16:05:00');

-- Seed 10 successful admin payout audits (APPROVE)
INSERT INTO Admin_Payout_Audit
(audit_id, payout_id, admin_user_id, action, from_status, to_status, note, created_at)
VALUES
(2001,1001,1,'APPROVE','PENDING','PAID','Approved monthly payout request','2026-02-01 10:06:00'),
(2002,1002,1,'APPROVE','PENDING','PAID','Approved monthly payout request','2026-02-02 11:11:00'),
(2003,1003,1,'APPROVE','PENDING','PAID','Approved monthly payout request','2026-02-03 15:01:00'),
(2004,1004,1,'APPROVE','PENDING','PAID','Approved monthly payout request','2026-02-04 16:31:00'),
(2005,1005,1,'APPROVE','PENDING','PAID','Approved monthly payout request','2026-02-05 14:21:00'),
(2006,1006,1,'APPROVE','PENDING','PAID','Approved monthly payout request','2026-02-06 13:16:00'),
(2007,1007,1,'APPROVE','PENDING','PAID','Approved monthly payout request','2026-02-07 12:01:00'),
(2008,1008,1,'APPROVE','PENDING','PAID','Approved monthly payout request','2026-02-08 17:46:00'),
(2009,1009,1,'APPROVE','PENDING','PAID','Approved monthly payout request','2026-02-09 11:51:00'),
(2010,1010,1,'APPROVE','PENDING','PAID','Approved monthly payout request','2026-02-10 15:36:00');

INSERT INTO Report (software_id, reporter_id, reason, status, created_at)
VALUES
(1, 6, 'Phần mềm bị crash khi mở ở Windows 11. Sau khi bấm Launch thì ứng dụng tự tắt.', 'ERROR_REVIEW', NOW()),
(2, 7, 'Không thể đăng nhập dù nhập đúng tài khoản. Hệ thống báo lỗi unknown error.', 'ERROR_REVIEW', NOW()),
(3, 8, 'Sau khi cài đặt xong, phần mềm không hiển thị giao diện chính.', 'ERROR_REVIEW', NOW());

INSERT INTO Report (
    software_id,
    reporter_id,
    reviewer_id,
    reason,
    status,
    bug_confirmed,
    reproduce_steps,
    reviewer_note,
    created_at,
    processed_at
)
VALUES
(
    1,
    6,
    4,
    'Phần mềm bị crash khi import file PDF lớn hơn 20MB.',
    'ERROR_APPROVAL',
    1,
    '1. Tai app ve may
2. Dang nhap tai khoan reviewer
3. Chon chuc nang import PDF
4. Upload file lon hon 20MB
5. App bi vang ra ngay sau khi xu ly',
    'Đã kiểm tra và xác nhận lỗi đúng như customer report. Có thể tái hiện ổn định.',
    NOW(),
    NOW()
),
(
    2,
    7,
    4,
    'Customer báo lỗi không lưu được project sau khi chỉnh sửa.',
    'ERROR_REJECTED',
    0,
    '1. Tai app
2. Mo project co san
3. Chinh sua noi dung
4. Bam Save
5. He thong luu binh thuong',
    'Không tái hiện được lỗi. Chức năng hoạt động bình thường trong môi trường test.',
    NOW(),
    NOW()
);

INSERT INTO Notification 
(user_id, title, content, type, priority, related_url)
VALUES 

-- ===== PENDING_APPROVAL =====
(3,
'Software Awaiting Approval - Discord',
'Software "Discord" has passed review and is waiting for your approval.',
'PENDING_APPROVAL',
'MEDIUM',
'/approval_pending_detail?softwareId=21'),

(3,
'Software Awaiting Approval - Slack',
'Software "Slack" is ready for approval after successful review.',
'PENDING_APPROVAL',
'MEDIUM',
'/approval_pending_detail?softwareId=26'),

(3,
'Software Awaiting Approval - Genshin Impact',
'"Genshin Impact" has completed review and is pending your decision.',
'PENDING_APPROVAL',
'MEDIUM',
'/approval_pending_detail?softwareId=28'),

(3,
'Software Awaiting Approval - Cyberpunk 2077',
'"Cyberpunk 2077" is now waiting for approval.',
'PENDING_APPROVAL',
'MEDIUM',
'/approval_pending_detail?softwareId=29'),

(3,
'Software Awaiting Approval - Zoom',
'"Zoom" has been reviewed and is ready for approval.',
'PENDING_APPROVAL',
'MEDIUM',
'/approval_pending_detail?softwareId=23'),

-- ===== REJECTED =====
(3,
'Software Rejected by Reviewer - Snapchat',
'"Snapchat" has been rejected during review. Please check details.',
'REJECTED',
'HIGH',
'/approval_pending_detail?softwareId=22'),

(3,
'Software Rejected by Reviewer - Notion',
'"Notion" was rejected due to performance issues.',
'REJECTED',
'HIGH',
'/approval_pending_detail?softwareId=25'),

(3,
'Software Rejected by Reviewer - Twitch',
'"Twitch" failed security checks during review.',
'REJECTED',
'HIGH',
'/approval_pending_detail?softwareId=27'),

(3,
'Software Rejected by Reviewer - Photoshop',
'"Adobe Photoshop" was rejected due to licensing issues.',
'REJECTED',
'HIGH',
'/approval_pending_detail?softwareId=24'),

(3,
'Software Rejected by Reviewer - Discord',
'"Discord" was rejected due to unstable build.',
'REJECTED',
'HIGH',
'/approval_pending_detail?softwareId=21');

