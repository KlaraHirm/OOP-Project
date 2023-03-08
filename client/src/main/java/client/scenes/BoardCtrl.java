package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for Board.fxml scene
 */
public class BoardCtrl implements Initializable {

    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;

    private final MainPageCtrl pageCtrl;


    @Inject
    public BoardCtrl(ServerUtils server, MainClientCtrl mainCtrl, MainPageCtrl pageCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.pageCtrl = pageCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void addBoard() throws IOException {
        pageCtrl.addBoard();
    }

}
