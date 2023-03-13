package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;

public class EditCardCtrl {

    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;

    @FXML
    private TextField title_field;

    @Inject
    public EditCardCtrl(ServerUtils server, MainClientCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void setTitleField(Card card) {
        title_field.setText(card.title);
    }

    public void submit(){
        //TODO
    }
}
