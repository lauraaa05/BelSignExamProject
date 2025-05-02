package gui.controllers;

import bll.OrderManager;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OperatorPreviewController {

    @FXML
    private MFXButton cameraButton;

    @FXML
    private Label orderNumberLabel;

    @FXML
    private TilePane imageTilePane;

    // To save images (maximum 5) for the preview
    private final List<ImageView> imageViews = new ArrayList<>();
    private static final int MAX_IMAGES = 5;

    // Set the order number and load its images
    public void setOrderNumber(String orderNumber) {
        orderNumberLabel.setText("Order: " + orderNumber);

        // Load existing images for this order (retrieve them from a database or file system)
        loadOrderImages(orderNumber);
    }

    // Load the images associated with the given order number
    private void loadOrderImages(String orderNumber) {
        // Example: Here you would typically fetch the images from a database or a folder structure.
        // For now, we'll simulate that by loading images from a folder specific to the order number.

        OrderManager orderManager = new OrderManager();
        List<File> orderImages = orderManager.getOrderImages(orderNumber);

        for (File imageFile : orderImages) {
            addImage(imageFile);  // Add each image to the view
        }
    }

    // Add an image to the TilePane and track it
    public void addImage(File imageFile) {
        if (imageViews.size() >= MAX_IMAGES) {
            System.out.println("Maximum of 5 images reached");
            return;
        }

        Image image = new Image(imageFile.toURI().toString());
        ImageView imageView = new ImageView(image);

        // Set image size
        imageView.setFitWidth(270);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        imageTilePane.getChildren().add(imageView);
        imageViews.add(imageView);
    }

    // For testing purposes, this method simulates the camera app where the operator can select an image from their file system
    public void openAndAddImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            addImage(selectedFile);
        }
    }

    // Handle the camera button click event (opens file chooser to select an image)
    @FXML
    private void handleCameraButtonClick(ActionEvent actionEvent) {
        Stage stage = (Stage) imageTilePane.getScene().getWindow();
        openAndAddImage(stage);
    }
}
