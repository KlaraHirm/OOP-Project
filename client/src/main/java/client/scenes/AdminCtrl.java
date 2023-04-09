package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

public class AdminCtrl {

    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;

    @FXML
    private Label connectionLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label passwordStatusLabel;

    @FXML
    private VBox boardListContainer;

    private Preferences preferences;

    private boolean passwordChecked = false;

    /**
     * \
     * @param server
     * @param mainCtrl
     * Sets EditCardCtrl
     */
    @Inject
    public AdminCtrl(ServerUtils server, MainClientCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.preferences = Preferences.userRoot();
    }

    public void loadContent() {
        connectionLabel.setText(server.isConnected() ? "Connected" : "Disconnected");

        if (passwordChecked) {
            List<Board> boards = server.getBoards();
            boardListContainer.getChildren().clear();
            for (Board board : boards) {
                try {
                    showBoard(board);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void checkPassword() {
        String password = passwordField.getText();
        if (server.checkPassword(password)) {
            passwordChecked = true;
            passwordStatusLabel.setText("Correct !");
            loadContent();
        } else {
            passwordChecked = false;
            passwordStatusLabel.setText("Wrong password");
        }
    }

    /**
     * goes back to the main page of the app
     */
    public void showMainPage() {
        try {
            mainCtrl.showOverview(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * loads main page showing a board
     * @param board board to show
     */
    public void viewBoard(Board board) {
        try {
            mainCtrl.showOverview(board);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * deletes board currently shown from server and client
     * @param board board to be deleted
     * @param boardElement the VBox of the board
     */
    public void deleteBoard(Board board, VBox boardElement) {
        server.deleteBoard(board);
        removeBoardFromPreferences(board);
        hideBoard(boardElement);
        loadContent();
    }

    /**
     * loads the server connection management UI
     */
    public void changeServerConnection(){
        mainCtrl.showServer();
    }

    /**
     * method which shows existing board
     * @param board object of class Board to show
     * @throws IOException
     */
    public void showBoard(Board board) throws IOException {
        URL location = getClass().getResource("AdminBoard.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Parent parent =  loader.load();
        AdminBoardCtrl adminBoardCtrl = loader.getController();
        adminBoardCtrl.setBoardObject(board);
        adminBoardCtrl.setAdminCtrl(this);
        adminBoardCtrl.setTitle();
        boardListContainer.getChildren().addAll(parent);
        HBox.setMargin(parent, new Insets(10, 10, 10, 10));
    }

    /**
     * Removes the board id from the preferences store, so that it is hidden in the board list
     * @param board the board to remove
     */
    private void removeBoardFromPreferences(Board board) {
        List<String> joinedBoards = new ArrayList<>(Arrays.asList(
                preferences.get(server.getServerURL(), "").split(",")
        ));
        joinedBoards.remove(Long.toString(board.id));
        preferences.put(server.getServerURL(), String.join(",", joinedBoards));
    }

    /**
     * Hides a board from the list
     * @param n the board element to hide
     */
    private void hideBoard(Node n) {
        if (n != null) boardListContainer.getChildren().remove(n);
    }

}
