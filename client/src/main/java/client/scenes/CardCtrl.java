package client.scenes;

import commons.Board;
import commons.Card;
import commons.CardList;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.*;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;

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

    @FXML
    private HBox tags;

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
     * setter for originalListElement which is used in drag and drop to represent the origin of drag
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
        for(Tag tag:cardObject.tags) {
            Circle tagSymbol = new Circle();
            tagSymbol.setRadius(10);
            tagSymbol.setManaged(true);
            tagSymbol.setStyle("-fx-fill: " + tag.color + ";");
            tags.getChildren().add(tagSymbol);
        }
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
    public void showEdit() throws IOException {
        pageCtrl.showEditCard(boardObject, listObject, cardObject);
    }

    /**
     * Drag and drop + Reorder by priority structure
     */
    public void makeDraggable() {
        card.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                System.out.println("DOUBLE CLICK");
                pageCtrl.showEditCard(boardObject, listObject, cardObject);
                return;
            }

            System.out.println("SINGLE CLICK");
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
            boolean foundList = false;
            for (int i = 0; i < boardElement.getChildren().size(); i++) {
                Node list = boardElement.getChildren().get(i);
                if (list instanceof VBox) {
                    if (card.getBoundsInParent()
                            .intersects(list.getBoundsInParent())) {
                        setListElement((VBox) list.lookup("#listContainer"));
                        setListObject(boardObject.cardLists.get(i));
                        foundList = true;
                        break;
                    }
                }
            }
            if(foundList) {
                this.cardsIntersect();
            }
            boardElement.getChildren().remove(card);
            if (!listElement.getChildren().contains(card)) {
                listElement.getChildren().add(card);
                pageCtrl.reorderCard(cardObject, originalListObject, listObject, listObject.cards.size());
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
        card.setManaged(false);
        boardElement.getChildren().add(card);

        AnchorPane.setBottomAnchor(card, null);
        AnchorPane.setTopAnchor(card, null);
        AnchorPane.setLeftAnchor(card, null);
        AnchorPane.setRightAnchor(card, null);

        ScrollPane listScrollPane = (ScrollPane)boardElement.lookup("#scrollPane_" + listObject.id);
        VBox listParent = (VBox)listScrollPane.getParent();
        card.setLayoutX(listParent.getLayoutX() + x);
        card.setLayoutY(listParent.getLayoutY() + y + listScrollPane.getLayoutY());
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
                    if(list.lookup("#list_container") != null) {
                        ((VBox) list.lookup("#list_container")).setBackground(new Background(
                                new BackgroundFill(Color.WHITE,
                                        CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                    intersectionFound = true;
                } else {
                    ((VBox) list).setBackground(new Background(
                            new BackgroundFill(Color.web("#eff6fa"),
                                    CornerRadii.EMPTY, Insets.EMPTY)));
                    if(list.lookup("#list_container") != null) {
                        ((VBox) list.lookup("#list_container")).setBackground(new Background(
                                new BackgroundFill(Color.web("#eff6fa"),
                                        CornerRadii.EMPTY, Insets.EMPTY)));
                    }
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
                    if(list.lookup("#list_container") != null) {
                        ((VBox) list.lookup("#list_container")).setBackground(new Background(
                                new BackgroundFill(Color.web("#eff6fa"),
                                        CornerRadii.EMPTY, Insets.EMPTY)));
                    }
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
        int firstIndex = -1;
        for (int indexCard = 0; indexCard < size; indexCard++) {
            Node aim = listElement.getChildren().get(indexCard);
            // Point testMouse = MouseInfo.getPointerInfo().getLocation();
            // Point2D mousePoint = new Point2D(mouseAnchorX, mouseAnchorY);
            if (card.localToScene(card.getBoundsInLocal())
                    .intersects(aim.localToScene(aim.getBoundsInLocal()))) {
                if(firstIndex==-1){
                    firstIndex = indexCard;
                }
                else {
                    listElement.getChildren().add(indexCard, card);
                    pageCtrl.reorderCard(cardObject, originalListObject, listObject, indexCard);
                    break;
                }
            }
        }
        if(!listElement.getChildren().contains(card)) {
            if(firstIndex!=-1) {
                listElement.getChildren().add(firstIndex, card);
                pageCtrl.reorderCard(cardObject, originalListObject, listObject, firstIndex);
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
