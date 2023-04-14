package client.scenes;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import client.utils.PreferenceUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.CardList;
import commons.Tag;
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
import javafx.util.converter.IntegerStringConverter;


public class MainPageCtrl implements Initializable {

    private final ServerUtils server;
    private final PreferenceUtils preferences;
    private final MainClientCtrl mainCtrl;

    private ObservableList<Board> data;

    private ObservableList<Tag> dataTags;

    @FXML
    private ComboBox<Board> boardsList;

    @FXML

    private ComboBox<Tag> tagsList;

    @FXML
    private AnchorPane mainPage;

    @FXML
    private Button disconnectButton;

    @FXML
    private Label connectionLabel;

    @FXML
    private TextField IDField;

    @FXML
    private Button loadBoardButton;

    private Board activeBoard;


    @Inject
    public MainPageCtrl(ServerUtils server, PreferenceUtils preferences, MainClientCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.preferences = preferences;
    }

    /**
     * Initialize method which is called after constructor, setts properties of ComboBox boardsList
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
        // convertor for boardsList (dropdown menu field with board names)
        boardsList.setConverter(new StringConverter<Board>() {

            /**
             * toString method which converts elements in boardsList to String - this is shown in UI in ComboBox
             * @param board the object of type {@code T} to convert
             * @return String representation of objects in boardsList
             */
            @Override
            public String toString(Board board) {
                return board.title + " (" + board.id + ")";
            }

            /**
             * fromString method which converts String representation back to object in boardsList
             * @param text the {@code String} to convert
             * @return actual object of String representation
             */
            @Override
            public Board fromString(String text) {
                String title = text.split(" ")[0];
                String idString = text.split(" ")[1];
                long boardId = Long.parseLong(idString.substring(1, idString.length()-1));
                return boardsList.getItems().stream().filter(b ->
                        b!=null && b.title.equals(title) && b.id == boardId).findFirst().orElse(null);
            }
        });


        //adding event listener to boards_list which calls method loadBoardContent only when new value is selected
        boardsList.valueProperty().addListener((observable, oldValue, newValue) -> {
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
        IDField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));

        refresh();
    }

    /**
     * method which loads board along with its content
     * @param selectedBoard object of class Board to be loaded
     * @throws IOException
     */
    public void loadBoardContent(Board selectedBoard) throws IOException {
        refresh();
        hideBoard(mainPage.lookup("#boardContainer"));
        AnchorPane boardContainer = (AnchorPane) showBoard(selectedBoard);
        HBox boardHbox = (HBox) boardContainer.lookup("#board");
        for(CardList list:selectedBoard.cardLists){
            VBox listContainer = (VBox) showList(selectedBoard, list, boardHbox);
            for(Card card :server.getCards(list.id)){
                showCard(selectedBoard, list, card, listContainer, boardHbox);
            }
        }
        activeBoard = selectedBoard;
    }

    /**
     * loads changes made to a board
     * @throws IOException
     */
    public void loadChange() throws IOException {
        Board updatedBoard = server.getBoard(activeBoard.id);
        loadBoardContent(updatedBoard);
    }


    /**
     * method which shows existing board
     * @param board object of class Board
     * @throws IOException
     */
    public Parent showBoard(Board board) throws IOException {
        activeBoard = board;
        URL location = getClass().getResource("Board.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Parent parent =  loader.load();
        BoardCtrl boardCtrl = loader.getController();
        boardCtrl.setPageCtrl(this);
        boardCtrl.setBoardObject(board);
        boardCtrl.setTitle();
        boardCtrl.setServer(server);
        boardCtrl.refreshTags();
        boardCtrl.setColors();
        mainPage.getChildren().addAll(parent);
        IDField.setText(Long.toString(board.id));
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
        preferences.saveBoardId(server.getServerURL(), board);
        hideBoard(mainPage.lookup("#boardContainer"));
        showBoard(board);
        refresh();
        boardsList.setValue(board);
    }

    /**
     * method which hides board from ui
     * @param n object of class Node to be hidden (board)
     */
    public void hideBoard(Node n) {
        if(n==null){
            return;
        }
        mainPage.getChildren().remove(n);
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
    public void deleteBoard(Board board, AnchorPane boardContainer) {
        server.deleteBoard(board);
        boardsList.getSelectionModel().clearSelection();
        boardsList.getItems().remove(board);
        preferences.removeBoardId(server.getServerURL(), board);
        refresh();
        hideBoard(boardContainer);
    }

    /**
     * hides a board from the board list
     * @param board board to be hidden
     * @param boardContainer the board element
     */
    public void leaveBoard(Board board, AnchorPane boardContainer) {
        boardsList.getSelectionModel().clearSelection();
        boardsList.getItems().remove(board);
        preferences.removeBoardId(server.getServerURL(), board);
        refresh();
        hideBoard(boardContainer);
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
        listCtrl.setListObject(list);
        listCtrl.setBoardObject(board);
        listCtrl.setTitle();
        listCtrl.setScrollPaneId();
        listCtrl.setListId();
        listCtrl.setColors();
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
     * @param listContainer the element corresponding to the list container (this should be the element
     *                       which has the cards as direct children)
     */
    public void deleteList(Board board, CardList list, VBox listContainer) {
        server.deleteList(board, list);
        hideList(listContainer, ((HBox)listContainer.getParent()));
        refresh();
    }

    /**
     * method which shows existing card
     * @param board object of class Board where card is
     * @param list object of class CardList where card is
     * @param card object of class Card which is to be shown
     * @param listElement the element corresponding to the list container (this should be the element
     *      which has the cards as direct children)
     * @param boardElement the element corresponding to the board container
     * @throws IOException
     */
    public void showCard(Board board, CardList list, Card card, VBox listElement, HBox boardElement) throws IOException {
        URL location = getClass().getResource("Card.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Parent parent =  loader.load();
        CardCtrl cardCtrl = loader.getController();
        cardCtrl.setPageCtrl(this);
        cardCtrl.setCardObject(card);
        cardCtrl.setListObject(list);
        cardCtrl.setBoardObject(board);
        cardCtrl.setFields();
        listElement.getChildren().addAll(parent);
        VBox.setMargin(parent, new Insets(5, 5, 5, 5));
        cardCtrl.setBoardElement(boardElement);
        cardCtrl.setListElement(listElement);
        cardCtrl.makeDraggable();
        cardCtrl.setCardId();
    }

    /**
     * onAction method which show the board with id currently in IDField
     */
    public void showBoard() throws IOException
    {
        long boardID = Integer.parseInt(IDField.getText());
        Board board = server.getBoard(boardID);
        refresh();

        if(board!=null) {
            preferences.saveBoardId(server.getServerURL(), board);
            loadBoardContent(board);
            boardsList.setValue(board);
        }
        else reset();
    }

    /**
     * method which creates new card (used as onAction)
     * @param board object of class Board where card is
     * @param list object of class CardList where card is
     * @param listElement the element corresponding to the list container (this should be the element
     *      which has the cards as direct children)
     * @throws IOException
     */
    public void newCard(Board board, CardList list, VBox listElement) throws IOException {
        Card card = new Card("Untitled");
        list.cards.add(card);
        card = server.addCard(list, card);
        showCard(board, list, card, listElement, (HBox) mainPage.lookup("#board"));
    }

    /**
     * method which hides card from ui
     * @param n object of class Node to be hidden (card)
     * @param listContainer object of class VBox which represents list where card is
     */
    public void hideCard(Node n, VBox listContainer) {
        listContainer.getChildren().remove(n);
    }


    /**
     * method which deletes card from server and ui
     * @param board object of class Board where card is - grandparent
     * @param list object of class CardList where card is - parent
     * @param card object of class Card which represents the card to be deleted
     * @param cardElement JavaFX element of the card
     */
    public void deleteCard(Board board, CardList list, Card card, VBox cardElement) {
        server.deleteCard(card, list, board);
        hideCard(cardElement, (VBox) cardElement.getParent());
        refresh();
    }

    /**
     * method which loads a scene to edit card
     * @param board object of class Board - grandparent of card
     * @param list object of class CardList - parent of card
     * @param card object of class Card which is to be edited
     */
    public void showEditCard(Board board, CardList list, Card card) throws IOException {
        mainCtrl.showEditCard(card, list, board);
    }

    /**
     * refreshes data variable
     */
    public void refresh() {
        var boards = server.getBoards();
        connectionLabel.setText(server.isConnected() ? "Connected" : "Disconnected");

        List<String> joinedBoardIDs = preferences.getJoinedBoardIds(server.getServerURL());
        List<Board> joinedBoards = new ArrayList<>();
        for (Board b : boards) {
            if (joinedBoardIDs.contains(Long.toString(b.id))) joinedBoards.add(b);
        }

        if(!server.isConnected()) {
            reset();
        } else if(!joinedBoards.equals(boardsList.getItems())) {
            data = FXCollections.observableList(joinedBoards);
            boardsList.setItems(data);
        }
    }


    /**
     * method which marks a card as done (checkbox checked) or not done (unchecked)
     * @param board object of class Board where card is - grandparent
     * @param list object of class CardList where card is - parent
     * @param card object of class Card which represents the card to be marked as done
     * @param cardElement JavaFX element of the card
     */
    public void toggleCardState(Board board, CardList list, Card card, VBox cardElement) {
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

    public void showEditTag(Tag tag, Board board) {
        mainCtrl.showEditTag(tag, board);
    }

    /**
     * loads the server connection management UI
     */
    public void changeServerConnection(){
        mainCtrl.showServer();
    }

    /**
     * loads the admin page
     */
    public void showAdminPage() {
        mainCtrl.showAdminPage();
    }


    /**
     * removes the currently showing board from list selection and main ui
     */
    public void reset(){
        hideBoard(mainPage.lookup("#boardContainer"));
        boardsList.getSelectionModel().clearSelection();
        IDField.setText("");
    }
}
