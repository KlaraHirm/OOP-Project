package server.api;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.CardListRepository;
import server.database.CardRepository;

@RestController
@RequestMapping("/api/list")
public class ListController {

    private final BoardRepository repoBoard;
    private final CardListRepository repoList;

    private final CardRepository repoCard;

    /**
     * Constructor for the ListController
     */
    public ListController(BoardRepository repoBoard, CardListRepository repoList, CardRepository repoCard) {
        this.repoBoard = repoBoard;
        this.repoList = repoList;
        this.repoCard = repoCard;
    }

    /**
     * Get a particular CardList using ID
     * @param id - the ID of the list
     * @return - json representation of the CardList
     * Returns 404 if the CardList does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<CardList> getList(@PathVariable("id") long id) {
        if (id < 0 || !repoList.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(repoList.findById(id).get());
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
        if (id < 0 || !repoList.existsById(id)) return ResponseEntity.notFound().build();
        CardList cardList = repoList.findById(id).get();
        cardList.cards.add(card);
        repoList.save(cardList);
        repoCard.save(card);
        return ResponseEntity.ok(card);
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
        if (!repoList.existsById(cardListNew.id)) return ResponseEntity.notFound().build();
        CardList cardListOld = repoList.findById(cardListNew.id).get();
        cardListNew.cards = cardListOld.cards;
        repoList.save(cardListNew);
        return ResponseEntity.ok(cardListNew);
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
        if (id < 0 || !repoList.existsById(id)) return ResponseEntity.notFound().build();
        if (id < 0 || !repoBoard.existsById(boardId)) return ResponseEntity.notFound().build();
        CardList cardList = repoList.findById(id).get();
        Board board = repoBoard.findById(boardId).get();
        for (int i = 0; i < cardList.cards.size(); i++) {
            repoCard.deleteById(cardList.cards.get(i).id);
        }
        board.cardLists.remove(cardList);
        repoList.deleteById(id);
        repoBoard.save(board);
        return ResponseEntity.ok(cardList);
    }

    /**
     * Reorder CardLists when drag and drop
     * @param idOld - the original list
     * @param idNew - the target list
     * @param idCard - the Card to be displaced
     * @return the saved target list
     * Returns 404 if IDs and position do not exist
     */
    @PutMapping(path = {"/{id}"})
    public ResponseEntity<CardList> reorder(@RequestParam("original") long idOld, @RequestParam("target") long idNew,
                                            @RequestParam("cardId") long idCard) {
        if ((idOld < 0 || !repoList.existsById(idOld)) &&
                (idNew < 0 || !repoList.existsById(idNew))) {
            return  ResponseEntity.notFound().build();
        }
        if (idCard < 0 || !repoCard.existsById(idCard)) return ResponseEntity.notFound().build();
        CardList oldList = repoList.findById(idOld).get();
        CardList newList = repoList.findById(idNew).get();
        Card card = repoCard.findById(idCard).get();
        int position = 0;
        if (!oldList.cards.contains(card)) return ResponseEntity.notFound().build();
        for (int i = 0; i < oldList.cards.size(); i++) {
            if (oldList.cards.get(i).equals(card)) {
                position++;
            }
        }
        oldList.cards.remove(card);
        newList.cards.add(position, card);
        repoList.save(oldList);
        repoList.save(newList);
        return ResponseEntity.ok(newList);
    }
}
