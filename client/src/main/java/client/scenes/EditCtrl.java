package client.scenes;

import client.objects.ListObjectCtrl;
import client.objects.TagObjectCtrl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.nio.file.Path;

public class EditCtrl {

    @FXML
    private Button addTask;
    @FXML
    private FlowPane listPane;
    @FXML
    private TextField checkField;

    @FXML
    private HBox tagBox;

    @FXML
    private void addTask() {
        try {
            FXMLLoader fxml = new FXMLLoader(EditCtrl.class.getClassLoader().getResource(
                    Path.of("client", "objects", "listObject.fxml").toString()));
            Parent n = (Parent)fxml.load();
            ListObjectCtrl controller = fxml.getController();
            controller.setName(checkField.getText());
            controller.setEditCtrl(this);
            checkField.setText("");
            listPane.getChildren().add(n);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addTag() {
        try {
            FXMLLoader fxml = new FXMLLoader(EditCtrl.class.getClassLoader().getResource(
                    Path.of("client", "objects", "tagObject.fxml").toString()));
            Parent n = (Parent)fxml.load();
            TagObjectCtrl controller = fxml.getController();
            controller.setEditCtrl(this);
            tagBox.getChildren().add(n);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(Node n) {
        listPane.getChildren().remove(n);
    }
    public void deleteTag(Node n) {
        tagBox.getChildren().remove(n);
    }
}