package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainClientCtrl {
    private Stage primaryStage;

    private MainPageCtrl overviewCtrl;
    private Scene overview;

    private NewBoardCtrl addCtrl;
    private Scene add;

    public void initialize(Stage primaryStage, Pair<MainPageCtrl, Parent> overview,
                           Pair<NewBoardCtrl, Parent> add) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        showOverview();
        primaryStage.show();
    }

    public void showOverview() {
        primaryStage.setTitle("Main Page");
        primaryStage.setScene(overview);
//        overviewCtrl.refresh();
    }

    public void showAdd() {
        primaryStage.setTitle("Main Page: Adding Board");
        primaryStage.setScene(add);
//        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public void addList(){
        SplitPane board = (SplitPane) add.lookup("#board");
        AnchorPane child = new AnchorPane();
        child.setStyle("-fx-background-color: #CAE2F0");
        child.getChildren().add(new Label("Title"));
        board.getItems().add(child);
    }
}
