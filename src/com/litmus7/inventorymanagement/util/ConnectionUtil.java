package com.litmus7.inventorymanagement.util;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionUtil {
    
    private static Properties properties = new Properties();
    static {
        try {
            properties.load(new FileInputStream("jdbc.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static Connection connection() throws SQLException {
        Connection connection = null;
        try {
            String username = properties.getProperty("user");
            String password = properties.getProperty("password");
            String url = properties.getProperty("dburl");
            
            if (properties.getProperty("flag") != null) {
                // DB already created
                connection = DriverManager.getConnection(url, username, password);
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS inventory (" +
                        "SKU INT AUTO_INCREMENT, " +
                        "ProductName VARCHAR(255), " +
                        "Quantity INT, " +
                        "Price DECIMAL(10,2), " +
                        "PRIMARY KEY (SKU))"
                    );
                }
            } else {
                // DB creation
                String baseUrl = url.substring(0, url.lastIndexOf("/"));
                try (Connection baseConn = DriverManager.getConnection(baseUrl, username, password);
                     Statement baseStmt = baseConn.createStatement()) {
                    baseStmt.executeUpdate("CREATE DATABASE IF NOT EXISTS Inventory");
                }
                
                // Connect to DB
                connection = DriverManager.getConnection(url, username, password);
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS inventory (" +
                        "SKU INT , " +
                        "ProductName VARCHAR(255), " +
                        "Quantity INT, " +
                        "Price DECIMAL(10,2), " +
                        "PRIMARY KEY (SKU))"
                    );
                }
                
                // Save flag to properties file
                properties.setProperty("flag", "true");
                properties.store(new FileOutputStream("jdbc.properties"), null);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

	public PreparedStatement prepareStatement(String query) {
		// TODO Auto-generated method stub
		return null;
	}
}
