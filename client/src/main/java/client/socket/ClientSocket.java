package client.socket;

import client.utils.ServerUtils;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ClientSocket implements Runnable{

    private String server;
    private ServerUtils serverUtils;

    /**
     * constructor for the socket
     * @param serverUtils
     */
    public ClientSocket(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    /**
     * run method for the thread
     */
    public void run() {
        //set up the socket
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        //message converter for json
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        //connect to server
        stompClient.connect("ws://localhost:8080/hello", new StompSessionHandler(serverUtils));
        //keep the thread running
        while(true) { }
    }
}
