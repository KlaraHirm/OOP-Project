package server.api;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import server.database.BoardRepository;
import server.database.CardListRepository;
import server.database.CardRepository;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListControllerTest {

    @Mock
    private BoardRepository repoBoard;
    @Mock
    private CardListRepository repoList;
    @Mock
    private CardRepository repoCard;
    private ListController controller;

    @BeforeEach
    void setUp() {
        controller = new ListController(repoBoard, repoList, repoCard);
    }

    /**
     * Test that getList returns the correct CardList when given a valid ID
     */
    @Test
    public void testGetList() {
        CardList list = new CardList("test");
        when(repoList.existsById(1L)).thenReturn(true);
        when(repoList.findById(1L)).thenReturn(Optional.of(list));
        assertEquals(list, controller.getList(1L).getBody());
    }

    /**
     * Test that getList returns 404
     * when CardList is not found or ID is invalid
     */
    @Test
    public void testGetList404()
    {
        assertEquals(ResponseEntity.notFound().build(), controller.getList(-1L));
        assertEquals(ResponseEntity.notFound().build(), controller.getList(1L));
    }

    /**
     * Test that addCard correctly adds the Card to the CardList and returns that Card
     */
    @Test
    public void testAddCardList()
    {
        CardList cardList = new CardList("test");
        cardList.id = 1L;
        Card card = new Card("test");
        card.id = 2L;
        cardList.cards = new ArrayList<>();

        when(repoList.existsById(1L)).thenReturn(true);
        when(repoList.findById(1L)).thenReturn(Optional.of(cardList));
        when(repoList.save(cardList)).thenReturn(cardList);
        assertEquals(card, controller.addCard(card, 1L).getBody());
        verify(repoList, times(1)).save(cardList);
        verify(repoCard, times(1)).save(card);
        assertEquals(1, cardList.cards.size());
    }

    /**
     * Test that addCard returns 404 when given an invalid CardList ID
     * or if CardList with a certain ID doesn't exit
     */
    @Test
    public void testAddCardInvalidListID()
    {
        Card card = new Card("test");
        assertEquals(ResponseEntity.notFound().build(), controller.addCard(card, -1L));
        when(repoList.existsById(1L)).thenReturn(false);
        assertEquals(ResponseEntity.notFound().build(), controller.addCard(card, 1L));
    }

    /**
     * Test that addCard returns 400 when given a null Card or null title
     */
    @Test
    public void testAddCardNull()
    {
        assertEquals(ResponseEntity.badRequest().build(), controller.addCard(null, 1L));
        assertEquals(ResponseEntity.badRequest().build(), controller.addCard(new Card(null), 1L));
    }

    /**
     * Test that deleteCardList correctly deletes
     * the CardList from the repo
     */
    @Test
    public void testDeleteList()
    {
        CardList cardList = new CardList("test");
        Board board = new Board("test");
        board.id = 1L;
        board.cardLists.add(cardList);
        cardList.id = 1L;
        when(repoList.existsById(1L)).thenReturn(true);
        when(repoBoard.existsById(1L)).thenReturn(true);
        when(repoList.findById(1L)).thenReturn(Optional.of(cardList));
        when(repoBoard.findById(1L)).thenReturn(Optional.of(board));
        assertEquals(ResponseEntity.ok(cardList), controller.deleteList(1L, 1L));
        verify(repoList, times(1)).deleteById(1L);
        verify(repoBoard, times(1)).save(board);
    }

    /**
     * Test that deleteList returns 404
     * when given an invalid ID of CardList
     */
    @Test
    public void testDeleteListNonExistent1()
    {
        when(repoList.existsById(1L)).thenReturn(false);
        assertEquals(ResponseEntity.notFound().build(), controller.deleteList(1L, 1L));
    }

    /**
     * Test that deleteList returns 404
     * when given an invalid ID of Board
     */
    @Test
    public void testDeleteListNonExistent2()
    {
        when(repoList.existsById(2L)).thenReturn(true);
        when(repoBoard.existsById(2L)).thenReturn(false);
        assertEquals(ResponseEntity.notFound().build(), controller.deleteList(2L, 2L));
    }

    /**
     * Test that deleteList returns a 404
     * when given negative IDs
     */
    @Test
    public void testDeleteListInvalidID()
    {
        assertEquals(ResponseEntity.notFound().build(), controller.deleteList(1L,-1L));
        assertEquals(ResponseEntity.notFound().build(), controller.deleteList(-1L,1L));
    }

    /**
     * Test that editList correctly changes the title of the CardList,
     * but leaves the cards in the CardLists intact
     */
    @Test
    public void testEditList()
    {
        CardList beforeList = new CardList("test");
        beforeList.id = 1L;
        List<Card> cardsIn = new ArrayList<>(List.of(new Card[]{new Card("test")}));
        beforeList.cards = cardsIn;

        CardList changeList = new CardList("test2");
        changeList.id = 1L;
        changeList.cards = new ArrayList<>();

        CardList afterList = new CardList("test2");
        afterList.id = 1L;
        afterList.cards = cardsIn;

        when(repoList.existsById(1L)).thenReturn(true);
        when(repoList.findById(1L)).thenReturn(Optional.of(beforeList));
        when(repoList.save(afterList)).thenReturn(afterList);

        assertEquals(afterList, controller.editList(changeList).getBody());
        verify(repoList, times(1)).save(afterList);
    }

    /**
     * Test that editList returns 404 when given a CardList
     * with a non-existent ID
     */
    @Test
    public void testEditListNonExistent()
    {
        CardList cardList = new CardList("test");
        cardList.id = 1L;
        when(repoList.existsById(1L)).thenReturn(false);
        assertEquals(ResponseEntity.notFound().build(), controller.editList(cardList));
    }

    /**
     * Test that editList returns 400 when passed null object
     * and when passed CardList with null title
     */
    @Test
    public void testEditListNull()
    {
        assertEquals(ResponseEntity.badRequest().build(), controller.editList(null));
        assertEquals(ResponseEntity.badRequest().build(), controller.editList(new CardList(null)));
    }

}