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

    @FXML
    private ColorPicker listBackgroundColor;

    @FXML
    private ColorPicker listFontColor;

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
        listBackgroundColor.setValue(Color.web(board.listBackColor));
        listFontColor.setValue(Color.web(board.listFontColor));
    }

    public void submit() throws IOException {
        board.title = titleField.getText();
        board.fontColor = "#" + fontColor.getValue().toString().substring(2, 8);
        board.backColor = "#" + backgroundColor.getValue().toString().substring(2, 8);
        board.listFontColor = "#" + listFontColor.getValue().toString().substring(2, 8);
        board.listBackColor = "#" + listBackgroundColor.getValue().toString().substring(2, 8);
        server.editBoard(board);
        mainCtrl.showOverview(board);
    }

    public void cancel() throws IOException {
        mainCtrl.showOverview(board);
    }

    public void resetBoard() {
        fontColor.setValue(Color.web("#2274a5"));
        backgroundColor.setValue(Color.web("#cae2f0"));
    }

    public void resetList() {
        listFontColor.setValue(Color.web("#2274a5"));
        listBackgroundColor.setValue(Color.web("#eff6fa"));
    }

}
