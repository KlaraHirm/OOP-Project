package server.services.interfaces;

import commons.Card;
import commons.CardList;

public interface CardListService {
    CardList getList(long listId);
    Card addCard(Card card, long listId);
    CardList editList(CardList list);
    CardList deleteList(long boardId, long listId);
    CardList reorder(long idOriginal, long idTarget, long idCard);
}
