package com.restaurant.reservationsystem.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig  {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Nha_Hang;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456789";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection (URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}