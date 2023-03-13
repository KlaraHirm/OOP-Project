package client.utils;

import commons.Board;
import commons.CardList;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";


    public List<Board> getBoards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/board") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Board>>() {});
    }

    public Board addBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/board") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    //TODO - in server BoardController new card list is created there, however shouldn't new card list be created in client?
    public CardList addList(Board board, CardList list) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/board/"+board.id+"/"+list.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(list, APPLICATION_JSON), CardList.class);
    }

}
