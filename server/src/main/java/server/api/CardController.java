package server.api;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.CardListRepository;
import server.database.CardRepository;

import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/card")
public class CardController {

    private final Random random;
    private final BoardRepository repo;
    private final CardListRepository listRepo;
    private final CardRepository cardRepo;

    public CardController(Random random, BoardRepository repo, CardListRepository listRepo, CardRepository cardRepo) {
        this.random = random;
        this.repo = repo;
        this.listRepo = listRepo;
        this.cardRepo = cardRepo;
    }


    /**
     * Get info about a card
     * @param cardId the id of the card
     * @return the card object
     * Returns 404 if the card does not exist
     */
    @GetMapping("")
    public ResponseEntity<Card> getCard(
            @RequestParam("card") long cardId
    ) {
        if (!cardRepo.existsById(cardId)) return ResponseEntity.notFound().build();
        Card card = cardRepo.findById(cardId).get();

        return ResponseEntity.ok(card);
    }

    /**
     * Add a new card to a list
     * @param listId the id of the list to add the card to
     * @param boardId the id of the board in which the list is
     * @param card the card object to add
     * @return the whole board as updated
     * Gives 404 if the list or board do not exist
     * Gives 400 if the body is malformed
     */
    @PostMapping("")
    public ResponseEntity<Board> addCard(
            @RequestBody Card card,
            @RequestParam("board") long boardId,
            @RequestParam("list") long listId)
    {
        if (card == null) return ResponseEntity.badRequest().build();

        if (!repo.existsById(boardId)) return ResponseEntity.notFound().build();
        Board board = repo.findById(boardId).get();

        Optional<CardList> cardList = board.cardLists.stream().filter(cl -> cl.id == listId).findFirst();
        if (cardList.isEmpty()) return ResponseEntity.notFound().build();

        cardList.get().cards.add(card);

        Board saved = repo.save(board);
        return ResponseEntity.ok(saved);
    }

    /**
     * Update a certain card
     * @param cardId the id of the card to edit
     * @param newCard the card object to add
     * @return the edited card
     * Gives 404 if the card does not exist
     * Gives 400 if the body is malformed
     */
    @PutMapping("")
    public ResponseEntity<Card>  editCard(
            @RequestBody Card newCard,
            @RequestParam("card") long cardId
    ) {
        if (newCard == null) return ResponseEntity.badRequest().build();

        if (!cardRepo.existsById(cardId)) return ResponseEntity.notFound().build();
        Card card = cardRepo.findById(cardId).get();

        card.title = newCard.title;

        Card saved = cardRepo.save(card);
        return ResponseEntity.ok(saved);
    }

    /**
     * Delete a card
     * @param cardId the id of the card to delete
     * @param listId the id of the list in which the card is
     * @param boardId the id of the board in which the list is
     * @return the whole board as updated
     * Returns 404 if the card, list or board do not exist
     */
    @DeleteMapping("")
    public ResponseEntity<Board> deleteCard(
            @RequestParam("card") long cardId,
            @RequestParam("board") long boardId,
            @RequestParam("list") long listId
    ) {
        Board board = repo.findById(boardId).get();
        if (!repo.existsById(boardId)) return ResponseEntity.notFound().build();

        Optional<CardList> cardList = board.cardLists.stream().filter(cl -> cl.id == listId).findFirst();
        if (cardList.isEmpty()) return ResponseEntity.notFound().build();

        Optional<Card> card = cardList.get().cards.stream().filter(c -> c.id == cardId).findFirst();
        if (card.isEmpty()) return ResponseEntity.notFound().build();

        cardList.get().cards.remove(card.get());

        Board saved = repo.save(board);
        return ResponseEntity.ok(saved);
    }
}
