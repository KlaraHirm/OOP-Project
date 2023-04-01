package client.scenes;

import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class CardCtrl {

    @FXML
    private VBox card;

    @FXML
    private Button delete_card;

    @FXML
    private Button edit_card;

    @FXML
    private Label title;

    @FXML
    private Label description;

    @FXML
    private CheckBox done;

    private Board board_object;

    private CardList list_object;

    private Card card_object;

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
     * @param list_object object of class CardList where card is
     */
    public void setList_object(CardList list_object) {
        this.list_object = list_object;
    }

    /**
     * setter for card_object
     * @param card_object   object of class Card representing this card
     */
    public void setCard_object(Card card_object) {
        this.card_object = card_object;
    }

    /**
     * setter for MainPageCtrl pageCtrl
     * @param pageCtrl object of class MainPageCtrl
     */
    public void setPageCtrl(MainPageCtrl pageCtrl) {
        this.pageCtrl = pageCtrl;
    }

    /**
     * set title which is shown in ui of card to its title with id in brackets
     */
    public void setTitle() {
        title.setText(card_object.title + " (" + card_object.id + ")");
        description.setText(card_object.description);
        done.setSelected(card_object.done);
    }

    /**
     * used as onAction to delete card
     */
    public void deleteCard() {
        pageCtrl.deleteCard(board_object, list_object, card_object, card);
    }

    /**
     * used as onAction to go to edit scene
     */
    public void showEdit() {
        pageCtrl.showEditCard(board_object, list_object, card_object);
    }

    /**
     * used as onAction to toggle the done value
     */
    public void toggleCardState() {
        pageCtrl.toggleCardState(board_object, list_object, card_object, card);
    }
}
