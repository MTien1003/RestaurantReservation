package com.restaurant.reservationsystem.dao;

import com.restaurant.reservationsystem.config.DatabaseConfig;
import com.restaurant.reservationsystem.models.Orders;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class orderDAO {
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    public boolean addOrder(int customerId, String productId, int quantity, Timestamp dateTime) {
        String sql = "INSERT INTO Orders ( product_id, customer_id, date, quantity) VALUES ( ?, ?, ?, ?)";
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {


            prepare.setString(1, productId);
            prepare.setInt(2, customerId);
            prepare.setTimestamp(3, dateTime);
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

    public double getTotalPrice(int customerId, Timestamp dateTime) {
        String sql = "SELECT SUM(o.quantity * p.price) AS total_price FROM Orders o JOIN Product p ON o.product_id = p.product_id WHERE customer_id = ? AND o.date = ?";

        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setInt(1, customerId);
            prepare.setTimestamp(2, dateTime);
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

    public ObservableList<Orders> getOrderListData( Timestamp datetime) {
        ObservableList<Orders> listData = FXCollections.observableArrayList();
        String sql = "SELECT o.order_id, o.product_id, p.product_name, p.type, p.price, o.quantity " +
                "FROM Orders o " +
                "JOIN Product p ON o.product_id = p.product_id " +
                "WHERE  o.date = ?";
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {


            prepare.setTimestamp(1, datetime);
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

    public boolean isCustomerIdExists(int customerId) {
        String query = "SELECT COUNT(*) FROM Customer WHERE customer_id = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
