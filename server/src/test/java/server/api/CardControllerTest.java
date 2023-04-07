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

import java.util.Objects;
import java.util.Optional;

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
import server.api.repository.TestBoardRepository;
import server.api.repository.TestCardListRepository;
import server.api.repository.TestCardRepository;
import server.services.CardServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CardControllerTest {

    @Mock
    private TestCardRepository cardRepo;

    @Mock
    private TestCardListRepository listRepo;

    @Mock
    private TestBoardRepository boardRepo;

    private CardServiceImpl service;
    private CardController sut;

    @BeforeEach
    public void setup()
    {
        service = new CardServiceImpl(cardRepo, listRepo, boardRepo);
        sut = new CardController();
        sut.cardService = service;
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

        Card card2 = new Card("Title2");
        card2.id = 1L;

        when(cardRepo.existsById(1L)).thenReturn(true);
        Mockito.lenient().when(cardRepo.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepo.save(card2)).thenReturn(card2);

        assertEquals(ResponseEntity.ok(card2), sut.editCard(card1));
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
}