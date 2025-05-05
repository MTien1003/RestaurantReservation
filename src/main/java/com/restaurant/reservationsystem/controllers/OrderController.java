package com.restaurant.reservationsystem.controllers;

import com.restaurant.reservationsystem.models.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.sql.*;

import static com.restaurant.reservationsystem.config.DatabaseConfig.getConnection;

public class OrderController {

    @FXML
    private TableView<Order> ordersTable;
    @FXML
    private TableColumn<Order, Integer> idColumn;
    @FXML
    private TableColumn<Order, String> nameColumn;
    @FXML
    private TableColumn<Order, String> dateColumn;
    @FXML
    private TableColumn<Order, Double> totalColumn;
    @FXML
    private TableColumn<Order,Double> quantityColumn;
    @FXML
    private TableColumn<Order, String> statusColumn;

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField dateField;
    @FXML
    private TextField quantityField;


    private Connection connection;
    private ObservableList<Order> orderList = FXCollections.observableArrayList();

    public void initialize() throws SQLException {
        connectToDatabase();
        setupTable();
        loadOrders();
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=RestaurantReservation;encrypt=true;trustServerCertificate=true", "sa", "123456789");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("orderName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        ordersTable.setItems(orderList);
    }

    private void loadOrders() {
        orderList.clear();
        String query = "SELECT * FROM orders";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                orderList.add(new Order(
                        resultSet.getInt("id"),
                        resultSet.getString("customer_name"),
                        resultSet.getString("order_date"),
                        resultSet.getDouble("total_amount"),
                        resultSet.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void addOrder() {

    }

    @FXML
    private void updateOrder() {
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("No Selection", "No Order Selected", "Please select an order to update.");
            return;
        }

        String query = "UPDATE orders SET customer_name = ?, order_date = ?, total_amount = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nameField.getText());
            preparedStatement.setString(2, dateField.getText());
            preparedStatement.setDouble(3, Double.parseDouble(quantityField.getText()));
            preparedStatement.setInt(5, selectedOrder.getId());
            preparedStatement.executeUpdate();
            loadOrders();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteOrder() {
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("No Selection", "No Order Selected", "Please select an order to delete.");
            return;
        }

        String query = "DELETE FROM orders WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, selectedOrder.getId());
            preparedStatement.executeUpdate();
            loadOrders();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void payOrder() {
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("No Selection", "No Order Selected", "Please select an order to mark as paid.");
            return;
        }

        String query = "UPDATE orders SET status = 'Paid' WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, selectedOrder.getId());
            preparedStatement.executeUpdate();
            loadOrders();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void viewInvoices() {
        // Logic to view invoices can be implemented here
        System.out.println("Viewing invoices...");
    }

    @FXML
    private void backToMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/restaurant/reservationsystem/fxml/menu.fxml"));
            Stage stage = (Stage) ordersTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        dateField.clear();
        quantityField.clear();

    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}