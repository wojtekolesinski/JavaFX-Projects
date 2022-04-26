package handlers;

import dto.DataTransferObject;

import java.net.DatagramPacket;
import java.net.InetAddress;

public interface Handler {
    default String parsePacket(DatagramPacket packet) {
        return  new String(packet.getData(), 0, packet.getLength());
    }

    DataTransferObject parseRequest(String request);
    void sendData(String data, InetAddress address, int port);
}
