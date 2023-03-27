package client.objects;

import client.scenes.EditCardCtrl;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class TagObjectCtrl {
    //gets box tags go in
    @FXML
    private HBox tagHBox;

    private EditCardCtrl editCtrl;

    //sets instance of editctrl
    public void setEditCtrl(EditCardCtrl e) {
        this.editCtrl = e;
    }

    //sends delete to editctrl with whats getting deleted
    public void deleteTag() {
        editCtrl.deleteTag(tagHBox);
    }
}
