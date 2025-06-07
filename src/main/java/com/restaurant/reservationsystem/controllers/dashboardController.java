package com.restaurant.reservationsystem.controllers;

import com.restaurant.reservationsystem.config.DatabaseConfig;
import com.restaurant.reservationsystem.dao.availableDAO;
import com.restaurant.reservationsystem.dao.dashboardDAO;
import com.restaurant.reservationsystem.dao.orderDAO;
import com.restaurant.reservationsystem.models.Admin;
import com.restaurant.reservationsystem.models.Orders;
import com.restaurant.reservationsystem.models.Product;
import com.restaurant.reservationsystem.utils.AlertUtils;
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
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class dashboardController implements Initializable  {

    @FXML
    private Button availableFD_addBtn;

    @FXML
    private Button availableFD_btn;

    @FXML
    private Button availableFD_clearBtn;

    @FXML
    private TableColumn<Product, String> availableFD_col_Price;

    @FXML
    private TableColumn<Product, String> availableFD_col_Status;

    @FXML
    private TableColumn<Product, String> availableFD_col_Type;

    @FXML
    private TableColumn<Product, String> availableFD_col_productID;

    @FXML
    private TableColumn<Product, String> availableFD_col_productName;

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
    private ComboBox<String> availableFD_productStatus;

    @FXML
    private ComboBox<String> availableFD_productType;

    @FXML
    private TextField availableFD_search;

    @FXML
    private Button availableFD_updateBtn;

    @FXML
    private Button close;

    @FXML
    private BarChart<String, Double> dashboard_ICChart;

    @FXML
    private Label dashboard_NC;

    @FXML
    private BarChart<String, Integer> dashboard_NOCChart;

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
    private TableColumn<Orders, String> order_col_price;

    @FXML
    private TableColumn<Orders, String> order_col_productID;

    @FXML
    private TableColumn<Orders, String> order_col_productName;

    @FXML
    private TableColumn<Orders, String> order_col_quantity;

    @FXML
    private TableColumn<Orders, String> order_col_type;

    @FXML
    private AnchorPane order_form;

    @FXML
    private Button order_payBtn;

    @FXML
    private ComboBox<String> order_productID;

    @FXML
    private ComboBox<String> order_productName;

    @FXML
    private Spinner<Integer> order_quantity;

    @FXML
    private Spinner<Integer> quantity;

    @FXML
    private Button order_receiptBtn;

    @FXML
    private Button order_removeBtn;

    @FXML
    private TableView<Orders> order_tableView;

    @FXML
    private Label order_total;

    @FXML
    private Label username;

    @FXML
    private AnchorPane main_form;

    @FXML
    private TableView<Product> availableFD_tableView;

    @FXML
    private Label order_balance;

    @FXML
    private TextField cus_id_field;


    public void close(){
        System.exit(0);
    }

    public void displayUsername(){
        String user = Admin.username;
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


    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private final dashboardDAO dashboardDAO = new dashboardDAO();

    public void dashboardNC() {
        int totalCount = dashboardDAO.getTotalCustomerOrder();
        dashboard_NC.setText(String.valueOf(totalCount));
    }
    public void dashboardTi() {
        double totalIncome = dashboardDAO.getTotalIncomeForToday();
        dashboard_Ti.setText("$" + totalIncome);
    }

    public void dashboardTincome() {
        double totalIncome = dashboardDAO.getTotalIncome();
        dashboard_Tincome.setText("$" + totalIncome);
    }

    public void dashboardNOOChart() {
        dashboard_NOCChart.getData().clear();
        Map<String, Integer> ordersByDate = dashboardDAO.getNumberOfOrdersByDate();
        XYChart.Series<String, Integer> chart = new XYChart.Series<>();

        ordersByDate.forEach((date, count) -> chart.getData().add(new XYChart.Data<>(date, count)));
        dashboard_NOCChart.getData().add(chart);
    }

    public void dashboardICC() {
        dashboard_ICChart.getData().clear();
        Map<String, Double> incomeChartData = dashboardDAO.getIncomeChartData();
        XYChart.Series<String, Double> chart = new XYChart.Series<>();

        incomeChartData.forEach((date, total) -> chart.getData().add(new XYChart.Data<>(date, total)));
        dashboard_ICChart.getData().add(chart);
    }

//--------------------------------------------------------------------------------------------------------------------//
    private String[] status={"Available","Not Available"};
    public void availableFDStatus(){
        List<String> listStatus= new ArrayList<>();
        for (String s:status){
            listStatus.add(s);
        }
        ObservableList listData= FXCollections.observableArrayList(listStatus);
        availableFD_productStatus.setItems(listData);
    }

    private String[] categories={"Food","Drink"};
    public void availableFDType(){
        List<String> listType= new ArrayList<>();
        for (String s:categories){
            listType.add(s);
        }
        ObservableList listData= FXCollections.observableArrayList(listType);
        availableFD_productType.setItems(listData);
    }


    private  final availableDAO availableDAO=new availableDAO();

    public void availableFDAdd() {
        String productId = availableFD_productID.getText();
        String productName = availableFD_productName.getText();
        String productType = (String) availableFD_productType.getSelectionModel().getSelectedItem();
        String productPrice = availableFD_productPrice.getText();
        String productStatus = (String) availableFD_productStatus.getSelectionModel().getSelectedItem();
        if (productId.isEmpty() || productName.isEmpty() || productPrice.isEmpty() || productType == null || productStatus == null) {
            AlertUtils.showErrorAlert("Error", "Please fill all fields");
        } else {
            if (availableDAO.isProductIdExists(productId)) {
                AlertUtils.showErrorAlert("Error", "Product ID already exists");
            } else {
                boolean success = availableDAO.addProduct(productId, productName, productType, productPrice, productStatus, avail_quantity);
                if (success) {
                    AlertUtils.showInfoAlert("Success", "Product added successfully");
                    availableFDShowData();
                    availableFDClear();
                }
            }
        }
    }


    public void availableFDUpdate() {
        String productId = availableFD_productID.getText();
        String productName = availableFD_productName.getText();
        String productType = (String)availableFD_productType.getSelectionModel().getSelectedItem();
        String productPrice = availableFD_productPrice.getText();
        String productStatus = (String)availableFD_productStatus.getSelectionModel().getSelectedItem();
        int quantityValue = (int) quantity.getValue();

        Alert alert;
        if (productId.isEmpty() || productName.isEmpty() || productPrice.isEmpty() || productType == null || productStatus == null) {
            AlertUtils.showErrorAlert("Error", "Please fill all fields");
        } else {
            boolean success = availableDAO.updateProduct(productId, productName, productType, productPrice, productStatus, quantityValue);
            if (success) {
                AlertUtils.showInfoAlert("Success", "Product updated successfully");
                availableFDShowData();
                availableFDClear();
            } else {
                AlertUtils.showErrorAlert("Error", "Failed to update product");
            }
        }
    }

    public void availableFDDelete() {
        String productId = availableFD_productID.getText();

        if (productId.isEmpty()) {
            AlertUtils.showErrorAlert("Error", "Please select a product to delete");
        } else {
            boolean success = availableDAO.deleteProduct(productId);
            if (success) {
                AlertUtils.showInfoAlert("Success", "Product deleted successfully");
                availableFDShowData();
                availableFDClear();
            } else {
                AlertUtils.showErrorAlert("Error", "Failed to delete product");
            }
        }
    }

    public void availableFDClear(){
        availableFD_productID.setText("");
        availableFD_productName.setText("");
        availableFD_productPrice.setText("");
        availableFD_productStatus.getSelectionModel().clearSelection();
        availableFD_productType.getSelectionModel().clearSelection();
        quantity.getValueFactory().setValue(0);
    }

    private ObservableList<Product> availableFDListData;
    public void availableFDShowData(){
        availableFDListData=availableFDList();
        availableFD_col_productID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        availableFD_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        availableFD_col_Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        availableFD_col_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        availableFD_col_Status.setCellValueFactory(new PropertyValueFactory<>("status"));
        availableFD_tableView.setItems(availableFDListData);
    }

    public ObservableList<Product> availableFDList() {
        return availableDAO.getAllProduct();
    }

    public void availableFDSelect(){
        Product catDate=availableFD_tableView.getSelectionModel().getSelectedItem();
        int num=availableFD_tableView.getSelectionModel().getSelectedIndex();
        if((num-1)<-1){
            return;
        }
        availableFD_productID.setText(catDate.getProductId());
        availableFD_productName.setText(catDate.getProductName());
        availableFD_productPrice.setText(String.valueOf(catDate.getPrice()));
        availableFD_productType.getSelectionModel().select(catDate.getType());
        availableFD_productStatus.getSelectionModel().select(catDate.getStatus());
        quantity.getValueFactory().setValue(catDate.getQuantity());

    }

    public void availableFDSearch() {
        FilteredList<Product> filter = new FilteredList<>(availableFDListData, e -> true);
        availableFD_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate(predicateProduct -> {
                if (newValue==null || newValue.isEmpty()) {
                    return true;
                }
                String searchKey = newValue.toLowerCase();
                if (predicateProduct.getProductId().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateProduct.getProductName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateProduct.getType().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (String.valueOf(predicateProduct.getPrice()).toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateProduct.getStatus().toLowerCase().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Product> sortList = new SortedList<>(filter);
        sortList.comparatorProperty().bind(availableFD_tableView.comparatorProperty());
        availableFD_tableView.setItems(sortList);
    }
//--------------------------------------------------------------------------------------------------------------------//
    private final orderDAO orderDAO = new orderDAO();// Ensure this is declared

    private  String customerPhone;
    private Timestamp currentDateTime;
    public void getCurrentDateTime() {
        currentDateTime = new Timestamp(System.currentTimeMillis());

    }

    public void orderAdd() {
        String productId = order_productID.getSelectionModel().getSelectedItem();
        String productName = order_productName.getSelectionModel().getSelectedItem();
        if (productId == null || productName == null) {
            AlertUtils.showErrorAlert("Error", "Please select a product and quantity.");
            return;
        }
        customerPhone = cus_id_field.getText();
        if (!orderDAO.isCustomerIdExists(customerPhone)) {
            String sql = "INSERT INTO Customer(CustomerPhone) VALUES (?)";
            try (Connection connect = DatabaseConfig.getConnection();
                 PreparedStatement prepare = connect.prepareStatement(sql)) {
                 prepare.setString(1, customerPhone);
                 prepare.executeUpdate();
                 } catch (Exception e) {
                    e.printStackTrace();
                    AlertUtils.showErrorAlert("Error", "Invalid phone number! Must be exactly 10 digits and start with 0");
                    return;
                }
            }

       if (customerPhone.isEmpty()) {
            AlertUtils.showErrorAlert("Error", "Please enter a customer's phone.");
            return;
       }
        // Use the instance of orderDAO to call the method
        Map<String, Object> productDetails = orderDAO.getProductDetails(productId);
        if (productDetails == null) {
            AlertUtils.showErrorAlert("Error", "Product details not found.");
            return;
        }
        double orderPrice = (double) productDetails.get("price");
        double totalPrice = orderPrice * qty;
        boolean success = orderDAO.addOrder(customerPhone, productId,qty, currentDateTime);
        if (success) {
            orderDisplayTotal();
            orderDisplayData();
            order_quantity.getValueFactory().setValue(0);
            order_productID.getSelectionModel().clearSelection();
            order_productName.getSelectionModel().clearSelection();
            AlertUtils.showInfoAlert("Success", "Order added successfully.");
        } else {
            AlertUtils.showErrorAlert("Error", "One or more order items exceed available stock!");
        }
    }

    public ObservableList<Orders> orderListData(){
        return orderDAO.getOrderListData(currentDateTime, customerPhone);

    }
    private ObservableList<Orders> orderData;
    public void orderDisplayData(){
        orderData=orderListData();
        order_col_productID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        order_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        order_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        order_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        order_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        order_tableView.setItems(orderData);

    }


    public void orderPay(){

        orderTotal();

        if (balance < 0 || totalP <= 0) {
            AlertUtils.showErrorAlert("Error", "Invalid input.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to pay?");
        Optional<ButtonType> option = confirmationAlert.showAndWait();

        if (option.isPresent() && option.get().equals(ButtonType.OK)) {
            boolean success = orderDAO.addPayment(customerPhone, totalP, new Date(System.currentTimeMillis()));
            if (success) {
                AlertUtils.showInfoAlert("Success", "Payment successful!");
                order_amount.setText("");
                order_balance.setText("0.0");
                order_total.setText("0.0");
                amount=0;
                balance=0;
                totalP=0;
                currentDateTime=null;
                order_tableView.getItems().clear();
                cus_id_field.setText("");
                order_productID.getSelectionModel().clearSelection();
                order_productName.getSelectionModel().clearSelection();
                order_quantity.getValueFactory().setValue(0);


            } else {
                AlertUtils.showErrorAlert("Error", "Payment failed.");
            }
        } else {
            AlertUtils.showInfoAlert("Information", "Payment cancelled.");
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
                order_balance.setText(String.format("%.2f",balance));
            }

        }
    }
    private double totalP=0;
    public void orderTotal(){
        totalP= orderDAO.getTotalPrice(customerPhone, currentDateTime);
    }

    public void orderDisplayTotal(){
        orderTotal();
        order_total.setText(String.valueOf(totalP));
    }

    private int item;
    public void orderSelectData() {
        Orders prod = order_tableView.getSelectionModel().getSelectedItem();
        int num = order_tableView.getSelectionModel().getSelectedIndex();

        if(prod==null|| num<0){
            return;
        }
        item = prod.getOrderId();
    }

    public void orderRemove() {
        if (item == 0) {
            AlertUtils.showErrorAlert("Error Message", "Please select the item first");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation Message");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to remove this item ?");
        Optional<ButtonType> option = confirmationAlert.showAndWait();

        if (option.isPresent() && option.get().equals(ButtonType.OK)) {
            boolean success = orderDAO.removeOrder(item);
            if (success) {
                AlertUtils.showInfoAlert("Information Message", "Successfully Removed!");
                orderDisplayData();
                orderDisplayTotal();
                order_amount.setText("");
                order_balance.setText("$0.0");
            } else {
                AlertUtils.showErrorAlert("Error Message", "Failed to remove the item.");
            }
        } else {
            AlertUtils.showInfoAlert("Information Message", "Cancelled!");
        }
    }

    public void orderProductId() {
        ObservableList<String> productIds = orderDAO.getAvailableProductIds();
        order_productID.setItems(productIds);
        orderProductName();
    }

    public void orderProductName() {
        String selectedProductId = order_productID.getSelectionModel().getSelectedItem();
        if (selectedProductId != null) {
            ObservableList<String> productNames = orderDAO.getProductNames(selectedProductId);
            order_productName.setItems(productNames);
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
    }

    private int avail_quantity;
    public void availableQuantity(){
        avail_quantity =(int) quantity.getValue();
    }
    private SpinnerValueFactory<Integer> SpinnerQuantity;
    public void availableSpinner(){
        SpinnerQuantity = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, 0);
        quantity.setValueFactory(SpinnerQuantity);
    }


    public void switchForm(ActionEvent event){
        if (event.getSource() == availableFD_btn) {
            availableFD_form.setVisible(true);
            order_form.setVisible(false);
            dashboard_form.setVisible(false);

            availableFDShowData();
            availableFDSearch();
            availableSpinner();


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
            dashboardTi();
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
        availableFDSearch();
        availableSpinner();

        orderProductId();
        orderProductName();
        orderSpinner();
        orderDisplayData();
        orderDisplayTotal();
    }

}
