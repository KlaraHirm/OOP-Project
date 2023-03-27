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
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.nio.file.Path;

public class EditCardCtrl {

    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;

    @FXML
    private TextField titleField;

    //gets the listPane
    @FXML
    private FlowPane listPane;

    //gets text field for checkbox
    @FXML
    private TextField checkField;

    //gets tag HBox
    @FXML
    private HBox tagBox;
    @FXML
    private TextArea bodyField;

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



    //not done working on
    Card aCard;
    //CardController cardCtrl;
    long cardId;

    //not done working on
    /*
    public void setEdit(CardController cardCtrl, long cardId){
        aCard = cardCtrl.getCard(cardId);
        this.cardCtrl = cardCtrl;
        this.cardId = cardId;
        titleField.setText(aCard.title);
        //bodyField.setText(aCard.body);
        //get and set subtasks
        //get and set tags
    }
    */


    //adds task with checkbox and x button to listpane
    @FXML
    private void addTask() {
        try {
            //gets fxml of ListObject.fxml
            FXMLLoader fxml = new FXMLLoader(EditCardCtrl.class.getClassLoader().getResource(
                    Path.of("client", "objects", "TaskObject.fxml").toString()));
            //loads it into parent object
            Parent n = (Parent)fxml.load();
            //sets name using setName in listObjectCtrl
            TaskObjectCtrl controller = fxml.getController();
            controller.setName(checkField.getText());
            //makes listobjectctrl capable of using editctrl
            controller.setEditCtrl(this);
            //clears textfield
            checkField.setText("");
            //adds fxml to listpane
            listPane.getChildren().add(n);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addTag() {
        TagPopupCtrl.display();
        try {
            FXMLLoader fxml = new FXMLLoader(EditCardCtrl.class.getClassLoader().getResource(
                    Path.of("client", "objects", "TagObject.fxml").toString()));
            Parent n = (Parent)fxml.load();
            TagObjectCtrl controller = fxml.getController();
            controller.setEditCtrl(this);
            tagBox.getChildren().add(n);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(Node n) {
        //removes object from listpane
        listPane.getChildren().remove(n);
    }
    public void deleteTag(Node n) {
        //removes object from tagBox
        tagBox.getChildren().remove(n);
    }

    //not done working on
    public void submitEdit(){
        Card edited = new Card(titleField.getText());
        //edited.setBody(bodyField.getText());
        //add subTasks to subTask list
        //edited. set subtaks
        //add tags to tag list
        //edited. set tags
        //cardCtrl.editCard(edited, cardId);
    }
}
