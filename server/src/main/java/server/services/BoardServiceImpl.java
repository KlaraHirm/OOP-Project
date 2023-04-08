package server.services;

import commons.Board;
import commons.CardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.services.interfaces.database.BoardRepository;
import server.services.interfaces.database.CardListRepository;
import server.services.interfaces.BoardService;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    @Autowired
    private BoardRepository boardRepo;

    @Autowired
    private CardListRepository listRepo;

    public BoardServiceImpl(BoardRepository boardRepo, CardListRepository listRepo) {
        this.boardRepo = boardRepo;
        this.listRepo = listRepo;
    }

    /**
     * Retrieve all boards in the database
     * @return list containing all boards
     */
    @Override
    public List<Board> getAllBoards() {
        return boardRepo.findAll();
    }

    /**
     * Retrieve particular Board using ID
     * @param boardId id of board
     * @return board object
     */
    @Override
    public Board getBoard(long boardId) {
        if (boardId < 0 || !boardRepo.existsById(boardId)) {
            return null;
        }
        return boardRepo.findById(boardId).get();
    }

    /**
     * Write Board to server
     * @param board board object to create/ write
     * @return object representation of successfully written board
     */
    @Override
    public Board addBoard(Board board) {
        if (board.title == null)
        {
            return null;
        }

        if(board.cardLists == null) board.cardLists = new ArrayList<>();

        else {
            //set place value of lists
            for (int i = 0; i < board.cardLists.size(); i++) {
                CardList list = board.cardLists.get(i);
                list.place = i;
                listRepo.save(list);
            }
        }
        return boardRepo.save(board);
    }

    /**
     * Update a board
     * @param changedBoard the board object to edit, with the corresponding id
     * @return the edited board
     * Gives null if the board does not exist
     * Gives null if the body is malformed
     */
    @Override
    public Board editBoard(Board changedBoard) {
        if (changedBoard==null || !boardRepo.existsById(changedBoard.id)) return null;
        Board board = boardRepo.findById(changedBoard.id).get();

        changedBoard.cardLists = board.cardLists;

        return boardRepo.save(changedBoard);
    }

    /**
     * Deletes a board with particular ID
     * @param boardId id of board to delete
     * @return deleted board
     */
    @Override
    public Board deleteBoardByID(long boardId) {
        if (boardId < 0 || !boardRepo.existsById(boardId)) {
            return null;
        }
        Board board = boardRepo.findById(boardId).get();
        boardRepo.deleteById(boardId);
        return board;
    }

    /**
     * Create a cardlist on board with id
     * @param list cardlist object to write/ create
     * @param boardId id of board to which cardlist should be attached
     * @return representation of written cardlist
     */
    @Override
    public CardList addCardList(CardList list, long boardId) {
        if(boardId < 0 || !boardRepo.existsById(boardId)) {
            return null;
        }

        Board board = boardRepo.findById(boardId).get();
        board.cardLists.add(list);
        if(list.cards == null) list.cards = new ArrayList<>();
        list.place = board.cardLists.indexOf(list);

        CardList saved = listRepo.save(list);
        boardRepo.save(board);
        return saved;
    }

    /**
     * Reorder a cardlist on a board
     * @param boardId id of the board
     * @param listId id of the cardlist
     * @param index new index of the cardlist
     * @return the updated board
     */
    @Override
    public Board reorderCardLists(long boardId, long listId, int index) {
        if(!boardRepo.existsById(boardId) || !listRepo.existsById(listId))
            return null;

        Board board = boardRepo.findById(boardId).get();
        CardList list = listRepo.findById(listId).get();

        if(!board.cardLists.contains(list)) return null;


        board.cardLists.remove(list);

        if(index >= board.cardLists.size()) board.cardLists.add(list);
        else board.cardLists.add(index, list);
        //Set place to index for all lists
        for(int i = 0; i < board.cardLists.size(); i ++)
        {
            board.cardLists.get(i).place = i;
        }

        boardRepo.save(board);
        return board;
    }
}
