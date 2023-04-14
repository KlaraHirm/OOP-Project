package client.scenes;

import client.scenes.EditCardCtrl;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class TagCtrl {
    //gets box tags go in
    @FXML
    private HBox tagHBox;

    @FXML
    private Label tagLabel;

    private EditCardCtrl editCtrl;

    private Tag tag;

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    /**
     * Sets tagLabel to name
     * **/
    public void setFields(){
        tagLabel.setText(tag.title);
        tagHBox.setStyle("-fx-background-color: " + tag.backColor + ";");
        tagLabel.setStyle("-fx-text-fill: " + tag.fontColor + ";");
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
        editCtrl.deleteTag(tagHBox, tag);
    }
}
