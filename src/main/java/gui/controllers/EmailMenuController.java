package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import utilities.EmailSender;

public class EmailMenuController {

    @FXML
    private TextField emailTxtField;
    @FXML
    private Label errorMessageLbl;

    private final EmailSender emailSender = new EmailSender();

    @FXML
    private void senPdfOnAct(ActionEvent actionEvent) {

        String email = emailTxtField.getText();

        boolean success = emailSender.sendEmail(email);

        if (success) {
            errorMessageLbl.setText("Email sent successfully");
            errorMessageLbl.setStyle("-fx-text-fill: green;");

            // to add short delay 3 seconds
            javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
            delay.setOnFinished(event -> emailTxtField.getScene().getWindow().hide());
            delay.play();

        } else {
            errorMessageLbl.setText("Please, enter a valid email address");
            errorMessageLbl.setStyle("-fx-text-fill: red;");
        }

    }
}