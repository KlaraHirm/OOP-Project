package server.services;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.CardListRepository;
import server.database.CardRepository;
import server.services.interfaces.CardListService;

import java.util.List;

@Service
public class CardListServiceImpl implements CardListService {

    @Autowired
    private BoardRepository boardRepo;

    @Autowired
    private CardListRepository listRepo;

    @Autowired
    private CardRepository cardRepo;

    public CardListServiceImpl(BoardRepository boardRepo, CardListRepository listRepo, CardRepository cardRepo) {
        this.boardRepo = boardRepo;
        this.listRepo = listRepo;
        this.cardRepo = cardRepo;
    }

    /**
     * Get a particular CardList using ID
     * @param listId - the ID of the list
     * @return - representation of the CardList
     * Returns null if the CardList does not exist
     */
    @Override
    public CardList getList(long listId) {
        if (listId < 0 || !listRepo.existsById(listId)) return null;
        return listRepo.findById(listId).get();
    }

    /**
     * Add a Card on a CardList with ID
     * @param card - Card object
     * @param listId - ID of the CardList to which Card should be attached
     * @return the saved card
     * Gives null if the CardList does not exist
     * Gives null if the body is malformed
     */
    @Override
    public Card addCard(Card card, long listId) {
        if (card == null || card.title == null) return null;
        if (listId < 0 || !listRepo.existsById(listId)) return null;
        CardList cardList = listRepo.findById(listId).get();
        cardList.cards.add(card);
        card.place = cardList.cards.size();
        card = cardRepo.save(card);
        listRepo.save(cardList);
        return card;
    }

    /**
     * Update a CardList
     * @param list - the CardList object to edit, with the corresponding ID
     * @return the edited CardList
     * Gives null if the CardList does not exist
     * Gives null if the body is malformed
     */
    @Override
    public CardList editList(CardList list) {
        if (list == null || list.title == null) return null;
        if (!listRepo.existsById(list.id)) return null;
        CardList cardListOld = listRepo.findById(list.id).get();
        list.cards = cardListOld.cards;
        listRepo.save(list);
        return list;
    }

    /**
     * Delete a CardList with ID
     * @param listId - the id of the CardList to delete
     * @param boardId - the id of the Board that owns the CardList
     * @return the deleted CardList
     * Returns null if the CardList does not exist
     */
    @Override
    public CardList deleteList(long boardId, long listId) {
        if (listId < 0 || !listRepo.existsById(listId)) return null;
        if (boardId < 0 || !boardRepo.existsById(boardId)) return null;
        CardList cardList = listRepo.findById(listId).get();
        Board board = boardRepo.findById(boardId).get();
        for (int i = 0; i < cardList.cards.size(); i++) {
            cardRepo.deleteById(cardList.cards.get(i).id);
        }
        board.cardLists.remove(cardList);
        listRepo.deleteById(listId);
        boardRepo.save(board);
        return cardList;
    }

    /**
     * Reorder CardLists when drag and drop
     * @param idOriginal - the original list
     * @param idTarget - the target list
     * @param idCard - the Card to be displaced
     * @return the saved target list
     * Returns null if IDs and position do not exist
     */
    @Override
    public CardList reorder(long idOriginal, long idTarget, long idCard, int cardPlace) {
        if ((idOriginal < 0 || !listRepo.existsById(idOriginal)) &&
                (idTarget < 0 || !listRepo.existsById(idTarget))) {
            return  null;
        }
        if (idCard < 0 || !cardRepo.existsById(idCard)) return null;
        CardList oldList = listRepo.findById(idOriginal).get();
        CardList newList = listRepo.findById(idTarget).get();
        Card card = cardRepo.findById(idCard).get();
        if (!oldList.cards.contains(card)) return null;
        oldList.cards.remove(card);
        int place = 1;
        for(Card c : oldList.cards) {
            c.place = place;
            place++;
        }
        newList.cards.add(cardPlace-1, card);
        place = 1;
        for(Card c : newList.cards) {
            c.place = place;
            place++;
        }
//        if(card.place-1==newList.cards.size()+1){
//            newList.cards.add(card);
//        }
//        else{
//            newList.cards.add(card.place-1, card);
//        }
        listRepo.save(oldList);
        listRepo.save(newList);
        return newList;
    }

    public List<Card> getCards(long listId) {
        if (listId < 0 || !listRepo.existsById(listId)) return null;
        CardList cardList = listRepo.findById(listId).get();
        return cardList.cards;
    }
}
