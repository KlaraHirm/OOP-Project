package server.services.interfaces;

import commons.Board;
import commons.Tag;

import java.util.List;

public interface TagService {
    Tag getTag(long tagId);
    Tag editTag(Tag tag);
    Tag deleteTagWithId(long tagId);
}
