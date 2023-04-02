package client.scenes;

import client.objects.TagObjectCtrl;
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
    @FXML
    private CheckBox urgentTag;
    @FXML
    private CheckBox tag1;
    @FXML
    private CheckBox tag2;
    @FXML
    private CheckBox tag3;



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
        if(!name.equals("")){
            editCtrl.createNewTag(name);
        }
        if(urgentTag.isSelected()){
            editCtrl.createNewTag("Urgent");
        }
        if(tag1.isSelected()){
            editCtrl.createNewTag("Tag 1");
        }
        if(tag2.isSelected()){
            editCtrl.createNewTag("Tag 2");
        }
        if(tag3.isSelected()){
            editCtrl.createNewTag("Tag 3");
        }
    }
    /**
     * cancels adding a tag
     * **/
    public void cancelTag(){
        popupWindow.close();
    }
}

