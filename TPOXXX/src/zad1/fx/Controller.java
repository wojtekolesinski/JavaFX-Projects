package zad1.fx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import zad1.Service;

public class Controller {

    private String city;
    private String currency;
    private Service service;

    @FXML public TextField countryInput;
    @FXML public Button updateButton;
    @FXML public TextField currencyInput;
    @FXML public TextField cityInput;
    @FXML public Button changeDataButton;
    @FXML public Label NBPRate;
    @FXML public Label currencyRate;
    @FXML public Label weatherInfo;
    @FXML public WebView Wikipedia;

    public Controller(Service service, String city, String currency) {
        this.service = service;
        this.city = city;
        this.currency = currency;
    }

    @FXML
    private void initialize() {
        countryInput.setPromptText(service.getCountry());
        cityInput.setPromptText(city);
        currencyInput.setPromptText(currency);

        WebEngine engine = Wikipedia.getEngine();
        engine.load("https://en.wikipedia.org/wiki/" + city);
        
        weatherInfo.setText(service.getWeatherObject().getInfo());

        currencyRate.setText(String.valueOf(service.getRateFor(currency)));
        NBPRate.setText(String.valueOf(service.getNBPRate()));

        System.out.println("initialized");
    }
    
    @FXML
    private void updateData() {
        if (!countryInput.getText().isEmpty())
            service = new Service(countryInput.getText());

        if (!cityInput.getText().isEmpty())
            city = cityInput.getText();

        if (!currencyInput.getText().isEmpty())
            currency = currencyInput.getText();

        service.getWeather(city);
        weatherInfo.setText(service.getWeatherObject().getInfo());

        currencyRate.setText(String.valueOf(service.getRateFor(currency)));
        NBPRate.setText(String.valueOf(service.getNBPRate()));

        WebEngine engine = Wikipedia.getEngine();
        engine.load("https://en.wikipedia.org/wiki/" + city);
    }
}
