package gui.controllers;

import be.Operator;
import bll.Camera;
import bll.OperatorManager;
import bll.QRCodeService;
import com.google.zxing.qrcode.QRCodeReader;
import dal.OperatorDAO;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.awt.image.BufferedImage;


public class OperatorLogInController {

    @FXML
    private Label welcomeText;

    @FXML
    private ImageView imgQRPicture;

    @FXML
    private TextField barcodeTextField;

    private Camera camera = new Camera();
    private boolean isPhotoTaken = false;

    private final OperatorDAO operatorDAO = new OperatorDAO();

    @FXML
    public void initialize() {
        // Attach a listener to detect when ENTER is pressed
        camera.initializeCamera();
        startWebcamStream();
        //barcodeTextField.setOnKeyPressed(this::handleBarcodeScan);
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

    private void startWebcamStream() {
        Thread stream = new Thread(() -> {
            QRCodeService qrCodeService = new QRCodeService();

            while(!isPhotoTaken) {
                BufferedImage image = camera.takePicture();
                if(image != null) {
                    Platform.runLater(() -> imgQRPicture.setImage(SwingFXUtils.toFXImage(image, null)));

                    try {
                        String content = qrCodeService.readQRCodeFromImage(image);

                        if (content != null &&  !content.isEmpty()) {
                            isPhotoTaken = true;
                            Platform.runLater(() -> loginOperator(content));
                        }
                    }  catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        stream.setDaemon(true);
        stream.start();
    }

    private void loginOperator(String scannedCode) {
        try {
            int operatorId = Integer.parseInt(scannedCode);
            Operator operator = operatorDAO.getOperatorById(operatorId);

            if (operator != null && operator.getRole().equalsIgnoreCase("Operator")) {
                welcomeText.setText("Welcome " + operator.getName() + "! Role: " + operator.getRole());
                // TODO: Navigate to Dashboard Scene
            } else {
                welcomeText.setText("Operator not found.");
                isPhotoTaken = false;
            }

        } catch (NumberFormatException e) {
            welcomeText.setText("Invalid barcode format. Please try again.");
            isPhotoTaken = false;
        }
        System.out.println("Scanned QR content: " + scannedCode);
    }
}
