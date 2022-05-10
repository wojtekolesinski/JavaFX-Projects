package server;

import com.google.gson.Gson;
import models.article.Article;
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
import java.util.List;


public class Client {

    private static final Charset charset = Charset.forName("ISO-8859-2");
    private static final Gson gsonInstance = new Gson();
    private User user;

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.registerUser();
        client.subscribeTopics(getTopics());
        client.getArticles().forEach(a -> {
            System.out.println(a.getTopic());
            System.out.println("\t"+a.getText());
        });
    }

    public void registerUser() {
        RegisterUserRequest request = new RegisterUserRequest();
        String response = sendData(gsonInstance.toJson(request));
        RegisterUserResponse registerUserResponse = gsonInstance.fromJson(response, RegisterUserResponse.class);
        this.user = new User(registerUserResponse.getId());
    }

    public void subscribeTopics(List<String> topics) {
        SubscribeTopicsRequest request = new SubscribeTopicsRequest(user.getId(), topics);
        user.getTopics().clear();
        user.getTopics().addAll(topics);
        sendData(gsonInstance.toJson(request));
    }

    public User getUser() {
        return user;
    }

    public List<Article> getArticles() {
        GetArticlesRequest request = new GetArticlesRequest(user.getId());
        String response = sendData(gsonInstance.toJson(request));
        GetArticlesResponse getArticlesResponse = gsonInstance.fromJson(response, GetArticlesResponse.class);
        return getArticlesResponse.getArticles();
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
}