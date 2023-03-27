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
    private final CardRepository cardRepo;

    public CardController(Random random, CardRepository cardRepo) {
        this.random = random;
        this.cardRepo = cardRepo;
    }


    /**
     * Get info about a card
     * @param cardId the id of the card
     * @return the card object
     * Returns 404 if the card does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<Card> getCard(
            @PathVariable("id") long cardId
    ) {
        if (!cardRepo.existsById(cardId)) return ResponseEntity.notFound().build();
        Card card = cardRepo.findById(cardId).get();

        return ResponseEntity.ok(card);
    }

    /**
     * Update a certain card
     * @param newCard the card object to edit, with the corresponding id
     * @return the edited card
     * Gives 404 if the card does not exist
     * Gives 400 if the body is malformed
     */
    @PutMapping("")
    public ResponseEntity<Card>  editCard(
            @RequestBody Card newCard
    ) {
        if (newCard == null) return ResponseEntity.badRequest().build();

        if (!cardRepo.existsById(newCard.id)) return ResponseEntity.notFound().build();

        Card saved = cardRepo.save(newCard);
        return ResponseEntity.ok(saved);
    }

    /**
     * Delete a card
     * @param cardId the id of the card to delete
     * @return the whole board as updated
     * Returns 404 if the card, list or board do not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Card> deleteCard(
            @PathVariable("id") long cardId
    ) {
        if (!cardRepo.existsById(cardId)) return ResponseEntity.notFound().build();

        Card deleted = cardRepo.findById(cardId).get();
        cardRepo.deleteById(cardId);

        return ResponseEntity.ok(deleted);
    }
}
