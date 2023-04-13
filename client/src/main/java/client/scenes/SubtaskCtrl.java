package client.scenes;

import commons.Subtask;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class SubtaskCtrl {
    //gets box tags go in

    @FXML
    private AnchorPane subtaskPane;

    @FXML
    private Label subtaskLabel;

    @FXML
    private CheckBox subtaskCheckbox;

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
        subtaskCheckbox.setSelected(subtask.done);
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
        editCtrl.deleteSubtask(subtaskPane, subtask);
    }

    public void toggleSubtaskState() {
        subtask.done = !subtask.done;
        subtaskCheckbox.setSelected(subtask.done);
    }
}
