package client.scenes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;

import static com.sun.javafx.scene.control.skin.Utils.getResource;

public class MainClientCtrl {
    private Stage primaryStage;

    private MainPageCtrl overviewCtrl;
    private Scene overview;

    private BoardCtrl boardCtrl;
    private Scene board;

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

    public void showOverview() {
        primaryStage.setTitle("Main Page");
        primaryStage.setScene(overview);
    }

    public void showAdd() throws IOException {
        primaryStage.setTitle("Main Page: Adding Board");
        AnchorPane page = (AnchorPane) overview.lookup("#main_page");
        URL location = getClass().getResource("Board.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Parent root = loader.load();
        page.getChildren().addAll(root);
        AnchorPane container = (AnchorPane) overview.lookup("#board_container");
        AnchorPane.setLeftAnchor(container,0.0);
        AnchorPane.setRightAnchor(container,0.0);
        AnchorPane.setTopAnchor(container,130.0);
        Button newList = (Button) overview.lookup("#new_list");
        newList.setOnAction(e->{
            addList();
        });
    }

    public void addList(){
        HBox board = (HBox) overview.lookup("#board");
        AnchorPane child = new AnchorPane();
        child.setStyle("-fx-background-color: #9ea0a5");
        child.getChildren().add(new Label("Title"));
        board.getChildren().add(child);
    }
}
