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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//@Service
public class StompSessionHandler extends StompSessionHandlerAdapter {

    private ServerUtils serverUtils;

    private StompSession session;

    private List<Subscription> subscriptionList;

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
        //System.out.println("this is the afterConnected method running");
        serverUtils.passSession(session);
        serverUtils.passStompSessionHandler(this);
        this.session = session;
        //System.out.println("this is the afterConnected method still running");
    }


    public void subscribe(long id) {

        for (Subscription sub : subscriptionList) sub.unsubscribe();
        subscribeToBoardUpdates(id);
        subscribeToListUpdates(id);
        subscribeToCardUpdates(id);

    }

    /**
     * a subscribe method for any board updates
     * @param id
     */
    public void subscribeToBoardUpdates(long id) {
        Subscription boardUpdateSubscription = session.subscribe(
                "/topic/boards/",
                new StompSessionHandlerAdapter() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Board.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        Platform.runLater( () -> {
                            pageCtrl.refresh();
                        });
                    }
                }
        );
    }


    /**
     * a subscribe method for any list updates
     * @param id
     */
    public void subscribeToListUpdates(long id) {
        Subscription addListSubscription = session.subscribe(
                "/topic/lists/",
                new StompSessionHandlerAdapter() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return CardList.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        Platform.runLater( () -> {
                            pageCtrl.refresh();
                        });
                    }
                }
        );
    }


    /**
     * a subscribe method for any card updates
     * @param id
     */
    public void subscribeToCardUpdates(long id) {
        Subscription addCardSubscription = session.subscribe(
                "/topic/cards/",
                new StompSessionHandlerAdapter() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Card.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        Platform.runLater( () -> {
                            pageCtrl.refresh();
                        });
                    }
                }
        );
    }


}
