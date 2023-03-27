package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest
{

    Board board;

    @BeforeEach
    public void setUp()
    {
        board = new Board("Test Board");
        board.cardLists = new ArrayList<>();
        board.id = 7L;
    }

    /**
     * Test that the Board constructor works as expected
     */
    @Test
    public void testConstructor()
    {
        assertEquals("Test Board", board.title);
        assertEquals(0, board.cardLists.size());
    }

    /**
     * Test that the Board Equals and the hashcodes methods works as expected
     */
    @Test
    public void testEqualsHashcode()
    {
        Board board2 = new Board("Test Board");
        board2.cardLists = new ArrayList<>();
        board2.id = 7L;
        assertEquals(board, board2);
        assertEquals(board.hashCode(), board2.hashCode());
    }

    /**
     * Test that the Board Equals method returns false and the hashcodes differ when different IDs
     */
    @Test
    public void testNotEqualsID()
    {
        Board board2 = new Board("Test Board");
        board2.cardLists = new ArrayList<>();
        board2.id = 8L;
        assertNotEquals(board, board2);
        assertNotEquals(board.hashCode(), board2.hashCode());
    }

    /**
     * Test that the Board Equals method returns false wand the hashcodes differ hen different titles
     */
    @Test
    public void testNotEqualsTitle()
    {
        Board board2 = new Board("Test Board 2");
        board2.cardLists = new ArrayList<>();
        board2.id = 7L;
        assertNotEquals(board, board2);
        assertNotEquals(board.hashCode(), board2.hashCode());
    }

    /**
     * Test that the Board Equals method returns false and the hashcodes differ when different card lists
     */
    @Test
    public void testNotEqualsCardLists()
    {
        Board board2 = new Board("Test Board");
        board2.cardLists = new ArrayList<>(List.of(new CardList("Test List 1"), new CardList("Test List 2")));
        board2.id = 7L;
        assertNotEquals(board, board2);
        assertNotEquals(board.hashCode(), board2.hashCode());
    }

    /**
     * Test that the Board toString method works as expected
     */
    @Test
    public void hasToString()
    {
        String actual = board.toString();
        assertTrue(actual.contains("id=7"));
        assertTrue(actual.contains("title=Test Board"));
        assertTrue(actual.contains("cardLists=[]"));
    }
}