package client.socket;

import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
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
    public void afterConnected(StompSession session, StompHeaders headers) {
        serverUtils.passSession(session);
        serverUtils.passStompSessionHandler(this);
        this.session = session;
    }


    public void subscribe(long id) {
        for (Subscription sub : subscriptionList) sub.unsubscribe();
        subscribeToEditBoard(id);
        subscribeToDeleteBoard(id);
        subscribeToAddList(id);
        subscribeToEditList(id);
        subscribeToDeleteList(id);
        subscribeToAddCard(id);
        subscribeToDeleteCard(id);
        subscribeToEditCard(id);
    }

    /**
     * a subscribe method for editing a board
     * @param id
     */
    public void subscribeToEditBoard(long id) {
        Subscription editBoardSubscription = session.subscribe(
                "/topic/boards/" + Long.toString(id) + "/edit",
                new StompSessionHandlerAdapter() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Board.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        Platform.runLater( () -> {
                            System.out.println((Board) payload);
                        });
                    }
                }
        );
    }

    /**
     * a subscribe method for deleting a board
     * @param id
     */
    public void subscribeToDeleteBoard(long id) {
        Subscription deleteBoardSubscription = session.subscribe(
                "/topic/boards/" + Long.toString(id) + "/delete",
                new StompSessionHandlerAdapter() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Board.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        Platform.runLater( () -> {
                            System.out.println((Board) payload);
                        });
                    }
                }
        );
    }

    /**
     * a subscribe method for adding a list
     * @param id
     */
    public void subscribeToAddList(long id) {
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

    /**
     * a subscribe method for editing a list
     * @param id
     */
    public void subscribeToEditList(long id) {
        Subscription addListSubscription = session.subscribe(
                "/topic/lists/" + Long.toString(id) + "/edit",
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

    /**
     * a subscribe method for deleting a list
     * @param id
     */
    public void subscribeToDeleteList(long id) {
        Subscription addListSubscription = session.subscribe(
                "/topic/lists/" + Long.toString(id) + "/delete",
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

    /**
     * a subscribe method for adding a card
     * @param id
     */
    public void subscribeToAddCard(long id) {
        Subscription addCardSubscription = session.subscribe(
                "/topic/cards/" + Long.toString(id) + "/add",
                new StompSessionHandlerAdapter() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Card.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        Platform.runLater( () -> {
                            System.out.println((Card) payload);
                        });
                    }
                }
        );
    }

    /**
     * a subscribe method for deleting a card
     * @param id
     */
    public void subscribeToDeleteCard(long id) {
        Subscription deleteCardSubscription = session.subscribe(
                "/topic/cards/" + Long.toString(id) + "/delete",
                new StompSessionHandlerAdapter() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Card.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        Platform.runLater( () -> {
                            System.out.println((Card) payload);
                        });
                    }
                }
        );
    }

    /**
     * a subscribe method for editing a card
     * @param id
     */
    public void subscribeToEditCard(long id) {
        Subscription editCardSubscription = session.subscribe(
                "/topic/cards/" + Long.toString(id) + "/edit",
                new StompSessionHandlerAdapter() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Card.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        Platform.runLater( () -> {
                            System.out.println((Card) payload);
                        });
                    }
                }
        );
    }
}
