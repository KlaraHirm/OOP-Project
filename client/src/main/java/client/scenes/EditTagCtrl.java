package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class EditTagCtrl {

    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;
    @FXML
    private TextField titleField;

    private Tag tag;

    private Board board;

    /**
     * \
     * @param server
     * @param mainCtrl
     * Sets EditCardCtrl
     */
    @Inject
    public EditTagCtrl(ServerUtils server, MainClientCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setFields(Tag tag) {
        titleField.setText(tag.title);
    }

    public void submit() throws IOException {
        tag.title = titleField.getText();
        tag = server.editTag(tag);
        mainCtrl.showOverview(board);
    }

    public void cancel() throws IOException {
        mainCtrl.showOverview(board);
    }

    public void delete() throws IOException {
//        server.deleteTag(tag);
        mainCtrl.showOverview(board);
    }
}
