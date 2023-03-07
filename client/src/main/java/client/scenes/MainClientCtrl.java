package client.scenes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;

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

    public void showAdd() {
        primaryStage.setTitle("Main Page: Adding Board");
        AnchorPane page = (AnchorPane) overview.lookup("#main_page");
        AnchorPane empty_board = (AnchorPane) board.lookup("#empty_board");
        AnchorPane.setBottomAnchor(page,0.0);
        page.getChildren().add(empty_board);
        Button newList = (Button) overview.lookup("#new_list");
        newList.setOnAction(e->{
            addList();
        });
    }

    public void addList(){
        SplitPane board = (SplitPane) overview.lookup("#board");
        AnchorPane child = new AnchorPane();
        child.setStyle("-fx-background-color: #CAE2F0");
        child.getChildren().add(new Label("Title"));
        board.getItems().add(child);
    }
}
