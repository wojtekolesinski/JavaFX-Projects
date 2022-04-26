package handlers;

import com.google.gson.Gson;
import dto.ClientResponse;
import dto.DataTransferObject;
import dto.LanguageServerRequest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;

public class LanguageServerHandler implements Runnable, Handler {
    DatagramSocket socket;
    LanguageServerRequest request;
    Map<String, String> dictionary;

    public LanguageServerHandler(DatagramSocket socket, DatagramPacket receivedPacket, Map<String, String> dictionary) {
        this.socket = socket;
        this.request = (LanguageServerRequest) parseRequest(parsePacket(receivedPacket));
        this.dictionary = dictionary;
    }

    @Override
    public DataTransferObject parseRequest(String request) {
        return new Gson().fromJson(request, LanguageServerRequest.class);
    }

    @Override
    public void sendData(String data, InetAddress address, int port) {
        DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        var word = dictionary.getOrDefault(request.getWord(), "not found");
        var clientResponse = new ClientResponse(word);
        sendData(new Gson().toJson(clientResponse), request.getClientAddress(), request.getClientPort());
    }
}
