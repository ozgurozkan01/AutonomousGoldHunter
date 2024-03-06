module com.example.prolab2_1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.prolab2_1 to javafx.fxml;
    exports com.example.prolab2_1;
}