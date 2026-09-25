CREATE DATABASE IF NOT EXISTS online_exam;
USE online_exam;
CREATE TABLE users (username VARCHAR(50) PRIMARY KEY, password_hash VARCHAR(255) NOT NULL, role VARCHAR(20) DEFAULT 'STUDENT');
CREATE TABLE questions (id INT AUTO_INCREMENT PRIMARY KEY, subject VARCHAR(50) NOT NULL, question_text TEXT NOT NULL, option1 TEXT NOT NULL, option2 TEXT NOT NULL, option3 TEXT NOT NULL, option4 TEXT NOT NULL, correct_index INT NOT NULL);
CREATE TABLE results (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(50) NOT NULL, subject VARCHAR(50) NOT NULL, correct INT, wrong INT, unanswered INT, score DECIMAL(8,2), status VARCHAR(10), attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);
INSERT INTO users VALUES ('student','student','STUDENT'), ('admin','admin','ADMIN');
