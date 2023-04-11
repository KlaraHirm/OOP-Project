package server.api;

import commons.Card;
import commons.CardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import server.services.CardListServiceImpl;
import java.util.List;

@RestController
@RequestMapping("/api/list")
public class ListController {

    @Autowired
    CardListServiceImpl listService;
    @Autowired
    SimpMessageSendingOperations messageTemplate;

    String update = "updates";

    /**
     * Get a particular CardList using ID
     * @param id - the ID of the list
     * @return - json representation of the CardList
     * Returns 404 if the CardList does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<CardList> getList(@PathVariable("id") long id) {
        CardList ret = listService.getList(id);
        if(ret==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ret);
    }

    /**
     * Add a Card on a CardList with ID
     * @param card - Card object
     * @param id - ID of the CardList to which Card should be attached
     * @return the saved card
     * Gives 404 if the CardList does not exist
     * Gives 400 if the body is malformed
     */
    @PostMapping(path = {"/{id}"})
    public ResponseEntity<Card> addCard(@RequestBody Card card, @PathVariable("id") long id)
    {
        if (card == null || card.title == null) return ResponseEntity.badRequest().build();
        Card ret = listService.addCard(card, id);
        if (ret == null) return ResponseEntity.notFound().build();
        messageTemplate.convertAndSend("/topic/updates", update);
        return ResponseEntity.ok(ret);
    }

    /**
     * Update a CardList
     * @param cardListNew - the CardList object to edit, with the corresponding ID
     * @return the edited CardList
     * Gives 404 if the CardList does not exist
     * Gives 400 if the body is malformed
     */
    @PutMapping(path = {""})
    public ResponseEntity<CardList> editList(@RequestBody CardList cardListNew) {
        if (cardListNew == null || cardListNew.title == null) return ResponseEntity.badRequest().build();
        CardList ret = listService.editList(cardListNew);
        if (ret == null) return ResponseEntity.notFound().build();
        messageTemplate.convertAndSend("/topic/updates", update);
        return ResponseEntity.ok(ret);
    }

    /**
     * Delete a CardList with ID
     * @param id - the id of the CardList to delete
     * @param boardId - the id of the Board that owns the CardList
     * @return the deleted CardList
     * Returns 404 if the CardList does not exist
     */
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<CardList> deleteList(@RequestParam("boardId") long boardId, @PathVariable("id") long id) {
        CardList ret = listService.deleteList(boardId, id);
        if (ret == null) return ResponseEntity.notFound().build();
        messageTemplate.convertAndSend("/topic/updates", update);
        return ResponseEntity.ok(ret);
    }

    /**
     * Reorder CardLists when drag and drop
     * @param idOld - the original list
     * @param idNew - the target list
     * @return the saved target list
     * Returns 404 if IDs and position do not exist
     */
    @PutMapping(path = {"/reorder"})
    public ResponseEntity<CardList> reorder(@RequestParam("original") long idOld, @RequestParam("target") long idNew,
                                  @RequestParam("cardId") long idCard, @RequestParam("cardPlace") int placeCard) {
        if(placeCard < 0 || idOld < 0 || idNew < 0 || idCard < 0) {
            return ResponseEntity.badRequest().build();
        }
        CardList ret = listService.reorder(idOld, idNew, idCard, placeCard);
        if (ret == null) {
            return  ResponseEntity.notFound().build();
        }
        messageTemplate.convertAndSend("/topic/updates", update);
        return ResponseEntity.ok(ret);
    }

    /**
     * Getter for cards in list
     * @param listId id of a list
     * @return ordered list of cards by place
     * Returns 404 when ID not exists
     */
    @GetMapping(path = {"/{id}/cards"})
    public ResponseEntity<List<Card>> getCards(@PathVariable("id") long listId) {
        List<Card> ret = listService.getCards(listId);
        if (ret == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ret);
    }
}
