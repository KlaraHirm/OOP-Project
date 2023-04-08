package client.socket;

import client.utils.ServerUtils;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.inject.Inject;

public class ClientSocket implements Runnable{

    private String server;
    private ServerUtils serverUtils;
    public StompSessionHandler handler;

    /**
     * constructor for the socket
     * @param serverUtils
     */
    @Inject
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
        handler = new StompSessionHandler(serverUtils);

        //serverUtils.passStompSessionHandler(handler);

        //connect to server
        stompClient.connect("ws://localhost:8080/hello", handler);

        /*if(serverUtils.isConnected()) {
            stompClient.connect(serverUtils.getServerURL(), handler);
        }*/

        //keep the thread running
        while(true) { }
    }
}
