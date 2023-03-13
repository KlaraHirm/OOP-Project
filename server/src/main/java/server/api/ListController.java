package server.api;

import commons.Board;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.CardListRepository;
import java.util.Random;


@RestController
@RequestMapping("/api/list")
public class ListController {
    private final Random random;
    private final BoardRepository repoBoard;
    private final CardListRepository repoList;

    /**
     * Constructor for the ListController
     */
    public ListController(Random random, BoardRepository repoBoard,
                          CardListRepository repoList) {
        this.random = random;
        this.repoBoard = repoBoard;
        this.repoList = repoList;
    }

    /**
     * Get info about a list
     * @param listId - the id of the list
     * @return - list object
     * Returns 404 if the list does not exist
     */
    @GetMapping(path = {""})
    public ResponseEntity<CardList> getList(@RequestParam("list") long listId) {
        if (!repoList.existsById(listId)) return ResponseEntity.notFound().build();
        CardList list = repoList.findById(listId).get();
        return ResponseEntity.ok(list);
    }

    /**
     * Add a new list to a board
     * @param boardId - the id of the board in which the list is
     * @param list - the list object to add
     * @return the whole board as updated
     * Gives 404 if the board does not exist
     * Gives 400 if the body is malformed
     */
    @PostMapping(path = {""})
    public ResponseEntity<Board> addList(@RequestBody CardList list, @RequestParam("board") long boardId) {
        if (list == null) return ResponseEntity.badRequest().build();
        if (!repoBoard.existsById(boardId)) return ResponseEntity.notFound().build();
        Board board = repoBoard.findById(boardId).get();
        board.cardLists.add(list);
        Board saved = repoBoard.save(board);
        return ResponseEntity.ok(saved);
    }

    /**
     * Update a certain list
     * @param listTitle - the new title
     * @param listId - the id of the list for update
     * @return the edited list
     * Gives 404 if the list does not exist
     * Gives 400 if the title is malformed
     */
    @PutMapping(path = {""})
    public ResponseEntity<CardList> editList(@RequestParam("title") String listTitle,
                                             @RequestParam("list") long listId) {
        if (listTitle == null) return ResponseEntity.badRequest().build();
        if (!repoList.existsById(listId)) return ResponseEntity.notFound().build();
        CardList list = repoList.findById(listId).get();
        list.title = listTitle;
        CardList saved = repoList.save(list);
        return ResponseEntity.ok(saved);
    }

    /**
     * Delete a list
     * @param listId - the id of the list to delete
     * @param boardId the id of the board in which the list is
     * @return the whole board as updated
     * Returns 404 if the list or board do not exist
     */
    @DeleteMapping(path = {""})
    public ResponseEntity<Board> deleteList(@RequestParam("list") long listId,
                                            @RequestParam("board") long boardId) {
        if (!repoList.existsById(listId)) return ResponseEntity.notFound().build();
        if (!repoBoard.existsById(boardId)) return ResponseEntity.notFound().build();
        Board board = repoBoard.findById(boardId).get();
        CardList list = repoList.findById(listId).get();
        board.cardLists.remove(list);
        Board saved = repoBoard.save(board);
        repoList.deleteById(listId);
        return ResponseEntity.ok(saved);
    }
}
