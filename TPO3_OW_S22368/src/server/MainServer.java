package server;

import handlers.MainServerHandler;
import javafx.util.Pair;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainServer {

    private static final Map<String, Pair<InetAddress, Integer>> children = new HashMap<>();
    public final static int port = 3473;

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);

        try (DatagramSocket serverSocket = new DatagramSocket(port)) {

            while (true) {
                byte[] receivedData = new byte[1024];
                DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
                serverSocket.receive(receivedPacket);

                threadPool.submit(new MainServerHandler(receivedPacket, serverSocket, children));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String parsePacket(DatagramPacket packet) {
        return  new String(packet.getData(), 0, packet.getLength());
    }
}
