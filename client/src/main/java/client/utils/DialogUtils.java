package client.utils;

import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class DialogUtils {

    public String showDialog(String initialText, String title, String header) {
        TextInputDialog dialog = new TextInputDialog(initialText);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        // Remove the question mark icon from the dialog
        dialog.getDialogPane().setGraphic(null);

        // Customize the style of the dialog
        dialog.getDialogPane().lookupButton(ButtonType.OK).setStyle(
                "-fx-text-fill: #2274a5;"
        );
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setStyle(
                "-fx-text-fill: #a32222;"
        );
        ((TextField)dialog.getEditor()).setStyle("-fx-text-fill: #2274A5; -fx-background-color: #CAE2F0;");

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
