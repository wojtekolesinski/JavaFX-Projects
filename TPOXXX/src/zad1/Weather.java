package zad1;

public class Weather {
    private String city;
    private String sky;
    private double temperature;
    private double pressure;
    private double humidity;
    private double wind;

    public Weather(String city, String sky, double temperature, double pressure, double humidity, double wind) {
        this.city = city;
        this.sky = sky;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.wind = wind;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "sky='" + sky + '\'' +
                ", temperature=" + temperature +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", wind=" + wind +
                '}';
    }

    public String getInfo() {
        return "City: " + city +
                "\nSky: " + sky +
                "\nTemperature: " + temperature +
                "\nPressure: " + pressure +
                "\nHumidity: " + humidity +
                "\nWind: " + wind;
    }
}
