package gui.controllers;

import be.Operator;
import be.QRCodeInfo;
import bll.CameraManager;
import bll.OperatorManager;
import bll.QRCodeManager;
import dk.easv.belsignexamproject.MainLogin;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utilities.LoggedInUser;
import utilities.SceneNavigator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;


public class OperatorLogInController {

    @FXML
    private Label welcomeText;

    @FXML
    private ImageView imgQRPicture;

    private CameraManager camera = new CameraManager();
    private boolean isPhotoTaken = false;

    private final SceneNavigator sceneNavigator = new SceneNavigator();
    private final OperatorManager  operatorManager = new OperatorManager();
    private final QRCodeManager qrCodeManager = new QRCodeManager();

    @FXML
    public void initialize() {
        camera.initializeCamera();
        startWebcamStream();
    }

    private void startWebcamStream() {
        Thread stream = new Thread(() -> {
            QRCodeManager qrCodeManager = new QRCodeManager();

            while (!isPhotoTaken) {
                BufferedImage image = camera.takePicture();
                if (image != null) {
                    Platform.runLater(() -> imgQRPicture.setImage(SwingFXUtils.toFXImage(image, null)));

                    try {
                        String content = qrCodeManager.readQRCodeFromImage(image);

                        if (content != null && !content.isEmpty()) {
                            isPhotoTaken = true;
                            Platform.runLater(() -> loginOperator(content));
                        }
                    } catch (Exception e) {
                    }
                }

                try {
                    Thread.sleep(33);
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
            QRCodeInfo qrInfo = qrCodeManager.getQRCodeByString(scannedCode);

            if (qrInfo != null) {
                int userId = qrInfo.getUserId();
                Operator operator = operatorManager.getOperatorByUserId(userId);

                if (operator != null && operator.getRole().equalsIgnoreCase("Operator")) {
                    LoggedInUser.setUser(operator);

                    FXMLLoader fxmlLoader = new FXMLLoader(MainLogin.class.getResource("/view/OperatorMain.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());

                    Platform.runLater(() -> {
                        Stage currentStage = (Stage) welcomeText.getScene().getWindow();
                        if (currentStage != null) {
                            currentStage.setTitle("Operator Main");
                            currentStage.setScene(scene);
                            currentStage.show();
                            camera.closeCamera();
                        }
                    });
                } else {
                    welcomeText.setText("Operator not found.");
                    isPhotoTaken = false;
                }
            } else {
                welcomeText.setText("QR code not found.");
                isPhotoTaken = false;
            }
        } catch (NumberFormatException | IOException | SQLException e) {
            welcomeText.setText("Invalid barcode or loading error.");
            isPhotoTaken = false;
            e.printStackTrace();
        }
        System.out.println("Scanned QR content: " + scannedCode);
    }

    @FXML
    private void openUsernamePasswordLoginAct(ActionEvent actionEvent) {
        sceneNavigator.switchTo(actionEvent, "MainLogin.fxml");
    }
}