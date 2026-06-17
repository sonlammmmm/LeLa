-- Mock Data for LeLa Flashcard Platform
-- Target: MySQL 8+
-- Usage: Run this script manually in your local or test database after Flyway migrations are applied.

SET FOREIGN_KEY_CHECKS = 0;

-- 1. Languages
INSERT INTO languages (id, language_code, name, native_name, flag_url, is_active) VALUES
(1, 'EN', 'English', 'English', 'https://flagcdn.com/w40/us.png', TRUE),
(2, 'VI', 'Vietnamese', 'Tiếng Việt', 'https://flagcdn.com/w40/vn.png', TRUE),
(3, 'JA', 'Japanese', '日本語', 'https://flagcdn.com/w40/jp.png', TRUE)
ON DUPLICATE KEY UPDATE id=id;

-- 2. Roles
INSERT INTO roles (id, role_code, name, description, is_active) VALUES
(1, 'ADMIN', 'Administrator', 'System administrator with full access', TRUE),
(2, 'MODERATOR', 'Moderator', 'Content moderator', TRUE),
(3, 'LEARNER', 'Learner', 'Standard user', TRUE),
(4, 'PRO_LEARNER', 'Pro Learner', 'Premium user with subscription', TRUE)
ON DUPLICATE KEY UPDATE id=id;

-- 3. Users
-- password hash is for 'password123' (bcrypt)
INSERT INTO users (id, username, email, password_hash, full_name, native_language_id, target_language_id, status, timezone, daily_goal_cards, xp_total, streak_current, streak_longest) VALUES
(1, 'admin', 'admin@lela.com', '$2a$10$wN2L0a3d4S5y2wZ7k0V.QOvX7W.oN6zJp5z5wZ/P0t7xPq9rP1UaG', 'System Admin', 2, 1, 'ACTIVE', 'Asia/Ho_Chi_Minh', 10, 5000, 10, 15),
(2, 'nguyenvana', 'nguyenvana@example.com', '$2a$10$wN2L0a3d4S5y2wZ7k0V.QOvX7W.oN6zJp5z5wZ/P0t7xPq9rP1UaG', 'Nguyen Van A', 2, 3, 'ACTIVE', 'Asia/Ho_Chi_Minh', 20, 1500, 5, 5),
(3, 'tranb', 'tranb@example.com', '$2a$10$wN2L0a3d4S5y2wZ7k0V.QOvX7W.oN6zJp5z5wZ/P0t7xPq9rP1UaG', 'Tran Thi B', 2, 1, 'ACTIVE', 'Asia/Ho_Chi_Minh', 15, 300, 1, 2)
ON DUPLICATE KEY UPDATE id=id;

-- 4. User Roles
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1),
(1, 3),
(2, 4),
(3, 3)
ON DUPLICATE KEY UPDATE user_id=user_id;

-- 5. Subscription Plans
INSERT INTO subscription_plans (id, plan_code, name, price, currency_code, billing_cycle, max_owned_decks, max_cards_per_deck, max_daily_reviews, quiz_enabled, leaderboard_enabled, offline_enabled, is_active) VALUES
(1, 'FREE', 'Free Plan', 0, 'VND', 'FREE', 3, 50, 100, FALSE, TRUE, FALSE, TRUE),
(2, 'PRO_MONTHLY', 'Pro Monthly', 59000, 'VND', 'MONTHLY', -1, -1, -1, TRUE, TRUE, TRUE, TRUE)
ON DUPLICATE KEY UPDATE id=id;

-- 6. User Subscriptions
INSERT INTO user_subscriptions (id, user_id, plan_id, status, started_at, expires_at, auto_renew) VALUES
(1, 2, 2, 'ACTIVE', NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), TRUE),
(2, 3, 1, 'ACTIVE', NOW(), NULL, FALSE)
ON DUPLICATE KEY UPDATE id=id;

-- 7. Decks
INSERT INTO decks (id, deck_code, slug, title, description, owner_id, language_id, category, difficulty, visibility, status, is_featured, total_cards) VALUES
(1, 'D-JPN-N5-01', 'japanese-n5-vocabulary-1', 'JLPT N5 Vocabulary (Part 1)', 'Essential vocabulary for the JLPT N5 exam.', 1, 3, 'Vocabulary', 'BEGINNER', 'PUBLIC', 'PUBLISHED', TRUE, 3),
(2, 'D-ENG-COMM', 'english-communication-basics', 'English Communication Basics', 'Common phrases for daily English communication.', 1, 1, 'Communication', 'BEGINNER', 'PUBLIC', 'PUBLISHED', FALSE, 2)
ON DUPLICATE KEY UPDATE id=id;

-- 8. Flashcards
INSERT INTO flashcards (id, deck_id, front_text, back_text, phonetic, example_text, card_order, is_active, created_by) VALUES
(1, 1, 'こんにちは', 'Hello / Good afternoon', 'Konnichiwa', 'こんにちは、お元気ですか？ (Hello, how are you?)', 1, TRUE, 1),
(2, 1, 'ありがとう', 'Thank you', 'Arigatou', '手伝ってくれてありがとう。 (Thank you for helping me.)', 2, TRUE, 1),
(3, 1, 'さようなら', 'Goodbye', 'Sayounara', 'また明日、さようなら。 (See you tomorrow, goodbye.)', 3, TRUE, 1),
(4, 2, 'How are you?', 'Bạn khỏe không?', 'haʊ ɑːr juː', 'Hi, how are you today?', 1, TRUE, 1),
(5, 2, 'Nice to meet you', 'Rất vui được gặp bạn', 'naɪs tu mi:t ju:', 'Nice to meet you too.', 2, TRUE, 1)
ON DUPLICATE KEY UPDATE id=id;

-- 9. Tags
INSERT INTO tags (id, name, slug) VALUES
(1, 'JLPT N5', 'jlpt-n5'),
(2, 'Greeting', 'greeting'),
(3, 'Basic', 'basic')
ON DUPLICATE KEY UPDATE id=id;

-- 10. Flashcard Tags
INSERT INTO flashcard_tags (flashcard_id, tag_id) VALUES
(1, 1), (1, 2),
(2, 1), (2, 3),
(3, 1), (3, 2),
(4, 2), (4, 3),
(5, 2), (5, 3)
ON DUPLICATE KEY UPDATE flashcard_id=flashcard_id;

-- 11. Quizzes
INSERT INTO quizzes (id, deck_id, quiz_code, title, description, quiz_type, pass_score, max_attempts, total_questions, created_by) VALUES
(1, 1, 'Q-JPN-N5-01', 'JLPT N5 Vocab Quiz 1', 'Test your knowledge on basic Japanese greetings.', 'MULTIPLE_CHOICE', 50.0, 3, 2, 1)
ON DUPLICATE KEY UPDATE id=id;

-- 12. Quiz Questions
INSERT INTO quiz_questions (id, quiz_id, source_card_id, question_text, question_type, points, display_order) VALUES
(1, 1, 1, 'What is the Japanese word for "Hello / Good afternoon"?', 'MULTIPLE_CHOICE', 10, 1),
(2, 1, 2, 'What does "ありがとう (Arigatou)" mean?', 'MULTIPLE_CHOICE', 10, 2)
ON DUPLICATE KEY UPDATE id=id;

-- 13. Quiz Question Options
INSERT INTO quiz_question_options (id, question_id, option_key, option_text, is_correct, display_order) VALUES
(1, 1, 'A', 'さようなら (Sayounara)', FALSE, 1),
(2, 1, 'B', 'こんにちは (Konnichiwa)', TRUE, 2),
(3, 1, 'C', 'ありがとう (Arigatou)', FALSE, 3),
(4, 1, 'D', 'すみません (Sumimasen)', FALSE, 4),

(5, 2, 'A', 'Goodbye', FALSE, 1),
(6, 2, 'B', 'Sorry', FALSE, 2),
(7, 2, 'C', 'Thank you', TRUE, 3),
(8, 2, 'D', 'Yes', FALSE, 4)
ON DUPLICATE KEY UPDATE id=id;

SET FOREIGN_KEY_CHECKS = 1;

-- Mock data insertion completed.
