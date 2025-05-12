package gui.controllers;

import be.Picture;
import bll.CameraManager;
import bll.PictureManager;
import dal.PictureDAO;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.rmi.server.RemoteObject;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static utilities.AlertHelper.showAlert;

public class PictureController {
    @FXML
    private ImageView imgVPicture;
    @FXML
    private Button btnCapture, btnRetake, btnSave, btnExit;
    @FXML
    private ComboBox<String> cBoxSide;
    @FXML
    private StackPane stackPane;
    @FXML
    private Label comboPlaceholder;

    private CameraManager camera = new CameraManager();
    private PictureManager pictureManager;
    private PictureDAO pictureDAO;
    private BufferedImage capturedImage;
    private boolean isPhotoTaken = false;
    private String orderNumber;
    private OperatorPreviewController operatorPreviewController;

    public void initialize() {
        camera.initializeCamera();
        startWebcamStream();

        imgVPicture.fitWidthProperty().bind(stackPane.widthProperty());
        imgVPicture.fitHeightProperty().bind(stackPane.heightProperty());

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

        comboPlaceholder.visibleProperty().bind(
                cBoxSide.getSelectionModel().selectedItemProperty().isNull()
                        .and(cBoxSide.itemsProperty().isNotNull())
                        .and(Bindings.isNotEmpty(cBoxSide.getItems()))
        );

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
        capturedImage = null;
        startWebcamStream();
        System.out.println("Image retaken!");
    }

    private void saveImage() {
        if (capturedImage != null && isPhotoTaken) {
            String selectedSide = cBoxSide.getValue();

            if (selectedSide == null || selectedSide.isEmpty()) {
                showAlert(Alert.AlertType.WARNING,
                        "No side selected", null,
                        "Please choose a side before saving the picture");
                return;
            }

            try {
                LocalDateTime timestamp = LocalDateTime.now();
                pictureManager.savePictureToDB(capturedImage, orderNumber, timestamp, selectedSide);

                if (List.of("Front", "Back", "Left", "Right", "Top").contains(selectedSide)) {
                    cBoxSide.getItems().remove(selectedSide);
                }

                cBoxSide.getSelectionModel().clearSelection();

                Picture previewPicture = new Picture(convertToByteArray(capturedImage), timestamp, selectedSide);
                operatorPreviewController.addImage(previewPicture);

                showAlert(Alert.AlertType.INFORMATION, "Save successful", null, "Picture saved");
                isPhotoTaken = false;
                startWebcamStream();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Save failed", null, "Failed to save picture");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Save failed", null, "No picture to save");
        }
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        System.out.println("Order number received in PictureController: " + orderNumber);

        pictureDAO = new PictureDAO();

        List<String> allSides = new ArrayList<>(List.of("Front", "Back", "Left", "Right", "Top"));

        try {
            List<String> takenSides = pictureDAO.getTakenSidesForOrderNumber(orderNumber);
            allSides.removeAll(takenSides);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        allSides.add("Extra");
        cBoxSide.getItems().setAll(allSides);
        cBoxSide.getSelectionModel().clearSelection();
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
            showAlert(Alert.AlertType.ERROR, "Error", null, "Failed to load operator preview");
        }
    }

    private byte[] convertToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    public void setOperatorPreviewController(OperatorPreviewController operatorPreviewController) {
        this.operatorPreviewController = operatorPreviewController;
    }
}