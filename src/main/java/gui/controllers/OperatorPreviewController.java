package gui.controllers;

import be.Order;
import be.Picture;
import bll.PictureManager;
import dal.OrderStatusDAO;
import dal.PictureDAO;
import dk.easv.belsignexamproject.OperatorLogInApp;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.mfxlocalization.Language;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import utilities.AlertHelper;
import utilities.SceneNavigator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static utilities.AlertHelper.showAlert;
import static utilities.AlertHelper.showConfirmationAlert;

public class OperatorPreviewController {

    @FXML
    private MFXButton cameraButton;

    @FXML
    private Button signOutButton, btnExit;

    @FXML
    private Label orderNumberLabel;

    @FXML
    private FlowPane imageFlowPane;

    @FXML
    private Button doneButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button fullscreenButton;

    //These are for swiping gesture
    @FXML
    private HBox swipeContainer;
    @FXML
    private Button swipeButton;

    private final List<ImageView> imageViews = new ArrayList<>();
    private static final int MIN_IMAGES = 5;
    private Order currentOrder;
    private Picture selectedPicture;
    private VBox selectedVBox;
    private final SceneNavigator sceneNavigator = new SceneNavigator();
    private PictureManager pictureManager;

    public void initialize() {
        btnExit.setOnAction(e -> {
            try {
                exit();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        swipeButton.setOnMousePressed(this::onMousePressed);
        swipeButton.setOnMouseDragged(this::onMouseDragged);
        swipeButton.setOnMouseReleased(this::onMouseReleased);

        PictureDAO pictureDAO = new PictureDAO();
        pictureManager = new PictureManager(pictureDAO);
    }

    public void setOrder(Order order) {
        this.currentOrder = order;
        orderNumberLabel.setText("Order: " + order);
        loadOrderImages(order.getOrderCode());
    }

    private void loadOrderImages(String orderNumber) {
        imageFlowPane.getChildren().clear();
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
        Image image = new Image(new ByteArrayInputStream(picture.getImageBytes()));
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(270);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        imageView.setEffect(new DropShadow(10, Color.rgb(0,0,0,0.5)));

        Label label = new Label(picture.getSide() + " - " +
                picture.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
        label.getStyleClass().add("preview-label");

        VBox vBox = new VBox(5, imageView, label);
        vBox.setAlignment(Pos.CENTER);
        label.getStyleClass().add("preview-box");

        vBox.setOnMouseClicked(event -> {
            selectedPicture = picture;
            selectedVBox = vBox;

            imageFlowPane.getChildren().forEach(node -> node.setStyle(""));

            vBox.setStyle("-fx-border-color: #4CAF50; -fx-border-width: 3;");

            deleteButton.setDisable(false);
            fullscreenButton.setDisable(false);
        });
        imageFlowPane.getChildren().add(vBox);
    }

    // Méthod change the scene
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
        pictureController.setOrder(currentOrder);
        pictureController.setOperatorPreviewController(this);

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
        if (currentOrder == null) {
            showAlert(Alert.AlertType.WARNING, "No order selected", null, "Please select an order");
            return;
        }

        try {
            PictureDAO pictureDAO = new PictureDAO();

            List<String> takenSides = pictureDAO.getTakenSidesForOrderNumber(currentOrder.getOrderCode());

            if (takenSides.containsAll(List.of("Front", "Back", "Left", "Right", "Top"))) {
                System.out.println("All sides have been photographed");
            } else {
                List<String> missingSides = List.of("Front", "Back", "Left", "Right", "Top")
                        .stream().filter(side -> !takenSides.contains(side))
                        .toList();

                AlertHelper.showAlert(
                        Alert.AlertType.WARNING,
                        "Missing sides",
                        "Not all required sides have been photographed",
                        "Missing: " + String.join(", ", missingSides)
                );
                return;
            }

            int imageCount = pictureDAO.countImagesForOrderNumber(currentOrder.getOrderCode());

            if (imageCount < MIN_IMAGES) {
                AlertHelper.showAlert(
                        Alert.AlertType.WARNING,
                        "Too few images",
                        "Order has only " + imageCount + " image(s).",
                        "You must upload at least " + MIN_IMAGES + " images to mark this order as done.");
                return;
            }

            boolean confirmed = AlertHelper.showConfirmationAlert(
                    "Confirm completion",
                    "Mark order as done?",
                    "Are you sure you want to mark order " + currentOrder + " as done?");
            if (!confirmed) {
                return;
            }

            OrderStatusDAO orderStatusDAO = new OrderStatusDAO();

            String codeOnly = currentOrder.getOrderCode();

            System.out.println("Extracted code: " + codeOnly);

            boolean updated = orderStatusDAO.updateOrderStatusAndRole(codeOnly, "operator", "qcu", "to_approve");

            if (!updated) {
                showAlert(Alert.AlertType.ERROR, "Update failed", null, "No matching order found for operator role.");
                return;
            }

            showAlert(Alert.AlertType.INFORMATION, "Order updated", null,
                    "Order " + currentOrder + " marked as done.");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/OperatorMain.fxml"));
            Parent root = fxmlLoader.load();

            OperatorMainController operatorMainController = fxmlLoader.getController();
            operatorMainController.refreshLists();

            Stage currentStage = (Stage) doneButton.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();

            showAlert(Alert.AlertType.ERROR, "Error", "Could not update order status", e.getMessage());
        }
    }

    //These are for swipin gesture
    private double startX;
    private final double TRIGGER_DISTANCE = 150; // Distance to consider it a full swipe

    private void onMousePressed(MouseEvent event) {
        startX = event.getSceneX();
    }

    private void onMouseDragged(MouseEvent event) {
        double offsetX = event.getSceneX() - startX;
        if (offsetX >= 0 && offsetX <= (swipeContainer.getWidth() - swipeButton.getWidth() - 20)) {
            swipeButton.setTranslateX(offsetX);
        }
    }

    private void onMouseReleased(MouseEvent event) {
        double offsetX = event.getSceneX() - startX;

        if (offsetX > TRIGGER_DISTANCE) {
            // Trigger confirmation or animation to end
            animateToPosition(swipeButton, swipeContainer.getWidth() - swipeButton.getWidth() - 20, true);
        } else {
            // Snap back
            animateToPosition(swipeButton, 0, false);
        }
    }

    private void markAsDone() {
        markAsDone(null); // Call the existing method with null
    }

    private void animateToPosition(Button button, double position, boolean isConfirmed) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(250), button);
        transition.setToX(position);

        transition.setOnFinished(event -> {
            if (isConfirmed) {
                System.out.println("Swipe Confirmed!");
                // Optionally call a method: handleSwipeConfirm();
                javafx.application.Platform.runLater(this::markAsDone);
            }
        });
        transition.play();
    }

    @FXML
    private void handleFullscreenButtonClick() {
        if (selectedPicture == null) return;

        Image image = new Image(new ByteArrayInputStream(selectedPicture.getImageBytes()));

        Stage imageStage = new Stage();
        sceneNavigator.switchToWithData(imageStage, "ImageViewer.fxml", (ImageViewerController controller) -> {
            controller.setImage(image);
        });
    }

    @FXML
    private void handleDeleteButtonClick() {
        if (selectedPicture == null) return;

        try {
            System.out.println("Deleting image with ID: " + selectedPicture.getImageId());
            pictureManager.deletePictureFromDB(selectedPicture.getImageId());
            imageFlowPane.getChildren().remove(selectedVBox);
            selectedPicture = null;
            selectedVBox = null;
            deleteButton.setDisable(true);
            fullscreenButton.setDisable(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}