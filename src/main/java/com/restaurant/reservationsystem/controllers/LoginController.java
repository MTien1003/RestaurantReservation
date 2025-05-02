package com.restaurant.reservationsystem.controllers;

import com.restaurant.reservationsystem.config.DatabaseConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import javax.swing.text.html.ImageView;
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
        String sql = "SELECT * FROM Admin WHERE username=? AND password=?";
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
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Login Successful");
                    alert.showAndWait();
                    // Load the next scene
                    // FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/restaurant/reservationsystem/fxml/nextScene.fxml"));
                    // Parent root = loader.load();
                    // Scene scene = new Scene(root);
                    // Stage stage = (Stage) loginbutton.getScene().getWindow();
                    // stage.setScene(scene);
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
