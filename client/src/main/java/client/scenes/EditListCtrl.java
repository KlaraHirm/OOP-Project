package client.scenes;

import client.objects.TagObjectCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;

public class EditListCtrl {

    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;

    @FXML
    private TextField titleField;

    @FXML
    private FlowPane listPane;

    @FXML
    private TextField checkField;

    private CardList list;

    private Board board;

    /**
     * \
     * @param server
     * @param mainCtrl
     * Sets EditCardCtrl
     */
    @Inject
    public EditListCtrl(ServerUtils server, MainClientCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void setList(CardList list) {
        this.list = list;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setFields(CardList list) {
        titleField.setText(list.title);
    }

    public void submit() throws IOException {
        list.title = titleField.getText();
        server.editList(list);
        mainCtrl.showOverview(board);
    }

    public void cancel() throws IOException {
        mainCtrl.showOverview(board);
    }

}
