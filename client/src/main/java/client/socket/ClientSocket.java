package client.socket;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ClientSocket implements Runnable{

    private String server;

    public void run() {

        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        //connect to server
        stompClient.connect("http://localhost:8080/hello", new StompSessionHandler());

        while(true) { }
    }
}
