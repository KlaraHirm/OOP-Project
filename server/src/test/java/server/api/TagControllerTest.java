package server.api;

import commons.Board;
import commons.Card;
import commons.CardList;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import server.api.repository.TestBoardRepository;
import server.api.repository.TestCardRepository;
import server.api.repository.TestTagRepository;
import server.services.TagServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagControllerTest {

    @Mock
    private TestTagRepository tagRepo;

    @Mock
    private TestCardRepository cardRepo;

    @Mock
    private TestBoardRepository repo;

    private TagServiceImpl service;
    private TagController sut;

    @BeforeEach
    public void setup()
    {
        service = new TagServiceImpl(tagRepo, cardRepo, repo);
        sut = new TagController();
        sut.tagService = service;
    }

    /**
     * Test that getTag returns the correct Tag when given a valid ID
     */
    @Test
    public void testGetList() {
        commons.Tag tag = new commons.Tag("test");
        when(tagRepo.existsById(1L)).thenReturn(true);
        when(tagRepo.findById(1L)).thenReturn(Optional.of(tag));
        assertNotNull(sut.getTag(1L));
        assertEquals(ResponseEntity.ok(tag), sut.getTag(1L));
    }

    /**
     * Test that getTag returns 404
     * when Tag is not found or ID is invalid
     */
    @Test
    public void testGetTag404()
    {
        assertEquals(ResponseEntity.notFound().build(), sut.getTag(-1L));
        assertEquals(ResponseEntity.notFound().build(), sut.getTag(1L));
    }

    /**
     * Test that editTag correctly changes the title of the Tag,
     * but leaves the cards in the Tag intact
     */
    @Test
    public void testEditTag()
    {
        Tag beforeTag = new Tag("test");
        beforeTag.id = 1L;

        Tag changeTag = new Tag("test2");
        changeTag.id = 1L;

        Tag afterTag = new Tag("test2");
        afterTag.id = 1L;

        when(tagRepo.existsById(1L)).thenReturn(true);
        Mockito.lenient().when(tagRepo.findById(1L)).thenReturn(Optional.of(beforeTag));
        when(tagRepo.save(afterTag)).thenReturn(afterTag);

        assertEquals(ResponseEntity.ok(afterTag), sut.editTag(changeTag));
        verify(tagRepo, times(1)).save(afterTag);
    }

    /**
     * Test that editTag returns 404 when given a Tag
     * with a non-existent ID
     */
    @Test
    public void testEditTagNonExistent()
    {
        Tag tag = new Tag("test");
        tag.id = 1L;
        when(tagRepo.existsById(1L)).thenReturn(false);
        assertEquals(ResponseEntity.notFound().build(), sut.editTag(tag));
    }

    /**
     * Test that editTag returns 400 when passed null object
     * and when passed Tag with null title
     */
    @Test
    public void testEditTagNull()
    {
        assertEquals(ResponseEntity.badRequest().build(), sut.editTag(null));
        assertEquals(ResponseEntity.notFound().build(), sut.editTag(new Tag(null)));
    }

    /**
     * Test that deleteTagWithId correctly deletes
     * the Tag from the repo
     */
    @Test
    public void testDeleteTag()
    {
        List<Board> boards = new ArrayList<>();
        Tag tag = new Tag("test");
        Board board = new Board("test");
        board.id = 1L;
        board.tags = new ArrayList<>();
        board.tags.add(tag);
        tag.id = 1L;
        Card card = new Card("test");
        card.tags = new ArrayList<>();
        card.tags.add(tag);
        card.id = 1L;
        boards.add(board);
        List<Card> cards = new ArrayList<>();
        cards.add(card);
        when(tagRepo.existsById(1L)).thenReturn(true);
        when(tagRepo.findById(1L)).thenReturn(Optional.of(tag));
        when(repo.findAll()).thenReturn(boards);
        when(cardRepo.findAll()).thenReturn(cards);
        assertEquals(ResponseEntity.ok(tag), sut.deleteTagWithId(1L));
        verify(tagRepo, times(1)).deleteById(1L);
    }

    /**
     * Test that deleteTagWithId returns 404
     * when given an invalid ID of Tag
     */
    @Test
    public void testDeleteTagNonExistent1()
    {
        when(tagRepo.existsById(1L)).thenReturn(false);
        assertEquals(ResponseEntity.notFound().build(), sut.deleteTagWithId(1L));
    }

    /**
     * Test that deleteTagWithId returns a 404
     * when given negative IDs
     */
    @Test
    public void testDeleteListInvalidID()
    {
        assertEquals(ResponseEntity.notFound().build(), sut.deleteTagWithId(-1L));
    }
}
