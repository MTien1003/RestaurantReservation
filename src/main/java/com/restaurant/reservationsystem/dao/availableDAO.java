package com.restaurant.reservationsystem.dao;

import com.restaurant.reservationsystem.config.DatabaseConfig;
import com.restaurant.reservationsystem.models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class availableDAO {
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    public boolean isProductIdExists(String productId){
        String checkData ="SELECT ProductId FROM Product WHERE ProductId = ?";
        connect = DatabaseConfig.getConnection();
        try{
            prepare=connect.prepareStatement(checkData);
            prepare.setString(1, productId);
            result = prepare.executeQuery();
            return result.next();
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public ObservableList<Product> getAllProduct() {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Product p JOIN Categories c ON c.CategoryName= p.CategoryName";
        try (Connection connect = DatabaseConfig.getConnection();
             Statement statement = connect.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {
                productList.add(new Product(
                        result.getString("ProductId"),
                        result.getString("ProductName"),
                        result.getString("CategoryName"),
                        result.getFloat("Price"),
                        result.getString("Status"),
                        result.getInt("Quantity")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }

    public boolean addProduct(String productId, String productName, String productType, String price, String status, int quantity) {
        String sql = "INSERT INTO Product (ProductId, ProductName, CategoryName, Price, Status, Quantity) VALUES (?, ?, ?, ?, ?, ?)";
        connect = DatabaseConfig.getConnection();
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, productId);
            prepare.setString(2, productName);
            prepare.setString(3, productType);
            prepare.setString(4, price);
            prepare.setString(5, status);
            prepare.setInt(6, quantity);
            prepare.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProduct(String productId, String productName, String productType, String price, String status, int quantity) {
        String sql = "UPDATE Product SET ProductName=?, CategoryName=?, Price=?, Status=?, Quantity=? WHERE ProductId=?";
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {
                prepare.setString(1, productName);
                prepare.setString(2, productType);
                prepare.setString(3, price);
                prepare.setString(4, status);
                prepare.setInt(5, quantity);
                prepare.setString(6, productId);

            int rowsUpdated = prepare.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(String productId) {
        String sql = "DELETE FROM Product WHERE ProductId = ?";
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {
            prepare.setString(1, productId);
            int rowsDeleted = prepare.executeUpdate();
            return rowsDeleted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
