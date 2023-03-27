package client.scenes;

import client.objects.TaskObjectCtrl;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.IOException;
import java.nio.file.Path;


public class TagPopupCtrl {

    public static void display()
    {
        try {
            Stage popupwindow=new Stage();

            popupwindow.initModality(Modality.APPLICATION_MODAL);
            popupwindow.setTitle("This is a pop up window");
            //gets fxml of ListObject.fxml
            FXMLLoader fxml = new FXMLLoader(EditCardCtrl.class.getClassLoader().getResource(
                    Path.of("client", "scenes", "TagPopup.fxml").toString()));
            //loads it into parent object
            Parent n = (Parent)fxml.load();
            Scene scene1= new Scene(n, 300, 250);

            popupwindow.setScene(scene1);

            popupwindow.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

