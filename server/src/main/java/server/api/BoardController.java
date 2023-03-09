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

import java.util.List;
import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.database.BoardRepository;
import server.database.CardListRepository;

import commons.*;

@RestController
@RequestMapping("/api/board")
public class BoardController
{

    private final Random random;
    private final BoardRepository repo;

    private final CardListRepository cardListRepo;

    public BoardController(Random random, BoardRepository repo, CardListRepository cardListRepo) {
        this.random = random;
        this.repo = repo;
        this.cardListRepo = cardListRepo;
    }

    @GetMapping(path = { "", "/" })
    public List<Board> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getBoard(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Board> postBoard(@RequestBody Board board)
    {
        if (board.title == null)
        {
            return ResponseEntity.badRequest().build();
        }

        Board saved = repo.save(board);
        return ResponseEntity.ok(saved);
    }

    @PostMapping(path = { "/{id}", "/{id}/" })
    public ResponseEntity<Board> postBoardWithID(@RequestBody Board board, @PathVariable("id") long id)
    {
        if (board.title == null || id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        board.id = id;
        Board saved = repo.save(board);
        return ResponseEntity.ok(saved);
    }

    @PostMapping(path = { "/{id}/cardlist", "/{id}/cardlist/" })
    public ResponseEntity<CardList> postCardlist(@RequestBody CardList cardList, @PathVariable("id") long id)
    {
        if (cardList.title == null || id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Board board = repo.findById(id).get();
        board.cardLists.add(cardList);

        Board saved = repo.save(board);
        return ResponseEntity.ok(cardList);
    }
}