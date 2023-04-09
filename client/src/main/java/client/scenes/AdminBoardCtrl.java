package client.scenes;

import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;

public class AdminBoardCtrl {

    @FXML
    private VBox adminBoard;

    @FXML
    private Label title;

    private Board boardObject;

    private AdminCtrl adminCtrl;

    /**
     * Setter for boardObject
     *
     * @param boardObject - object of class Board
     */
    public void setBoardObject(Board boardObject) {
        this.boardObject = boardObject;
    }

    /**
     * Setter for adminCtrl
     *
     * @param adminCtrl - object of class AdminCtrl
     */
    public void setAdminCtrl(AdminCtrl adminCtrl) {
        this.adminCtrl = adminCtrl;
    }

    /**
     * Setter for title of the board
     * represented with the ID next to it
     */
    public void setTitle() {
        title.setText(boardObject.title + " (" + boardObject.id + ")");
    }

    /**
     * Use as onAction to delete Card
     */
    public void deleteBoard() {
        adminCtrl.deleteBoard(boardObject, adminBoard);
    }

    /**
     * Use as onAction to view board
     */
    public void viewBoard() {
        adminCtrl.viewBoard(boardObject);
    }
}
