package server.services;

import commons.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.*;
import server.services.interfaces.CardService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepo;

    @Autowired
    private CardListRepository listRepo;

    @Autowired
    private BoardRepository boardRepo;

    @Autowired
    private TagRepository tagRepo;

    @Autowired
    private SubtaskRepository subtaskRepo;

    public CardServiceImpl(
            CardRepository cardRepo,
            CardListRepository listRepo,
            BoardRepository boardRepo,
            TagRepository tagRepo,
            SubtaskRepository subtaskRepo
    ) {
        this.cardRepo = cardRepo;
        this.listRepo = listRepo;
        this.boardRepo = boardRepo;
        this.tagRepo = tagRepo;
        this.subtaskRepo = subtaskRepo;
    }


    /**
     * Get info about a card
     * @param cardId the id of the card
     * @return the card object
     * Returns null if the card does not exist
     */
    @Override
    public Card getCard(long cardId) {
        if (cardId<0 || !cardRepo.existsById(cardId)) return null;

        return cardRepo.findById(cardId).get();
    }

    /**
     * Update a certain card + attach Tags
     * @param card the card object to edit, with the corresponding id
     * @return the edited card
     * Gives null if the card does not exist
     * Gives null if the body is malformed
     */
    @Override
    public Card editCard(Card card) {
        if (card == null) return null;
        if (!cardRepo.existsById(card.id)) return null;

        Card original = cardRepo.findById(card.id).get();
        if(original.tags==null) original.tags = new ArrayList<>();
        if(card.tags==null) card.tags = new ArrayList<>();
        for(Tag tagOriginal : original.tags) {
            if(tagOriginal.cards == null) tagOriginal.cards = new ArrayList<>();
            tagOriginal.cards.remove(original);
            tagRepo.save(tagOriginal);
        }
        List<Tag> tags = new ArrayList<>();
        for(Tag tagCard : card.tags) {
            if(!tagRepo.existsById(tagCard.id)){
                continue;
            }
            if(tagCard.cards == null) tagCard.cards = new ArrayList<>();
            tagCard.cards.add(card);
            tagRepo.save(tagCard);
            tags.add(tagCard);
        }
        card.tags = tags;

        if(original.subtasks==null) original.subtasks = new ArrayList<>();
        if(card.subtasks==null) card.subtasks = new ArrayList<>();
        for(Subtask subtaskOriginal : original.subtasks) {
            if(!card.subtasks.contains(subtaskOriginal)) {
                subtaskRepo.save(subtaskOriginal);
            }
        }
        for(Subtask subtaskCard : card.subtasks) {
            if(!original.subtasks.contains(subtaskCard)) {
                subtaskRepo.save(subtaskCard);
            }
        }

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
        if (!cardRepo.existsById(cardId)) return null;
        if (!listRepo.existsById(listId)) return null;
        if (!boardRepo.existsById(boardId)) return null;

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

    @Override
    public Card deleteTagFromCard(long cardId, Tag tag) {
        if(!cardRepo.existsById(cardId)){
            return null;
        }
        Card card = cardRepo.findById(cardId).get();
        if(!card.tags.contains(tag) || !tag.cards.contains(card)) {
            return null;
        }
        card.tags.remove(tag);
        tag.cards.remove(card);
        cardRepo.save(card);
        tagRepo.save(tag);
        return card;
    }

    /**
     * Add a subtask on a card with ID
     * @param subtask - Subtask object
     * @param cardId - ID of the CardList to which Card should be attached
     * @return the saved card
     * Gives null if the CardList does not exist
     * Gives null if the body is malformed
     */
    @Override
    public Subtask addSubtask(Subtask subtask, long cardId) {
        if (subtask == null || subtask.title == null) return null;
        if (cardId < 0 || !cardRepo.existsById(cardId)) return null;
        Card card = cardRepo.findById(cardId).get();
        card.subtasks.add(subtask);
        subtask.place = card.subtasks.size();
        subtask = subtaskRepo.save(subtask);
        cardRepo.save(card);
        return subtask;
    }

    /**
     * Reorder subtasks when drag and drop
     * @param cardId - the card in which to move the subtask
     * @param subtaskId - the subtask to move
     * @param subtaskPlace - the place to move it
     * @return the saved card
     * Returns null if IDs and position do not exist
     */
    @Override
    public Card reorder(long cardId, long subtaskId, int subtaskPlace) {
        if (cardId < 0 || !cardRepo.existsById(cardId)) {
            return  null;
        }
        if (subtaskId < 0 || !subtaskRepo.existsById(subtaskId)) return null;
        Card card = cardRepo.findById(cardId).get();
        Subtask subtask = subtaskRepo.findById(subtaskId).get();
        if (!card.subtasks.contains(subtask)) return null;
        card.subtasks.remove(subtask);
        if (subtaskPlace < card.subtasks.size()) {
            card.subtasks.add(subtaskPlace, subtask);
        } else {
            card.subtasks.add(subtask);
        }
        int place = 1;
        for(Subtask s : card.subtasks) {
            s.place = place;
            place++;
        }
        cardRepo.save(card);
        return card;
    }
}
