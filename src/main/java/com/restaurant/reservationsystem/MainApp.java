package com.restaurant.reservationsystem;

import com.restaurant.reservationsystem.dao.CustomerDAO;
import com.restaurant.reservationsystem.models.Customer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage)  {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/com/restaurant/reservationsystem/fxml/hello-view.fxml"));
            Scene scene=new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}