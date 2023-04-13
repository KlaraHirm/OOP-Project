package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class BoardCtrl implements Initializable {

    @FXML
    private HBox board;

    @FXML
    private AnchorPane boardContainer;

    @FXML
    private Label boardTitle;

    @FXML
    private Button deleteBoard;

    @FXML
    private Button newList;

    private ObservableList<Tag> dataTags;

    @FXML

    private ComboBox<Tag> tagsList;

    private MainPageCtrl pageCtrl;

    private Board boardObject;

    private ServerUtils server;

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

        //adding event listener to tags_list which calls method loadBoardContent only when new value is selected
        tagsList.valueProperty().addListener((observable, oldValue, newValue) -> {
            // This method will only run when a new value is selected
            if(newValue == null){
                return;
            }
            showEditTag(newValue, boardObject);
        });
    }

    /**
     * setter for server
     * @param server ServerUtils
     */
    public void setServer(ServerUtils server) {
        this.server = server;
    }

    /**
     * Setter for MainPageCtrl pageCtrl
     * @param pageCtrl object of class MainPageCtrl
     */
    public void setPageCtrl(MainPageCtrl pageCtrl) {
        this.pageCtrl = pageCtrl;
    }

    /**
     * Setter for Board boardObject
     * @param boardObject object of class Board which this controller represents
     */
    public void setBoardObject(Board boardObject) {
        this.boardObject = boardObject;
    }

    /**
     * set title which is shown in ui of board to its title with id in brackets
     */
    public void setTitle() {
        boardTitle.setText(boardObject.title + " (" + boardObject.id + ")");
    }

    public void setColors() {
        board.setStyle("-fx-background-color: "+boardObject.backColor);
    }

    /**
     * used as onAction to delete board
     */
    public void deleteBoard() {
        pageCtrl.deleteBoard(boardObject, boardContainer);
    }

    /**
     * used as onAction to leave board
     */
    public void leaveBoard() {
        pageCtrl.leaveBoard(boardObject, boardContainer);
    }

    /**
     * used as onAction to add new list
     * @throws IOException
     */
    public void addNewList() throws IOException {
        pageCtrl.newList(boardObject, board);
    }

    /**
     * used as onAction to show the page to edit the board
     */
    public void showEdit() {
        pageCtrl.showEditBoard(boardObject);
    }

    /**
     * Refreshes tagsList
     */
    public void refreshTags() {
        var tags = server.getTags(boardObject);
        if(!tagsList.getItems().equals(tags)){
            dataTags = FXCollections.observableList(tags);
            tagsList.setItems(dataTags);
            boardObject.tags = tags;
        }
    }

    /**
     * Go to editTag scene
     * @param tag tag to edit
     * @param board board where tag is
     */
    public void showEditTag(Tag tag, Board board) {
        pageCtrl.showEditTag(tag, board);
    }

    /**
     * create new tag
     */
    public void newTag() {
        TextInputDialog dialog = new TextInputDialog("Untitled");
        dialog.setTitle("New Tag");
        dialog.setHeaderText("Enter the name of the new tag:");
        dialog.setContentText("Tag Name:");

        // Remove the question mark icon from the dialog
        dialog.getDialogPane().setGraphic(null);

        // Customize the style of the dialog
        dialog.getDialogPane().lookupButton(ButtonType.OK).setStyle("-fx-font: 16 arial; -fx-base: #567752;");
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setStyle("-fx-font: 16 arial; -fx-base: #a32222;");
        ((TextField)dialog.getEditor()).setStyle("-fx-font: 18 arial; -fx-text-fill: #2274A5; -fx-background-color: #CAE2F0;");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            String tagName = result.get();
            String randomColor = generateHexColor();
            Tag tag = new Tag(tagName, randomColor);
            tag = server.addTag(boardObject, tag);
            pageCtrl.refresh();
            refreshTags();
        }
    }

    public String generateHexColor() {
        Random random = new Random();
        // Generate random RGB values
        int r = 200 + random.nextInt(56);
        int g = 200 + random.nextInt(56);
        int b = 200 + random.nextInt(56);

        // Ensure alpha value is "ff"
        String alpha = "ff";

        // Convert RGB values to hexadecimal format

        // Return hexadecimal color code
        return String.format("#%02x%02x%02x%s", r, g, b, alpha);
    }

}
