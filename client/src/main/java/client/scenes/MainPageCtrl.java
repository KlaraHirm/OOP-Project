package client.scenes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;


public class MainPageCtrl implements Initializable {

    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;

    private ObservableList<Board> data;



    @Inject
    public MainPageCtrl(ServerUtils server, MainClientCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void addBoard(long board_id) throws IOException {
        mainCtrl.showAdd(board_id);
    }

    public void newBoard() throws IOException {
        Board board = new Board("Untitled");
        server.addBoard(board);
        addBoard(board.id);
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
