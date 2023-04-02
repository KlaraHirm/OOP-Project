package server.services;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.CardListRepository;
import server.database.CardRepository;
import server.services.interfaces.CardListService;

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
     * @param listId
     * @return
     */
    @Override
    public CardList getList(long listId) {
        if (listId < 0 || !listRepo.existsById(listId)) return null;
        return listRepo.findById(listId).get();
    }

    /**
     * @param card
     * @param listId
     * @return
     */
    @Override
    public Card addCard(Card card, long listId) {
        if (card == null || card.title == null) return null;
        if (listId < 0 || !listRepo.existsById(listId)) return null;
        CardList cardList = listRepo.findById(listId).get();
        cardList.cards.add(card);
        listRepo.save(cardList);
        cardRepo.save(card);
        return card;
    }

    /**
     * @param list
     * @return
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
     * @param boardId
     * @param listId
     * @return
     */
    @Override
    public CardList deleteList(long boardId, long listId) {
        if (listId < 0 || !listRepo.existsById(listId)) return null;
        if (boardId < 0 || !boardRepo.existsById(boardId)) return null;
        CardList cardList = listRepo.findById(listId).get();
        Board board = boardRepo.findById(boardId).get();
        for (int i = 0; i < cardList.cards.size(); i++) {
            boardRepo.deleteById(cardList.cards.get(i).id);
        }
        board.cardLists.remove(cardList);
        listRepo.deleteById(listId);
        boardRepo.save(board);
        return cardList;
    }

    /**
     * @param idOriginal
     * @param idTarget
     * @param idCard
     * @return
     */
    @Override
    public CardList reorder(long idOriginal, long idTarget, long idCard) {
        if ((idOriginal < 0 || !listRepo.existsById(idOriginal)) &&
                (idTarget < 0 || !listRepo.existsById(idTarget))) {
            return  null;
        }
        if (idCard < 0 || !cardRepo.existsById(idCard)) return null;
        CardList oldList = listRepo.findById(idOriginal).get();
        CardList newList = listRepo.findById(idTarget).get();
        Card card = cardRepo.findById(idCard).get();
        int position = 0;
        if (!oldList.cards.contains(card)) return null;
        for (int i = 0; i < oldList.cards.size(); i++) {
            if (oldList.cards.get(i).equals(card)) {
                position++;
            }
        }
        oldList.cards.remove(card);
        newList.cards.add(position, card);
        listRepo.save(oldList);
        listRepo.save(newList);
        return newList;
    }
}
