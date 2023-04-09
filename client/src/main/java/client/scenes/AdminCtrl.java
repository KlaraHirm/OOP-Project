package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.io.IOException;

public class AdminCtrl {

    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;

    @FXML
    private Label connectionLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label passwordStatusLabel;

    private Board board;

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
    }

    public void loadContent() {
        connectionLabel.setText(server.isConnected() ? "Connected" : "Disconnected");

        if (passwordChecked) {
            var boards = server.getBoards();
        }
    }

    public void checkPassword() {
        String password = passwordField.getText();
        if (server.checkPassword(password)) {
            passwordChecked = true;
            passwordStatusLabel.setText("Correct !");

            // TODO
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
     * loads the server connection management UI
     */
    public void changeServerConnection(){
        mainCtrl.showServer();
    }

}
