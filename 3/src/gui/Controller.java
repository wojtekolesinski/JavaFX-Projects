package gui;

import client.SimpleClient;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    private final SimpleClient client = new SimpleClient(36649);

    @FXML public ComboBox<String> languageComboBox = new ComboBox<String>();;
    @FXML public Button loadLanguagesButton;
    @FXML public Label outputLabel;
    @FXML public TextField inputTextArea;
    @FXML public Button translateButton;

    @FXML
    private void initialize() {
    }

    @FXML
    private void loadLanguages() {
        languageComboBox.setItems(FXCollections.observableList(client.getAvailableLanguages()));
        System.out.println(languageComboBox.getItems());
    }

    @FXML
    private void translate() {
        if (inputTextArea.getText().isEmpty()) {
            outputLabel.setText("You have to input a word");
            return;
        }

        if (languageComboBox.getValue() == null) {
            outputLabel.setText("Pick a language");
            return;
        }

        var result = client.translate(languageComboBox.getValue(), inputTextArea.getText());
        outputLabel.setText(result);
    }
}
