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
import javafx.scene.chart.XYChart;
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
import java.util.Optional;
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
    private TableColumn<Product, String> order_col_price;

    @FXML
    private TableColumn<Product, String> order_col_productID;

    @FXML
    private TableColumn<Product, String> order_col_productName;

    @FXML
    private TableColumn<Product, String> order_col_quantity;

    @FXML
    private TableColumn<Product, String> order_col_type;

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
    private TableView<Product> order_tableView;

    @FXML
    private Label order_total;

    @FXML
    private Label username;

    @FXML
    private AnchorPane main_form;

    @FXML
    private TableView<categories> availableFD_tableView;

    @FXML
    private Label order_balance;

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



    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    public void dashboardNC(){
        String sql="SELECT COUNT(customer_id) AS total_count FROM product_info";
        int nc=0;
        connect=DatabaseConfig.getConnection();
        try{
            statement=connect.createStatement();
            result=statement.executeQuery(sql);

            if(result.next()){
                nc=result.getInt("total_count");
            }

            dashboard_NC.setText(String.valueOf(nc));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void dashboardTi(){
        java.util.Date date = new java.util.Date();
        java.sql.Date sqlDate=new java.sql.Date(date.getTime());

        String sql="SELECT SUM(total) AS total_price FROM product_info WHERE date='"+sqlDate+"'";
        connect =DatabaseConfig.getConnection();
        double ti=0;

        try {
            statement = connect.createStatement();
            result = statement.executeQuery(sql);
            if (result.next()) {
                ti = result.getDouble("total_price");
            }
            dashboard_Tincome.setText("$" + String.valueOf(ti));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardTincome(){
        String sql="SELECT SUM(total) AS total_price FROM product_info";
        connect=DatabaseConfig.getConnection();
        double ti=0;
        try {
            statement = connect.createStatement();
            result = statement.executeQuery(sql);
            if (result.next()) {
                ti = result.getDouble("total_price");
            }
            dashboard_Tincome.setText("$" + String.valueOf(ti));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardNOOChart(){
        try{
            dashboard_NOCChart.getData().clear();
            String sql="SELECT date, COUNT(customer_id) AS total_count FROM product_info GROUP BY date ORDER BY date ASC ";
            connect=DatabaseConfig.getConnection();
            XYChart.Series chart=new XYChart.Series();

            prepare=connect.prepareStatement(sql);
            result=prepare.executeQuery();

            while (result.next()){
                chart.getData().add(new XYChart.Data(result.getString(1), result.getInt(2)));

            }
            dashboard_NOCChart.getData().add(chart);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void dashboardICC(){
        dashboard_ICChart.getData().clear();

        String sql = "SELECT date, SUM(total) AS total_price FROM product_info GROUP BY date ORDER BY date ASC ";

        connect= DatabaseConfig.getConnection();

        try {

            XYChart.Series chart = new XYChart.Series();

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {

                chart.getData().add(new XYChart.Data(result.getString(1), result.getDouble(2)));

            }

            dashboard_ICChart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public void availableFDClear(){
        availableFD_productID.setText("");
        availableFD_productName.setText("");
        availableFD_productPrice.setText("");
        availableFD_productStatus.getSelectionModel().clearSelection();
        availableFD_productType.getSelectionModel().clearSelection();


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

    public void orderAdd(){
        orderCustomerId();
        orderTotal();
        String sql="INSERT INTO product"
                + "(customer_id,product_id, product_name,type,price,quantity,date)"
                + "VALUES (?,?,?,?,?,?,?)";
        connect=DatabaseConfig.getConnection();
        try{
            String orderType="";
            double orderPrice=0;
            String checkData="SELECT * FROM category WHERE product_id= '"+order_productID.getSelectionModel().getSelectedItem()+"'";
            statement=connect.createStatement();
            result=statement.executeQuery(checkData);
            if(result.next()){
                orderType=result.getString("type");
                orderPrice=result.getDouble("price");
            }

            prepare=connect.prepareStatement(sql);
            prepare.setString(1,String.valueOf(customerId));
            prepare.setString(2,(String) order_productID.getSelectionModel().getSelectedItem());
            prepare.setString(3,(String) order_productName.getSelectionModel().getSelectedItem());
            prepare.setString(4,orderType);

            double totalPrice=orderPrice*qty;
            prepare.setString(5,String.valueOf(totalPrice));
            prepare.setString(6,String.valueOf(qty));

            java.util.Date date = new java.util.Date();
            java.sql.Date sqlDate=new java.sql.Date(date.getTime());
            prepare.setString(7,String.valueOf(sqlDate));
            prepare.executeUpdate();

            orderDisplayTotal();
            orderDisplayData();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void orderPay(){
        orderCustomerId();
        orderTotal();

        String sql="INSERT INTO product_info(customer_id,total,date) VALUES(?,?,?)";
        connect=DatabaseConfig.getConnection();
        try {
            Alert alert;
            if (balance == 0 || String.valueOf(balance) == "$0.0" || String.valueOf(balance) == null
                    || totalP == 0 || String.valueOf(totalP) == "$0.0" || String.valueOf(totalP) == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid :3");
                alert.showAndWait();
            }
            else{
                alert =new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to pay?");
                Optional<ButtonType> option=alert.showAndWait();
                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, String.valueOf(customerId));
                    prepare.setString(2, String.valueOf(totalP));

                    java.util.Date date = new java.util.Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare.setString(3, String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successful!");
                    alert.showAndWait();

                    order_total.setText("$0.0");
                    order_balance.setText("$0.0");
                    order_amount.setText("");

                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled!");
                    alert.showAndWait();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private double totalP=0;
    public void orderTotal(){
        orderCustomerId();
        String sql="SELECT SUM(price) AS total_price FROM product WHERE customer_id= "+customerId;
        connect=DatabaseConfig.getConnection();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                totalP = result.getDouble("total_price");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private double amount;
    private double balance;
    public void orderAmount(){
        orderTotal();
        Alert alert;
        if(order_amount.getText().isEmpty()|| order_amount.getText()==null|| order_amount.getText()==""){
            alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter amount");
            alert.showAndWait();
        }
        else{
            amount=Double.parseDouble(order_amount.getText());
            if(amount<totalP){
                order_amount.setText("");
            }
            else{
                balance=(amount-totalP);
                order_balance.setText("$"+String.valueOf(balance));


            }

        }
    }

    public void orderDisplayTotal(){
        orderTotal();
        order_total.setText("$"+String.valueOf(totalP));
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
                prod =new Product(
                        result.getString("product_id"),
                        result.getString("product_name"),
                        result.getString("type"),
                        result.getDouble("price"),
                        result.getInt("quantity"));
                System.out.println(result.getString("type"));
                listData.add(prod);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return listData;

    }

    public void orderRemove() {

        String sql = "DELETE FROM product WHERE id = ?";

        connect = DatabaseConfig.getConnection();

        try {
            Alert alert;

            if (item == 0 ) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select the item first");
                alert.showAndWait();
                return;
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to Remove Item: " + item + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Removed!");
                    alert.showAndWait();

                    orderDisplayData();
                    orderDisplayTotal();

                    order_amount.setText("");
                    order_balance.setText("$0.0");

                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled!");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int item;

    public void orderSelectData() {

        Product prod = order_tableView.getSelectionModel().getSelectedItem();
        int num = order_tableView.getSelectionModel().getSelectedIndex();

        if(prod==null|| num<0){
            return;

        }

        item = prod.getId();
    }

    private ObservableList<Product> orderData;
    public void orderDisplayData(){
        orderData=orderListData();
        order_col_productID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        order_col_productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        order_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        order_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        order_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        order_tableView.setItems(orderData);


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
            orderDisplayData();
            orderDisplayTotal();

        } else if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            availableFD_form.setVisible(false);
            order_form.setVisible(false);

            dashboardNC();
            dashboardTincome();
            dashboardICC();
            dashboardNOOChart();
        }
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dashboardNC();
        dashboardTincome();
        dashboardICC();
        dashboardNOOChart();
        dashboardTi();




        displayUsername();
        availableFDStatus();
        availableFDType();

        availableFDShowData();

        orderProductId();
        orderProductName();
        orderSpinner();
        orderDisplayData();
        orderDisplayTotal();
    }



}
