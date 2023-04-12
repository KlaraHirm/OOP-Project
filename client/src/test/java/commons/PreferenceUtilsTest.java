package commons;

import client.utils.PreferenceUtils;
import client.utils.ServerUtils;
import commons.mocks.BuilderMock;
import commons.mocks.ClientMock;
import commons.mocks.PreferencesMock;
import commons.mocks.WebTargetMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class PreferenceUtilsTest {

    private PreferenceUtils preferences;

    @Mock
    private PreferencesMock preferencesMock;

    @BeforeEach
    public void setUp()
    {
        preferencesMock = new PreferencesMock();
        preferences = new PreferenceUtils();
        preferences.preferences = preferencesMock;
    }

    @Test
    void testSaveBoardId() {
        Board board2 = new Board("title");
        board2.id = 2;
        Board board3 = new Board("title");
        board3.id = 3;
        preferences.saveBoardId("server", board2);
        preferences.saveBoardId("server", board3);
        assertEquals("2,3", preferencesMock.get("server", ""));
    }

    @Test
    void testRemoveBoardId() {
        Board board = new Board("title");
        board.id = 3;
        preferencesMock.put("server", "2,3,4");
        preferences.removeBoardId("server", board);
        assertEquals("2,4", preferencesMock.get("server", ""));
    }

    @Test
    void testGetJoinedBoardIds() {
        preferencesMock.put("server", "2,3,4");
        assertEquals(Arrays.asList("2", "3", "4"), preferences.getJoinedBoardIds("server"));
    }
}
