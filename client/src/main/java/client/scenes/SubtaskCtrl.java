package client.scenes;

import client.utils.DialogUtils;
import commons.Card;
import commons.Subtask;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;

public class SubtaskCtrl {
    //gets box tags go in

    @FXML
    private VBox subtaskContainer;

    @FXML
    private Label subtaskLabel;

    @FXML
    private CheckBox subtaskDone;

    private VBox subtaskList;

    private AnchorPane page;

    private EditCardCtrl editCtrl;

    private Subtask subtask;

    private Card card;

    private double mouseAnchorX;
    private double mouseAnchorY;

    public void setSubtask(Subtask subtask) {
        this.subtask = subtask;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * Sets subtaskLabel to name and subtaskCheckbox to done
     * **/
    public void setFields(){
        subtaskLabel.setText(subtask.title);
        subtaskDone.setSelected(subtask.done);
    }

    /**
     * @param e
     * Sets instance of editCardCtrl
     * **/
    public void setEditCtrl(EditCardCtrl e) {
        this.editCtrl = e;
    }

    /**
     * sends delete to editctrl with whats getting deleted
     * **/
    public void deleteSubtask() {
        editCtrl.deleteSubtask(subtaskContainer, subtask);
    }

    public void setSubtaskList(VBox subtaskList) {
        this.subtaskList = subtaskList;
    }

    public void setPage(AnchorPane page) {
        this.page = page;
    }

    /**
     * Toggles the subtask done state
     */
    public void toggleSubtaskState() {
        subtask.done = !subtask.done;
        subtaskDone.setSelected(subtask.done);
    }

    /**
     * Shows the edit screen for the subtask
     */
    public void showEdit() {
        // TODO: Use dependency injection
        String title = new DialogUtils().showDialog(
                subtask.title,
                "Edit subtask",
                "Enter the subtask title:"
        );
        if (title != null) {
            subtask.title = title;
            subtaskLabel.setText(title);
        }
    }

    public void makeDraggable() {
        subtaskContainer.setOnMousePressed(mouseEvent -> {
            this.mousePressed();
            mouseAnchorX = mouseEvent.getSceneX();
            mouseAnchorY = mouseEvent.getSceneY();
        });
        subtaskContainer.setOnMouseDragged(mouseEvent -> {
            double deltaX = mouseEvent.getSceneX() - mouseAnchorX;
            double deltaY = mouseEvent.getSceneY() - mouseAnchorY;
            double newX = subtaskContainer.getLayoutX() + deltaX;
            double newY = subtaskContainer.getLayoutY() + deltaY;

            subtaskContainer.setLayoutX(newX);
            subtaskContainer.setLayoutY(newY);

            mouseAnchorX = mouseEvent.getSceneX();
            mouseAnchorY = mouseEvent.getSceneY();
        });
        subtaskContainer.setOnMouseReleased(mouseEvent -> {
            this.cardsIntersect();
            page.getChildren().remove(subtaskContainer);
            if (!subtaskList.getChildren().contains(subtaskContainer)) {
                subtaskList.getChildren().add(subtaskContainer);
                editCtrl.reorderSubtask(subtask, card.subtasks.size()+1);
            }
            mouseAnchorX = mouseEvent.getSceneX();
            mouseAnchorY = mouseEvent.getSceneY();
            subtaskContainer.setManaged(true);
        });
    }

    /**
     * Make Card draggable when pressed
     */
    public void mousePressed() {
        double x = subtaskContainer.getLayoutX();
        double y = subtaskContainer.getLayoutY();
        subtaskList.getChildren().remove(subtaskContainer);
        subtaskContainer.setManaged(false);
        page.getChildren().add(subtaskContainer);

        AnchorPane.setBottomAnchor(subtaskContainer, null);
        AnchorPane.setTopAnchor(subtaskContainer, null);
        AnchorPane.setLeftAnchor(subtaskContainer, null);
        AnchorPane.setRightAnchor(subtaskContainer, null);

        ScrollPane listScrollPane = (ScrollPane)page.lookup("#scrollPane");
        subtaskContainer.setLayoutX(page.getLayoutX() + x);
        subtaskContainer.setLayoutY(page.getLayoutY() + y + listScrollPane.getLayoutY());
    }

    /**
     * Check if Card intersects with another
     * and reorder the CardList
     */
    public void cardsIntersect() {
        int size = subtaskList.getChildren().size();
        int firstIndex = -1;
        for (int indexCard = 0; indexCard < size; indexCard++) {
            Node aim = subtaskList.getChildren().get(indexCard);
            if (subtaskContainer.localToScene(subtaskContainer.getBoundsInLocal())
                    .intersects(aim.localToScene(aim.getBoundsInLocal()))) {
                if(firstIndex==-1){
                    firstIndex = indexCard;
                }
                else {
                    subtaskList.getChildren().add(indexCard, subtaskContainer);
                    editCtrl.reorderSubtask(subtask, indexCard);
                    break;
                }
            }
        }
        if(!subtaskList.getChildren().contains(subtaskContainer)) {
            if(firstIndex!=-1) {
                subtaskList.getChildren().add(firstIndex, subtaskContainer);
                editCtrl.reorderSubtask(subtask, firstIndex);
            }
        }
        page.getChildren().remove(subtaskContainer);
    }
}
