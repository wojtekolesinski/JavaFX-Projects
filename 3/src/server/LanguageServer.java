package server;

import com.google.gson.Gson;
import dto.LanguageServerHello;
import handlers.LanguageServerHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class LanguageServer {

    private final String language;
    private final Map<String, String> dictionary;

    public LanguageServer(String language) {
        this.language = language;
        dictionary = new HashMap<>();
    }

    public Map<String, String> getDictionary() {
        return dictionary;
    }

    public void loadData() {
        var path = "./data/"+language+".csv";
        try {
            Files.readAllLines(Path.of(path)).forEach(l -> {
                var data = l.split(";");
                dictionary.put(data[0], data[1]);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dumpData() {
        var path = "./data/"+language+".csv";
        var lines = dictionary.entrySet().stream().map(e -> e.getKey() + ";" + e.getValue()).toList();
        try {
            Files.write(Path.of(path), lines, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        var lang = args[0];
        var port = Integer.parseInt(args[1]);
        var languageServer = new LanguageServer(lang);
        languageServer.loadData();

        var threadPool = Executors.newFixedThreadPool(5);

        try (DatagramSocket socket = new DatagramSocket(port)) {
            var request = new LanguageServerHello(lang, InetAddress.getLocalHost(), port);
            var data = new Gson().toJson(request);
            DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), InetAddress.getLocalHost(), MainServer.port);
            socket.send(packet);

            while (true) {
                byte[] receivedData = new byte[1024];
                DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
                socket.receive(receivedPacket);

                threadPool.submit(new LanguageServerHandler(socket, receivedPacket, languageServer.getDictionary()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
