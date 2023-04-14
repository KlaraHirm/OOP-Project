package server.api;

import commons.Board;
import commons.Card;
import commons.CardList;
import commons.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import server.api.repository.*;
import server.api.util.SimpMessagingTemplateMock;
import server.services.SubtaskServiceImpl;
import server.services.SubtaskServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubtaskControllerTest {

    @Mock
    private TestSubtaskRepository subtaskRepo;

    @Mock
    private TestCardRepository cardRepo;
    
    @Mock
    private TestCardListRepository listRepo;

    @Mock
    private TestBoardRepository boardRepo;

    @Mock
    private SimpMessageSendingOperations messageTemplate;

    private SubtaskServiceImpl service;
    private SubtaskController sut;

    @BeforeEach
    public void setup()
    {
        messageTemplate = new SimpMessagingTemplateMock();
        service = new SubtaskServiceImpl(boardRepo, listRepo, cardRepo, subtaskRepo);
        sut = new SubtaskController();
        sut.subtaskService = service;
        sut.messageTemplate = messageTemplate;
    }

    /**
     * Test that getSubtask returns the correct Subtask when given a valid ID
     */
    @Test
    public void testGetSubtask() {
        Subtask subtask = new Subtask("test");
        when(subtaskRepo.existsById(1L)).thenReturn(true);
        when(subtaskRepo.findById(1L)).thenReturn(Optional.of(subtask));
        assertNotNull(sut.getSubtask(1L));
        assertEquals(ResponseEntity.ok(subtask), sut.getSubtask(1L));
    }

    /**
     * Test that getSubtask returns 400
     * when Subtask ID is invalid
     */
    @Test
    public void testGetSubtask400()
    {
        assertEquals(ResponseEntity.badRequest().build(), sut.getSubtask(-1L));
    }

    /**
     * Test that getSubtask returns 404
     * when Subtask is not found
     */
    @Test
    public void testGetSubtask404() {
        assertEquals(ResponseEntity.notFound().build(), sut.getSubtask(1L));

    }

    /**
     * Test that editSubtask correctly changes the title of the Subtask,
     * but leaves the cards in the Subtask intact
     */
    @Test
    public void testEditSubtask()
    {
        Subtask beforeSubtask = new Subtask("test");
        beforeSubtask.id = 1L;

        Subtask changeSubtask = new Subtask("test2");
        changeSubtask.id = 1L;

        Subtask afterSubtask = new Subtask("test2");
        afterSubtask.id = 1L;

        when(subtaskRepo.existsById(1L)).thenReturn(true);
        Mockito.lenient().when(subtaskRepo.findById(1L)).thenReturn(Optional.of(beforeSubtask));
        when(subtaskRepo.save(afterSubtask)).thenReturn(afterSubtask);

        assertEquals(ResponseEntity.ok(afterSubtask), sut.editSubtask(changeSubtask));
        verify(subtaskRepo, times(1)).save(afterSubtask);
    }

    /**
     * Test that editSubtask returns 404 when given a Subtask
     * with a non-existent ID
     */
    @Test
    public void testEditSubtaskNonExistent()
    {
        Subtask subtask = new Subtask("test");
        subtask.id = 1L;
        assertEquals(ResponseEntity.notFound().build(), sut.editSubtask(subtask));
    }

    /**
     * Test that editSubtask returns 400 when passed null object
     * and when passed Subtask with null title
     */
    @Test
    public void testEditSubtaskNull()
    {
        assertEquals(ResponseEntity.badRequest().build(), sut.editSubtask(null));
        assertEquals(ResponseEntity.badRequest().build(), sut.editSubtask(new Subtask(null)));
    }

    /**
     * Test that deleteSubtaskWithId correctly deletes
     * the Subtask from the repo
     */
    @Test
    public void testDeleteSubtask()
    {
        List<Board> boards = new ArrayList<>();
        CardList list = new CardList("test");
        list.id = 1L;
        Board board = new Board("test");
        board.id = 2L;
        Card card = new Card("test");
        card.id = 3L;
        Subtask subtask = new Subtask("test");
        subtask.id = 4L;
        board.cardLists = new ArrayList<>();
        board.cardLists.add(list);
        list.cards = new ArrayList<>();
        list.cards.add(card);
        card.subtasks = new ArrayList<>();
        card.subtasks.add(subtask);

        boards.add(board);

        when(subtaskRepo.existsById(4L)).thenReturn(true);
        when(subtaskRepo.findById(4L)).thenReturn(Optional.of(subtask));
        when(cardRepo.existsById(3L)).thenReturn(true);
        when(cardRepo.findById(3L)).thenReturn(Optional.of(card));
        when(listRepo.existsById(2L)).thenReturn(true);
        when(listRepo.findById(2L)).thenReturn(Optional.of(list));
        when(boardRepo.existsById(1L)).thenReturn(true);
        when(boardRepo.findById(1L)).thenReturn(Optional.of(board));
        Mockito.lenient().when(boardRepo.findAll()).thenReturn(boards);
        assertEquals(ResponseEntity.ok(subtask), sut.deleteSubtask(1L,2L, 3L, 4L));
        verify(subtaskRepo, times(1)).deleteById(4L);
    }

    /**
     * Test that deleteSubtaskWithId returns 404
     * when given an invalid ID of Subtask
     */
    @Test
    public void testDeleteSubtaskNonExistent1()
    {
        when(subtaskRepo.existsById(1L)).thenReturn(false);
        assertEquals(ResponseEntity.notFound().build(), sut.deleteSubtask(1L, 1L, 1L, 1L));
    }

    /**
     * Test that deleteSubtaskWithId returns a 404
     * when given negative IDs
     */
    @Test
    public void testDeleteListInvalidID()
    {
        assertEquals(ResponseEntity.badRequest().build(), sut.deleteSubtask(-1L, 1L, 1L, 1L));
    }
}
