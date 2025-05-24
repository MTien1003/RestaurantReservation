package com.restaurant.reservationsystem.dao;

import com.restaurant.reservationsystem.config.DatabaseConfig;
import com.restaurant.reservationsystem.models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class orderDAO {
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    public boolean addOrder(int customerId, String productId, String productName, String type, double price, int quantity, java.sql.Date date) {
        String sql = "INSERT INTO product (customer_id, product_id, product_name, type, price, quantity, date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setInt(1, customerId);
            prepare.setString(2, productId);
            prepare.setString(3, productName);
            prepare.setString(4, type);
            prepare.setDouble(5, price);
            prepare.setInt(6, quantity);
            prepare.setDate(7, date);

            prepare.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Object> getProductDetails(String productId) {
        String sql = "SELECT type, price FROM category WHERE product_id = ?";
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setString(1, productId);
            try (ResultSet result = prepare.executeQuery()) {
                if (result.next()) {
                    Map<String, Object> productDetails = new HashMap<>();
                    productDetails.put("type", result.getString("type"));
                    productDetails.put("price", result.getDouble("price"));
                    return productDetails;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }





        public boolean addPayment(int customerId, double total, Date date) {
            String sql = "INSERT INTO product_info(customer_id, total, date) VALUES(?, ?, ?)";
            try (Connection connect = DatabaseConfig.getConnection();
                 PreparedStatement prepare = connect.prepareStatement(sql)) {

                prepare.setInt(1, customerId);
                prepare.setDouble(2, total);
                prepare.setDate(3, date);

                prepare.executeUpdate();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

    public double getTotalPrice(int customerId) {
        String sql = "SELECT SUM(price) AS total_price FROM product WHERE customer_id = ?";
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setInt(1, customerId);
            try (ResultSet result = prepare.executeQuery()) {
                if (result.next()) {
                    return result.getDouble("total_price");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ObservableList<Product> getOrderListData(int customerId) {
        ObservableList<Product> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM product WHERE customer_id = ?";
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setInt(1, customerId);
            try (ResultSet result = prepare.executeQuery()) {
                while (result.next()) {
                    Product prod = new Product(
                            result.getInt("id"),
                            result.getString("product_id"),
                            result.getString("product_name"),
                            result.getString("type"),
                            result.getDouble("price"),
                            result.getInt("quantity")
                    );
                    listData.add(prod);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }


    public boolean removeOrder(int itemId) {
        String sql = "DELETE FROM product WHERE id = ?";
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setInt(1, itemId);
            prepare.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public int getNextCustomerId() {
        String sql = "SELECT customer_id FROM product";
        String checkData = "SELECT customer_id FROM product_info";
        int customerId = 0;

        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement productStmt = connect.prepareStatement(sql);
             ResultSet productResult = productStmt.executeQuery();
             Statement statement = connect.createStatement();
             ResultSet infoResult = statement.executeQuery(checkData)) {

            if (productResult.next()) {
                customerId = productResult.getInt("customer_id");
            }

            int customerInfoId = 0;
            if (infoResult.next()) {
                customerInfoId = infoResult.getInt("customer_id");
            }

            customerId = Math.max(customerId, customerInfoId) + 1;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return customerId;
    }


    public ObservableList<String> getAvailableProductIds() {
        String sql = "SELECT product_id FROM category WHERE status='Available'";
        ObservableList<String> productIds = FXCollections.observableArrayList();

        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql);
             ResultSet result = prepare.executeQuery()) {

            while (result.next()) {
                productIds.add(result.getString("product_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productIds;
    }


    public ObservableList<String> getProductNames(String productId) {
        String sql = "SELECT product_name FROM category WHERE product_id = ?";
        ObservableList<String> productNames = FXCollections.observableArrayList();

        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setString(1, productId);
            try (ResultSet result = prepare.executeQuery()) {
                while (result.next()) {
                    productNames.add(result.getString("product_name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productNames;
    }


}
