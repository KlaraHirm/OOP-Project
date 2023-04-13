package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubtaskTest {

    Subtask subTask;

    @BeforeEach
    public void setUp()
    {
        subTask = new Subtask("Test SubTask", true, 1);
        subTask.id = 7L;
    }

    /**
     * Test that the SubTask constructor works as expected
     */
    @Test
    public void testConstructor()
    {
        assertEquals("Test SubTask", subTask.title);
    }

    /**
     * Test that the SubTask Equals and the hash codes methods works as expected
     */
    @Test
    public void testEqualsHashcode()
    {
        Subtask subtask2 = new Subtask("Test SubTask", true, 1);
        subtask2.id = 7L;
        assertEquals(subTask, subtask2);
        assertEquals(subTask.hashCode(), subtask2.hashCode());
    }

    /**
     * Test that the SubTask Equals method returns false and the hash codes differ when different IDs
     */
    @Test
    public void testNotEqualsID()
    {
        Subtask subtask1 = new Subtask("Test SubTask 1", true, 1);
        subtask1.id = 8L;
        assertNotEquals(subTask, subtask1);
        assertNotEquals(subTask.hashCode(), subtask1.hashCode());
    }

    /**
     * Test that the SubTask Equals method returns false and the hashcodes differ when different titles
     */
    @Test
    public void testNotEqualsTitle()
    {
        Subtask subtask3 = new Subtask("Test SubTask 3", true, 1);
        subtask3.id = 7L;
        assertNotEquals(subTask, subtask3);
        assertNotEquals(subTask.hashCode(), subtask3.hashCode());
    }

    /**
     * Test that the SubTask Equals method returns false and the hashcodes differ when different done values
     */
    @Test
    public void testNotEqualsDone()
    {
        Subtask subtask2 = new Subtask("Test SubTask",false,1);
        subtask2.id = 7L;
        assertNotEquals(subTask, subtask2);
        assertNotEquals(subTask.hashCode(), subtask2.hashCode());
    }

    /**
     * Test that the SubTask Equals method returns false and the hashcodes differ when different place values
     */
    @Test
    public void testNotEqualsPlace()
    {
        Subtask subtask2 = new Subtask("Test SubTask",false,2);
        subtask2.id = 7L;
        assertNotEquals(subTask, subtask2);
        assertNotEquals(subTask.hashCode(), subtask2.hashCode());
    }

    /**
     * Test that the SubTask toString method works as expected
     */
    @Test
    public void hasToString()
    {
        String actual = subTask.toString();
        assertTrue(actual.contains("id=7"));
        assertTrue(actual.contains("title=Test SubTask"));
    }
}
