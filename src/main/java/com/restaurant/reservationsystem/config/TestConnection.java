package com.restaurant.reservationsystem.config;


import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            System.out.println("Kết nối thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}