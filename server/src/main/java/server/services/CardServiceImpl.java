package server.services;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.CardListRepository;
import server.database.CardRepository;
import server.services.interfaces.CardService;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepo;

    @Autowired
    private CardListRepository listRepo;

    @Autowired
    private BoardRepository boardRepo;

    public CardServiceImpl(CardRepository cardRepo, CardListRepository listRepo, BoardRepository boardRepo) {

        this.cardRepo = cardRepo;
        this.listRepo = listRepo;
        this.boardRepo = boardRepo;
    }


    /**
     * Get info about a card
     * @param cardId the id of the card
     * @return the card object
     * Returns null if the card does not exist
     */
    @Override
    public Card getCard(long cardId) {
        if (!cardRepo.existsById(cardId)) return null;

        return cardRepo.findById(cardId).get();
    }

    /**
     * Update a certain card
     * @param card the card object to edit, with the corresponding id
     * @return the edited card
     * Gives null if the card does not exist
     * Gives null if the body is malformed
     */
    @Override
    public Card editCard(Card card) {
        if (card == null) return null;

        if (!cardRepo.existsById(card.id)) return null;

        return cardRepo.save(card);
    }

    /**
     * Delete a card
     * @param cardId the id of the card to delete
     * @param listId the id of the list where card is
     * @param boardId the id of the board
     * @return the whole board as updated
     * Returns null if the card, list or board do not exist
     */
    @Override
    public Card deleteCard(long boardId, long listId, long cardId) {
        if(cardId < 0) return null;
        if (!cardRepo.existsById(cardId)) return null;

        CardList cardList = listRepo.findById(listId).get();
        Board board = boardRepo.findById(boardId).get();
        Card deleted = cardRepo.findById(cardId).get();
        int listIndex = board.cardLists.indexOf(cardList);

        cardList.cards.remove(deleted);
        board.cardLists.set(listIndex, cardList);
        boardRepo.save(board);
        listRepo.save(cardList);
        cardRepo.deleteById(cardId);

        return deleted;
    }
}