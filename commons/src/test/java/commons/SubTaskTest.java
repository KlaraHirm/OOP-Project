package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubTaskTest {

    SubTask subTask;

    @BeforeEach
    public void setUp()
    {
        subTask = new SubTask("Test SubTask", true);
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
        SubTask subTask2 = new SubTask("Test SubTask", true);
        subTask2.id = 7L;
        assertEquals(subTask, subTask2);
        assertEquals(subTask.hashCode(), subTask2.hashCode());
    }

    /**
     * Test that the SubTask Equals method returns false and the hash codes differ when different IDs
     */
    @Test
    public void testNotEqualsID()
    {
        SubTask subTask1 = new SubTask("Test SubTask 1", true);
        subTask1.id = 8L;
        assertNotEquals(subTask, subTask1);
        assertNotEquals(subTask.hashCode(), subTask1.hashCode());
    }

    /**
     * Test that the SubTask Equals method returns false and the hashcodes differ when different titles
     */
    @Test
    public void testNotEqualsTitle()
    {
        SubTask subTask3 = new SubTask("Test SubTask 3", true);
        subTask3.id = 7L;
        assertNotEquals(subTask, subTask3);
        assertNotEquals(subTask.hashCode(), subTask3.hashCode());
    }

    /**
     * Test that the SubTask Equals method returns false and the hashcodes differ when different done values
     */
    @Test
    public void testNotEqualsDone()
    {
        SubTask subTask2 = new SubTask("Test SubTask",false);
        subTask2.id = 7L;
        assertNotEquals(subTask, subTask2);
        assertNotEquals(subTask.hashCode(), subTask2.hashCode());
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
