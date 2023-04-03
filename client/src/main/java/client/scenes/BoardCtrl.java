package client.scenes;

import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class BoardCtrl {

    @FXML
    private HBox board;

    @FXML
    private AnchorPane board_container;

    @FXML
    private Label board_title;

    @FXML
    private Button delete_board;

    @FXML
    private Button new_list;

    private MainPageCtrl pageCtrl;

    private Board board_object;

    /**
     * Setter for MainPageCtrl pageCtrl
     * @param pageCtrl object of class MainPageCtrl
     */
    public void setPageCtrl(MainPageCtrl pageCtrl) {
        this.pageCtrl = pageCtrl;
    }

    /**
     * Setter for Board board_object
     * @param board_object object of class Board which this controller represents
     */
    public void setBoard_object(Board board_object) {
        this.board_object = board_object;
    }

    /**
     * set title which is shown in ui of board to its title with id in brackets
     */
    public void setTitle() {
        board_title.setText(board_object.title + " (" + board_object.id + ")");
    }

    /**
     * used as onAction to delete board
     */
    public void deleteBoard() {
        pageCtrl.deleteBoard(board_object, board_container);
    }

    /**
     * used as onAction to add new list
     * @throws IOException
     */
    public void addNewList() throws IOException {
        pageCtrl.newList(board_object, board);
    }

    /**
     * used as onAction to show the page to edit the board
     */
    public void showEdit() {
        pageCtrl.showEditBoard(board_object);
    }
}
