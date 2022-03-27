/**
 *
 *  @author Olesiński Wojciech S22368
 *
 */

package zad1;


import javafx.application.Application;
import javafx.application.Platform;
import zad1.fx.GUI;

import java.io.File;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Service s = new Service("Poland");
        String weatherJson = s.getWeather("Warsaw");
        Double rate1 = s.getRateFor("USD");
        Double rate2 = s.getNBPRate();
        // ...
        // część uruchamiająca GUI
        Application.launch(GUI.class);
        String classpath = System.getProperty("java.class.path");
        String[] classPathValues = classpath.split(File.pathSeparator);
        Arrays.stream(classPathValues).forEach(System.out::println);
    }
}

