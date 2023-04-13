package client.scenes;

import commons.Subtask;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SubtaskCtrl {
    //gets box tags go in

    @FXML
    private VBox subtaskContainer;

    @FXML
    private Label subtaskLabel;

    @FXML
    private CheckBox subtaskDone;

    private EditCardCtrl editCtrl;

    private Subtask subtask;

    public void setSubtask(Subtask subtask) {
        this.subtask = subtask;
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

    }
}
