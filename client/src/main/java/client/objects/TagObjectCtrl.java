package client.objects;

import client.scenes.EditCardCtrl;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class TagObjectCtrl {
    //gets box tags go in
    @FXML
    private HBox tagHBox;

    @FXML
    private static Label tagLabel;

    private EditCardCtrl editCtrl;

    /**
     * @param name
     * Sets tagLabel to name
     * **/
    public static void setTagLabel(String name){
        tagLabel.setText(name);
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
    public void deleteTag() {
        editCtrl.deleteTag(tagHBox);
    }
}
