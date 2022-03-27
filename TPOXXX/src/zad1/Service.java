package zad1;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Service {
    private final String country;
    private final String localCurrencyCode;
    private Weather weatherObject;

    public Service(String country) {
        this.country = country;
        Locale locale = Arrays.stream(Locale.getAvailableLocales())
                .filter(loc -> loc.getDisplayCountry(Locale.ENGLISH).equals(country))
                .findFirst()
                .get();
        this.localCurrencyCode = Currency.getInstance(locale).getCurrencyCode();
    }

    public String getCountry() {
        return country;
    }

    public Weather getWeatherObject() {
        return weatherObject;
    }

    public String getWeather(String city) {
        String key = "e10ed18f4a2394bca3578c708d321bd4";
        URL url = null;
        try {
            url = new URL("https://api.openweathermap.org/data/2.5/weather?q="+city+","+country+"&appid="+key);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String s = getResponseString(url);

        Gson gson = new Gson();
        Map map = gson.fromJson(s, Map.class);
        String sky = (String)((Map)((List)map.get("weather")).get(0)).get("main");
        Map main = (Map)map.get("main");
        double temp = (double) main.get("temp");
        double pressure = (double) main.get("pressure");
        double humidity = (double) main.get("humidity");
        double wind = (double) ((Map)map.get("wind")).get("speed");
        this.weatherObject = new Weather(city, sky, temp, pressure, humidity, wind);
        return weatherObject.toString();
    }

    public Double getRateFor(String currency) {
        URL url = null;
        try {
            url = new URL("https://api.exchangerate.host/latest?base="+currency+"&symbols="+localCurrencyCode);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String s = getResponseString(url);

        Gson gson = new Gson();
        Map data = gson.fromJson(s, Map.class);
        double rate = (double) ((Map)data.get("rates")).get(localCurrencyCode);
        System.out.println(rate);
        return rate;
    }

    private String getResponseString(URL url) {
        String s = "";

        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
            String line;
            while((line = in.readLine()) != null)
                s += line;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public Double getNBPRate() {
        if (localCurrencyCode.equals("PLN")) {
            System.out.println(1);
            return 1d;
        }

        String kursyA = "http://www.nbp.pl/kursy/kursya.html";
        String kursyB = "http://www.nbp.pl/kursy/kursyb.html";

        Document docA = null, docB = null;
        try {
            docA = Jsoup.connect(kursyA).get();
            docB = Jsoup.connect(kursyB).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements rows = docA.select("tr");
        rows.addAll(docB.select("tr"));

        double rate = -1d;

        for (Element row: rows) {
            Elements columns = row.select("td");
            if (columns.size() != 3) continue;

            String[] data = columns.get(1).text().split(" ");
            int value = Integer.parseInt(data[0]);
            String code = data[1];
            rate = Double.parseDouble(columns.get(2).text().replace(',', '.'));
            if (code.equals(localCurrencyCode)) {
                System.out.println(value);
                System.out.println(code);
                System.out.println(rate);
                return rate;
            }
        }
        return rate;
    }
}
