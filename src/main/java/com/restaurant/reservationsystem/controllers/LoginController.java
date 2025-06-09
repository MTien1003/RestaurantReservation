package com.restaurant.reservationsystem.controllers;

import com.restaurant.reservationsystem.config.DatabaseConfig;
import com.restaurant.reservationsystem.models.Admin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public  class LoginController {

    @FXML
    private Pane loginPane;

    @FXML
    private Button loginbutton;



    @FXML
    private Pane paneCover;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;
// DATABASE TOOLS
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    public void login() throws SQLException {
        String sql = "SELECT * FROM Managers WHERE Username=? AND Password=?";
        connect = DatabaseConfig.getConnection();
        try {

            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());
            result = prepare.executeQuery();

            Alert alert;
            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all fields");
                alert.showAndWait();
            } else {
                if (result.next()) {
                    Admin.username = username.getText(); // Store the username in the data class
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Login Successful");
                    alert.showAndWait();

                    loginbutton.getScene().getWindow().hide(); // Hides the current window
                    // Load the next scene
                    Parent root = FXMLLoader.load(getClass().getResource("/com/restaurant/reservationsystem/fxml/dashboard.fxml"));
                    Stage stage=new Stage();
                    Scene scene=new Scene(root);
                    stage.setScene(scene);
                    stage.show();


                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid username or password");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// CLOSE THE PROGRAM
    public void close(){
            System.exit(0);
    }

}
