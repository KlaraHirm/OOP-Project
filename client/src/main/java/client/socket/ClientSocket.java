package client.socket;

import client.scenes.MainPageCtrl;
import client.utils.ServerUtils;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.inject.Inject;

public class ClientSocket implements Runnable{

    private String server;
    private ServerUtils serverUtils;
    public StompSessionHandler handler;
    private MainPageCtrl pageCtrl;

    /**
     * constructor for the socket
     * @param serverUtils
     * @param pageCtrl
     */
    @Inject
    public ClientSocket(ServerUtils serverUtils, MainPageCtrl pageCtrl) {
        this.serverUtils = serverUtils;
        this.pageCtrl = pageCtrl;

    }

    /**
     * run method for the thread
     */
    public void run() {
        //set up the socket
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient =
                new WebSocketStompClient(webSocketClient);
        //message converter for json
        stompClient.setMessageConverter(new StringMessageConverter());
        //create a session handler
        handler = new StompSessionHandler(serverUtils, pageCtrl);

        //get the server URL
        server= serverUtils.getServerURL();
        //delete the http and add the ws
        String original="http";
        String replacement = "ws";
        String serverAddress = server.replace(original, replacement);
        serverAddress = serverAddress + "/hello";
        //connect the websocket
        stompClient.connect(serverAddress, handler);

        //keep the thread running
        while(true) { }
    }
}
