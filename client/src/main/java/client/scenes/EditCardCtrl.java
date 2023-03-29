package client.scenes;

import client.objects.TagObjectCtrl;
import client.objects.TaskObjectCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;

public class EditCardCtrl {

    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;

    @FXML
    private TextField titleField;

    @FXML
    private FlowPane listPane;

    @FXML
    private TextField checkField;

    @FXML
    private HBox tagBox;
    @FXML
    private TextArea bodyField;

    /**
     * \
     * @param server
     * @param mainCtrl
     * Sets EditCardCtrl
     */
    @Inject
    public EditCardCtrl(ServerUtils server, MainClientCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void setTitleField(Card card) {
        titleField.setText(card.title);
    }

    public void submit(){
        mainCtrl.showOverview();
    }
    @FXML
    private Button addTask;



    /**
     * adds a subtask to the listPane
     * uses fxml loader to load TaskObject.fxml
     * **/
    @FXML
    private void addTask() {
        try {
            FXMLLoader fxml = new FXMLLoader(EditCardCtrl.class.getClassLoader().getResource(
                    Path.of("client", "objects", "TaskObject.fxml").toString()));
            Parent n = (Parent)fxml.load();
            TaskObjectCtrl controller = fxml.getController();
            controller.setName(checkField.getText());
            controller.setEditCtrl(this);
            checkField.setText("");
            listPane.getChildren().add(n);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * adds a tag using fxml loader
     * loads TagObject.fxml
     * calls display on TagPopup
     * **/
    @FXML
    private void addTag() {
        try {
            Stage popupwindow = new Stage();
            popupwindow.initModality(Modality.APPLICATION_MODAL);
            popupwindow.setTitle("Add Tag(s)");

            //gets fxml of ListObject.fxml
            FXMLLoader fxml = new FXMLLoader(EditCardCtrl.class.getClassLoader().getResource(
                    Path.of("client", "scenes", "TagPopup.fxml").toString()));

            Scene popUpScene = new Scene((Parent)fxml.load(), 200, 250);
            popupwindow.setScene(popUpScene);

            //loads it into parent object
            TagPopupCtrl popupCtrl = fxml.getController();
            popupCtrl.setEditCtrl(this);

            popupwindow.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param n
     * deletes object from listpane
     * **/
    public void deleteTask(Node n) {
        listPane.getChildren().remove(n);
    }

    public void createNewTag(String name) {
        try {
            FXMLLoader fxml = new FXMLLoader(EditCardCtrl.class.getClassLoader().getResource(
                    Path.of("client", "objects", "TagObject.fxml").toString()));
            Parent n = (Parent)fxml.load();
            TagObjectCtrl controller = fxml.getController();
            controller.setEditCtrl(this);
            controller.setTagLabel(name);
            tagBox.getChildren().add(n);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * deletes object from tagBox
     * **/
    public void deleteTag(Node n) {
        //removes object from tagBox
        tagBox.getChildren().remove(n);
    }

}
