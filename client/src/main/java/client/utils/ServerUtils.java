package client.utils;

import commons.Board;
import commons.Card;
import commons.CardList;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

    /**
     * get all existing boards in db
     * @return list of all boards
     */
    public List<Board> getBoards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/board") //
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
                .target(SERVER).path("api/board") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    /**
     * delete board based on its id
     * @param board object of class Board to be deleted
     * @return response
     */
    public Response deleteBoard(Board board){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/board/"+board.id) //
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
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/list/"+listId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<CardList>() {});
    }

    /**
     * add newly created list to db
     * @param board object of class Board in which the list is
     * @param list object of class CardList which is to be added to db
     * @return should be the same list but with the generated id //TODO for now it is not working the id is still 0
     */
    public CardList addList(Board board, CardList list) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/board/"+board.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(list, APPLICATION_JSON), CardList.class);
    }

    /**
     * delete list based on the board id and list id
     * @param board object of class Board where the list is
     * @param list object of class CardList to be deleted //TODO for now not rly working because we dont have the real id of list
     * @return response
     */
    public Response deleteList(Board board, CardList list) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/list/"+list.id) //
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
                .target(SERVER).path("api/list/"+list.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * delete specified card
     * @param card card object to delete
     * @return deleted card (for undo)
     */
    public Card deleteCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/card/"+card.id) //
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
                .target(SERVER).path("api/card") //
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
    public CardList editCardPosition(Card card, CardList original, CardList target) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/list/reorder") //
                .queryParam("original", original.id) //
                .queryParam("target", target.id) //
                .queryParam("cardId", card.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(card, APPLICATION_JSON), CardList.class);
    }

}
