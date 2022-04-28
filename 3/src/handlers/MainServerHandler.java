package handlers;

import com.google.gson.Gson;
import dto.*;
import javafx.util.Pair;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;

public class MainServerHandler implements Runnable, Handler{
    private final DatagramSocket serverSocket;
    private final InetAddress clientAddress;
    private final int clientPort;
    private final Map<String, Pair<InetAddress, Integer>> servers;
    private final DataTransferObject dto;

    public MainServerHandler(DatagramPacket receivedPacket, DatagramSocket serverSocket, Map<String, Pair<InetAddress, Integer>> servers){
        this.serverSocket = serverSocket;
        this.clientAddress = receivedPacket.getAddress();
        this.clientPort = receivedPacket.getPort();
        var data = parsePacket(receivedPacket);
        System.out.println(data);
        this.dto = parseRequest(data);
        this.servers = servers;
    }

    public void sendData(String data, InetAddress address, int port) {
        DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), address, port);
        try {
            serverSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataTransferObject parseRequest(String request) {
        var gson = new Gson();
        var dto = gson.fromJson(request, DataTransferObject.class);
        return switch (dto.getHeader()) {
            case "hello" -> gson.fromJson(request, LanguageServerHello.class);
            case "ClientRequest" -> gson.fromJson(request, ClientRequest.class);
            case "ListLanguages" -> gson.fromJson(request, ListLanguagesRequest.class);
            case "TimedOut" -> gson.fromJson(request, RequestTimedOut.class);
            default -> throw new IllegalStateException("Unexpected value: " + dto.getHeader());
        };
    }

    public void handleHelloRequest() {
        var request = (LanguageServerHello) dto;
        servers.put(request.getLanguage(), new Pair<InetAddress, Integer>(request.getAddress(), request.getPort()));
    }
    public void handleClientRequest() {
        var clientRequest = (ClientRequest) dto;

        var child = servers.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(clientRequest.getLanguage()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(new Pair<InetAddress, Integer>(InetAddress.getLoopbackAddress(), -1));

        if (child.getValue() == -1) {
            var request = new ClientResponse("Language not in the database");
            sendData(new Gson().toJson(request) , clientAddress, clientPort);
            return;
        }

        var request = new LanguageServerRequest(clientRequest.getWord(), clientAddress, clientPort);
        sendData(new Gson().toJson(request), child.getKey(), child.getValue());
    }

    public void handleListLanguagesRequest() {
        var languages = servers.keySet().stream().toList();
        var response = new ListLanguagesResponse(languages);
        sendData(new Gson().toJson(response), clientAddress, clientPort);
    }

    public void handleTimeOut() {
        var info = (RequestTimedOut) dto;
        servers.remove(info.getLanguage());
    }

    @Override
    public void run() {
        switch (dto.getHeader()) {
            case "hello" -> handleHelloRequest();
            case "ClientRequest" -> handleClientRequest();
            case "ListLanguages" -> handleListLanguagesRequest();
            case "TimedOut" -> handleTimeOut();

            default -> throw new IllegalStateException("Unexpected value: " + dto.getHeader());
        }
    }
}
