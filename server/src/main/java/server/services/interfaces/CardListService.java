package server.services.interfaces;

import commons.Card;
import commons.CardList;

import java.util.List;

public interface CardListService {
    CardList getList(long listId);
    Card addCard(Card card, long listId);
    CardList editList(CardList list);
    CardList deleteList(long boardId, long listId);
    CardList reorder(long idOriginal, long idTarget, long idCard, int cardPlace);
    List<Card> getCards(long listId);
}
