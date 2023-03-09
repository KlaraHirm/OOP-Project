package client.scenes;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.checkerframework.checker.units.qual.C;

public class editUI extends Application {


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Edit Card");

        HBox taskBox = new HBox();
        taskBox.setAlignment(Pos.CENTER_LEFT);

        GridPane layout = new GridPane();
        layout.setMinSize(500, 350);
        layout.setPadding(new Insets(10,10,10,10));
        layout.setVgap(5);
        layout.setHgap(5);

        TextField titleField = new TextField();
        titleField.setPromptText("title");
        titleField.setMaxWidth(200);

        TextArea infoField = new TextArea();
        infoField.setWrapText(true);
        infoField.setPromptText("info");
        infoField.setMaxWidth(480);
        infoField.setMaxHeight(100);
        infoField.setPrefHeight(Double.MAX_VALUE);
        infoField.setPrefWidth(Double.MAX_VALUE);

        TextField newTaskField = new TextField();
        newTaskField.setPromptText("new task");
        newTaskField.setMaxWidth(200);
        newTaskField.setPadding(new Insets(0,10,0,0));

        Label text = new Label("Tasks");
        text.setPadding(new Insets(10,10,0,10));

        FlowPane pane = new FlowPane(10, 10);
        pane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        pane.setMaxWidth(480);
        pane.setMaxHeight(100);
        pane.setColumnHalignment(HPos.LEFT);
        pane.setPrefHeight(Double.MAX_VALUE);
        pane.setPrefWidth(Double.MAX_VALUE);
        pane.setPrefWrapLength(100);

        Button submitButton = new Button("Submit");

        Button taskButton = new Button("Add Task");
        taskButton.setPadding(new Insets(0,10,0,10));
        taskButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String taskDis = newTaskField.getText();
                CheckBox newBox = new CheckBox(taskDis);
                newBox.setPadding(new Insets(5,10,5,5));
                Button closeIt = new Button("X");
                ToolBar newBar = new ToolBar();
                newBar.getItems().add(newBox);
                newBar.getItems().add(closeIt);
                pane.getChildren().add(newBar);
                newTaskField.setText("");
                closeIt.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        pane.getChildren().remove(newBar);
                    }
                });
            }
        });

        HBox tagBox = new HBox();
        tagBox.setAlignment(Pos.CENTER_LEFT);
        tagBox.setSpacing(10);

        TextField tagField = new TextField();
        tagField.setPromptText("new Tag");
        tagField.setMaxWidth(100);

        HBox conTag = new HBox();
        conTag.setAlignment(Pos.CENTER_LEFT);
        conTag.setSpacing(10);

        Button addTag = new Button("+");
        addTag.setMinSize(20,20);
        addTag.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String taskDis = tagField.getText();
                Label text = new Label(taskDis);
                Button closeIt = new Button("X");
                ToolBar newBar = new ToolBar();
                newBar.getItems().add(text);
                newBar.getItems().add(closeIt);
                conTag.getChildren().add(newBar);
                tagField.setText("");
                closeIt.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        conTag.getChildren().remove(newBar);
                    }
                });
            }
        });

        tagBox.getChildren().add(tagField);
        tagBox.getChildren().add(addTag);

        taskBox.getChildren().add(text);
        taskBox.getChildren().add(newTaskField);
        taskBox.getChildren().add(taskButton);

        layout.add(titleField, 0,0);
        layout.add(infoField, 0,1);
        layout.add(taskBox, 0, 2);
        layout.add(pane, 0, 3);
        layout.add(tagBox, 0, 4);
        layout.add(conTag, 0, 5);
        layout.add(submitButton, 0,6);

        Scene scene = new Scene(layout, 500, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}