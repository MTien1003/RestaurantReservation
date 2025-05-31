package com.restaurant.reservationsystem.dao;

import com.restaurant.reservationsystem.config.DatabaseConfig;
import com.restaurant.reservationsystem.models.Orders;
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

    public boolean addOrder( int customerId, String productId, int quantity, java.sql.Date date) {
        String sql = "INSERT INTO Orders ( product_id, customer_id, date, quantity) VALUES ( ?, ?, ?, ?)";
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {


            prepare.setString(1, productId);
            prepare.setInt(2, customerId);
            prepare.setDate(3, date);
            prepare.setInt(4, quantity);


            prepare.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Object> getProductDetails(String productId) {
        String sql = "SELECT type, price FROM Product WHERE product_id = ?";
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
            String sql = "INSERT INTO Invoice(customer_id,total, date) VALUES(?, ?, ?)";
            try (Connection connect = DatabaseConfig.getConnection();
                 PreparedStatement prepare = connect.prepareStatement(sql)) {

                prepare.setInt(1,  customerId);
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
        String sql = "SELECT SUM(o.quantity * p.price) AS total_price FROM Orders o JOIN Product p ON o.product_id = p.product_id WHERE customer_id = ?";

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

    public ObservableList<Orders> getOrderListData(int customerId) {
        ObservableList<Orders> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Orders o JOIN Product p ON o.product_id=p.product_id WHERE customer_id = ?";
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setInt(1, customerId);
            try (ResultSet result = prepare.executeQuery()) {
                while (result.next()) {
                    Orders order = new Orders(
                            result.getInt("order_id"),
                            result.getString("product_id"),
                            result.getString("product_name"),
                            result.getString("type"),
                            result.getDouble("price"),
                            result.getInt("quantity")
                    );
                    listData.add(order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }


    public boolean removeOrder(int orderId) {
        String sql = "DELETE FROM Orders WHERE order_id = ?";
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setInt(1, orderId);
            prepare.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public int getNextCustomerId() {
        String sql = "SELECT MAX(customer_id) AS max_id FROM Customer";
        int nextId = 1; // Mặc định nếu bảng rỗng

        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement stmt = connect.prepareStatement(sql);
             ResultSet result = stmt.executeQuery()) {

            if (result.next()) {
                nextId = result.getInt("max_id") + 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return nextId;
    }



    public ObservableList<String> getAvailableProductIds() {
        String sql = "SELECT product_id FROM Product WHERE status='Available'";
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
        String sql = "SELECT product_name FROM Product WHERE product_id = ?";
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
