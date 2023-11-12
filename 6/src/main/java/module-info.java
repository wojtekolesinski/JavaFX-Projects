module com.example.tpo6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.naming;
    requires activemq.all;
    requires moshi;

    exports com.example.tpo6.application;
    opens com.example.tpo6.application to javafx.fxml;
    opens com.example.tpo6.dto to moshi;
}