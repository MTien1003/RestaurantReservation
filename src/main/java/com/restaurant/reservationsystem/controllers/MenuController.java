package com.restaurant.reservationsystem.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

public class MenuController {
    @FXML
    private Button logout;

    @FXML
    private Button orderbutton;

    public void logout() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                logout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("/com/restaurant/reservationsystem/fxml/login.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);


                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openProductsScene() {
        try {
            // Load the products.fxml file
            Parent root = FXMLLoader.load(getClass().getResource("/com/restaurant/reservationsystem/fxml/products.fxml"));

            // Get the current stage
            Stage stage = (Stage) logout.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openOrdersScene() {
        try {
            // Load the orders.fxml file
            Parent root = FXMLLoader.load(getClass().getResource("/com/restaurant/reservationsystem/fxml/order.fxml"));

            // Get the current stage
            Stage stage = (Stage) orderbutton.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}