package client.scenes;

import client.utils.DialogUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
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
    private VBox subtaskBox;

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

        tagsList.setCellFactory(listView -> new ListCell<Tag>() {
            @Override
            protected void updateItem(Tag tag, boolean empty) {
                super.updateItem(tag, empty);
                if (empty || tag == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(tag.title + " (" + tag.id + ")");
                    if (tag.color != null && !tag.color.isEmpty()) {
                        setBackground(new Background(new BackgroundFill(Color.web(tag.color), CornerRadii.EMPTY, Insets.EMPTY)));
                    } else {
                        setBackground(null);
                    }
                }
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


    public void setCard(Card card) throws IOException {
        this.card = card;
        poll(card.id);
    }

    public void setList(CardList list) {
        this.list = list;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setFields(Card card) throws IOException {
        titleField.setText(card.title);
        bodyField.setText(card.description);
        clearTags();
        for (Tag tag : card.tags) {
            showTag(tag);
        }
        clearSubtasks();
        for (Subtask subtask : card.subtasks) {
            showSubtask(subtask);
        }
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
        tagBox.getChildren().clear();
        mainCtrl.showOverview(board);
    }

    public void cancel() throws IOException {
        tagBox.getChildren().clear();
        mainCtrl.showOverview(board);
    }

    public void clearTags() {
        tagBox.getChildren().clear();
    }

    public void showTag(Tag tag) throws IOException {
        FXMLLoader fxml = new FXMLLoader(EditCardCtrl.class.getClassLoader().getResource(
                Path.of("client", "scenes", "Tag.fxml").toString()));
        Parent n = (Parent)fxml.load();
        TagCtrl controller = fxml.getController();
        controller.setTag(tag);
        controller.setEditCtrl(this);
        controller.setFields();
        tagBox.getChildren().add(n);
    }

    public void clearSubtasks() {
        subtaskBox.getChildren().clear();
    }

    public void showSubtask(Subtask subtask) throws IOException {
        FXMLLoader fxml = new FXMLLoader(EditCardCtrl.class.getClassLoader().getResource(
                Path.of("client", "scenes", "Subtask.fxml").toString()));
        Parent n = (Parent)fxml.load();
        SubtaskCtrl controller = fxml.getController();
        controller.setSubtask(subtask);
        controller.setEditCtrl(this);
        controller.setFields();
        subtaskBox.getChildren().add(n);
    }

    public void addTag(Tag tag) throws IOException {
        showTag(tag);
        card.tags.add(tag);
    }

    public void deleteTag(HBox tagElement, Tag tag) {
        tagBox.getChildren().remove(tagElement);
        card.tags.remove(tag);
        server.editCard(card);
    }

    public void addSubtask(Subtask subtask) throws IOException {
        showSubtask(subtask);
        card.subtasks.add(subtask);
    }

    public void newSubtask() throws IOException {
        String title = new DialogUtils().showDialog(
                "",
                "New subtask",
                "Enter the subtask title:"
        );
        if (title != null) {
            Subtask subtask = new Subtask(title);
            addSubtask(subtask);
        }
    }

    public void deleteSubtask(Node subtaskElement, Subtask subtask) {
        subtaskBox.getChildren().remove(subtaskElement);
        card.subtasks.remove(subtask);
    }
}
