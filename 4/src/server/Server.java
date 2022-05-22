package server;

import com.google.gson.Gson;
import models.article.Article;
import models.request.Request;
import models.request.admin.AddArticlesRequest;
import models.request.admin.AddTopicsRequest;
import models.request.admin.RemoveTopicsRequest;
import models.request.response.EmptyResponse;
import models.request.response.GetArticlesResponse;
import models.request.response.GetTopicsResponse;
import models.request.response.RegisterUserResponse;
import models.request.user.*;
import models.user.User;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.*;

public class Server {
    public final static int PORT = 12413;
    public static String ADDRESS = "localhost";
    private static List<String> TOPICS;
    private static List<User> USERS;
    private static List<Article> ARTICLES;

    public static boolean handleRequest(SocketChannel channel) throws IOException {
        if (!channel.isOpen()) return false;

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        StringBuffer stringBuffer = new StringBuffer();
        Charset charset  = Charset.forName("ISO-8859-2");

        stringBuffer.setLength(0);
        buffer.clear();

        long start = System.currentTimeMillis();
        int timeout = 100;
        readLoop:
        while (true) {
            buffer.clear();
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
            }
            if (System.currentTimeMillis() - start > timeout) return false;
        }


        String requestJson = stringBuffer.toString();
        System.out.println(requestJson);

        Gson gson = new Gson();
        Request request = gson.fromJson(requestJson, Request.class);

        Class requestClass = switch (request.getHeader()) {
            case Request.REGISTER_REQUEST -> RegisterUserRequest.class;
            case Request.GET_TOPICS_REQUEST -> GetTopicsRequest.class;
            case Request.GET_ARTICLES_REQUEST -> GetArticlesRequest.class;
            case Request.SUBSCRIBE_TOPICS_REQUEST -> SubscribeTopicsRequest.class;
            case Request.UNSUBSCRIBE_TOPICS_REQUEST -> UnsubscribeTopicsRequest.class;
            case Request.ADD_ARTICLES_REQUEST -> AddArticlesRequest.class;
            case Request.ADD_TOPICS_REQUEST -> AddTopicsRequest.class;
            case Request.REMOVE_TOPICS_REQUEST -> RemoveTopicsRequest.class;
            default -> null;
        };

        request = (Request) gson.fromJson(requestJson, requestClass);

        Request response = switch (request.getHeader()) {
            case Request.REGISTER_REQUEST -> registerNewUser(request);
            case Request.GET_TOPICS_REQUEST -> getTopics();
            case Request.GET_ARTICLES_REQUEST -> getArticles(request);
            case Request.SUBSCRIBE_TOPICS_REQUEST -> subscribeTopics(request);
            case Request.UNSUBSCRIBE_TOPICS_REQUEST -> unsubscribeTopics(request);
            case Request.ADD_ARTICLES_REQUEST -> addArticles(request);
            case Request.ADD_TOPICS_REQUEST -> addTopics(request);
            case Request.REMOVE_TOPICS_REQUEST -> removeTopics(request);
            default -> throw new IllegalStateException("Unexpected value: " + request.getHeader());
        };

        String responseJson = gson.toJson(response);
        System.out.println("\t"+responseJson+"\n");
        channel.write(charset.encode(CharBuffer.wrap(responseJson + "\n")));
        return true;
    }

    private static Request removeTopics(Request request) {
        RemoveTopicsRequest removeTopicsRequest = (RemoveTopicsRequest) request;
        TOPICS.removeAll(removeTopicsRequest.getTopics());
        ARTICLES.removeAll(
            ARTICLES.stream()
                    .filter(article -> removeTopicsRequest.getTopics().contains(article.getTopic()))
                    .toList()
        );
        return new EmptyResponse();
    }

    private static Request addTopics(Request request) {
        AddTopicsRequest addTopicsRequest = (AddTopicsRequest) request;
        TOPICS.addAll(addTopicsRequest.getTopics());
        return new EmptyResponse();
    }

    private static Request addArticles(Request request) {
        AddArticlesRequest addArticlesRequest = (AddArticlesRequest) request;
        ARTICLES.addAll(addArticlesRequest.getArticles());
        return new EmptyResponse();
    }

    private static Request unsubscribeTopics(Request request) {
        return null;
    }

    private static Request subscribeTopics(Request request) {
        SubscribeTopicsRequest subscribeTopicsRequest = (SubscribeTopicsRequest) request;
        User user = USERS.stream()
                .filter(u -> u.getId() == subscribeTopicsRequest.getUserId())
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        user.getTopics().clear();
        user.getTopics().addAll(subscribeTopicsRequest.getTopics());
        return new EmptyResponse();
    }

    private static Request getArticles(Request request) {
        GetArticlesRequest getArticlesRequest = (GetArticlesRequest) request;
        User user = USERS.stream()
                .filter(u -> u.getId() == getArticlesRequest.getUserId())
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        List<Article> articles = ARTICLES.stream()
                .filter(article -> user.getTopics().contains(article.getTopic()))
                .toList();
        return new GetArticlesResponse(articles);
    }

    private static Request getTopics() {
        return new GetTopicsResponse(TOPICS);
    }

    private static Request registerNewUser(Request request) {
        RegisterUserRequest registerUserRequest = (RegisterUserRequest) request;
        int id = registerUserRequest.getId();
        if (id == -1) {
            id = USERS.stream()
                    .mapToInt(User::getId)
                    .max()
                    .orElse(0) + 1;
            USERS.add(new User(id));
        }

        return new RegisterUserResponse(id);
    }

    public static void main(String[] args) {
        String lipsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";
        TOPICS = new ArrayList<>(List.of("Politics", "Sport", "Celebrities", "Cooking", "Dating", "Movies", "Music"));
        ARTICLES = new ArrayList<>(TOPICS.stream()
                .map(t -> new Article(t, lipsum.substring(0, (int)(Math.random() * lipsum.length() / 2) + lipsum.length()/2)))
                .toList());
        USERS = new ArrayList<>();

        try (
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            Selector selector = Selector.open()
        ) {
            serverSocket.socket().bind(new InetSocketAddress(ADDRESS, PORT));
            serverSocket.configureBlocking(false);

            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Server started");

            while (true) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        SocketChannel clientChannel = serverSocket.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    }
                    if (key.isReadable()) {
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        if(!handleRequest(clientChannel)) {
                            key.cancel();
                        }
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
