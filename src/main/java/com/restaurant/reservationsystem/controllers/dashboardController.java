package com.restaurant.reservationsystem.controllers;

import com.restaurant.reservationsystem.config.DatabaseConfig;
import com.restaurant.reservationsystem.models.Product;
import com.restaurant.reservationsystem.models.categories;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.sql.*;

public class dashboardController implements Initializable  {

    @FXML
    private Button availableFD_addBtn;

    @FXML
    private Button availableFD_btn;

    @FXML
    private Button availableFD_clearBtn;

    @FXML
    private TableColumn<categories, String> availableFD_col_Price;

    @FXML
    private TableColumn<categories, String> availableFD_col_Status;

    @FXML
    private TableColumn<categories, String> availableFD_col_Type;

    @FXML
    private TableColumn<categories, String> availableFD_col_productID;

    @FXML
    private TableColumn<categories, String> availableFD_col_productName;

    @FXML
    private Button availableFD_deleteBtn;

    @FXML
    private AnchorPane availableFD_form;

    @FXML
    private TextField availableFD_productID;

    @FXML
    private TextField availableFD_productName;

    @FXML
    private TextField availableFD_productPrice;

    @FXML
    private ComboBox<?> availableFD_productStatus;

    @FXML
    private ComboBox<?> availableFD_productType;

    @FXML
    private TextField availableFD_search;

    @FXML
    private Button availableFD_updateBtn;

    @FXML
    private Button close;

    @FXML
    private BarChart<?, ?> dashboard_ICChart;

    @FXML
    private Label dashboard_NC;

    @FXML
    private BarChart<?, ?> dashboard_NOCChart;

    @FXML
    private Label dashboard_Ti;

    @FXML
    private Label dashboard_Tincome;

    @FXML
    private Button dashboard_btn;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Button logout;

    @FXML
    private Button order_addBtn;

    @FXML
    private TextField order_amount;

    @FXML
    private Button order_btn;

    @FXML
    private TableColumn<?, ?> order_col_price;

    @FXML
    private TableColumn<?, ?> order_col_productID;

    @FXML
    private TableColumn<?, ?> order_col_productName;

    @FXML
    private TableColumn<?, ?> order_col_quantity;

    @FXML
    private TableColumn<?, ?> order_col_type;

    @FXML
    private AnchorPane order_form;

    @FXML
    private Button order_payBtn;

    @FXML
    private ComboBox<?> order_productID;

    @FXML
    private ComboBox<?> order_productName;

    @FXML
    private Spinner<Integer> order_quantity;

    @FXML
    private Button order_receiptBtn;

    @FXML
    private Button order_removeBtn;

    @FXML
    private TableView<?> order_tableView;

    @FXML
    private Label order_total;

    @FXML
    private Label username;

    @FXML
    private AnchorPane main_form;

    @FXML
    private TableView<categories> availableFD_tableView;

    public void close(){
        System.exit(0);
    }

    public void displayUsername(){
        String user = data.username;
        user=user.substring(0,1).toUpperCase()+user.substring(1);
        username.setText(user);
    }
    public void logout(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);
        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                // Close the current window
                logout.getScene().getWindow().hide();
                // Load the login scene
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/com/restaurant/reservationsystem/fxml/login.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //available food/drinks
    private String[] status={"Available","Not Available"};
    public void availableFDStatus(){
        List<String> listStatus= new ArrayList<>();
        for (String s:status){
            listStatus.add(s);
        }
        ObservableList listData= FXCollections.observableArrayList(listStatus);
        availableFD_productStatus.setItems(listData);
    }

    private String[] categories={"Meals","Drinks"};
    public void availableFDType(){
        List<String> listType= new ArrayList<>();
        for (String s:categories){
            listType.add(s);
        }
        ObservableList listData= FXCollections.observableArrayList(listType);
        availableFD_productType.setItems(listData);
    }

    public void switchForm(ActionEvent event){
        if (event.getSource() == availableFD_btn) {
            availableFD_form.setVisible(true);
            order_form.setVisible(false);
            dashboard_form.setVisible(false);

            availableFDShowData();
            availableFDSearch();


        } else if (event.getSource() == order_btn) {
            order_form.setVisible(true);
            availableFD_form.setVisible(false);
            dashboard_form.setVisible(false);

            orderProductId();
            orderProductName();
            orderSpinner();
            orderListData();

        } else if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            availableFD_form.setVisible(false);
            order_form.setVisible(false);
        }
    }

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    public void availableFDAdd(){
        String sql="INSERT INTO category (product_id,product_name,product_type,price,status) VALUES (?,?,?,?,?)";
        connect= DatabaseConfig.getConnection();
        try{
            prepare=connect.prepareStatement(sql);
            prepare.setString(1,availableFD_productID.getText());
            prepare.setString(2,availableFD_productName.getText());
            prepare.setString(3,(String) availableFD_productType.getSelectionModel().getSelectedItem());
            prepare.setString(4,availableFD_productPrice.getText());
            prepare.setString(5,(String) availableFD_productStatus.getSelectionModel().getSelectedItem());

            Alert alert;
            if(availableFD_productID.getText().isEmpty()||
                    availableFD_productName.getText().isEmpty()||
                    availableFD_productPrice.getText().isEmpty()||
                    availableFD_productType.getSelectionModel().getSelectedItem()==null||
                    availableFD_productStatus.getSelectionModel().getSelectedItem()==null){
                alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all fields");
                alert.showAndWait();
            }
            else{
                String checkData="SELECT product_id FROM category WHERE product_id= '"+availableFD_productID.getText()+"'";
                connect=DatabaseConfig.getConnection();
                statement=connect.createStatement();
                result=statement.executeQuery(checkData);
                if(result.next()){
                    alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Product ID already exists");
                    alert.showAndWait();
                }
                else{
                    prepare.executeUpdate();
                    alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Product added successfully");
                    alert.showAndWait();

                    //To show the data
                    availableFDShowData();
                    //To clear the fields
                    availableFDClear();
                }
            }
        }catch (Exception e){
            e.printStackTrace();}
    }

    public ObservableList<categories> availableFDList(){
        ObservableList<categories> availableFDList= FXCollections.observableArrayList();
        String sql="SELECT * FROM category";
        connect=DatabaseConfig.getConnection();
        try{
            statement=connect.createStatement();
            result=statement.executeQuery(sql);
            while(result.next()){
                availableFDList.add(new categories(result.getString("product_id"),
                        result.getString("product_name"),
                        result.getString("product_type"),
                        result.getString("price"),
                        result.getString("status")));
            }
        }catch (Exception e){
            e.printStackTrace();}
        return availableFDList;
    }
    private ObservableList<categories> availableFDListData;
    public void availableFDShowData(){
        availableFDListData=availableFDList();
        availableFD_col_productID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        availableFD_col_productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        availableFD_col_Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        availableFD_col_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        availableFD_col_Status.setCellValueFactory(new PropertyValueFactory<>("status"));

        availableFD_tableView.setItems(availableFDListData);
    }

    public void availableFDClear(){
        availableFD_productID.setText("");
        availableFD_productName.setText("");
        availableFD_productPrice.setText("");
        availableFD_productStatus.getSelectionModel().clearSelection();
        availableFD_productType.getSelectionModel().clearSelection();


    }
// an san pham ben table view thi hien ben textfield
    public void availableFDSelect(){
        categories catDate=availableFD_tableView.getSelectionModel().getSelectedItem();
        int num=availableFD_tableView.getSelectionModel().getSelectedIndex();
        if((num-1)<-1){
            return;
        }
        availableFD_productID.setText(catDate.getProductId());
        availableFD_productName.setText(catDate.getName());
        availableFD_productPrice.setText(String.valueOf(catDate.getPrice()));

    }

    public void availableFDUpdate(){
        String sql="UPDATE category SET product_name=?,product_type=?,price=?,status=? WHERE product_id=?";
        connect=DatabaseConfig.getConnection();
        try{
            prepare=connect.prepareStatement(sql);
            prepare.setString(1,availableFD_productName.getText());
            prepare.setString(2,(String) availableFD_productType.getSelectionModel().getSelectedItem());
            prepare.setString(3,availableFD_productPrice.getText());
            prepare.setString(4,(String) availableFD_productStatus.getSelectionModel().getSelectedItem());
            prepare.setString(5,availableFD_productID.getText());

            Alert alert;
            if(availableFD_productID.getText().isEmpty()||
                    availableFD_productName.getText().isEmpty()||
                    availableFD_productPrice.getText().isEmpty()||
                    availableFD_productType.getSelectionModel().getSelectedItem()==null||
                    availableFD_productStatus.getSelectionModel().getSelectedItem()==null){
                alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all fields");
                alert.showAndWait();
            }
            else{
                prepare.executeUpdate();
                alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Product updated successfully");
                alert.showAndWait();

                //To show the data
                availableFDShowData();
                //To clear the fields
                availableFDClear();
            }
        }catch (Exception e){
            e.printStackTrace();}
    }

    public void availableFDDelete(){
        String sql="DELETE FROM category WHERE product_id=?";
        connect=DatabaseConfig.getConnection();
        try{
            prepare=connect.prepareStatement(sql);
            prepare.setString(1,availableFD_productID.getText());

            Alert alert;
            if(availableFD_productID.getText().isEmpty()){
                alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please select a product to delete");
                alert.showAndWait();
            }
            else{
                prepare.executeUpdate();
                alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Product deleted successfully");
                alert.showAndWait();

                //To show the data
                availableFDShowData();
                //To clear the fields
                availableFDClear();
            }
        }catch (Exception e){
            e.printStackTrace();}

    }

    public void availableFDSearch() {

        FilteredList<categories> filter = new FilteredList<>(availableFDListData, e -> true);

        availableFD_search.textProperty().addListener((observabl, oldValue, newValue) -> {

            filter.setPredicate(predicateCategories -> {

                if (newValue==null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (predicateCategories.getProductId().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateCategories.getName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateCategories.getType().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateCategories.getPrice().toString().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateCategories.getStatus().toLowerCase().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<categories> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(availableFD_tableView.comparatorProperty());
        availableFD_tableView.setItems(sortList);

    }

    public void orderProductId() {
        String sql = "SELECT product_id FROM category WHERE status='Available'";
        connect = DatabaseConfig.getConnection();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            ObservableList listData = FXCollections.observableArrayList();
            while (result.next()) {
                listData.add(result.getString("product_id"));
            }
            order_productID.setItems(listData);
            orderProductName();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void orderProductName() {
        String sql="SELECT product_name FROM category WHERE product_id= '"+order_productID.getSelectionModel().getSelectedItem()+"'";
        connect=DatabaseConfig.getConnection();
        try{
            prepare=connect.prepareStatement(sql);
            result=prepare.executeQuery();
            ObservableList listData= FXCollections.observableArrayList();
            while(result.next()){
                listData.add(result.getString("product_name"));
            }
            order_productName.setItems(listData);
    }catch (Exception e){
            e.printStackTrace();
        }
    }
    private SpinnerValueFactory<Integer> Spinner;
    public void orderSpinner(){
        Spinner=new SpinnerValueFactory.IntegerSpinnerValueFactory(0,50,0);
        order_quantity.setValueFactory(Spinner);
    }
    private int qty;
    public void orderQuantity(){
        qty= (int) order_quantity.getValue();
        System.out.println(qty);
    }



    public ObservableList<Product> orderListData(){

        orderCustomerId();
        ObservableList<Product> listData= FXCollections.observableArrayList();
        String sql="SELECT * FROM product WHERE customer_id= "+customerId;
        connect=DatabaseConfig.getConnection();
        try{
            prepare=connect.prepareStatement(sql);
            result=prepare.executeQuery();
            Product prod;
            while(result.next()){
                prod =new Product(result.getString("product_id"),
                        result.getString("product_name"),
                        result.getString("type"),
                        result.getDouble("price"),
                        result.getInt("quantity"));
                listData.add(prod);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    return listData;

    }
    private  int customerId;
    public void orderCustomerId(){
        String sql="SELECT customer_id FROM product";
        connect=DatabaseConfig.getConnection();
        try{
            prepare=connect.prepareStatement(sql);
            result=prepare.executeQuery();
            while(result.next()){
                customerId=result.getInt("customer_id");
            }
            String checkData="SELECT customer_id FROM product_info" ;

            statement=connect.createStatement();
            result=statement.executeQuery(checkData);
            int customerInfoId=0;
            while(result.next()){
                customerInfoId=result.getInt("customer_id");
            }

            if(customerId==0){
                customerId+=1;
            }
            else if(customerId== customerInfoId){
                customerId+=1;
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUsername();
        availableFDStatus();
        availableFDType();
        availableFDShowData();

        orderProductId();
        orderProductName();
        orderSpinner();
        orderListData();
    }



}
