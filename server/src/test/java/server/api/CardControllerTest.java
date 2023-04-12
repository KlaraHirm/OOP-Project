/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import server.api.repository.TestBoardRepository;
import server.api.repository.TestCardListRepository;
import server.api.repository.TestCardRepository;
import server.api.repository.TestTagRepository;
import server.api.util.SimpMessagingTemplateMock;
import server.services.CardServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CardControllerTest {

    @Mock
    private TestCardRepository cardRepo;

    @Mock
    private TestCardListRepository listRepo;

    @Mock
    private TestBoardRepository boardRepo;

    @Mock
    private TestTagRepository tagRepo;

    private CardServiceImpl service;
    private CardController sut;

    @Mock
    private SimpMessageSendingOperations messageTemplate;

    @BeforeEach
    public void setup()
    {
        messageTemplate = new SimpMessagingTemplateMock();
        service = new CardServiceImpl(cardRepo, listRepo, boardRepo, tagRepo);
        sut = new CardController();
        sut.cardService = service;
        sut.messageTemplate = messageTemplate;
    }

    @Test
    public void testGetCard() {
        Card card = new Card("Title");
        when(cardRepo.existsById(1L)).thenReturn(true);
        when(cardRepo.findById(1L)).thenReturn(Optional.of(card));

        assertNotNull(sut.getCard(1L));
        assertEquals("Title", Objects.requireNonNull(sut.getCard(1L).getBody()).title);
    }

    @Test
    public void testGetCardNotFound() {
        assertEquals(ResponseEntity.notFound().build(), sut.getCard(1L));
        assertEquals(ResponseEntity.notFound().build(), sut.getCard(-1L));
    }

    @Test
    public void testPutCard() {
        Card card = new Card("Title");
        card.id = 1L;

        Card card1 = new Card("Title2");
        card1.id = 1L;

        when(cardRepo.existsById(1L)).thenReturn(true);
        Mockito.lenient().when(cardRepo.findById(1L)).thenReturn(Optional.of(card));
        Mockito.lenient().when(cardRepo.save(card1)).thenReturn(card1);

        assertEquals(ResponseEntity.ok(card1), sut.editCard(card1));
        verify(cardRepo, times(1)).save(card1);
    }

    @Test
    public void editCardTest() {
        Card card = new Card("Title");
        card.id = 1L;
        card.tags = new ArrayList<>();

        Tag tag = new Tag("Tag");
        tag.id = 1L;
        tag.cards = new ArrayList<>();

        card.tags.add(tag);
        tag.cards.add(card);

        Card card1 = new Card("Title2");
        card1.id = 1L;

        when(cardRepo.existsById(1L)).thenReturn(true);
        Mockito.lenient().when(cardRepo.findById(1L)).thenReturn(Optional.of(card));
        Mockito.lenient().when(cardRepo.save(card1)).thenReturn(card1);

        assertEquals(ResponseEntity.ok(card1), sut.editCard(card1));
        verify(cardRepo, times(1)).save(card1);
    }

    @Test
    public void editCardBadRequest() {
        assertEquals(ResponseEntity.badRequest().build(), sut.editCard(null));
    }

    @Test
    public void editCardNotFound() {
        assertEquals(ResponseEntity.notFound().build(), sut.editCard(new Card("Title")));
    }

    @Test
    public void testDeleteCard() {
        Card card = new Card("Title");
        card.id = 1L;
        Board board = new Board("Board");
        board.id = 1L;
        CardList list = new CardList("List");
        list.id = 1L;
        list.cards.add(card);
        board.cardLists.add(list);
        Mockito.lenient().when(cardRepo.existsById(1L)).thenReturn(true);
        when(cardRepo.findById(1L)).thenReturn(Optional.of(card));
        when(listRepo.existsById(1L)).thenReturn(true);
        when(listRepo.findById(1L)).thenReturn(Optional.of(list));
        when(boardRepo.findById(1L)).thenReturn(Optional.of(board));
        when(boardRepo.existsById(1L)).thenReturn(true);
        doNothing().when(cardRepo).deleteById(1L);
        assertEquals(ResponseEntity.ok(card), sut.deleteCard(1L,1L,1L));
        verify(cardRepo, times(1)).deleteById(1L);
    }

    @Test
    public void deleteCardBadRequest() {
        assertEquals(ResponseEntity.badRequest().build(), sut.deleteCard(-1L, 1L, 1L));
        assertEquals(ResponseEntity.badRequest().build(), sut.deleteCard(1L, -1L, 1L));
        assertEquals(ResponseEntity.badRequest().build(), sut.deleteCard(1L, 1L, -1L));
    }

    @Test
    public void deleteCardNotFound() {
        assertEquals(ResponseEntity.notFound().build(), sut.deleteCard(1L, 1L, 1L));
    }

    @Test
    public void getTagsTest() {
        Card card = new Card("Card");
        card.id = 1L;
        when(cardRepo.existsById(1L)).thenReturn(true);
        when(cardRepo.findById(1L)).thenReturn(Optional.of(card));
        Tag tag1 = new Tag("Tag 1");
        tag1.id = 1L;
        Mockito.lenient().when(tagRepo.existsById(1L)).thenReturn(true);
        Mockito.lenient().when(tagRepo.findById(1L)).thenReturn(Optional.of(tag1));
        Tag tag2 = new Tag("Tag 2");
        tag2.id = 2L;
        Mockito.lenient().when(tagRepo.existsById(2L)).thenReturn(true);
        Mockito.lenient().when(tagRepo.findById(2L)).thenReturn(Optional.of(tag2));
        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);
        card.tags = tags;
        List<Card> cards = new ArrayList<>();
        cards.add(card);
        tag1.cards = cards;
        tag2.cards = cards;
        assertEquals(ResponseEntity.ok(tags), sut.getTags(1L));
    }

    @Test
    public void getTags400() {
        assertEquals(ResponseEntity.badRequest().build(), sut.getTags(-1L));
    }

    @Test
    public void getTags404() {
        assertEquals(ResponseEntity.notFound().build(), sut.getTags(1L));
    }

    @Test
    public void deleteTagFromCardTest() {
        Card card = new Card("Card");
        card.id = 1L;
        when(cardRepo.existsById(1L)).thenReturn(true);
        when(cardRepo.findById(1L)).thenReturn(Optional.of(card));
        Tag tag1 = new Tag("Tag 1");
        tag1.id = 1L;
        Mockito.lenient().when(tagRepo.existsById(1L)).thenReturn(true);
        Mockito.lenient().when(tagRepo.findById(1L)).thenReturn(Optional.of(tag1));
        Tag tag2 = new Tag("Tag 2");
        tag2.id = 2L;
        Mockito.lenient().when(tagRepo.existsById(2L)).thenReturn(true);
        Mockito.lenient().when(tagRepo.findById(2L)).thenReturn(Optional.of(tag2));
        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);
        card.tags = tags;
        List<Card> cards = new ArrayList<>();
        cards.add(card);
        tag1.cards = cards;
        tag2.cards = cards;
        Card updated =  sut.deleteTagFromCard(1L, tag1).getBody();
        card.tags.remove(tag1);
        assertEquals(card, updated);
        tag1.cards.remove(card);
        assertEquals(tag1, tagRepo.findById(1L).get());
    }

    @Test
    public void deleteTagFromCard400() {
        assertEquals(ResponseEntity.badRequest().build(), sut.deleteTagFromCard(-1L, new Tag("Tag")));
        assertEquals(ResponseEntity.badRequest().build(), sut.deleteTagFromCard(1L, null));
        assertEquals(ResponseEntity.badRequest().build(), sut.deleteTagFromCard(1L, new Tag(null)));
    }

    @Test
    public void deleteTagFromCard404() {
        assertEquals(ResponseEntity.notFound().build(), sut.deleteTagFromCard(1L, new Tag("Tag")));
        Card card = new Card("card");
        card.id = 1L;
        when(cardRepo.existsById(1L)).thenReturn(true);
        when(cardRepo.findById(1L)).thenReturn(Optional.of(card));
        Tag tag = new Tag("tag");
        tag.id = 1L;
        Mockito.lenient().when(tagRepo.existsById(1L)).thenReturn(true);
        Mockito.lenient().when(tagRepo.findById(1L)).thenReturn(Optional.of(tag));
        card.tags = new ArrayList<>();
        card.tags.add(tag);
        tag.cards = new ArrayList<>();
        assertEquals(ResponseEntity.notFound().build(), sut.deleteTagFromCard(1L, tag));
        card.tags.remove(tag);
        tag.cards.add(card);
        assertEquals(ResponseEntity.notFound().build(), sut.deleteTagFromCard(1L, tag));
    }

}