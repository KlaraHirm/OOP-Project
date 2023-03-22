package client.objects;

import client.scenes.EditCtrl;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

public class TagObjectCtrl {
    //gets box tags go in
    @FXML
    private HBox tagHBox;

    private EditCtrl editCtrl;

    //sets instance of editctrl
    public void setEditCtrl(EditCtrl e) {
        this.editCtrl = e;
    }

    //sends delete to editctrl with whats getting deleted
    public void deleteTag() {
        editCtrl.deleteTag(tagHBox);
    }
}
