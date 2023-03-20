package client.objects;

import client.scenes.EditCtrl;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

public class TagObjectCtrl {
    @FXML
    private HBox tagHBox;
    @FXML
    private EditCtrl editCtrl;

    public void setEditCtrl(EditCtrl e) {
        this.editCtrl = e;
    }

    public void deleteTag() {
        editCtrl.deleteTag(tagHBox);
    }
}
