package client.scenes;

import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class CardCtrl {

    @FXML
    private VBox card;

    @FXML
    private Button deleteCard;

    @FXML
    private Button editCard;

    @FXML
    private Label title;

    private Board boardObject;

    private HBox boardElement;

    private CardList listObject;

    private Card cardObject;

    private VBox listElement;

    private MainPageCtrl pageCtrl;

    private double mouseAnchorX;
    private double mouseAnchorY;

    public void setBoard_element(HBox boardElement) {
        this.boardElement = boardElement;
    }

    public void setList_element(VBox listElement) {
        this.listElement = listElement;
    }

    /**
     * setter for boardObject
     * @param boardObject object of class Board where list is
     */
    public void setBoard_object(Board boardObject) {
        this.boardObject = boardObject;
    }

    /**
     * setter for listObject
     * @param listObject object of class CardList where card is
     */
    public void setList_object(CardList listObject) {
        this.listObject = listObject;
    }

    /**
     * setter for cardObject
     * @param cardObject object of class Card representing this card
     */
    public void setCard_object(Card cardObject) {
        this.cardObject = cardObject;
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
        title.setText(cardObject.title + " (" + cardObject.id + ")");
    }

    /**
     * used as onAction to delete card
     */
    public void deleteCard() {
        pageCtrl.deleteCard(boardObject, listObject, cardObject, card);
    }

    /**
     * used as onAction to go to edit scene
     */
    public void showEdit() {
        pageCtrl.showEditCard(boardObject, listObject, cardObject);
    }


    public void makeDraggable() {

        card.setOnMousePressed(mouseEvent -> {
            double x = card.getLayoutX();
            double y = card.getLayoutY();

            listElement.getChildren().remove(card);
            boardElement.getChildren().add(card);
            card.setManaged(false);

            AnchorPane.setBottomAnchor(card, null);
            AnchorPane.setTopAnchor(card, null);
            AnchorPane.setLeftAnchor(card, null);
            AnchorPane.setRightAnchor(card, null);
            mouseAnchorX = mouseEvent.getSceneX();
            mouseAnchorY = mouseEvent.getSceneY();

            card.setLayoutX(listElement.getLayoutX() + x);
            card.setLayoutY(listElement.getLayoutY() + y);

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

            for (Node targetList : boardElement.getChildren()) {
                if (targetList instanceof VBox) {
                    if (card.getBoundsInParent().intersects(targetList.getBoundsInParent())) {
                        ((VBox) targetList).setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                    } else {
                        ((VBox) targetList).setBackground(new Background(new BackgroundFill(Color.web("#eff6fa"), CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                }
            }

        });

        card.setOnMouseReleased(mouseEvent -> {
            for (Node targetList : boardElement.getChildren()) {
                if (targetList instanceof VBox) {
                    if (card.getBoundsInParent().intersects(targetList.getBoundsInParent())) {
                        ((VBox) targetList).setBackground(new Background
                                (new BackgroundFill(Color.web("#eff6fa"), CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                }
            }
            int size = listElement.getChildren().size();
            for (Node targetList : boardElement.getChildren()) {
                if (targetList instanceof VBox) {
                    if (card.getBoundsInParent().intersects(targetList.getBoundsInParent())) {
                        setList_element((VBox) targetList);
                        for (int indexCard = 0; indexCard < size; indexCard++) {
                            Node targetCard = listElement.getChildren().get(indexCard);
                            // Point testMouse = MouseInfo.getPointerInfo().getLocation();
                            Point2D mousePoint = new Point2D(mouseAnchorX, mouseAnchorY);
                            if (indexCard == 0 && (targetCard.contains(targetCard.screenToLocal(mousePoint)) || targetCard.contains(mousePoint))) {
                                boardElement.getChildren().remove(card);
                                listElement.getChildren().add(1, card);
                                break;
                            }
                            if (listElement.getChildren().get(indexCard) instanceof VBox) {
                                if (targetCard.contains(targetCard.screenToLocal(mousePoint)) || targetCard.contains(mousePoint)) {
                                    boardElement.getChildren().remove(card);
                                    listElement.getChildren().add(indexCard, card);
                                    break;
                                }
                            }
                        }
                        if (!listElement.getChildren().contains(card)) {
                            listElement.getChildren().add(card);
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
