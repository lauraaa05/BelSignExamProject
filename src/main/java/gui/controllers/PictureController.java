package gui.controllers;

import be.Order;
import be.Picture;
import bll.CameraManager;
import bll.PictureManager;
import exceptions.BLLException;
import exceptions.ImageProcessingException;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private StackPane stackPane;
    @FXML
    private GridPane gridCapturedImages;
    @FXML
    private Label lblCurrentSide;

    private final CameraManager camera = new CameraManager();
    private final PictureManager pictureManager = new  PictureManager();
    private BufferedImage capturedImage;
    private boolean isPhotoTaken = false;
    private Order order;
    private OperatorPreviewController operatorPreviewController;
    private final List<String> sides = List.of("Front", "Back", "Right", "Left", "Top");
    private List<String> sidesToTake;
    private int currentSideIndex = 0;
    private int thumbnailCount = 0;
    private int extraPictureCount = 0;

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
            String currentSide;
            if (currentSideIndex < sidesToTake.size()) {
                currentSide = sidesToTake.get(currentSideIndex);
            } else {
                extraPictureCount++;
                currentSide = "Extra " + extraPictureCount;
            }

            LocalDateTime timestamp = LocalDateTime.now();

            try {
                pictureManager.savePictureToDB(capturedImage, order.getFormattedOrderText(), timestamp, currentSide);

                byte[] imageBytes = convertToByteArray(capturedImage);
                Picture previewPicture = new Picture(imageBytes, timestamp, currentSide);
                operatorPreviewController.addImage(previewPicture);
                addThumbnail(previewPicture);

                currentSideIndex++;
                updateCurrentSideLabel();

                isPhotoTaken = false;
                startWebcamStream();
            } catch (BLLException | ImageProcessingException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Picture save failed", null, e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Save failed", null, "No picture to save");
        }
    }

    public void setOrder(Order order) {
        this.order = order;
        System.out.println("Order number received in PictureController: " + order.getOrderCode());

        List<String> allSides = new ArrayList<>(List.of("Front", "Back", "Right", "Left", "Top"));

        try {
            List<String> takenSides = pictureManager.getTakenSidesForOrderNumber(order.getFormattedOrderText());
            List<String> formattedTaken = takenSides.stream()
                    .map(this::formatSide)
                    .toList();

            allSides.removeIf(side -> formattedTaken.contains(formatSide(side)));

            extraPictureCount = (int) formattedTaken.stream()
                    .filter(side -> side.startsWith("Extra"))
                    .count();
        } catch (BLLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", null, "Failed to load sides from database.");
        }
        sidesToTake = allSides;
        currentSideIndex = 0;
        updateCurrentSideLabel();
    }

    private void exit() throws IOException {
        try {
            camera.closeCamera();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/OperatorPreview.fxml"));
            Parent root = fxmlLoader.load();

            OperatorPreviewController controller = fxmlLoader.getController();
            controller.setOrder(order);

            Stage stage = (Stage) btnExit.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", null, "Failed to load operator preview");
        }
    }

    private byte[] convertToByteArray(BufferedImage image) throws ImageProcessingException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new ImageProcessingException("Failed to convert image to byte array", e);
        }
    }

    public void setOperatorPreviewController(OperatorPreviewController operatorPreviewController) {
        this.operatorPreviewController = operatorPreviewController;
    }

    private void addThumbnail(Picture picture) throws ImageProcessingException {
        try {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(picture.getImageBytes()));
            if (bufferedImage == null) {
                System.err.println("Failed to load image from picture");
                return;
            }

            ImageView thumbnail = new ImageView(SwingFXUtils.toFXImage(bufferedImage, null));
            thumbnail.setFitWidth(100);
            thumbnail.setPreserveRatio(true);

            Label label = new Label(picture.getSide());
            VBox imageBox = new VBox(5, thumbnail, label);
            imageBox.setStyle("-fx-alignment: center; -fx-padding: 5; -fx-border-color: lightgray; -fx-border-width: 1;");

            int col = thumbnailCount % 2;
            int row = thumbnailCount / 2;

            Platform.runLater(() -> gridCapturedImages.add(imageBox, col, row));
            thumbnailCount++;

        } catch (IOException e) {
            throw new ImageProcessingException("Failed to load thumbnail image", e);
        }
    }

    private void updateCurrentSideLabel() {
        if(currentSideIndex < sidesToTake.size()) {
            lblCurrentSide.setText(sidesToTake.get(currentSideIndex));
        } else {
            lblCurrentSide.setText("Extra " + (extraPictureCount + 1));
        }
    }

    private String formatSide(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}