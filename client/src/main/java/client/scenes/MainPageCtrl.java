package client.scenes;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import client.scenes.*;
import javafx.util.converter.IntegerStringConverter;


public class MainPageCtrl implements Initializable {

    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;

    private ObservableList<Board> data;

    @FXML
    private ComboBox<Board> boards_list;

    @FXML
    private AnchorPane main_page;
    @FXML
    private Button disconnect_button;

    @FXML
    private Label connection_label;

    @FXML
    private TextField ID_field;

    @FXML
    private Button Load_board_button;

    private Preferences preferences;

    @Inject
    public MainPageCtrl(ServerUtils server, MainClientCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.preferences = Preferences.userRoot();
    }

    /**
     * Initialize method which is called after constructor, setts properties of ComboBox boards_list
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // convertor for board_list (dropdown menu field with board names)
        boards_list.setConverter(new StringConverter<Board>() {

            /**
             * toString method which converts elements in boards_list to String - this is shown in UI in ComboBox
             * @param board the object of type {@code T} to convert
             * @return String representation of objects in boards_list
             */
            @Override
            public String toString(Board board) {
                return board.title + " (" + board.id + ")";
            }

            /**
             * fromString method which converts String representation back to object in boards_list
             * @param text the {@code String} to convert
             * @return actual object of String representation
             */
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
            if(newValue == null || oldValue == newValue){
                return;
            }
            try {
                loadBoardContent(newValue);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        //force only ints to be entered in ID_field
        ID_field.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));

        refresh();
    }

    /**
     * method which loads board along with its content
     * @param selectedBoard object of class Board to be loaded
     * @throws IOException
     */
    public void loadBoardContent(Board selectedBoard) throws IOException {
        refresh();
        hideBoard(main_page.lookup("#board_container"));
        AnchorPane board_container = (AnchorPane) showBoard(selectedBoard);
        HBox boardHbox = (HBox) board_container.lookup("#board");
        for(CardList list:selectedBoard.cardLists){
            VBox listContainer = (VBox) showList(selectedBoard, list, boardHbox);
            for(Card card :server.getCards(list.id)){
                showCard(selectedBoard, list, card, listContainer, boardHbox);
            }
        }
    }

    /**
     * method which shows existing board
     * @param board object of class Board
     * @throws IOException
     */
    public Parent showBoard(Board board) throws IOException {
        URL location = getClass().getResource("Board.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Parent parent =  loader.load();
        BoardCtrl boardCtrl = loader.getController();
        boardCtrl.setPageCtrl(this);
        boardCtrl.setBoard_object(board);
        boardCtrl.setTitle();
        main_page.getChildren().addAll(parent);
        ID_field.setText(Long.toString(board.id));
        return parent;
    }

    /**
     * method which creates new board (used as onAction) and adds id to db
     * @throws IOException
     */
    public void newBoard() throws IOException {
        if(!server.isConnected()) {
            //JOptionPane.showMessageDialog(null, "You are not connected to a server!"); freezes for some reason
            return;
        }
        Board board = new Board("Untitled");
        board = server.addBoard(board);
        saveBoardId(board);
        hideBoard(main_page.lookup("#board_container"));
        showBoard(board);
        refresh();
        boards_list.setValue(board);
    }

    /**
     * method which hides board from ui
     * @param n object of class Node to be hidden (board)
     */
    public void hideBoard(Node n) {
        if(n==null){
            return;
        }
        main_page.getChildren().remove(n);
    }

    /**
     * method which loads a scene to edit board
     * @param board object of class Board - board to be edited
     */
    public void showEditBoard(Board board) {
        mainCtrl.showEditBoard(board);
    }


    /**
     * deletes board currently shown from server and client
     * @param board board to be deleted
     */
    public void deleteBoard(Board board, AnchorPane board_container) {
        server.deleteBoard(board);
        removeBoardId(board);
        refresh();
        hideBoard(board_container);
    }

    /**
     * hides a board from the board list
     * @param board board to be hidden
     * @param board_container the board element
     */
    public void leaveBoard(Board board, AnchorPane board_container) {
        removeBoardId(board);
        refresh();
        hideBoard(board_container);
    }

    /**
     * method which shows existing list
     * @param board object of class Board where list is
     * @param list object of class CardList which is to be shown
     * @param boardElement the element corresponding to the board container
     * @throws IOException
     */
    public VBox showList(Board board, CardList list, HBox boardElement) throws IOException {
        URL location = getClass().getResource("List.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Parent parent =  loader.load();
        ListCtrl listCtrl = loader.getController();
        listCtrl.setPageCtrl(this);
        listCtrl.setList_object(list);
        listCtrl.setBoard_object(board);
        listCtrl.setTitle();
        listCtrl.setScrollPaneId();
        listCtrl.setListId();
        boardElement.getChildren().addAll(parent);
        HBox.setMargin(parent, new Insets(10, 10, 10, 10));
        refresh();
        return listCtrl.getListContainer();
    }

    /**
     * method which creates new list (used as onAction) to a board specified by id
     * @param board object of class Board where list is added
     * @param boardElement the element corresponding to the board container
     * @throws IOException
     */
    public void newList(Board board, HBox boardElement) throws IOException {
        CardList list = new CardList("Untitled");
        list = server.addList(board, list);
        refresh();
        showList(board, list, boardElement);
    }

    /**
     * method which hides list from ui
     * @param n object of class Node to be hidden (list)
     * @param board object of class HBox which represents board where list is
     */
    public void hideList(Node n, HBox board){
        board.getChildren().remove(n);
    }

    /**
     * method which loads a scene to edit list
     * @param board object of class Board - grandparent of card
     * @param list object of class CardList - list to be edited
     */
    public void showEditList(Board board, CardList list) {
        mainCtrl.showEditList(list, board);
    }


    /**
     * deletes list specified in parameters
     * @param board object of class Board where list is
     * @param list object of class CardList which is to be deleted
     * @param list_container the element corresponding to the list container (this should be the element
     *                       which has the cards as direct children)
     */
    public void deleteList(Board board, CardList list, VBox list_container) {
        server.deleteList(board, list);
        hideList(list_container, ((HBox)list_container.getParent()));
        refresh();
    }

    /**
     * method which shows existing card
     * @param board object of class Board where card is
     * @param list object of class CardList where card is
     * @param card object of class Card which is to be shown
     * @param list_element the element corresponding to the list container (this should be the element
     *      which has the cards as direct children)
     * @param board_element the element corresponding to the board container
     * @throws IOException
     */
    public void showCard(Board board, CardList list, Card card, VBox list_element, HBox board_element) throws IOException {
        URL location = getClass().getResource("Card.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Parent parent =  loader.load();
        CardCtrl cardCtrl = loader.getController();
        cardCtrl.setPageCtrl(this);
        cardCtrl.setCardObject(card);
        cardCtrl.setListObject(list);
        cardCtrl.setBoardObject(board);
        cardCtrl.setFields();
        list_element.getChildren().addAll(parent);
        VBox.setMargin(parent, new Insets(5, 5, 5, 5));
        cardCtrl.setBoardElement(board_element);
        cardCtrl.setListElement(list_element);
        cardCtrl.makeDraggable();
        cardCtrl.setCardId();
    }

    /**
     * onAction method which show the board with id currently in id_field
     */
    public void showBoard() throws IOException
    {
        long boardID = Integer.parseInt(ID_field.getText());
        Board board = server.getBoard(boardID);
        refresh();

        if(board!=null) {
            saveBoardId(board);
            loadBoardContent(board);
            boards_list.setValue(board);
        }
        else reset();
    }

    /**
     * method which creates new card (used as onAction)
     * @param board object of class Board where card is
     * @param list object of class CardList where card is
     * @param list_element the element corresponding to the list container (this should be the element
     *      which has the cards as direct children)
     * @throws IOException
     */
    public void newCard(Board board, CardList list, VBox list_element) throws IOException {
        Card card = new Card("Untitled");
        list.cards.add(card);
        card = server.addCard(list, card);
        showCard(board, list, card, list_element, (HBox) main_page.lookup("#board"));
    }

    /**
     * method which hides card from ui
     * @param n object of class Node to be hidden (card)
     * @param list_container object of class VBox which represents list where card is
     */
    public void hideCard(Node n, VBox list_container) {
        list_container.getChildren().remove(n);
    }


    /**
     * method which deletes card from server and ui
     * @param board object of class Board where card is - grandparent
     * @param list object of class CardList where card is - parent
     * @param card object of class Card which represents the card to be deleted
     * @param card_element JavaFX element of the card
     */
    public void deleteCard(Board board, CardList list, Card card, VBox card_element) {
        server.deleteCard(card, list, board);
        hideCard(card_element, (VBox) card_element.getParent());
        refresh();
    }

    /**
     * method which loads a scene to edit card
     * @param board object of class Board - grandparent of card
     * @param list object of class CardList - parent of card
     * @param card object of class Card which is to be edited
     */
    public void showEditCard(Board board, CardList list, Card card) {
        mainCtrl.showEditCard(card, board);
    }

    /**
     * refreshes data variable
     */
    public void refresh() {
        var boards = server.getBoards();
        connection_label.setText(server.isConnected() ? "Connected" : "Disconnected");

        List<String> joinedBoardIDs = Arrays.asList(
                preferences.get(server.getServerURL(), "").split(",")
        );
        List<Board> joinedBoards = new ArrayList<>();
        for (Board b : boards) {
            if (joinedBoardIDs.contains(Long.toString(b.id))) joinedBoards.add(b);
        }

        if(!server.isConnected()) {
            reset();
        } else if(!joinedBoards.equals(boards_list.getItems())) {
            data = FXCollections.observableList(joinedBoards);
            boards_list.setItems(data);
        }
    }

    /**
     * method which marks a card as done (checkbox checked) or not done (unchecked)
     * @param board object of class Board where card is - grandparent
     * @param list object of class CardList where card is - parent
     * @param card object of class Card which represents the card to be marked as done
     * @param card_element JavaFX element of the card
     */
    public void toggleCardState(Board board, CardList list, Card card, VBox card_element) {
        card.done = !card.done;
        server.editCard(card);
        refresh();
    }

    /**
     * calls server to reorder card, used during drag and drop
     * @param card object of class Card representing the card being dragged
     * @param original object of class CardList representing the origin of drag
     * @param target object of class CardList representing the target of drag
     */
    public void reorderCard(Card card, CardList original, CardList target, int cardPlace) {
        server.editCardPosition(card, original, target, cardPlace);
        refresh();
    }

    /**
     * method used to get list based on id
     * @param listId id of list
     * @return object of class CardList which had the same id as passed in listId
     */
    public CardList getList(long listId) {
        return server.getList(listId);
    }

    /**
     * loads the server connection management UI
     */
    public void changeServerConnection(){
        mainCtrl.showServer();
    }


    /**
     * removes the currently showing board from list selection and main ui
     */
    public void reset(){
        hideBoard(main_page.lookup("#board_container"));
        boards_list.getSelectionModel().clearSelection();
        ID_field.setText("");
    }


    /**
     * Saves the board id to the preferences store, so that it appears in the board list
     */
    private void saveBoardId(Board board) {
        String joinedBoardIDs = preferences.get(server.getServerURL(), "");
        if (!joinedBoardIDs.isBlank()) joinedBoardIDs += ",";
        joinedBoardIDs += board.id;
        preferences.put(server.getServerURL(), joinedBoardIDs);
    }

    /**
     * Removes the board id from the preferences store, so that it is hidden in the board list
     */
    private void removeBoardId(Board board) {
        boards_list.getSelectionModel().clearSelection();
        boards_list.getItems().remove(board);

        List<String> joinedBoards = new ArrayList<>(Arrays.asList(
                preferences.get(server.getServerURL(), "").split(",")
        ));
        joinedBoards.remove(Long.toString(board.id));
        preferences.put(server.getServerURL(), String.join(",", joinedBoards));
    }
}
