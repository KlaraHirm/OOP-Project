package client.scenes;

import commons.Card;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;


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


    public void initialize(Stage primaryStage, Pair<MainPageCtrl, Parent> overview,
                           Pair<EditCardCtrl, Parent> editCard, Pair<ServerConnectionCtrl, Parent> serverConnection) {

        this.primaryStage = primaryStage;

        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.editCardCtrl = editCard.getKey();
        this.editCard = new Scene(editCard.getValue());

        this.serverCtrl = serverConnection.getKey();
        this.serverCon = new Scene(serverConnection.getValue());

        showOverview();
        primaryStage.show();
        overviewCtrl.refresh();



    }

    /**
     * Show main page
     */
    public void showOverview() {
        primaryStage.setTitle("Main Page");
        primaryStage.setScene(overview);
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
     * Refresh main page
     */
    public void refreshOverview() {
        overviewCtrl.refresh();
    }

    /**
     * Show edit card page
     * @param card
     */
    public void showEditCard(Card card) {
        primaryStage.setTitle("Edit Card");
        editCardCtrl.setTitleField(card);
        primaryStage.setScene(editCard);
    }

    /**
     * Reset main page board selection
     */
    public void resetOverview()
    {
        overviewCtrl.reset();
    }
}
