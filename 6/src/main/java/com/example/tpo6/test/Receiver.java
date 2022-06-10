package com.example.tpo6.test;

import javax.jms.*;
import javax.naming.*;
import java.util.Hashtable;


public class Receiver {

    public static void main(String[] args) {
        Connection connection = null;
        try {
            Hashtable environment = new Hashtable(11);
            environment.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            environment.put(Context.PROVIDER_URL, "tcp://localhost:61616");
            environment.put("topic.queue1", "queue1");

            Context context = new InitialContext(environment);
            ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
            String adminDestinationName = args[0];
            System.out.println(adminDestinationName);
            Destination destination = (Destination) context.lookup(adminDestinationName);
            connection = factory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer receiver = session.createConsumer(destination);
            MessageListener listener = new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        System.out.println(((TextMessage)message).getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            };
            receiver.setMessageListener(listener);

            connection.start();
            System.out.println("Receiver started");
            while (true) {
                Thread.sleep(1000);
            }
//            Message message = receiver.receive();
//            if (message instanceof TextMessage) {
//                TextMessage text = (TextMessage) message;
//                System.out.println("Received: " + text.getText());
//            } else if (message != null) {
//                System.out.println("Received non text message");
//            }
//            message = receiver.receive();
//            if (message instanceof TextMessage) {
//                TextMessage text = (TextMessage) message;
//                System.out.println("Received: " + text.getText());
//            } else if (message != null) {
//                System.out.println("Received non text message");
//            }
        } catch (Exception exc) {
            exc.printStackTrace();
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