package com.example.tpo6.test;

import javax.naming.*;
import javax.jms.*;
import java.util.Hashtable;
import java.util.Scanner;

public class Sender {

    public static void main(String[] args) {

        Hashtable environment = new Hashtable(11);
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        environment.put(Context.PROVIDER_URL, "tcp://localhost:61616");
        environment.put("topic.topic1", "topic1");

        Connection connection = null;
        try {

            Context context = new InitialContext(environment);
            ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");

            String adminDestinationName = "topic1";
            Destination destination = (Destination) context.lookup(adminDestinationName);
            connection = factory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer sender = session.createProducer(destination);

            // test
//            MessageConsumer receiver = session.createConsumer(destination);

            // end test
            connection.start();
            TextMessage message = session.createTextMessage();
            message.setText(args[1]);
            sender.send(message);
            System.out.println("Sender sent message: " + args[1]);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Enter new message");
                String in = scanner.nextLine();
                message.setText(in);
                sender.send(message);
            }

//            Message receivedMessage = receiver.receive();
//
//            TextMessage text = (TextMessage) receivedMessage;
//            System.out.println("Received: " + text.getText());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException exc) {
                    System.err.println(exc);
                }
            }
        }
//        System.exit(0);
    }
}