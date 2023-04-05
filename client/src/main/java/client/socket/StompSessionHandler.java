package client.socket;

import client.utils.ServerUtils;
import commons.CardList;
import javafx.application.Platform;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StompSessionHandler extends StompSessionHandlerAdapter {

    private ServerUtils serverUtils;

    private StompSession session;

    private List<Subscription> subscriptionList;

    public StompSessionHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
        subscriptionList = new ArrayList<>();
    }

    public void afterConnected(StompSession session, StompHeaders headers) {
        serverUtils.passSession(session);
        serverUtils.passStompSessionHandler(this);
        this.session = session;
    }


    public void subscribe(long id) {
        for (Subscription sub : subscriptionList) sub.unsubscribe();

        Subscription addListSubscription = session.subscribe(
                "/topic/lists/" + Long.toString(id) + "/add",
                new StompSessionHandlerAdapter() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return CardList.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        Platform.runLater( () -> {
                            System.out.println((CardList) payload);
                        });
                    }
                }
        );
    }
}
