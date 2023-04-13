package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

import java.io.IOException;

public class EditBoardCtrl {

    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;

    @FXML
    private TextField titleField;

    @FXML
    private FlowPane listPane;

    @FXML
    private TextField checkField;

    @FXML
    private ColorPicker backgroundColor;

    @FXML
    private ColorPicker fontColor;

    private Board board;

    /**
     * \
     * @param server
     * @param mainCtrl
     * Sets EditCardCtrl
     */
    @Inject
    public EditBoardCtrl(ServerUtils server, MainClientCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setFields(Board board) {
        titleField.setText(board.title);
        backgroundColor.setValue(Color.web(board.backColor));
        fontColor.setValue(Color.web(board.fontColor));
    }

    public void submit() throws IOException {
        board.title = titleField.getText();
        server.editBoard(board);
        mainCtrl.showOverview(board);
    }

    public void cancel() throws IOException {
        mainCtrl.showOverview(board);
    }

}
