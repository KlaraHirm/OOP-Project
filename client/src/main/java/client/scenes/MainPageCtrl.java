package client.scenes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;


public class MainPageCtrl implements Initializable {

    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;

    private ObservableList<Board> data;

    @FXML
    private ComboBox<Board> boards_list;



    @Inject
    public MainPageCtrl(ServerUtils server, MainClientCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boards_list.setItems(data);
        boards_list.setConverter(new StringConverter<Board>() {

            @Override
            public String toString(Board board) {
                return board.title;
            }

            @Override
            public Board fromString(String title) {
                return boards_list.getItems().stream().filter(b ->
                        b.title.equals(title)).findFirst().orElse(null);
            }
        });
    }

    public void addBoard(long board_id) throws IOException {
        mainCtrl.showAdd(board_id);
        boards_list.setItems(data);
    }

    public void newBoard() throws IOException {
        Board board = new Board("Untitled");
        server.addBoard(board);
        addBoard(board.id);
        refresh();
    }

    public void addList(long board_id) throws IOException {
        mainCtrl.addList((int) board_id, (int)(Math.random()*(Integer.MAX_VALUE)));
    }

    public void addCard(int board_id, int list_id) throws IOException {
        mainCtrl.addCard(board_id, list_id, (int)(Math.random()*(Integer.MAX_VALUE)));
    }

    public void refresh() throws IOException {
        var boards = server.getBoards();
        data = FXCollections.observableList(boards);
        for(Board b:data){
            addBoard(b.id);
        }
    }

}
