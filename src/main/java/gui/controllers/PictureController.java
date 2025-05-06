package gui.controllers;

import bll.CameraManager;
import bll.PictureManager;
import dal.PictureDAO;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class PictureController {
    @FXML
    private ImageView imgVPicture;
    @FXML
    private Button btnCapture, btnRetake, btnSave, btnExit;

    private CameraManager camera = new CameraManager();
    private PictureManager pictureManager;
    private BufferedImage capturedImage;
    private boolean isPhotoTaken = false;
    private String orderNumber;

    public void initialize() {
        camera.initializeCamera();
        startWebcamStream();

        btnCapture.setOnAction(e -> captureImage());
        btnRetake.setOnAction(e -> retakeImage());
        btnSave.setOnAction(e -> saveImage());
        btnExit.setOnAction(e -> {
            try {
                exit();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        pictureManager = new PictureManager(new PictureDAO());
    }

    private void startWebcamStream() {
        Thread stream = new Thread(() -> {
            while(!isPhotoTaken) {
                BufferedImage image = camera.takePicture();
                if(image != null) {
                    Platform.runLater(() -> imgVPicture.setImage(SwingFXUtils.toFXImage(image, null)));
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

    private void captureImage() {
        capturedImage = camera.takePicture();
        isPhotoTaken = true;
        imgVPicture.setImage(SwingFXUtils.toFXImage(capturedImage, null));
        System.out.println("Image captured!");
    }

    private void retakeImage() {
        isPhotoTaken = false;
        startWebcamStream();
        System.out.println("Image retaken!");
    }

    private void saveImage() {
        if (capturedImage != null) {
            try {
                pictureManager.savePictureToDB(capturedImage, orderNumber);
                showAlert(Alert.AlertType.INFORMATION, "Picture saved");
                isPhotoTaken = false;
                startWebcamStream();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Failed to save picture");
            }
        } else  {
            showAlert(Alert.AlertType.ERROR, "No picture to save");
        }
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        System.out.println("Order number received in PictureController: " + orderNumber);
    }

    private void switchToPreviewScene(Stage currentStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/OperatorPreview.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        currentStage.setTitle("Operator Preview");
        currentStage.setScene(scene);
        currentStage.show();
        System.out.println("Switching to OperatorPreview scene");
    }

    private void exit() throws IOException {
        try {
            camera.closeCamera();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/OperatorPreview.fxml"));
            Parent root = fxmlLoader.load();

            OperatorPreviewController controller = fxmlLoader.getController();
            controller.setOrderNumber(orderNumber);

            Stage stage = (Stage) btnExit.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to load operator preview");
        }
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Save Picture");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
