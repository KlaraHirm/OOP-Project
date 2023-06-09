package server.api;

import commons.Board;
import commons.CardList;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import server.api.repository.TestBoardRepository;
import server.api.repository.TestCardListRepository;
import server.api.repository.TestTagRepository;
import server.api.util.SimpMessagingTemplateMock;
import server.services.BoardServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardControllerTest
{

    @Mock
    private TestBoardRepository repo;

    @Mock
    private TestCardListRepository listRepo;

    @Mock
    private TestTagRepository tagRepo;

    @Mock
    private BoardServiceImpl service;
    private BoardController sut;
    @Mock
    private SimpMessageSendingOperations messageTemplate;

    @BeforeEach
    public void setup()
    {
        messageTemplate = new SimpMessagingTemplateMock();
        service = new BoardServiceImpl(repo, listRepo, tagRepo);
        sut = new BoardController();
        sut.boardService = service;
        sut.messageTemplate = messageTemplate;
    }

    /**
     * Test that getAll returns all boards in the repo
     */
    @Test
    public void testGetAll()
    {
        List<Board> boards = new ArrayList<>(List.of(new Board[]{new Board("test1"), new Board("test2")}));
        when(repo.findAll()).thenReturn(boards);
        assertEquals(ResponseEntity.ok(boards), sut.getAll());

    }
    /**
     * Test that getAll returns an empty list when the repo is empty
     */
    @Test
    public void testGetAllEmpty()
    {
        List<Board> boards = new ArrayList<>();
        when(repo.findAll()).thenReturn(boards);
        assertEquals(ResponseEntity.ok(boards), sut.getAll());
    }

    /**
     * Test that getBoard returns the correct board when given a valid id
     */
    @Test
    public void testGetBoard()
    {
        Board board = new Board("test");
        when(repo.existsById(1L)).thenReturn(true);
        when(repo.findById(1L)).thenReturn(Optional.of(board));
        assertEquals(ResponseEntity.ok(board), sut.getBoard(1L));
    }

    /**
     * Test that getBoard returns a bad request when given an invalid id
     */
    @Test
    public void testGetBoardInvalidId()
    {
        assertEquals(ResponseEntity.notFound().build(), sut.getBoard(-1L));
    }

    /**
     * Test that getBoard returns a bad request when given an id referencing a non-existent board
     */
    @Test
    public void testGetBoardNonExistentId()
    {
        when(repo.existsById(any())).thenReturn(false);
        assertEquals(ResponseEntity.notFound().build(), sut.getBoard(1L));
    }

    /**
     * Test that addBoard saves the board in the repo and returns the correct board
     */
    @Test
    public void testAddBoard()
    {
        Board board = new Board("test");
        when(repo.save(board)).thenReturn(board);
        assertEquals(ResponseEntity.ok(board), sut.addBoard(board));
        verify(repo, times(1)).save(board);
    }

    @Test
    public void testAddBoardCreatesPlaces()
    {
        Board board = new Board("test");
        board.cardLists = new ArrayList<>(List.of(new CardList[]{
                new CardList("list1"), new CardList("list2")}));
        when(repo.save(board)).thenReturn(board);
        Board returned = sut.addBoard(board).getBody();
        assertNotNull(returned);
        assertEquals(2, returned.cardLists.size());
        for(int i = 0; i < 2; i++)
        {
            assertEquals(i, returned.cardLists.get(i).place);
        }
    }

    /**
     * Test that addBoard creates a new cardLists list when the board's cardLists list is null
     */
    @Test
    public void testAddBoardNullList()
    {
        Board board = new Board("test");
        board.cardLists = null;
        when(repo.save(board)).thenReturn(board);
        assertEquals(ResponseEntity.ok(board), sut.addBoard(board));
        assertNotNull(board.cardLists);
        verify(repo, times(1)).save(board);
    }

    /**
     * Test that addBoard returns a bad request and doesn't attempt to save to the repo when title = null
     */
    @Test
    public void testAddBoardNullTitle()
    {
        Board board = new Board(null);
        assertEquals(ResponseEntity.badRequest().build(), sut.addBoard(board));
    }

    /**
     * Test that editBoard correctly changes the title of the board, but leaves the cardLists list intact
     */
    @Test
    public void testEditBoard()
    {
        Board beforeBoard = new Board("test");
        beforeBoard.id = 1L;
        List<CardList> cardLists = new ArrayList<>(List.of(new CardList[]{new CardList("test")}));
        beforeBoard.cardLists = cardLists;

        Board changeBoard = new Board("test2");
        changeBoard.id = 1L;
        changeBoard.cardLists = new ArrayList<>();

        Board afterBoard = new Board("test2");
        afterBoard.id = 1L;
        afterBoard.cardLists = cardLists;

        when(repo.existsById(1L)).thenReturn(true);
        when(repo.findById(1L)).thenReturn(Optional.of(beforeBoard));
        when(repo.save(afterBoard)).thenReturn(afterBoard);

        assertEquals(ResponseEntity.ok(afterBoard), sut.editBoard(changeBoard));

        verify(repo, times(1)).save(afterBoard);
    }

    /**
     * Test that editBoard returns a not found request when given a board object with a non-existent id, and doesn't attempt to modify the repo
     */
    @Test
    public void testEditBoardNonExistent()
    {
        Board board = new Board("test");
        board.id = 1L;
        when(repo.existsById(1L)).thenReturn(false);
        assertEquals(ResponseEntity.notFound().build(), sut.editBoard(board));
    }

    /**
     * Test that editBoard returns a bad request when passed null, and doesn't attempt to modify the repo
     */
    @Test
    public void testEditBoardNull()
    {
        assertEquals(ResponseEntity.badRequest().build(), sut.editBoard(null));
    }

    /**
     * Test that deleteBoard correctly deletes the board from the repo
     */
    @Test
    public void testDeleteBoard()
    {
        Board board = new Board("test");
        board.id = 1L;
        when(repo.existsById(1L)).thenReturn(true);
        when(repo.findById(1L)).thenReturn(Optional.of(board));
        doNothing().when(repo).deleteById(1L);
        assertEquals(ResponseEntity.ok(board), sut.deleteBoardWithID(1L));
        verify(repo, times(1)).deleteById(1L);
    }

    /**
     * Test that deleteBoard returns a bad request when given an invalid id, and doesn't attempt to modify the repo
     */
    @Test
    public void testDeleteBoardNonExistent()
    {
        when(repo.existsById(1L)).thenReturn(false);
        assertEquals(ResponseEntity.badRequest().build(), sut.deleteBoardWithID(1L));
    }

    /**
     * Test that deleteBoard returns a bad request when given an invalid id, and doesn't attempt to modify the repo
     */
    @Test
    public void testDeleteBoardInvalidID()
    {
        assertEquals(ResponseEntity.badRequest().build(), sut.deleteBoardWithID(-1L));
    }

    /**
     * Test that addCardList correctly adds the cardList to the board, and returns the that cardList
     */
    @Test
    public void testAddCardList()
    {
        Board board = new Board("test");
        board.id = 1L;
        CardList cardList = new CardList("test");
        cardList.id = 1L;

        board.cardLists = new ArrayList<>();

        when(repo.existsById(1L)).thenReturn(true);
        when(repo.findById(1L)).thenReturn(Optional.of(board));
        when(repo.save(board)).thenReturn(board);
        when(listRepo.save(cardList)).thenReturn(cardList);
        assertEquals(ResponseEntity.ok(cardList), sut.addCardList(cardList, 1L));
        verify(repo, times(1)).save(board);
        assertEquals(1, board.cardLists.size());
    }

    /**
     * Test that addCardlist succesfully injects an empty list of cards into the cardList when it is null
     */
    @Test
    public void testAddCardListNullList()
    {
        Board board = new Board("test");
        board.id = 1L;
        CardList cardList = new CardList("test");
        cardList.id = 1L;
        cardList.cards = null;

        when(repo.existsById(1L)).thenReturn(true);
        when(repo.findById(1L)).thenReturn(Optional.of(board));
        when(repo.save(board)).thenReturn(board);
        when(listRepo.save(cardList)).thenReturn(cardList);
        assertEquals(ResponseEntity.ok(cardList), sut.addCardList(cardList, 1L));
        assertNotNull(cardList.cards);
        verify(repo, times(1)).save(board);
    }

    /**
     * Test that addCardList returns a not found error when given an invalid board id, and doesn't attempt to modify the repo
     */
    @Test
    public void testAddCardListInvalidBoardID()
    {
        CardList cardList = new CardList("test");
        assertEquals(ResponseEntity.notFound().build(), sut.addCardList(cardList, -1L));
    }

    /**
     * Test that addCardList returns a not found error when given a non-existent board id, and doesn't attempt to modify the repo
     */
    @Test
    public void testAddCardListNonExistentBoardID()
    {
        CardList cardList = new CardList("test");
        when(repo.existsById(1L)).thenReturn(false);
        assertEquals(ResponseEntity.notFound().build(), sut.addCardList(cardList, 1L));
    }

    /**
     * Test that addCardList returns a bad request when given a null cardList, and doesn't attempt to modify the repo
     */
    @Test
    public void testAddCardListNull()
    {
        assertEquals(ResponseEntity.badRequest().build(), sut.addCardList(null, 1L));
    }

    /**
     * Test that addCardList returns a bad request when title is null, and doesn't attempt to modify the database
     */
    @Test
    public void testAddCardListNullTitle()
    {
        CardList cardList = new CardList(null);
        assertEquals(ResponseEntity.badRequest().build(), sut.addCardList(cardList, 1L));
    }

    /**
     * Test that reorderCardLists correctly places the cardlist at the index specified,
     * and fixes all place values on the cardlists
     */
    @Test
    public void testReorderCardLists()
    {
        Board board = new Board("test");
        board.id = 1L;
        CardList cardList1 = new CardList("test1", 0);
        cardList1.id = 1L;
        CardList cardList2 = new CardList("test2", 1);
        cardList2.id = 2L;
        CardList cardList3 = new CardList("test3", 2);
        cardList3.id = 3L;
        board.cardLists = new ArrayList<>(List.of(new CardList[]{cardList1, cardList2, cardList3}));

        when(repo.existsById(1L)).thenReturn(true);
        when(repo.findById(1L)).thenReturn(Optional.of(board));
        when(repo.save(board)).thenReturn(board);
        when(listRepo.existsById(3L)).thenReturn(true);
        when(listRepo.findById(3L)).thenReturn(Optional.of(cardList3));

        assertEquals(ResponseEntity.ok(board), sut.reorderCardLists(1L, 3L, 0));
        assertEquals(3, board.cardLists.size());
        assertEquals(3L, board.cardLists.get(0).id);
        assertEquals(1L, board.cardLists.get(1).id);
        assertEquals(2L, board.cardLists.get(2).id);
        assertEquals(0, cardList3.place);
        assertEquals(1, cardList1.place);
        assertEquals(2, cardList2.place);
        verify(repo, times(1)).save(board);
    }

    /**
     * Test that getAllTags returns all Tag in the repo
     */
    @Test
    public void testGetAllTags()
    {
        Board board = new Board("test");
        List<Tag> tags = new ArrayList<>(List.of(new Tag[]{new Tag("test1"), new Tag("test2")}));
        board.tags = new ArrayList<>(List.of(new Tag[]{new Tag("test1"), new Tag("test2")}));
        board.id = 1L;
        when(repo.findById(1L)).thenReturn(Optional.of(board));
        assertEquals(ResponseEntity.ok(tags), sut.getAllTags(1L));
    }
}
