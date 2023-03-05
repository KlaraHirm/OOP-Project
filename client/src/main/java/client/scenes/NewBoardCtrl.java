package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;

import java.net.URL;
import java.util.ResourceBundle;


public class NewBoardCtrl implements Initializable {

    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;


    @Inject
    public NewBoardCtrl(ServerUtils server, MainClientCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void addNewList() {
        mainCtrl.addList();
    }


}
