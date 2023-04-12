package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTest
{

    Tag tag;

    @BeforeEach
    public void setUp()
    {
        tag = new Tag("Test Tag");
        tag.id = 1L;
    }

    /**
     * Test that the Tag constructor works as expected
     */
    @Test
    public void testConstructor()
    {
        assertEquals("Test Tag", tag.title);
    }

    /**
     * Test that the Tag Equals and the hashcodes methods works as expected
     */
    @Test
    public void testEqualsHashcode()
    {
        Tag tag2 = new Tag("Test Tag");
        tag2.id = 1L;
        assertEquals(tag, tag2);
        assertEquals(tag.hashCode(), tag2.hashCode());
    }

    /**
     * Test that the Tag Equals method returns false and the hashcodes differ when different IDs
     */
    @Test
    public void testNotEqualsID()
    {
        Tag tag2 = new Tag("Test Tag");
        tag2.id = 8L;
        assertNotEquals(tag, tag2);
        assertNotEquals(tag.hashCode(), tag2.hashCode());
    }

    /**
     * Test that the Tag Equals method returns false and the hashcodes differ when different titles
     */
    @Test
    public void testNotEqualsTitle()
    {
        Tag tag2 = new Tag("Test Tag 2");
        tag2.id = 1L;
        assertNotEquals(tag, tag2);
        assertNotEquals(tag.hashCode(), tag2.hashCode());
    }

    /**
     * Test that the Tag Equals method returns true and the hashcodes are equal even though the place is the same
     */
    @Test
    public void testEqualsDifferentPlace()
    {
        Tag tag2 = new Tag("Test Tag");
        tag2.id = 1L;
        assertEquals(tag, tag2);
        assertEquals(tag.hashCode(), tag2.hashCode());
    }

    /**
     * Test that the Tag toString method works as expected
     */
    @Test
    public void hasToString()
    {
        String actual = tag.toString();
        assertTrue(actual.contains("id=1"));
        assertTrue(actual.contains("title=Test Tag"));
    }
}
