package client.objects;

import client.scenes.EditCardCtrl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TagObjectCtrl {
    //gets box tags go in
    @FXML
    private HBox tagHBox;

    @FXML
    private Label tagLabel;

    private EditCardCtrl editCtrl;

    public TagObjectCtrl() {

    }

    /**
     * @param name
     * Sets tagLabel to name
     * **/
    public void setTagLabel(String name){
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
