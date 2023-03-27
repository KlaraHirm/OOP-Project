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

    public void setPageCtrl(MainPageCtrl pageCtrl) {
        this.pageCtrl = pageCtrl;
    }

    public void setBoard_object(Board board_object) {
        this.board_object = board_object;
    }

    public void setTitle() {
        board_title.setText(board_object.title + " (" + board_object.id + ")");
    }

    public void deleteBoard() {
        pageCtrl.deleteBoard(board_object, board_container);
    }

    public void addNewList() throws IOException {
        pageCtrl.newList(board_object);
    }


}
