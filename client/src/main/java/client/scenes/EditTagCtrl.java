package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;

public class EditTagCtrl {

    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;
    @FXML
    private TextField titleField;

    @FXML
    private ColorPicker backgroundColor;

    @FXML
    private ColorPicker fontColor;

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
        backgroundColor.setValue(Color.web(tag.backColor));
        fontColor.setValue(Color.web(tag.fontColor));
    }

    public void submit() throws IOException {
        tag.title = titleField.getText();
        tag.fontColor = "#" + fontColor.getValue().toString().substring(2, 8);
        tag.backColor = "#" + backgroundColor.getValue().toString().substring(2, 8);
        tag = server.editTag(tag);
        mainCtrl.showOverview(board);
    }

    public void cancel() throws IOException {
        mainCtrl.showOverview(board);
    }

    public void delete() throws IOException {
        server.deleteTag(tag, board.id);
        mainCtrl.showOverview(board);
    }
}
