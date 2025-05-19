package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.SwipeEvent;

public class SwipeDraftController {

    @FXML
    private void handleSwipeRight(SwipeEvent event) {
        System.out.println("Swipe Right detected!");
    }
}
