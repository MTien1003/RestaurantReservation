package com.restaurant.reservationsystem.dao;

import com.restaurant.reservationsystem.config.DatabaseConfig;
import com.restaurant.reservationsystem.models.Orders;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class orderDAO {
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    public boolean addOrder(String phone, String productId, int quantity, Timestamp dateTime) {
        String sql = "INSERT INTO Orders ( ProductId, CustomerPhone, Date, Quantity) VALUES ( ?, ?, ?, ?)";
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {
            prepare.setString(1, productId);
            prepare.setString(2, phone);
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
        String sql = "SELECT CategoryName, Price FROM Product WHERE ProductId = ?";
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {
            prepare.setString(1, productId);
            try (ResultSet result = prepare.executeQuery()) {
                if (result.next()) {
                    Map<String, Object> productDetails = new HashMap<>();
                    productDetails.put("type", result.getString("CategoryName"));
                    productDetails.put("price", result.getDouble("Price"));
                    return productDetails;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


        public boolean addPayment(String phone, double total, Date date) {
            String sql = "INSERT INTO Invoice(CustomerPhone,Total, Date) VALUES(?, ?, ?)";
            try (Connection connect = DatabaseConfig.getConnection();
                 PreparedStatement prepare = connect.prepareStatement(sql)) {
                prepare.setString(1,  phone);
                prepare.setDouble(2, total);
                prepare.setDate(3, date);
                prepare.executeUpdate();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

    public double getTotalPrice(String phone, Timestamp dateTime) {
        String sql = "SELECT dbo.fn_TinhTongTienDonHang(?, ?) AS total_price;";

        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {
            prepare.setString(1, phone);
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

    public ObservableList<Orders> getOrderListData( Timestamp datetime, String phone) {
        ObservableList<Orders> listData = FXCollections.observableArrayList();
        String sql = "EXEC GetOrdersByDateAndPhone ?, ?;";
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {
            prepare.setTimestamp(1, datetime);
            prepare.setString(2, phone);
            try (ResultSet result = prepare.executeQuery()) {
                while (result.next()) {
                    Orders order = new Orders(
                            result.getInt("OrderId"),
                            result.getString("ProductId"),
                            result.getString("ProductName"),
                            result.getString("CategoryName"),
                            result.getDouble("Price"),
                            result.getInt("Quantity")
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
        String sql = "DELETE FROM Orders WHERE OrderId = ?";
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
        String sql = "SELECT ProductId FROM Product WHERE Status='Available'";
        ObservableList<String> productIds = FXCollections.observableArrayList();

        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql);
             ResultSet result = prepare.executeQuery()) {
            while (result.next()) {
                productIds.add(result.getString("ProductId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productIds;
    }


    public ObservableList<String> getProductNames(String productId) {
        String sql = "SELECT ProductName FROM Product WHERE ProductId = ?";
        ObservableList<String> productNames = FXCollections.observableArrayList();
        try (Connection connect = DatabaseConfig.getConnection();
             PreparedStatement prepare = connect.prepareStatement(sql)) {
            prepare.setString(1, productId);
            try (ResultSet result = prepare.executeQuery()) {
                while (result.next()) {
                    productNames.add(result.getString("ProductName"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productNames;
    }

    public boolean isCustomerIdExists(String phone) {
        String query = "SELECT COUNT(*) FROM Customer WHERE CustomerPhone = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, phone);
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
