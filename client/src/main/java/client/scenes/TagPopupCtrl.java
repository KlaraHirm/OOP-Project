package client.scenes;

import client.objects.TagObjectCtrl;
import client.objects.TaskObjectCtrl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;


public class TagPopupCtrl {

    private Stage popupWindow;
    private EditCardCtrl editCtrl;
    @FXML
    private TextField tagField;
    @FXML
    private AnchorPane root;


    /**
     * @param e
     * Sets EditCardCtrl instance
     * **/
    public void setEditCtrl(EditCardCtrl e) {
        editCtrl = e;
        this.popupWindow = (Stage)root.getScene().getWindow();
    }


    /**
     * Submits a tag to be added to EditCard
     * **/
    public void submitTag(){
        popupWindow.close();
        String name = tagField.getText();
        editCtrl.createNewTag(name);
    }
    /**
     * cancels adding a tag
     * **/
    public void cancelTag(){
        popupWindow.close();
    }
}

