package client.scenes;

import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javax.xml.bind.annotation.XmlType;
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

    private CardList list_object;

    private Card card_object;

    private MainPageCtrl pageCtrl;

    private double mouseAnchorX;
    private double mouseAnchorY;

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

    public void makeDraggable(VBox node_card, VBox node_list, HBox node_board, AnchorPane mainpage) {

        node_card.setOnMousePressed(mouseEvent -> {
            // changing node_list to board so that z-index is lowest
            node_card.setManaged(false);
            node_list.getChildren().remove(node_card);
            node_board.getChildren().add(node_card);

            AnchorPane.setBottomAnchor(node_card, null);
            AnchorPane.setTopAnchor(node_card, null);
            AnchorPane.setLeftAnchor(node_card, null);
            AnchorPane.setRightAnchor(node_card, null);

//            for (int i = 0; i < node_board.getChildren().size(); i++) {
//                if (node_board.getChildren().get(i) instanceof VBox) {
//                    if (node_card.getBoundsInParent().intersects(node_board.getChildren().get(i).getBoundsInParent())) {
//                        node_card.setLayoutX(node_board.getLayoutX() - node_board.getChildren().get(i).getLayoutX());
//                        node_card.setLayoutY(node_board.getLayoutY() - node_board.getChildren().get(i).getLayoutY());
//                        break;
//                    }
//                }
//            }

            mouseAnchorX = mouseEvent.getSceneX() - node_card.getTranslateX();
            mouseAnchorY = mouseEvent.getSceneY() - node_card.getTranslateY();
        });

        node_card.setOnMouseDragged(mouseEvent -> {

            //  Calculate the button's new position
//            double deltaX = mouseEvent.getSceneX() - mouseAnchorX;
//            double deltaY = mouseEvent.getSceneY() - mouseAnchorY;
//            double newX = node_card.getLayoutX() + deltaX;
//            double newY = node_card.getLayoutY() + deltaY;
            node_card.setTranslateX(mouseEvent.getSceneX() - mouseAnchorX);
            node_card.setTranslateY(mouseEvent.getSceneY() - mouseAnchorY);

            // Update the button's position
//            node_card.setLayoutX(newX);
//            node_card.setLayoutY(newY);
//
//            mouseAnchorX = mouseEvent.getSceneX();
//            mouseAnchorY = mouseEvent.getSceneY();

//            for (Node n : node_board.getChildren()) {
//                if (n instanceof VBox) {
//                    while (node_card.getBoundsInParent().intersects(n.getBoundsInParent())) {
//                        ((VBox) n).setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//                    }
//                }
//            }
//            for (Node n : node_board.getChildren()) {
//                if (n instanceof VBox) {
//                    if (!node_card.getBoundsInParent().intersects(n.getBoundsInParent())) {
//                        ((VBox) n).setBackground(new Background(new BackgroundFill(Color.web("#eff6fa"), CornerRadii.EMPTY, Insets.EMPTY)));
//                        break;
//                    }
//                }
//            }
        });

        node_card.setOnMouseReleased(mouseEvent -> {
            // Check if the button is within the bounds of another VBox
            for (Node n : node_board.getChildren()) {
                if (n instanceof VBox) {
                    if (node_card.getBoundsInParent().intersects(n.getBoundsInParent())) {
                        Bounds bounds = node_card.localToScene(node_card.getBoundsInLocal());
                        //( (VBox) n ).setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                        // Remove the button from its current node_list and add it to the new node_list
                        node_board.getChildren().remove(node_card);
                        ((VBox) n).getChildren().add(node_card);
//                        node_card.toFront();
//                        mouseAnchorX = mouseEvent.getSceneX() - node_card.getTranslateX();
//                        mouseAnchorY = mouseEvent.getSceneY() - node_card.getTranslateY();

//                        node_card.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
//                        node_card.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);

                        node_card.setManaged(true); // layout of card is managed by new list (VBox)
                        break;
                    }
                }
            }

        });
    }
}
