package com.example.tpo6.application;

import com.example.tpo6.dto.MessageDto;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Controller implements MessageListener {

    @FXML public TextField messageInput;
    @FXML public Button sendButton;
    @FXML public VBox messagesBox;
    @FXML public TextField usernameInput;
    @FXML public Button usernameSaveButton;
    @FXML public ScrollPane messagesScrollPane;

    JsonAdapter<MessageDto> jsonAdapter = new Moshi.Builder().build().adapter(MessageDto.class);

    Connection connection;
    MessageProducer messageProducer;
    Session session;
    String username = "anon";

    public void initialize() {
        messagesBox.heightProperty().addListener((observableValue, number, t1) -> messagesScrollPane.setVvalue((Double) t1));

        Hashtable environment = new Hashtable(11);
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        environment.put(Context.PROVIDER_URL, "tcp://localhost:61616");
        environment.put("topic.topic1", "topic1");

        try {
            Context context = new InitialContext(environment);
            ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
            String adminTopicName = "topic1";

            Destination destination = (Destination) context.lookup(adminTopicName);
            connection = factory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer receiver = session.createConsumer(destination);
            messageProducer = session.createProducer(destination);
            receiver.setMessageListener(this);

            connection.start();
            System.out.println("Receiver started");
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        try {
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        System.out.println("bye bye");
    }

    public void sendMessage() {
        String message = messageInput.getText();
        if (!message.isEmpty()) {
            System.out.println("Sending message: " + message);
            MessageDto messageDto = new MessageDto(username, message, System.currentTimeMillis());
            try {
                TextMessage textMessage = session.createTextMessage();
                textMessage.setText(jsonAdapter.toJson(messageDto));
                messageProducer.send(textMessage);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        messageInput.setText("");
    }

    public void saveUsername() {
        String newUsername = usernameInput.getText();
        if (!newUsername.isEmpty()) {
            username = newUsername;
        }
    }

    @Override
    public void onMessage(Message message) {

        Platform.runLater(() -> {
            MessageDto messageDto = null;
            try {
                messageDto = jsonAdapter.fromJson(((TextMessage)message).getText());
            } catch (JMSException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (messageDto == null)
                return;

            List<Node> nodes = new ArrayList<>();

            HBox hbox = new HBox();
            hbox.setPadding(new Insets(5, 5, 5, 10));
            HBox hbox2 = new HBox();
            hbox2.setPadding(new Insets(0, 5, 0, 5));

            Text text = new Text(messageDto.getMessage());
            Text usernameText = new Text(messageDto.getUser());
            TextFlow textFlow = new TextFlow(text);
            textFlow.setPadding(new Insets(5, 10, 5, 10));

            if (messageDto.getUser().equals(username)) {
                hbox.setAlignment(Pos.CENTER_RIGHT);
                hbox2.setAlignment(Pos.CENTER_RIGHT);
                textFlow.setStyle("""
                -fx-color: rgb(239, 242, 255);
                -fx-background-color: rgb(15, 125, 242);
                -fx-background-radius: 20px
                """);
                text.setFill(Color.color(.934, .945, .996));
            } else {
                hbox.setAlignment(Pos.CENTER_LEFT);
                hbox2.setAlignment(Pos.CENTER_LEFT);
                textFlow.setStyle("""
                -fx-background-color: rgb(233, 233, 235);
                -fx-background-radius: 20px
                """);
                nodes.add(hbox2);
            }

            nodes.add(hbox);
            hbox.getChildren().add(textFlow);
            hbox2.getChildren().add(usernameText);
            messagesBox.getChildren().addAll(nodes);
        });

    }
}