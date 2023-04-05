package client.utils;

import commons.Board;
import commons.Card;
import commons.CardList;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    private static String serverURL = "http://localhost:8080/";
    private static boolean connected = false;

    /**
     * get all existing boards in db
     * @return list of all boards
     */
    public List<Board> getBoards() {
        if(!connected)
            return new ArrayList<>();
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("api/board") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Board>>() {});
    }

    /**
     * add new board to db
     * @param board object of class Board to be added
     * @return the same board, however with generated id (before that it was always 0)
     */
    public Board addBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
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
        return ClientBuilder.newClient(new ClientConfig()) //
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
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("api/board/"+board.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    /**
     * add newly created list to db
     * @param board object of class Board in which the list is
     * @param list object of class CardList which is to be added to db
     * @return should be the same list but with the generated id
     */
    public CardList addList(Board board, CardList list) {
        return ClientBuilder.newClient(new ClientConfig()) //
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
        return ClientBuilder.newClient(new ClientConfig()) //
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
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("api/list/"+list.id) //
                .queryParam("boardId", board.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    /**
     * add newly created card to db
     * @param list object of class CardList where the card is
     * @param card newly create object of class Card which is to be added to the db
     * @return updated card (with updated id)
     */
    public Card addCard(CardList list, Card card) {
        return ClientBuilder.newClient(new ClientConfig()) //
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
        return ClientBuilder.newClient(new ClientConfig()) //
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
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("api/card") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(card, APPLICATION_JSON), Card.class);
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
            Response response = ClientBuilder.newClient(new ClientConfig()).target(URL).path("api/board").request(APPLICATION_JSON).accept(APPLICATION_JSON).get();
            connected = response.getStatus() == 200;
        } catch (Exception e) {}
        return connected;
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
     * set values for server url and connect directly
     * SHOULD ONLY BE USED FOR TESTS
     * @param serverURL the value for the serverURL property
     * @param connected the value for the connected property
     */
    public void _setValues(String serverURL, boolean connected){
        ServerUtils.serverURL = serverURL;
        ServerUtils.connected = connected;
    }

}
