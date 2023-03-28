package client.objects;

import client.scenes.EditCardCtrl;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

public class TaskObjectCtrl {
    //gets checkbox
    @FXML
    private CheckBox name;

    //gets HBox
    @FXML
    private HBox listHBox;
    private EditCardCtrl editCtrl;

    /**
     * sets name using .setText
     * **/
    public void setName(String name) {
        this.name.setText(name);
    }

    /**
     * Sets instance of editCardCtrl
     * **/
    public void setEditCtrl(EditCardCtrl e) {
        this.editCtrl = e;
    }

    /**
     * deleteTask is onAction method for a button
     * calls deleteTask in editCtrl
     * **/
    public void deleteTask() {
        //sends to editCtrl
        editCtrl.deleteTask(listHBox);
    }
}
