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
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

    @Test
    void testAddBoard() {
        serverUtils.addBoard(new Board("title"));
        assertEquals("http://domain.test:12", client.url);
        assertEquals("api/board", webTargetMock.path);
        assertEquals("POST", builderMock.method);
        assertEquals(Arrays.asList(), webTargetMock.queryParamKeys);
    }

}
