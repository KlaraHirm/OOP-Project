package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardListTest
{

    CardList cardList;

    @BeforeEach
    public void setUp()
    {
        cardList = new CardList("Test CardList", 1);
        cardList.cards = new ArrayList<>();
        cardList.id = 7L;
    }

    /**
     * Test that the CardList constructor works as expected
     */
    @Test
    public void testConstructor()
    {
        assertEquals("Test CardList", cardList.title);
        assertEquals(0, cardList.cards.size());
    }

    /**
     * Test that the CardList Equals and the hashcodes methods works as expected
     */
    @Test
    public void testEqualsHashcode()
    {
        CardList cardList2 = new CardList("Test CardList", 1);
        cardList2.cards = new ArrayList<>();
        cardList2.id = 7L;
        assertEquals(cardList, cardList2);
        assertEquals(cardList.hashCode(), cardList2.hashCode());
    }

    /**
     * Test that the CardList Equals method returns false and the hashcodes differ when different IDs
     */
    @Test
    public void testNotEqualsID()
    {
        CardList cardList2 = new CardList("Test CardList", 1);
        cardList2.cards = new ArrayList<>();
        cardList2.id = 8L;
        assertNotEquals(cardList, cardList2);
        assertNotEquals(cardList.hashCode(), cardList2.hashCode());
    }

    /**
     * Test that the CardList Equals method returns false and the hashcodes differ when different titles
     */
    @Test
    public void testNotEqualsTitle()
    {
        CardList cardList2 = new CardList("Test CardList 2", 1);
        cardList2.cards = new ArrayList<>();
        cardList2.id = 7L;
        assertNotEquals(cardList, cardList2);
        assertNotEquals(cardList.hashCode(), cardList2.hashCode());
    }

    /**
     * Test that the CardList Equals method returns false and the hashcodes differ when different cards
     */
    @Test
    public void testNotEqualsCards()
    {
        CardList cardList2 = new CardList("Test CardList", 1);
        cardList2.cards = new ArrayList<>();
        cardList2.cards.add(new Card("Test Card"));
        cardList2.id = 7L;
        assertNotEquals(cardList, cardList2);
        assertNotEquals(cardList.hashCode(), cardList2.hashCode());
    }

    /**
     * Test that the CardList Equals method still returns true and the hashcodes are the same when different places
     */
    @Test
    public void testEqualsPlace()
    {
        CardList cardList2 = new CardList("Test CardList", 2);
        cardList2.cards = new ArrayList<>();
        cardList2.id = 7L;
        assertEquals(cardList, cardList2);
        assertEquals(cardList.hashCode(), cardList2.hashCode());
    }

    /**
     * Test that the CardList toString method works as expected
     */
   @Test
   public void hasToString()
   {
           String actual = cardList.toString();
           assertTrue(actual.contains("id=7"));
           assertTrue(actual.contains("title=Test CardList"));
           assertTrue(actual.contains("place=1"));
           assertTrue(actual.contains("cards=[]"));
   }
}