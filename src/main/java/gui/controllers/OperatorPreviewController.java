package gui.controllers;

import be.Picture;
import bll.OrderManager;
import dal.OrderStatusDAO;
import dal.PictureDAO;
import dk.easv.belsignexamproject.OperatorLogInApp;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OperatorPreviewController {

    @FXML
    private MFXButton cameraButton;

    @FXML
    private Button signOutButton, btnExit;

    @FXML
    private Label orderNumberLabel;

    @FXML
    private TilePane imageTilePane;

    @FXML
    private Button doneButton;

    private final List<ImageView> imageViews = new ArrayList<>();
    private static final int MAX_IMAGES = 5;
    private String currentOrderNumber;

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
        this.currentOrderNumber = orderNumber;
        orderNumberLabel.setText("Order: " + orderNumber);
        loadOrderImages(orderNumber);
    }

    private void loadOrderImages(String orderNumber) {
        imageTilePane.getChildren().clear();
        imageViews.clear();

        PictureDAO pictureDAO = new PictureDAO();
        try {
            List<Picture> pictures = pictureDAO.getPicturesByOrderNumber(orderNumber);
            for (Picture picture : pictures) {
                addImage(picture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addImage(Picture picture) {
        Image image = new Image(new ByteArrayInputStream(picture.getImage()));
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(270);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        imageView.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ImageViewer.fxml"));
                Parent root = loader.load();

                ImageViewerController controller = loader.getController();
                controller.setImage(image);

                Stage stage = new Stage();
                stage.setTitle("Image preview");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        imageTilePane.getChildren().add(imageView);
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
    private void handleCameraButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/PictureView.fxml"));
        Parent root = fxmlLoader.load();

        PictureController pictureController = fxmlLoader.getController();
        pictureController.setOrderNumber(currentOrderNumber);

        Stage currentStage = (Stage) cameraButton.getScene().getWindow();
        currentStage.setScene(new Scene(root));
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

    @FXML
    private void markAsDone(ActionEvent actionEvent) {
        if (currentOrderNumber == null ||  currentOrderNumber.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No order selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select an order");
            alert.showAndWait();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm completion");
        confirmation.setHeaderText("Mark order as done?");
        confirmation.setContentText("Are you sure you want to mark order " +  currentOrderNumber + " as done?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                OrderStatusDAO orderStatusDAO =  new OrderStatusDAO();

                String codeOnly = currentOrderNumber.substring(currentOrderNumber.lastIndexOf('-') + 1);

                System.out.println("Extracted code: " + codeOnly);

                orderStatusDAO.updateOrderStatus(codeOnly, "operator", "done");

                Alert succes =  new Alert(Alert.AlertType.INFORMATION);
                succes.setTitle("Order updated");
                succes.setHeaderText(null);
                succes.setContentText("Order " + currentOrderNumber + " marked as done.");
                succes.showAndWait();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/OperatorMain.fxml"));
                Parent root = fxmlLoader.load();

                OperatorMainController operatorMainController = fxmlLoader.getController();
                operatorMainController.refreshLists();

                Stage currentStage = (Stage) doneButton.getScene().getWindow();
                currentStage.setScene(new Scene(root));
            } catch (Exception e) {
                e.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("Could not update order status.");
                error.setContentText(e.getMessage());
                error.showAndWait();
            }
        }
    }
}
