package com.restaurant.reservationsystem.dao;

import com.restaurant.reservationsystem.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class dashboardDAO {

    public int getTotalCustomerOrder() {
        String sql = "SELECT COUNT(DISTINCT CustomerPhone) AS total_customerOrder FROM Invoice";
        int totalCount = 0;
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql);
             ResultSet result = prepare.executeQuery()) {

            if (result.next()) {
                totalCount = result.getInt("total_customerOrder");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalCount;
    }

    public double getTotalIncomeForToday() {
        String sql =" SELECT SUM(Total) AS total_income FROM Invoice WHERE Date = ?";
        double totalIncome = 0;
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {
            java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
            prepare.setDate(1, sqlDate);
            try (ResultSet result = prepare.executeQuery()) {
                if (result.next()) {
                    totalIncome = result.getDouble("total_income");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalIncome;
    }

    public double getTotalIncome() {
        String sql = "SELECT SUM(Total) AS total_income FROM Invoice";
        double totalIncome = 0;
        try (Connection connect = DatabaseConfig.getConnection();
             Statement statement = connect.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            if (result.next()) {
                totalIncome = result.getDouble("total_income");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalIncome;
    }

    public Map<String, Integer> getNumberOfOrdersByDate() {
        String sql = "SELECT Date, COUNT(DISTINCT CustomerPhone) AS total_count FROM Invoice GROUP BY Date ORDER BY Date ASC";
        Map<String, Integer> ordersByDate = new LinkedHashMap<>();
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql);
             ResultSet result = prepare.executeQuery()) {

            while (result.next()) {
                ordersByDate.put(result.getString("Date"), result.getInt("total_count"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ordersByDate;
    }

    public Map<String, Double> getIncomeChartData() {
        String sql = "SELECT Date, SUM(Total) AS total_price FROM Invoice GROUP BY Date ORDER BY Date ASC";
        Map<String, Double> incomeChartData = new LinkedHashMap<>();
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql);
             ResultSet result = prepare.executeQuery()) {

            while (result.next()) {
                incomeChartData.put(result.getString("Date"), result.getDouble("total_price"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return incomeChartData;
    }
}
