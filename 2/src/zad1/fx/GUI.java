package zad1.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import zad1.Service;

import java.io.FileInputStream;

public class GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(c -> new Controller(new Service("Poland"), "Warsaw", "USD"));
        Parent root = loader.load(new FileInputStream("resources/zad1/fx/GUI.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setTitle("TPO2");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
