package client.scenes;

import commons.Board;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ListCtrl {

    @FXML
    private Button delete_list;

    @FXML
    private VBox list;

    @FXML
    private Label list_title;

    @FXML
    private Button new_card;

    private Board board_object;

    private CardList list_object;

    private MainPageCtrl pageCtrl;

    public void setBoard_object(Board board_object) {
        this.board_object = board_object;
    }

    public void setList_object(CardList list_object) {
        this.list_object = list_object;
    }

    public void setPageCtrl(MainPageCtrl pageCtrl) {
        this.pageCtrl = pageCtrl;
    }

    public void setTitle() {
        list_title.setText(list_object.title + " (" + list_object.id + ")");
    }

    public void newCard() throws IOException {
        pageCtrl.newCard(board_object, list_object, list);
    }

    public void deleteList() {
        pageCtrl.deleteList(board_object, list_object, list);
    }
}
