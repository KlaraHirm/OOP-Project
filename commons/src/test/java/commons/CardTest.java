package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest
{

    Card card;

    @BeforeEach
    public void setUp()
    {
        card = new Card("Test Card", 1, "Description", true);
        card.id = 7L;
    }

    /**
     * Test that the Card constructor works as expected
     */
    @Test
    public void testConstructor()
    {
        assertEquals("Test Card", card.title);
    }

    /**
     * Test that the Card Equals and the hashcodes methods works as expected
     */
    @Test
    public void testEqualsHashcode()
    {
        Card card2 = new Card("Test Card", 1, "Description", true);
        card2.id = 7L;
        assertEquals(card, card2);
        assertEquals(card.hashCode(), card2.hashCode());
    }

    /**
     * Test that the Card Equals method returns false and the hashcodes differ when different IDs
     */
    @Test
    public void testNotEqualsID()
    {
        Card card2 = new Card("Test Card", 1, "Description", true);
        card2.id = 8L;
        assertNotEquals(card, card2);
        assertNotEquals(card.hashCode(), card2.hashCode());
    }

    /**
     * Test that the Card Equals method returns false and the hashcodes differ when different titles
     */
    @Test
    public void testNotEqualsTitle()
    {
        Card card2 = new Card("Test Card 2", 1, "Description", true);
        card2.id = 7L;
        assertNotEquals(card, card2);
        assertNotEquals(card.hashCode(), card2.hashCode());
    }

    /**
     * Test that the Card Equals method returns false and the hashcodes differ when different descriptions
     */
    @Test
    public void testNotEqualsDescription()
    {
        Card card2 = new Card("Test Card", 1, "Description 2", true);
        card2.id = 7L;
        assertNotEquals(card, card2);
        assertNotEquals(card.hashCode(), card2.hashCode());
    }

    /**
     * Test that the Card Equals method returns false and the hashcodes differ when different done values
     */
    @Test
    public void testNotEqualsDone()
    {
        Card card2 = new Card("Test Card", 1, "Description", false);
        card2.id = 7L;
        assertNotEquals(card, card2);
        assertNotEquals(card.hashCode(), card2.hashCode());
    }


    /**
     * Test that the Card Equals method returns true and the hashcodes are equal even though the place is the same
     */
    @Test
    public void testEqualsDifferentPlace()
    {
        Card card2 = new Card("Test Card", 3, "Description", true);
        card2.id = 7L;
        assertEquals(card, card2);
        assertEquals(card.hashCode(), card2.hashCode());
    }

    /**
     * Test that the Card toString method works as expected
     */
    @Test
    public void hasToString()
    {
        String actual = card.toString();
        assertTrue(actual.contains("id=7"));
        assertTrue(actual.contains("title=Test Card"));
    }
}
