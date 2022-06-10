module com.example.tpo6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.naming;
    requires activemq.all;

    opens com.example.tpo6 to javafx.fxml;
    exports com.example.tpo6;
}