package dk.easv.belsignexamproject;

import be.Operator;
import bll.OperatorManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class OperatorLogInController {

    @FXML
    private Label welcomeText;

    @FXML
    private TextField barcodeTextField;

    private final OperatorManager operatorManager = new OperatorManager(); // Declare it at the top

    @FXML
    public void initialize() {
        // Attach a listener to detect when ENTER is pressed
        barcodeTextField.setOnKeyPressed(this::handleBarcodeScan);
    }

    private void handleBarcodeScan(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String scannedCode = barcodeTextField.getText().trim();

            if (!scannedCode.isEmpty()) {
                loginOperator(scannedCode);
            } else {
                welcomeText.setText("Please scan a valid barcode.");
            }
        }
    }

    private void loginOperator(String scannedCode) {
        try {
            int operatorId = Integer.parseInt(scannedCode);

            Operator operator = operatorManager.getOperatorById(operatorId);

            if (operator != null) {
                welcomeText.setText("Welcome " + operator.getName() + "! Role: " + operator.getRole());
                // TODO: Navigate to Dashboard Scene
            } else {
                welcomeText.setText("Operator not found.");
            }

        } catch (NumberFormatException e) {
            welcomeText.setText("Invalid barcode format. Please try again.");
        }
    }
}
