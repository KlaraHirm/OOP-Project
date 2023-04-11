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
    private AnchorPane boardContainer;

    @FXML
    private Label boardTitle;

    @FXML
    private Button deleteBoard;

    @FXML
    private Button newList;

    private MainPageCtrl pageCtrl;

    private Board boardObject;

    /**
     * Setter for MainPageCtrl pageCtrl
     * @param pageCtrl object of class MainPageCtrl
     */
    public void setPageCtrl(MainPageCtrl pageCtrl) {
        this.pageCtrl = pageCtrl;
    }

    /**
     * Setter for Board boardObject
     * @param boardObject object of class Board which this controller represents
     */
    public void setBoardObject(Board boardObject) {
        this.boardObject = boardObject;
    }

    /**
     * set title which is shown in ui of board to its title with id in brackets
     */
    public void setTitle() {
        boardTitle.setText(boardObject.title + " (" + boardObject.id + ")");
    }

    /**
     * used as onAction to delete board
     */
    public void deleteBoard() {
        pageCtrl.deleteBoard(boardObject, boardContainer);
    }

    /**
     * used as onAction to leave board
     */
    public void leaveBoard() {
        pageCtrl.leaveBoard(boardObject, boardContainer);
    }

    /**
     * used as onAction to add new list
     * @throws IOException
     */
    public void addNewList() throws IOException {
        pageCtrl.newList(boardObject, board);
    }

    /**
     * used as onAction to show the page to edit the board
     */
    public void showEdit() {
        pageCtrl.showEditBoard(boardObject);
    }
}
