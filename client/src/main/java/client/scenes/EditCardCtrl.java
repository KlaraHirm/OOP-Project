package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.CardList;
import commons.Tag;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;


public class EditCardCtrl implements Initializable {

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

    @FXML
    private ComboBox<Tag> tagsList;

    private ObservableList<Tag> dataTags;

    private Card card;

    private CardList list;

    private Board board;

    private Thread pollThread = null;

    /**
     * Sets EditCardCtrl
     * @param server
     * @param mainCtrl
     */
    @Inject
    public EditCardCtrl(ServerUtils server, MainClientCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // convertor for tags_list (dropdown menu field with tag names)
        tagsList.setConverter(new StringConverter<Tag>() {

            /**
             * toString method which converts elements in tags_list to String - this is shown in UI in ComboBox
             * @param tag the object of type {@code T} to convert
             * @return String representation of objects in tags_list
             */
            @Override
            public String toString(Tag tag) {
                return tag.title + " (" + tag.id + ")";
            }

            /**
             * fromString method which converts String representation back to object in tags_list
             * @param text the {@code String} to convert
             * @return actual object of String representation
             */
            @Override
            public Tag fromString(String text) {
                String title = text.split(" ")[0];
                String idString = text.split(" ")[1];
                long tagId = Long.parseLong(idString.substring(1, idString.length()-1));
                return tagsList.getItems().stream().filter(t ->
                        t!=null && t.title.equals(title) && t.id == tagId).findFirst().orElse(null);
            }
        });

        //adding event listener to tags_list which calls method loadBoardContent only when new value is selected
        tagsList.valueProperty().addListener((observable, oldValue, newValue) -> {
            // This method will only run when a new value is selected
            if(newValue == null){
                return;
            }
            try {
                addTag(newValue);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Starts the long polling to check if the card is deleted
     */
    public void poll(Long cardId) {
        if (pollThread != null) pollThread.interrupt();

        pollThread = new Thread(() -> {
            while (true) {
                if (card == null) continue;
                Boolean result = false;
                try {
                    result = server.pollCard(cardId);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (result) {
                    Platform.runLater(() -> {
                        try {
                            mainCtrl.showOverview(board);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    break;
                }
            }
        });
        pollThread.start();
    }


    public void setCard(Card card) {
        this.card = card;
        poll(card.id);
    }

    public void setList(CardList list) {
        this.list = list;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setFields(Card card) {
        titleField.setText(card.title);
        bodyField.setText(card.description);
    }

    public void setTagsList(List<Tag> tags) {
        tags.removeIf(tag -> card.tags.contains(tag));
        dataTags = FXCollections.observableList(tags);
        tagsList.setItems(dataTags);
    }

    public void deleteCard() throws IOException {
        server.deleteCard(card, list, board);
        mainCtrl.showOverview(board);
    }

    public void submit() throws IOException {
        card.title = titleField.getText();
        card.description = bodyField.getText();
        server.editCard(card);
        mainCtrl.showOverview(board);
    }

    public void cancel() throws IOException {
        mainCtrl.showOverview(board);
    }

    public void addTag(Tag tag) throws IOException {
        if(tagBox.getChildren().size() == 5) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Maximum Number of Tags Reached");
            alert.setHeaderText(null);
            alert.setContentText("You can't add more than 5 tags.");
            alert.showAndWait();
            return;
        }

        FXMLLoader fxml = new FXMLLoader(EditCardCtrl.class.getClassLoader().getResource(
                Path.of("client", "scenes", "Tag.fxml").toString()));
        Parent n = (Parent)fxml.load();
        TagCtrl controller = fxml.getController();
        controller.setTag(tag);
        controller.setEditCtrl(this);
        controller.setFields();
        tagBox.getChildren().add(n);
        //        server.addTagToCard()
        // update card
    }

}
