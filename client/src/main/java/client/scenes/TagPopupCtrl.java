package client.scenes;

import client.objects.TagObjectCtrl;
import client.objects.TaskObjectCtrl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.IOException;
import java.nio.file.Path;


public class TagPopupCtrl {

    static Stage windowTag;
    private static EditCardCtrl editCtrl;
    @FXML
    private TextField tagField;

    /**
     * Sets EditCardCtrl instance
     * **/
    public static void setEditCtrl(EditCardCtrl e) {
        editCtrl = e;
    }

    /**
     * displays popup window to add a tag
     * uses fxml loader to load TagPopup.fxml
     * **/
    public static void display()
    {
        try {
            Stage popupwindow=new Stage();
            windowTag=popupwindow;

            popupwindow.initModality(Modality.APPLICATION_MODAL);
            popupwindow.setTitle("Add Tag(s)");
            //gets fxml of ListObject.fxml
            FXMLLoader fxml = new FXMLLoader(EditCardCtrl.class.getClassLoader().getResource(
                    Path.of("client", "scenes", "TagPopup.fxml").toString()));
            //loads it into parent object
            Parent n = (Parent)fxml.load();
            Scene scene1= new Scene(n, 200, 250);

            popupwindow.setScene(scene1);

            popupwindow.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Submits a tag to be added to EditCard
     * **/
    public void submitTag(){
        windowTag.close();
        String name = tagField.getText();
        TagObjectCtrl.setTagLabel(name);
        editCtrl.tagSubmit();
    }
    /**
     * cancels adding a tag
     * **/
    public void cancelTag(){
        windowTag.close();
        editCtrl.tagCancel();
    }



}

