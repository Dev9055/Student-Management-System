package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing the database connection.
 * It provides a single static method to get a connection.
 */
public class DatabaseConnection {
    
    // --- Database Credentials ---
    // TODO: In a real application, load these from a 'resources/config.properties' file.
    
    // Replace 'studentdb' with your database name if different
    private static final String URL = "jdbc:mysql://localhost:3306/studentdb"; 
    
    // Replace 'root' with your MySQL username
    private static final String USER = "root"; 
    
    // !! IMPORTANT: Replace 'your_password' with your actual MySQL password !!
    private static final String PASSWORD = "500124743"; 

    /**
     * Attempts to establish a connection to the database.
     * * @return A Connection object.
     * @throws RuntimeException if the database driver is not found or connection fails.
     */
    public static Connection getConnection() {
        try {
            // 1. Load the MySQL JDBC Driver
            // This line is technically optional in modern JDBC (Type 4) drivers,
            // but it's good practice for compatibility.
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 2. Attempt to get the connection
            return DriverManager.getConnection(URL, USER, PASSWORD);
            
        } catch (ClassNotFoundException e) {
            // This error happens if you forgot to add the mysql-connector-java.jar to your 'lib' folder
            throw new RuntimeException("Error: MySQL JDBC Driver not found!", e);
        } catch (SQLException e) {
            // This error happens if the URL, user, or password is wrong, or if the DB server is not running
            throw new RuntimeException("Error: Could not connect to the database!", e);
        }
    }
}