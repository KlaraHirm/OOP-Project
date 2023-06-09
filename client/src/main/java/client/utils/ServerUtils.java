package client.utils;

import client.scenes.MainPageCtrl;
import client.socket.ClientSocket;
import client.socket.StompSessionHandler;
import commons.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Service;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Service
@Singleton
public class ServerUtils {

    public Client client;

    public static String serverURL = "http://localhost:8080/";
    public static boolean connected = false;

    public ServerUtils() {
        client = ClientBuilder.newClient(new ClientConfig());
    }

    private StompSession session;
    private StompSessionHandler stompSessionHandler;

    /**
     * get all existing boards in db
     * @return list of all boards
     */
    public List<Board> getBoards() {
        if(!connected)
            return new ArrayList<>();
        return client //
                .target(serverURL).path("api/board") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Board>>() {});
    }

    /**
     * get board with ID
     * @param id id of board to be returned
     * @return board with board.id == id
     */
    public Board getBoard(long id)
    {
        if(!connected)
            return null;
        return client //
                .target(serverURL).path("api/board/"+id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON).get(Board.class);
    }

    /**
     * add new board to db
     * @param board object of class Board to be added
     * @return the same board, however with generated id (before that it was always 0)
     */
    public Board addBoard(Board board) {
        return client //
                .target(serverURL).path("api/board") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    /**
     * edit board
     * @param board object of class Board to be edited (with corresponding id)
     * @return the edited board
     */
    public Board editBoard(Board board) {
        return client //
                .target(serverURL).path("api/board") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    /**
     * delete board based on its id
     * @param board object of class Board to be deleted
     * @return response
     */
    public Response deleteBoard(Board board){
        return client //
                .target(serverURL).path("api/board/"+board.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    /**
     * getter for list
     * @param listId id of list which we want to get
     * @return object of class CardList which has the same id as passed in listId
     */
    public CardList getList(long listId) {
        return client //
                .target(serverURL).path("api/list/"+listId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<CardList>() {});
    }

    /**
     * add newly created list to db
     * @param board object of class Board in which the list is
     * @param list object of class CardList which is to be added to db
     * @return should be the same list but with the generated id
     */
    public CardList addList(Board board, CardList list) {
        return client //
                .target(serverURL).path("api/board/"+board.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(list, APPLICATION_JSON), CardList.class);
    }

    /**
     * edit a list
     * @param list object of class CardList to be edited (with corresponding id)
     * @return edited card list
     */
    public CardList editList(CardList list) {
        return client //
                .target(serverURL).path("api/list") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(list, APPLICATION_JSON), CardList.class);
    }

    /**
     * delete list based on the board id and list id
     * @param list object of class CardList to be deleted
     * @return response
     */
    public Response deleteList(Board board, CardList list) {
        return client //
                .target(serverURL).path("api/list/"+list.id) //
                .queryParam("boardId", board.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    /**
     * getter for card
     * @param cardId id of card which we want to get
     * @return object of class Card which has the same id as passed in cardId
     */
    public Card getCard(long cardId) {
        return client //
                .target(serverURL).path("api/card/"+cardId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Card>() {});
    }

    /**
     * add newly created card to db
     * @param list object of class CardList where the card is
     * @param card newly create object of class Card which is to be added to the db
     * @return updated card (with updated id)
     */
    public Card addCard(CardList list, Card card) {
        return client //
                .target(serverURL).path("api/list/"+list.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * delete specified card
     * @param card card object to delete
     * @param list list object to update
     * @param board board object to update
     * @return deleted card (for undo)
     */
    public Card deleteCard(Card card, CardList list, Board board) {
        return client //
                .target(serverURL).path("api/card/"+card.id) //
                .queryParam("boardId", board.id) //
                .queryParam("listId", list.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete(Card.class);
    }

    /**
     * edit specified card
     * @param card card object to edit
     * @return edited card
     */
    public Card editCard(Card card) {
        return client //
                .target(serverURL).path("api/card") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * edits position of card, used during drag and drop
     * @param card card being dragged
     * @param original list origin of drag
     * @param target list target of drag
     * @return updated target
     */
    public CardList editCardPosition(Card card, CardList original, CardList target, int cardPlace) {
        return client //
                .target(serverURL).path("api/list/reorder") //
                .queryParam("original", original.id) //
                .queryParam("target", target.id) //
                .queryParam("cardId", card.id) //
                .queryParam("cardPlace", cardPlace) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(card, APPLICATION_JSON), CardList.class);
    }

    /**
     * getter for cards in list
     * @param listId id of list
     * @return list of cards ordered by place
     */
    public List<Card> getCards(long listId) {
        return client //
                .target(serverURL).path("api/list/" + listId + "/cards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Card>>() {
                });
    }

    /**
     * add newly created tag to db
     * @param board object of class Board where the tag is
     * @param tag newly create object of class Tag which is to be added to the db
     * @return updated card (with updated id)
     */
    public Tag addTag(Board board, Tag tag) {
        return client //
                .target(serverURL).path("api/board/tag/"+board.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    /**
     * get all existing boards in db
     * @return list of all boards
     */
    public List<Tag> getTags(Board board) {
        return client //
                .target(serverURL).path("api/board/tags/"+board.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Tag>>() {});
    }

    /**
     * edit specified tag
     * @param tag tag object to edit
     * @return edited tag
     */
    public Tag editTag(Tag tag) {
        return client //
                .target(serverURL).path("api/tag") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    /**
     * delete specified tag
     * @param tag Tag object to delete
     * @return deleted tag (for undo)
     */
    public Tag deleteTag(Tag tag, long boardId) {
        return client //
                .target(serverURL).path("api/tag/"+tag.id) //
                .queryParam("boardId", boardId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete(Tag.class);
    }

    /**
     * add newly created subtask to db
     * @param card object of class Card where the subtask is
     * @param subtask newly create object of class Subtask which is to be added to the db
     * @return updated card (with updated id)
     */
    public Subtask addSubtask(Subtask subtask, Card card) {
        return client //
                .target(serverURL).path("api/card/"+card.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(subtask, APPLICATION_JSON), Subtask.class);
    }

    /**
     * edit subtask
     * @param subtask subtask to be edited
     * @param card card where subtask is
     * @param index new place of subtask
     * @return edited subtask
     */
    public Card reorderSubtask(Subtask subtask, Card card, int index) {
        return client //
                .target(serverURL).path("api/card/reorder") //
                .queryParam("cardId", card.id) //
                .queryParam("subtaskId", subtask.id) //
                .queryParam("subtaskPlace", index) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(subtask, APPLICATION_JSON), Card.class);
    }

    /**
     * start long polling for a card if it was deleted
     * @param cardId id of a card
     * @return true if card was deleted
     */
    public Boolean pollCard(long cardId) {
        Response resp =  null;
        while (resp == null || resp.getStatus() == 503) {
            resp =  ClientBuilder.newClient(new ClientConfig())
                    .target(serverURL)
                    .path("api/card/poll/" + cardId)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON) //
                    .get();
        }
        return resp.readEntity(Boolean.class);

    }

    /**
     * get whether the client is connected to server
     */
    public boolean isConnected(){
        return connected;
    }

    /**
     * connect to server with specified url
     * @param URL server url
     * @return true if succesfully connected, false otherwise
     */
    public boolean connect(String URL){
        connected = false;
        try {
            serverURL = URL;
            Response response = client
                    .target(URL)
                    .path("api/board")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get();
            connected = response.getStatus() == 200;
        } catch (Exception e) {}
        return connected;
    }

    /**
     * check if the admin password is correct
     * @param password The password for the admin page
     * @return true if successfully checked, false otherwise
     */
    public boolean checkPassword(String password){
        Response response = client
                    .target(serverURL)
                    .path("api/admin/check")
                    .queryParam("password", password) //
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get();
        return response.getStatus() == 200;
    }

    /**
     * disconnect from server
     */
    public void disconnect(){
        connected = false;
    }

    /**
     * get server url
     * @return server url
     */
    public String getServerURL(){
        return serverURL;
    }

    /**
     * start the socket thread
     */
    public void socketInit(MainPageCtrl pageCtrl) {
        ClientSocket clientSocket = new ClientSocket(this, pageCtrl);
        //create and start the thread for the socket
        Thread thread = new Thread(clientSocket);
        thread.start();
    }

    /**
     * passes the session
     * @param session
     */
    public void passSession(StompSession session) {
        this.session = session;
    }

    /**
     * passes the session handler
     * @param handler
     */
    public void passStompSessionHandler(StompSessionHandler handler) {
        this.stompSessionHandler = handler;
    }

}
