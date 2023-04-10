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
import java.util.concurrent.atomic.AtomicBoolean;

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
            return ResponseEntity.notFound().build();
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
     * @param listId the id of the list where card is
     * @param boardId the id of the board
     * @return the whole board as updated
     * Returns 404 if the card, list or board do not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Card> deleteCard(
            @RequestParam("boardId") long boardId, @RequestParam("listId") long listId, @PathVariable("id") long cardId
    ) {
        if(cardId < 0 || listId < 0 || boardId < 0) return ResponseEntity.badRequest().build();
        // create a new atomic boolean to track if the task exists
        AtomicBoolean taskExists = new AtomicBoolean(true);

        // start a new thread to handle the long polling request
        new Thread(new CardPoller(cardId, taskExists)).start();

        Card ret = cardService.deleteCard(boardId, listId, cardId);
        if (ret == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/poll/{id}")
    public ResponseEntity<Boolean> pollCard(
            @PathVariable("id") long cardId
    ) {
        boolean exists = cardService.cardExists(cardId);
        return ResponseEntity.ok(exists);
    }
}
