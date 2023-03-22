package client.scenes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import javax.swing.event.ChangeEvent;
import javafx.beans.value.ChangeListener ;


public class MainPageCtrl implements Initializable {

    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;

    private ObservableList<Board> data;

    @FXML
    private ComboBox<Board> boards_list;

    private ChangeListener<String> changeListener;



    @Inject
    public MainPageCtrl(ServerUtils server, MainClientCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // convertor for board_list (dropdown menu field with board names)
        boards_list.setConverter(new StringConverter<Board>() {

            @Override
            public String toString(Board board) {
                return board.title + " (" + board.id + ")";
            }

            @Override
            public Board fromString(String text) {
                String title = text.split(" ")[0];
                String id_string = text.split(" ")[1];
                long board_id = Long.parseLong(id_string.substring(1, id_string.length()-1));
                return boards_list.getItems().stream().filter(b ->
                        b!=null && b.title.equals(title) && b.id == board_id).findFirst().orElse(null);
            }
        });

        //adding event listener to boards_list which calls method loadBoardContent only when new value is selected
        boards_list.valueProperty().addListener((observable, oldValue, newValue) -> {
            // This method will only run when a new value is selected
            if(newValue == null){
                return;
            }
            try {
                loadBoardContent(newValue);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

//        refresh();
    }

    public void loadBoardContent(Board selected_board) throws IOException {
        mainCtrl.hideBoard();
        addBoard(selected_board);
        for(CardList list:selected_board.cardLists){
            addList(selected_board, list);
            for(Card card:list.cards){
                addCard(selected_board, list, card);
            }
        }
    }

    /**
     * method which shows existing board
     * @param board object of class Board
     * @throws IOException
     */
    public void addBoard(Board board) throws IOException {
        refresh();
        mainCtrl.showBoard(board);
    }

    /**
     * method which creates new board (used as onAction) and adds id to db
     * @throws IOException
     */
    public void newBoard() throws IOException {
        Board board = new Board("Untitled");
        board = server.addBoard(board);
        mainCtrl.hideBoard();
        addBoard(board);
//        boards_list.setValue(board);
    }

    /**
     * deletes board currently shown from server and client
     * @param board board to be deleted
     */
    public void deleteBoard(Board board) {
        boards_list.getSelectionModel().clearSelection();
        boards_list.getItems().remove(board);
        server.deleteBoard(board);
        refresh();
        mainCtrl.hideBoard();

    }

    /**
     * method which shows existing list
     * @param board object of class Board where list is
     * @param list object of class CardList which is to be shown
     * @throws IOException
     */
    public void addList(Board board, CardList list) throws IOException {
        mainCtrl.showList(board, list);
    }

    /**
     * method which creates new list (used as onAction) to a board specified by id
     * @param board object of class Board where list is added
     * @throws IOException
     */
    public void newList(Board board) throws IOException {
        CardList list = new CardList("Untitled");
        list = server.addList(board, list);
        list.id = (long)(Math.random()*(Integer.MAX_VALUE)); //TODO - for now because controllers do not return updated object
        refresh();
        addList(board, list);
    }

    /**
     * deletes list specified in parameters
     * @param board object of class Board where list is
     * @param list object of class CardList which is to be deleted
     */
    public void deleteList(Board board, CardList list) {
        server.deleteList(board, list);
        mainCtrl.hideList(board, list);
        refresh();
    }

    /**
     * method which shows existing card
     * @param board object of class Board where card is
     * @param list object of class CardList where card is
     * @param card object of class Card which is to be shown
     * @throws IOException
     */
    public void addCard(Board board, CardList list, Card card) throws IOException {
        mainCtrl.showCard(board, list, card);
    }

    /**
     * method which creates new card (used as onAction)
     * @param board object of class Board where card is
     * @param list object of class CardList where card is
     * @throws IOException
     */
    public void newCard(Board board, CardList list) throws IOException {
        Card card = new Card("Untitled");
        card.id = (long)(Math.random()*(Integer.MAX_VALUE)); //TODO - for now because controllers do not return updated object
        list.cards.add(card);
        addCard(board, list, card);
    }

    //TODO - public void deleteCard(Card card)

    public void editCard(Board board, CardList list, Card card) {
        mainCtrl.showEditCard(card);
    }
    /**
     * refreshes data variable
     */
    public void refresh() {
        var boards = server.getBoards();
        data = FXCollections.observableList(boards);
        boards_list.setItems(data);
    }


}
