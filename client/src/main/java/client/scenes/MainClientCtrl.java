package client.scenes;

import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
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


    public void initialize(Stage primaryStage, Pair<MainPageCtrl, Parent> overview) {

        this.primaryStage = primaryStage;

        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

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
     * Show server window
     */
    public void showServer() {
        primaryStage.setTitle("Server Connection");
        primaryStage.setScene(overview);
    }

    /**
     * Shows board
     * @param board object of class Board to be shown
     * @throws IOException
     */
    public void showBoard(Board board) throws IOException {
        primaryStage.setTitle("Main Page: Adding Board");

        //load board template into main page using FXMLLoader
        AnchorPane page = (AnchorPane) overview.lookup("#main_page");
        Parent root = loadFXML("Board.fxml");
        page.getChildren().addAll(root);

        //select template board elements
        AnchorPane container = (AnchorPane) overview.lookup("#board_container");
        AnchorPane.setTopAnchor(container,130.0);
        Button newList = (Button) overview.lookup("#new_list");
        HBox box = (HBox) overview.lookup("#board");

        //rename board element ids to their specific ids
        newList.setId("new_list_"+board.id);
        container.setId("board_container_"+board.id);
        box.setId("board_"+board.id);

        //set action on click of new list
        newList.setOnAction(e->{
            try {
                overviewCtrl.newList(board);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * method which removes last element if it is a board
     * (used so that new board can be shown without overlaying boards on top of each other)
     */
    public void hideCurrentBoard() {
        AnchorPane page = (AnchorPane) overview.lookup("#main_page");
        Node last_element = page.getChildren().get(page.getChildren().size()-1);
        if(last_element.getId().contains("board_container_")){
            page.getChildren().remove(last_element);
        }
    }

    /**
     * Shows list
     * @param board object of class Board where list is
     * @param list  object of class CardList to be shown
     */
    public void showList(Board board, CardList list) throws IOException {
        primaryStage.setTitle("Main Page: Adding List");
        HBox board_element = (HBox) overview.lookup("#board_"+board.id);

        //load list template into board using loadFXML
        Parent root = loadFXML("List.fxml");
        board_element.getChildren().addAll(root);
        // margin of lists when added to board
        HBox.setMargin(root, new Insets(10, 10, 10, 10));
        root.autosize();
        HBox.setHgrow(root, Priority.ALWAYS);  // resizing of child elements to fit HBox

        //rename list elements to be identified by their id (for now random int generated in MainPageCtrl)
        VBox box = (VBox) overview.lookup("#list");
        Button new_card = (Button) overview.lookup("#new_card");
        box.setId("list_"+list.id);
        new_card.setId("new_card_"+list.id);

        //set action on click of new card
        new_card.setOnAction(e->{
            try {
                overviewCtrl.newCard(board, list);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Shows card
     * @param board object of class Board where card is
     * @param list object of class CardList where card is
     * @param card object of class Card to be added
     * @throws IOException
     */
    public void showCard(Board board, CardList list, Card card) throws IOException {
        primaryStage.setTitle("Main Page: Adding Card");
        VBox list_element = (VBox) overview.lookup("#list_"+list.id);

        //load card template into list using loadFXML method
        Parent root = loadFXML("Card.fxml");
        list_element.getChildren().addAll(root);
        VBox.setVgrow(root, Priority.ALWAYS); // resizing of child elements to fit VBox

        // rename card elements to be identified by their id
        //set action on click
    }

    /**
     * method which loads fxml resource using FXMLLoader and returns it as JavaFX Parent object
     * @param resource resource where fxml file is
     * @return JavaFX PArent object of fxml file
     */
    public Parent loadFXML(String resource) throws IOException {
        URL location = getClass().getResource(resource);
        FXMLLoader loader = new FXMLLoader(location);
        return loader.load();
    }


}
