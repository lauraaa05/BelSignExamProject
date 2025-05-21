package gui.controllers;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class SwipeDraftController {

    @FXML
    private HBox swipeContainer;

    @FXML
    private Button swipeButton;

    private double startX;
    private final double TRIGGER_DISTANCE = 150; // Distance to consider it a full swipe

    @FXML
    public void initialize() {
        swipeButton.setOnMousePressed(this::onMousePressed);
        swipeButton.setOnMouseDragged(this::onMouseDragged);
        swipeButton.setOnMouseReleased(this::onMouseReleased);
    }

    private void onMousePressed(MouseEvent event) {
        startX = event.getSceneX();
    }

    private void onMouseDragged(MouseEvent event) {
        double offsetX = event.getSceneX() - startX;
        if (offsetX >= 0 && offsetX <= (swipeContainer.getWidth() - swipeButton.getWidth() - 20)) {
            swipeButton.setTranslateX(offsetX);
        }
    }

    private void onMouseReleased(MouseEvent event) {
        double offsetX = event.getSceneX() - startX;

        if (offsetX > TRIGGER_DISTANCE) {
            // Trigger confirmation or animation to end
            animateToPosition(swipeButton, swipeContainer.getWidth() - swipeButton.getWidth() - 20, true);
        } else {
            // Snap back
            animateToPosition(swipeButton, 0, false);
        }
    }

    private void animateToPosition(Button button, double position, boolean isConfirmed) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(250), button);
        transition.setToX(position);
        transition.setOnFinished(e -> {
            if (isConfirmed) {
                System.out.println("Swipe Confirmed!");
                // Optionally call a method: handleSwipeConfirm();
            }
        });
        transition.play();
    }
}
