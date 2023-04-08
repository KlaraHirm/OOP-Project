package server.services.interfaces;

import commons.Board;
import commons.Tag;

import java.util.List;

public interface TagService {

    List<Tag> getAllTags();

    Tag getTag(long tagId);

    Tag addTag(Tag tag);

    Tag editTag(Tag tag);

    Tag deleteTagWithId(long boardId);
}
