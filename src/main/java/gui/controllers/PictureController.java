package gui.controllers;

import bll.CameraManager;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PictureController {
    @FXML
    private ImageView imgVPicture;
    @FXML
    private Button btnCapture, btnRetake, btnSave, btnExit;

    private CameraManager camera = new CameraManager();
    private BufferedImage capturedImage;
    private boolean isPhotoTaken = false;

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
        if(capturedImage != null) {
            try {
                ImageIO.write(capturedImage, "PNG", new File("snapshot.png"));
                System.out.println("Picture Saved!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        Stage currentStage = (Stage) btnExit.getScene().getWindow();
        camera.closeCamera();
        switchToPreviewScene(currentStage);
    }
}
