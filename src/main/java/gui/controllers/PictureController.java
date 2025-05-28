package gui.controllers;

import be.Order;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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
//    @FXML
//    private ComboBox<String> cBoxSide;
    @FXML
    private StackPane stackPane;
//    @FXML
//    private Label comboPlaceholder;
    @FXML
    private GridPane gridCapturedImages;
    @FXML
    private Label lblCurrentSide;
    @FXML
    private ImageView imgPicture;

    private CameraManager camera = new CameraManager();
    private PictureManager pictureManager;
    private PictureDAO pictureDAO;
    private BufferedImage capturedImage;
    private boolean isPhotoTaken = false;
    private Order order;
    private OperatorPreviewController operatorPreviewController;
    private final List<String> sides = List.of("Front", "Back", "Right", "Left", "Top");
    private int currentSideIndex = 0;
    private int thumbnailCount = 0;

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
            if (currentSideIndex >= sides.size()) {
                showAlert(Alert.AlertType.INFORMATION, "All sides captured", null, "You have captured all required sides.");
                return;
            }
            String currentSide = sides.get(currentSideIndex);
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
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Picture save failed", null, "Picture save failed");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Save failed", null, "No picture to save");
        }
    }

    public void setOrder(Order order) {
        this.order = order;
        System.out.println("Order number received in PictureController: " + order.getOrderCode());

        pictureDAO = new PictureDAO();

        List<String> allSides = new ArrayList<>(List.of("Front", "Back", "Right", "Left", "Top"));

        try {
            List<String> takenSides = pictureDAO.getTakenSidesForOrderNumber(order.getOrderCode());
            allSides.removeAll(takenSides);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        allSides.add("Extra");
        currentSideIndex = 0;
        updateCurrentSideLabel();
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
            controller.setOrder(order);

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

    private void addThumbnail(Picture picture) {
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
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Thumbnail Error", null, "Failed to load thumbnail image.");
        }
    }

    private void updateCurrentSideLabel() {
        if(currentSideIndex < sides.size()) {
            lblCurrentSide.setText(sides.get(currentSideIndex));
        } else {
            lblCurrentSide.setText("Extra");
        }
    }
}