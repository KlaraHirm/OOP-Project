package client.scenes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.Initializable;


public class MainPageCtrl implements Initializable {

    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;

//    private int boards;



    @Inject
    public MainPageCtrl(ServerUtils server, MainClientCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void addBoard() throws IOException {
//        mainCtrl.showOverview();
        mainCtrl.showAdd((int)(Math.random()*(Integer.MAX_VALUE)));
    }

    public void addList(int board_id) throws IOException {
        mainCtrl.addList(board_id, (int)(Math.random()*(Integer.MAX_VALUE)));
    }

    public void addCard(int board_id, int list_id) throws IOException {
        mainCtrl.addCard(board_id, list_id, (int)(Math.random()*(Integer.MAX_VALUE)));
    }

}
