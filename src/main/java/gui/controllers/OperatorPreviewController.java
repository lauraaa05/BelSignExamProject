package gui.controllers;

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

    // I'm gonna use this arrayList to save maximum 5 image next to each other in the OperatorPriview
    private final List<ImageView> imageViews = new ArrayList<>();
    private static final int MAX_IMAGES = 5;

    // You need to call this when operator takes a photo
    public void addImage(File imageFile) {
        if (imageViews.size() >= MAX_IMAGES) {
            System.out.println("Maximum of 5 images reached");
            return;
        }

        Image image = new Image(imageFile.toURI().toString());
        ImageView imageView = new ImageView(image);

        // To set size
        imageView.setFitWidth(270);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        imageTilePane.getChildren().add(imageView);
        imageViews.add(imageView);
    }

    //This is for testing purposes later change this with camera application which operator take photos of a product
    //and saves them

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


    //Change and connect this with opertor camera app later!
    @FXML
    private void handleCameraButtonClick(ActionEvent actionEvent) {
        Stage stage = (Stage) imageTilePane.getScene().getWindow();
        openAndAddImage(stage);
    }
}
