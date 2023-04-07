package client.scenes;

import commons.Board;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    /**
     * setter for board_object
     * @param board_object object of class Board where list is
     */
    public void setBoard_object(Board board_object) {
        this.board_object = board_object;
    }

    /**
     * setter for list_object
     * @param list_object object of class CardList representing a list
     */
    public void setList_object(CardList list_object) {
        this.list_object = list_object;
    }

    /**
     * setter for MainPageCtrl pageCtrl
     * @param pageCtrl object of class MainPageCtrl
     */
    public void setPageCtrl(MainPageCtrl pageCtrl) {
        this.pageCtrl = pageCtrl;
    }

    /**
     * set title which is shown in ui of list to its title with id in brackets
     */
    public void setTitle() {
        list_title.setText(list_object.title + " (" + list_object.id + ")");
    }

    /**
     * used as onAction to create new card
     * @throws IOException
     */
    public void newCard() throws IOException {
        pageCtrl.newCard(board_object, list_object, list);
    }

    /**
     * used as onAction to delete current list
     */
    public void deleteList() {
        pageCtrl.deleteList(board_object, list_object, list);
    }

    /**
     * sets id of element representing list to contain its object id
     */
    public void setListId() {
        list.setId("list_" + list_object.id);
    }

    /**
     * used as onAction to show the page to edit the list
     */
    public void showEdit() {
        pageCtrl.showEditList(board_object, list_object);
    }
}
