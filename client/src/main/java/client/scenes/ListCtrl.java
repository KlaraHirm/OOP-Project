package client.scenes;

import client.utils.ServerUtilsOld;
import com.google.inject.Inject;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ListCtrl implements Initializable {

    private final ServerUtilsOld server;
    private final MainClientCtrl mainCtrl;


    @Inject
    public ListCtrl(ServerUtilsOld server, MainClientCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
