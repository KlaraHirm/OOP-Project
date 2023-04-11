package commons;

import client.utils.ServerUtils;
import commons.mocks.BuilderMock;
import commons.mocks.ClientMock;
import commons.mocks.WebTargetMock;
import jakarta.ws.rs.client.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

public class ServerUtilsTest {

    @Mock
    public ClientMock client;

    @Mock
    public WebTargetMock webTargetMock;

    @Mock
    public BuilderMock builderMock;

    public ServerUtils serverUtils;

    @BeforeEach
    public void setUp()
    {
        builderMock = new BuilderMock();
        webTargetMock = new WebTargetMock(builderMock);
        client = new ClientMock(webTargetMock);

        serverUtils = new ServerUtils();
        serverUtils.client = client;

        serverUtils.serverURL = "http://domain.test:12";
        serverUtils.connected = true;
    }

    /**
     *
     */
    @Test
    void testGetBoards() {
        serverUtils.getBoards();
        assertEquals("http://domain.test:12", client.url);
        assertEquals("api/board", webTargetMock.path);
        assertEquals("GET", builderMock.method);
        assertNull(builderMock.entity);
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testGetBoard() {
        serverUtils.getBoard(1);
        assertEquals("http://domain.test:12", client.url);
        assertEquals("api/board/1", webTargetMock.path);
        assertEquals("GET", builderMock.method);
        assertNull(builderMock.entity);
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testAddBoard() {
        Board board = new Board("title");
        serverUtils.addBoard(board);
        assertEquals("http://domain.test:12", client.url);
        assertEquals("api/board", webTargetMock.path);
        assertEquals("POST", builderMock.method);
        assertEquals(board, builderMock.entity.getEntity());
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testEditBoard() {
        Board board = new Board("title");
        serverUtils.editBoard(board);
        assertEquals("http://domain.test:12", client.url);
        assertEquals("api/board", webTargetMock.path);
        assertEquals("PUT", builderMock.method);
        assertEquals(board, builderMock.entity.getEntity());
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testDeleteBoard() {
        Board board = new Board("title");
        board.id = 1;
        serverUtils.deleteBoard(board);
        assertEquals("http://domain.test:12", client.url);
        assertEquals("api/board/1", webTargetMock.path);
        assertEquals("DELETE", builderMock.method);
        assertNull(builderMock.entity);
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testGetList() {
        serverUtils.getList(1);
        assertEquals("http://domain.test:12", client.url);
        assertEquals("api/list/1", webTargetMock.path);
        assertEquals("GET", builderMock.method);
        assertNull(builderMock.entity);
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testAddList() {
        Board board = new Board("title");
        board.id = 1;
        CardList list = new CardList("title");
        serverUtils.addList(board, list);
        assertEquals("http://domain.test:12", client.url);
        assertEquals("api/board/1", webTargetMock.path);
        assertEquals("POST", builderMock.method);
        assertEquals(list, builderMock.entity.getEntity());
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testEditList() {
        CardList list = new CardList("title");
        serverUtils.editList(list);
        assertEquals("http://domain.test:12", client.url);
        assertEquals("api/list", webTargetMock.path);
        assertEquals("PUT", builderMock.method);
        assertEquals(list, builderMock.entity.getEntity());
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testDeleteList() {
        Board board = new Board("title");
        board.id = 1;
        CardList list = new CardList("title");
        list.id = 2;
        serverUtils.deleteList(board, list);
        assertEquals("http://domain.test:12", client.url);
        assertEquals("api/list/2", webTargetMock.path);
        assertEquals("DELETE", builderMock.method);
        assertNull(builderMock.entity);
        assertEquals(Arrays.asList("boardId"), webTargetMock.queryParamKeys);
        // assertEquals(Arrays.asList(), webTargetMock.queryParamValues);
    }

    @Test
    void testGetCard() {
        serverUtils.getCard(1);
        assertEquals("http://domain.test:12", client.url);
        assertEquals("api/card/1", webTargetMock.path);
        assertEquals("GET", builderMock.method);
        assertNull(builderMock.entity);
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testAddCard() {
        CardList list = new CardList("title");
        list.id = 2;
        Card card = new Card("title");
        card.id = 3;
        serverUtils.addCard(list, card);
        assertEquals("http://domain.test:12", client.url);
        assertEquals("api/list/2", webTargetMock.path);
        assertEquals("POST", builderMock.method);
        assertEquals(card, builderMock.entity.getEntity());
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testEditCard() {
        Card card = new Card("title");
        serverUtils.editCard(card);
        assertEquals("http://domain.test:12", client.url);
        assertEquals("api/card", webTargetMock.path);
        assertEquals("PUT", builderMock.method);
        assertEquals(card, builderMock.entity.getEntity());
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testDeleteCard() {
        Board board = new Board("title");
        board.id = 1;
        CardList list = new CardList("title");
        list.id = 2;
        Card card = new Card("title");
        card.id = 3;
        serverUtils.deleteCard(card, list, board);
        assertEquals("http://domain.test:12", client.url);
        assertEquals("api/card/3", webTargetMock.path);
        assertEquals("DELETE", builderMock.method);
        assertNull(builderMock.entity);
        assertEquals(Arrays.asList("boardId", "listId"), webTargetMock.queryParamKeys);
        // assertEquals(Arrays.asList(), webTargetMock.queryParamValues);
    }

    @Test
    void testConnect() {
        serverUtils.connected = false;

        serverUtils.connect("http://domain.test:1234");
        assertEquals("http://domain.test:12", client.url);
        assertEquals("api/card/3", webTargetMock.path);
        assertEquals("DELETE", builderMock.method);
        assertNull(builderMock.entity);
        assertEquals(Arrays.asList("boardId", "listId"), webTargetMock.queryParamKeys);
    }

}
