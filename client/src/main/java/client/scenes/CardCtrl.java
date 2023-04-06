package client.scenes;

import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

    @FXML
    private Label description;

    @FXML
    private CheckBox done;
    private Card cardObject;

    private Board boardObject;

    private HBox boardElement;

    private CardList listObject;
    private CardList originalListObject;

    private VBox listElement;

    private MainPageCtrl pageCtrl;

    private double mouseAnchorX;
    private double mouseAnchorY;

    /**
     * Setter for boardElement
     *
     * @param boardElement - the HBox element of the Board
     */
    public void setBoardElement(HBox boardElement) {
        this.boardElement = boardElement;
    }

    /**
     * Setter for listElement
     *
     * @param listElement - the Vbox element of the List
     */
    public void setListElement(VBox listElement) {
        this.listElement = listElement;
    }

    /**
     * Setter for boardObject
     *
     * @param boardObject - object of class Board
     */
    public void setBoardObject(Board boardObject) {
        this.boardObject = boardObject;
    }

    /**
     * Setter for listObject
     *
     * @param listObject - object of class CardList
     */
    public void setListObject(CardList listObject) {
        this.listObject = listObject;
    }

    /**
     * setter for original_lis_element which is used in drag and drop to represent the origin of drag
     * @param originalListObject object of class CardList
     */
    public void setOriginalListObject(CardList originalListObject) {
        this.originalListObject = originalListObject;
    }

    /**
     * Setter for cardObject
     * @param cardObject - object of class Card
     */
    public void setCardObject(Card cardObject) {
        this.cardObject = cardObject;
    }

    /**
     * Setter for pageCtrl
     *
     * @param pageCtrl - object of class MainPageCtrl
     */
    public void setPageCtrl(MainPageCtrl pageCtrl) {
        this.pageCtrl = pageCtrl;
    }

    /**
     * Setter for title of a Card
     * represented with the ID next to it
     */
    public void setTitle() {
        title.setText(cardObject.title + " (" + cardObject.id + ")");
    }

    /**
     * Setter for (title, description, check) of a Card
     * represented with the ID next to it
     */
    public void setFields() {
        title.setText(cardObject.title + " (" + cardObject.id + ")");
        description.setText(cardObject.description);
        done.setSelected(cardObject.done);
    }

    /**
     * Use as onAction to delete Card
     */
    public void deleteCard() {
        pageCtrl.deleteCard(boardObject, listObject, cardObject, card);
    }

    /**
     * Use as onAction to go to edit Scene
     */
    public void showEdit() {
        pageCtrl.showEditCard(boardObject, listObject, cardObject);
    }

    /**
     * Drag and drop + Reorder by priority structure
     */
    public void makeDraggable() {
        card.setOnMousePressed(mouseEvent -> {
            setOriginalListObject(listObject);
            this.mousePressed();
            mouseAnchorX = mouseEvent.getSceneX();
            mouseAnchorY = mouseEvent.getSceneY();
        });
        card.setOnMouseDragged(mouseEvent -> {
            double deltaX = mouseEvent.getSceneX() - mouseAnchorX;
            double deltaY = mouseEvent.getSceneY() - mouseAnchorY;
            double newX = card.getLayoutX() + deltaX;
            double newY = card.getLayoutY() + deltaY;

            card.setLayoutX(newX);
            card.setLayoutY(newY);

            mouseAnchorX = mouseEvent.getSceneX();
            mouseAnchorY = mouseEvent.getSceneY();
            this.mouseHighlightStart();
        });
        card.setOnMouseReleased(mouseEvent -> {
            this.mouseHighlightEnd();
            for (Node list : boardElement.getChildren()) {
                if (list instanceof VBox) {
                    if (card.getBoundsInParent()
                            .intersects(list.getBoundsInParent())) {
                        setListElement((VBox) list);
                        long newListId = Long.parseLong(listElement.getId().split("_")[1]); //retrieves object id from element id
                        setListObject(pageCtrl.getList(newListId));
                        break;
                    }
                }
            }
            this.cardsIntersect();
            if (!listElement.getChildren().contains(card)) {
                listElement.getChildren().add(card);
            }
            mouseAnchorX = mouseEvent.getSceneX();
            mouseAnchorY = mouseEvent.getSceneY();
            card.setManaged(true);
        });
    }

    /**
     * Make Card draggable when pressed
     */
    public void mousePressed() {
        double x = card.getLayoutX();
        double y = card.getLayoutY();
        listElement.getChildren().remove(card);
        boardElement.getChildren().add(card);
        card.setManaged(false);

        AnchorPane.setBottomAnchor(card, null);
        AnchorPane.setTopAnchor(card, null);
        AnchorPane.setLeftAnchor(card, null);
        AnchorPane.setRightAnchor(card, null);

        card.setLayoutX(listElement.getLayoutX() + x);
        card.setLayoutY(listElement.getLayoutY() + y);
    }

    /**
     * Change colour of CardList when hover over it
     */
    public void mouseHighlightStart() {
        boolean intersectionFound = false;
        for (Node list : boardElement.getChildren()) {
            if (list instanceof VBox) {
                if (!intersectionFound && card.getBoundsInParent()
                        .intersects(list.getBoundsInParent())) {
                    ((VBox) list).setBackground(new Background(
                            new BackgroundFill(Color.WHITE,
                                    CornerRadii.EMPTY, Insets.EMPTY)));
                    intersectionFound = true;
                } else {
                    ((VBox) list).setBackground(new Background(
                            new BackgroundFill(Color.web("#eff6fa"),
                                    CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }
        }
    }


    /**
     * Return original colour to CardLists
     * when Card is added to a CardList
     */
    public void mouseHighlightEnd() {
        for (Node list : boardElement.getChildren()) {
            if (list instanceof VBox) {
                if (card.getBoundsInParent()
                        .intersects(list.getBoundsInParent())) {
                    ((VBox) list).setBackground(new Background(
                            new BackgroundFill(Color.web("#eff6fa"),
                                    CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }
        }
    }

    /**
     * Check if Card intersects with another
     * and reorder the CardList
     */
    public void cardsIntersect() {
        int size = listElement.getChildren().size();
        for (int indexCard = 0; indexCard < size; indexCard++) {
            Node aim = listElement.getChildren().get(indexCard);
            // Point testMouse = MouseInfo.getPointerInfo().getLocation();
            // Point2D mousePoint = new Point2D(mouseAnchorX, mouseAnchorY);
            if (indexCard == 0 && card.localToScene(card.getBoundsInLocal())
                    .intersects(aim.localToScene(aim.getBoundsInLocal()))) {
                listElement.getChildren().add(1, card);
                pageCtrl.reorderCard(cardObject, originalListObject, listObject, indexCard+1);
                break;
            }
            else if (aim instanceof VBox) {
                if (card.localToScene(card.getBoundsInLocal())
                        .intersects(aim.localToScene(aim.getBoundsInLocal())) || indexCard == size-1) {

                    listElement.getChildren().add(indexCard+1, card);
                    pageCtrl.reorderCard(cardObject, originalListObject, listObject, indexCard+1);
                    break;
                }
            }
        }
        boardElement.getChildren().remove(card);
    }


    /**
     * used as onAction to toggle the done value
     */
    public void toggleCardState() {
        pageCtrl.toggleCardState(boardObject, listObject, cardObject, card);
    }

    /**
     * setter for id of VBox representing card
     */
    public void setCardId() {
        card.setId("card_"+cardObject.id);
    }
}
