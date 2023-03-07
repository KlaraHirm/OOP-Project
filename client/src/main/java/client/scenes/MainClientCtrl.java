package client.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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

    public void initialize(Stage primaryStage, Pair<MainPageCtrl, Parent> overview,
                           Pair<BoardCtrl, Parent> board) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());


        this.boardCtrl = board.getKey();
        this.board = new Scene(board.getValue());

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
     * @throws IOException
     */
    public void showAdd(int board_count) throws IOException {
        primaryStage.setTitle("Main Page: Adding Board");
        AnchorPane page = (AnchorPane) overview.lookup("#main_page");
        URL location = getClass().getResource("Board.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Parent root = loader.load();
        page.getChildren().addAll(root);
        AnchorPane container = (AnchorPane) overview.lookup("#board_container");
        AnchorPane.setTopAnchor(container,130.0);
        container.setId("board_container_"+board_count);
        Button newList = (Button) overview.lookup("#new_list");
        newList.setId("new_list_"+board_count);
        HBox box = (HBox) overview.lookup("#board");
        box.setId("board_"+board_count);
        newList.setOnAction(e->{
            addList(board_count);
        });
    }

    /**
     * Add new empty list
     */
    public void addList(int board_count){
        HBox board = (HBox) overview.lookup("#board_"+board_count);
        AnchorPane child = new AnchorPane();
        child.setStyle("-fx-background-color: #9ea0a5");
        child.getChildren().add(new Label("Title"));
        board.getChildren().add(child);
    }
}
