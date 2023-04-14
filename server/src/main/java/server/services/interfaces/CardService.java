package server.services.interfaces;

import commons.Card;
import commons.Subtask;
import commons.Tag;

import java.util.List;

public interface CardService {
    Card getCard(long cardId);
    Card editCard(Card card);
    Card deleteCard(long boardId, long listId, long cardId);
    Card deleteTagFromCard(long cardId, Tag tag);
    Subtask addSubtask(Subtask subtask, long id);
    Card reorder(long cardId, long subtaskId, int subtaskPlace);
}
