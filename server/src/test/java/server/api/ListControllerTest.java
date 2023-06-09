package server.api;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import server.api.repository.TestBoardRepository;
import server.api.repository.TestCardListRepository;
import server.api.repository.TestCardRepository;
import server.api.util.SimpMessagingTemplateMock;
import server.services.CardListServiceImpl;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListControllerTest {

    @Mock
    private TestBoardRepository repo;

    @Mock
    private TestCardListRepository listRepo;

    @Mock
    private TestCardRepository cardRepo;

    private CardListServiceImpl service;
    private ListController sut;

    @Mock
    private SimpMessageSendingOperations messageTemplate;

    @BeforeEach
    public void setup()
    {
        messageTemplate = new SimpMessagingTemplateMock();
        service = new CardListServiceImpl(repo, listRepo, cardRepo);
        sut = new ListController();
        sut.listService = service;
        sut.messageTemplate = messageTemplate;
    }


    /**
     * Test that getList returns the correct CardList when given a valid ID
     */
    @Test
    public void testGetList() {
        CardList list = new CardList("test");
        when(listRepo.existsById(1L)).thenReturn(true);
        when(listRepo.findById(1L)).thenReturn(Optional.of(list));
        assertEquals(ResponseEntity.ok(list), sut.getList(1L));
    }

    /**
     * Test that getList returns 404
     * when CardList is not found or ID is invalid
     */
    @Test
    public void testGetList404()
    {
        assertEquals(ResponseEntity.notFound().build(), sut.getList(-1L));
        assertEquals(ResponseEntity.notFound().build(), sut.getList(1L));
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

        when(listRepo.existsById(1L)).thenReturn(true);
        when(listRepo.findById(1L)).thenReturn(Optional.of(cardList));
        when(listRepo.save(cardList)).thenReturn(cardList);
        when(cardRepo.save(card)).thenReturn(card);
        assertEquals(ResponseEntity.ok(card), sut.addCard(card, 1L));
        verify(listRepo, times(1)).save(cardList);
        verify(cardRepo, times(1)).save(card);
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
        assertEquals(ResponseEntity.notFound().build(), sut.addCard(card, -1L));
        when(listRepo.existsById(1L)).thenReturn(false);
        assertEquals(ResponseEntity.notFound().build(), sut.addCard(card, 1L));
        assertEquals(ResponseEntity.badRequest().build(), sut.addCard(null, 1L));
    }

    /**
     * Test that addCard returns 400 when given a null Card or null title
     */
    @Test
    public void testAddCardNull()
    {
        assertEquals(ResponseEntity.badRequest().build(), sut.addCard(null, 1L));
        assertEquals(ResponseEntity.badRequest().build(), sut.addCard(new Card(null), 1L));
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
        when(listRepo.existsById(1L)).thenReturn(true);
        when(repo.existsById(1L)).thenReturn(true);
        when(listRepo.findById(1L)).thenReturn(Optional.of(cardList));
        when(repo.findById(1L)).thenReturn(Optional.of(board));
        assertEquals(ResponseEntity.ok(cardList), sut.deleteList(1L, 1L));
        verify(listRepo, times(1)).deleteById(1L);
        verify(repo, times(1)).save(board);
    }

    /**
     * Test that deleteList returns 404
     * when given an invalid ID of CardList
     */
    @Test
    public void testDeleteListNonExistent1()
    {
        when(listRepo.existsById(1L)).thenReturn(false);
        assertEquals(ResponseEntity.notFound().build(), sut.deleteList(1L, 1L));
    }

    /**
     * Test that deleteList returns 404
     * when given an invalid ID of Board
     */
    @Test
    public void testDeleteListNonExistent2()
    {
        when(listRepo.existsById(2L)).thenReturn(true);
        when(repo.existsById(2L)).thenReturn(false);
        assertEquals(ResponseEntity.notFound().build(), sut.deleteList(2L, 2L));
    }

    /**
     * Test that deleteList returns a 404
     * when given negative IDs
     */
    @Test
    public void testDeleteListInvalidID()
    {
        assertEquals(ResponseEntity.notFound().build(), sut.deleteList(1L, -1L));
        assertEquals(ResponseEntity.notFound().build(), sut.deleteList(-1L, 1L));
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

        when(listRepo.existsById(1L)).thenReturn(true);
        when(listRepo.findById(1L)).thenReturn(Optional.of(beforeList));
        when(listRepo.save(afterList)).thenReturn(afterList);

        assertEquals(ResponseEntity.ok(afterList), sut.editList(changeList));
        verify(listRepo, times(1)).save(afterList);
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
        when(listRepo.existsById(1L)).thenReturn(false);
        assertEquals(ResponseEntity.notFound().build(), sut.editList(cardList));
    }

    /**
     * Test that editList returns 400 when passed null object
     * and when passed CardList with null title
     */
    @Test
    public void testEditListNull()
    {
        assertEquals(ResponseEntity.badRequest().build(), sut.editList(null));
        assertEquals(ResponseEntity.badRequest().build(), sut.editList(new CardList(null)));
    }

    @Test
    public void testGetCards() {
        Card card1 = new Card("Card1");
        card1.place = 1;
        Card card2 = new Card("Card2");
        card2.place = 2;
        CardList list = new CardList("List");
        list.id = 1L;
        List<Card> listCards = new ArrayList<>();
        listCards.add(card1);
        listCards.add(card2);
        list.cards.add(card1);
        list.cards.add(card2);
        when(listRepo.existsById(1L)).thenReturn(true);
        when(listRepo.findById(1L)).thenReturn(Optional.of(list));
        assertEquals(ResponseEntity.ok(listCards), sut.getCards(1L));

    }

    @Test
    public void testGetCards404() {
        assertEquals(ResponseEntity.notFound().build(), sut.getCards(1L));
    }

    @Test
    public void testReorder() {
        Card card1 = new Card("Card1");
        card1.id = 1L;
        card1.place = 1;
        when(cardRepo.existsById(1L)).thenReturn(true);
        when(cardRepo.findById(1L)).thenReturn(Optional.of(card1));
        CardList cardList = new CardList("CL1");
        cardList.id = 1L;
        when(listRepo.existsById(1L)).thenReturn(true);
        when(listRepo.findById(1L)).thenReturn(Optional.of(cardList));
        CardList cardList1 = new CardList("CL2");
        cardList1.id = 2L;
        Mockito.lenient().when(listRepo.existsById(2L)).thenReturn(true);
        when(listRepo.findById(2L)).thenReturn(Optional.of(cardList1));
        cardList.cards.add(card1);
        assertTrue(Objects.requireNonNull(sut.reorder(1L, 2L, 1L, 1).getBody()).cards.contains(card1));
        assertFalse(Objects.requireNonNull(sut.getList(1L).getBody()).cards.contains(card1));
    }

    @Test
    public void testReorder404() {
        Card card1 = new Card("Card1");
        card1.id = 1L;
        card1.place = 1;
        when(cardRepo.existsById(1L)).thenReturn(true);
        when(cardRepo.findById(1L)).thenReturn(Optional.of(card1));
        CardList cardList = new CardList("CL1");
        cardList.id = 1L;
        when(listRepo.existsById(1L)).thenReturn(true);
        when(listRepo.findById(1L)).thenReturn(Optional.of(cardList));
        CardList cardList1 = new CardList("CL2");
        cardList1.id = 2L;
        Mockito.lenient().when(listRepo.existsById(2L)).thenReturn(true);
        when(listRepo.findById(2L)).thenReturn(Optional.of(cardList1));
        assertEquals(ResponseEntity.notFound().build(), sut.reorder(1L, 2L, 1L, 1));
    }

    @Test
    public void testReorder400() {
        assertEquals(ResponseEntity.badRequest().build(), sut.reorder(-1L, 2L, 1L, 1));
        assertEquals(ResponseEntity.badRequest().build(), sut.reorder(1L, -2L, 1L, 1));
        assertEquals(ResponseEntity.badRequest().build(), sut.reorder(1L, 2L, -1L, 1));
        assertEquals(ResponseEntity.badRequest().build(), sut.reorder(1L, 2L, 1L, -1));
    }

}