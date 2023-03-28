package client.scenes;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


public class DraggableCtrl {

    private double mouseAnchorX;
    private double mouseAnchorY;

    public void makeDraggable(VBox node_card, VBox node_list, HBox node_board) {

        node_card.setOnMousePressed(mouseEvent -> {
            // changing node_list to board so that z-index is lowest
            node_card.setManaged(false);
            node_list.getChildren().remove(node_card);
            node_board.getChildren().add(node_card);

            AnchorPane.setBottomAnchor(node_card, null);
            AnchorPane.setTopAnchor(node_card, null);
            AnchorPane.setLeftAnchor(node_card, null);
            AnchorPane.setRightAnchor(node_card, null);
            mouseAnchorX = mouseEvent.getSceneX();
            mouseAnchorY = mouseEvent.getSceneY();

        });

        node_card.setOnMouseDragged(mouseEvent -> {

            //  Calculate the button's new position
            double deltaX = mouseEvent.getSceneX() - mouseAnchorX;
            double deltaY = mouseEvent.getSceneY() - mouseAnchorY;
            double newX = node_card.getLayoutX() + deltaX;
            double newY = node_card.getLayoutY() + deltaY;

            // Update the button's position
            node_card.setLayoutX(newX);
            node_card.setLayoutY(newY);

            mouseAnchorX = mouseEvent.getSceneX();
            mouseAnchorY = mouseEvent.getSceneY();

            for (Node n : node_board.getChildren()) {
                if (n instanceof VBox) {
                    if (node_card.getBoundsInParent().intersects(n.getBoundsInParent())) {
                        ( (VBox) n ).setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                        break;
                    }
                }
            }
        });

        node_card.setOnMouseReleased(mouseEvent -> {

            // Check if the button is within the bounds of another VBox
            for (Node n : node_board.getChildren()) {
                if (n instanceof VBox) {
                    if (node_card.getBoundsInParent().intersects(n.getBoundsInParent())) {
                        Bounds bounds = node_card.localToScene(node_card.getBoundsInLocal());
                       ( (VBox) n ).setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                        // Remove the button from its current node_list and add it to the new node_list
                        node_board.getChildren().remove(node_card);
                        ((VBox) n).getChildren().add(node_card);

                        mouseAnchorX = mouseEvent.getSceneX();
                        mouseAnchorY = mouseEvent.getSceneY();

                        node_card.setManaged(true); // layout of card is managed by new list (VBox)
                        break;
                    }
                }
            }

        });
    }
}
