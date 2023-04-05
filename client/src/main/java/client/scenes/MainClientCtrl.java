package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.catalina.Server;

import javax.inject.Inject;
import java.io.IOException;


/**
 * Main Controller for Application
 */
public class MainClientCtrl {
    private Stage primaryStage;

    private MainPageCtrl overviewCtrl;
    private Scene overview;  //main page

    private EditCardCtrl editCardCtrl;
    private Scene editCard;  //edit card page

    private EditListCtrl editListCtrl;
    private Scene editList; // edit list page

    private EditBoardCtrl editBoardCtrl;
    private Scene editBoard;

    private ServerUtils serverUtils;

    /**
     * constructor for MainClientCtrl for the dependency injection, never actually used
     * @param serverUtils
     */
    @Inject
    public MainClientCtrl(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }


    public void initialize(Stage primaryStage,
                           Pair<MainPageCtrl, Parent> overview,
                           Pair<EditCardCtrl, Parent> editCard,
                           Pair<EditListCtrl, Parent> editList,
                           Pair<EditBoardCtrl, Parent> editBoard
    ) throws IOException {

        this.primaryStage = primaryStage;

        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.editCardCtrl = editCard.getKey();
        this.editCard = new Scene(editCard.getValue());

        this.editListCtrl = editList.getKey();
        this.editList = new Scene(editList.getValue());

        this.editBoardCtrl = editBoard.getKey();
        this.editBoard = new Scene(editBoard.getValue());

        showOverview(null);
        primaryStage.show();
        overviewCtrl.refresh();

        serverUtils.socketInit();
    }

    /**
     * Show main page
     */
    public void showOverview(Board board) throws IOException {
        primaryStage.setTitle("Main Page");
        primaryStage.setScene(overview);
        if(board!=null){
            overviewCtrl.loadBoardContent(board);
        }

    }

    /**
     * Show server window
     */
    public void showServer() {
        primaryStage.setTitle("Server Connection");
        primaryStage.setScene(overview);
    }

    public void showEditCard(Card card, Board board) {
        primaryStage.setTitle("Edit Card");
        editCardCtrl.setFields(card);
        primaryStage.setScene(editCard);
        editCardCtrl.setCard(card);
        editCardCtrl.setBoard(board);
    }

    public void showEditList(CardList list, Board board) {
        primaryStage.setTitle("Edit List");
        editListCtrl.setFields(list);
        primaryStage.setScene(editList);
        editListCtrl.setList(list);
        editListCtrl.setBoard(board);
    }

    public void showEditBoard(Board board) {
        primaryStage.setTitle("Edit Board");
        editBoardCtrl.setFields(board);
        primaryStage.setScene(editBoard);
        editBoardCtrl.setBoard(board);
    }
}
