package client.scenes;

import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javax.xml.bind.annotation.XmlType;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CardCtrl {

    @FXML
    private VBox card;

    @FXML
    private Button delete_card;

    @FXML
    private Button edit_card;

    @FXML
    private Label title;

    private Board board_object;

    private HBox board_element;

    private CardList list_object;

    private Card card_object;

    private VBox list_element;

    private MainPageCtrl pageCtrl;

    private double mouseAnchorX;
    private double mouseAnchorY;

    public void setBoard_element(HBox board_element) {
        this.board_element = board_element;
    }

    public void setList_element(VBox list_element) {
        this.list_element = list_element;
    }

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

    public void makeDraggable() {

        card.setOnMousePressed(mouseEvent -> {
            double x = card.getLayoutX();
            double y = card.getLayoutY();

            list_element.getChildren().remove(card);
            board_element.getChildren().add(card);
            card.setManaged(false);

            AnchorPane.setBottomAnchor(card, null);
            AnchorPane.setTopAnchor(card, null);
            AnchorPane.setLeftAnchor(card, null);
            AnchorPane.setRightAnchor(card, null);
            mouseAnchorX = mouseEvent.getSceneX();
            mouseAnchorY = mouseEvent.getSceneY();

            card.setLayoutX(list_element.getLayoutX()+x);
            card.setLayoutY(list_element.getLayoutY()+y);

        });

        card.setOnMouseDragged(mouseEvent -> {

            //  Calculate the button's new position
            double deltaX = mouseEvent.getSceneX() - mouseAnchorX;
            double deltaY = mouseEvent.getSceneY() - mouseAnchorY;
            double newX = card.getLayoutX() + deltaX;
            double newY = card.getLayoutY() + deltaY;

            // Update the button's position
            card.setLayoutX(newX);
            card.setLayoutY(newY);

            mouseAnchorX = mouseEvent.getSceneX();
            mouseAnchorY = mouseEvent.getSceneY();

            for (Node n : board_element.getChildren()) {
                if (n instanceof VBox) {
                    if (card.getBoundsInParent().intersects(n.getBoundsInParent())) {
                        ( (VBox) n ).setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                        break;
                    }
                }
            }

            for (Node n : board_element.getChildren()) {
                if (n instanceof VBox) {
                    if (!card.getBoundsInParent().intersects(n.getBoundsInParent())) {
                        ((VBox) n).setBackground(new Background(new BackgroundFill(Color.web("#eff6fa"), CornerRadii.EMPTY, Insets.EMPTY)));
                        break;
                    }
                }
            }

        });

        card.setOnMouseReleased(mouseEvent -> {
            for (Node n : board_element.getChildren()) {
                if (n instanceof VBox) {
                    if (card.getBoundsInParent().intersects(n.getBoundsInParent())) {
                        setList_element((VBox) n);
                        for (int indexCard = 0; indexCard < list_element.getChildren().size(); indexCard++) {
                                Node test = list_element.getChildren().get(indexCard);
                                Point p = MouseInfo.getPointerInfo().getLocation();
                                Point2D point = new Point2D(mouseAnchorX, mouseAnchorY);
                            if (indexCard == 0 && (test.contains(test.screenToLocal(point)) || test.contains(point))) {
                                board_element.getChildren().remove(card);
                                list_element.getChildren().add(1, card);
                                break;
                            }
                            if (list_element.getChildren().get(indexCard) instanceof VBox) {
                                if (test.contains(test.screenToLocal(point)) || test.contains(point)) {
                                    board_element.getChildren().remove(card);
                                    list_element.getChildren().add(indexCard, card);
                                    break;
                                }
                            }
                        }
                        if (!list_element.getChildren().contains(card)) {
                            list_element.getChildren().add(card);
                        }
                        mouseAnchorX = mouseEvent.getSceneX();
                        mouseAnchorY = mouseEvent.getSceneY();
                        card.setManaged(true);
                        }
                    }
                }
        });

    }


}
