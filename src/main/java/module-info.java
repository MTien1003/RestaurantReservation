module com.restaurant.reservationsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;


    opens com.restaurant.reservationsystem to javafx.fxml;
    exports com.restaurant.reservationsystem;
}