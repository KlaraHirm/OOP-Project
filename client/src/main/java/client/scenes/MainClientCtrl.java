package client.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
     * @param board_count number of boards in application
     * @throws IOException
     */
    public void showAdd(int board_count) throws IOException {
        primaryStage.setTitle("Main Page: Adding Board");

        //load board template into main page using FXMLLoader
        AnchorPane page = (AnchorPane) overview.lookup("#main_page");
        URL location = getClass().getResource("Board.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Parent root = loader.load();
        page.getChildren().addAll(root);

        //select template board elements
        AnchorPane container = (AnchorPane) overview.lookup("#board_container");
        AnchorPane.setTopAnchor(container,130.0);
        Button newList = (Button) overview.lookup("#new_list");
        HBox box = (HBox) overview.lookup("#board");

        //rename board element ids to their specific ids
        newList.setId("new_list_"+board_count);
        container.setId("board_container_"+board_count);
        box.setId("board_"+board_count);

        //set action on click of new list
        newList.setOnAction(e->{
            try {
                addList(board_count);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Add new empty list
     * @param board_count number of boards in application
     */
    public void addList(int board_count) throws IOException {
        HBox board = (HBox) overview.lookup("#board_"+board_count);

        //load board template into main page using FXMLLoader
        URL location = getClass().getResource("List.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Parent root = loader.load();
        board.getChildren().addAll(root);
        HBox.setHgrow(root, Priority.ALWAYS);
    }
}
