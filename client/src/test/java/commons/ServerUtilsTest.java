package commons;

import client.utils.ServerUtils;
import commons.mocks.BuilderMock;
import commons.mocks.ClientMock;
import commons.mocks.WebTargetMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ServerUtilsTest {

    @Mock
    public ClientMock client;

    @Mock
    public WebTargetMock webTargetMock;

    @Mock
    public BuilderMock builderMock;

    public ServerUtils serverUtils;

    private String serverURL = "http://domain.test:12";

    @BeforeEach
    public void setUp()
    {
        builderMock = new BuilderMock();
        webTargetMock = new WebTargetMock(builderMock);
        client = new ClientMock(webTargetMock);

        serverUtils = new ServerUtils();
        serverUtils.client = client;

        serverUtils.serverURL = serverURL;
        serverUtils.connected = true;
    }

    /**
     *
     */
    @Test
    void testGetBoards() {
        serverUtils.getBoards();
        assertEquals(serverURL, client.url);
        assertEquals("api/board", webTargetMock.path);
        assertEquals("GET", builderMock.method);
        assertNull(builderMock.entity);
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testGetBoard() {
        serverUtils.getBoard(1);
        assertEquals(serverURL, client.url);
        assertEquals("api/board/1", webTargetMock.path);
        assertEquals("GET", builderMock.method);
        assertNull(builderMock.entity);
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testAddBoard() {
        Board board = new Board("title");
        serverUtils.addBoard(board);
        assertEquals(serverURL, client.url);
        assertEquals("api/board", webTargetMock.path);
        assertEquals("POST", builderMock.method);
        assertEquals(board, builderMock.entity.getEntity());
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testEditBoard() {
        Board board = new Board("title");
        serverUtils.editBoard(board);
        assertEquals(serverURL, client.url);
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
        assertEquals(serverURL, client.url);
        assertEquals("api/board/1", webTargetMock.path);
        assertEquals("DELETE", builderMock.method);
        assertNull(builderMock.entity);
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testGetList() {
        serverUtils.getList(1);
        assertEquals(serverURL, client.url);
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
        assertEquals(serverURL, client.url);
        assertEquals("api/board/1", webTargetMock.path);
        assertEquals("POST", builderMock.method);
        assertEquals(list, builderMock.entity.getEntity());
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testEditList() {
        CardList list = new CardList("title");
        serverUtils.editList(list);
        assertEquals(serverURL, client.url);
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
        assertEquals(serverURL, client.url);
        assertEquals("api/list/2", webTargetMock.path);
        assertEquals("DELETE", builderMock.method);
        assertNull(builderMock.entity);
        assertEquals(Arrays.asList("boardId"), webTargetMock.queryParamKeys);
        // assertEquals(Arrays.asList(), webTargetMock.queryParamValues);
    }

    @Test
    void testGetCard() {
        serverUtils.getCard(1);
        assertEquals(serverURL, client.url);
        assertEquals("api/card/1", webTargetMock.path);
        assertEquals("GET", builderMock.method);
        assertNull(builderMock.entity);
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testGetCards() {
        serverUtils.getCards(1);
        assertEquals(serverURL, client.url);
        assertEquals("api/list/1/cards", webTargetMock.path);
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
        assertEquals(serverURL, client.url);
        assertEquals("api/list/2", webTargetMock.path);
        assertEquals("POST", builderMock.method);
        assertEquals(card, builderMock.entity.getEntity());
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testEditCard() {
        Card card = new Card("title");
        serverUtils.editCard(card);
        assertEquals(serverURL, client.url);
        assertEquals("api/card", webTargetMock.path);
        assertEquals("PUT", builderMock.method);
        assertEquals(card, builderMock.entity.getEntity());
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testEditCardPosition() {
        Card card = new Card("title");
        CardList original = new CardList("original");
        CardList target = new CardList("target");
        serverUtils.editCardPosition(card, original, target, 2);
        assertEquals(serverURL, client.url);
        assertEquals("api/list/reorder", webTargetMock.path);
        assertEquals("PUT", builderMock.method);
        assertEquals(card, builderMock.entity.getEntity());
        assertEquals(Arrays.asList("original", "target", "cardId", "cardPlace"), webTargetMock.queryParamKeys);
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
        assertEquals(serverURL, client.url);
        assertEquals("api/card/3", webTargetMock.path);
        assertEquals("DELETE", builderMock.method);
        assertNull(builderMock.entity);
        assertEquals(Arrays.asList("boardId", "listId"), webTargetMock.queryParamKeys);
    }

    @Test
    void testConnect() {
        serverUtils.connected = false;

        serverUtils.connect("http://domain2.test:1234");
        assertEquals("http://domain2.test:1234", client.url);
        assertTrue(serverUtils.isConnected());
    }

    @Test
    void testDisconnect() {
        serverUtils.disconnect();
        assertFalse(serverUtils.isConnected());
    }

    @Test
    void testCheckPassword() {
        serverUtils.checkPassword("password");
        assertEquals(serverURL, client.url);
        assertEquals("api/admin/check", webTargetMock.path);
        assertEquals("GET", builderMock.method);
        assertNull(builderMock.entity);
        assertEquals(Arrays.asList("password"), webTargetMock.queryParamKeys);
    }

    @Test
    void testGetServerURL() {
        assertEquals(serverUtils.getServerURL(), serverURL);
    }

    @Test
    void testGetBoardNotConnected() {
        serverUtils.connected = false;
        assertNull(serverUtils.getBoard(3));
        assertNull(client.url);
    }

    @Test
    void testGetBoardsNotConnected() {
        serverUtils.connected = false;
        assertEquals(new ArrayList<Board>(), serverUtils.getBoards());
        assertNull(client.url);
    }

    @Test
    void testGetTags() {
        Board board = new Board("Board");
        board.id = 1;
        serverUtils.getTags(board);
        assertEquals(serverURL, client.url);
        assertEquals("api/board/tags/1", webTargetMock.path);
        assertEquals("GET", builderMock.method);
        assertNull(builderMock.entity);
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testEditTag() {
        Tag tag = new Tag("Tag");
        serverUtils.editTag(tag);
        assertEquals(serverURL, client.url);
        assertEquals("api/tag", webTargetMock.path);
        assertEquals("PUT", builderMock.method);
        assertEquals(tag, builderMock.entity.getEntity());
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testDeleteTag() {
        Board board = new Board("Board");
        board.id = 1;
        Tag tag = new Tag("Tag");
        tag.id = 1;
        serverUtils.deleteTag(tag, 1);
        assertEquals(serverURL, client.url);
        assertEquals("api/tag/1", webTargetMock.path);
        assertEquals("DELETE", builderMock.method);
        assertNull(builderMock.entity);
        assertEquals(Arrays.asList("boardId"), webTargetMock.queryParamKeys);
    }

    @Test
    void testEditSubtaskPosition() {
        Subtask task = new Subtask("title");
        Subtask task2 = new Subtask("title");
        Card card = new Card("card");
        serverUtils.reorderSubtask(task, card, 2);
        assertEquals(serverURL, client.url);
        assertEquals("api/card/reorder", webTargetMock.path);
        assertEquals("PUT", builderMock.method);
        assertEquals(task, builderMock.entity.getEntity());
        assertEquals(Arrays.asList("cardId", "subtaskId", "subtaskPlace"), webTargetMock.queryParamKeys);
    }

}
