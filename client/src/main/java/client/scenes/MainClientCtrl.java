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


    public void initialize(Stage primaryStage, Pair<MainPageCtrl, Parent> overview,
                           Pair<EditCardCtrl, Parent> editCard) {

        this.primaryStage = primaryStage;

        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.editCardCtrl = editCard.getKey();
        this.editCard = new Scene(editCard.getValue());

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
        primaryStage.setScene(overview);
    }

    public void showEditCard(Card card) {
        primaryStage.setTitle("Edit Card");
        editCardCtrl.setTitleField(card);
        primaryStage.setScene(editCard);
    }
}
