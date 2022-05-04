package server;

import com.google.gson.Gson;
import models.article.Article;
import models.article.Topic;
import models.request.admin.AddTopicsRequest;
import models.request.admin.RemoveTopicsRequest;
import models.request.response.GetArticlesResponse;
import models.request.response.GetTopicsResponse;
import models.request.response.RegisterUserResponse;
import models.request.user.GetArticlesRequest;
import models.request.user.GetTopicsRequest;
import models.request.user.RegisterUserRequest;
import models.request.user.SubscribeTopicsRequest;
import models.user.User;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Admin {

    private static final Charset charset = Charset.forName("ISO-8859-2");
    private static final Gson gsonInstance = new Gson();

    public static void main(String[] args) throws IOException {
        Admin admin = new Admin();
    }

    public static List<String> getTopics() {
        GetTopicsRequest request = new GetTopicsRequest();
        String response = sendData(gsonInstance.toJson(request));
        GetTopicsResponse getTopicsResponse = gsonInstance.fromJson(response, GetTopicsResponse.class);
        return getTopicsResponse.getTopics();
    }

    public static String sendData(String data) {
        try {
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(Server.ADDRESS, Server.PORT));


            while (!channel.finishConnect()) {
                System.out.print("waiting \t " + LocalDateTime.now() + "\r");
                Thread.sleep(100);
            }

            int rozmiar_bufora = 1024;
            channel.write(charset.encode(data + "\n"));

            ByteBuffer buffer = ByteBuffer.allocate(rozmiar_bufora);
            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.setLength(0);
            buffer.clear();

            readLoop:
            while (true) {
                int n = channel.read(buffer);
                if (n > 0) {
                    buffer.flip();
                    CharBuffer cbuf = charset.decode(buffer);
                    while (cbuf.hasRemaining()) {
                        char c = cbuf.get();
                        if (c == '\r' || c == '\n')
                            break readLoop;
                        else
                            stringBuffer.append(c);
                    }
                    buffer.clear();
                }
            }
            return stringBuffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateTopics(List<String> newTopics) {
        newTopics = new ArrayList<>(newTopics);
        List<String> oldTopics = getTopics();
        newTopics.removeAll(oldTopics);
        AddTopicsRequest request = new AddTopicsRequest(newTopics);
        sendData(gsonInstance.toJson(request));
    }

    public void removeTopics(List<String> topics) {
        RemoveTopicsRequest request = new RemoveTopicsRequest(topics);
        sendData(gsonInstance.toJson(request));
    }
}