package client.socket;

import client.utils.ServerUtils;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

public class StompSessionHandler extends StompSessionHandlerAdapter {

    private ServerUtils serverUtils;

    public StompSessionHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    public void afterConnected(StompSession session, StompHeaders headers) {
        serverUtils.passSession(session);
    }


}
