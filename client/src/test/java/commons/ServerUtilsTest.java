package commons;

import client.utils.ServerUtils;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Mockito.when;

public class ServerUtilsTest {

    @Mock
    public Client client;
    public ServerUtils serverUtils;

    @BeforeEach
    public void setUp()
    {
        client = ClientBuilder.newClient(new ClientConfig());
        serverUtils = new ServerUtils();
        serverUtils.client = client;

        serverUtils.serverURL = "http://0.0.0.0:8080";
        serverUtils.connected = true;
    }

    /**
     *
     */
    @Test
    void getBoards() {
        List<Board> response = serverUtils.getBoards();
        System.out.println(response);

        // TODO
    }

    @Test
    void addBoard() {
        // TODO
    }

}
