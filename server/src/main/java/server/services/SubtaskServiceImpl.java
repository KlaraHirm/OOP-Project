package server.services;

import commons.Board;
import commons.CardList;
import commons.Subtask;
import commons.Card;
import commons.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.*;
import server.services.interfaces.SubtaskService;

import java.util.ArrayList;

@Service
public class SubtaskServiceImpl implements SubtaskService {

    @Autowired
    private SubtaskRepository subtaskRepo;

    @Autowired
    private CardRepository cardRepo;

    @Autowired
    private CardListRepository listRepo;

    @Autowired
    private BoardRepository boardRepo;

    public SubtaskServiceImpl(BoardRepository boardRepo, CardListRepository listRepo, CardRepository cardRepo, SubtaskRepository subtaskRepo) {
        this.boardRepo = boardRepo;
        this.listRepo = listRepo;
        this.cardRepo = cardRepo;
        this.subtaskRepo = subtaskRepo;
    }


    /**
     * Get info about a subtask
     * @param subtaskId the id of the subtask
     * @return the subtask object
     * Returns null if the subtask does not exist
     */
    @Override
    public Subtask getSubtask(long subtaskId) {
        if (subtaskId<0 || !subtaskRepo.existsById(subtaskId)) return null;

        return subtaskRepo.findById(subtaskId).get();
    }

    /**
     * Update a certain subtask + attach Tags
     * @param subtask the subtask object to edit, with the corresponding id
     * @return the edited subtask
     * Gives null if the subtask does not exist
     * Gives null if the body is malformed
     */
    @Override
    public Subtask editSubtask(Subtask subtask) {
        if (subtask == null) return null;
        if (!subtaskRepo.existsById(subtask.id)) return null;
        return subtaskRepo.save(subtask);
    }

    /**
     * Delete a subtask
     * @param subtaskId the id of the subtask to delete
     * @param listId the id of the list where subtask is
     * @param boardId the id of the board
     * @return the whole board as updated
     * Returns null if the subtask, list or board do not exist
     */
    @Override
    public Subtask deleteSubtask(long boardId, long listId, long cardId, long subtaskId) {
        if (!subtaskRepo.existsById(subtaskId)) return null;
        if (!cardRepo.existsById(cardId)) return null;
        if (!listRepo.existsById(listId)) return null;
        if (!boardRepo.existsById(boardId)) return null;

        Card card = cardRepo.findById(cardId).get();
        CardList list = listRepo.findById(listId).get();
        Board board = boardRepo.findById(boardId).get();
        Subtask deleted = subtaskRepo.findById(subtaskId).get();
        int listIndex = board.cardLists.indexOf(list);
        int cardIndex = list.cards.indexOf(card);

        card.subtasks.remove(deleted);
        board.cardLists.set(listIndex, list);
        list.cards.set(cardIndex, card);
        boardRepo.save(board);
        listRepo.save(list);
        cardRepo.save(card);
        subtaskRepo.deleteById(subtaskId);

        return deleted;
    }
}
