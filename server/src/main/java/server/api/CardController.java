package server.api;

import commons.Card;
import commons.Subtask;
import commons.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import server.services.CardServiceImpl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/card")
public class CardController {

    @Autowired
    CardServiceImpl cardService;
    @Autowired
    SimpMessageSendingOperations messageTemplate;

    private ExecutorService cardPoll = Executors.newFixedThreadPool(5);

    private final Object lock = new Object();

    String update = "updates";

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
        if(ret==null) {
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
    public ResponseEntity<Card> editCard(@RequestBody Card newCard) {
        if (newCard == null) return ResponseEntity.badRequest().build();
        Card ret = cardService.editCard(newCard);
        if (ret == null) return ResponseEntity.notFound().build();
        messageTemplate.convertAndSend("/topic/updates", update);
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
            @RequestParam("boardId") long boardId,
            @RequestParam("listId") long listId,
            @PathVariable("id") long cardId
    ) {
        if(cardId < 0 || listId < 0 || boardId < 0)
            return ResponseEntity.badRequest().build();
        Card ret = cardService.deleteCard(boardId, listId, cardId);
        if (ret == null) return ResponseEntity.notFound().build();
        synchronized(lock) {
            lock.notifyAll();
        }
        messageTemplate.convertAndSend("/topic/updates", update);
        return ResponseEntity.ok(ret);
    }

    /**
     * Long Polling to detect if card was deleted
     * @param cardId id of a card
     * @return true if card was deleted
     */
    @GetMapping("/poll/{id}")
    public DeferredResult<Boolean> pollCard(@PathVariable("id") long cardId) {
        DeferredResult<Boolean> deferredResult = new DeferredResult<>();
        cardPoll.execute(() -> {
            synchronized(lock) {
                while (pollCardExist(cardId)) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                deferredResult.setResult(true);
            }
        });
        return deferredResult;
    }

    /**
     * Method which checks if card exists
     * @param cardId id of a card
     * @return true if card exists, false otherwise
     */
    public boolean pollCardExist(long cardId) {
        if (cardId < 0) {
            return false;
        }
        Card ret = cardService.getCard(cardId);
        return ret != null;
    }

    /**
     * Getter for all tags of a card
     * @param cardId id of a card
     * @return all the tags card has
     * Gives 400 if cardId < 0
     */
    @GetMapping("/tags/{id}")
    public ResponseEntity<List<Tag>> getTags(@PathVariable("id") long cardId) {
        if(cardId < 0) {
            return ResponseEntity.badRequest().build();
        }
        Card card = cardService.getCard(cardId);
        if(card == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(card.tags);
    }

    /**
     * Removes Tag from a Card
     * @param cardId id of a card
     * @param tag tag to be removed from card
     * @return updated card
     * Gives 400 if cardId < 0 or tag is null or tag.title is null
     * Gives 404 if card doesn't exist or card doesn't contain the tag or the tag isn't in card
     */
    @DeleteMapping("/tag/{id}")
    public ResponseEntity<Card> deleteTagFromCard(@PathVariable("id") long cardId, @RequestBody Tag tag) {
        if(cardId < 0 || tag == null || tag.title == null) {
            return ResponseEntity.badRequest().build();
        }
        Card card = cardService.deleteTagFromCard(cardId, tag);
        if(card == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(card);
    }

    /**
     * Add a Subtask on a card with ID
     * @param subtask - Subtask object
     * @param id - ID of the card to add it to
     * @return the saved card
     * Gives 404 if the CardList does not exist
     * Gives 400 if the body is malformed
     */
    @PostMapping(path = {"/{id}"})
    public ResponseEntity<Subtask> addSubtask(@RequestBody Subtask subtask, @PathVariable("id") long id)
    {
        if (subtask == null || subtask.title == null) return ResponseEntity.badRequest().build();
        Subtask ret = cardService.addSubtask(subtask, id);
        if (ret == null) return ResponseEntity.notFound().build();
        messageTemplate.convertAndSend("/topic/updates", update);
        return ResponseEntity.ok(ret);
    }

}
