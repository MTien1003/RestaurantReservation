package com.restaurant.reservationsystem.dao;

import com.restaurant.reservationsystem.config.DatabaseConfig;
import com.restaurant.reservationsystem.models.categories;
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
        String checkData ="SELECT product_id FROM cart WHERE product_id = ?";
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


    public ObservableList<categories> getAllCategories() {
        ObservableList<categories> categoryList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM category";
        try (Connection connect = DatabaseConfig.getConnection();
             Statement statement = connect.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {
                categoryList.add(new categories(
                        result.getString("product_id"),
                        result.getString("product_name"),
                        result.getString("product_type"),
                        result.getString("price"),
                        result.getString("status")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    public boolean addProduct(String productId, String productName, String productType, String price, String status) {
        String sql = "INSERT INTO category (product_id, product_name, product_type, price, status) VALUES (?, ?, ?, ?, ?)";
        connect = DatabaseConfig.getConnection();
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, productId);
            prepare.setString(2, productName);
            prepare.setString(3, productType);
            prepare.setString(4, price);
            prepare.setString(5, status);
            prepare.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateProduct(String productId, String productName, String productType, String price, String status) {
        String sql = "UPDATE category SET product_name=?, product_type=?, price=?, status=? WHERE product_id=?";
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setString(1, productName);
            prepare.setString(2, productType);
            prepare.setString(3, price);
            prepare.setString(4, status);
            prepare.setString(5, productId);

            int rowsUpdated = prepare.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(String productId) {
        String sql = "DELETE FROM category WHERE product_id = ?";
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
