package client;

import com.google.gson.Gson;
import dto.*;
import server.MainServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.List;

public class SimpleClient {

    private int port;

    public SimpleClient(int port) {
        this.port = port;
    }

    public List<String> getAvailableLanguages() {
        var request = new ListLanguagesRequest();
        var data = new Gson().toJson(request);
        var response = askServer(data);
        var langs = new Gson().fromJson(response, ListLanguagesResponse.class);
        return langs.getLanguages();
    }

    public String askServer(String data) {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            socket.setSoTimeout(500);

            DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), InetAddress.getLocalHost(), MainServer.port);
            socket.send(packet);

            byte[] receivedData = new byte[1024];
            DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
            try {
                socket.receive(receivedPacket);
            } catch (SocketTimeoutException e) {
                var gson = new Gson();
                System.out.printf("Request %s timed out\n", data);
                var request = gson.fromJson(data, DataTransferObject.class);
                if (request.getHeader().equals("ClientRequest")) {
                    var clientRequest = gson.fromJson(data, ClientRequest.class);
                    var timeOutInfo = new RequestTimedOut(clientRequest.getLanguage());
                    var timeOutData = gson.toJson(timeOutInfo);
                    packet.setData(timeOutData.getBytes());
                    socket.send(packet);
                }
                return gson.toJson(new ClientResponse("Request timed out"));
            }
            return parsePacket(receivedPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parsePacket(DatagramPacket packet) {
        return new String(packet.getData(), 0, packet.getLength());
    }

    public String translate(String language, String word) {
        var request = new ClientRequest(word, language, port);
        var gson = new Gson();
        var response = askServer(gson.toJson(request));

        var parsedResponse = gson.fromJson(response, ClientResponse.class);
        return parsedResponse.getWord();
    }
}
