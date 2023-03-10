package client.scenes;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;

/**
 * Main Controller for Application
 */
public class MainClientCtrl {
    private Stage primaryStage;

    private MainPageCtrl overviewCtrl;
    private Scene overview;  //main page

    private BoardCtrl boardCtrl;
    private Scene board;  //empty board template

    private ListCtrl listCtrl;
    private Scene list;  //empty board template

    public void initialize(Stage primaryStage, Pair<MainPageCtrl, Parent> overview,
                           Pair<BoardCtrl, Parent> board, Pair<ListCtrl, Parent> list) {
        this.primaryStage = primaryStage;

        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());


        this.boardCtrl = board.getKey();
        this.board = new Scene(board.getValue());

        this.listCtrl = list.getKey();
        this.list = new Scene(list.getValue());

        showOverview();
        primaryStage.show();

    }

    /**
     * Show main page
     */
    public void showOverview() {
        primaryStage.setTitle("Main Page");
        primaryStage.setScene(overview);
    }

    /**
     * Add new empty board
     * @param board_id number of boards in application
     * @throws IOException
     */
    public void showAdd(int board_id) throws IOException {
        primaryStage.setTitle("Main Page: Adding Board");

        //load board template into main page using FXMLLoader
        AnchorPane page = (AnchorPane) overview.lookup("#main_page");
        URL location = getClass().getResource("Board.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        loader.setController(overviewCtrl); // setting controller to MainPage before adding it
        Parent root = loader.load();
        page.getChildren().addAll(root);

        //select template board elements
        AnchorPane container = (AnchorPane) overview.lookup("#board_container");
        AnchorPane.setTopAnchor(container,130.0);
        Button newList = (Button) overview.lookup("#new_list");
        HBox box = (HBox) overview.lookup("#board");

        //rename board element ids to their specific ids
        newList.setId("new_list_"+board_id);
        container.setId("board_container_"+board_id);
        box.setId("board_"+board_id);

        //set action on click of new list
        newList.setOnAction(e->{
            try {
                overviewCtrl.addList(board_id);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Add new empty list
     * @param board_id number of boards in application
     */
    public void addList(int board_id, int list_id) throws IOException {
        primaryStage.setTitle("Main Page: Adding List");
        HBox board = (HBox) overview.lookup("#board_"+board_id);

        //load list template into board using FXMLLoader
        URL location = getClass().getResource("List.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Parent root = loader.load();
        board.getChildren().addAll(root);
        // margin of lists when added to board
        HBox.setMargin(root, new Insets(10, 10, 10, 10));
        root.autosize();
        HBox.setHgrow(root, Priority.ALWAYS);  // resizing of child elements to fit HBox

        //rename list elements to be identified by their id (for now random int generated in MainPageCtrl)
        VBox box = (VBox) overview.lookup("#list");
        Button new_card = (Button) overview.lookup("#new_card");
        box.setId("list_"+list_id);
        new_card.setId("new_card_"+list_id);

        //set action on click of new card
        new_card.setOnAction(e->{
            try {
                overviewCtrl.addCard(board_id, list_id);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void addCard(int board_id, int list_id, int card_id) throws IOException {
        primaryStage.setTitle("Main Page: Adding Card");
        VBox list = (VBox) overview.lookup("#list_"+list_id);

        //load card template into list using FXMLLoader
        URL location = getClass().getResource("Card.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Parent root = loader.load();
        list.getChildren().addAll(root);
        VBox.setVgrow(root, Priority.ALWAYS); // resizing of child elements to fit VBox

        // rename card elements to be identified by their id
        //set action on click
    }
}
