-- This script creates the database and table needed for the Student Management System.
-- Run this in your MySQL client (like MySQL Workbench or the command-line) before starting the application.

-- 1. Create the database (Optional: if it doesn't exist)
-- If the database already exists, this line will cause an error, which is safe to ignore.
drop database studentdb;
CREATE DATABASE studentdb;

-- 2. Select the database to use
USE studentdb;

-- 3. Create the 'students' table
-- This table will store all the student information.
CREATE TABLE students (
    id INT PRIMARY KEY,        -- Unique ID for each student (Primary Key)
    name VARCHAR(100) NOT NULL,             -- Student's name, cannot be empty
    email VARCHAR(100) NOT NULL UNIQUE      -- Student's email, must be unique and cannot be empty
);

-- 5. (Optional) Verify the data was inserted
SELECT * FROM students;
