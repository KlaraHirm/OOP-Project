package client.scenes;

import commons.Board;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ListCtrl {

    @FXML
    private Button deleteList;

    @FXML
    private VBox list;

    @FXML
    private VBox listContainer;

    @FXML
    private Label listTitle;

    @FXML
    private Button newCard;

    @FXML
    private ScrollPane scrollPane;

    private Board boardObject;

    private CardList listObject;

    private MainPageCtrl pageCtrl;

    /**
     * setter for boardObject
     * @param boardObject object of class Board where list is
     */
    public void setBoardObject(Board boardObject) {
        this.boardObject = boardObject;
    }

    /**
     * setter for listObject
     * @param listObject object of class CardList representing a list
     */
    public void setListObject(CardList listObject) {
        this.listObject = listObject;
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
        listTitle.setText(listObject.title + " (" + listObject.id + ")");
    }

    /**
     * used as onAction to create new card
     * @throws IOException
     */
    public void newCard() throws IOException {
        pageCtrl.newCard(boardObject, listObject, listContainer);
    }

    /*
     * Returns the inner list container (where the cards go)
     */
    public VBox getListContainer() {
        return listContainer;
    }

    /*
     * Returns the outer list container (under the board HBox)
     */
    public VBox getList() {
        return list;
    }

    public void setScrollPaneId() {
        scrollPane.setId("scrollPane_" + listObject.id);
    }

    /**
     * used as onAction to delete current list
     */
    public void deleteList() {
        pageCtrl.deleteList(boardObject, listObject, list);
    }

    /**
     * sets id of element representing list to contain its object id
     */
    public void setListId() {
        list.setId("list_" + listObject.id);
    }

    /**
     * used as onAction to show the page to edit the list
     */
    public void showEdit() {
        pageCtrl.showEditList(boardObject, listObject);
    }

    public void setColors() {
        list.setStyle("-fx-background-color: " + boardObject.listBackColor + ";");
        listContainer.setStyle("-fx-background-color: " + boardObject.listBackColor + ";");
        listTitle.setStyle("-fx-text-fill: " + boardObject.listFontColor + ";");
    }
}
