package client.socket;

import client.scenes.MainPageCtrl;
import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.application.Platform;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//@Service
public class StompSessionHandler extends StompSessionHandlerAdapter {

    private ServerUtils serverUtils;

    private StompSession session;

    private List<Subscription> subscriptionList;
    @Inject
    private MainPageCtrl pageCtrl;

    /**
     * constructor for the stomp session handler
     * @param serverUtils
     */
    public StompSessionHandler(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
        subscriptionList = new ArrayList<>();
    }

    /**
     * passes session and handler to server utils upon connection
     * @param session the client STOMP session
     * @param headers the STOMP CONNECTED frame headers
     */
    @Override
    public void afterConnected(StompSession session, StompHeaders headers) {
        serverUtils.passSession(session);
        serverUtils.passStompSessionHandler(this);
        this.session = session;
    }

    /**
     * a subscribe method for any updates
     * @param id
     */
    @Inject
    public void subscribe(long id) {

        for (Subscription sub : subscriptionList) sub.unsubscribe();

        Subscription updatesSubscription = session.subscribe(
                "/topic/updates",
                new StompSessionHandlerAdapter() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return String.class;
                    }
                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        Platform.runLater( () -> {
                            try {
                                pageCtrl.loadChange();
                            }
                            catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                }
        );
    }




}
