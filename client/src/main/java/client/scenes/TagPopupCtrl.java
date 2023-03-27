package client.scenes;

import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;


public class TagPopupCtrl {

    public static void display()
    {
        Stage popupwindow=new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("This is a pop up window");


        Label label1 = new Label("Tag:");

        TextField field = new TextField();
        field.setMaxWidth(150);

        Button submit= new Button("Submit");
        Button cancel= new Button("Cancel");

        submit.setOnAction(e -> setTag(popupwindow));



        VBox layout= new VBox(10);


        layout.getChildren().addAll(label1, field, submit, cancel);

        layout.setAlignment(Pos.CENTER);

        Scene scene1= new Scene(layout, 300, 250);

        popupwindow.setScene(scene1);

        popupwindow.showAndWait();

    }

    public static void setTag(Stage popupwindow){

        popupwindow.close();
    }

}

