package dk.easv.belsignexamproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class OperatorLogInController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}