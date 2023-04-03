package server.api;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.CardListRepository;
import server.database.CardRepository;
import server.services.CardListServiceImpl;
import server.services.CardServiceImpl;

import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/card")
public class CardController {

    @Autowired
    CardServiceImpl cardService;


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
        Card ret = cardService.getCard(cardId);
        if(ret==null){
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ret);
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
        Card ret = cardService.editCard(newCard);
        if (ret == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ret);
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
        Card ret = cardService.deleteCard(cardId);
        if (ret == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ret);
    }
}
