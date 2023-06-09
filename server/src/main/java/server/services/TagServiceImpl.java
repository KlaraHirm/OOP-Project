package server.services;

import commons.Board;
import commons.Card;
import commons.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.CardRepository;
import server.database.TagRepository;
import server.services.interfaces.TagService;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepo;

    @Autowired
    private CardRepository cardRepo;

    @Autowired
    private BoardRepository boardRepo;

    public TagServiceImpl(TagRepository tagRepo, CardRepository cardRepo, BoardRepository boardRepo) {
        this.tagRepo = tagRepo;
        this.cardRepo = cardRepo;
        this.boardRepo = boardRepo;
    }

    /**
     * Retrieve particular Tag using ID
     * @param tagId - ID of the Tag
     * @return - Tag object
     */
    @Override
    public Tag getTag(long tagId) {
        if (!tagRepo.existsById(tagId)) {
            return null;
        }
        return tagRepo.findById(tagId).get();
    }

    /**
     * Update a Tag
     * @param changedTag - the Tag object to edit, with the corresponding ID
     * @return - the edited Tag
     * Gives null if the Tag does not exist
     * Gives null if the body is malformed
     */
    @Override
    public Tag editTag(Tag changedTag) {
        if (changedTag == null || !tagRepo.existsById(changedTag.id)) return null;
        Tag original = tagRepo.findById(changedTag.id).get();
        changedTag.cards = original.cards;
        return tagRepo.save(changedTag);
    }

    /**
     * Deletes a Tag with particular ID
     * @param tagId - ID of the Tag to delete
     * @return - deleted Tag
     */
    @Override
    public Tag deleteTagWithId(long tagId, long boardId) {
        if (tagId < 0 || !tagRepo.existsById(tagId) || !boardRepo.existsById(boardId)) {
            return null;
        }
        Tag tag = tagRepo.findById(tagId).get();
        Board board = boardRepo.findById(boardId).get();
        board.tags.remove(tag);
        boardRepo.save(board);

        for (Card card : tag.cards) {
            card.tags.remove(tag);
            cardRepo.save(card);
        }
        tagRepo.deleteById(tagId);
        return tag;
    }

}
