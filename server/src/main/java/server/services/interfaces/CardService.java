package server.services.interfaces;

import commons.Card;

public interface CardService {
    Card getCard(long cardId);
    Card editCard(Card card);
    Card deleteCard(long boardId, long listId, long cardId);

}
