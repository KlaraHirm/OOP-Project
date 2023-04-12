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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import commons.*;
import server.services.BoardServiceImpl;
import server.services.TagServiceImpl;

@RestController
@RequestMapping("/api/board")
public class BoardController
{
    @Autowired
    BoardServiceImpl boardService;
    @Autowired
    SimpMessageSendingOperations messageTemplate;

    String update = "updates";

    /**
     * Retrieve all boards in the database
     * @return a json array containing all boards
     */
    @GetMapping("")
    public ResponseEntity<List<Board>> getAll() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }

    /**
     * Retrieve particular Board using ID
     * @param id id of board
     * @return Json representation of board object
     */
    @GetMapping("/{id}")
    public ResponseEntity<Board> getBoard(@PathVariable("id") long id) {
        Board ret = boardService.getBoard(id);
        if (ret==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ret);
    }

    /**
     * Write Board to server
     * @param board board object to create/ write
     * @return json representation of successfully written board
     */
    @PostMapping("")
    public ResponseEntity<Board> addBoard(@RequestBody Board board)
    {   Board ret = boardService.addBoard(board);
        if (ret == null)
        {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(ret);
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
        Board ret = boardService.editBoard(changedBoard);
        if (ret==null) return ResponseEntity.notFound().build();
        messageTemplate.convertAndSend("/topic/updates", update);
        return ResponseEntity.ok(ret);
    }

    /**
     * Deletes a board with particular ID
     * @param id id of board to delete
     * @return Response indicating success fo deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Board> deleteBoardWithID(@PathVariable("id") long id)
    {
        Board ret = boardService.deleteBoardByID(id);
        if (ret  == null) {
            return ResponseEntity.badRequest().build();
        }
        messageTemplate.convertAndSend("/topic/updates", update);
        return ResponseEntity.ok(ret);
    }

    /**
     * Create a cardlist on board with id
     * @param cardList cardlist object to write/ create
     * @param id id of board to which cardlist should be attached
     * @return json representation of written cardlist
     */
    @PostMapping("/{id}")
    public ResponseEntity<CardList> addCardList
    (@RequestBody CardList cardList,
     @PathVariable("id") long id)
    {
        if (cardList == null || cardList.title == null) {
            return ResponseEntity.badRequest().build();
        }
        CardList ret = boardService.addCardList(cardList,id);
        if(ret==null) {
            return ResponseEntity.notFound().build();
        }
        messageTemplate.convertAndSend("/topic/updates", update);
        return ResponseEntity.ok(ret);
    }

    /**
     * Reorder a cardlist on a board
     * @param boardId id of the board
     * @param listId id of the cardlist
     * @param index new index of the cardlist
     * @return the updated board
     */
    @PutMapping(path = { "/{id}/reorder" })
    public ResponseEntity<Board> reorderCardLists
    (@PathVariable("id") long boardId,
     @RequestParam long listId, @RequestParam int index)
    {
        if(boardId <= 0 || listId <= 0 || index < 0)
            return ResponseEntity.notFound().build();
        Board ret = boardService.reorderCardLists(boardId, listId, index);
        if(ret==null){
            return ResponseEntity.badRequest().build();
        }
        messageTemplate.convertAndSend("/topic/updates", update);
        return ResponseEntity.ok(ret);
    }

    /**
     * Write a Tag to the Board
     * @param tag - Tag object to create/ write
     * @return - json representation of successfully written Tag
     * Gives 400 if the body is malformed
     */
    @PostMapping("/tag/{id}")
    public ResponseEntity<Tag> addTag(@PathVariable("id") long boardId, @RequestBody Tag tag) {
        if (boardId < 0 || tag == null || tag.title == null) {
            return ResponseEntity.badRequest().build();
        }
        Tag retrieved = boardService.addTag(boardId, tag);
        if (retrieved == null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(retrieved);
    }

    /**
     * Retrieve all Tags in a Board
     * @return - a json array containing all Tags
     */
    @GetMapping("/tags/{id}")
    public ResponseEntity<List<Tag>> getAllTags(@PathVariable("id") long boardId) {
        return ResponseEntity.ok(boardService.getAllTags(boardId));
    }
}
