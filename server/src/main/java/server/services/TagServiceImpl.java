package server.services;

import commons.Board;
import commons.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.services.interfaces.database.BoardRepository;
import server.services.interfaces.database.CardRepository;
import server.services.interfaces.database.TagRepository;
import server.services.interfaces.TagService;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepo;

    @Autowired
    private CardRepository cardRepo;

    @Autowired
    private BoardRepository boardRepo;

    public TagServiceImpl(TagRepository tagRepo, CardRepository cardRepo) {
        this.tagRepo = tagRepo;
        this.cardRepo = cardRepo;
    }

    /**
     * Retrieve all Tags in the database
     * @return - list containing all Tags
     */
    @Override
    public List<Tag> getAllTags() {
        return tagRepo.findAll();
    }

    /**
     * Retrieve particular Tag using ID
     * @param tagId - ID of the Tag
     * @return - Tag object
     */
    @Override
    public Tag getTag(long tagId) {
        if (tagId < 0 || !tagRepo.existsById(tagId)) {
            return null;
        }
        return tagRepo.findById(tagId).get();
    }

    /**
     * Write Tag to server
     * @param tag - Tag object to create/ write
     * @return - object representation of successfully written Tag
     */
    @Override
    public Tag addTag(Board board, Tag tag) {
        if (tag.title == null)
        {
            return null;
        }
        if (board == null) {
            return null;
        }
        //board.tags.add(tag);
        boardRepo.save(board);
        return tagRepo.save(tag);
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
        return tagRepo.save(changedTag);
    }

    /**
     * Deletes a Tag with particular ID
     * @param tagId - ID of the Tag to delete
     * @return - deleted Tag
     */
    @Override
    public Tag deleteTagWithId(long tagId) {
        if (tagId < 0 || !tagRepo.existsById(tagId)) {
            return null;
        }
        Tag tag = tagRepo.findById(tagId).get();
        tagRepo.deleteById(tagId);
        return tag;
    }

}
