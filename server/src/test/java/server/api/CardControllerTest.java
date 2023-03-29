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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.Random;

import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commons.Person;
import commons.Quote;
import server.api.repository.TestBoardRepository;
import server.api.repository.TestCardRepository;
import server.api.repository.TestCardListRepository;
import server.api.util.RandomMock;

public class CardControllerTest {

    public int nextInt;
    private RandomMock random;
    private TestBoardRepository repo;
    private TestCardListRepository listRepo;
    private TestCardRepository cardRepo;

    private CardController sut;

    @BeforeEach
    public void setup() {
        random = new RandomMock();
        cardRepo = new TestCardRepository();
        sut = new CardController(random, cardRepo);
    }

    @Test
    public void testGetCard() {
        Card c = new Card("Title");
        Card saved = cardRepo.save(c);

        assertEquals(sut.getCard(saved.id).getBody().title, "Title");
    }

    @Test
    public void testPutCard() {
        Card c = new Card("Title");
        Card saved = cardRepo.save(c);

        saved.title = "Title2";
        sut.editCard(saved);

        assertEquals(cardRepo.findById(saved.id).get().title, "Title2");
    }

    @Test
    public void testDeleteCard() {
        Card c = new Card("Title");
        Card saved = cardRepo.save(c);

        sut.deleteCard(saved.id);

        assertTrue(cardRepo.findById(saved.id).isEmpty());
    }
}