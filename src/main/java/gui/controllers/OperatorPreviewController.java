package gui.controllers;

import bll.OrderManager;
import dk.easv.belsignexamproject.OperatorLogInApp;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OperatorPreviewController {

    @FXML
    private MFXButton cameraButton;

    @FXML
    private Button signOutButton, btnExit; // Asegúrate de que esté en tu FXML

    @FXML
    private Label orderNumberLabel;

    @FXML
    private TilePane imageTilePane;

    private final List<ImageView> imageViews = new ArrayList<>();
    private static final int MAX_IMAGES = 5;

    public void initialize() {
        btnExit.setOnAction(e -> {
            try {
                exit();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void setOrderNumber(String orderNumber) {
        orderNumberLabel.setText("Order: " + orderNumber);
        loadOrderImages(orderNumber);
    }

    private void loadOrderImages(String orderNumber) {
        OrderManager orderManager = new OrderManager();
        List<File> orderImages = orderManager.getOrderImages(orderNumber);

        for (File imageFile : orderImages) {
            addImage(imageFile);
        }
    }

    public void addImage(File imageFile) {
        if (imageViews.size() >= MAX_IMAGES) {
            System.out.println("Maximum of 5 images reached");
            return;
        }

        Image image = new Image(imageFile.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(270);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        imageTilePane.getChildren().add(imageView);
        imageViews.add(imageView);
    }

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

    // Método reutilizable para cambiar a la escena de login
    private void switchToLoginScene(Stage currentStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(OperatorLogInApp.class.getResource("/view/OperatorLogIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        currentStage.setTitle("Operator Login");
        currentStage.setScene(scene);
        currentStage.show();
    }

    // Method "Sign Out"//
    @FXML
    private void handleSignOutButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) signOutButton.getScene().getWindow();
        try {
            switchToLoginScene(currentStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCameraButtonClick(ActionEvent actionEvent) {
        Stage stage = (Stage) imageTilePane.getScene().getWindow();
        openAndAddImage(stage);
    }

    private void switchToMainScene(Stage currentStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/OperatorMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        currentStage.setTitle("Operator Main");
        currentStage.setScene(scene);
        currentStage.show();
        System.out.println("Switching to OperatorMain scene");
    }

    private void exit() throws IOException {
        Stage currentStage = (Stage) btnExit.getScene().getWindow();
        switchToMainScene(currentStage);
    }
}
