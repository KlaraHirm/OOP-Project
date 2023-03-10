package server.api;

import commons.Board;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.Optional;
import java.util.Random;


@RestController
@RequestMapping("/api/list")
public class ListController{
    private final Random random;
    private final BoardRepository repo;

    public ListController(Random random, BoardRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    @GetMapping(path = { "", "/" })
    public ResponseEntity<CardList> getList(@RequestParam("list") long listId, @RequestParam("board") long boardId)
    {
        if (!repo.existsById(boardId)) return ResponseEntity.badRequest().build();
        Board board = repo.findById(boardId).get();

        Optional<CardList> list = board.cardLists.stream().filter(l -> l.id == listId).findFirst();

        return ResponseEntity.ok(list.get());
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Board> addList(@RequestBody CardList list, @RequestParam("board") long boardId)
    {
        if (list == null) return ResponseEntity.badRequest().build();

        if (!repo.existsById(boardId)) return ResponseEntity.badRequest().build();
        Board board = repo.getById(boardId);
        board.cardLists.add(list);
        Board saved = repo.save(board);
        return ResponseEntity.ok(saved);
    }
}
