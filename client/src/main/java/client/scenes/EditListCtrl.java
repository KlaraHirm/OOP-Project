package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

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
