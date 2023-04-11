package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.CardList;
import commons.Tag;
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

    private ServerConnectionCtrl serverCtrl;
    private Scene serverCon; //server connection page

    private EditListCtrl editListCtrl;
    private Scene editList; // edit list page

    private EditBoardCtrl editBoardCtrl;
    private Scene editBoard;

    private EditTagCtrl editTagCtrl;
    private Scene editTag;
    private AdminCtrl adminCtrl;
    private Scene admin;

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
                           Pair<EditBoardCtrl, Parent> editBoard,
                           Pair<EditTagCtrl, Parent> editTag,
                           Pair<ServerConnectionCtrl, Parent> serverConnection,
                           Pair<AdminCtrl, Parent> admin
    ) throws IOException {

        this.primaryStage = primaryStage;

        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.editCardCtrl = editCard.getKey();
        this.editCard = new Scene(editCard.getValue());

        this.serverCtrl = serverConnection.getKey();
        this.serverCon = new Scene(serverConnection.getValue());

        this.editListCtrl = editList.getKey();
        this.editList = new Scene(editList.getValue());

        this.editBoardCtrl = editBoard.getKey();
        this.editBoard = new Scene(editBoard.getValue());

        this.editTagCtrl = editTag.getKey();
        this.editTag = new Scene(editTag.getValue());

        showServer();
        this.adminCtrl = admin.getKey();
        this.admin = new Scene(admin.getValue());

        showServer();
        primaryStage.show();
        overviewCtrl.refresh();
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
        primaryStage.setScene(serverCon);
        serverCtrl.setUIValues();
    }

    /**
     * Init websocket connection
     */
    public void socketInit() {
        serverUtils.socketInit(overviewCtrl);
    }

    /**
     * Refresh main page
     */
    public void refreshOverview() {
        overviewCtrl.refresh();
    }

    /**
     * Show edit card page
     * @param card card to edit
     * @param board that the card belongs to
     */
    public void showEditCard(Card card, CardList list, Board board) {
        primaryStage.setTitle("Edit Card");
        primaryStage.setScene(editCard);
        editCardCtrl.setFields(card);
        editCardCtrl.setCard(card);
        editCardCtrl.setBoard(board);
        editCardCtrl.setList(list);
    }

    /**
     * Shows the admin page
     */
    public void showAdminPage() {
        primaryStage.setTitle("Admin Page");
        primaryStage.setScene(admin);
        adminCtrl.loadContent();
    }

    /**
     * Reset main page board selection
     */
    public void resetOverview()
    {
        overviewCtrl.reset();
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

    public void showEditTag(Tag tag, Board board) {
        primaryStage.setTitle("Edit Tag");
        editTagCtrl.setFields(tag);
        primaryStage.setScene(editTag);
        editTagCtrl.setBoard(board);
    }
}
