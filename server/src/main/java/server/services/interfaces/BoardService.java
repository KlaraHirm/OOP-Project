package server.services.interfaces;

import commons.Board;
import commons.CardList;
import commons.Tag;

import java.util.List;

public interface BoardService {
    List<Board> getAllBoards();
    Board getBoard(long boardId);
    Board addBoard(Board board);
    Board editBoard(Board board);
    Board deleteBoardByID(long boardId);
    CardList addCardList(CardList list, long boardId);
    Board reorderCardLists(long boardId, long listId, int index);
    Tag addTag(Board board, Tag tag);
    List<Tag> getAllTags(long boardId);
}
