package server.services.interfaces;

import commons.Card;
import commons.Tag;

public interface CardService {
    Card getCard(long cardId);
    Card editCard(Card card);
    Card deleteCard(long boardId, long listId, long cardId);
    Card attachTag(long cardId, long tagId);
}
