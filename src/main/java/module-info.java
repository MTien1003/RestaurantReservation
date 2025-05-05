module com.restaurant.reservationsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires java.desktop;


    opens com.restaurant.reservationsystem to javafx.fxml;
    exports com.restaurant.reservationsystem;
    opens com.restaurant.reservationsystem.controllers to javafx.fxml;
    exports com.restaurant.reservationsystem.controllers to javafx.fxml;

    opens com.restaurant.reservationsystem.models to javafx.base;
}