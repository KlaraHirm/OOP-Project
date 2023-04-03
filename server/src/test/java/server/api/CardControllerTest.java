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

import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import server.api.repository.TestCardRepository;
import server.services.BoardServiceImpl;
import server.services.CardServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CardControllerTest {

    @Mock
    private TestCardRepository cardRepo;

    @Mock
    private CardServiceImpl service;
    private CardController sut;

    @BeforeEach
    public void setup()
    {
        service = new CardServiceImpl(cardRepo);
        sut = new CardController();
        sut.cardService = service;
    }

    @Test
    public void testGetCard() {
        Card c = new Card("Title");
        when(cardRepo.existsById(1L)).thenReturn(true);
        when(cardRepo.findById(1L)).thenReturn(Optional.of(c));

        assertNotNull(service.getCard(1L));
        assertEquals("Title", Objects.requireNonNull(sut.getCard(1L).getBody()).title);
    }

    @Test
    public void testPutCard() {
        Card c = new Card("Title");
        c.id = 1L;

        Card e = new Card("Title2");
        e.id = 1L;

        Card a = new Card("Title2");
        a.id = 1L;

        when(cardRepo.existsById(1L)).thenReturn(true);
        Mockito.lenient().when(cardRepo.findById(1L)).thenReturn(Optional.of(c));
        when(cardRepo.save(a)).thenReturn(a);

        assertEquals(ResponseEntity.ok(a), sut.editCard(e));
        verify(cardRepo, times(1)).save(e);
    }

    @Test
    public void testDeleteCard() {
        Card c = new Card("Title");
        c.id = 1L;
        Mockito.lenient().when(cardRepo.existsById(1L)).thenReturn(true);
        when(cardRepo.findById(1L)).thenReturn(Optional.of(c));
        doNothing().when(cardRepo).deleteById(1L);
        assertEquals(ResponseEntity.ok(c), sut.deleteCard(1L));
        verify(cardRepo, times(1)).deleteById(1L);
    }
}