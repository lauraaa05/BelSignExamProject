package gui.controllers;

import be.Order;
import be.Picture;
import be.QualityControl;
import be.Report;
import bll.OrderStatusManager;
import bll.PictureManager;
import exceptions.BLLException;
import exceptions.DALException;
import gui.model.ReportModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utilities.SceneNavigator;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class QCUNewReportController {

    @FXML
    private Label signatureLabel;

    @FXML
    private TextArea commentsTextArea;

    @FXML
    private Button submitButton;

    @FXML
    private Button rejectButton;

    @FXML
    private Label generalCommentsLabel;

    @FXML
    private TilePane photoTile;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label orderNumberLabel;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final ReportModel reportModel = new ReportModel();

    private final SceneNavigator sceneNavigator = new SceneNavigator();

    private final OrderStatusManager orderStatusManager = new OrderStatusManager();

    private final PictureManager pictureManager = new PictureManager();

    private Order currentOrder;

    private QualityControl currentUser;

    @FXML
    public void initialize() {
        submitButton.setOnAction(this::handleSubmitReport);

        rejectButton.setOnAction(e -> handleReject());
    }

    public void setOrder(Order order) {
        this.currentOrder = order;
        orderNumberLabel.setText("ORDER NUMBER: " + order);

        loadPictures(order.toString());
        loadLatestComment(order.getOrderCode());

        try {
            String status = new OrderStatusManager().getStatusForOrder(order.getOrderCode());
            if ("done".equalsIgnoreCase(status)) {
                submitButton.setVisible(false);
                commentsTextArea.setEditable(false);
            }
        } catch (BLLException e) {
            e.printStackTrace();
            showError("Failed to retrieve order status: " + e.getMessage());
        }
    }

    private VBox createImageCard(Picture picture) {
        VBox vBox = new VBox(10);
        vBox.setStyle("-fx-background-color: #fff; -fx-padding: 10px; -fx-border-color: #d1d5db; -fx-border-radius: 5px; -fx-background-radius: 8px;");
        vBox.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(520);
        imageView.setFitWidth(420);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(new ByteArrayInputStream(picture.getImage())));

        String sideText = (picture.getSide() == null || picture.getSide().isEmpty()) ? "Unknown" : picture.getSide();
        Label sideLabel = new Label("Side: " + sideText);
        Label dateLabel = new Label("Date: " + picture.getTimestamp().format(formatter));

        vBox.getChildren().addAll(imageView, sideLabel, dateLabel);
        return vBox;
    }

    private void loadPictures(String orderNumber) {
        try {
            List<Picture> pictures = pictureManager.getPicturesByOrderNumberRaw(orderNumber);
            photoTile.getChildren().clear();

            for (Picture picture : pictures) {
                VBox imageCard = createImageCard(picture);
                photoTile.getChildren().add(imageCard);
            }

            if (pictures.isEmpty()) {
                System.out.println("No pictures found for order number: " + orderNumber);
            }
        } catch (DALException e) {
            System.err.println("Database error while loading pictures: " + e.getMessage());
        }
    }

    private void loadLatestComment(String orderNumber) {
        try {
            String latestComment = reportModel.getLatestCommentByOrderNumber(orderNumber);
            generalCommentsLabel.setText(latestComment != null ? latestComment : "No comments yet.");
        } catch (Exception e) {
            generalCommentsLabel.setText("Failed to fetch comment: " + e.getMessage());
        }
    }

    private String extractOrderNumber() {
        return orderNumberLabel.getText().replace("ORDER NUMBER: ", "").trim();
    }

    @FXML
    private void goBackBtnAction(ActionEvent actionEvent) {
        sceneNavigator.switchTo(actionEvent, "QCUMain.fxml");
    }


    @FXML
    private void handleReject() {
        try {
            String commentText = commentsTextArea.getText();
            String fullOrderNumber = extractOrderNumber();
            String orderCode = fullOrderNumber.substring(fullOrderNumber.lastIndexOf("-") + 1);

            // Save rejected comment
            if (commentText != null && !commentText.isEmpty()) {
                Report report = new Report(4, "[REJECTED] " + commentText, fullOrderNumber, LocalDateTime.now(), orderCode);
                reportModel.insertReport(report);
            }

            // Update order status to 'rejected'
            boolean updated = new OrderStatusManager().updateOrderStatusAndRole(orderCode, 2, 1, 1029);

            if (updated) {
                System.out.println("Order marked as rejected and returned to operator.");
            } else {
                System.out.println("Failed to update order status to rejected.");
            }

            // UI updates
            rejectButton.setDisable(true);
            rejectButton.setVisible(false);
            commentsTextArea.setEditable(false);

            // Navigate away
            sceneNavigator.switchTo("/view/QCUMain.fxml");
            Stage stage = (Stage) rejectButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCurrentUser(QualityControl user) {
        this.currentUser = user;
        signatureLabel.setText(user.getFirstName() + " " + user.getLastName());
    }


    @FXML
    private void handleSubmitReport(ActionEvent actionEvent) {
        if (currentOrder == null || currentUser == null) {
            System.out.println("Missing order or user");
            return;
        }

        String commentText = commentsTextArea.getText();
        if (commentText == null || commentText.isEmpty()) {
            System.out.println("Comment is empty");
            return;
        }

        try {
            String fullOrderNumber = extractOrderNumber();
            String orderCode = currentOrder.getOrderCode();

            //To save comment to comment field
            Report report = new Report(currentUser.getId(), commentText, fullOrderNumber, LocalDateTime.now(), orderCode);
            reportModel.insertReport(report);

            reportModel.saveDoneReport(orderCode, currentUser.getId());

            // to update status
            boolean updated = new OrderStatusManager().updateOrderStatus(orderCode, 2, 1028);
            if (updated) {
                System.out.println("Order marked as done");
            }

            //to clear gui
            commentsTextArea.clear();
            commentsTextArea.setEditable(false);
            submitButton.setVisible(false);
            rejectButton.setVisible(false);

            //Navigate back
            sceneNavigator.switchTo("/view/QCUMain.fxml");

            //Confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Report Submitted");
            alert.setHeaderText(null);
            alert.setContentText("Report has been submitted successfully.");
            alert.showAndWait();
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Submission Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to submit report.");
            alert.showAndWait();

        }
    }
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}