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

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.database.BoardRepository;

import commons.*;
import server.database.CardListRepository;

@RestController
@RequestMapping("/api/board")
public class BoardController
{

    private final BoardRepository repo;

    private final CardListRepository repoList;

    /**
     * Constructor for the BoardController
     */
    public BoardController(BoardRepository repo, CardListRepository repoList) {
        this.repo = repo;
        this.repoList = repoList;
    }

    /**
     * Retrieve all boards in the database
     * @return a json array containing all boards
     */
    @GetMapping("")
    public List<Board> getAll() {
        return repo.findAll();
    }

    /**
     * Retrieve particular Board using ID
     * @param id id of board
     * @return Json representation of board object
     */
    @GetMapping("/{id}")
    public ResponseEntity<Board> getBoard(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Write Board to server
     * @param board board object to create/ write
     * @return json representation of successfully written board
     */
    @PostMapping("")
    public ResponseEntity<Board> addBoard(@RequestBody Board board)
    {
        if (board.title == null)
        {
            return ResponseEntity.badRequest().build();
        }

        Board saved = repo.save(board);
        if(saved.cardLists == null) saved.cardLists = new ArrayList<>();
        return ResponseEntity.ok(saved);
    }

    /**
     * Update a board
     * @param changedBoard the board object to edit, with the corresponding id
     * @return the edited board
     * Gives 404 if the board does not exist
     * Gives 400 if the body is malformed
     */
    @PutMapping("")
    public ResponseEntity<Board> editBoard(@RequestBody Board changedBoard) {
        if (changedBoard == null) return ResponseEntity.badRequest().build();

        if (!repo.existsById(changedBoard.id)) return ResponseEntity.notFound().build();
        Board board = repo.findById(changedBoard.id).get();

        changedBoard.cardLists = board.cardLists;

        Board saved = repo.save(changedBoard);
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes a board with particular ID
     * @param id id of board to delete
     * @return Response indicating success fo deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Board> deleteBoardWithID(@PathVariable("id") long id)
    {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Board board = repo.findById(id).get();
        repo.deleteById(id);
        return ResponseEntity.ok(board);
    }

    /**
     * Create a cardlist on board with id
     * @param cardList cardlist object to write/ create
     * @param id id of board to which cardlist should be attached
     * @return json representation of written cardlist
     */
    @PostMapping("/{id}")
    public ResponseEntity<CardList> addCardList(@RequestBody CardList cardList, @PathVariable("id") long id)
    {
        if (cardList == null || cardList.title == null) {
            return ResponseEntity.badRequest().build();
        }
        if(id < 0 || !repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Board board = repo.findById(id).get();
        board.cardLists.add(cardList);
        if(cardList.cards == null) cardList.cards = new ArrayList<>();

        CardList saved = repoList.save(cardList);
        repo.save(board);
        return ResponseEntity.ok(saved);
    }
}
